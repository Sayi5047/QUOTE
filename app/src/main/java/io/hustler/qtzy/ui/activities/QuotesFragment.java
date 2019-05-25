package io.hustler.qtzy.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.Services.QuoteLoaderService;
import io.hustler.qtzy.ui.adapters.LocalAdapter;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;
import io.hustler.qtzy.ui.database.QuotesDbHelper;
import io.hustler.qtzy.ui.pojo.Quote;
import io.hustler.qtzy.ui.utils.IntentConstants;
import io.hustler.qtzy.ui.utils.Toast_Snack_Dialog_Utils;

/**
 * Created by Sayi on 07-10-2017.
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
//LoaderManager.LoaderCallbacks<List<Quote>>,
public class QuotesFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    RecyclerView rv;
    ProgressBar loader;
    @Nullable
    LocalAdapter localAdapter;
    SharedPreferences sharedPreferences;
    @NonNull
    private List<Quote> quotesList = new ArrayList<>();


    public static QuotesFragment newInstance() {
        return new QuotesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainfragmentlayout, container, false);
        rv = view.findViewById(R.id.main_rv);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        loader = view.findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (!sharedPreferences.getBoolean(Constants.IS_QUOTES_LOADED_KEY, false)) {
            Objects.requireNonNull(getActivity()).startService(new Intent(getActivity(), QuoteLoaderService.class));
            Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), "QuotesNotLoaded");
        } else {
            Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), "Quotes Loaded");

        }
        localAdapter = new LocalAdapter(getActivity(), null, new LocalAdapter.OnQuoteClickListener() {
            @Override
            public void onQuoteClicked(int position, @NonNull GradientDrawable color, Quote quote, View view) {

                Intent intent = new Intent(getActivity(), QuoteDetailsActivity.class);
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), new Pair<View, String>(view, getString(R.string.root_quote))).toBundle();
                intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quote);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.putExtra(IntentConstants.GRADIENT_COLOR1, color.getColors());
                }
                startActivity(intent, bundle);
            }
        });
        loadQuotes();
        return view;
    }

    public void loadQuotes() {

        if (sharedPreferences.getBoolean(Constants.IS_QUOTES_LOADED_KEY, true)) {
            new QuotesloadTask().execute();
        } else {
            loader.setVisibility(View.VISIBLE);
        }

        // TODO: 27-01-2019 ADD A REST UTILITY CALL FOR GET QUOTES BY CATEGORY ATTITUDE
//        new Restutility(getActivity()).getQuotes(new QuotzyApiResponseListener() {
//            @Override
//            public void onSuccess(String message) {
//
//            }
//
//            @Override
//            public void onDataGet(BaseResponse response) {
//                ResponseQuotesService responseQuotesService = ((ResponseQuotesService) response);
//                for (CategoriesFragment.Quotes quotes : responseQuotesService.getData()) {
//                    Quote quote = new Quote();
//                    quote.setId(quotes.getId());
//                    quote.setQuote(quotes.getQuote());
//                    quote.setAuthor(quotes.getAuthor());
//                    quote.setCategory(quotes.getCategory());
//                    quote.setIsLiked(0);
//                    quotes.setCountry(null);
//                    quotesList.add(quote);
//                }
//                setAdapter(new ArrayList<Quote>(quotesList));
//            }
//
//            @Override
//            public void onError(String message) {
//                Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), "ERROR OCCURED");
//            }
//        }, getActivity(), Constants.QUOTZY_API_GET_QUOTES_BY_CATEGORY + "Attitude");
    }

    private void setAdapter(@Nullable final ArrayList<Quote> quotes) {
        if (quotes == null) {
        } else {
            loader.setVisibility(View.GONE);
            localAdapter.addData(quotes);
            localAdapter.notifyDataSetChanged();
            rv.setAdapter(localAdapter);

        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (Objects.equals(key, Constants.IS_QUOTES_LOADED_KEY)) {
            new QuotesloadTask().execute();
            Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), "RECEIVED CALL BACK");
        }
    }

    @Override
    public void onStart() {
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        super.onStart();
    }

    @Override
    public void onStop() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);

        super.onStop();
    }


    class QuotesloadTask extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(String... strings) {

            quotesList = new QuotesDbHelper(getActivity()).getQuotesByCategory("Attitude");


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setAdapter(new ArrayList<>(quotesList));

        }
    }


//
//    @Override
//    public Loader<List<Quote>> onCreateLoader(int id, Bundle args) {
////        return new Quotesloader(getActivity());
//        return null
//    }
//
//    @Override
//    public void onLoadFinished(Loader<List<Quote>> loader, List<Quote> data) {
////        this.loader.setVisibility(View.GONE);
////        setAdapter(new ArrayList<Quote>(data));
//    }
//
//    @Override
//    public void onLoaderReset(Loader<List<Quote>> loader) {
////        setAdapter(null);
//    }
    //    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d("MainAdapter","ON RESUME");
//        rv.setAdapter(null);
//        setAdapter(rv);
//
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        Log.d("MainAdapter","ON DETACH");
//
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        Log.d("MainAdapter","ON Attach");
//
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        Log.d("MainAdapter","ON Start");
//        rv.setAdapter(null);
//        setAdapter(rv);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        Log.d("MainAdapter","ON STOP");
//
//    }
    //    public void setLayout(int i) {
//        if(rv!=null){
//            switch (i){
//                case 1:
//                    rv.setLayoutManager(new LinearLayoutManager(getActivity()));
//                    getRandomQuotes();
//
//                    break;
//                case 2:
//                    rv.setLayoutManager(new GridLayoutManager(getActivity(),2));
//                    getRandomQuotes();
//
//                    break;
//                case 3: rv.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
//                    getRandomQuotes();
//
//                    break;
//            }
//        }
//    }
}
