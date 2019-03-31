package com.project.michael.photoalbum.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.project.michael.photoalbum.R;
import com.project.michael.photoalbum.model.Photo;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private List<Photo> photoList;
    private onItemClickListener listener;
    private Context context;

    public interface onItemClickListener {
        void onItemClick(Photo photo);
    }

    public PhotoAdapter(List<Photo> photoList, Context context, onItemClickListener listener) {
        this.photoList = photoList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View photoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_grid, parent, false);
        return new PhotoViewHolder(photoView);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, final int position) {
        //Log.d("path", photoList.get(position).getPath());
        holder.bind(photoList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public void swapItems(List< Photo > photoList){
        this.photoList = photoList;
        notifyDataSetChanged();
    }



    public static class PhotoViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;

        PhotoViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);
        }

        void bind(final Photo photo, final onItemClickListener listener) {
            thumbnail.setImageBitmap(BitmapFactory.decodeFile(photo.getPath()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(photo);
                }
            });
        }
    }
}
