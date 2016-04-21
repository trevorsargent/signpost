package ml.signpost.signpost.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;



/**
 * Created by student on 3/17/16.
 */
public class Post implements Parcelable {

    @SerializedName("id")
    UUID mId;

    @SerializedName("title")
    String mTitle;

    @SerializedName("lat")
    double mLat;

    @SerializedName("long")
    double mLng;

    public Post(UUID mId, String mTitle, double mLat, double mLng) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mLat = mLat;
        this.mLng = mLng;
    }


    protected Post(Parcel in) {
        mId = (UUID) in.readSerializable();
        mTitle = in.readString();
        mLat = in.readDouble();
        mLng = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(mId);
        dest.writeString(mTitle);
        dest.writeDouble(mLat);
        dest.writeDouble(mLng);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public double getLat() {
        return mLat;
    }

    public double getLng() {
        return mLng;
    }

    public String getTitle() {
        return mTitle;
    }

    public UUID getId() {
        return mId;
    }
}
