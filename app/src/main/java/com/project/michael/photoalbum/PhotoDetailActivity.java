package com.project.michael.photoalbum;

import com.facebook.FacebookSdk;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.project.michael.photoalbum.database.DBHelper;
import com.project.michael.photoalbum.database.PhotoDBHelper;
import com.project.michael.photoalbum.model.Photo;

import java.io.File;
import java.io.IOException;

public class PhotoDetailActivity extends AppCompatActivity {

    private ImageView thumbnail;
    private TextView latitude;
    private TextView longitude;
    private TextView caption;
    private Spinner spinner;
    private Button change;
    private Button back;
    private DBHelper db;
    private PhotoDBHelper photoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        thumbnail = findViewById(R.id.photo_thumb);
        latitude = findViewById(R.id.photo_latitude);
        longitude = findViewById(R.id.photo_longitude);
        caption = findViewById(R.id.photo_caption);
        spinner = findViewById(R.id.album_spinner);
        change = findViewById(R.id.change_album);
        back = findViewById(R.id.photo_back_button);
        db = new DBHelper(this);
        photoDB = new PhotoDBHelper(this);

        final String albumName = getIntent().getExtras().getString("album");
        final String name = getIntent().getExtras().getString("name");
        final Photo photo = photoDB.getPhotoWithNameAndAlbum(name, albumName);

        

        thumbnail.setImageBitmap(BitmapFactory.decodeFile(photo.getPath()));
        latitude.append(photo.getLatitude() + "");
        longitude.append(photo.getLongitude() + "");
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(PhotoDetailActivity.this, android.R.layout.simple_list_item_1, db.getAllAlbumNames());
        spinner.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoDetailActivity.this.finish();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newAlbum = spinner.getSelectedItem().toString();
                String oldPath = photo.getPath();
                photoDB.changeAlbum(newAlbum, photo.getName());
                String imageFileName = photo.getAlbum() + "_" + photo.getName() + "_";
                try {
                    File photoFile = File.createTempFile(
                            imageFileName,
                            ".jpg",
                            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    );
                    Log.d("New name", photo.getPath());
                    String data[] = oldPath.split("/");
                    File file = new File("/storage/emulated/0/Android/data/com.project.michael.photoalbum/files/Pictures", data[data.length - 1]);
                    boolean result = file.delete();
                    if (result) {
                        Log.d("deletion", "successful");
                    } else {
                        Log.d("deletion", "failed");
                    }
                } catch (IOException e) {
                    Log.d("Storing photo", "Failed");
                }
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
