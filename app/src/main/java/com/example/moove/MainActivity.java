package com.example.moove;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements NavigationHost {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new LandingPage())
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
