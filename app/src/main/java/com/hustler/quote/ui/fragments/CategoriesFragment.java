package com.hustler.quote.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hustler.quote.R;
import com.hustler.quote.ui.adapters.CategoriesAdapter;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

/**
 * Created by anvaya5 on 20/12/2017.
 */

public class CategoriesFragment extends android.support.v4.app.Fragment {
    RecyclerView categories_rv;
    CategoriesAdapter categoriesAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.quote_categories_layout,container,false);
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        categories_rv = (RecyclerView) view.findViewById(R.id.rv_categories);
        categories_rv.setLayoutManager(new GridLayoutManager(getActivity(),2));
        categories_rv.setAdapter(new CategoriesAdapter(getActivity(), new CategoriesAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClicked(String category, int position) {
                Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(),category+" "+position);

            }
        }));
    }
}
