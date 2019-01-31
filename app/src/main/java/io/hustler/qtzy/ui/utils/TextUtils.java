package io.hustler.qtzy.ui.utils;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.graphics.Palette;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;
import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.CustomSpan.CustomTypefaceSpan;
import io.hustler.qtzy.ui.customviews.Sticker.StickerTextView;

import static io.hustler.qtzy.ui.apiRequestLauncher.Constants.FONT_CIRCULAR;

/**
 * Created by Sayi on 02-12-2017.
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
public class TextUtils {
    public static void setFont(Activity activity, TextView tv, String fontname) {
        if (null != activity) {
            tv.setTypeface(Typeface.createFromAsset(activity.getApplicationContext().getAssets(), fontname));
        }
    }

    public static void setFont(Activity activity, StickerTextView tv, String fontname) {
        if (null != activity) {
            tv.setTypeface(Typeface.createFromAsset(activity.getApplicationContext().getAssets(), fontname));
        }
    }
    public static void setEdit_Font(Activity activity, EditText et, String fontname) {
        et.setTypeface(Typeface.createFromAsset(activity.getApplicationContext().getAssets(), fontname));
    }

    public static void set_Radio_font(Activity activity, RadioButton et, String fontname) {
        et.setTypeface(Typeface.createFromAsset(activity.getApplicationContext().getAssets(), fontname));
    }

    public static String getArrayItem_of_String(Activity activity, String name, int index) {
        int arrayid;
        arrayid = activity.getResources().getIdentifier(name, "array", activity.getApplicationContext().getPackageName());
        TypedArray typedArray;
        typedArray = activity.getResources().obtainTypedArray(arrayid);
        String string_from_array = typedArray.getString(index);
        typedArray.recycle();
        return string_from_array;

    }

    public static void applyTextShadow(TextView tv, float raduis, float x, float y, int color) {
        tv.setShadowLayer(raduis, x, y, color);
    }

    public static void findText_and_applyTypeface(ViewGroup viewGroup, Activity activity) {
        int childcount = viewGroup.getChildCount();
        for (int i = 0; i < childcount; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                findText_and_applyTypeface((ViewGroup) view, activity);
            } else if (view instanceof TextView) {
                setFont(activity, ((TextView) view), FONT_CIRCULAR);
            } else if (view instanceof EditText) {
                setEdit_Font(activity, ((EditText) view), FONT_CIRCULAR);
            } else if (view instanceof Button) {
                setFont(activity, ((TextView) view), FONT_CIRCULAR);
            } else if (view instanceof RadioButton) {
                set_Radio_font(activity, ((RadioButton) view), FONT_CIRCULAR);
            }
        }
    }

    public static void findText_and_applycolor(ViewGroup viewGroup, Activity activity, Palette.Swatch swatch) {
        int childcount = viewGroup.getChildCount();
        for (int i = 0; i < childcount; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                findText_and_applycolor((ViewGroup) view, activity, swatch);
            } else if (view instanceof TextView) {
                if(null==swatch){
                    ((TextView) view).setTextColor(Color.BLACK);

                }else {
                    ((TextView) view).setTextColor(swatch.getRgb());

                }
            }
//            }else if (view instanceof ImageView) {
//                ((ImageView) view).setBackgroundColor(swatch.getRgb());
//            }
        }
    }

    public static void findText_and_applyamim_slideup(ViewGroup viewGroup, Activity activity) {
        int childcount = viewGroup.getChildCount();
        for (int i = 0; i < childcount; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                findText_and_applyamim_slideup((ViewGroup) view, activity);
            } else if (view instanceof TextView) {
                view.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.slideup));
            }
        }
    }

    public static void findText_and_applyamim_slidedown(ViewGroup viewGroup, Activity activity) {
        int childcount = viewGroup.getChildCount();
        for (int i = 0; i < childcount; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                findText_and_applyamim_slideup((ViewGroup) view, activity);
            } else if (view instanceof TextView) {
                view.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.slidedown));
            }
        }
    }

    public static int getMatColor(Activity activity, String md_300) {
        int returnColor = Color.BLACK;
        int arrayId = activity.getResources().getIdentifier(md_300, "array", activity.getApplicationContext().getPackageName());

        if (arrayId != 0) {
            TypedArray colors = activity.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.BLACK);
            colors.recycle();
        }
        return returnColor;
    }

    public static int getMainMatColor(String arrayname, Activity activity) {
        int returnColor = Color.BLACK;
        int arrayId = activity.getResources().getIdentifier(arrayname, "array", activity.getApplicationContext().getPackageName());

        if (arrayId != 0) {
            TypedArray colors = activity.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.BLACK);
            colors.recycle();
        }
        return returnColor;
    }


    public static void setMenu_Font(Menu navigationView, Activity activity) {
        Menu menu = navigationView;
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            SubMenu subMenu = menuItem.getSubMenu();
            if (subMenu != null) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenu_Item = subMenu.getItem();
                    setFont_For_menu(subMenu_Item, activity);
                }
            }
            setFont_For_menu(menuItem, activity);

        }
    }

    private static void setFont_For_menu(MenuItem subMenu, Activity activity) {
        try {
            if (null != activity) {
                Typeface typeface = Typeface.createFromAsset(activity.getResources().getAssets(), FONT_CIRCULAR);
                SpannableString spannableString = new SpannableString(subMenu.getTitle());
                spannableString.setSpan(new CustomTypefaceSpan("", typeface), 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                subMenu.setTitle(spannableString);
            }
        } catch (RuntimeException rte) {
            FirebaseCrash.log(rte.getStackTrace().toString());
        }
    }
}
