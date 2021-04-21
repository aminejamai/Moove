package com.example.moove.fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.widget.Toast;

import com.example.moove.R;
import com.example.moove.database.DBManager;
import com.example.moove.exceptions.UninitializedDatabaseException;
import com.example.moove.models.User;
import com.example.moove.navigation.NavigationHost;
import com.example.moove.utilities.JsonFormatter;
import com.example.moove.utilities.ProgressBar;
import com.facebook.*;
import com.facebook.CallbackManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.transition.MaterialSharedAxis;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;
import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;

public class LoginPage extends Fragment {

    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, false));
        setExitTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, true));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_page, container, false);

        mCallbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();

        createRequest();

        view.findViewById(R.id.google_login_button).setOnClickListener(view1 -> signIn());

        LoginButton loginButton = view.findViewById(R.id.facebook_login_button);
        loginButton.setPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                Log.d("Facebook Auth", "Event Cancelled ...");
            }
            @Override
            public void onError(FacebookException error) {
                Log.e("Facebook Auth", error.getMessage());
            }
        });

        return view;
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(Objects.requireNonNull(LoginPage.this.getActivity()), task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    User.currentUser = new User(user.getUid(), user.getEmail(),
                        user.getDisplayName(), user.getPhoneNumber(), user.getPhotoUrl(),
                        0, 0, new Timestamp(0, 0));
                    try {
                        DBManager.getInstance().createUser(User.currentUser);
                    } catch (UninitializedDatabaseException e) {
                        Log.e("DatabaseException", e.getMessage());
                    }
                    ((NavigationHost) LoginPage.this.getActivity()).navigateTo(
                        new DashboardPage(), false);
                }
                else
                    Log.e("Facebook Auth", "Failed to authenticate");
            });
    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "not connected", Toast.LENGTH_SHORT).show();
                Log.e("Result: ", e.getMessage());
            }
        }
    }

    private void firebaseAuthWithGoogle(String IdToken) {
        ProgressDialog progressDialog = ProgressBar.createCircularDialog(getContext());
        AuthCredential credential = GoogleAuthProvider.getCredential(IdToken, null);
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(Objects.requireNonNull(LoginPage.this.getActivity()), task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    User.currentUser = new User(user.getUid(), user.getEmail(),
                        user.getDisplayName(), user.getPhoneNumber(), user.getPhotoUrl(),
                        0, 0, new Timestamp(0, 0));
                    try {
                        DBManager.getInstance().createUser(User.currentUser);
                    } catch (UninitializedDatabaseException e) {
                        Log.e("DatabaseException", e.getMessage());
                    }
                    progressDialog.dismiss();
                    ((NavigationHost) LoginPage.this.getActivity()).navigateTo(
                        new DashboardPage(), false);
                }
            });
    }
}
