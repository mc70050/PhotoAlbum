package com.project.michael.photoalbum;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.michael.photoalbum.adapter.AlbumAdapter;
import com.project.michael.photoalbum.database.DBHelper;
import com.project.michael.photoalbum.database.PhotoDBHelper;
import com.project.michael.photoalbum.model.Album;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.project.michael.photoalbum.model.Photo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_SEARCH = 0;
    private final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 3;

    private AlbumAdapter albumAdapter;
    private DBHelper albumDB;
    private PhotoDBHelper photoDB;
    private FusedLocationProviderClient mFusedLocationClient;

    private double latitude = 0;
    private double longitude = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        albumDB = new DBHelper(this);
        photoDB = new PhotoDBHelper(this);

        if (albumDB.numberOfAlbums() == 0) {
            albumDB.addAlbum("New");
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();
        // Initiate and set Recycler View
        RecyclerView albumRecyclerView = findViewById(R.id.albumList);
        albumRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));

        // Initiate adapter for list
        List<Album> albumList = albumDB.getAllAlbums();
        albumAdapter = new AlbumAdapter(albumList, getApplicationContext());
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        albumRecyclerView.setLayoutManager(verticalLayoutManager);
        albumRecyclerView.setAdapter(albumAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(this,
                            "Permission Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Log.d("TakePicture", "Success");
        }
        if (requestCode == REQUEST_SEARCH && resultCode == RESULT_OK) {
            Log.d("Search", "Success");
            Bundle bun = new Bundle();
            bun.putString("Album", null);
            bun.putString("STARTDATE", data.getStringExtra("STARTDATE"));
            bun.putString("ENDDATE", data.getStringExtra("ENDDATE"));
            bun.putDoubleArray("STARTLOC", data.getDoubleArrayExtra("STARTLOC"));
            bun.putDoubleArray("ENDLOC", data.getDoubleArrayExtra("ENDLOC"));
            Intent intent = new Intent(this, PhotoActivity.class);
            intent.putExtras(bun);
            startActivity(intent);
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            } else {
                                Log.d("Location Error: ", "Something wrong inside the getLocation()");
                            }
                        }
                    });
        }
    }

    public void goToSearch(View v) {
        startActivityForResult(new Intent(MainActivity.this, SearchActivity.class), REQUEST_SEARCH);
    }

    private void takePicture(View v, String album) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
                Photo photo = new Photo(album, latitude, longitude, timeStamp, "");
                photoDB.addPhoto(photo.getName(), timeStamp, latitude, longitude, "", album);
                String imageFileName = photo.getAlbum() + "_" + photo.getName() + "_";
                photoFile = File.createTempFile(
                        imageFileName,
                        ".jpg",
                        getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                );
            } catch (IOException ex) {
                Log.d("IOException", ex.toString());
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.project.michael.photoalbum",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void showPopup(final View v) {
        final Dialog dialog = new Dialog(this);
        final ListView listView;
        Button cancel;
        dialog.setContentView(R.layout.popup);
        dialog.setCanceledOnTouchOutside(false);
        listView = dialog.findViewById(R.id.List);
        cancel = dialog.findViewById(R.id.cancel_button);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, albumDB.getAllAlbumNames());
        listView.setAdapter(adapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                Log.d("Item", item);
                takePicture(findViewById(android.R.id.content), item);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void newAlbumDialog(final View v) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_album_dialog);
        dialog.setCanceledOnTouchOutside(false);
        final Button confirm = dialog.findViewById(R.id.new_album_okay);
        final Button cancel = dialog.findViewById(R.id.new_album_cancel);
        final EditText albumName = dialog.findViewById(R.id.new_album_text);
        final TextView warning = dialog.findViewById(R.id.new_album_warning);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!albumDB.findAlbum(albumName.getText().toString())) {
                    Log.d("Album Name", albumName.getText().toString());
                    albumDB.addAlbum(albumName.getText().toString());
                    albumAdapter.swapItems(albumDB.getAllAlbums());
                    dialog.dismiss();
                } else {
                    warning.setVisibility(View.VISIBLE);
                }
            }
        });
        dialog.show();
    }

    public void deleteAlbumDialog(final View v) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.delete_album_dialog);
        dialog.setCanceledOnTouchOutside(false);
        final Button confirm = dialog.findViewById(R.id.del_album_okay);
        final Button cancel = dialog.findViewById(R.id.del_album_cancel);
        final EditText albumName = dialog.findViewById(R.id.del_album_text);
        final TextView warning = dialog.findViewById(R.id.del_album_warning);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (albumDB.findAlbum(albumName.getText().toString())) {
                    Log.d("Delete Album", albumName.getText().toString());
                    albumDB.deleteAlbumWithName(albumName.getText().toString());
                    /* Also need to delete all photos in this album
                     * in both database and internal storage */
                    photoDB.deletePhotosInAlbum(albumName.getText().toString());
                    albumAdapter.swapItems(albumDB.getAllAlbums());
                    dialog.dismiss();
                } else {
                    warning.setVisibility(View.VISIBLE);
                }
            }
        });
        dialog.show();
    }

}
