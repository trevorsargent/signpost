package ml.signpost.signpost.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import ml.signpost.signpost.Models.User;
import ml.signpost.signpost.Modules.Signpost;
import ml.signpost.signpost.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {

    @Bind(R.id.fragment_login_username_edittext)
    EditText mEditText;
    @Bind(R.id.fragment_login_login_button)
    Button mLoginButton;
    @Bind(R.id.fragment_login_adduser_button)
    Button mAddUserButton;

    Signpost mBackend;

    String mUsername;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, rootView);
        mBackend = ((MainActivity)getContext()).getBackend();
        return rootView;

    }

    @OnClick(R.id.fragment_login_login_button)
    void onLoginButtonClicked(){
        String username = mEditText.getText().toString().trim();
        mBackend.getUser(username).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.body()==null){
                    Toast.makeText(getContext(), "User does not exist!", Toast.LENGTH_SHORT).show();
                } else {
                    MainActivity activity = ((MainActivity)getContext());
                    activity.setUser(response.body().get(0));
                    activity.onBackPressed();

                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getContext(), "User does not exist!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.fragment_login_adduser_button)
    void onAddUserButtonClicked() {
        mUsername = mEditText.getText().toString().trim();
        if (mUsername.equals("")) {
            Toast.makeText(getContext(), "Enter username", Toast.LENGTH_SHORT).show();
        } else {
            mBackend.getUser(mUsername).enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                    if (response.body()==null) {
                        Log.d("TAG", "username not found hopefully boop");
                        User user = new User();
                        user.setUserName(mUsername);
                        mBackend.createUser(user);
                        mBackend.getUser(mUsername).enqueue(new Callback<List<User>>() {
                            @Override
                            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                                if(response.body()==null)Log.d("TAG","response was null :'(");
                                ((MainActivity) getContext()).setUser(response.body().get(0));
                                ((MainActivity) getContext()).onBackPressed();
                            }

                            @Override
                            public void onFailure(Call<List<User>> call, Throwable t) {
                                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "Username already exists!", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    //TODO danger zone might not have ref to username

                }
            });
        }
    }
}
