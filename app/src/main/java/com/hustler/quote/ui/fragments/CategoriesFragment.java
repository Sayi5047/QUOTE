package com.hustler.quote.ui.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hustler.quote.R;
import com.hustler.quote.ui.activities.QuoteDetailsActivity;
import com.hustler.quote.ui.adapters.CategoriesAdapter;
import com.hustler.quote.ui.adapters.LocalAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.database.QuotesDbHelper;
import com.hustler.quote.ui.pojo.Quote;
import com.hustler.quote.ui.utils.TextUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

import java.util.ArrayList;

/**
 * Created by anvaya5 on 20/12/2017.
 */

public class CategoriesFragment extends android.support.v4.app.Fragment {
    RecyclerView categories_rv;
    CategoriesAdapter categoriesAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quote_categories_layout, container, false);
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        categories_rv = (RecyclerView) view.findViewById(R.id.rv_categories);
        categories_rv.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        categories_rv.setAdapter(new CategoriesAdapter(getActivity(), new CategoriesAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClicked(String category, int position, GradientDrawable gradientDrawable) {
//                Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(),category+" "+position);
                bringupQuotes(category, gradientDrawable);
            }
        }));
    }

    private void bringupQuotes(String category, GradientDrawable gradientDrawable) {
        final Dialog dialog = new Dialog(getContext(), R.style.EditTextDialog_non_floater);
        dialog.setContentView(R.layout.dialog_category_layout);
        dialog.getWindow().setBackgroundDrawable(gradientDrawable);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog_non_floater;


        TextView catgory_name;
        AdView adView;
        FloatingActionButton close_button;
        ArrayList<Quote> quoteslist = new ArrayList<>();
        dialog.show();

        quoteslist = (ArrayList<Quote>) new QuotesDbHelper(getContext()).getQuotesByCategory(category);
        if (quoteslist.size() <= 0) {
            dialog.cancel();
            Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.no_quotes_available));
        } else {
            catgory_name = (TextView) dialog.findViewById(R.id.tv_category_name);
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialog.getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
            }else {
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dialog.getWindow().setStatusBarColor(gradientDrawable.getColors()[1]);
                    catgory_name.setBackgroundColor(gradientDrawable.getColors()[1]);

                } else {
                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        dialog.getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
                    }
                }

            }
            categories_rv = (RecyclerView) dialog.findViewById(R.id.rv_category_list);
            close_button = (FloatingActionButton) dialog.findViewById(R.id.bt_close);
            adView = (AdView) dialog.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            TextUtils.setFont(getActivity(), catgory_name, Constants.FONT_CIRCULAR);
            catgory_name.setText(category + " ( " + quoteslist.size() + " )");
            categories_rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            categories_rv.setAdapter(new LocalAdapter(getActivity(), quoteslist, new LocalAdapter.OnQuoteClickListener() {
                @Override
                public void onQuoteClicked(int position, int color, Quote quote, View view) {
                    Intent intent = new Intent(getActivity(), QuoteDetailsActivity.class);
                    intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quote);
                    startActivity(intent);
                }
            }));
            close_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categories_rv.setAdapter(null);
                    dialog.dismiss();
                }
            });

        }

        dialog.setCancelable(false);

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_BACK || keyCode == event.KEYCODE_HOME) {
                    dialog.dismiss();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

}
