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
}
