package com.project.michael.photoalbum.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.michael.photoalbum.MainActivity;
import com.project.michael.photoalbum.PhotoActivity;
import com.project.michael.photoalbum.R;
import com.project.michael.photoalbum.model.Album;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private List<Album> albumList;
    Context context;

    public AlbumAdapter(List<Album> albumList, Context context) {
        this.albumList = albumList;
        this.context = context;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_list, parent, false);
        AlbumViewHolder avh = new AlbumViewHolder(groceryProductView, new AlbumAdapter.AlbumViewHolder.myViewHolderClicks() {
            public void onAlbum(View v, String name) {
                Log.d("Album name", name);
                Bundle bun = new Bundle();
                bun.putString("Album", name);
                Intent intent = new Intent(parent.getContext(), PhotoActivity.class);
                intent.putExtras(bun);
                parent.getContext().startActivity(intent);
            }
        });
        return avh;
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, final int position) {
        holder.textview.setText(albumList.get(position).getName());
    }

    public void swapItems(List< Album > albumList){
        this.albumList = albumList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textview;
        public myViewHolderClicks mListener;

        public AlbumViewHolder(View view, myViewHolderClicks listener) {
            super(view);
            mListener = listener;
            textview = view.findViewById(R.id.album_name);
            textview.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onAlbum(v, textview.getText().toString());
        }

        public interface myViewHolderClicks {
            void onAlbum(View v, String name);
        }
    }
}
