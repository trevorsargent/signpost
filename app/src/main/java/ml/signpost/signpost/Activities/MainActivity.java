package ml.signpost.signpost.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ml.signpost.signpost.Fragments.MainMapFragment;
import ml.signpost.signpost.Fragments.MainPopularFragment;
import ml.signpost.signpost.Models.Post;
import ml.signpost.signpost.Modules.Signpost;
import ml.signpost.signpost.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    Retrofit retrofit;
    Signpost backend;

    private ArrayList<Post> mPosts;


    @Bind(R.id.activity_main_bottom_navigation)
    AHBottomNavigation mBottomNav;

    FragmentManager fm = getSupportFragmentManager();
    private GoogleApiClient mGoogleApiClient;

    public ArrayList<Post> getPosts() {
        return mPosts;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        prepBottomBar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_main_fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl("http://www.signpost.ml/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        backend = retrofit.create(Signpost.class);

        mPosts = new ArrayList<>();
        backend.allPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                Log.d("MainMapFragment", "onResponse called");
                mPosts.addAll(response.body());
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d("MainMapFragment", t.getMessage());
            }
        });

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mGoogleApiClient.connect();
    }


    void prepBottomBar() {
        AHBottomNavigationItem map = new AHBottomNavigationItem("Map", R.drawable.ic_maps_place, Color.parseColor("#455C65"));
        AHBottomNavigationItem popular = new AHBottomNavigationItem("Popular", R.drawable.ic_maps_local_bar, Color.parseColor("#455C65"));
        AHBottomNavigationItem nearby = new AHBottomNavigationItem("Nearby", R.drawable.ic_maps_local_restaurant, Color.parseColor("#455C65"));

        mBottomNav.addItem(map);
        mBottomNav.addItem(popular);
        mBottomNav.addItem(nearby);

        mBottomNav.setDefaultBackgroundColor(R.color.colorPrimaryDark);

        mBottomNav.setAccentColor(R.color.colorAccent);
        mBottomNav.setInactiveColor(R.color.colorInactive);

        mBottomNav.setColored(true);
        mBottomNav.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {
                if (position == 0) {
                    fm.beginTransaction()
                            .replace(R.id.activity_main_frame_layout, MainMapFragment.newInstance())
                            .commit();
                } else if (position == 1) {
                    fm.beginTransaction()
                            .replace(R.id.activity_main_frame_layout, MainPopularFragment.newInstance())
                            .commit();
                }
            }
        });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //TODO get the location!!!!
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
//https://github.com/aurelhubert/ahbottomnavigation