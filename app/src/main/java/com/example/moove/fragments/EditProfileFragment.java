package com.example.moove.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.moove.database.DBManager;
import com.example.moove.exceptions.UninitializedDatabaseException;
import com.example.moove.models.User;
import com.example.moove.navigation.NavigationHost;
import com.example.moove.utilities.CircleCropTransform;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moove.R;
import com.example.moove.utilities.ProgressBar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class EditProfileFragment extends Fragment {
    private TextInputEditText emailInput, nameInput, birthInput, weightInput, heightInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_profile_fragment, container, false);

        ((Button) view.findViewById(R.id.profile_edit_back_btn)).setOnClickListener(v -> {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
        });

        emailInput = view.findViewById(R.id.profile_edit_email);
        nameInput = view.findViewById(R.id.profile_edit_name);
        birthInput = view.findViewById(R.id.profile_edit_birthday);
        weightInput = view.findViewById(R.id.profile_edit_weight);
        heightInput = view.findViewById(R.id.profile_edit_height);

        Picasso.get().load(User.currentUser.getPhotoUrl()).transform(new CircleCropTransform())
            .into((ImageView) view.findViewById(R.id.profile_image1));

        if (User.currentUser.getEmail() != null) {
            emailInput.setText(User.currentUser.getEmail());
        }

        if (User.currentUser.getUsername() != null) {
            nameInput.setText(User.currentUser.getUsername());
        }

        if (User.currentUser.getBirthDate() != null) {
            birthInput.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(User.currentUser.getBirthDate().toDate()));
        }

        if (User.currentUser.getWeight() > 0) {
            weightInput.setText(String.valueOf(User.currentUser.getWeight()));
        }

        if (User.currentUser.getHeight() > 0) {
            heightInput.setText(String.valueOf(User.currentUser.getHeight()));
        }

        ((Button) view.findViewById(R.id.profile_edit_save)).setOnClickListener(v -> {
            ProgressDialog progressDialog = ProgressBar.createCircularDialog(getContext());

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("username", Objects.requireNonNull(nameInput.getText()).toString());
            if (Objects.requireNonNull(birthInput.getText()).toString().matches("^([0-9]){2}\\/[0-9]{2}\\/[0-9]{4}$")) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                try {
                    Timestamp timestamp = new Timestamp(Objects.requireNonNull(formatter.parse(birthInput.getText().toString())));
                    dataMap.put("birthDate", timestamp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            dataMap.put("weight", Long.parseLong(Objects.requireNonNull(weightInput.getText()).toString()));
            dataMap.put("height", Long.parseLong(Objects.requireNonNull(heightInput.getText()).toString()));

            Query query = DBManager.getInstance().getUsersCollectionRef().whereEqualTo("userId", User.currentUser.getId());
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult())
                        DBManager.getInstance().getUsersCollectionRef()
                            .document(documentSnapshot.getId()).set(dataMap, SetOptions.merge()).addOnCompleteListener(t -> {
                                Query updatedQuery = DBManager.getInstance().getUsersCollectionRef().whereEqualTo("userId", User.currentUser.getId());
                                updatedQuery.get().addOnCompleteListener(updatedTask -> {
                                    if (updatedTask.isSuccessful()) {
                                        for (DocumentSnapshot updatedDocumentSnapshot : updatedTask.getResult()) {
                                            User.currentUser.setEmail(updatedDocumentSnapshot.getString("email"));
                                            User.currentUser.setUsername(updatedDocumentSnapshot.getString("username"));
                                            User.currentUser.setPhoneNumber(updatedDocumentSnapshot.getString("phoneNumber"));
                                            User.currentUser.setPhotoUrl(updatedDocumentSnapshot.getString("photoUrl"));
                                            User.currentUser.setWeight(updatedDocumentSnapshot.getLong("weight"));
                                            User.currentUser.setHeight(updatedDocumentSnapshot.getLong("height"));
                                            User.currentUser.setBirthDate(updatedDocumentSnapshot.getTimestamp("birthDate"));
                                            User.currentUser.setLastHeartRate(Integer.parseInt((Objects.requireNonNull(updatedDocumentSnapshot.getLong("lastHeartRate")).toString())));
                                        }
                                    }
                                    progressDialog.dismiss();
                                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                                });
                        });
                }
            });
        });

        return view;
    }
}
