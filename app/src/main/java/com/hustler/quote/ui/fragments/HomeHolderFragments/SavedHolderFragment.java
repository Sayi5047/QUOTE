package com.hustler.quote.ui.fragments.HomeHolderFragments;

import android.content.Context;
import android.net.Uri;
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

import com.hustler.quote.ui.apiRequestLauncher.Constants;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.hustler.quote.R;
import com.hustler.quote.ui.fragments.UserWorkFragment;
import com.hustler.quote.ui.utils.TextUtils;


public class SavedHolderFragment extends Fragment {


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

    public SavedHolderFragment() {
        // Required empty public constructor
    }


    @NonNull
    public static SavedHolderFragment newInstance(String param1, String param2) {
        SavedHolderFragment fragment = new SavedHolderFragment();
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
        fragmentTransaction.add(R.id.holderFrame, new UserWorkFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        rdBtn1.setText(getString(R.string.images));
        rdBtn2.setText(getString(R.string.wallpapers));
        rdBtn2.setVisibility(View.GONE);
        TextUtils.set_Radio_font(getActivity(), rdBtn1, Constants.FONT_CIRCULAR);
        TextUtils.set_Radio_font(getActivity(), rdBtn2, Constants.FONT_CIRCULAR);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

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
                fragmentTransaction.replace(R.id.holderFrame, new UserWorkFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.rd_btn2:
//                FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
//                fragmentTransaction2.setCustomAnimations(R.anim.slide_in_right, 0);
//                fragmentTransaction2.replace(R.id.holderFrame,  new UserWorkFragment();
//                fragmentTransaction2.addToBackStack(null);
//                fragmentTransaction2.commit();
                break;
        }
    }
}
