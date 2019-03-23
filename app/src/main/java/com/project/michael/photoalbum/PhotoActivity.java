package com.project.michael.photoalbum;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.project.michael.photoalbum.adapter.PhotoAdapter;
import com.project.michael.photoalbum.database.PhotoDBHelper;
import com.project.michael.photoalbum.model.Photo;

import java.util.ArrayList;

public class PhotoActivity extends AppCompatActivity {

    private PhotoDBHelper photoDB;
    private RecyclerView photoRecyclerView;
    private PhotoAdapter adapter;
    private ArrayList<Photo> photoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        photoDB = new PhotoDBHelper(this);

        photoRecyclerView = findViewById(R.id.photo_grids);


        String album = getIntent().getExtras().getString("Album");
        photoList = photoDB.getAllPhotosFromAlbum(album);
        //Log.d("Album size", photoList.get(2).getLatitude() + "");
        adapter = new PhotoAdapter(photoList, getApplicationContext());
        photoRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        photoRecyclerView.setAdapter(adapter);
    }
}
