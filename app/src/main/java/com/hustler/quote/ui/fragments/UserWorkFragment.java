package com.hustler.quote.ui.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hustler.quote.R;
import com.hustler.quote.ui.adapters.UserWorkAdapter;
import com.hustler.quote.ui.pojo.UserWorkImages;
import com.hustler.quote.ui.utils.AnimUtils;
import com.hustler.quote.ui.utils.FileUtils;
import com.hustler.quote.ui.utils.PermissionUtils;
import com.hustler.quote.ui.utils.TextUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Sayi on 17-12-2017.
 */

public class UserWorkFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    private static final int MY_PERMISSION_REQUEST_ = 1001;
    RecyclerView rv;
    ProgressBar loader;
    UserWorkAdapter adapter;
    UserWorkImages userWorkImages;
    private LinearLayout dataView;
    private RelativeLayout quoteOfDayLl;
    private LinearLayout noPermissionView;
    private TextView imageText;
    private TextView message;
    UserWorkAdapter userWorkAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_work_fragment, container, false);
        rv = (RecyclerView) view.findViewById(R.id.main_rv);
        loader = (ProgressBar) view.findViewById(R.id.loader);
        dataView = (LinearLayout) view.findViewById(R.id.data_view);
        noPermissionView = (LinearLayout) view.findViewById(R.id.no_permission_view);
        imageText = (TextView) view.findViewById(R.id.image_text);
        message = (TextView) view.findViewById(R.id.message);
        loader.setVisibility(View.GONE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                checkPermission_and_proceed();
            }
        }).run();
        return view;
    }

    private void checkPermission_and_proceed() {
        if (PermissionUtils.isPermissionAvailable(getActivity())) {
            noPermissionView.setVisibility(View.GONE);
            dataView.setVisibility(View.VISIBLE);
            setRecyclerview();
        } else {
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST_);
        }
    }


    private void setRecyclerview() {
        userWorkImages = FileUtils.getImagesFromSdCard();
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        if (userWorkImages == null) {
            dataView.setVisibility(View.GONE);
            noPermissionView.setVisibility(View.VISIBLE);
            message.setText(getActivity().getString(R.string.no_work));
        }
        userWorkAdapter = new UserWorkAdapter(getActivity(), userWorkImages.getImagesPaths(), userWorkImages.getImageNames(), new UserWorkAdapter.OnImageClickListner() {
            @Override
            public void onImageClickListneer(int position, String imageName, String imagepath) {
                try {
                    android.support.media.ExifInterface exifInterface = new android.support.media.ExifInterface(imagepath);
                    buildDialog(userWorkImages.getImagesPaths().length, position, userWorkAdapter, rv, exifInterface, imageName, imagepath);
                    Log.d("xval", exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH) + "");
                    Log.d("yval", exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH) + "");
                    Log.d("date", exifInterface.getAttribute(ExifInterface.TAG_DATETIME) + "");
                    Log.d("date", exifInterface.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION) + "");
                    ;
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.image_unavailable));
                }
            }
        });

        rv.setAdapter(userWorkAdapter);
        rv.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.slideup));
    }

    private void buildDialog(final int count, final int position, final UserWorkAdapter userWorkAdapter, final RecyclerView rv, android.support.media.ExifInterface exifInterface, String imageName, final String imagepath) {
        final Dialog dialog = new Dialog(getActivity(), R.style.EditTextDialog_non_floater);
        dialog.setContentView(R.layout.user_work_show_item);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog_non_floater;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE);
        final RelativeLayout rootLayout;
        final TextView header;
        ImageView closeIv;
        ImageView ivWork;
        FloatingActionButton fabDelete;
        FloatingActionButton fabWallpaper;
        FloatingActionButton fabShare;
        TextView tvDelete;
        TextView tvWallpaper;
        TextView tvShare;
        TextView metaDataDateTv;
        TextView metaDataResolutionTv;
        TextView metaFileLocation, metaImageSize;
        final ArrayList<Palette.Swatch> swatches = new ArrayList<>();

        rootLayout = (RelativeLayout) dialog.findViewById(R.id.root_layout);
        header = (TextView) dialog.findViewById(R.id.header);
        closeIv = (ImageView) dialog.findViewById(R.id.close_iv);
        ivWork = (ImageView) dialog.findViewById(R.id.iv_work);
        fabDelete = (FloatingActionButton) dialog.findViewById(R.id.fab_delete);
        fabWallpaper = (FloatingActionButton) dialog.findViewById(R.id.fab_wallpaper);
        fabShare = (FloatingActionButton) dialog.findViewById(R.id.fab_share);
        tvDelete = (TextView) dialog.findViewById(R.id.tv_delete);
        tvWallpaper = (TextView) dialog.findViewById(R.id.tv_wallpaper);
        tvShare = (TextView) dialog.findViewById(R.id.tv_share);
        metaDataDateTv = (TextView) dialog.findViewById(R.id.meta_data_date_tv);
        metaDataResolutionTv = (TextView) dialog.findViewById(R.id.meta_data_resolution_tv);
        metaFileLocation = (TextView) dialog.findViewById(R.id.meta_data_location_tv);
        metaImageSize = (TextView) dialog.findViewById(R.id.meta_data_size_tv);


        TextUtils.findText_and_applyTypeface(rootLayout, getActivity());
//        TextUtils.findText_and_applyamim_slideup(rootLayout,getActivity());

        header.setText(imageName);

        Glide.with(getActivity()).load(imagepath).asBitmap().fitCenter().crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(ivWork);

        Palette.from(BitmapFactory.decodeFile(imagepath)).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                swatches.add(0, palette.getVibrantSwatch());
                if (swatches.get(0) == null) {
                    swatches.set(0, palette.getDominantSwatch());
                }
                TextUtils.findText_and_applycolor(rootLayout, getActivity(), swatches.get(0));
                header.setBackgroundColor(swatches.get(0).getRgb());
                header.setTextColor(getActivity().getResources().getColor(android.R.color.white));

            }
        });


        metaDataDateTv.setText(exifInterface.getAttribute(android.support.media.ExifInterface.TAG_DATETIME));
        metaDataResolutionTv.setText(getActivity().getString(
                R.string.Resolution,
                exifInterface.getAttributeInt(android.support.media.ExifInterface.TAG_IMAGE_WIDTH, 0),
                exifInterface.getAttributeInt(android.support.media.ExifInterface.TAG_IMAGE_LENGTH, 0)));
        metaFileLocation.setText(getActivity().getString(R.string.Path, imagepath));
        metaImageSize.setText(getActivity().getString(R.string.size, new File(imagepath).length() / 1024));
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagepath != null) {
                    Toast_Snack_Dialog_Utils.createDialog(getActivity(),
                            getString(R.string.are_you_sure),
                            getString(R.string.this_will_delete),
                            getString(R.string.cancel),
                            getString(R.string.delete),
                            new Toast_Snack_Dialog_Utils.Alertdialoglistener() {
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
                FileUtils.setwallpaper(getActivity(), imagepath);
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
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_BACK){
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message: {
                checkPermission_and_proceed();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        rv.setAdapter(null);
        checkPermission_and_proceed();
    }
}
