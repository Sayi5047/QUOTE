package com.hustler.quote.ui.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by anvaya5 on 19/12/2017.
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
public class DateandTimeutils {
    public static final String DATE_FORMAT_1 = "hh:mm a";
    public static final String DATE_FORMAT_2 = "dd MMM yyyy hh:mm a";
    public static final String DATE_FORMAT_3 = "dd-MMM-yyyy";
    public static final String DATE_FORMAT_4 = "dd MMM yyyy";

    public static String convertDate(long timestamp, String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat(format, Locale.getDefault());
        calendar.setTimeInMillis(timestamp);
        return dateformat.format(calendar.getTime());
    }
}
