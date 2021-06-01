package com.example.moove.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.moove.R;
import com.example.moove.navigation.NavigationHost;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class WorkoutFragment extends Fragment implements OnMapReadyCallback {
    public static final int REQUEST_CODE_FINE_LOCATION = 2;

    private View view;

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    public Button button;
    private TextView time;
    private static final LatLng LOWER_MANHATTAN = new LatLng(40.722543, -73.998585);
    private static final LatLng BROOKLYN_BRIDGE = new LatLng(40.7057, -73.9964);
    private final boolean started = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.workout_fragment, container, false);

        StepCounterFragment.started = false;

        ActivityCompat.requestPermissions(Objects.requireNonNull(this.getActivity()),
            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_FINE_LOCATION);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null)
            mapFragment.getMapAsync(this);

        button = (Button) view.findViewById(R.id.button2);
        button.setOnClickListener(v1 -> {
            if (!StepCounterFragment.started) {
                StepCounterFragment.started = true;
                Fragment fragment = new StepCounterFragment();
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.map, fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(null).commit();

                button.setText("END EXERCICE");
            }
            else {
                StepCounterFragment.started = false;
                Fragment fragment = new WorkoutSummaryFragment();
                ((NavigationHost) getActivity()).navigateTo(fragment, true);

                button.setText("Start new exercice");
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_FINE_LOCATION) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Snackbar.make(
                    view.findViewById(R.id.constraintLayout),
                    getString(R.string.locationPermissionRequired),
                    Snackbar.LENGTH_LONG
                ).show();
            }
            else {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (lastKnownLocation != null)
                        centreMapOnLocation(lastKnownLocation,"Your Location");
                }
            }
        }
    }

    public void centreMapOnLocation(Location location, String title) {
        LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());

        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(userLocation).title("START"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,16));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = Objects.requireNonNull(getActivity()).getIntent();
        if (intent.getIntExtra("Place Number",0) == 0) {
            // Zoom into users location
            locationManager = (LocationManager) Objects.requireNonNull(getActivity())
                .getSystemService(Context.LOCATION_SERVICE);

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    centreMapOnLocation(location,"Your Location");
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {}

                @Override
                public void onProviderEnabled(String s) {}

                @Override
                public void onProviderDisabled(String s) {}
            };

            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,
                    0,locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager
                    .GPS_PROVIDER);
            }
            else
                ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_FINE_LOCATION);
        }
    }
}
