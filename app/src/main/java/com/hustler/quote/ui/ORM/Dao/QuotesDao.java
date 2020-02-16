package com.hustler.quote.ui.ORM.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hustler.quote.ui.ORM.Tables.QuotesTable;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface QuotesDao {
    @Query("SELECT * FROM mstr_quotes")
    LiveData<List<QuotesTable>> getAll();

    @Insert(onConflict = REPLACE)
    void insertUser(QuotesTable mUser);

    @Insert
    void insertAllUser(QuotesTable... mUsersList);

    @Delete
    void delete(QuotesTable mUser);

    @Update
    void updateUser(QuotesTable mUser);

    @Query("SELECT * FROM mstr_quotes WHERE id = :uId")
    LiveData<QuotesTable> getQuotesById(int uId);


    @Query("SELECT * FROM mstr_quotes WHERE quote_category = :category")
    LiveData<List<QuotesTable>> loadAllbyCategory(String category);

    @Query("SELECT * FROM mstr_quotes WHERE quote_category = :category")
    List<QuotesTable> loadAllbyCategoryNONLIVEDATA(String category);

    @Query("SELECT * FROM mstr_quotes WHERE quote_body Like  '%' || :query || '%' ")
    LiveData<List<QuotesTable>> findQuotesByQuery(String query);

    @Query("Select * from mstr_quotes where quote_is_liked = :value")
    LiveData<List<QuotesTable>> getlikedQuotes(Boolean value);


}
