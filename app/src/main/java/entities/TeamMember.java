package entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName="TeamMember")
public class TeamMember implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "phoneNumber")
    private String phoneNumber;

    @ColumnInfo(name = "email")
    private String email;

    public TeamMember() {
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

    public TeamMember(Parcel in) {
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

    public static final Parcelable.Creator<entities.TeamMember> CREATOR
            = new Parcelable.Creator<entities.TeamMember>() {
        public entities.TeamMember createFromParcel(Parcel in) {
            return new entities.TeamMember(in);
        }

        public entities.TeamMember[] newArray(int size) {
            return new entities.TeamMember[size];
        }
    };
}


