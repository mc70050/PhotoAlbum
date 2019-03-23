package com.project.michael.photoalbum.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.michael.photoalbum.R;
import com.project.michael.photoalbum.model.Photo;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private List<Photo> photoList;
    Context context;

    public PhotoAdapter(List<Photo> photoList, Context context) {
        this.photoList = photoList;
        this.context = context;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View photoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_grid, parent, false);
        PhotoViewHolder holder = new PhotoViewHolder(photoView, new PhotoAdapter.PhotoViewHolder.myViewHolderClicks() {
            public void onPhoto(View v) {
                // Change later
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, final int position) {
        //Log.d("path", photoList.get(position).getPath());
        holder.thumbnail.setImageBitmap(BitmapFactory.decodeFile(photoList.get(position).getPath()));
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public void swapItems(List< Photo > photoList){
        this.photoList = photoList;
        notifyDataSetChanged();
    }



    public static class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView thumbnail;
        public myViewHolderClicks mListener;

        public PhotoViewHolder(View view, myViewHolderClicks listener) {
            super(view);
            mListener = listener;
            thumbnail = view.findViewById(R.id.thumbnail);
            thumbnail.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onPhoto(v);
        }

        public interface myViewHolderClicks {
            void onPhoto(View v);
        }
    }
}
