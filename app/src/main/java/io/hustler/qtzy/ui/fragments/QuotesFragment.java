package io.hustler.qtzy.ui.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.Executors.AppExecutor;
import io.hustler.qtzy.ui.ORM.AppDatabase;
import io.hustler.qtzy.ui.ORM.Tables.QuotesTable;
import io.hustler.qtzy.ui.ViewModels.MainViewModel;
import io.hustler.qtzy.ui.activities.QuoteDetailsActivity;
import io.hustler.qtzy.ui.adapters.LocalAdapter;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;
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
public class QuotesFragment extends Fragment {

    private RecyclerView rv;
    private ProgressBar loader;
    private ArrayList<QuotesTable> quotesTableArrayList;
    @Nullable
    private LocalAdapter localAdapter;
    private SharedPreferences sharedPreferences;
    private AppDatabase appDatabase;
    private AppExecutor appExecutor;
    private MainViewModel mainViewModel;

    private String TAG = "QUOTESFRAGMENT";

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
        appDatabase = AppDatabase.getmAppDatabaseInstance(getActivity());
        appExecutor = AppExecutor.getInstance();
        mainViewModel = new MainViewModel(Objects.requireNonNull(getActivity()).getApplication());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (sharedPreferences.getBoolean(Constants.IS_QUOTES_LOADED_KEY, false)) {
            loadQuotesToUi();
        } else {
            loadQuotesToDatabase();
        }
        Log.i(TAG, "ON CREATEVIEW CALLED");
        return view;
    }

    private void loadQuotesToDatabase() {
        appExecutor.getDiskExecutor().execute(new Runnable() {
            @Override
            public void run() {
                load_from_Arrays();
                for (QuotesTable quotesTable : quotesTableArrayList) {
                    Log.i(TAG, quotesTable.getQuotes() + "-->" + " Addded");
                    appDatabase.quotesDao().insertUser(quotesTable);
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Constants.IS_QUOTES_LOADED_KEY, true);
                editor.apply();
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadQuotesToUi();
                    }
                });
            }
        });

    }

    public void loadQuotesToUi() {

        if (sharedPreferences.getBoolean(Constants.IS_QUOTES_LOADED_KEY, true)) {
            loader.setVisibility(View.GONE);
            final LiveData<List<QuotesTable>> data = mainViewModel.getMainFragmentQuotes();
            final Observer<List<QuotesTable>> observer = new Observer<List<QuotesTable>>() {
                @Override
                public void onChanged(@Nullable final List<QuotesTable> quotesTables) {
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            data.removeObservers(getActivity());
                            setAdapter(quotesTables);

                            Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), "Change Occured");
                        }
                    });
                }
            };
            data.observe(this, observer);
        } else {
            loader.setVisibility(View.VISIBLE);
            loadQuotesToDatabase();
        }

    }


    private void setAdapter(@Nullable final List<QuotesTable> quotes) {
        if (quotes == null) {
            loader.setVisibility(View.VISIBLE);
            Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), "Quotes are null");
        } else {
            loader.setVisibility(View.GONE);

            localAdapter = new LocalAdapter(getActivity(), (ArrayList<QuotesTable>) quotes, new LocalAdapter.OnQuoteClickListener() {
                @Override
                public void onQuoteClicked(int position, @NonNull GradientDrawable color, QuotesTable quote, View view) {

                    Intent intent = new Intent(getActivity(), QuoteDetailsActivity.class);
                    intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quote.getId());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.putExtra(IntentConstants.GRADIENT_COLOR1, color.getColors());
                    }
                    startActivity(intent);
                }
            });
            rv.setAdapter(localAdapter);

        }
    }

//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        if (Objects.equals(key, Constants.IS_QUOTES_LOADED_KEY)) {
//            loadQuotesToUi();
//        }
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
//        Log.i(TAG, "ON RESUME CALLED");
//
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
//        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
//        Log.i(TAG, "ON PAUSE CALLED");
//    }

    private void load_from_Arrays() {

        String[] bodies = getResources().getStringArray(R.array.quote_bodies);
        String[] authors = getResources().getStringArray(R.array.quote_authors);
        String[] categories = getResources().getStringArray(R.array.quote_categories);
        final String[] languages = getResources().getStringArray(R.array.quote_languages);
        quotesTableArrayList = new ArrayList<>();
        for (int i = 0; i < bodies.length; i++) {
            QuotesTable quote = new QuotesTable(bodies[i], authors[i], categories[i], languages[0], false);
            quotesTableArrayList.add(quote);
        }


    }

//    class QuotesloadTask extends AsyncTask<String, String, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//
//        @Override
//        protected Void doInBackground(String... strings) {
//
//            quotesList = new QuotesDbHelper(getActivity()).getQuotesByCategory("Attitude");
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            setAdapter(new ArrayList<>(quotesList));
//
//        }
//    }

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
