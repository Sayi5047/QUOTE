package io.hustler.qtzy.ui.ORM;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import io.hustler.qtzy.ui.ORM.Dao.ImagesDao;
import io.hustler.qtzy.ui.ORM.Dao.QuotesDao;
import io.hustler.qtzy.ui.ORM.Tables.ImagesTable;
import io.hustler.qtzy.ui.ORM.Tables.QuotesTable;

/*WE USE THIS CLASS ABSTRACT SO THAT THIS CAN BE INSTANTIATED ONLY ONCE & WE REQUIRE ONLY ONE INSTANCE THROUGHOUT LIFE CYCLE*/
@Database(entities = {ImagesTable.class, QuotesTable.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase mAppDatabaseinstance;
    private static final String DATA_BASE_NAME = "quotzy-database";
    private static final Object LOCK = new Object();

    public abstract ImagesDao imagesDao();

    public abstract QuotesDao quotesDao();

    public static AppDatabase  getmAppDatabaseInstance(Context context) {
        if (mAppDatabaseinstance == null) {
            synchronized (LOCK) {
                mAppDatabaseinstance = Room.databaseBuilder(context, AppDatabase.class, DATA_BASE_NAME).allowMainThreadQueries().build();
            }
        }
        return mAppDatabaseinstance;

    }
}
