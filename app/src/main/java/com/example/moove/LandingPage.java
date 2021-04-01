package com.example.moove;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.transition.MaterialSharedAxis;

public class LandingPage extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setExitTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, true));
        setReenterTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, false));
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.landing_page, container, false);

        MaterialButton getStartedButton = view.findViewById(R.id.land_page_get_started);
        getStartedButton.setOnClickListener(view1 -> ((NavigationHost) getActivity())
                .navigateTo(new LoginPage(), false));

        MaterialTextView tosText = view.findViewById(R.id.land_page_terms);
        tosText.setOnClickListener(view2 -> ((NavigationHost) getActivity())
                .navigateTo(new TosPage(), true));
        
        return view;
    }
}
