package com.hustler.quote.ui.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hustler.quote.ui.pojo.Quote;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anvaya5 on 27/12/2017.
 */

public class QuotesDbHelper extends SQLiteOpenHelper {
    long id ;

    public static final int DATABASE_VERSION = 1;
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
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contract.Quotes.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Contract.Quotes.SQL_DELETE_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public void addQuote(Quote quote) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Contract.Quotes.COLUMN_ID, id);
        id++;
        contentValues.put(Contract.Quotes.QUOTE_BODY, quote.getQuote_body());
        contentValues.put(Contract.Quotes.QUOTE_AUTHOR, quote.getQuote_author());
        contentValues.put(Contract.Quotes.QUOTE_CATEGORY, quote.getQuote_category());
        contentValues.put(Contract.Quotes.QUOTE_LANGUAGE, quote.getQuote_language());

        sqLiteDatabase.insert(Contract.Quotes.TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    public List<Quote> getAllQuotes() {
        List<Quote> allQuotes = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String select_query = "SELECT * FROM " + Contract.Quotes.TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(select_query, null);

        while (cursor.moveToNext()) {
            Quote quote = new Quote();
            quote.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Contract.Quotes.COLUMN_ID)));
            quote.setQuote_body(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_BODY)));
            quote.setQuote_author(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_AUTHOR)));
            quote.setQuote_category(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_CATEGORY)));
            quote.setQuote_language(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_LANGUAGE)));
            allQuotes.add(quote);
        }
        cursor.close();
        return allQuotes;
    }
    public List<Quote> getAllQuotesLimited() {
        List<Quote> allQuotes = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        String select_query = "SELECT * FROM " + Contract.Quotes.TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(select_query, null);

        while (cursor.moveToPosition(5)) {
            Quote quote = new Quote();
            quote.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Contract.Quotes.COLUMN_ID)));
            quote.setQuote_body(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_BODY)));
            quote.setQuote_author(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_AUTHOR)));
            quote.setQuote_category(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_CATEGORY)));
            quote.setQuote_language(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_LANGUAGE)));
            allQuotes.add(quote);
        }
        cursor.close();
        return allQuotes;
    }

    public void deleteAllQuotes() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(Contract.Quotes.TABLE_NAME, null, null);
        database.close();
    }


    public List<Quote> getQuotesByCategory(String category) {
        List<Quote> categorised_Quotes = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String[] pprojection=new String[]{
                (Contract.Quotes.COLUMN_ID),
                (Contract.Quotes.QUOTE_BODY),
                (Contract.Quotes.QUOTE_AUTHOR),
                (Contract.Quotes.QUOTE_CATEGORY),
                (Contract.Quotes.QUOTE_LANGUAGE),
        };

//        String select_by_category = "SELECT * FROM " + Contract.Quotes.TABLE_NAME + " WHERE " + Contract.Quotes.QUOTE_CATEGORY + " = " + category;
        Cursor cursor=database.query(Contract.Quotes.TABLE_NAME ,pprojection," quote_category = ?",new String[]{
                category
        },null,null,null,null);
//        Cursor cursor = database.rawQuery(select_by_category, null);

        while (cursor.moveToNext()) {
            Quote quote = new Quote();
            quote.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Contract.Quotes.COLUMN_ID)));
            quote.setQuote_body(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_BODY)));
            quote.setQuote_author(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_AUTHOR)));
            quote.setQuote_category(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_CATEGORY)));
            quote.setQuote_language(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_LANGUAGE)));
            categorised_Quotes.add(quote);
        }
        cursor.close();
        return categorised_Quotes;

    }

    public List<Quote> getQuotesByAuthor(String author_Name) {
        List<Quote> categorised_Quotes = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
//
//        String select_by_author = "SELECT * FROM " + Contract.Quotes.TABLE_NAME + " WHERE " + Contract.Quotes.QUOTE_AUTHOR + " = " + author_Name;
//        Cursor cursor = database.rawQuery(select_by_author, null);
        String[] projection=new String[]{
                (Contract.Quotes.COLUMN_ID),
                (Contract.Quotes.QUOTE_BODY),
                (Contract.Quotes.QUOTE_AUTHOR),
                (Contract.Quotes.QUOTE_CATEGORY),
                (Contract.Quotes.QUOTE_LANGUAGE),
        };

//        String select_by_category = "SELECT * FROM " + Contract.Quotes.TABLE_NAME + " WHERE " + Contract.Quotes.QUOTE_CATEGORY + " = " + category;
        Cursor cursor=database.query(Contract.Quotes.TABLE_NAME ,projection," quote_author = ?",new String[]{
                author_Name
        },null,null,null,null);

        while (cursor.moveToNext()) {
            Quote quote = new Quote();
            quote.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Contract.Quotes.COLUMN_ID)));
            quote.setQuote_body(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_BODY)));
            quote.setQuote_author(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_AUTHOR)));
            quote.setQuote_category(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_CATEGORY)));
            quote.setQuote_language(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_LANGUAGE)));
            categorised_Quotes.add(quote);
        }
        cursor.close();
        return categorised_Quotes;

    }

    public List<Quote> getQuotesByLanguage(String language_name) {
        List<Quote> categorised_Quotes = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();

//        String select_by_language = "SELECT * FROM " + Contract.Quotes.TABLE_NAME + " WHERE " + Contract.Quotes.QUOTE_LANGUAGE + " = " + language_name;
//        Cursor cursor = database.rawQuery(select_by_language, null);
        String[] pprojection=new String[]{
                (Contract.Quotes.COLUMN_ID),
                (Contract.Quotes.QUOTE_BODY),
                (Contract.Quotes.QUOTE_AUTHOR),
                (Contract.Quotes.QUOTE_CATEGORY),
                (Contract.Quotes.QUOTE_LANGUAGE),
        };

        Cursor cursor=database.query(Contract.Quotes.TABLE_NAME ,pprojection," quote_language = ?",new String[]{
                language_name
        },null,null,null,null);
        while (cursor.moveToNext()) {
            Quote quote = new Quote();
            quote.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Contract.Quotes.COLUMN_ID)));
            quote.setQuote_body(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_BODY)));
            quote.setQuote_author(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_AUTHOR)));
            quote.setQuote_category(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_CATEGORY)));
            quote.setQuote_language(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Quotes.QUOTE_LANGUAGE)));
            categorised_Quotes.add(quote);
        }
        cursor.close();
        return categorised_Quotes;

    }
}

