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
import ml.signpost.signpost.R;

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_sign, container, false);
        ButterKnife.bind(this, rootView);

        mLocation = ((MainActivity) getActivity()).getLastLocation();
        mPosts = ((MainActivity)getActivity()).getPosts();
        mPostNames = new ArrayList<>();
        for (Post p: mPosts){
            mPostNames.add(p.getTitle());
            Log.d("TAG:", p.getTitle());
        }

        mPostNames.add(getString(R.string.fragment_create_sign_newpost));

        mSpinner = new Spinner(getContext());
        mAdapter = new ArrayAdapter<>(getContext(), R.layout.create_sign_spinner_row, mPostNames);
        mSpinner.setAdapter(mAdapter);
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
            mSign = new Sign();

            if(mSelectedItemText.equals(getString(R.string.fragment_create_sign_newpost))){
                //make new post then add sign to post
                makeNewPost();
                mSign.setMessage(messageText);

            } else {
                for(Post p: mPosts){
                    if(p.getTitle().equals(mSelectedItemText)) {
                        mSign.setPostId(p.getId());
                        mSign.setMessage(messageText);
                        break;
                    }
                }
            }
        }
        //TODO update map with new post
        ((MainActivity)getContext()).onBackPressed();
    }

    private int getIndex(String title) {
        for(int i = 0; i<mSpinner.getCount(); i++){
            if(mSpinner.getItemAtPosition(i).toString().equals(title)) return i;
        }
        //Parul's idea (If you're getting a nullpointerexception then you know why)
        return -1;
    }

    private void makeNewPost() {
        Post newPost = new Post();
        newPost.setLat(mLocation.getLatitude());
        newPost.setLng(mLocation.getLongitude());
        FragmentTransaction ft = ((MainActivity)getContext()).getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_create_sign_main_layout, CreatePostDialogFragment.newInstance(newPost));
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onPostMade(Post newPost) {
        mSelectedItemText = newPost.getTitle();
        mAdapter.add(newPost.getTitle());
        mAdapter.notifyDataSetChanged();
        mSpinner.setSelection(getIndex(newPost.getTitle()));

        mSign.setPostId(newPost.getId());
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity)getActivity()).showNav();
    }
}
