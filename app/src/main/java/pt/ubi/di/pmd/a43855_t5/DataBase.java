package pt.ubi.di.pmd.a43855_t5;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Client.class, Appointment.class}, version = 2, exportSchema = false)
public abstract class DataBase extends RoomDatabase {
    private static DataBase instance;

    public abstract MyDao myDao();

    public static synchronized DataBase getInstance(Context context){
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    DataBase.class, "DB")
                    .createFromAsset("database/DB.db")
                    .allowMainThreadQueries()
                    .setJournalMode(JournalMode.TRUNCATE)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
