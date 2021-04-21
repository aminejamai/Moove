package com.example.moove.fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moove.R;

public class ChallengesPage extends Fragment {

    private int numOfGuests = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(
        @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.challenges_page, container, false);

        Button backBtn = view.findViewById(R.id.challenge_back_btn);
        backBtn.setOnClickListener(v -> {
            ChallengesPage.this.getActivity().getSupportFragmentManager().popBackStack();
        });

        dialogSetup(view);

        return view;
    }

    private void dialogSetup(View view) {
        Dialog addChallenge = new Dialog(getContext());

        ImageView addChallengeButton = view.findViewById(R.id.addChallengeBtn);
        addChallengeButton.setOnClickListener(v1 -> {
            changeDialogGravity(addChallenge, Gravity.CENTER);
            addChallenge.setContentView(R.layout.create_new_challenge);
            Button nextBtn = addChallenge.findViewById(R.id.new_challenge_next_btn);
            nextBtn.setOnClickListener(v2 -> {
                addChallenge.setContentView(R.layout.guests);
                changeDialogGravity(addChallenge, Gravity.BOTTOM);

                TextView guestsNumber = addChallenge.findViewById(R.id.showNumber);
                ImageView incrementBtn = addChallenge.findViewById(R.id.increment);
                incrementBtn.setOnClickListener(v3 -> {
                    numOfGuests++;
                    guestsNumber.setText(String.format("%d", numOfGuests));
                });
                ImageView decrementBtn = addChallenge.findViewById(R.id.decrement);
                decrementBtn.setOnClickListener(v3 -> {
                    if (numOfGuests > 1) {
                        numOfGuests--;
                        guestsNumber.setText(String.format("%d", numOfGuests));
                    }
                });

                Button guestNextBtn = addChallenge.findViewById(R.id.guestsNumberNext);
                guestNextBtn.setOnClickListener(v3 -> {
                    addChallenge.setContentView(R.layout.activity_details_dialog);
                    addChallenge.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    addChallenge.show();
                });

                addChallenge.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                addChallenge.show();
            });

            Button closeBtn = addChallenge.findViewById(R.id.close_btn);
            closeBtn.setOnClickListener(v3 -> addChallenge.dismiss());

            addChallenge.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            addChallenge.show();
        });
    }

    private void changeDialogGravity(Dialog dialog, int targetGravity) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowLayoutParams = window.getAttributes();
        windowLayoutParams.gravity = targetGravity;
        window.setAttributes(windowLayoutParams);
    }
}
