package com.hustler.quote.ui.fragments.HomeHolderFragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.hustler.quote.R;
import com.hustler.quote.ui.fragments.FAV_images_fragment;
import com.hustler.quote.ui.fragments.FAV_quotes_fragment;
import com.hustler.quote.ui.utils.TextUtils;

import static com.hustler.quote.ui.apiRequestLauncher.Constants.FONT_CIRCULAR;


public class FavouritesHolderFragment extends Fragment {


    @Nullable
    @BindView(R.id.holderFrame)
    FrameLayout holderFrame;
    @Nullable
    @BindView(R.id.rd_btn1)
    RadioButton rdBtn1;
    @Nullable
    @BindView(R.id.rd_btn2)
    RadioButton rdBtn2;
    Unbinder unbinder;
    FragmentManager fragmentManager;

    public FavouritesHolderFragment() {
    }


    @NonNull
    public static FavouritesHolderFragment newInstance(String param1, String param2) {
        FavouritesHolderFragment fragment = new FavouritesHolderFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quotes_holder, container, false);
        unbinder = ButterKnife.bind(this, view);
        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.holderFrame, new FAV_images_fragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        rdBtn1.setText(getString(R.string.Wallpaper));
        rdBtn2.setText(getString(R.string.Quotes));
        TextUtils.set_Radio_font(getActivity(),rdBtn1,FONT_CIRCULAR);
        TextUtils.set_Radio_font(getActivity(),rdBtn2,FONT_CIRCULAR);
        rdBtn2.setVisibility(View.VISIBLE);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rd_btn1, R.id.rd_btn2})
    public void onViewClicked(@NonNull View view) {
        switch (view.getId()) {
            case R.id.rd_btn1:
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, 0);
                fragmentTransaction.replace(R.id.holderFrame, new FAV_images_fragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.rd_btn2:
                FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                fragmentTransaction2.setCustomAnimations(R.anim.slide_in_right, 0);
                fragmentTransaction2.replace(R.id.holderFrame, new FAV_quotes_fragment());
                fragmentTransaction2.addToBackStack(null);
                fragmentTransaction2.commit();
                break;
        }
    }
}
