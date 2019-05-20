package io.hustler.qtzy.ui.fragments.HomeHolderFragments;

import android.content.Context;
import android.os.Bundle;
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
import io.hustler.qtzy.ui.fragments.FAV_images_fragment;
import io.hustler.qtzy.ui.fragments.FAV_quotes_fragment;


public class FavouritesHolderFragment extends Fragment {


    @BindView(R.id.holderFrame)
    FrameLayout holderFrame;
    @BindView(R.id.rd_btn1)
    RadioButton rdBtn1;
    @BindView(R.id.rd_btn2)
    RadioButton rdBtn2;
    Unbinder unbinder;
    FragmentManager fragmentManager;

    public FavouritesHolderFragment() {
    }


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
    public void onViewClicked(View view) {
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
