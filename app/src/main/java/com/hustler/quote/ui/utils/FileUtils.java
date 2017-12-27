package com.hustler.quote.ui.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.media.ExifInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.pojo.UserWorkImages;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Sayi on 30-11-2017.
 */

public class FileUtils {

    public interface onSaveComplete {
        public void onImageSaveListner(File file);
    }

    public static void savetoDevice(final ViewGroup layout, final Activity activity, final onSaveComplete listneer) {
        final String[] name = {null};
        final String[] format = {null};
        final File[] filetoReturn = new File[1];
        final String[] projectname = new String[1];


        final Dialog dialog = new Dialog(activity, R.style.EditTextDialog);
        dialog.setContentView(View.inflate(activity, R.layout.save_image_layout, null));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
        dialog.setCancelable(false);

        TextView headTv;
        final EditText etProjectName;
        final RadioGroup rdGroup;
        final RadioButton rbJpeg;
        final RadioButton rbPng;
        LinearLayout btLlLayout;
        LinearLayout root;
        Button btClose, btSave;


        headTv = (TextView) dialog.findViewById(R.id.head_tv);
        etProjectName = (EditText) dialog.findViewById(R.id.et_project_name);
        rdGroup = (RadioGroup) dialog.findViewById(R.id.rd_group);
        rbJpeg = (RadioButton) dialog.findViewById(R.id.rb_jpeg);
        rbPng = (RadioButton) dialog.findViewById(R.id.rb_png);
        btLlLayout = (LinearLayout) dialog.findViewById(R.id.bt_ll_layout);
        root = (LinearLayout) dialog.findViewById(R.id.root_Lo);
        btClose = (Button) dialog.findViewById(R.id.bt_close);
        btSave = (Button) dialog.findViewById(R.id.bt_save);

        TextUtils.findText_and_applyTypeface(root, activity);

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbJpeg.getId() == rdGroup.getCheckedRadioButtonId()) {
                    format[0] = Constants.JPEG;

                } else if (rbPng.getId() == rdGroup.getCheckedRadioButtonId()) {
                    format[0] = Constants.PNG;
                }
                if (etProjectName.getText().length() <= 0 || etProjectName.getText() == null) {
                    etProjectName.setError(activity.getString(R.string.please_enter_project_name));
                } else {
                    name[0] = etProjectName.getText().toString();
                    if (name[0] == null) {
                        projectname[0] = "QUOTES--" + DateandTimeutils.convertDate(System.currentTimeMillis(), DateandTimeutils.DATE_FORMAT_2);
                    } else {
                        projectname[0] = name[0];
                    }
//        Bitmap bitmap = layout.getDrawingCache();


                    layout.buildDrawingCache(true);
                    Bitmap bitmap = layout.getDrawingCache(true).copy(Bitmap.Config.ARGB_8888, false);
                    layout.destroyDrawingCache();
//        layout.setDrawingCacheEnabled(false);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    if (format[0].equalsIgnoreCase(Constants.JPEG)) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    } else {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    }

                    File directoryChecker;
                    File savingFile;

                    directoryChecker = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getString(R.string.Quotzy));
                    if (format[0].equalsIgnoreCase(Constants.JPEG)) {
                        if (directoryChecker.isDirectory()) {
                            savingFile = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getString(R.string.Quotzy) +
                                    File.separator + projectname[0] + ".jpg");
                        } else {
                            directoryChecker.mkdir();
                            savingFile = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getString(R.string.Quotzy) +
                                    File.separator + projectname[0] + ".jpg");
                        }

                    } else {
                        if (directoryChecker.isDirectory()) {
                            savingFile = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getString(R.string.Quotzy) +
                                    File.separator + projectname[0] + ".png");
                        } else {
                            directoryChecker.mkdir();
                            savingFile = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getString(R.string.Quotzy) +
                                    File.separator + projectname[0] + ".png");
                        }

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
                        exifInterface.setAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION, filetoReturn[0].getName());
                        exifInterface.setAttribute(ExifInterface.TAG_DATETIME, DateandTimeutils.convertDate(System.currentTimeMillis(), DateandTimeutils.DATE_FORMAT_2));
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                    listneer.onImageSaveListner(filetoReturn[0]);
                    dialog.dismiss();
                }
            }
        });
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

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
                    new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ", new String[]{filePath}, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
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

    public static void setwallpaper(Activity activity, String imagepath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(WallpaperManager.getInstance(activity).
                    getCropAndSetWallpaperIntent(FileUtils.getImageContentUri(activity, new File(imagepath))));

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

    public static void shareImage(Activity activity, String imagePath) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, imagePath);
        shareIntent.putExtra(Intent.EXTRA_TITLE, imagePath);
        if (imagePath != null) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(imagePath)));
            shareIntent.setType("image/jpeg");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivity(Intent.createChooser(shareIntent, "send"));
        } else {
            Toast_Snack_Dialog_Utils.show_ShortToast(activity, activity.getString(R.string.Unable_to_save_share_image));
        }


    }
}
