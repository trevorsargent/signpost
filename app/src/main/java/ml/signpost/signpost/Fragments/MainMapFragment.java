package ml.signpost.signpost.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ml.signpost.signpost.Models.Post;
import ml.signpost.signpost.Modules.Signpost;
import ml.signpost.signpost.Modules.SignpostSQLiteOpenHelper;
import ml.signpost.signpost.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static MainMapFragment sInstance;
    @Bind(R.id.layout_map_view_map)
    MapView mMap;
    GoogleMap mGoogleMap;
    private ArrayList<Post> mPosts;
    private SignpostSQLiteOpenHelper mHelper;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    public static final int CODE_PERMISSIONS_REQUEST = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_main_map, container, false);

        ButterKnife.bind(this, rootview);
        mHelper = SignpostSQLiteOpenHelper.getInstance(getContext());

        mMap.getMapAsync(this);
        mMap.onCreate(savedInstanceState);


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
        mMap.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("MainMapFragment", "onMapReady called");

        mGoogleMap = googleMap;

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });

//        populateMap();
    }

    public static MainMapFragment newInstance() {

        if (sInstance == null) {
            MainMapFragment fragment = new MainMapFragment();
            Bundle args = new Bundle();

            fragment.setArguments(args);
            sInstance = fragment;
            return sInstance;
        } else {
            return sInstance;
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, 0, 0, "logout");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.activity_main_frame_layout, new LoginFragment());
                transaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_PERMISSIONS_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://www.signpost.ml/api/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();


                Signpost backend = retrofit.create(Signpost.class);

                mPosts = new ArrayList<>();
                backend.locationPosts(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 10).enqueue(new Callback<List<Post>>() {
                    @Override
                    public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                        Log.d("MainMapFragment", "onResponse called");
                        mPosts.addAll(response.body());
                        populateMap();
                    }

                    @Override
                    public void onFailure(Call<List<Post>> call, Throwable t) {
                        Log.d("MainMapFragment", t.getMessage());
                        Toast.makeText(getContext(), "Error Fetching Posts", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
            return;
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("TAG", "onConnected called");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            requestPermissions(new String[]{Manifest.permission_group.LOCATION},CODE_PERMISSIONS_REQUEST);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        Log.d("TAG", "CURRENT: Lat: " + mLastLocation.getLatitude() + " Lng: " + mLastLocation.getLongitude());


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void populateMap() {
        if (mPosts != null && !mPosts.isEmpty()) {
            for (Post e : mPosts) {
                        Log.d("TAG", "Lat: " + e.getLat() + "Long: " + e.getLng() + "Title: " + e.getTitle());

                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(e.getLat(), e.getLng())).title(e.getTitle()));
            }
        } else {
            Toast.makeText(getContext(), R.string.fragment_map_no_posts, Toast.LENGTH_LONG).show();
        }

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
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())));


    }
}