package dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import entities.Point;
import java.util.List;

@Dao
public interface PointDao {
    @Query("SELECT * FROM Point")
    List<Point> getAll();

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    void insertOne(Point point);
}
