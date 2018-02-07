package com.hustler.quote.ui.Widgets;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.activities.BaseActivity;

/**
 * Created by anvaya5 on 23/01/2018.
 */

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
