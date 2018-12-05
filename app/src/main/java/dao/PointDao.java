package dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import entities.Point;
import java.util.List;

@Dao
public interface PointDao {
    @Query("SELECT * FROM Point")
    List<Point> getAll();

    @Update
    void update(Point point);

    @Query("UPDATE Point SET rating=:rating WHERE id=:id")
    void updateRating(long id, int rating);

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    void insertOne(Point point);
}
