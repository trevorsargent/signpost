package ml.signpost.signpost.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ml.signpost.signpost.Models.Post;
import ml.signpost.signpost.Models.Sign;
import ml.signpost.signpost.Modules.Signpost;
import ml.signpost.signpost.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dominic on 4/28/2016.
 */
public class CreateSignFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public static final String ARG_LNG = "long";
    public static final String ARG_LAT = "lat";

    @Bind(R.id.activity_create_sign_mapview)
    GoogleMap mGMap;

    @Bind(R.id.activity_create_sign_messageEditText)
    EditText mMessageEditText;

    @Bind(R.id.activity_create_sign_radiobutton1)
    RadioButton mRadioButton1;

    @Bind(R.id.activity_create_sign_radiobutton2)
    RadioButton mRadioButton2;

    @Bind(R.id.activity_create_sign_radiobutton3)
    RadioButton mRadioButton3;

    @Bind(R.id.activity_create_sign_radiobutton4)
    RadioButton mRadioButton4;

    @Bind(R.id.activity_create_sign_radiobutton5)
    RadioButton mRadioButton5;

    @Bind(R.id.activity_create_sign_spinner)
    Spinner mSpinner;

    @Bind(R.id.activity_create_sign_saveButton)
    Button mSaveButton;

    ArrayList<Post> mPosts;
    ArrayList<String> mPostNames;
    Signpost mBackend;
    double mLat;
    double mLng;
    String mSelectedItemText;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_sign, container, false);
        ButterKnife.bind(this, rootView);

        //TODO Get location from arguments
        mLat = getArguments().getDouble(ARG_LAT);
        mLng = getArguments().getDouble(ARG_LNG);

        mBackend.locationPosts(mLat, mLng, 5).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                mPosts.addAll(response.body());
                for(Post p: mPosts) {
                    //TODO this might be addresses not strings?
                    mPostNames.add(p.getTitle());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(getContext(), R.string.fragment_create_sign_failure_nearby_post_get, Toast.LENGTH_LONG).show();
            }
        });

        mPostNames.add(getString(R.string.fragment_create_sign_newpost));

        mSpinner = new Spinner(getContext());
        ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.create_sign_spinner_row, mPostNames);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);

        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //TODO set post at location as parent post
        Toast.makeText(getContext(), R.string.fragment_create_sign_item_clicked, Toast.LENGTH_SHORT).show();
        mSelectedItemText = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //TODO do nothing?
    }

    @OnClick(R.id.activity_create_sign_saveButton)
    public void onSaveButtonClicked(){
        String messageText = mMessageEditText.getText().toString().trim();

        if(mSelectedItemText!=null)
            Toast.makeText(getContext(), "Please select a post", Toast.LENGTH_LONG).show();
        else if(messageText.equals(""))
            Toast.makeText(getContext(), "No Message", Toast.LENGTH_SHORT).show();
        else {
            Sign sign = new Sign();

            if(mSelectedItemText.equals(R.string.fragment_create_sign_newpost)){
                //make new post then add sign to post
            } else {
                for(Post p: mPosts){
                    if(p.getTitle().equals(mSelectedItemText)) {
                        sign.setPostId(p.getId());
                        sign.setMessage(messageText);
                        break;
                    }
                }
            }

        }
    }

}
