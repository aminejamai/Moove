package com.example.moove.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.moove.R;
import com.example.moove.utilities.SharedPrefsHandler;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Objects;

public class SettingsFragment extends Fragment {
    SwitchMaterial aSwitch;
    SharedPrefsHandler prefsHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        aSwitch = view.findViewById(R.id.mode_switch);
        prefsHandler = new SharedPrefsHandler(Objects.requireNonNull(getContext()));

        if (prefsHandler.getState())
            ((AppCompatActivity) Objects.requireNonNull(getActivity())).getDelegate()
                    .setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else
            ((AppCompatActivity) Objects.requireNonNull(getActivity())).getDelegate()
                    .setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        if (prefsHandler.getState())
            aSwitch.setChecked(true);

        aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                prefsHandler.setState(true);
                ((AppCompatActivity)getActivity()).getDelegate().setLocalNightMode(AppCompatDelegate
                        .MODE_NIGHT_NO);
            }
            else {
                prefsHandler.setState(false);
                ((AppCompatActivity)getActivity()).getDelegate().setLocalNightMode(AppCompatDelegate
                        .MODE_NIGHT_YES);
            }
        });

        return view;
    }
}
