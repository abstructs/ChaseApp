package entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName="Member")
public class Member implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "phoneNumber")
    private String phoneNumber;

    @ColumnInfo(name = "email")
    private String email;

    public Member() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Member(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.phoneNumber = in.readString();
        this.email = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.getId());
        dest.writeString(this.getName());
        dest.writeString(this.getPhoneNumber());
        dest.writeString(this.getEmail());
    }

    public static final Parcelable.Creator<Member> CREATOR
            = new Parcelable.Creator<Member>() {
        public Member createFromParcel(Parcel in) {
            return new Member(in);
        }

        public Member[] newArray(int size) {
            return new Member[size];
        }
    };
}


