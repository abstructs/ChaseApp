package database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import dao.PointDao;
import dao.MemberDao;
import entities.Member;
import entities.Point;
import entities.Task;

@Database(entities = { Point.class, Task.class, Member.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract PointDao pointDao();

    public abstract MemberDao memberDao();

    public static synchronized AppDatabase getAppDatabase(Context context) {
        if(INSTANCE == null) {

            INSTANCE = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "chase-database")
                    .build();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
