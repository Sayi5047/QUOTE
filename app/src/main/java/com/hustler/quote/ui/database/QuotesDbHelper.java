package com.hustler.quote.ui.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.NonNull;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import com.hustler.quote.ui.pojo.Quote;

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
public class QuotesDbHelper extends SQLiteOpenHelper {
    long id;

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "quotescollection.db";

    public QuotesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public QuotesDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public QuotesDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(Contract.Quotes.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Contract.Quotes.SQL_DELETE_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    /*ADD QUOTE ONE BY ONE*/
    public void addQuote(@NonNull Quote quote) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Contract.Quotes.COLUMN_ID, id);
        id++;
        contentValues.put(Contract.Quotes.QUOTE_BODY, quote.getQuote());
        contentValues.put(Contract.Quotes.QUOTE_AUTHOR, quote.getAuthor());
        contentValues.put(Contract.Quotes.QUOTE_CATEGORY, quote.getCategory());
        contentValues.put(Contract.Quotes.QUOTE_LANGUAGE, quote.getLanguage());
        contentValues.put(Contract.Quotes.QUOTE_IS_LIKED, 0);

        sqLiteDatabase.insert(Contract.Quotes.TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    /*GETTING ALL QUOTES AT ONCE*/
    @NonNull
    public List<Quote> getAllQuotes() {
        List<Quote> allQuotes = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String select_query = "SELECT * FROM " + Contract.Quotes.TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(select_query, null);

        while (cursor.moveToNext()) {
            Quote quote = new Quote();
            quote.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Contract.Quotes.COLUMN_ID)));
            quote.setQuote(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_BODY)));
            quote.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_AUTHOR)));
            quote.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_CATEGORY)));
            quote.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_LANGUAGE)));
            quote.setIsLiked(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_IS_LIKED)));
            allQuotes.add(quote);
        }
        cursor.close();
        sqLiteDatabase.close();
        return allQuotes;
    }

    @NonNull
    public List<Quote> getAllQuotesLimited() {
        List<Quote> allQuotes = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String select_query = "SELECT * FROM " + Contract.Quotes.TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(select_query, null);

        while (cursor.moveToPosition(5)) {
            Quote quote = new Quote();
            quote.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Contract.Quotes.COLUMN_ID)));
            quote.setQuote(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_BODY)));
            quote.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_AUTHOR)));
            quote.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_CATEGORY)));
            quote.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_LANGUAGE)));
            quote.setIsLiked(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_IS_LIKED)));

            allQuotes.add(quote);
        }
        cursor.close();
        return allQuotes;
    }

    /*DELETING ALL QUOTES AT ONCE*/
    public void deleteAllQuotes() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(Contract.Quotes.TABLE_NAME, null, null);
        database.close();
    }

    /*GETTER METHODS FOR SEARCH QURIES*/
    @NonNull
    public List<Quote> getQuotesByCategory(String category) {
        List<Quote> categorised_Quotes = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String[] pprojection = new String[]{
                (Contract.Quotes.COLUMN_ID),
                (Contract.Quotes.QUOTE_BODY),
                (Contract.Quotes.QUOTE_AUTHOR),
                (Contract.Quotes.QUOTE_CATEGORY),
                (Contract.Quotes.QUOTE_LANGUAGE),
                (Contract.Quotes.QUOTE_IS_LIKED),
        };

        Cursor cursor = database.
                query(Contract.Quotes.TABLE_NAME,
                        pprojection,
                        " quote_category = ?",
                        new String[]{category},
                        null, null, null, null);

        while (cursor.moveToNext()) {
            Quote quote = new Quote();
            quote.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Contract.Quotes.COLUMN_ID)));
            quote.setQuote(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_BODY)));
            quote.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_AUTHOR)));
            quote.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_CATEGORY)));
            quote.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_LANGUAGE)));
            quote.setIsLiked(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_IS_LIKED)));

            categorised_Quotes.add(quote);
        }
        cursor.close();
        database.close();
        return categorised_Quotes;

    }

    @NonNull
    public List<Quote> getQuotesBystring(String query) {
        List<Quote> categorised_Quotes = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String[] pprojection = new String[]{
                (Contract.Quotes.COLUMN_ID),
                (Contract.Quotes.QUOTE_BODY),
                (Contract.Quotes.QUOTE_AUTHOR),
                (Contract.Quotes.QUOTE_CATEGORY),
                (Contract.Quotes.QUOTE_LANGUAGE),
                (Contract.Quotes.QUOTE_IS_LIKED),
        };

        String rawQury = "SELECT * from " + Contract.Quotes.TABLE_NAME + " WHERE " + Contract.Quotes.QUOTE_BODY + " LIKE " + "'%" + query + "%'";
//        Cursor cursor1 = database.rawQuery(rawQury, pprojection);
        Cursor cursor = database.
                query(Contract.Quotes.TABLE_NAME,
                        pprojection,
                        " quote_body LIKE ?",
                        new String[]{"%" + query + "%"},
                        null, null, null, null);

        while (cursor.moveToNext()) {
            Quote quote = new Quote();
            quote.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Contract.Quotes.COLUMN_ID)));
            quote.setQuote(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_BODY)));
            quote.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_AUTHOR)));
            quote.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_CATEGORY)));
            quote.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_LANGUAGE)));
            quote.setIsLiked(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_IS_LIKED)));

            categorised_Quotes.add(quote);
        }
        cursor.close();
        database.close();
        return categorised_Quotes;

    }

    @NonNull
    public List<Quote> getQuotesByAuthor(String author_Name) {
        List<Quote> categorised_Quotes = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
//
//        String select_by_author = "SELECT * FROM " + Contract.Quotes.TABLE_NAME + " WHERE " + Contract.Quotes.QUOTE_AUTHOR + " = " + author_Name;
//        Cursor cursor = database.rawQuery(select_by_author, null);
        String[] projection = new String[]{
                (Contract.Quotes.COLUMN_ID),
                (Contract.Quotes.QUOTE_BODY),
                (Contract.Quotes.QUOTE_AUTHOR),
                (Contract.Quotes.QUOTE_CATEGORY),
                (Contract.Quotes.QUOTE_LANGUAGE),
                (Contract.Quotes.QUOTE_IS_LIKED),

        };

        Cursor cursor = database.query(Contract.Quotes.TABLE_NAME, projection, " quote_author = ?", new String[]{
                author_Name
        }, null, null, null, null);

        while (cursor.moveToNext()) {
            Quote quote = new Quote();
            quote.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Contract.Quotes.COLUMN_ID)));
            quote.setQuote(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_BODY)));
            quote.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_AUTHOR)));
            quote.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_CATEGORY)));
            quote.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_LANGUAGE)));
            quote.setIsLiked(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_IS_LIKED)));

            categorised_Quotes.add(quote);
        }
        cursor.close();
        database.close();
        return categorised_Quotes;

    }

    @NonNull
    public List<Quote> getQuotesByLanguage(String language_name) {
        List<Quote> categorised_Quotes = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();

//        String select_by_language = "SELECT * FROM " + Contract.Quotes.TABLE_NAME + " WHERE " + Contract.Quotes.QUOTE_LANGUAGE + " = " + language_name;
//        Cursor cursor = database.rawQuery(select_by_language, null);
        String[] pprojection = new String[]{
                (Contract.Quotes.COLUMN_ID),
                (Contract.Quotes.QUOTE_BODY),
                (Contract.Quotes.QUOTE_AUTHOR),
                (Contract.Quotes.QUOTE_CATEGORY),
                (Contract.Quotes.QUOTE_LANGUAGE),
                (Contract.Quotes.QUOTE_IS_LIKED),
        };

        Cursor cursor = database.query(Contract.Quotes.TABLE_NAME, pprojection, " quote_language = ?", new String[]{
                language_name
        }, null, null, null, null);
        while (cursor.moveToNext()) {
            Quote quote = new Quote();
            quote.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Contract.Quotes.COLUMN_ID)));
            quote.setQuote(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_BODY)));
            quote.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_AUTHOR)));
            quote.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_CATEGORY)));
            quote.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_LANGUAGE)));
            quote.setIsLiked(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_IS_LIKED)));

            categorised_Quotes.add(quote);
        }
        cursor.close();
        database.close();
        return categorised_Quotes;

    }

//    public List<String> getallUniqueCategories() {
//        List<Quote> categorised_Quotes = new ArrayList<>();
//        SQLiteDatabase database = this.getReadableDatabase();
//
////        String select_by_language = "SELECT * FROM " + Contract.Quotes.TABLE_NAME + " WHERE " + Contract.Quotes.QUOTE_LANGUAGE + " = " + language_name;
////        Cursor cursor = database.rawQuery(select_by_language, null);
//        String[] pprojection = new String[]{
//                (Contract.Quotes.QUOTE_CATEGORY)        };
//
//        Cursor cursor = database.query(Contract.Quotes.TABLE_NAME, pprojection, " quote_language = ?", new String[]{
//                "English"
//        }, null, null, null, null);
//        while (cursor.moveToNext()) {
//            Quote quote = new Quote();
//            quote.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Contract.Quotes.COLUMN_ID)));
//            quote.setQuote(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_BODY)));
//            quote.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_AUTHOR)));
//            quote.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_CATEGORY)));
//            quote.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_LANGUAGE)));
//            quote.setIsLiked(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_IS_LIKED)));
//
//            categorised_Quotes.add(quote);
//        }
//        cursor.close();
//        database.close();
//        return categorised_Quotes;
//    }


    /*LIKE AND LIKE OPERATIONS*/
    public int addToFavourites(@NonNull Quote quote) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.Quotes.QUOTE_IS_LIKED, 1);
        int rows_Affected = database.
                update(
                        Contract.Quotes.TABLE_NAME,
                        contentValues,
                        Contract.Quotes.QUOTE_BODY + " =?",
                        new String[]{quote.getQuote()}
                );
        database.close();
        return rows_Affected;
    }

    public int removeFromFavorites(@NonNull Quote quote) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.Quotes.QUOTE_IS_LIKED, 0);

        int rowsAffected = database.
                update(
                        Contract.Quotes.TABLE_NAME,
                        contentValues,
                        Contract.Quotes.QUOTE_BODY + " =?",
                        new String[]{quote.getQuote()}
                );
        database.close();
        return rowsAffected;
    }

    /*METHOD TO GET ALL FAVOURITE QUOTES OF USER*/
    @NonNull
    public List<Quote> getAllFav_Quotes() {
        AbstractList<Quote> favourite_Quotes = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.
                query(
                        Contract.Quotes.TABLE_NAME,
                        null,
                        " quote_is_liked =?",
                        new String[]{String.valueOf(1)}
                        , null, null, null, null);
        while (cursor.moveToNext()) {
            Quote quote = new Quote();
            quote.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Contract.Quotes.COLUMN_ID)));
            quote.setQuote(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_BODY)));
            quote.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_AUTHOR)));
            quote.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_CATEGORY)));
            quote.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_LANGUAGE)));
            quote.setIsLiked(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_IS_LIKED)));

            favourite_Quotes.add(quote);
        }
        cursor.close();
        sqLiteDatabase.close();
        return favourite_Quotes;
    }
}

