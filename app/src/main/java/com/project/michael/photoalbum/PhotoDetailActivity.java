package com.project.michael.photoalbum;

import com.facebook.FacebookSdk;

import android.graphics.Bitmap;
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

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
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
    private Button share;
    private Button backup;
    private DBHelper db;
    private PhotoDBHelper photoDB;
    private ShareDialog shareDialog;

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
        share = findViewById(R.id.photo_share);
        backup = findViewById(R.id.photo_backup);
        db = new DBHelper(this);
        photoDB = new PhotoDBHelper(this);
        shareDialog = new ShareDialog(this);

        final String albumName = getIntent().getExtras().getString("album");
        final String name = getIntent().getExtras().getString("name");
        final Photo photo = photoDB.getPhotoWithNameAndAlbum(name, albumName);

        ShareButton shareButton = (ShareButton)findViewById(R.id.photo_share);
        Bitmap image = BitmapFactory.decodeFile(photo.getPath());
        SharePhoto sharedPhoto = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(sharedPhoto)
                .build();
        shareButton.setShareContent(content);

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

//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bitmap image = BitmapFactory.decodeFile(photo.getPath());
//                SharePhoto sharedPhoto = new SharePhoto.Builder()
//                        .setBitmap(image)
//                        .build();
//                SharePhotoContent content = new SharePhotoContent.Builder()
//                        .addPhoto(sharedPhoto)
//                        .build();
//                shareDialog.show(content);
//            }
//        });
    }
}
