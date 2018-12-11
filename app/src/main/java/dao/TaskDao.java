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
public interface TaskDao {
    @Query("SELECT * FROM Task")
    List<Task> getAll();

    //    @Query("SELECT * FROM Point WHERE achieved=:achieved")
    //    Task getAcheieved(int achieved);


    @Update
    void updateOne(Task task);

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    void insertOne(Task task);

    @Delete
    void deleteOne(Task task);
}
