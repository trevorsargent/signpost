package ml.signpost.signpost.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import ml.signpost.signpost.Activities.MainActivity;
import ml.signpost.signpost.Models.Post;
import ml.signpost.signpost.Modules.SignpostSQLiteOpenHelper;
import ml.signpost.signpost.R;

public class MainMapFragment extends Fragment implements OnMapReadyCallback {

    private final String TAG = getClass().getSimpleName();
    private static MainMapFragment sInstance;
    @Bind(R.id.layout_map_view_map)
    MapView mMap;
    GoogleMap mGoogleMap;
    private SignpostSQLiteOpenHelper mHelper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_main_map, container, false);

        ButterKnife.bind(this, rootview);
        mHelper = SignpostSQLiteOpenHelper.getInstance(getContext());

        mMap.getMapAsync(this);
        mMap.onCreate(savedInstanceState);


        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMap.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        Log.d(TAG, "onMapReady called");

        mGoogleMap = googleMap;

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });

        populateMap();
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


    private void populateMap() {
        ArrayList<Post> posts = ((MainActivity) getActivity()).getPosts();
        if (posts != null && !posts.isEmpty()) {
            for (Post e : posts) {
//                        Log.d(TAG, "Lat: " + e.getLat() + "Long: " + e.getLng() + "Title: " + e.getTitle());

                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(e.getLat(), e.getLng())).title(e.getTitle()));
            }
        } else {
            Toast.makeText(getContext(), R.string.fragment_map_no_posts, Toast.LENGTH_LONG).show();
        }
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(((MainActivity) getActivity()).getLastLocation().getLatitude(), ((MainActivity) getActivity()).getLastLocation().getLongitude()), 15));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);

    }
}