package ml.signpost.signpost.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import ml.signpost.signpost.Fragments.MainPopularFragment;
import ml.signpost.signpost.Models.Post;
import ml.signpost.signpost.R;

public class PostActivity extends AppCompatActivity {

    @Bind(R.id.activity_post_view_post_title_text)
    TextView mPostTitle;

    private Post mPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view_activity);

        ButterKnife.bind(this);

        mPost = getIntent().getParcelableExtra(MainPopularFragment.ARG_POST);

        mPostTitle.setText(mPost.getTitle());
        Log.d("TAG", String.valueOf(mPost.getId()));
    }
}
