package com.project.michael.photoalbum.model;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class Photo {

    private final String SEPARATOR = "_";

    private String album;
    private String name;
    private double latitude;
    private double longitude;
    private String date;
    private String caption;

    /**
     * Default constructor
     */
    public Photo() {
        album = "new";
        latitude = 0;
        longitude = 0;
        date = "";
        caption = "";
    }

    /**
     * Constructor for initializing a Photo object.
     * Name is a combination of all other fields.
     * @param album name of album
     * @param latitude double representing latitude
     * @param longitude double representing longitude
     * @param date date of photo taken
     * @param caption description/comments of photo
     */
    public Photo(String album, double latitude, double longitude, String date, String caption) {
        this.album = album;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.caption = caption;
        setName(album, latitude, longitude, date, caption);
    }

    public void setAlbum(String album) { this.album = album; }

    public String getAlbum() { return album; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLatitude() { return latitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getLongitude() { return longitude; }

    public void setDate(String date) { this.date = date; }

    public String getDate() { return date; }

    public void setCaption(String caption) { this.caption = caption; }

    public void setName(String album, double latitude, double longitude, String date, String caption) {
        String temp = album + SEPARATOR + latitude + SEPARATOR + longitude + SEPARATOR + date + SEPARATOR + caption;
        name = temp.hashCode() + "";
    }

    public String getName() { return name; }

    public String getPath() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.project.michael.photoalbum/files/Pictures");
        File[] fList = file.listFiles();
        if (fList != null) {
            for (File f : file.listFiles()) {
                Log.d("name1", f.getName());
                Log.d("name2", getName());
                String parts[] = f.getName().split("_");
                if (parts[1].equalsIgnoreCase(getName())) {
                    Log.d("Testing", "here");
                    return f.getPath();
                }
            }
        }
        return null;
    }
}
