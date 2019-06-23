package io.hustler.qtzy.ui.ORM.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.hustler.qtzy.ui.ORM.Tables.QuotesTable;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

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

    @Query("SELECT * FROM mstr_quotes WHERE quote_body Like  '%' || :query || '%' ")
    LiveData<List<QuotesTable>> findQuotesByQuery(String query);

    @Query("Select * from mstr_quotes where quote_is_liked = :value")
    LiveData<List<QuotesTable>> getlikedQuotes(Boolean value);


}
