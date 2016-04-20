package ml.signpost.signpost.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import ml.signpost.signpost.Models.Post;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ml.signpost.signpost.Fragments.MainPopularFragment;
import ml.signpost.signpost.Fragments.SignRecyclerViewAdapter;
import ml.signpost.signpost.Models.Post;
import ml.signpost.signpost.Models.Sign;
import ml.signpost.signpost.Modules.Signpost;
import ml.signpost.signpost.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostActivity extends AppCompatActivity implements SignRecyclerViewAdapter.OnRowClickListener {


    @Bind(R.id.activity_post_view_recycler_view)
    RecyclerView mRecyclerView;
    private Post mPost;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private SignRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_post_view_activity);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.signpost.ml/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Signpost backend = retrofit.create(Signpost.class);

        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        final ArrayList<Sign> list = new ArrayList<>();
        mAdapter = new SignRecyclerViewAdapter(list, this);
        mRecyclerView.setAdapter(mAdapter);

        mPost = getIntent().getParcelableExtra(MainPopularFragment.ARG_POST);

        setContentView(R.layout.activity_post_view_activity);
        Log.d("TAG", "post title: " + mPost.getTitle());

        backend.signsForPost(mPost.getTitle()).enqueue(new Callback<List<Sign>>() {

            @Override
            public void onResponse(Call<List<Sign>> call, Response<List<Sign>> response) {

                list.addAll(response.body());
                for(Sign e : list){
                    Log.d("TAG", e.toString());
                }
                mAdapter.addItems(list);
            }

            @Override
            public void onFailure(Call<List<Sign>> call, Throwable t) {
                Log.d("TAG", t.getMessage());
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onRowClick(Sign sign) {

    }
}
