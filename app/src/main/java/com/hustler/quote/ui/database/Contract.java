package com.hustler.quote.ui.database;

import android.provider.BaseColumns;

/**
 * Created by anvaya5 on 27/12/2017.
 */

public class Contract implements BaseColumns {
    private Contract() {
    }
    public static class Quotes implements BaseColumns {
        public static final String TABLE_NAME = "quotes";
        public static final String COLUMN_ID = "idInt";
        public static final String QUOTE_BODY = "quote_body";
        public static final String QUOTE_AUTHOR = "quote_author";
        public static final String QUOTE_CATEGORY = "quote_category";
        public static final String QUOTE_LANGUAGE = "quote_language";
        public static final String QUOTE_IS_LIKED = "quote_is_liked";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_ID + " LONG," +
                        QUOTE_BODY + " STRING," +
                        QUOTE_AUTHOR + " STRING," +
                        QUOTE_CATEGORY + " STRING," +
                        QUOTE_LANGUAGE + " STRING,"+
                        QUOTE_IS_LIKED+" INTEGER )";
        ;

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS" + TABLE_NAME;


    }
}
