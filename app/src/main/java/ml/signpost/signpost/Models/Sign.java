package ml.signpost.signpost.Models;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * Created by student on 3/17/16.
 */
public class Sign {

    @SerializedName("id")
    UUID mId;

//    @SerializedName("post_id")
//    UUID mPostId;
//
//    @SerializedName("user_id")
//    UUID mUserId;

    @SerializedName("message")
    String mMessage;

    public Sign(UUID mId, UUID mPostId, UUID mUserId, String mMessage) {
        this.mId = mId;
//        this.mPostId = mPostId;
//        this.mUserId = mUserId;
        this.mMessage = mMessage;
    }

    public String getMessage() {
        return mMessage;
    }

    public String toString(){
        return "{message: " + mMessage + "}";
    }
}
