package ml.signpost.signpost.Models;

/**
 * Created by student on 3/17/16.
 */
public class Sign {
    int mId;
    int mPostId;
    int mUserId;
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
}
