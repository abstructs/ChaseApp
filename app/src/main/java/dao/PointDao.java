package dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
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

    @Query("SELECT * FROM Point WHERE title LIKE :query")
    List<Point> searchByTitle(String query);

    @Query("SELECT * FROM Point WHERE tag LIKE :query")
    List<Point> searchByTag(String query);

    @Query("SELECT * FROM Point WHERE id=:id")
    Point getOne(long id);

    @Update
    void updateOne(Point point);

    @Query("UPDATE Point SET rating=:rating WHERE id=:id")
    void updateRating(long id, int rating);

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    long insertOne(Point point);

    @Delete
    void deleteOne(Point point);
}
