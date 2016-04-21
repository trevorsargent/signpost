package ml.signpost.signpost.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ml.signpost.signpost.Activities.PostActivity;
import ml.signpost.signpost.Models.Post;
import ml.signpost.signpost.Modules.Signpost;
import ml.signpost.signpost.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainPopularFragment extends Fragment implements PostRecyclerViewAdapter.OnRowClickListener {

    @Bind(R.id.fragment_main_popular_recycler_view)
    RecyclerView mRecyclerView;
    private PostRecyclerViewAdapter mAdapter;
    private static MainPopularFragment sInstance;
    public static final String ARG_POST = "POST.CODEPOST";
    public static final int CODE_POST = 5;

    public static MainPopularFragment newInstance() {

        if (sInstance == null) {
            MainPopularFragment fragment = new MainPopularFragment();
            Bundle args = new Bundle();

            fragment.setArguments(args);
            sInstance = fragment;
            return sInstance;
        } else {
            return sInstance;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.signpost.ml/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Signpost backend = retrofit.create(Signpost.class);


        super.onCreateView(inflater, container, savedInstanceState);

        View rootview = inflater.inflate(R.layout.fragment_main_popular, container, false);

        ButterKnife.bind(this, rootview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final ArrayList<Post> list = new ArrayList<>();
        mAdapter = new PostRecyclerViewAdapter(list, this);
        mRecyclerView.setAdapter(mAdapter);

        backend.allPosts().enqueue(new Callback<List<Post>>() {


            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                list.addAll(response.body());
                mAdapter.addItems(list);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d("TAG", t.getMessage());
                Toast.makeText(getContext(), "Error Fetching Posts", Toast.LENGTH_SHORT).show();
            }
        });


        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onRowClick(Post post) {

        Intent intent = new Intent(getActivity(), PostActivity.class);
        intent.putExtra(ARG_POST, post);
        startActivityForResult(intent, CODE_POST);

//        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);

        Toast.makeText(getContext(), "congrats you clicked a row", Toast.LENGTH_SHORT).show();
    }
}