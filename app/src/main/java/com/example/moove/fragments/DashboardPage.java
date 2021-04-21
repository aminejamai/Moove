package com.example.moove.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.moove.R;
import com.example.moove.models.User;
import com.example.moove.navigation.NavigationHost;
import com.example.moove.utilities.CircleCropTransform;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.transition.MaterialSharedAxis;

import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class DashboardPage extends Fragment implements OnNavigationItemSelectedListener {

    private HomeFragment homeFragment;
    private ShareFragment shareFragment;
    private AboutFragment aboutFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, false));
        setExitTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, true));
        setReenterTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, false));
    }

    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dashboard_page, container, false);

        homeFragment = new HomeFragment();

        topBarSetup(view);
        bottomBarSetup(view);

        return view;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String title = (String) item.getTitle();
        switch (title) {
            case "Profile": {
                ((NavigationHost) DashboardPage.this.getActivity())
                    .navigateTo(new ProfilePage(), true);
                break;
            }
            case "Challenges": {
                ((NavigationHost) DashboardPage.this.getActivity())
                    .navigateTo(new ChallengesPage(), true);
                break;
            }
            case "Settings": {
                ((NavigationHost) DashboardPage.this.getActivity())
                    .navigateTo(new SettingsPage(), true);
                break;
            }
            case "Logout": {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to logout ?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        FirebaseAuth.getInstance().signOut();
                        ((NavigationHost) DashboardPage.this.getActivity())
                            .navigateTo(new LoginPage(), false);
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.cancel();
                    });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            }
        }
        return false;
    }

    private void topBarSetup(View view) {
        Toolbar toolbar = view.findViewById(R.id.topAppBar);
        DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);
        NavigationView navigationView = view.findViewById(R.id.navigation_view);
        CoordinatorLayout frame = view.findViewById(R.id.dash_content_frame);

        View headerView = navigationView.getHeaderView(0);
        ((TextView) headerView.findViewById(R.id.nav_email)).setText(User.currentUser.getEmail());
        ((TextView) headerView.findViewById(R.id.nav_username))
            .setText(User.currentUser.getUsername());
        Picasso.get().load(User.currentUser.getPhotoUrl()).transform(new CircleCropTransform())
            .into((ImageView) headerView.findViewById(R.id.nav_profile_pic));

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
            DashboardPage.this.getActivity(),
            drawerLayout,
            toolbar,
            R.string.open_navigation_drawer,
            R.string.close_navigation_drawer
        ) {
            @SuppressLint("NewApi")
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                super.onDrawerSlide(drawerView, slideOffset);
                float moveFactor = (drawerView.getWidth() * slideOffset);

                frame.setTranslationX(moveFactor);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void bottomBarSetup(View view) {
        MeowBottomNavigation bottomNavigation = view.findViewById(R.id.bottomAppBar);

        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_baseline_info_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_baseline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_share_24));
        bottomNavigation.setOnShowListener(item -> {
            Fragment fragment = null;
            switch (item.getId()) {
                case 1: {
                    if (aboutFragment == null)
                        aboutFragment = new AboutFragment();
                    fragment = aboutFragment;
                    break;
                }
                case 2: {
                    fragment = homeFragment;
                    break;
                }
                case 3: {
                    if (shareFragment == null)
                        shareFragment = new ShareFragment();
                    fragment = shareFragment;
                    break;
                }
            }
            loadFragment(fragment);
        });

        bottomNavigation.show(2, true);
        bottomNavigation.setOnClickMenuListener(item -> {});
        bottomNavigation.setOnReselectListener(item -> {});
    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            DashboardPage.this.getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.dash_scroll_view, fragment)
                .commit();
        }
    }
}
