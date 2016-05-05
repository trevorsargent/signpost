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
    Post mParentPost;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_sign, container, false);

        ButterKnife.bind(this, rootView);

        mBackend = ((MainActivity) getActivity()).getBackend();

        mLocation = ((MainActivity) getActivity()).getLastLocation();
        mPosts = ((MainActivity) getActivity()).getPosts();
        mPostNames = new ArrayList<>();

        mPostNames.add(this.getString(R.string.select_a_post));
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
       // Toast.makeText(getContext(), R.string.fragment_create_sign_item_clicked, Toast.LENGTH_SHORT).show();
        mSelectedItemText = (String) parent.getItemAtPosition(position);
        if(mSelectedItemText.equals(R.string.select_a_post)) {}
        else if (mSelectedItemText.equals("Make New Post")) {
            makeNewPost();
        }
        mParentPost = getPost(mSelectedItemText);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //TODO do nothing?
    }

    @OnClick(R.id.activity_create_sign_saveButton)
    public void onSaveButtonClicked() {
        String messageText = mMessageEditText.getText().toString().trim();

        if (mParentPost == null || mSelectedItemText.equals(R.string.select_a_post))
            Toast.makeText(getContext(), "Please select a post", Toast.LENGTH_LONG).show();
        else if (messageText.equals(""))
            Toast.makeText(getContext(), "No Message", Toast.LENGTH_SHORT).show();
        else {
            mSign = new Sign();

            Log.d("TAG", "gonna do the thing");
            mSign.setPostId(mParentPost.getId());
            mSign.setMessage(messageText);
            Log.d("TAG", mSign.toString());
            mBackend.createSign(mSign).enqueue(new Callback<Sign>() {
                @Override
                public void onResponse(Call<Sign> call, Response<Sign> response) {
                    mSign = response.body();

                    Log.d("TAG", "sign created on server: " + mSign.toString());
                    ((MainActivity)getContext()).mFab.setVisibility(View.VISIBLE);
                    ((MainActivity) getContext()).onBackPressed();
                }

                @Override
                public void onFailure(Call<Sign> call, Throwable t) {
                    Log.d("TAG", t.getLocalizedMessage());
                }
            });


        }
        //((MainActivity) getContext()).onBackPressed();

    }

    private int getIndex(String title) {
        for (int i = 0; i < mSpinner.getCount(); i++) {
            if (mSpinner.getItemAtPosition(i).toString().equals(title)) return i;
        }
        //Parul's idea (If you're getting a nullpointerexception then you know why)
        return -1;
    }

    private Post getPost(String title) {
        for (Post p : mPosts) {
            if (p.getTitle().equals(title)) {
                return p;
            }
        }
        return null;
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
        mParentPost = newPost;
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
