package dao;

import android.arch.persistence.room.Dao;
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

    @Update
    void update(Member member);

    @Query("UPDATE Member SET name WHERE id=:id")
    void updateName(long id, String name);

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    void insertOne(Member member);
}
