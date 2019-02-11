package io.hustler.qtzy.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.adapters.LocalAdapter;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;
import io.hustler.qtzy.ui.apiRequestLauncher.QuotzyApiResponseListener;
import io.hustler.qtzy.ui.apiRequestLauncher.ResponseQuotesService;
import io.hustler.qtzy.ui.apiRequestLauncher.Restutility;
import io.hustler.qtzy.ui.fragments.CategoriesFragment;
import io.hustler.qtzy.ui.pojo.Quote;
import io.hustler.qtzy.ui.apiRequestLauncher.Base.BaseResponse;
import io.hustler.qtzy.ui.utils.IntentConstants;
import io.hustler.qtzy.ui.utils.Toast_Snack_Dialog_Utils;

import java.util.ArrayList;
import java.util.List;

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
    LocalAdapter localAdapter;
    SharedPreferences sharedPreferences;
    private List<Quote> quotesList=new ArrayList<>();


    public static QuotesFragment newInstance() {
        return new QuotesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainfragmentlayout, container, false);
        rv = view.findViewById(R.id.main_rv);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        loader = view.findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        localAdapter = new LocalAdapter(getActivity(), null, new LocalAdapter.OnQuoteClickListener() {
            @Override
            public void onQuoteClicked(int position, GradientDrawable color, Quote quote, View view) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    getActivity().getWindow().setEnterTransition(new Slide());
//                    Intent intent = new Intent(getActivity(), QuoteDetailsActivity.class);
//                    intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quote);
//                    ActivityOptionsCompat options = ActivityOptionsCompat.
//                            makeSceneTransitionAnimation(getActivity(),
//                                    view,
//                                    getString(R.string.quotes_author_transistion));
//                    startActivity(intent, options.toBundle());
//                } else {
                Intent intent = new Intent(getActivity(), QuoteDetailsActivity.class);
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), new Pair<View, String>(view, getString(R.string.root_quote))).toBundle();
                intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quote);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.putExtra(IntentConstants.GRADIENT_COLOR1, color.getColors());

                } else {

                }
                startActivity(intent, bundle);
//                }
            }
        });
        loadQuotes();
        return view;
    }

    public void loadQuotes() {
        // TODO: 27-01-2019 ADD A REST UTILITY CALL FOR GET QUOTES BY CATEGORY ATTITUDE
        new Restutility(getActivity()).getQuotes(new QuotzyApiResponseListener() {
            @Override
            public void onSuccess(String message) {

            }

            @Override
            public void onDataGet(BaseResponse response) {
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
                setAdapter(new ArrayList<Quote>(quotesList));
            }

            @Override
            public void onError(String message) {
                Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), "ERROR OCCURED");
            }
        }, getActivity(), Constants.QUOTZY_API_GET_QUOTES_BY_CATEGORY + "Attitude");

//        if (sharedPreferences.getBoolean(Constants.IS_QUOTES_LOADED_KEY, true)) {
////            getLoaderManager().initLoader(0, null, this);
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    quotes = new QuotesDbHelper(getActivity()).getQuotesByCategory("Attitude");
//                    setAdapter(new ArrayList<Quote>(quotes));
//                }
//            }).run();
//        } else {
//            loader.setVisibility(View.VISIBLE);
//        }
    }

    private void setAdapter(final ArrayList<Quote> quotes) {
        if (quotes == null) {
        } else {
            localAdapter.addData(quotes);
            localAdapter.notifyDataSetChanged();
            rv.setAdapter(localAdapter);
            loader.setVisibility(View.GONE);

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

    /**
     * Called when a shared preference is changed, added, or removed. This
     * may be called even if a preference is set to its existing value.
     * <p>
     * <p>This callback will be run on your main thread.
     *
     * @param sharedPreferences The {@link SharedPreferences} that received
     *                          the change.
     * @param key               The key of the preference that was changed, added, or
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key == Constants.IS_QUOTES_LOADED_KEY) {
            loadQuotes();
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
