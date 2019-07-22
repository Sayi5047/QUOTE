package io.hustler.qtzy.ui.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.activities.EditorActivity;
import io.hustler.qtzy.ui.adapters.UserWorkAdapter;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;
import io.hustler.qtzy.ui.pojo.UserWorkImages;
import io.hustler.qtzy.ui.utils.AdUtils;
import io.hustler.qtzy.ui.utils.FileUtils;
import io.hustler.qtzy.ui.utils.PermissionUtils;
import io.hustler.qtzy.ui.utils.TextUtils;
import io.hustler.qtzy.ui.utils.Toast_Snack_Dialog_Utils;

/**
 * Created by Sayi on 17-12-2017.
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
public class UserWorkFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    private static final int MY_PERMISSION_REQUEST_ = 1001;
    RecyclerView rv;
    ProgressBar loader;
    UserWorkAdapter adapter;
    @Nullable
    UserWorkImages userWorkImages;
    private LinearLayout dataView;
    private RelativeLayout quoteOfDayLl;
    private LinearLayout noPermissionView;
    private ImageView imageText;
    private TextView message;
    UserWorkAdapter userWorkAdapter;

    public UserWorkFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_work_fragment, container, false);
        rv = view.findViewById(R.id.main_rv);
        loader = view.findViewById(R.id.loader);
        dataView = view.findViewById(R.id.data_view);
        noPermissionView = view.findViewById(R.id.no_permission_view);
        imageText = view.findViewById(R.id.image_text);
        message = view.findViewById(R.id.message);
        loader.setVisibility(View.GONE);
        checkPermission_and_proceed();
        TextUtils.findText_and_applyTypeface(noPermissionView, getActivity());
        return view;

    }

    private void checkPermission_and_proceed() {
        if (PermissionUtils.isPermissionAvailable(Objects.requireNonNull(getActivity()))) {
            noPermissionView.setVisibility(View.GONE);
            dataView.setVisibility(View.VISIBLE);
            setRecyclerview();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_);
        }
    }


    private void setRecyclerview() {
        rv.setLayoutManager(new LinearLayoutManager(Objects.requireNonNull(getActivity()).getApplicationContext(), LinearLayoutManager.VERTICAL, true));
        userWorkImages = FileUtils.getImagesFromSdCard(getActivity());
        setUpAdapter();
//        new GetUserImagesTask().execute();
    }

    private void setUpAdapter() {
        if (userWorkImages == null || userWorkImages.getImageNames().length <= 0 || userWorkImages.getImagesPaths().length <= 0) {
            dataView.setVisibility(View.GONE);
            noPermissionView.setVisibility(View.VISIBLE);
            message.setText(Objects.requireNonNull(getActivity()).getString(R.string.no_work));
            noPermissionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EditorActivity.class);
                    intent.putExtra(Constants.INTENT_IS_FROM_EDIT_KEY, 1);
                    startActivity(intent);
                }
            });
        } else {
            userWorkAdapter = new UserWorkAdapter(getActivity(), userWorkImages.getImagesPaths(), userWorkImages.getImageNames(), new UserWorkAdapter.OnImageClickListner() {
                @Override
                public void onImageClickListneer(int position, String imageName, @NonNull String imagepath) {
                    try {
                        android.support.media.ExifInterface exifInterface = new android.support.media.ExifInterface(imagepath);
                        buildDialog(userWorkImages.getImagesPaths().length, rv, exifInterface, imageName, imagepath);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.image_unavailable));
                    }
                }
            });

            rv.setAdapter(userWorkAdapter);

        }
    }

    private void buildDialog(final int count, @NonNull final RecyclerView rv, android.support.media.ExifInterface exifInterface, String imageName, @NonNull final String imagepath) {
        final Dialog dialog = new Dialog(getActivity(), R.style.EditTextDialog_non_floater);
        dialog.setContentView(View.inflate(getActivity().getApplicationContext(), R.layout.user_work_show_item, null));
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog_non_floater;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_rounded_drawable);

        final RelativeLayout rootLayout;
        final TextView header;
        ImageView closeIv;
        ImageView ivWork;
        AdView adView2;
        FloatingActionButton fabDelete;
        FloatingActionButton fabWallpaper;
        FloatingActionButton fabShare;
        TextView metaDataDateTv;
        TextView metaDataResolutionTv;
        TextView metaFileLocation, metaImageSize;
        final ArrayList<Palette.Swatch> swatches = new ArrayList<>();

        rootLayout = dialog.findViewById(R.id.root_layout);
        header = dialog.findViewById(R.id.header);
        closeIv = dialog.findViewById(R.id.close_iv);
        ivWork = dialog.findViewById(R.id.iv_work);
        fabDelete = dialog.findViewById(R.id.fab_delete);
        fabWallpaper = dialog.findViewById(R.id.fab_wallpaper);
        fabShare = dialog.findViewById(R.id.fab_share);
        metaDataDateTv = dialog.findViewById(R.id.meta_data_date_tv);
        metaDataResolutionTv = dialog.findViewById(R.id.meta_data_resolution_tv);
        metaFileLocation = dialog.findViewById(R.id.meta_data_location_tv);
        metaImageSize = dialog.findViewById(R.id.meta_data_size_tv);

        adView2 = dialog.findViewById(R.id.adView2);


        AdUtils.loadBannerAd(adView2, getActivity());

        TextUtils.findText_and_applyTypeface(rootLayout, getActivity());

        header.setText(imageName);

        Glide.with(getActivity()).load(imagepath).asBitmap().fitCenter().crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivWork);

        Palette.from(BitmapFactory.decodeFile(imagepath)).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@NonNull Palette palette) {
                swatches.add(0, palette.getVibrantSwatch());
                if (swatches.get(0) == null) {
                    swatches.set(0, palette.getDominantSwatch());
                }
                TextUtils.findText_and_applycolor(rootLayout, getActivity(), swatches.get(0));
                if (null != swatches.get(0)) {
                    header.setBackgroundColor(swatches.get(0).getRgb());
                    header.setTextColor(getActivity().getResources().getColor(android.R.color.white));
                    dialog.getWindow().setStatusBarColor(swatches.get(0).getRgb());

                }

            }
        });


        metaDataDateTv.setText(exifInterface.getAttribute(android.support.media.ExifInterface.TAG_DATETIME));
        metaDataResolutionTv.setText(getActivity().getString(R.string.Resolution, exifInterface.getAttributeInt(android.support.media.ExifInterface.TAG_IMAGE_WIDTH, 0), exifInterface.getAttributeInt(android.support.media.ExifInterface.TAG_IMAGE_LENGTH, 0)));
        metaFileLocation.setText(getActivity().getString(R.string.Path, imagepath));
        metaImageSize.setText(getActivity().getString(R.string.size, new File(imagepath).length() / 1024));
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagepath != null) {
                    Toast_Snack_Dialog_Utils.createDialog(getActivity(), getString(R.string.are_you_sure), getString(R.string.this_will_delete), getString(R.string.cancel), getString(R.string.delete), new Toast_Snack_Dialog_Utils.Alertdialoglistener() {
                        @Override
                        public void onPositiveselection() {
                            File file = new File(imagepath);
                            file.delete();
                            Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.deleted));
                            rv.setAdapter(null);
                            setRecyclerview();
//                                    rv.invalidate();
//                                    userWorkAdapter.updateData(position);
////                                    rv.removeViewAt(position);
//                                    userWorkAdapter.notifyItemRemoved(position);
//                                    userWorkAdapter.notifyItemRangeChanged(position,userWorkImages.getImagesPaths().length);
//                                    userWorkAdapter.notifyDataSetChanged();
                            dialog.dismiss();


                        }

                        @Override
                        public void onNegativeSelection() {

                        }
                    });
                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.unable));
                }
            }
        });
        fabWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtils.setwallpaper(Objects.requireNonNull(getActivity()).getApplicationContext(), imagepath);
            }
        });
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtils.shareImage(getActivity(), imagepath);
            }
        });
        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(@NonNull DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    noPermissionView.setVisibility(View.GONE);
                    dataView.setVisibility(View.VISIBLE);
                    setRecyclerview();
                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    dataView.setVisibility(View.GONE);
                    noPermissionView.setVisibility(View.VISIBLE);
                    message.setText(getActivity().getString(R.string.no_permission));
                    message.setOnClickListener(this);
                }
            }
        }
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.message: {
                checkPermission_and_proceed();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPermission_and_proceed();
    }

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
    public class GetUserImagesTask extends AsyncTask<String, String, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param strings The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */


        @Nullable
        @Override
        protected Void doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setUpAdapter();
            super.onPostExecute(aVoid);

        }
    }
}
