package io.hustler.qtzy.ui.fragments;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.activities.QuoteDetailsActivity;
import io.hustler.qtzy.ui.activities.WallpapersPagerActivity;
import io.hustler.qtzy.ui.adapters.FavPagerAdapter;
import io.hustler.qtzy.ui.adapters.FavWallpaperAdapter;
import io.hustler.qtzy.ui.adapters.LocalAdapter;
import io.hustler.qtzy.ui.apiRequestLauncher.Constants;
import io.hustler.qtzy.ui.customviews.MyViewPager;
import io.hustler.qtzy.ui.database.ImagesDbHelper;
import io.hustler.qtzy.ui.database.QuotesDbHelper;
import io.hustler.qtzy.ui.pojo.Quote;
import io.hustler.qtzy.ui.pojo.unspalsh.Unsplash_Image;
import io.hustler.qtzy.ui.utils.IntentConstants;
import io.hustler.qtzy.ui.utils.TextUtils;
import io.hustler.qtzy.ui.utils.Toast_Snack_Dialog_Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
public class UserFavuritesFragment extends Fragment {
    ImageView imageView, iv_no_fav;
    RecyclerView recyclerView, rv_imag_no_fv;
    TabLayout tabLayout;
    MyViewPager viewPager;
    ArrayList<Quote> quotes;
    FavPagerAdapter favPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fav_layout, container, false);
        viewPager = view.findViewById(R.id.fav_viewPager);
        tabLayout = view.findViewById(R.id.fav_tabLayout);
        favPagerAdapter = new FavPagerAdapter(getActivity(), getChildFragmentManager());
        viewPager.setAdapter(favPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        imageView = view.findViewById(R.id.iv);
        iv_no_fav = view.findViewById(R.id.iv_no_fav);
        recyclerView = view.findViewById(R.id.rv);
        rv_imag_no_fv = view.findViewById(R.id.rv_imag_no_fv);

        editTabLayout();
        return view;
    }

    private void editTabLayout() {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();

            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    TextView textView = (TextView) tabViewChild;
                    textView.setAllCaps(false);
//                    setAnimation(textView);
                    TextUtils.setFont(getActivity(), textView, Constants.FONT_CIRCULAR);
                }
            }
        }

    }

    private void setAdapter(RecyclerView recyclerView) {
        final ArrayList<Quote>[] arrayLists = new ArrayList[]{new ArrayList<>()};
        final List<Unsplash_Image>[] imageLists = new List[]{new LinkedList()};
        final Unsplash_Image[][] images = new Unsplash_Image[1][1];
        new Thread(new Runnable() {
            @Override
            public void run() {
//                arrayLists[0] = (ArrayList<Quote>) new QuotesDbHelper(getActivity()).getAllFav_Quotes();
                imageLists[0] = new ImagesDbHelper(getActivity()).getAllFav();
            }
        }).run();
        recyclerView.setAdapter(new LocalAdapter(getActivity(), arrayLists[0], new LocalAdapter.OnQuoteClickListener() {
            @Override
            public void onQuoteClicked(int position, @NonNull GradientDrawable color, Quote quote, View view) {
                Intent intent = new Intent(getActivity(), QuoteDetailsActivity.class);
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), new Pair<>(view, getString(R.string.root_quote))
                       ).toBundle();
                intent.putExtra(Constants.INTENT_QUOTE_OBJECT_KEY, quote);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.putExtra(IntentConstants.GRADIENT_COLOR1, color.getColors());

                } else {

                }
                startActivity(intent, bundle);
//                Intent intent = new Intent(
//                        WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
//                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
//                        new ComponentName(getActivity(), MyWallpaperService.class));
//                startActivity(intent);
            }
        }));
        rv_imag_no_fv.setAdapter(new FavWallpaperAdapter(getActivity(), imageLists[0], new FavWallpaperAdapter.OnWallpaperClickListener() {
            @Override
            public void onWallpaperClicked(int position, @NonNull Unsplash_Image wallpaper) {
                images[0] = new Unsplash_Image[imageLists[0].size()];
                for (int i = 0; i < images[0].length; i++) {
                    images[0][i] = imageLists[0].get(i);
                }
                Toast_Snack_Dialog_Utils.show_ShortToast(getActivity(), wallpaper.getId());
                Intent intent = new Intent(getActivity(), WallpapersPagerActivity.class);

                intent.putExtra(Constants.Pager_position, position);
                intent.putExtra(Constants.is_from_fav, true);
                intent.putExtra(Constants.PAGER_LIST_WALL_OBKHECTS, images[0]);
                startActivity(intent);
            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();

    }


}
