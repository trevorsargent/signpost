package ml.signpost.signpost.Models;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * Created by student on 3/17/16.
 */
public class User {

    @SerializedName("id")
    UUID mId;

    @SerializedName("username")
    String mUserName;

    @SerializedName("name")
    String mName;

    public User(UUID mId, String mUserName, String mName) {
        this.mId = mId;
        this.mUserName = mUserName;
        this.mName = mName;


    }
}
