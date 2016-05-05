package ml.signpost.signpost.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import ml.signpost.signpost.Activities.MainActivity;
import ml.signpost.signpost.Models.Post;
import ml.signpost.signpost.Modules.Signpost;
import ml.signpost.signpost.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreatePostDialogFragment extends DialogFragment {

    public static final String ARG_POST = "post";

    TextInputEditText mEditText;

    Post mPost;
    Signpost mBackend;
    NewPostMadeListener mListener;


    public interface NewPostMadeListener{
        void onPostMade(Post post);
    }

    public CreatePostDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_create_post,null);
        builder.setView(rootView);
        mEditText = (TextInputEditText) rootView.findViewById(R.id.fragment_dialog_title_edittext);
        builder.setTitle(R.string.create_post)
                .setPositiveButton(R.string.add,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
//                                sendResult(REQUEST_CODE);
                                        onPositiveButtonClicked();
                                    }
                                })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                onNegativeButtonClicked();
                            }
                        });

        return builder.create();

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
 //       View rootView = inflater.inflate(R.layout.fragment_create_post,container,false);
        //mEditText = (TextInputEditText) rootView.findViewById(R.id.fragment_dialog_title_edittext);
//
        mPost = (Post) getArguments().getSerializable(ARG_POST);
//        mPost.nullId();
        mBackend = ((MainActivity)getContext()).getBackend();
        mListener = (CreateSignFragment)getTargetFragment();
//        return rootView;
        return null;
    }


    public void onPositiveButtonClicked(){
       // mEditText.clearFocus();
        //make sure no posts have the same name somehow
        Log.d("TAG", "onPositiveButtonClicked() called");
        String title = mEditText.getText().toString();
        Log.d("TAG", "");
        //TODO figure out why this is empty1?!?!?
        //title = "miller"; // testing purposes
        if(title.trim().length() > 0) {

            mPost.setTitle(title);
            mBackend.createPost(mPost).enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    mPost = response.body();
//                    Log.d("TAG", mPost.toString());
                    if(mPost==null){
                        Toast.makeText(getContext(), R.string.failed_to_make_post, Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                    mListener.onPostMade(mPost);
                    Log.d("Post title: ", mPost.getTitle());
                    dismiss();
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    Log.d("TAG", t.getLocalizedMessage());
                    Toast.makeText(getContext(), R.string.failed_to_make_post, Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(getContext(), R.string.please_enter_name, Toast.LENGTH_SHORT).show();
        }
    }

    public void onNegativeButtonClicked(){
        dismiss();
    }

}
