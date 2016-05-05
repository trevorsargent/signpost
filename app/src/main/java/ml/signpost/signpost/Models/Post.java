package ml.signpost.signpost.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by student on 3/17/16.
 */
public class Post implements Serializable {

    @SerializedName("id")
    int mId;

    @SerializedName("title")
    String mTitle;

    @SerializedName("lat")
    double mLat;

    @SerializedName("long")
    double mLng;

    public Post(int mId, String mTitle, double mLat, double mLng) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mLat = mLat;
        this.mLng = mLng;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double mLat) {
        this.mLat = mLat;
    }

    public double getLng() {
        return mLng;
    }

    public void setLng(double mLng) {
        this.mLng = mLng;
    }

    public Post() {

    }

    public String toString(){
        return "Title: " + mTitle +
                ", id: " + mId +
                ", lat: " + mLat +
                ", lng: " + mLng;

    }


//    public void nullId() {
//        mId = null;
//    }
}
