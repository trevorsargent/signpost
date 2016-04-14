package ml.signpost.signpost.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ml.signpost.signpost.Fragments.MainPopularFragment;
import ml.signpost.signpost.Models.Post;
import ml.signpost.signpost.Models.Sign;
import ml.signpost.signpost.Modules.Signpost;
import ml.signpost.signpost.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.signpost.ml/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Signpost backend = retrofit.create(Signpost.class);
        final ArrayList<Sign> list = new ArrayList<>();
        backend.signsForPost(mPost.getTitle()).enqueue(new Callback<List<Sign>>() {


            @Override
            public void onResponse(Call<List<Sign>> call, Response<List<Sign>> response) {
                list.addAll(response.body());
//                mAdapter.addItems(list);
            }

            @Override
            public void onFailure(Call<List<Sign>> call, Throwable t) {
                Log.d("TAG", t.getMessage());
                Toast.makeText(getApplicationContext(), "Error Fetching Posts", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
