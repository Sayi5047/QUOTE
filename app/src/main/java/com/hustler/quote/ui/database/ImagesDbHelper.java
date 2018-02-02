package com.hustler.quote.ui.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hustler.quote.ui.pojo.unspalsh.Links;
import com.hustler.quote.ui.pojo.unspalsh.Profile_image;
import com.hustler.quote.ui.pojo.unspalsh.Unsplash_Image;
import com.hustler.quote.ui.pojo.unspalsh.Urls;
import com.hustler.quote.ui.pojo.unspalsh.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sayi on 02-02-2018.
 */

public class ImagesDbHelper extends SQLiteOpenHelper {

    long id;
    Activity activity;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "favimages.db";


    public ImagesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public ImagesDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ImagesDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contract.Images.CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Contract.Images.SQL_DELETE_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }


    public void addFav(final Unsplash_Image image) {
        final SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();

        new Thread(new Runnable() {
            @Override
            public void run() {

                contentValues.put(Contract.Images.IMAGE_ID, image.getId());
                contentValues.put(Contract.Images.IMAGE_USERNAME, image.getUser().getUsername());
                contentValues.put(Contract.Images.IMAGE_PROFILEPIC, image.getUser().getProfile_image().getLarge());
                contentValues.put(Contract.Images.IMAGE_REGULAR, image.getUrls().getRegular());
                contentValues.put(Contract.Images.IMAGE_HD, image.getUrls().getFull());
                contentValues.put(Contract.Images.IMAGE_UHD, image.getUrls().getRaw());
                contentValues.put(Contract.Images.IMAGE_DOWNLOAD_LOCATION, image.getLinks().getDownload_location());
                contentValues.put(Contract.Images.IMAGE_IS_LIKED, 1);

                sqLiteDatabase.insert(Contract.Images.TABLE_NAME, null, contentValues);
                sqLiteDatabase.close();

            }
        }).run();
    }

    public void removeFav(final Unsplash_Image image) {
        final SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();

        new Thread(new Runnable() {
            @Override
            public void run() {
                sqLiteDatabase.delete(Contract.Images.TABLE_NAME, Contract.Images.IMAGE_ID +  " =?" ,new String[]{image.getId()});
                sqLiteDatabase.close();

            }
        }).run();
    }

    public List<Unsplash_Image> getAllFav() {

        final List<Unsplash_Image> allQuotes = new ArrayList<>();

        final SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        String select_query = "SELECT * FROM " + Contract.Images.TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(select_query, null);
        while (cursor.moveToNext()) {
            Unsplash_Image unsplash_image = new Unsplash_Image();

            User user = new User();
            Profile_image profile_image = new Profile_image();
            Urls urls = new Urls();
            Links links = new Links();

            user.setUsername((cursor.getString(cursor.getColumnIndexOrThrow(Contract.Images.IMAGE_USERNAME))));

            profile_image.setLarge(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Images.IMAGE_PROFILEPIC)));

            urls.setRegular((cursor.getString(cursor.getColumnIndexOrThrow(Contract.Images.IMAGE_REGULAR))));
            urls.setFull((cursor.getString(cursor.getColumnIndexOrThrow(Contract.Images.IMAGE_HD))));
            urls.setRaw((cursor.getString(cursor.getColumnIndexOrThrow(Contract.Images.IMAGE_UHD))));

            links.setDownload_location(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Images.IMAGE_DOWNLOAD_LOCATION)));

            user.setProfile_image(profile_image);

            unsplash_image.setId(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Images.IMAGE_ID)));
            unsplash_image.setUser(user);
            unsplash_image.setUrls(urls);
            unsplash_image.setLinks(links);

            allQuotes.add(unsplash_image);

        }
        cursor.close();
        sqLiteDatabase.close();
        return allQuotes;
//            }
//        }).run();

    }

}
