package io.hustler.qtzy.ui.fragments.HomeHolderFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.fragments.Categoris_wallpaper_fragment;
import io.hustler.qtzy.ui.fragments.WallpaperFragment;
import io.hustler.qtzy.ui.utils.TextUtils;

import static io.hustler.qtzy.ui.apiRequestLauncher.Constants.FONT_CIRCULAR;


public class WallpapersHolderFragment extends Fragment {


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

    public WallpapersHolderFragment() {
    }


    @NonNull
    public static WallpapersHolderFragment newInstance(String param1, String param2) {
        WallpapersHolderFragment fragment = new WallpapersHolderFragment();
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
//        rdBtn1.performClick();
        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.holderFrame, new WallpaperFragment());

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        rdBtn1.setText(getString(R.string.Wallpaper));
        rdBtn2.setText(getString(R.string.categories));
        TextUtils.set_Radio_font(getActivity(),rdBtn1,FONT_CIRCULAR);
        TextUtils.set_Radio_font(getActivity(),rdBtn2,FONT_CIRCULAR);
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
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,0);
                fragmentTransaction.add(R.id.holderFrame, new WallpaperFragment());

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.rd_btn2:
                FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                fragmentTransaction2.setCustomAnimations(R.anim.slide_in_right, 0);
                fragmentTransaction2.replace(R.id.holderFrame, new Categoris_wallpaper_fragment());

                fragmentTransaction2.addToBackStack(null);
                fragmentTransaction2.commit();
                break;
        }
    }

}
