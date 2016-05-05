package ml.signpost.signpost.Fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ml.signpost.signpost.Activities.MainActivity;
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
public class CreateSignFragment extends Fragment implements AdapterView.OnItemSelectedListener, CreatePostDialogFragment.NewPostMadeListener {

    public static final String ARG_LNG = "lng";
    public static final String ARG_LAT = "lat";

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
    Location mLocation;
    String mSelectedItemText;
    ArrayAdapter<String> mAdapter;
    Sign mSign;
    Signpost mBackend;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_sign, container, false);

        ButterKnife.bind(this, rootView);

        mBackend = ((MainActivity) getActivity()).getBackend();

        mLocation = ((MainActivity) getActivity()).getLastLocation();
        mPosts = ((MainActivity) getActivity()).getPosts();
        mPostNames = new ArrayList<>();
        for (Post p : mPosts) {
            mPostNames.add(p.getTitle());
//            Log.d("TAG:", p.getTitle());
        }

        mPostNames.add(getString(R.string.fragment_create_sign_newpost));

        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mPostNames);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
        mSpinner.setOnItemSelectedListener(this);

        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getContext(), R.string.fragment_create_sign_item_clicked, Toast.LENGTH_SHORT).show();
        mSelectedItemText = (String) parent.getItemAtPosition(position);
        if(mSelectedItemText.equals("Make New Post")){
            makeNewPost();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //TODO do nothing?
    }

    @OnClick(R.id.activity_create_sign_saveButton)
    public void onSaveButtonClicked() {
        String messageText = mMessageEditText.getText().toString().trim();

        if (mSelectedItemText != null)
            Toast.makeText(getContext(), "Please select a post", Toast.LENGTH_LONG).show();
        else if (messageText.equals(""))
            Toast.makeText(getContext(), "No Message", Toast.LENGTH_SHORT).show();
        else {
            mSign = new Sign();
            for (Post p : mPosts) {
                if (p.getTitle().equals(mSelectedItemText)) {
                    Log.d("TAG", "gonna do the thing");
                    mSign.setPostId(p.getId());
                    mSign.setMessage(messageText);
                    mBackend.createSign(mSign).enqueue(new Callback<List<Sign>>() {
                        @Override
                        public void onResponse(Call<List<Sign>> call, Response<List<Sign>> response) {
                            mSign = response.body().iterator().next();
                            Log.d("TAG", "sign created on server");
                        }

                        @Override
                        public void onFailure(Call<List<Sign>> call, Throwable t) {
                            Log.d("TAG", t.getLocalizedMessage());
                        }
                    });
                    break;
                }
            }

        }
        //TODO update map with new post
        ((MainActivity)

                getContext()

        ).

                onBackPressed();

    }

    private int getIndex(String title) {
        for (int i = 0; i < mSpinner.getCount(); i++) {
            if (mSpinner.getItemAtPosition(i).toString().equals(title)) return i;
        }
        //Parul's idea (If you're getting a nullpointerexception then you know why)
        return -1;
    }

    private void makeNewPost() {
        Post newPost = new Post();

        newPost.setLat(mLocation.getLatitude());
        newPost.setLng(mLocation.getLongitude());
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        CreatePostDialogFragment df = CreatePostDialogFragment.newInstance(newPost);
        df.setTargetFragment(this, 0);
        df.show(ft, "dialog fragment!");
    }

    @Override
    public void onPostMade(Post newPost) {
        mSelectedItemText = newPost.getTitle();
        mPosts.add(newPost);
        mAdapter.add(newPost.getTitle());
        mAdapter.notifyDataSetChanged();
        mSpinner.setSelection(getIndex(newPost.getTitle()));
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) getActivity()).showNav();
    }
}
