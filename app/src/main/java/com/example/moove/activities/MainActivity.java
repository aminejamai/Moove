package com.example.moove.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.moove.database.DBManager;
import com.example.moove.fragments.DashboardFragment;
import com.example.moove.fragments.HeartFragment;
import com.example.moove.fragments.LandingFragment;
import com.example.moove.R;
import com.example.moove.models.User;
import com.example.moove.navigation.NavigationHost;
import com.example.moove.utilities.ProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationHost {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBManager.getInstance().initDB();

        setContentView(R.layout.main_activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            ProgressDialog progressDialog = ProgressBar.createCircularDialog(this);

            String id = user.getUid();
            User.currentUser = new User(id);

            Query query = DBManager.getInstance().getUsersCollectionRef()
                    .whereEqualTo("userId", id);
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        User.currentUser.setEmail(documentSnapshot.getString("email"));
                        User.currentUser.setUsername(documentSnapshot.getString("username"));
                        User.currentUser
                            .setPhoneNumber(documentSnapshot.getString("phoneNumber"));
                        User.currentUser.setPhotoUrl(documentSnapshot.getString("photoUrl"));
                        User.currentUser.setWeight(documentSnapshot.getLong("weight"));
                        User.currentUser.setHeight(documentSnapshot.getLong("height"));
                        User.currentUser
                            .setBirthDate(documentSnapshot.getTimestamp("birthDate"));
                        User.currentUser.setLastHeartRate(Integer.parseInt((Objects.requireNonNull(documentSnapshot.getLong("lastHeartRate")).toString())));
                    }

                    progressDialog.dismiss();
                    getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, new DashboardFragment())
                        .commit();
                }
            });
        }

        else if (savedInstanceState == null) {
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new LandingFragment())
                .commit();
        }
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToStack) {
        FragmentTransaction transaction = getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.container, fragment);

        if (addToStack)
            transaction.addToBackStack(null);

        transaction.commit();
    }
}
