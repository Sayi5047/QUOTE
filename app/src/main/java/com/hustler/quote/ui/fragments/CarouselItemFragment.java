package com.hustler.quote.ui.fragments;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.pojo.OffLineQuotes;
import com.hustler.quote.ui.utils.AnimUtils;
import com.hustler.quote.ui.utils.TextUtils;


/**
 * Created by Sayi Manoj Sugavasi on 12/03/2018.
 */

public class CarouselItemFragment extends Fragment {
    private static final String ARGUMENT_BANNER_OBJECT = "BANNER_OBJECT_KEY";

    ImageView imageView;
    TextView textView1, textView2;
    RelativeLayout root;
    @Nullable
    OffLineQuotes banners;


    @NonNull
    public static CarouselItemFragment newInstance(OffLineQuotes banners) {
        CarouselItemFragment fragment = new CarouselItemFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(ARGUMENT_BANNER_OBJECT, banners);
        fragment.setArguments(arguments);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.carousel_item, container, false);
        findViews(view);
        loadBannerData();

        return view;
    }

    private void findViews(View view) {
        imageView = view.findViewById(R.id.pagerImg);
        textView1 = view.findViewById(R.id.offer_head);
        textView2 = view.findViewById(R.id.offer_text);
        root = view.findViewById(R.id.root);
        assert null != getActivity();
        TextUtils.findText_and_applyTypeface(root, getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setClipToOutline(true);
        }

        int color1 = TextUtils.getMatColor(getActivity(), "mdcolor_500");
        int color2 = TextUtils.getMatColor(getActivity(), "mdcolor_500");
        root.setBackground(AnimUtils.createDrawable(color1, color2, getActivity()));

    }

    private void loadBannerData() {
        banners = (OffLineQuotes) getArguments().getSerializable(ARGUMENT_BANNER_OBJECT);
        if (banners != null) {
            fillViews(banners);
        }
    }

    private void fillViews(OffLineQuotes banners) {
        textView1.setText(banners.getQuoteText());
        textView2.setText(banners.getQuoteAuthor());
    }


}
