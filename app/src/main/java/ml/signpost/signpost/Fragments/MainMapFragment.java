package ml.signpost.signpost.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import ml.signpost.signpost.Models.Post;
import ml.signpost.signpost.R;
import ml.signpost.signpost.SignpostSQLiteOpenHelper;

public class MainMapFragment extends Fragment implements OnMapReadyCallback {

    private static MainMapFragment sInstance;
    @Bind(R.id.layout_map_view_map)
    MapView mMap;
    GoogleMap mGoogleMap;
    private ArrayList<Post> mPosts;
    private SignpostSQLiteOpenHelper mHelper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_main_map, container, false);

        ButterKnife.bind(this, rootview);
        mHelper = SignpostSQLiteOpenHelper.getInstance(getContext());

        mMap.getMapAsync(this);
        mMap.onCreate(savedInstanceState);
//        String username = null;
//        if (getArguments() != null) {
//
//            mUsername = getArguments().getString(ARG_USER);
//            Log.d("mapfrag username", mUsername);
//        }
//
//        if (mUsername != null && !mUsername.equals("")) {
//            mPins = mHelper.getPinsForUser(mUsername);
//        } else {
//            Log.d("THING", "NULL USER");
//        }
//        setHasOptionsMenu(true);
        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMap.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        // Add a marker in Sydney and move the camera

        if (mPosts != null) {
            for (Post e : mPosts) {
                Log.d("TAG", e.getLat() + e.getLng() + e.getTitle());

                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(e.getLat(), e.getLng())).title(e.getTitle()));
            }
        } else {

            Toast.makeText(this.getActivity(), R.string.fragment_map_no_posts, Toast.LENGTH_LONG).show();
        }

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

//                openDialogFragment(latLng);

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
}