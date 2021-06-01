package com.example.moove.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moove.R;
import com.example.moove.models.User;

import java.util.Locale;

public class HomeFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment, container, false);

        if (User.currentUser.getLastHeartRate() > 0)
        ((TextView) view.findViewById(R.id.home_heart_rate))
            .setText(String.format(Locale.getDefault(), "%d BPM", User.currentUser.getLastHeartRate()));

        return view;
    }
}
