package com.project.michael.photoalbum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.project.michael.photoalbum.adapter.PhotoAdapter;
import com.project.michael.photoalbum.database.PhotoDBHelper;
import com.project.michael.photoalbum.model.Photo;

import java.util.ArrayList;
import java.util.Iterator;

public class PhotoActivity extends AppCompatActivity {

    private PhotoDBHelper photoDB;
    private PhotoAdapter adapter;
    private ArrayList<Photo> photoList;

    private static final int PHOTO_DETAIL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        photoDB = new PhotoDBHelper(this);

        RecyclerView photoRecyclerView = findViewById(R.id.photo_grids);


        String album = getIntent().getExtras().getString("Album");
        if (album == null) {
            Log.d("Album name", "null");
            getSearchResults(getIntent());
        } else {
            photoList = photoDB.getAllPhotosFromAlbum(album);
        }
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

    private void getSearchResults(Intent data) {
        photoList = photoDB.getAllPhotos();
        Iterator<Photo> iterator = photoList.iterator();
        String startDate = data.getStringExtra("STARTDATE");
        String endDate = data.getStringExtra("ENDDATE");
        double[] startloc = data.getDoubleArrayExtra("STARTLOC");
        double[] endloc = data.getDoubleArrayExtra("ENDLOC");

        while (iterator.hasNext()) {
            Photo photo = iterator.next();
            if (!photo.matchCondition(startDate, endDate, startloc, endloc)) {
                iterator.remove();
            }
        }
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
