package com.hustler.quote.ui.utils;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.media.ExifInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.hustler.quote.R;
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
        Bitmap bitmap = layout.getDrawingCache(true).copy(Bitmap.Config.ARGB_8888, false);
        layout.destroyDrawingCache();
//        layout.setDrawingCacheEnabled(false);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        File directoryChecker;
        File savingFile;

        directoryChecker = new File(Environment.getExternalStorageDirectory() + File.separator + "Quotzy");
        if (directoryChecker.isDirectory()) {
            savingFile = new File(Environment.getExternalStorageDirectory() + File.separator + "Quotzy" +
                    File.separator + "QUOTES--" + System.currentTimeMillis() + ".jpeg");
        } else {
            directoryChecker.mkdir();
            savingFile = new File(Environment.getExternalStorageDirectory() + File.separator + "Quotzy" +
                    File.separator + "QUOTES--" + DateandTimeutils.convertDate(System.currentTimeMillis(), DateandTimeutils.DATE_FORMAT_2) + ".jpeg");
        }

        filetoReturn[0] = savingFile;
        Log.d("ImageLocation -->", savingFile.toString());
        try {
            savingFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(savingFile);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.close();
//                    App.showToast(QuoteDetailsctivity.this,getString(R.string.image_saved));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ExifInterface exifInterface = new ExifInterface(filetoReturn[0].getAbsolutePath());
            exifInterface.setAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION,filetoReturn[0].getName());
            exifInterface.setAttribute(ExifInterface.TAG_DATETIME, DateandTimeutils.convertDate(System.currentTimeMillis(), DateandTimeutils.DATE_FORMAT_2));
        } catch (IOException io) {
            io.printStackTrace();
        }

        return filetoReturn[0];


    }

    public static File savetoDeviceCustom(final ViewGroup layout) {
        final File[] filetoReturn = new File[1];
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        layout.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        layout.buildDrawingCache(true);
        Bitmap bitmap = layout.getDrawingCache(true).copy(Bitmap.Config.ARGB_8888, false);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {

            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[16 * 1024];
        scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);

//        Bitmap bitmap = layout.getDrawingCache();
        layout.destroyDrawingCache();
//        layout.setDrawingCacheEnabled(false);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

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
//                    App.showToast(QuoteDetailsctivity.this,getString(R.string.image_saved));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filetoReturn[0];


    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;

    }

    public static Bitmap returnBitmap(final ViewGroup layout) {
        layout.buildDrawingCache();
        layout.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = layout.getDrawingCache(true).copy(Bitmap.Config.ARGB_8888, false);
        layout.destroyDrawingCache();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return bitmap;
    }

    // METHOSD TOOK FROM INTERNET
    public static Uri getImageContentUri(Context context, File imageFile) {
        try {
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
        } catch (Exception ie) {
            if (ie instanceof SecurityException) {
                Toast_Snack_Dialog_Utils.show_ShortToast((Activity) context, context.getString(R.string.permission_comulsory));
            } else {
                Toast_Snack_Dialog_Utils.show_ShortToast((Activity) context, context.getString(R.string.operation_failed));

            }
            return null;
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

    public static  void setwallpaper(Activity activity,String imagepath){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(WallpaperManager.getInstance(activity).
                    getCropAndSetWallpaperIntent(new Uri.Builder().path(imagepath).build()));

            activity.startActivity(intent);
        } else {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
                wallpaperManager.setBitmap((bitmap));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void shareImage(Activity activity,String imagePath){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT,imagePath);
        intent.putExtra(Intent.EXTRA_TITLE,imagePath);
        if(imagePath!=null){
            intent.putExtra(Intent.EXTRA_STREAM,imagePath);
            intent.putExtra(Intent.EXTRA_MIME_TYPES,"jpeg");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivity(Intent.createChooser(intent, "send"));
        }else {
            Toast_Snack_Dialog_Utils.show_ShortToast(activity,activity.getString(R.string.Unable_to_save_share_image));
        }
    }
}
