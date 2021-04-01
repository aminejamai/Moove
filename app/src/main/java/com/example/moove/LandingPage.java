package com.example.moove;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class LandingPage extends Fragment {

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.landing_page, container, false);

        MaterialButton getStartedButton = view.findViewById(R.id.lp_get_started);
        getStartedButton.setOnClickListener(view1 -> ((NavigationHost) getActivity())
                .navigateTo(new LoginPage(), false));

        return view;
    }
}
