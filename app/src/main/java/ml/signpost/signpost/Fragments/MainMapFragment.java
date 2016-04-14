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
    }

//    private void openDialogFragment(LatLng latLng) {
//        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//        CreatePinDF df = CreatePinDF.newInstance();
//        Bundle bundle = new Bundle();
//        bundle.putString(ARG_USER, mUsername);
//        bundle.putInt(ARG_USERID, mHelper.idFromUserName(mUsername));
//        bundle.putDouble(ARG_LAT, latLng.latitude);
//        bundle.putDouble(ARG_LNG, latLng.longitude);
//        df.setArguments(bundle);
//        //df.setTargetFragment(this, PinMeAlertDialogFragment.REQUEST_CODE);
//        //ft.addToBackStack(null);
//
//
//        df.show(ft, "dialogfragment");
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Bundle info=null;
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == PinMeAlertDialogFragment.REQUEST_CODE){
//            info = data.getBundleExtra(PinMeAlertDialogFragment.ARG_PIN);
//        }
//
//        mPin.setTitle(info.getString("title"));
//        mPin.setDescription(info.getString("desc"));
//        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mPin.getLat(), mPin.getLng())).title(mPin.getTitle()));
//    }

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


//    public void addPin(Pin pin) {
//        mHelper.insertPin(pin);
//        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(pin.getLat(), pin.getLng())).title(pin.getTitle()).snippet(pin.getDescription()));
//    }

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

        mPosts = new ArrayList<>();
        backend.locationPosts(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 10).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                Log.d("MainMapFragment", "onResponse called");
                mPosts.addAll(response.body());

                if (mPosts != null && !mPosts.isEmpty()) {
                    for (Post e : mPosts) {
//                        Log.d("TAG", "Lat: " + e.getLat() + "Long: " + e.getLng() + "Title: " + e.getTitle());

                        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(e.getLat(), e.getLng())).title(e.getTitle()));
                    }
                } else {
                    Toast.makeText(getContext(), R.string.fragment_map_no_posts, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.d("MainMapFragment", t.getMessage());
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