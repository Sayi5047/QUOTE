package io.hustler.qtzy.ui.fragments.HomeHolderFragments;

import android.animation.ValueAnimator;
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
import io.hustler.qtzy.ui.activities.QuotesFragment;
import io.hustler.qtzy.ui.fragments.CategoriesFragment;
import io.hustler.qtzy.ui.utils.TextUtils;

import static io.hustler.qtzy.ui.apiRequestLauncher.Constants.FONT_CIRCULAR;


public class QuotesHolderFragment extends Fragment {


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

    final float bigSize = 18;
    final float endSize = 10;
    final short animationDuration = 600;
    ValueAnimator r1DecreaseAnimator, r2DecreaseAnimator;
    ValueAnimator r1IncreaseAnimator, r2IncreaseAnimator;


    public QuotesHolderFragment() {
        // Required empty public constructor
    }


    @NonNull
    public static QuotesHolderFragment newInstance() {
        QuotesHolderFragment fragment = new QuotesHolderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quotes_holder, container, false);

        unbinder = ButterKnife.bind(this, view);
//        rdBtn1.performClick();
        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.holderFrame, new QuotesFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        setValueAnimationsForButtons();
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
                if (!(fragmentManager.findFragmentById(R.id.holderFrame) instanceof QuotesFragment)) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, 0);

                    fragmentTransaction.replace(R.id.holderFrame, new QuotesFragment());
                    fragmentTransaction.addToBackStack(null);

                    fragmentTransaction.commit();
//                    if (rdBtn2.getTextSize() == bigSize) {
//                        r2DecreaseAnimator.start();
//                    }
//                    r1IncreaseAnimator.start();


                }
                break;

            case R.id.rd_btn2:
                if (!(fragmentManager.findFragmentById(R.id.holderFrame) instanceof CategoriesFragment)) {


                    FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();
                    fragmentTransaction2.setCustomAnimations(R.anim.slide_in_right, 0);

                    fragmentTransaction2.replace(R.id.holderFrame, new CategoriesFragment());
                    fragmentTransaction2.addToBackStack(null);
                    fragmentTransaction2.commit();
//                    if (rdBtn1.getTextSize() == bigSize) {
//                        r1DecreaseAnimator.start();
//                    }
//                    r2IncreaseAnimator.start();

                }
                break;

        }

    }


    private void setValueAnimationsForButtons() {
        r1DecreaseAnimator = ValueAnimator.ofFloat(bigSize, endSize);
        r2DecreaseAnimator = ValueAnimator.ofFloat(bigSize, endSize);

        r1IncreaseAnimator = ValueAnimator.ofFloat(endSize, bigSize);
        r2IncreaseAnimator = ValueAnimator.ofFloat(endSize, bigSize);

        r2IncreaseAnimator.setDuration(animationDuration);
        r2DecreaseAnimator.setDuration(animationDuration);
        r1IncreaseAnimator.setDuration(animationDuration);
        r1DecreaseAnimator.setDuration(animationDuration);

//        setValueAnimationListener(rdBtn1, r1IncreaseAnimator);
//        setValueAnimationListener(rdBtn1, r1DecreaseAnimator);
//        setValueAnimationListener(rdBtn2, r2IncreaseAnimator);
//        setValueAnimationListener(rdBtn2, r2DecreaseAnimator);
    }

    private static void setValueAnimationListener(@NonNull final RadioButton rdBtn1, ValueAnimator r1IncreaseAnimator2) {
        r1IncreaseAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                rdBtn1.setTextSize((Float) valueAnimator.getAnimatedValue());
            }
        });
    }


}
