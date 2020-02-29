package com.hustler.quote.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.transition.MaterialSharedAxis;
import com.hustler.quote.R;
import com.hustler.quote.ui.Executors.AppExecutor;
import com.hustler.quote.ui.ORM.AppDatabase;
import com.hustler.quote.ui.ORM.Tables.QuotesTable;
import com.hustler.quote.ui.ViewModels.MainViewModel;
import com.hustler.quote.ui.activities.QuoteDetailsActivity;
import com.hustler.quote.ui.adapters.QuotesAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.utils.IntentConstants;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private QuotesAdapter quotesAdapter;
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
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        setQuotesAdapter();
        if (sharedPreferences.getBoolean(Constants.IS_QUOTES_LOADED_KEY, false)) {
            loadQuotesToUi();
        } else {
            loadQuotesToDatabase();
        }
        Log.i(TAG, "ON CREATEVIEW CALLED");
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.X, false));
        setExitTransition(MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.X, true));
    }

    private void setQuotesAdapter() {
        quotesAdapter = new QuotesAdapter(getActivity(), (position, color, quote, view) -> {
            Intent intent = new Intent(getActivity(), QuoteDetailsActivity.class);
            intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quote.getId());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.putExtra(IntentConstants.GRADIENT_COLOR1, color);
            }
            startActivity(intent);
        });
        rv.setAdapter(quotesAdapter);
    }

    private void loadQuotesToDatabase() {
        appExecutor.getDiskExecutor().execute(() -> {
            load_from_Arrays();
            for (QuotesTable quotesTable : quotesTableArrayList) {
                Log.i(TAG, quotesTable.getQuotes() + "-->" + " Addded");
                appDatabase.quotesDao().insertUser(quotesTable);
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.IS_QUOTES_LOADED_KEY, true);
            editor.apply();
            Objects.requireNonNull(getActivity()).runOnUiThread(this::loadQuotesToUi);
        });

    }

    private void loadQuotesToUi() {

        if (sharedPreferences.getBoolean(Constants.IS_QUOTES_LOADED_KEY, true)) {
            loader.setVisibility(View.GONE);
            mainViewModel.getQuotes(Objects.requireNonNull(getContext()).getApplicationContext());
            mainViewModel.mainFragmentQuotesList.observe(getViewLifecycleOwner(), QuotesFragment.this::setAdapter);

        } else {
            loader.setVisibility(View.VISIBLE);
            loadQuotesToDatabase();
        }

    }


    private void setAdapter(@Nullable final List<QuotesTable> quotes) {
        Log.i(TAG, "setAdapter: ROOM DATA CHANGED");
        if (quotes == null) {
            loader.setVisibility(View.VISIBLE);
            Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), "Quotes are null");
        } else {
            loader.setVisibility(View.GONE);
            assert quotesAdapter != null;
            quotesAdapter.addData((ArrayList<QuotesTable>) quotes);
        }
    }


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

}
