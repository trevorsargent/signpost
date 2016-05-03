package ml.signpost.signpost.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ml.signpost.signpost.Fragments.CreateSignFragment;
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

public class MainActivity extends AppCompatActivity {
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    //TAG
    private final String TAG = getClass().getSimpleName();

    //PERMISSIONS
    public static final int CODE_PERMISSION_START = 3;
    public static final int CODE_PERMISSION_HANDLE = 4;

    //LOCATION VARIABLES
    private static int UPDATE_INTERVAL = 10000;
    private static int FATEST_INTERVAL = 5000;
    private static int DISPLACEMENT = 10;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public Location getLastLocation() {
        return mLastLocation;
    }


    //RETROFIT
    Retrofit retrofit;

    public Signpost getBackend() {
        return backend;
    }

    Signpost backend;

    //DATA
    public static final String ARG_POST = "POST.CODEPOST";
    public static final int CODE_POST = 5;

    private ArrayList<Post> mPosts;

    public ArrayList<Post> getPosts() {
        return mPosts;
    }


    @Bind(R.id.activity_main_bottom_navigation)
    AHBottomNavigation mBottomNav;

    FragmentManager fm = getSupportFragmentManager();


    //LIFECYCLE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        prepBottomBar();

        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.activity_main_fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.activity_main_relative_layout, new CreateSignFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl("http://www.signpost.ml/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        backend = retrofit.create(Signpost.class);

        mPosts = new ArrayList<>();

    }

    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }




    //OPERATIONS

    private void handleLocation(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Log.d(TAG, "LOC - Lat: " + lat + " Lng: " + lng);

        backend.locationPosts(lat,lng, 12).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                Log.d("MainMapFragment", "onResponse called");
                mPosts.addAll(response.body());

                fm.beginTransaction()
                        .replace(R.id.activity_main_frame_layout, MainMapFragment.newInstance())
                        .commit();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d("MainMapFragment", t.getMessage());
            }
        });
    }



    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, CODE_PERMISSION_HANDLE);
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLastLocation != null) {
            handleLocation(mLastLocation);
        } else {
            Log.d(TAG, "Couldn't get the location. Make sure location is enabled on the device");
        }
    }

    //LOCATION SERVICES

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, CODE_PERMISSION_START);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CODE_PERMISSION_START :
                startLocationUpdates();
                break;
            case CODE_PERMISSION_HANDLE :
                getCurrentLocation();
                break;
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location changed!", Toast.LENGTH_SHORT).show();

        getCurrentLocation();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: " + connectionResult.getErrorCode());
    }

    //UI
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

    public void startPostDetail(String postTitle){
        Post post = null;
        for(Post e: mPosts){
            if(e.getTitle().equals(postTitle)){
                post = e;
            }
        }

        if(post!=null){
            Intent intent = new Intent(this, PostActivity.class);
            intent.putExtra(ARG_POST, post);
            startActivityForResult(intent, CODE_POST);
        }else{
            Toast.makeText(this, "cannot find post", Toast.LENGTH_SHORT).show();
        }

    }

}
//https://github.com/aurelhubert/ahbottomnavigation