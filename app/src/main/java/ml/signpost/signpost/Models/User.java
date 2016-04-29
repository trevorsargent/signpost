package ml.signpost.signpost.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by student on 3/17/16.
 */
public class User {

    @SerializedName("id")
    int mId;

    @SerializedName("username")
    String mUserName;

    @SerializedName("name")
    String mName;

    public User(int mId, String mUserName, String mName) {
        this.mId = mId;
        this.mUserName = mUserName;
        this.mName = mName;


    }
}
