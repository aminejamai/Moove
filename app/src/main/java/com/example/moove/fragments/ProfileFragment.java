package com.example.moove.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moove.R;
import com.example.moove.models.User;
import com.example.moove.navigation.NavigationHost;
import com.example.moove.utilities.CircleCropTransform;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ProfileFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        Picasso.get().load(User.currentUser.getPhotoUrl()).transform(new CircleCropTransform())
            .into((ImageView) view.findViewById(R.id.profile_image));

        ((TextView) view.findViewById(R.id.full_name)).setText(User.currentUser.getUsername());

        long weight = User.currentUser.getWeight();
        if (weight > 0)
            ((TextView) view.findViewById(R.id.profile_weight)).setText(String.valueOf(weight));

        int age = User.currentUser.getAge();
        if (age > -1)
            ((TextView) view.findViewById(R.id.profile_age)).setText(String.valueOf(age));

        ((Button) view.findViewById(R.id.edit_profile_button)).setOnClickListener(v1 -> {
            ((NavigationHost) getActivity()).navigateTo(new EditProfileFragment(), true);
        });

        ((Button) view.findViewById(R.id.profile_back_btn)).setOnClickListener(v1 -> {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}
