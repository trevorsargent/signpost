package ml.signpost.signpost.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ml.signpost.signpost.Activities.MainActivity;
import ml.signpost.signpost.Models.Post;
import ml.signpost.signpost.Modules.Signpost;
import ml.signpost.signpost.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreatePostDialogFragment extends android.support.v4.app.DialogFragment {

    public static final String ARG_POST = "post";

    @Bind(R.id.fragment_dialog_create_post_edittext)
    EditText mEditText;

    @Bind(R.id.fragment_dialog_create_post_positive_button)
    Button mPosButton;

    @Bind(R.id.fragment_dialog_create_post_negative_button)
    Button mNegButton;

    Post mPost;
    Signpost mBackend;
    NewPostMadeListener mListener;


    public interface NewPostMadeListener{
        void onPostMade(Post post);
    }

    public CreatePostDialogFragment() {
        // Required empty public constructor
    }


    public static CreatePostDialogFragment newInstance(Post newPost) {
        CreatePostDialogFragment fragment = new CreatePostDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_POST, newPost);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_post,container,false);
        ButterKnife.bind(this, rootView);
        mPost = (Post) getArguments().getSerializable(ARG_POST);
        mBackend = ((MainActivity)getContext()).getBackend();
        mListener = (CreateSignFragment)getTargetFragment();
        return rootView;
    }

    @OnClick(R.id.fragment_dialog_create_post_positive_button)
    public void onPositiveButtonClicked(){
        //make sure no posts have the same name somehow
        String title = mEditText.getText().toString();
        if(!title.trim().equals("")) {
            mPosButton.setClickable(false);
            mNegButton.setClickable(false);
            mPost.setTitle(title);

            mBackend.createPost(mPost).enqueue(new Callback<List<Post>>() {
                @Override
                public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                    mPost = response.body().get(0);
                }

                @Override
                public void onFailure(Call<List<Post>> call, Throwable t) {
                    Toast.makeText(getContext(), R.string.failed_to_make_post, Toast.LENGTH_SHORT).show();
                }
            });
        mListener.onPostMade(mPost);
        dismiss();
        } else {
            Toast.makeText(getContext(), R.string.please_enter_name, Toast.LENGTH_SHORT).show();
            mPosButton.setClickable(true);
            mNegButton.setClickable(true);
        }
    }

    @OnClick(R.id.fragment_dialog_create_post_negative_button)
    public void onNegativeButtonClicked(){
        dismiss();
    }

}
