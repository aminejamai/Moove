package com.example.moove.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moove.R;
import com.example.moove.models.Workout;
import com.example.moove.navigation.NavigationHost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class WorkoutSummaryFragment extends Fragment {
    TextView totalSteps, totalCalories, time;

    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    public Workout myWorkout;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    public View onCreateView(
        @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.workout_summary, container, false);

        Button historyButton = view.findViewById(R.id.recent_workouts);
        historyButton.setOnClickListener(v1 -> {
            ((NavigationHost) getActivity()).navigateTo(new WorkoutHistoryFragment(), true);
        });

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
        myWorkout = new Workout(123, 0,"JOGGING", new Date(), StepCounterFragment.duration,20,"0393888487");
        db.collection("workouts")
            .add(myWorkout)
            .addOnSuccessListener(documentReference -> {
                //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Log.w(TAG, "Error adding document", e);
                    Toast.makeText(getContext(), "failure", Toast.LENGTH_SHORT).show();
                }
            });
        totalSteps = view.findViewById(R.id.totalSteps);
        totalCalories = view.findViewById(R.id.totalCalories);
//        time = view.findViewById(R.id.timer);

        totalSteps.setText("Time elapsed: "+ StepCounterFragment.duration);
        totalCalories.setText("Burned calories :" + 2*60*0.0175*10 + "kcal");

        return view;
    }

    public double burnedCalories(int minutes, double weight){
        return minutes*weight*0.0175*10;
    }
}
