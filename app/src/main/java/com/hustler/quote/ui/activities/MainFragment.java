package com.hustler.quote.ui.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hustler.quote.R;
import com.hustler.quote.ui.adapters.MainAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.apiRequestLauncher.QuotesApiResponceListener;
import com.hustler.quote.ui.apiRequestLauncher.Restutility;
import com.hustler.quote.ui.pojo.QuotesFromFC;
import com.hustler.quote.ui.superclasses.App;

import java.util.List;
import java.util.Random;

/**
 * Created by Sayi on 07-10-2017.
 */

public class MainFragment extends Fragment {

    RecyclerView rv;
    ProgressBar loader;
    MainAdapter adapter;
    TextView quote_of_day, quote_author;
    int selectedQuote;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainfragmentlayout, container, false);
        rv = (RecyclerView) view.findViewById(R.id.main_rv);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        loader = (ProgressBar) view.findViewById(R.id.loader);
        quote_of_day = (TextView) view.findViewById((R.id.quote_of_day));
        quote_author = (TextView) view.findViewById(R.id.quote_of_day_author);
        quote_of_day.setTypeface(App.getZingCursive(getActivity(), Constants.FONT_ZINGCURSIVE));
        quote_author.setTypeface(App.getZingCursive(getActivity(), Constants.FONT_ZINGCURSIVE));
        quote_author.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slideup));
        quote_of_day.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slideup));

        getRandomQuotes();
        return view;
    }

    private void getRandomQuotes() {

        new Restutility(getActivity()).getRandomQuotes(getActivity(), new QuotesApiResponceListener() {
            @Override
            @Nullable
            public void onSuccess(List<QuotesFromFC> quotes) {
                loader.setVisibility(View.GONE);
                rv.setAdapter(new MainAdapter(getActivity(), quotes, new MainAdapter.OnItemClicListener() {
                    @Override
                    public void onItemClickHappened(QuotesFromFC quotesFromFC, View view) {
                        Toast.makeText(getActivity(), quotesFromFC.getAuthor(), Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getActivity().getWindow().setEnterTransition(new Explode());
                            Intent intent = new Intent(getActivity(), QuoteDetailsActivity.class);
                            intent.putExtra(Constants.INTENT_QUOTE_OBJECT, quotesFromFC);
                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation(getActivity(),
                                            view,
                                            getString(R.string.quotes_author_transistion));
                            startActivity(intent, options.toBundle());
                        } else {
                            Intent intent = new Intent(getActivity(), QuoteDetailsActivity.class);
                            intent.putExtra(Constants.INTENT_QUOTE_OBJECT, quotesFromFC);
                            startActivity(intent);
                        }

                    }
                }));
                selectedQuote = new Random().nextInt(quotes.size() - 1) + 1;
                quote_of_day.setText(quotes.get(selectedQuote).getBody());
//                adapter.updateQuotes(quotes);

                quote_author.setText(quotes.get(selectedQuote).getAuthor());


            }

            @Override
            public void onError(String message) {
                loader.setVisibility(View.GONE);
                App.showToast(getActivity(), getString(R.string.no_quotes_available));
            }
        });
    }

}
