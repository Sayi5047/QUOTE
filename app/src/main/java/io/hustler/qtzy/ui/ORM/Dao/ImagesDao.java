package io.hustler.qtzy.ui.ORM.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.hustler.qtzy.ui.ORM.Tables.ImagesTable;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

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
