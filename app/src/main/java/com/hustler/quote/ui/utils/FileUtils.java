package com.hustler.quote.ui.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ViewGroup;

import com.hustler.quote.ui.pojo.UserWorkImages;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Sayi on 30-11-2017.
 */

public class FileUtils {

    public static File savetoDevice(final ViewGroup layout) {
        final File[] filetoReturn = new File[1];
//        Bitmap bitmap = layout.getDrawingCache();
        layout.buildDrawingCache(true);
        Bitmap bitmap = layout.getDrawingCache(true).copy(Bitmap.Config.RGB_565, false);
        layout.destroyDrawingCache();
//        layout.setDrawingCacheEnabled(false);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        File file1;
        File file;

        file1 = new File(Environment.getExternalStorageDirectory() + File.separator + "Quotzy");
        if (file1.isDirectory()) {
            file = new File(Environment.getExternalStorageDirectory() + File.separator + "Quotzy" +
                    File.separator + "QUOTES--" + System.currentTimeMillis() + ".jpg");
        } else {
            file1.mkdir();
            file = new File(Environment.getExternalStorageDirectory() + File.separator + "Quotzy" +
                    File.separator + "QUOTES--" + System.currentTimeMillis() + ".jpg");
        }

        filetoReturn[0] = file;
        Log.d("ImageLocation -->", file.toString());
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.close();
//                    App.showToast(QuoteDetailsActivity.this,getString(R.string.image_saved));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        new Thread() {
//            @Override
//            public void run() {
//
//
//            }
//        }.start();
        return filetoReturn[0];

//        file = new File(new StringBuilder().append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+File.separator+"QUOTZY")

//                file. new StringBuilder()
//                .append(File.separator)
//                .append("QUOTES--")
//                        .append(quote.getAuthor())
//                .append(System.currentTimeMillis())
//                .append(".jpeg")
//                .toString());


    }

    public static Bitmap returnBitmap(final ViewGroup layout) {
        layout.buildDrawingCache();
        Bitmap bitmap = layout.getDrawingCache(true).copy(Bitmap.Config.RGB_565, false);
        layout.destroyDrawingCache();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return bitmap;
    }

    // METHOSD TOOK FROM INTERNET
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath},
                null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static UserWorkImages getImagesFromSdCard() {
        File file;
        String[] imagePaths;
        String[] imageNames;
        File[] files;

        file = new File(Environment.getExternalStorageDirectory() + File.separator + "Quotzy");
        file.mkdir();
        if (file.isDirectory()) {
            files = file.listFiles();
            imageNames = new String[files.length];
            imagePaths = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                imageNames[i] = files[i].getName();
                imagePaths[i] = files[i].getAbsolutePath();
            }
            return new UserWorkImages(imagePaths, imageNames);
        } else {
            return null;
        }

    }


}
