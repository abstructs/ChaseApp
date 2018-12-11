package dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import entities.Task;

@Dao
public interface PointTaskDao {
    @Query("SELECT * FROM Task WHERE point_id = :point_id")
    List<Task> getAll(long point_id);

    @Query("DELETE FROM Task WHERE point_id = :point_id")
    void deleteAllTasks(long point_id);
}
