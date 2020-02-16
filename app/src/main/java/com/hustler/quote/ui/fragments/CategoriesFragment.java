package com.hustler.quote.ui.fragments;

import android.app.Dialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.hustler.quote.ui.Executors.AppExecutor;
import com.hustler.quote.ui.adapters.CategoriesAdapter;
import com.hustler.quote.ui.adapters.LocalAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Base.BaseResponse;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.apiRequestLauncher.ListnereInterfaces.QuotzyApiResponseListener;
import com.hustler.quote.ui.apiRequestLauncher.ResponseQuotesService;
import com.hustler.quote.ui.apiRequestLauncher.Restutility;
import com.hustler.quote.ui.utils.AdUtils;
import com.hustler.quote.ui.utils.IntentConstants;
import com.hustler.quote.ui.utils.TextUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.hustler.quote.R;

import com.hustler.quote.ui.ORM.AppDatabase;
import com.hustler.quote.ui.ORM.Tables.QuotesTable;
import com.hustler.quote.ui.activities.QuoteDetailsActivity;
import com.hustler.quote.ui.pojo.Quote;

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
public class CategoriesFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    RecyclerView categories_rv;
    ArrayList<Quote> quotesList;

    AppDatabase appDatabase;
    AppExecutor appExecutor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quote_categories_layout, container, false);
        appDatabase = AppDatabase.getmAppDatabaseInstance(getContext());
        appExecutor = AppExecutor.getInstance();
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
//                loadQuotesToUi(category, cat2, gradientDrawable);
                bringupQuotesOLD(category, cat2, gradientDrawable);
            }
        }));
    }


    private void bringupQuotesOLD(final String category, final String cat2, @NonNull final GradientDrawable gradientDrawable) {
        final Dialog dialog = new Dialog(getContext(), R.style.EditTextDialog_non_floater);
        dialog.setContentView(R.layout.dialog_category_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog_non_floater;
        TextView catgory_name = null;
        AdView adView;
        FloatingActionButton close_button;
        final LiveData<List<QuotesTable>> quoteslist;
        categories_rv.setNestedScrollingEnabled(true);
        categories_rv = dialog.findViewById(R.id.rv_category_list);
        close_button = dialog.findViewById(R.id.bt_close);
        adView = dialog.findViewById(R.id.adView);
        AdUtils.loadBannerAd(adView, getActivity());
        catgory_name = dialog.findViewById(R.id.tv_category_name);
        TextUtils.setFont(getActivity(), catgory_name, Constants.FONT_CIRCULAR);

        if (Objects.equals(cat2, " ")) {
            catgory_name.setText(String.format("%s", category));

        } else {
            catgory_name.setText(String.format("%s & %s", cat2, category));

        }
        categories_rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        quoteslist = appDatabase.quotesDao().loadAllbyCategory(category);
        final TextView finalCatgory_name = catgory_name;
        quoteslist.observe(this, new Observer<List<QuotesTable>>() {
            @Override
            public void onChanged(@Nullable List<QuotesTable> quotesTables) {
                Log.i(TAG, "ON CHAGE CALLED");
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        setQuotesAdapter(gradientDrawable, dialog, quoteslist, finalCatgory_name);

                    }
                });
            }
        });
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categories_rv.setAdapter(null);
                dialog.dismiss();
            }
        });
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

        dialog.show();

    }

    private void setQuotesAdapter(@NonNull GradientDrawable gradientDrawable, Dialog dialog, LiveData<List<QuotesTable>> quoteslist, TextView categoryname) {
        if (Objects.requireNonNull(quoteslist.getValue()).size() <= 0) {
            dialog.cancel();
            Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.no_quotes_available));
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Objects.requireNonNull(dialog.getWindow()).setStatusBarColor(Color.WHITE);
                    categoryname.setBackgroundColor(Color.WHITE);
                    categoryname.setBackgroundColor(Objects.requireNonNull(gradientDrawable.getColors())[1]);

                } else {
                    Objects.requireNonNull(dialog.getWindow()).setStatusBarColor(Color.WHITE);
                    categoryname.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.colorAccent));

                }

            }


            categories_rv.setAdapter(new LocalAdapter(getActivity(), (ArrayList<QuotesTable>) quoteslist.getValue(), new LocalAdapter.OnQuoteClickListener() {
                @Override
                public void onQuoteClicked(int position, @NonNull GradientDrawable color, QuotesTable quote, View view) {
                    Intent intent = new Intent(getActivity(), QuoteDetailsActivity.class);
                    intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quote.getId());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.putExtra(IntentConstants.GRADIENT_COLOR1, color.getColors());

                    } else {

                    }
                    startActivity(intent);
                }
            }));

            Log.i(TAG, "ADAPTER SET");

        }
    }


    /*REST API CALLs*/
    private void bringupQuotes(String category, String cat2, @NonNull GradientDrawable gradientDrawable) {
        final Dialog dialog = new Dialog(getContext(), R.style.EditTextDialog_non_floater);
        dialog.setContentView(R.layout.dialog_category_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog_non_floater;


        TextView catgory_name;
        AdView adView;
        FloatingActionButton close_button;
        dialog.show();

//        if (quotesList.size() <= 0) {
//            dialog.cancel();
//            Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), getString(R.string.no_quotes_available));
//        } else {
//            catgory_name = dialog.findViewById(R.id.tv_category_name);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    dialog.getWindow().setStatusBarColor(Color.WHITE);
//                    catgory_name.setBackgroundColor(Color.WHITE);
//                    catgory_name.setBackgroundColor(gradientDrawable.getColors()[1]);
//
//
//                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    dialog.getWindow().setStatusBarColor(Color.WHITE);
//                    catgory_name.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
//
//                }
//
////                } else {
////                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
////                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
////                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                        dialog.getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
////                    }
////                }
//            }
//
//            categories_rv = dialog.findViewById(R.id.rv_category_list);
//            close_button = dialog.findViewById(R.id.bt_close);
//            adView = dialog.findViewById(R.id.adView);
//            AdUtils.loadBannerAd(adView, getActivity());
//            TextUtils.setFont(getActivity(), catgory_name, Constants.FONT_CIRCULAR);
//            if (cat2 == " ") {
//                catgory_name.setText(String.format("%s", category));
//
//            } else {
//                catgory_name.setText(String.format("%s & %s", cat2, category));
//
//            }
//            categories_rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//            categories_rv.setAdapter(new LocalAdapter(getActivity(), quotesList, new LocalAdapter.OnQuoteClickListener() {
//                @Override
//                public void onQuoteClicked(int position, @NonNull GradientDrawable color, Quote quote, View view) {
//                    Intent intent = new Intent(getActivity(), QuoteDetailsActivity.class);
//                    Bundle bundle = makeSceneTransitionAnimation(getActivity(), new Pair<>(view, getString(R.string.root_quote))).toBundle();
//                    intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quote);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        intent.putExtra(IntentConstants.GRADIENT_COLOR1, color.getColors());
//
//                    }
//                    startActivity(intent, bundle);
//                }
//            }));
//            close_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    categories_rv.setAdapter(null);
//                    dialog.dismiss();
//                }
//            });
//
//        }

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

    /*REST API CALLS*/
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
//                bringupQuotes(category, cat2, gradientDrawable);
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

}




