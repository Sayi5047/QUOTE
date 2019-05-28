package io.hustler.qtzy.ui.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.activities.QuoteDetailsActivity;
import io.hustler.qtzy.ui.adapters.CategoriesAdapter;
import io.hustler.qtzy.ui.adapters.LocalAdapter;
import io.hustler.qtzy.ui.apiRequestLauncher.Base.BaseResponse;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;
import io.hustler.qtzy.ui.apiRequestLauncher.ListnereInterfaces.QuotzyApiResponseListener;
import io.hustler.qtzy.ui.apiRequestLauncher.ResponseQuotesService;
import io.hustler.qtzy.ui.apiRequestLauncher.Restutility;
import io.hustler.qtzy.ui.database.QuotesDbHelper;
import io.hustler.qtzy.ui.pojo.Quote;
import io.hustler.qtzy.ui.utils.AdUtils;
import io.hustler.qtzy.ui.utils.IntentConstants;
import io.hustler.qtzy.ui.utils.TextUtils;
import io.hustler.qtzy.ui.utils.Toast_Snack_Dialog_Utils;

import static android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

/**
 * Created by Sayi Manoj Sugavasi on 20/12/2017.
 */

/*   Copyright [2018] [Sayi Manoj Sugavasi]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/
public class CategoriesFragment extends android.support.v4.app.Fragment {
    RecyclerView categories_rv;
    CategoriesAdapter categoriesAdapter;
    ArrayList<Quote> quotesList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quote_categories_layout, container, false);
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        categories_rv = view.findViewById(R.id.rv_categories);
        categories_rv.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        categories_rv.setAdapter(new CategoriesAdapter(getActivity(), new CategoriesAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClicked(String category, String cat2, int position, @NonNull GradientDrawable gradientDrawable) {
//                Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(),category+" "+position);
//                loadQuotes(category, cat2, gradientDrawable);
                bringupQuotesOLD(category, cat2, gradientDrawable);
            }
        }));
    }

    private void bringupQuotes(String category, String cat2, @NonNull GradientDrawable gradientDrawable) {
        final Dialog dialog = new Dialog(getContext(), R.style.EditTextDialog_non_floater);
        dialog.setContentView(R.layout.dialog_category_layout);
//        dialog.getWindow().setBA(gradientDrawable);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog_non_floater;


        TextView catgory_name;
        AdView adView;
        FloatingActionButton close_button;
        dialog.show();

        if (quotesList.size() <= 0) {
            dialog.cancel();
            Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.no_quotes_available));
        } else {
            catgory_name = dialog.findViewById(R.id.tv_category_name);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dialog.getWindow().setStatusBarColor(Color.WHITE);
                    catgory_name.setBackgroundColor(Color.WHITE);
                    catgory_name.setBackgroundColor(gradientDrawable.getColors()[1]);


                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dialog.getWindow().setStatusBarColor(Color.WHITE);
                    catgory_name.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));

                }

//                } else {
//                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        dialog.getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
//                    }
//                }
            }

            categories_rv = dialog.findViewById(R.id.rv_category_list);
            close_button = dialog.findViewById(R.id.bt_close);
            adView = dialog.findViewById(R.id.adView);
            AdUtils.loadBannerAd(adView, getActivity());
            TextUtils.setFont(getActivity(), catgory_name, Constants.FONT_CIRCULAR);
            if (cat2 == " ") {
                catgory_name.setText(String.format("%s", category));

            } else {
                catgory_name.setText(String.format("%s & %s", cat2, category));

            }
            categories_rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            categories_rv.setAdapter(new LocalAdapter(getActivity(), quotesList, new LocalAdapter.OnQuoteClickListener() {
                @Override
                public void onQuoteClicked(int position, @NonNull GradientDrawable color, Quote quote, View view) {
                    Intent intent = new Intent(getActivity(), QuoteDetailsActivity.class);
                    Bundle bundle = makeSceneTransitionAnimation(getActivity(), new Pair<>(view, getString(R.string.root_quote))).toBundle();
                    intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quote);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.putExtra(IntentConstants.GRADIENT_COLOR1, color.getColors());

                    }
                    startActivity(intent, bundle);
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
            public boolean onKey(@NonNull DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
                    dialog.dismiss();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public ArrayList<Quote> loadQuotes(final String category, final String cat2, @NonNull final GradientDrawable gradientDrawable) {
        quotesList = new ArrayList<>();
        final ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setVisibility(View.VISIBLE);
        // TODO: 27-01-2019 ADD A REST UTILITY CALL FOR GET QUOTES BY CATEGORY ATTITUDE
        new Restutility(getActivity()).getQuotes(new QuotzyApiResponseListener() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onDataGet(BaseResponse response) {
                progressBar.setVisibility(View.GONE);

                ResponseQuotesService responseQuotesService = ((ResponseQuotesService) response);
                for (CategoriesFragment.Quotes quotes : responseQuotesService.getData()) {
                    Quote quote = new Quote();
                    quote.setId(quotes.getId());
                    quote.setQuote(quotes.getQuote());
                    quote.setAuthor(quotes.getAuthor());
                    quote.setCategory(quotes.getCategory());
                    quote.setIsLiked(0);
                    quotes.setCountry(null);
                    quotesList.add(quote);
                }
                bringupQuotes(category, cat2, gradientDrawable);
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), "ERROR OCCURED");
            }
        }, getActivity(), Constants.QUOTZY_API_GET_QUOTES_BY_CATEGORY + category);
        return quotesList;


    }

    public static class Quotes {
        private String quote, author, country, category;
        private Long id;

//        public static Quote returnInstance(String quote, String author, String country, String category, Long id) {
//            return new Quote(quote, author, country, category, id);
//        }

        public Quotes(String quote, String author, String country, String category, Long id) {
            this.quote = quote;
            this.author = author;
            this.country = country;
            this.category = category;
            this.id
                    = id;
        }

        public String getQuote() {
            return quote;
        }

        public void setQuote(String quote) {
            this.quote = quote;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    public static class Data {
        ArrayList<Quotes> data = new ArrayList<>();

        public ArrayList<Quotes> getData() {
            return data;
        }

        public void setData(ArrayList<Quotes> data) {
            this.data = data;
        }
    }

    private void bringupQuotesOLD(String category, String cat2, @NonNull GradientDrawable gradientDrawable) {
        final Dialog dialog = new Dialog(getContext(), R.style.EditTextDialog_non_floater);
        dialog.setContentView(R.layout.dialog_category_layout);
//        dialog.getWindow().setBA(gradientDrawable);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog_non_floater;


        TextView catgory_name = null;
        AdView adView;
        FloatingActionButton close_button;
        ArrayList<Quote> quoteslist = new ArrayList<>();
        dialog.show();

        quoteslist = (ArrayList<Quote>) new QuotesDbHelper(getActivity().getApplicationContext()).getQuotesByCategory(category);
        if (quoteslist.size() <= 0) {
            dialog.cancel();
            Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.no_quotes_available));
        } else {
            catgory_name = dialog.findViewById(R.id.tv_category_name);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dialog.getWindow().setStatusBarColor(Color.WHITE);
                    catgory_name.setBackgroundColor(Color.WHITE);
                    catgory_name.setBackgroundColor(gradientDrawable.getColors()[1]);


                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dialog.getWindow().setStatusBarColor(Color.WHITE);
                    catgory_name.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));

                }

//                } else {
//                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        dialog.getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
//                    }
//                }
            }

            categories_rv = dialog.findViewById(R.id.rv_category_list);
            close_button = dialog.findViewById(R.id.bt_close);
            adView = dialog.findViewById(R.id.adView);
            AdUtils.loadBannerAd(adView, getActivity());
            TextUtils.setFont(getActivity(), catgory_name, Constants.FONT_CIRCULAR);
            if (cat2 == " ") {
                catgory_name.setText(String.format("%s", category));

            } else {
                catgory_name.setText(String.format("%s & %s", cat2, category));

            }
            categories_rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            categories_rv.setAdapter(new LocalAdapter(getActivity(), quoteslist, new LocalAdapter.OnQuoteClickListener() {
                @Override
                public void onQuoteClicked(int position, @NonNull GradientDrawable color, Quote quote, View view) {
                    Intent intent = new Intent(getActivity(), QuoteDetailsActivity.class);
                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), new Pair<>(view, getString(R.string.root_quote))).toBundle();
                    intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quote);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.putExtra(IntentConstants.GRADIENT_COLOR1, color.getColors());

                    } else {

                    }
                    startActivity(intent, bundle);
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
            public boolean onKey(@NonNull DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
                    dialog.dismiss();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}




