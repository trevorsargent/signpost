package ml.signpost.signpost.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ml.signpost.signpost.Models.Post;
import ml.signpost.signpost.Modules.Signpost;
import ml.signpost.signpost.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class MainPopularFragment extends Fragment implements PostRecyclerViewAdapter.OnRowClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    @Bind(R.id.fragment_main_popular_recycler_view)
    RecyclerView mRecyclerView;
    private PostRecyclerViewAdapter mAdapter;
    private static MainPopularFragment sInstance;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

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




        super.onCreateView(inflater, container, savedInstanceState);

        View rootview = inflater.inflate(R.layout.fragment_main_popular, container, false);

        ButterKnife.bind(this, rootview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final ArrayList<Post> list = new ArrayList<>();
        mAdapter = new PostRecyclerViewAdapter(list, this);
        mRecyclerView.setAdapter(mAdapter);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mGoogleApiClient.connect();


        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onRowClick(Post post) {
        Toast.makeText(getContext(), "congrats you clicked a row", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.signpost.ml/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Signpost backend = retrofit.create(Signpost.class);
        Log.d("TAG", "CURRENT: Lat: " + mLastLocation.getLatitude() + " Lng: " + mLastLocation.getLongitude());

        backend.locationPosts(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 30).enqueue(new Callback<List<Post>>() {


            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                ArrayList<Post> list = new ArrayList<>();
                list.addAll(response.body());
                mAdapter.addItems(list);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d("TAG", t.getMessage());
                Toast.makeText(getContext(), "Error Fetching Posts", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}