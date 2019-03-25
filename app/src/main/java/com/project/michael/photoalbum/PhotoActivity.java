package com.project.michael.photoalbum;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.project.michael.photoalbum.adapter.PhotoAdapter;
import com.project.michael.photoalbum.database.DBHelper;
import com.project.michael.photoalbum.database.PhotoDBHelper;
import com.project.michael.photoalbum.model.Photo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PhotoActivity extends AppCompatActivity {

    private PhotoDBHelper photoDB;
    private RecyclerView photoRecyclerView;
    private PhotoAdapter adapter;
    private ArrayList<Photo> photoList;

    private static final int PHOTO_DETAIL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        photoDB = new PhotoDBHelper(this);

        photoRecyclerView = findViewById(R.id.photo_grids);


        String album = getIntent().getExtras().getString("Album");
        photoList = photoDB.getAllPhotosFromAlbum(album);
        //Log.d("Album size", photoList.get(2).getLatitude() + "");
        adapter = new PhotoAdapter(photoList, getApplicationContext(), new PhotoAdapter.onItemClickListener() {
            @Override
            public void onItemClick(final Photo photo) {
                Bundle bun = new Bundle();
                bun.putString("name", photo.getName());
                bun.putString("album", photo.getAlbum());
                Intent intent = new Intent(getBaseContext(), PhotoDetailActivity.class);
                intent.putExtras(bun);
                PhotoActivity.this.startActivityForResult(intent, PHOTO_DETAIL);
            }
        });
        photoRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        photoRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_DETAIL) {
            if (resultCode == RESULT_OK) {
                adapter.swapItems(photoDB.getAllPhotos());
            }
        }
    }
}
