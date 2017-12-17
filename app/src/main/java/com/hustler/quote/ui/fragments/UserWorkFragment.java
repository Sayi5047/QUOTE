package com.hustler.quote.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hustler.quote.R;
import com.hustler.quote.ui.adapters.MainAdapter;
import com.hustler.quote.ui.adapters.UserWorkAdapter;
import com.hustler.quote.ui.pojo.UserWorkImages;
import com.hustler.quote.ui.superclasses.App;
import com.hustler.quote.ui.utils.FileUtils;

/**
 * Created by Sayi on 17-12-2017.
 */

public class UserWorkFragment extends android.support.v4.app.Fragment {
    RecyclerView rv;
    ProgressBar loader;
    UserWorkAdapter adapter;
    UserWorkImages userWorkImages;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.mainfragmentlayout,container,false);
        rv = (RecyclerView) view.findViewById(R.id.main_rv);
        loader =(ProgressBar) view.findViewById(R.id.loader);
        loader.setVisibility(View.GONE);
        userWorkImages= FileUtils.getImagesFromSdCard();
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rv.setAdapter(new UserWorkAdapter(getActivity(), userWorkImages.getImagesPaths(), userWorkImages.getImageNames(), new UserWorkAdapter.OnImageClickListner() {
            @Override
            public void onImageClickListneer(String imageName, String imagepath) {
                App.showToast(getActivity(),imageName+"  "+imagepath);
            }
        }));
        return view;
    }
}
