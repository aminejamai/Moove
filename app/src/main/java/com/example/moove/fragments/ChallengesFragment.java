package com.example.moove.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.moove.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChallengesFragment extends Fragment {

    private int numOfGuests = 1;
    private final List<Test> tests = new ArrayList<Test>();

    private class Test {
        private String name;

        public Test(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private class TestAdapter extends BaseAdapter {

        Context context;
        List<Test> testList;
        LayoutInflater layoutInflater;

        public TestAdapter(Context context, List<Test> tests){
            this.context = context;
            this.testList = tests;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return testList.size();
        }

        @Override
        public Test getItem(int position) {
            return testList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = layoutInflater.inflate(R.layout.challenge_row, null);

            Test currentTest = getItem(position);
            TextView textView = convertView.findViewById(R.id.textView22);

            textView.setText(currentTest.getName());
            return convertView;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(
        @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.challenges_page, container, false);

        Button backBtn = view.findViewById(R.id.challenge_back_btn);
        backBtn.setOnClickListener(v -> {
            Objects.requireNonNull(ChallengesFragment.this.getActivity())
                .getSupportFragmentManager().popBackStack();
        });

        dialogSetup(view);

        tests.add(new Test("Amine"));
        tests.add(new Test("Hamza"));
        tests.add(new Test("Soufiane"));
        tests.add(new Test("Ismail"));

        ListView lsView = view.findViewById(R.id.listView1);
        lsView.setAdapter(new TestAdapter(getContext(), tests));

        return view;
    }

    private void dialogSetup(View view) {
        Dialog addChallenge = new Dialog(getContext());
        addChallenge.setOnDismissListener(l1 -> numOfGuests = 1);

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
                    addChallenge.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
