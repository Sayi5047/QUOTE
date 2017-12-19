package com.hustler.quote.ui.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.adapters.UserWorkAdapter;
import com.hustler.quote.ui.pojo.UserWorkImages;
import com.hustler.quote.ui.superclasses.App;
import com.hustler.quote.ui.utils.FileUtils;
import com.hustler.quote.ui.utils.PermissionUtils;

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
        checkPermission_and_proceed();
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
        if(userWorkImages==null){
            dataView.setVisibility(View.GONE);
            noPermissionView.setVisibility(View.VISIBLE);
            message.setText(getActivity().getString(R.string.no_work));
        }
        rv.setAdapter(new UserWorkAdapter(getActivity(), userWorkImages.getImagesPaths(), userWorkImages.getImageNames(), new UserWorkAdapter.OnImageClickListner() {
            @Override
            public void onImageClickListneer(String imageName, String imagepath) {
                App.showToast(getActivity(), imageName + "  " + imagepath);
                buildDialog();
            }
        }));
    }

    private void buildDialog() {
        Dialog dialog = new Dialog(getActivity(),R.style.EditTextDialog);
        dialog.setContentView(R.layout.user_work_show_item);
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
        switch (v.getId()){
            case R.id.message:{
                checkPermission_and_proceed();
            }
        }
    }
}
