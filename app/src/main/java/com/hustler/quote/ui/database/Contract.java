package com.hustler.quote.ui.database;

import android.provider.BaseColumns;


/**
 * Created by Sayi on 27/12/2017.
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
                        QUOTE_LANGUAGE + " STRING," +
                        QUOTE_IS_LIKED + " INTEGER )";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;


    }

    public static class Images implements BaseColumns {

        public static final String TABLE_NAME = "images";
        public static final String IMAGE_ID = "image_id";
        public static final String IMAGE_USERNAME = "image_username";
        public static final String IMAGE_PROFILEPIC = "image_profilepic";
        public static final String IMAGE_REGULAR = "image_regular";
        public static final String IMAGE_HD = "image_hd";
        public static final String IMAGE_UHD = "image_uhd";
        public static final String IMAGE_DOWNLOAD_LOCATION = "image_download_loc";
        public static final String IMAGE_IS_LIKED = "image_is_liked";

        public static final String CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                IMAGE_ID + " STRING," +
                IMAGE_USERNAME + " STRING," +
                IMAGE_PROFILEPIC + " STRING," +
                IMAGE_REGULAR + " STRING," +
                IMAGE_HD + " STRING," +
                IMAGE_UHD + " STRING," +
                IMAGE_DOWNLOAD_LOCATION + " STRING," +
                IMAGE_IS_LIKED + " INTEGER )";

        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }




}
