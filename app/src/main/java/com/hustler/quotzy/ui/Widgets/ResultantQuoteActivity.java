package com.hustler.quotzy.ui.Widgets;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hustler.quotzy.R;
import com.hustler.quotzy.ui.activities.BaseActivity;

/**
 * Created by anvaya5 on 23/01/2018.
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
public class ResultantQuoteActivity extends BaseActivity {
    private LinearLayout root;
    private LinearLayout mainWidgetLayout;
    private LinearLayout rootWidget;
    private TextView quoteBody;
    private TextView quoteAuthor;
    private ImageView widgetEditQuote;

    public ResultantQuoteActivity(LinearLayout root, LinearLayout mainWidgetLayout, LinearLayout rootWidget, TextView quoteBody, TextView quoteAuthor, ImageView widgetEditQuote, ImageView widgetEditRefresh) {
        this.root = root;
        this.mainWidgetLayout = mainWidgetLayout;
        this.rootWidget = rootWidget;
        this.quoteBody = quoteBody;
        this.quoteAuthor = quoteAuthor;
        this.widgetEditQuote = widgetEditQuote;
        this.widgetEditRefresh = widgetEditRefresh;
    }

    private ImageView widgetEditRefresh;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quote_widget_layout);
        root = findViewById(R.id.root);
        mainWidgetLayout = findViewById(R.id.main_widget_layout);
        rootWidget = findViewById(R.id.root_widget);
        quoteBody = findViewById(R.id.quote_body);
        quoteAuthor = findViewById(R.id.quote_author);
        widgetEditQuote = findViewById(R.id.widget_edit_quote);
        widgetEditRefresh = findViewById(R.id.widget_edit_refresh);
    }


}
