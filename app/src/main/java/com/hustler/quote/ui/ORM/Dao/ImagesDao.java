package com.hustler.quote.ui.ORM.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.hustler.quote.ui.ORM.Tables.ImagesTable;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ImagesDao {

    @Query("SELECT * FROM mstr_images")
    LiveData<List<ImagesTable>> getAllImages();

    @Insert(onConflict = REPLACE)
    void insertimage(ImagesTable mUser);

    @Insert
    void insertAllImages(ImagesTable... mUsersList);

    @Delete
    void delete(ImagesTable mUser);

    @Update
    void updateImage(ImagesTable mUser);

    @Query("SELECT * FROM mstr_images WHERE id = :uId")
    ImagesTable getimagesbyId(int uId);


    @Query("Select * from mstr_images where image_is_liked = :value")
    LiveData<List<ImagesTable>> getAllLikedImages(Boolean value);
}
