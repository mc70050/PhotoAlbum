package com.project.michael.photoalbum.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.michael.photoalbum.model.Photo;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PhotoDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Photo";
    public static final String TABLE_NAME = "photos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_CAPTION = "caption";
    public static final String COLUMN_ALBUM = "album";

    public PhotoDBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_LATITUDE + " REAL, " +
                COLUMN_LONGITUDE + " REAL, " +
                COLUMN_CAPTION + " TEXT, " +
                COLUMN_ALBUM + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /*
     * Creates a new photo into the database.
     */
    public boolean addPhoto(String name, String time, double latitude, double longitude, String caption, String album) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_CAPTION, caption);
        values.put(COLUMN_ALBUM, album);
        db.insert(TABLE_NAME, null, values);
        return true;
    }

    public ArrayList<Photo> getAllPhotos() {
        ArrayList<Photo> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            list.add(new Photo(res.getString(res.getColumnIndex(COLUMN_ALBUM)),
                    res.getDouble(res.getColumnIndex(COLUMN_LATITUDE)),
                    res.getDouble(res.getColumnIndex(COLUMN_LONGITUDE)),
                    res.getString(res.getColumnIndex(COLUMN_TIME)),
                    res.getString(res.getColumnIndex(COLUMN_CAPTION))));
            res.moveToNext();
        }
        return list;
    }

    public ArrayList<Photo> getAllPhotosFromAlbum(String name) {
        ArrayList<Photo> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + TABLE_NAME + " where album=" + "'" + name + "'" + "", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            list.add(new Photo(res.getString(res.getColumnIndex(COLUMN_ALBUM)),
                    res.getDouble(res.getColumnIndex(COLUMN_LATITUDE)),
                    res.getDouble(res.getColumnIndex(COLUMN_LONGITUDE)),
                    res.getString(res.getColumnIndex(COLUMN_TIME)),
                    res.getString(res.getColumnIndex(COLUMN_CAPTION))));
            res.moveToNext();
        }
        return list;
    }

    public Integer deletePhotosInAlbum(String album) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "album = ? ",
                new String[] { album });
    }
}
