package ml.signpost.signpost.Models;

import java.util.UUID;

/**
 * Created by student on 3/17/16.
 */
public class Sign {
    UUID mId;
    int mPostId;
    int mUserId;
    String mMessage;

    public Sign(UUID mId, int mPostId, int mUserId, String mMessage) {
        this.mId = mId;
        this.mPostId = mPostId;
        this.mUserId = mUserId;
        this.mMessage = mMessage;
    }
}
