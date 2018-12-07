package dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;
import entities.Member;

@Dao
public interface MemberDao {

    @Query("SELECT * FROM Member")
    List<Member> getAll();

    @Query("SELECT * FROM Member WHERE id=:id")
    Member getOne(long id);

    @Update
    void updateOne(Member member);

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    void insertOne(Member member);

    @Delete
    void deleteOne(Member member);
}
