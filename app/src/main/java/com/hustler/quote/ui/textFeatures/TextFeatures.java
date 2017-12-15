package com.hustler.quote.ui.textFeatures;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.hustler.quote.R;

/**
 * Created by anvaya5 on 15/12/2017.
 */

public class TextFeatures {

    public static void apply_Text_Shadow(Activity activity,TextView textView){

        Dialog dialog = new Dialog(activity, R.style.EditTextDialog);
        dialog.setContentView(R.layout.shadow_text_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.EditTextDialog;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

    }
}
