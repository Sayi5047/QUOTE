package com.hustler.quote.ui.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.crash.FirebaseCrash;
import com.hustler.quote.R;
import com.hustler.quote.ui.Executors.AppExecutor;
import com.hustler.quote.ui.adapters.InstallFontAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.pojo.UserWorkImages;
import com.hustler.quote.ui.textFeatures.TextFeatures;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by Sayi on 30-11-2017.
 */
/*   Copyright [2018] [Sayi Manoj Sugavasi]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/
public class FileUtils {

    public static void unzipAndSave(File file, @NonNull final Activity activity, String action) {
        final List<String> zipContents = new ArrayList<>();
        String zipLocation = null;
        try {
            if (action.equals(Intent.ACTION_VIEW)) {
                zipLocation = Environment.getExternalStorageDirectory() + file.getAbsolutePath();
            } else if (action.equals(Intent.ACTION_SEND)) {
                zipLocation = file.getAbsolutePath();
            }


            Log.d("ZIP Location " + action, zipLocation);
            ZipFile zipToRead = new ZipFile(zipLocation);

            Enumeration zipEntries = zipToRead.entries();
            while (zipEntries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) zipEntries.nextElement();
                String currentEntry = entry.getName();
                Log.d("ZIP LOCATIONS", currentEntry);
                if (currentEntry.endsWith(".ttf")) {
                    zipContents.add(currentEntry);
                } else if (currentEntry.endsWith(".otf")) {
                    zipContents.add(currentEntry);
                } else if (currentEntry.endsWith(".TTF")) {
                    zipContents.add(currentEntry);
                } else if (currentEntry.endsWith(".OTF")) {
                    zipContents.add(currentEntry);

                }
            }

            String targetLocationPath = Constants.APP_FONTS_FOLDER;
            String tempTargetLocationPath = Constants.APP_TEMP_FONTS_FOLDER;
            final String recentlySavedFontsFolderPath;
//            File tempTargetLocationFile = new File(tempTargetLocationPath);
            final File targetDirectory = new File(targetLocationPath);

//            if (!tempTargetLocationFile.isDirectory()) {
//                tempTargetLocationFile.mkdirs();
//            }
            if (!targetDirectory.isDirectory()) {
                targetDirectory.mkdirs();
            }

            recentlySavedFontsFolderPath = doUnZIP(file, tempTargetLocationPath);
            final Dialog dialog = new Dialog(activity);
            dialog.setContentView(View.inflate(activity, R.layout.install_fonts_layout, null));
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.white_rounded_drawable);
            dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
            dialog.setCancelable(false);
            RecyclerView recyclerView;
            Button btClose, btInstall;
            AdView adView;
            recyclerView = dialog.findViewById(R.id.font_recycler);
            btClose = dialog.findViewById(R.id.bt_close);
            btInstall = dialog.findViewById(R.id.bt_save);
            adView = dialog.findViewById(R.id.adView);
            AdUtils.loadBannerAd(adView, activity);

            recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

            final String[] source_font_paths_list = TextFeatures.getDownloadedFonts(new File(Objects.requireNonNull(recentlySavedFontsFolderPath)));

            recyclerView.setAdapter(new InstallFontAdapter(activity, zipContents, recentlySavedFontsFolderPath, targetLocationPath, source_font_paths_list));
            dialog.show();
            btInstall.setOnClickListener(v -> {
                ProgressDialog dialog1 = new ProgressDialog(activity);
                dialog1.setTitle(activity.getString(R.string.installing_Fonts));
                dialog1.show();
                int i = 0;
                while (i < zipContents.size()) {
                    new File(source_font_paths_list[i]).renameTo(new File(targetDirectory + File.separator + zipContents.get(i)));
                    i++;
                }
                dialog1.cancel();
                Toast_Snack_Dialog_Utils.createDialog(activity, activity.getString(R.string.congratulations), activity.getString(R.string.font_installed), null, activity.getString(R.string.close), new Toast_Snack_Dialog_Utils.Alertdialoglistener() {
                    @Override
                    public void onPositiveSelection() {
                    }

                    @Override
                    public void onNegativeSelection() {

                    }
                });
                dialog.dismiss();
            });
            btClose.setOnClickListener(v -> dialog.dismiss());
            dialog.setOnKeyListener((dialog12, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
                    dialog12.dismiss();
                    return true;
                } else {
                    return false;
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static String doUnZIP(@NonNull final File sourceZipLocation, final String tempTargetLocationPath) {

        try {
//            TEMPORARY FILES
            FileInputStream sourceFileInputStream = new FileInputStream(sourceZipLocation);
            ZipInputStream sourceZipInputStream = new ZipInputStream(sourceFileInputStream);
//
            ZipEntry zipEntry;
//          TO CREATE A SINGLE FOLDER WITH FOLDER NAME
            String firstZipEntryName = sourceZipInputStream.getNextEntry().getName();
            sourceZipInputStream.closeEntry();
            Log.e("ZIP ENTRY NAME", firstZipEntryName);

            String newFontFolder = tempTargetLocationPath + File.separator + firstZipEntryName.substring(0, firstZipEntryName.length() - 4);
            File targetUnzippingFolder = new File(newFontFolder);
            if (!targetUnzippingFolder.isDirectory()) {
                targetUnzippingFolder.mkdirs();
            }
            while ((zipEntry = sourceZipInputStream.getNextEntry()) != null) {
                File newFile = new File(newFontFolder + File.separator + zipEntry.getName());
                Log.e(" New File ", newFile.getAbsolutePath());
                FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[2048];
                int count;
                while ((count = sourceZipInputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, count);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    fileOutputStream.write(bytes);
                    byteArrayOutputStream.reset();
                }
                sourceZipInputStream.closeEntry();
                fileOutputStream.close();

            }

            sourceZipInputStream.close();
            return newFontFolder;
        } catch (IOException ie) {
            ie.printStackTrace();
            Log.e("EXCEPTION CAUGHT", Objects.requireNonNull(ie.getMessage()));
            return null;
        }


    }

    public static void saveProjectToDevice(@NonNull final ViewGroup sourceLayout,
                                           @NonNull final Activity activity,
                                           @NonNull final onSaveComplete onSaveCompleteListener,
                                           final int gifFrameCount) {

        final String[] name = {null};
        final String[] format = {null};
        final File[] filetoReturn = new File[1];
        final String[] projectname = new String[1];
        AdView adView;
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(View.inflate(activity, R.layout.save_image_layout, null));
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.white_rounded_drawable);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
        dialog.setCancelable(false);

        TextView headTv;
        final EditText etProjectName;
        final RadioGroup rdGroup;
        final RadioButton rbJpeg;
        final RadioButton rbPng;
        LinearLayout root;
        Button btClose, btSave, gifBtn;


        etProjectName = dialog.findViewById(R.id.et_project_name);
        rdGroup = dialog.findViewById(R.id.rd_group);
        rbJpeg = dialog.findViewById(R.id.rb_jpeg);
        rbPng = dialog.findViewById(R.id.rb_png);
        root = dialog.findViewById(R.id.root_Lo);
        btClose = dialog.findViewById(R.id.bt_close);
        btSave = dialog.findViewById(R.id.bt_save);
        adView = dialog.findViewById(R.id.adView);
        gifBtn = dialog.findViewById(R.id.gif_btn);
        AdUtils.loadBannerAd(adView, activity);
        TextUtils.findText_and_applyTypeface(root, activity);
        gifBtn.setVisibility(View.GONE);
        gifBtn.setOnClickListener(view -> {
            ProgressBar progressBar = new ProgressBar(activity);
            progressBar.setVisibility(View.VISIBLE);
            final ArrayList<File> savedFilesList = new ArrayList<>();
            for (int i = 0; i < gifFrameCount; i++) {
                /*this should returns a value for every 100 milliseconds. so if there are 14 frames* then gif will be of length 1.4 sec/                             */

                Handler handler = new Handler();
                final int finalI = i;
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        savedFilesList.add(saveTempGifFrame(activity, sourceLayout, finalI));
                    }
                };
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 100);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

//                        animatedGifEncoder.start(byteArrayOutputStream);
//                        for (File currentFrame : savedFilesList) {
//                            animatedGifEncoder.addFrame(BitmapFactory.decodeFile(currentFrame.getAbsoluteFile().getAbsolutePath()));
//                        }
//                        animatedGifEncoder.finish();

            File file = new File(Constants.APP_SAVED_PICTURES_FOLDER + File.separator + DateandTimeutils.convertDate(System.currentTimeMillis(), DateandTimeutils.DATE_FORMAT_2) + ".gif");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath());
                fileOutputStream.write(byteArrayOutputStream.toByteArray());
                fileOutputStream.close();
                onSaveCompleteListener.onImageSaveListener(file);
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                progressBar.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
                progressBar.setVisibility(View.GONE);

            }

        });

        btSave.setOnClickListener(v -> {
            if ((etProjectName.getText().length() <= 0)) {
                etProjectName.setError(activity.getString(R.string.please_enter_project_name));
            } else if ((null == etProjectName.getText())) {
                etProjectName.setError(activity.getString(R.string.please_enter_project_name));
            } else {
                name[0] = etProjectName.getText().toString();
                if (null == name[0]) {
                    projectname[0] = "QUOTZY--" + DateandTimeutils.convertDate(System.currentTimeMillis(), DateandTimeutils.DATE_FORMAT_2);
                } else {
                    projectname[0] = name[0];
                }
            }
            saveTask(sourceLayout, activity, onSaveCompleteListener, format, filetoReturn, projectname, dialog, rdGroup, rbJpeg, rbPng);
        });
        btClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
                dialog1.dismiss();
                return true;
            } else {
                return false;
            }
        });


    }

    private static void saveTask(@NonNull ViewGroup sourceLayout, @NonNull Activity activity, @NonNull onSaveComplete onSaveCompleteListener,
                                 String[] format, File[] filetoReturn, String[] projectname, Dialog dialog, RadioGroup rdGroup, RadioButton rbJpeg, RadioButton rbPng) {
        AppExecutor.getInstance().getFileExecutor().execute(() -> {

            File directoryChecker2;
            File savingFile2;
            Bitmap bitmap;

            directoryChecker2 = new File(Constants.APP_SAVED_PICTURES_FOLDER);
            if (rbJpeg.getId() == rdGroup.getCheckedRadioButtonId()) {
                format[0] = Constants.JPEG;

            } else if (rbPng.getId() == rdGroup.getCheckedRadioButtonId()) {
                format[0] = Constants.PNG;
            }
            sourceLayout.buildDrawingCache(true);
            bitmap = sourceLayout.getDrawingCache(true).copy(Bitmap.Config.ARGB_8888, false);
            sourceLayout.destroyDrawingCache();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            if (Constants.JPEG.equalsIgnoreCase(format[0])) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            } else {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            }


            if (Constants.JPEG.equalsIgnoreCase(format[0])) {
                if (directoryChecker2.isDirectory()) {
                    savingFile2 = getSavingFile(null, true, ".jpg", activity, projectname[0]);
                } else {
                    savingFile2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + activity.getString(R.string.Qotzy) + File.separator + projectname[0] + ".jpg");
                }
            } else {
                if (directoryChecker2.isDirectory()) {
                    savingFile2 = getSavingFile(null, true, ".png", activity, projectname[0]);
                } else {
                    savingFile2 = getSavingFile(directoryChecker2, false, ".png", activity, projectname[0]);
                }
            }

            filetoReturn[0] = savingFile2;
            Log.d("ImageLocation -->", savingFile2.toString());
            try {
                savingFile2.createNewFile();
                FileOutputStream fileOutputStream2 = new FileOutputStream(savingFile2);
                fileOutputStream2.write(byteArrayOutputStream.toByteArray());
                fileOutputStream2.close();
                ContentValues contentValues = storeAndGetImageMetaData(savingFile2);
                activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
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
            onSaveCompleteListener.onImageSaveListener(filetoReturn[0]);
            AppExecutor.getInstance().getMainThreadExecutor().execute(() -> {
                Toast.makeText(Objects.requireNonNull(activity.getApplicationContext()), "File is Saved in  " + savingFile2, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });

        });
    }

    private static File getSavingFile(@NonNull File directoryChecker2, boolean b, String format, @NonNull Activity activity, String name) {
        if (!b) {
            directoryChecker2.mkdirs();
        }
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + activity.getString(R.string.Qotzy) + File.separator + name + format);
    }

    public static void showPostSaveDialog(@NonNull final Activity activity, @Nullable final File savedFile) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                RelativeLayout rootRl;
                ImageView savedImage;
                ImageView gradientImage;
                LinearLayout shareLayoutImage;
                ImageView facebook;
                ImageView whatsapp;
                ImageView instagram;
                ImageView twitter;
                ImageView others;
                final TextView ratingbarText;
                final RatingBar ratingBar;
                Button closeBtn;
                AdView adView;
                final SharedPreferences sharedPreferences;
                final Boolean isRating_clicked;

                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(View.inflate(activity, R.layout.post_save_image_layout, null));
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);
                dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
                dialog.setCancelable(false);
                rootRl = dialog.findViewById(R.id.root_Rl);
                savedImage = dialog.findViewById(R.id.saved_image);
                gradientImage = dialog.findViewById(R.id.gradient_image);
                shareLayoutImage = dialog.findViewById(R.id.share_layout_image);
                facebook = dialog.findViewById(R.id.facebook);
                whatsapp = dialog.findViewById(R.id.whatsapp);
                instagram = dialog.findViewById(R.id.instagram);
                twitter = dialog.findViewById(R.id.twitter);
                others = dialog.findViewById(R.id.others);
                ratingbarText = dialog.findViewById(R.id.ratingbar_text);
                ratingBar = dialog.findViewById(R.id.rating_bar);
                closeBtn = dialog.findViewById(R.id.close_btn);
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
                isRating_clicked = sharedPreferences.getBoolean(IntentConstants.RATING_GIVEN, false);
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                setListeners(facebook, "com.facebook.katana", activity, savedFile);
                setListeners(whatsapp, "com.whatsapp", activity, savedFile);
                setListeners(twitter, "com.twitter.android", activity, savedFile);
                setListeners(instagram, "com.instagram.android", activity, savedFile);
                closeBtn.setOnClickListener(v -> dialog.dismiss());
                savedImage.setClipToOutline(true);
                Glide.with(activity).load(savedFile).centerCrop().into(savedImage);
                if (isRating_clicked) {
                    ratingbarText.setText(activity.getString(R.string.thanks_for_rating));
                } else {
                    ratingbarText.setText(activity.getString(R.string.enjoying));
                }
                others.setOnClickListener(v -> {
                    final Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setType("image/jpeg");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT, quote_editor_body.getText());
//                shareIntent.putExtra(Intent.EXTRA_TITLE, quote_editor_author.getText());
                    Uri uri = null;
                    if (savedFile != null) {
                        if (Build.VERSION.SDK_INT >= 24) {
                            uri = FileProvider.getUriForFile(activity, activity.getString(R.string.file_provider_authority), savedFile);
                        } else {
                            uri = Uri.fromFile(savedFile);
                        }
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        activity.startActivity(Intent.createChooser(shareIntent, "send"));
                    }
                });
                ratingBar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rateInPlayStore(activity, editor);
                    }
                });
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        rateInPlayStore(activity, editor);
                    }
                });
                ratingbarText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rateInPlayStore(activity, editor);
                    }
                });

                dialog.show();
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(@NonNull DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
                            dialog.dismiss();
                            return true;
                        } else {
                            return false;
                        }
                    }
                });

            }
        }).run();
    }

    private static void rateInPlayStore(Activity activity, @NonNull SharedPreferences.Editor editor) {

        final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity  object
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }

        editor.putBoolean(IntentConstants.RATING_GIVEN, true);
        editor.commit();

    }

    private static void setListeners(ImageView facebook, @NonNull final String packageName, @NonNull final Activity activity, final File savedFile) {
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSpecificApp(packageName, activity, savedFile);
            }
        });
    }

    private static void launchSpecificApp(@NonNull String s, Activity activity, @Nullable File savedFile) {
        Intent fbIntent = activity.getPackageManager().getLaunchIntentForPackage(s);
        if (fbIntent != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage(s);
            shareIntent.putExtra(Intent.EXTRA_TITLE, activity.getString(R.string.share_text));
            shareIntent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.share_description));
            shareIntent.setType("image/jpeg");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT, quote_editor_body.getText());
//                shareIntent.putExtra(Intent.EXTRA_TITLE, quote_editor_author.getText());
            Uri uri = null;
            if (savedFile != null) {
                if (Build.VERSION.SDK_INT >= 24) {
                    uri = FileProvider.getUriForFile(activity, activity.getString(R.string.file_provider_authority), savedFile);
                } else {
                    uri = Uri.fromFile(savedFile);

                }
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                activity.startActivity(shareIntent);
            } else {
                Toast_Snack_Dialog_Utils.show_ShortToast(activity, "File does not exist");

            }
//            activity.startActivity(shareIntent);
        } else {
            Toast_Snack_Dialog_Utils.show_ShortToast(activity, activity.getString(R.string.app_does_not_exist));
        }
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

    public static Pair<Bitmap, ByteArrayOutputStream> returnBitmap(final ViewGroup layout, boolean isJpeg) {
        layout.buildDrawingCache();
//        layout.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = layout.getDrawingCache(true).copy(Bitmap.Config.ARGB_8888, false);
        layout.destroyDrawingCache();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (isJpeg) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        } else {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        }
        return new Pair<>(bitmap, byteArrayOutputStream);
    }

    public static Uri getImageContentUri(@NonNull Context context, @NonNull File imageFile) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context, context.getString(R.string.file_provider_authority), imageFile);
        } else {
            uri = Uri.fromFile(imageFile);
        }
        return uri;


    }

    @NonNull
    public static ContentValues storeAndGetImageMetaData(File parent) {
        ContentValues image = new ContentValues();
        image.put(MediaStore.Images.Media.TITLE, parent.getName());
        image.put(MediaStore.Images.Media.DISPLAY_NAME, parent.getName());
        image.put(MediaStore.Images.Media.DESCRIPTION, "Quotzy Image");
        image.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        if (parent.getAbsolutePath().contains(".jpg")) {
            image.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        } else if (parent.getAbsolutePath().contains(".png")) {
            image.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        } else {
            image.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            image.put(MediaStore.Images.Media.ORIENTATION, 0);
            image.put(MediaStore.Images.ImageColumns.BUCKET_ID, parent.toString().toLowerCase().hashCode());
            image.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, parent.getName().toLowerCase());
        }

        image.put(MediaStore.Images.Media.SIZE, parent.length());
        image.put(MediaStore.Images.Media.DATA, parent.getAbsolutePath());
        return image;
    }

    public static UserWorkImages getImagesFromSdCard(Activity activity) {
        File file;
        String[] imagePaths;
        String[] imageNames;
        File[] files;

        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + activity.getString(R.string.Qotzy));
        file.mkdir();
        if (file.isDirectory()) {
            files = file.listFiles();
            if (null != files) {
                imageNames = new String[files.length];
                imagePaths = new String[files.length];
                for (int i = 0; i < files.length; i++) {
                    imageNames[i] = files[i].getName();
                    imagePaths[i] = files[i].getAbsolutePath();
                }
                return new UserWorkImages(imagePaths, imageNames);
            } else {
                file.mkdirs();
                return null;
            }

        } else {
            file.mkdirs();
            return null;
        }

    }

    public static void setWallpaper(@NonNull Context activity, @NonNull String imageLocationPath) {
        try {
            Intent intent = new Intent(WallpaperManager.getInstance(activity).
                    getCropAndSetWallpaperIntent(FileUtils.getImageContentUri(activity, new File(imageLocationPath))));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrash.log(e.getMessage());
        }

    }

    public static void shareImage(@Nullable Activity activity, @Nullable String imagePath) {
        if (activity != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, imagePath);
            shareIntent.putExtra(Intent.EXTRA_TITLE, imagePath);
            if (imagePath != null) {
                if (Build.VERSION.SDK_INT >= 24) {
                    shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(activity.getApplicationContext(), activity.getString(R.string.file_provider_authority), new File(imagePath)));
                } else {
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(imagePath)));
                }
                shareIntent.setType("image/jpeg");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                activity.startActivity(Intent.createChooser(shareIntent, "send"));
            } else {
                Toast_Snack_Dialog_Utils.show_ShortToast(activity, activity.getString(R.string.Unable_to_save_share_image));
            }
        } else {
            Log.e("FILEUTILS", "ACTIVITY IS NOULL TO SHARE IMG");
        }


    }

    public static File downloadImageToSd_Card(String param, String download_image_name, @NonNull Context applicationContext) {

        File downloading_File;
        FileOutputStream fileOutputStream;
        InputStream inputStream;

        try {
//                GET URL
            URL url = new URL(param);
//                CRETAE DIRECTORY IN SD CARD WITH GIVEN NAME
            Environment.getExternalStorageDirectory().toString();
            File destination_downloading_directory = new File(Constants.APP_WALLPAPERS_FOLDER);
            if (!destination_downloading_directory.exists()) {
                destination_downloading_directory.mkdirs();
            }
//                NOW CREATE ONE MORE FILE INSIDE THE DIRECTORY THAT BEEN MADE
            downloading_File = new File(destination_downloading_directory + File.separator + download_image_name);
            if (downloading_File.exists()) {
                downloading_File.delete();
            }

            try {
//                    OPEN A URL CONNECTION AND ATTACH TO HTTPURLCONNECTION
                URLConnection urlConnection = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

//                    GET DATA FROM INPUT STREAM && ATTACH OOUTPUT STREAM OBJECT TO THE FILE TO BE DOWNLOADED FILE OUTPUT STRAM OBJECT
                inputStream = httpURLConnection.getInputStream();
                fileOutputStream = new FileOutputStream(downloading_File);
//                    WRITE THE DATA TO BUFFER SO WE CAN COPY EVERYTHING AT ONCE TO MEMORY WHICH IMPROOVES EFFECIANCY
                byte[] buffer = new byte[2048];
                int bufferLength;
                while ((bufferLength = inputStream.read(buffer)) > 0) {

                    fileOutputStream.write(buffer, 0, bufferLength);


                }
                inputStream.close();
                fileOutputStream.close();
                ContentValues contentValues = storeAndGetImageMetaData(downloading_File);
                Uri result = applicationContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                Log.d("IMAGE SAVED", "Image Saved in sd card");
                return downloading_File;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }


    }


    // Will implement once I reach more than 1000 users.
    public static void saveProjectWithAds(@NonNull final ViewGroup sourceLayout,
                                          @NonNull final Activity activity,
                                          @NonNull final onSaveComplete listener) {


        final String[] name = {null};
        final String[] format = {null};
        final File[] filetoReturn = new File[1];
        final String[] projectname = new String[1];
        AdView adView;
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(View.inflate(activity, R.layout.save_image_layout_ads, null));
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.white_rounded_drawable);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
        dialog.setCancelable(false);

        final EditText etProjectName;
        final RadioGroup rdGroup;
        final RadioButton rbJpeg;
        final RadioButton rbPng;
        final LinearLayout adLayout;
        LinearLayout root;
        final RewardedVideoAd mRewardedVideoAd;
        final Button btClose, btSave, remove_watermark, watch_ad, buy_pro;


        etProjectName = dialog.findViewById(R.id.et_project_name);
        rdGroup = dialog.findViewById(R.id.rd_group);
        rbJpeg = dialog.findViewById(R.id.rb_jpeg);
        rbPng = dialog.findViewById(R.id.rb_png);
        root = dialog.findViewById(R.id.root_Lo);
        adLayout = dialog.findViewById(R.id.bt_ll_watch_Ad_layout);
        adLayout.setVisibility(View.GONE);
        btClose = dialog.findViewById(R.id.bt_close);
        btSave = dialog.findViewById(R.id.bt_save);
        remove_watermark = dialog.findViewById(R.id.remove_watermark_bt);
        watch_ad = dialog.findViewById(R.id.bt_watch_ad);
        buy_pro = dialog.findViewById(R.id.bt_buy_pro);
        adView = dialog.findViewById(R.id.adView);
        AdUtils.loadBannerAd(adView, activity);
        TextUtils.findText_and_applyTypeface(root, activity);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity);
        AdUtils.loadRewardAd(mRewardedVideoAd, activity);
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
            }

            @Override
            public void onRewardedVideoAdOpened() {
                Toast.makeText(activity, "Video OPened", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                Toast.makeText(activity, "Video Closed", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRewarded(@NonNull RewardItem rewardItem) {
                Toast.makeText(activity, "onRewarded! currency: " + rewardItem.getType() + "  amount: " + rewardItem.getAmount(), Toast.LENGTH_SHORT).show();
                sourceLayout.findViewById(R.id.mark_quotzy_tv).setVisibility(View.GONE);
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                Toast.makeText(activity, "Video Clicked", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });

        remove_watermark.setOnClickListener(v -> {
//                adLayout.setVisibility(View.VISIBLE);
            sourceLayout.findViewById(R.id.mark_quotzy_tv).setVisibility(View.GONE);
            sourceLayout.findViewById(R.id.quotzy).setVisibility(View.GONE);
            remove_watermark.setVisibility(View.GONE);

        });
        watch_ad.setOnClickListener(v -> {
//                if(mRewardedVideoAd.isLoaded()){
            mRewardedVideoAd.show();

        });
        buy_pro.setOnClickListener(v -> {
            // TODO: 07-01-2018 need to implement the PRO VERSION OF APP LINK
        });

        btSave.setOnClickListener(v -> {
            if ((etProjectName.getText().length() <= 0)) {
                etProjectName.setError(activity.getString(R.string.please_enter_project_name));
            } else if ((null == etProjectName.getText())) {
                etProjectName.setError(activity.getString(R.string.please_enter_project_name));
            } else {
                name[0] = etProjectName.getText().toString();
                if (null == name[0]) {
                    projectname[0] = "QUOTZY--" + DateandTimeutils.convertDate(System.currentTimeMillis(), DateandTimeutils.DATE_FORMAT_2);
                } else {
                    projectname[0] = name[0];
                }
            }
            saveTask(sourceLayout, activity, listener, format, filetoReturn, projectname, dialog, rdGroup, rbJpeg, rbPng);
        });
        btClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
                dialog1.dismiss();
                return true;
            } else {
                return false;
            }
        });


    }


    public interface onSaveComplete {
        void onImageSaveListener(File file);

    }

    @Deprecated
    private static File saveTempGifFrame(Activity activity, ViewGroup sourceLayout, int frameNumber) {
        File framesFolder = new File(Constants.TEMP_GIF_APP_SAVED_PICTURES_FOLDER);
        if (!framesFolder.isDirectory()) {
            framesFolder.mkdirs();
        }
        String destinationFilePath = null;
        String fileName = "Frame " + frameNumber + "-" + DateandTimeutils.convertDate(System.currentTimeMillis(), DateandTimeutils.DATE_FORMAT_2);
        final File currentFrameImage = new File(framesFolder + File.separator + fileName + ".png");

        try {
            if (currentFrameImage.createNewFile()) {

                sourceLayout.buildDrawingCache(true);
                Bitmap bitmap = sourceLayout.getDrawingCache(true).copy(Bitmap.Config.ARGB_8888, false);
                sourceLayout.destroyDrawingCache();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                FileOutputStream fileOutputStream2 = new FileOutputStream(currentFrameImage);
                fileOutputStream2.write(byteArrayOutputStream.toByteArray());
                fileOutputStream2.close();
                ContentValues contentValues = storeAndGetImageMetaData(currentFrameImage);
                Uri result = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            } else {
                Log.e("FILEUTILS", "File Creation Failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentFrameImage;

    }

    @Deprecated
    public interface OnFinishListener {
        void onfinish(File file);

    }

    @Deprecated
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
            file = new File(Environment.getExternalStorageDirectory() + File.separator + "Quotzy" + File.separator + "QUOTES--" + System.currentTimeMillis() + ".jpg");
        } else {
            file1.mkdir();
            file = new File(Environment.getExternalStorageDirectory() + File.separator + "Quotzy" + File.separator + "QUOTES--" + System.currentTimeMillis() + ".jpg");
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


    @Deprecated
    public static Bitmap drawable_from_url(String url) throws java.io.IOException {

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-agent", "Mozilla/4.0");

        connection.connect();
        InputStream input = connection.getInputStream();

        return BitmapFactory.decodeStream(input);
    }


    @Deprecated
    private static void saveToShared(String folderName, Activity activity) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(activity).edit();
        editor.putString(Constants.TEMP_FILE_NAME_KEY, folderName);
        editor.apply();
    }

    @Deprecated
    @Nullable
    private static String getSavedShared(Activity activity) {
        return PreferenceManager.getDefaultSharedPreferences(activity).getString(Constants.TEMP_FILE_NAME_KEY, null);
    }
//    private static class FrameSavingTask extends AsyncTask<String, String, Void> {
//        int framecount;
//        Dialog dialog;
//        @SuppressLint("StaticFieldLeak")
//        Activity activity;
//        @SuppressLint("StaticFieldLeak")
//        ViewGroup sourceLayout;
//        private onSaveComplete listneer;
//        File file;
//        private static final int NOTIFY_ID = 5004;
//
//        private static final String CHANNEL_ID = "QUOTZY";
//        NotificationChannel channel;
//        androidx.core.app.NotificationCompat.Builder mNotification_Builder;
//        NotificationManager mNotificationManager;
//
//        int width;
//        int height;
//
//        FrameSavingTask(int framecount, Dialog dialog, Activity activity, ViewGroup sourceLayout, onSaveComplete listneer, int width, int height) {
//            this.framecount = framecount;
//            this.dialog = dialog;
//            this.activity = activity;
//            this.sourceLayout = sourceLayout;
//            this.listneer = listneer;
//            this.width = width;
//            this.height = height;
//        }
//
//        final ArrayList<File> savedFilesList = new ArrayList<>();
//
//        @SuppressLint("WrongThread")
//        @Override
//        protected Void doInBackground(String... strings) {
//            mNotificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
//            if (Build.VERSION.SDK_INT >= O) {
//                CharSequence name = "Gif Builder";
//                String description = activity.getString(R.string.notication_channel_desc);
//                int importance = NotificationManager.IMPORTANCE_HIGH;
//
//                channel = new NotificationChannel(CHANNEL_ID, name, importance);
//                channel.setDescription(description);
//                assert mNotificationManager != null;
//                mNotificationManager.createNotificationChannel(channel);
//                mNotification_Builder = new NotificationCompat.Builder(activity, CHANNEL_ID);
//                mNotification_Builder.setContentTitle("Building Gif images...").setContentText("Gif in progress")
//                        .setSmallIcon(R.drawable.ic_launcher)
//                        .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_launcher))
//                        .setBadgeIconType(R.drawable.ic_launcher)
//                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Gif in progress"))
//                        .setColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorAccent))
//                        .setPriority(NotificationCompat.PRIORITY_HIGH)
//                        .setProgress(0, 0, true).setAutoCancel(true).setOngoing(true);
//                mNotificationManager.notify(NOTIFY_ID, mNotification_Builder.build());
//
//            } else {
//                listneer.onImageSaveListener(null);
//                dialog.dismiss();
//                mNotification_Builder = new NotificationCompat.Builder(activity);
//                mNotification_Builder.setContentTitle("Building Gif images...")
//                        .setContentText("GIf in progress")
//                        .setSmallIcon(R.drawable.ic_launcher)
//                        .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_launcher))
//                        .setBadgeIconType(R.drawable.ic_launcher).setStyle(new NotificationCompat.BigTextStyle().bigText("Gif in progress"))
//                        .setColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorAccent))
//                        .setPriority(NotificationCompat.PRIORITY_HIGH).setProgress(0, 0, true)
//                        .setAutoCancel(true)
//                        .setOngoing(true);
//                mNotificationManager.notify(NOTIFY_ID, mNotification_Builder.build());
//
//            }
//            for (int i = 0; i < framecount; i++) {
////                Toast_Snack_Dialog_Utils.show_ShortToast(activity, String.valueOf(framecount * 10));
//                /*this should returns a value for every 100 milliseconds. so if there are 14 frames* then gif will be of length 1.4 sec/                             */
//                Objects.requireNonNull(savedFilesList).add(saveTempGifFrame(activity, sourceLayout, i));
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(83);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).run();
//            }
//
//            AnimatedGifEncoder animatedGifEncoder = new AnimatedGifEncoder();
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//
//            animatedGifEncoder.start(byteArrayOutputStream);
//            animatedGifEncoder.setDelay(83);
//            animatedGifEncoder.setFrameRate(12);
//            for (File currentFrame : savedFilesList) {
//                animatedGifEncoder.addFrame(BitmapFactory.decodeFile(currentFrame.getAbsoluteFile().getAbsolutePath()));
//            }
//            animatedGifEncoder.finish();
//            file = new File(Constants.APP_SAVED_PICTURES_FOLDER + File.separator
//                    + DateandTimeutils.convertDate(System.currentTimeMillis(), DateandTimeutils.DATE_FORMAT_2) + ".gif");
//            try {
//                FileOutputStream fileOutputStream = new FileOutputStream(file);
//                fileOutputStream.write(byteArrayOutputStream.toByteArray());
//                fileOutputStream.close();
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
////            GifEncoder gifEncoder = new GifEncoder();
////            try {
////                gifEncoder.init(width, height, file.getPath(), GifEncoder.EncodingType.ENCODING_TYPE_NORMAL_LOW_MEMORY);
////                for (File currentFrame : savedFilesList) {
////
////                    gifEncoder.encodeFrame(BitmapFactory.decodeFile(currentFrame.getPath()), 100);
////                }
////
////
////                gifEncoder.close();
////            } catch (FileNotFoundException e) {
////                e.printStackTrace();
////            }
//
//// Bitmap is MUST ARGB_8888.
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//
//
//            Intent intent_gallery = new Intent(Intent.ACTION_VIEW, FileProvider.getUriForFile(activity.getApplicationContext(), activity.getString(R.string.file_provider_authority), (file)));
//            intent_gallery.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), NOTIFY_ID, intent_gallery, PendingIntent.FLAG_ONE_SHOT);
//            mNotification_Builder.setContentTitle("Completed");
//            mNotification_Builder.setContentIntent(pendingIntent);
//            mNotification_Builder.setContentText("GIF Successfully downloaded to SD card").setProgress(100, 100, false);// Removes the progress bar
//            mNotification_Builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Click to open"));
//
//            mNotificationManager.notify(NOTIFY_ID, mNotification_Builder.build());
//
//            if (file != null) {
//                try {
//
//                    Intent intent = new Intent(WallpaperManager.getInstance(activity.getApplicationContext()).
//                            getCropAndSetWallpaperIntent(FileProvider.getUriForFile(activity.getApplicationContext(), "com.hustler.quote.fileprovider", (file))));
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    activity.startActivity(intent);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    FirebaseCrash.log(e.getMessage());
//                }
//
//            } else {
//                try {
//                    Toast_Snack_Dialog_Utils.show_ShortToast((Activity) activity.getApplicationContext(), "Failed to Show Image");
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            listneer.onImageSaveListener(file);
//            dialog.dismiss();
//        }
//
//    }

}
