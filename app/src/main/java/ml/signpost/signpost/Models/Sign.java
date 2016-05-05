package ml.signpost.signpost.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by student on 3/17/16.
 */
public class Sign {

    @SerializedName("id")
    int mId;

    @SerializedName("post_id")
    int mPostId;

    @SerializedName("user_id")
    int mUserId;

    @SerializedName("message")
    String mMessage;

    public Sign(int mId, int mPostId, int mUserId, String mMessage) {
        this.mId = mId;
        this.mPostId = mPostId;
        this.mUserId = mUserId;
        this.mMessage = mMessage;
    }

    public Sign() {

    }

    public void setPostId(int postId) {
        this.mPostId = postId;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public int getPostId() {
        return mPostId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public String toString(){
        return "id: " + mId +
                "postId: " + mPostId +
                "message: " +mMessage;
    }
}
