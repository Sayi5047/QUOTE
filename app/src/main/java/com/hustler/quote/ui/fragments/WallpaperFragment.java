package com.hustler.quote.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.adapters.WallpaperPagerAdapter;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.utils.TextUtils;

/**
 * Created by Sayi on 26-01-2018.
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
public class WallpaperFragment extends android.support.v4.app.Fragment {


    TextView credit;

    TabLayout walltabs;
    ViewPager wallPager;
    WallpaperPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_wallpaper_fragment, container, false);

        credit = view.findViewById(R.id.crdit);
//        walltabs = view.findViewById(R.id.wall_tabLayout);
        wallPager = view.findViewById(R.id.wall_viewPager);
//        editTabLayout();
        adapter = new WallpaperPagerAdapter(getActivity(), getChildFragmentManager());
        wallPager.setAdapter(adapter);


        credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://unsplash.com?utm_source=Quotzy&utm_medium=referral"));
                startActivity(intent);
            }
        });
        return view;
    }

    private void editTabLayout() {
        ViewGroup vg = (ViewGroup) walltabs.getChildAt(0);
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
                    TextUtils.setFont(getActivity(), textView, Constants.FONT_ZINGCURSIVE);
                }
            }
        }

    }












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


//    public void clearEverything() {
//        rv.setAdapter(null);
//// 2. Notify the adapter of the update
//
//        try {
//            wallAdapter2.notifyDataSetChanged();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            wallAdapter.notifyDataSetChanged();
//
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//
//
//        // or notifyItemRangeRemoved
//// 3. Reset endless scroll listener when performing a new search
////        infiniteScrolListener.resetState();
//        infiniteScrolListener.resetState();
//    }
}
