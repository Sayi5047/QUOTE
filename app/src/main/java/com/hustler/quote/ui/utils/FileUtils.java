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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.media.ExifInterface;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.hustler.quote.R;
import com.hustler.quote.ui.adapters.InstallFontAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.pojo.UserWorkImages;
import com.hustler.quote.ui.textFeatures.TextFeatures;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
    static String folderName = null;

    public static void unzipandSave(File file, final Activity activity) {
        int Buffer = 2048;
        File sourcezipLocation = file;
        File destinationUnZipLocation;
        final List<String> zipContents = new ArrayList<>();
        String fontsUnZipPath = sourcezipLocation.getAbsolutePath();
        try {
            Log.d("ZIP Path", Environment.getExternalStorageDirectory() + sourcezipLocation.getAbsolutePath());
            Log.d("ZIP Path", sourcezipLocation.getAbsolutePath());
            ZipFile zipToRead = new ZipFile(sourcezipLocation.getAbsolutePath());

            Enumeration zipEntries = zipToRead.entries();
            while (zipEntries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) zipEntries.nextElement();
                String currentEntry = entry.getName();

//                Toast_Snack_Dialog_Utils.show_ShortToast(activity, currentEntry);
                Log.d("ZIP LOCATIONS", currentEntry);
                if (currentEntry.endsWith(".ttf")) {
                    zipContents.add(currentEntry);
                } else if (currentEntry.endsWith(".otf")) {
                    zipContents.add(currentEntry);

                } else if (currentEntry.endsWith(".TTF")) {
                    zipContents.add(currentEntry);

                } else if (currentEntry.endsWith(".OTF")) {
                    zipContents.add(currentEntry);

                } else {
                }
            }

            String targetLocationPath = Environment.getExternalStorageDirectory() + File.separator + "Fonts";
            String tempTargetLocationPath = Environment.getExternalStorageDirectory() + File.separator + activity.getString(R.string.Temp);
            final String temporarly_Saved_fonts_Path;

            File targetLocationFile = null;
            File tempTargetLocationFile = null;
            targetLocationFile = new File(targetLocationPath);
            tempTargetLocationFile = new File(tempTargetLocationPath);
            if (tempTargetLocationFile.isDirectory()) {
                temporarly_Saved_fonts_Path = doUnZIP(activity, sourcezipLocation, tempTargetLocationPath);

            } else {
                tempTargetLocationFile.mkdirs();
                temporarly_Saved_fonts_Path = doUnZIP(activity, sourcezipLocation, tempTargetLocationPath);
            }

            final Dialog dialog = new Dialog(activity, R.style.EditTextDialog);
            dialog.setContentView(View.inflate(activity, R.layout.install_fonts_layout, null));
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);
            dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
            dialog.setCancelable(false);
            TextView headTv;
            final EditText etProjectName;
            LinearLayout btLlLayout;
            RecyclerView recyclerView;
            LinearLayout root = null;
            Button btClose, btInstall;
            AdView adView = null;
            recyclerView = dialog.findViewById(R.id.font_recycler);
            headTv = dialog.findViewById(R.id.head_tv);
            etProjectName = dialog.findViewById(R.id.et_project_name);
            btLlLayout = dialog.findViewById(R.id.bt_ll_layout);
            root = dialog.findViewById(R.id.root_Lo);
            btClose = dialog.findViewById(R.id.bt_close);
            btInstall = dialog.findViewById(R.id.bt_save);
            adView = dialog.findViewById(R.id.adView);
            AdUtils.loadBannerAd(adView, activity);

            recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            final String finalLOcation = Environment.getExternalStorageDirectory() + File.separator + activity.getString(R.string.Quotzy) + File.separator + activity.getString(R.string.Fonts);

            final String[] source_font_paths_list = TextFeatures.getDownloadedFonts(activity, new File(temporarly_Saved_fonts_Path));

            recyclerView.setAdapter(new InstallFontAdapter(activity, zipContents, temporarly_Saved_fonts_Path, finalLOcation, source_font_paths_list));
            dialog.show();
            btInstall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProgressDialog dialog1 = new ProgressDialog(activity);
                    dialog1.setTitle(activity.getString(R.string.installing_Fonts));
                    dialog1.show();
                    for (int i = 0; i < zipContents.size(); i++) {
                        new File(source_font_paths_list[i]).renameTo(new File(finalLOcation + File.separator + zipContents.get(i)));
                    }
                    dialog1.cancel();
                    Toast_Snack_Dialog_Utils.createDialog(activity,
                            activity.getString(R.string.congratulations),
                            activity.getString(R.string.font_installed)
                            , null, activity.getString(R.string.close), new Toast_Snack_Dialog_Utils.Alertdialoglistener() {
                                @Override
                                public void onPositiveselection() {
                                }

                                @Override
                                public void onNegativeSelection() {

                                }
                            });
                    dialog.dismiss();
                }
            });
            btClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
                        dialog.dismiss();
                        return true;
                    } else {
                        return false;
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static String doUnZIP(final Activity activity, final File sourcezipLocation, final String tempTargetLocationPath) {

        try {
//            TEMPORARY FILES
            FileInputStream fileInputStreamtemp = new FileInputStream(sourcezipLocation);
            ZipInputStream zipInputStreamtemp = new ZipInputStream(fileInputStreamtemp);
//
            ZipEntry zipEntry = null;
// TO CREATE A SINGLE FOLDER WITH FOLDER NAME
            String firstZipEntryName = zipInputStreamtemp.getNextEntry().getName();
            zipInputStreamtemp.closeEntry();


            Log.e("ZIP ENTRY NAME", firstZipEntryName);

            String newFolder = tempTargetLocationPath + File.separator + firstZipEntryName.substring(0, firstZipEntryName.length() - 4);
            File tragetUnzippingFolder = new File(newFolder);
            if (tragetUnzippingFolder.isDirectory() == true) {

            } else {
                tragetUnzippingFolder.mkdirs();
            }

            FileInputStream fileInputStream = new FileInputStream(sourcezipLocation);
            ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                File newFile = new File(newFolder + File.separator + zipEntry.getName());
                Log.e(" New File ", newFile.getAbsolutePath());
                FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[2048];
                int count;
                while ((count = zipInputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, count);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    fileOutputStream.write(bytes);
                    byteArrayOutputStream.reset();
                }
                zipInputStream.closeEntry();
                fileOutputStream.close();

            }


            zipInputStream.close();
            zipInputStreamtemp.close();
            return newFolder;
        } catch (IOException ie) {
            ie.printStackTrace();
            Log.e("EXCEPTION CAUGHT", ie.getMessage());
            return null;
        }


    }

    private static void saveToShared(String folderName, Activity activity) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(activity).edit();
        editor.putString(Constants.TEMP_FILE_NAME_KEY, folderName);
        editor.apply();
    }

    private static String getSavedShared(Activity activity) {
        return PreferenceManager.getDefaultSharedPreferences(activity).getString(Constants.TEMP_FILE_NAME_KEY, null);
    }

    public interface onSaveComplete {
        void onImageSaveListner(File file);
    }

    public static void savetoDevice(final ViewGroup layout, final Activity activity, final onSaveComplete listneer) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String[] name = {null};
                final String[] format = {null};
                final File[] filetoReturn = new File[1];
                final String[] projectname = new String[1];
                AdView adView;
                final Dialog dialog = new Dialog(activity, R.style.EditTextDialog);
                dialog.setContentView(View.inflate(activity, R.layout.save_image_layout, null));
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);
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


                headTv = dialog.findViewById(R.id.head_tv);
                etProjectName = dialog.findViewById(R.id.et_project_name);
                rdGroup = dialog.findViewById(R.id.rd_group);
                rbJpeg = dialog.findViewById(R.id.rb_jpeg);
                rbPng = dialog.findViewById(R.id.rb_png);
                btLlLayout = dialog.findViewById(R.id.bt_ll_layout);
                root = dialog.findViewById(R.id.root_Lo);
                btClose = dialog.findViewById(R.id.bt_close);
                btSave = dialog.findViewById(R.id.bt_save);
                adView = dialog.findViewById(R.id.adView);
                AdUtils.loadBannerAd(adView, activity);
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

                            directoryChecker = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getString(R.string.Quotzy) + File.separator + activity.getString(R.string.images));
                            if (format[0].equalsIgnoreCase(Constants.JPEG)) {
                                if (directoryChecker.isDirectory()) {
                                    savingFile = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getString(R.string.Quotzy) + File.separator + activity.getString(R.string.images) +
                                            File.separator + projectname[0] + ".jpg");
                                } else {
                                    directoryChecker.mkdirs();
                                    savingFile = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getString(R.string.Quotzy) + File.separator + activity.getString(R.string.images) +
                                            File.separator + projectname[0] + ".jpg");
                                }

                            } else {
                                if (directoryChecker.isDirectory()) {
                                    savingFile = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getString(R.string.Quotzy) + File.separator + activity.getString(R.string.images) +
                                            File.separator + projectname[0] + ".png");
                                } else {
                                    directoryChecker.mkdir();
                                    savingFile = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getString(R.string.Quotzy) + File.separator + activity.getString(R.string.images) +
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
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
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

    // Will implement once I reach more than 1000 users.
    public static void savetoDeviceWithAds(final ViewGroup layout, final Activity activity, final onSaveComplete listneer) {
        final String[] name = {null};
        final String[] format = {null};
        final File[] filetoReturn = new File[1];
        final String[] projectname = new String[1];
        AdView adView;
        final Dialog dialog = new Dialog(activity, R.style.EditTextDialog);
        dialog.setContentView(View.inflate(activity, R.layout.save_image_layout_ads, null));
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
        dialog.setCancelable(false);

        TextView headTv;
        final EditText etProjectName;
        final RadioGroup rdGroup;
        final RadioButton rbJpeg;
        final RadioButton rbPng;
        final LinearLayout btLlLayout, adLayout;
        LinearLayout root;
        final RewardedVideoAd mRewardedVideoAd;
        Button btClose, btSave, remove_watermark, watch_ad, buy_pro;


        headTv = dialog.findViewById(R.id.head_tv);
        etProjectName = dialog.findViewById(R.id.et_project_name);
        rdGroup = dialog.findViewById(R.id.rd_group);
        rbJpeg = dialog.findViewById(R.id.rb_jpeg);
        rbPng = dialog.findViewById(R.id.rb_png);
        btLlLayout = dialog.findViewById(R.id.bt_ll_layout);
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
            public void onRewarded(RewardItem rewardItem) {
                Toast.makeText(activity, "onRewarded! currency: " + rewardItem.getType() + "  amount: " + rewardItem.getAmount(), Toast.LENGTH_SHORT).show();
                layout.findViewById(R.id.mark_quotzy_tv).setVisibility(View.GONE);
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

        remove_watermark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adLayout.setVisibility(View.VISIBLE);
            }
        });
        watch_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(mRewardedVideoAd.isLoaded()){
                mRewardedVideoAd.show();

            }
        });
        buy_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 07-01-2018 need to implement the PRO VERSION OF APP LINK
            }
        });
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

    // METHOD TOOK FROM INTERNET
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

    public static UserWorkImages getImagesFromSdCard(Activity activity) {
        File file;
        String[] imagePaths;
        String[] imageNames;
        File[] files;

        file = new File(Environment.getExternalStorageDirectory() + File.separator + "Quotzy" + File.separator + activity.getString(R.string.images));
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
            file.mkdirs();
            return null;
        }

    }

    public static void setwallpaper(Activity activity, String imagepath) {
        Intent intent = new Intent(WallpaperManager.getInstance(activity).
                getCropAndSetWallpaperIntent(FileUtils.getImageContentUri(activity, new File(imagepath))));

        activity.startActivity(intent);
    }

    public static void shareImage(Activity activity, String imagePath) {
        if (activity != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, imagePath);
            shareIntent.putExtra(Intent.EXTRA_TITLE, imagePath);
            if (imagePath != null) {
                if (Build.VERSION.SDK_INT >= 24) {
                    shareIntent.putExtra(Intent.EXTRA_STREAM,
                            FileProvider.getUriForFile(activity.getApplicationContext(),  activity.getString(R.string.file_provider_authority), new File(imagePath)));
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


    public static File downloadImageToSd_Card(String param, String download_image_name) {

        File downloading_File;
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;

        try {
//                GET URL
            URL url = new URL(param);
//                CRETAE DIRECTORY IN SD CARD WITH GIVEN NAME
            String SdCard = Environment.getExternalStorageDirectory().toString();
            File destination_downloading_directory = new File(SdCard + File.separator + Constants.APPFOLDER + Constants.Wallpapers);
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
                int bufferLength = 0;
                int manoj = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {

                    fileOutputStream.write(buffer, 0, bufferLength);
                    manoj++;

                }
                inputStream.close();
                fileOutputStream.close();
                Log.d("IMAGE SAVED", "Image Saved in sd card");
                return downloading_File;
            } catch (IOException e) {
                inputStream = null;
                fileOutputStream = null;
                e.printStackTrace();
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }


    }


}
