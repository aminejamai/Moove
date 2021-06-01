package com.example.moove.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moove.R;
import com.example.moove.models.Workout;
import com.example.moove.utilities.WorkoutAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.*;

import static com.facebook.FacebookSdk.getApplicationContext;

public class WorkoutHistoryFragment extends Fragment {
    FirebaseFirestore db;

    private static final ArrayList<Workout> workouts = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    public View onCreateView(
        @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.workout_history, container, false);

        db = FirebaseFirestore.getInstance();

        db.collection("workouts").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Workout data = document.toObject(Workout.class);

                    workouts.add(new Workout(data.getDistance(), data.getNumSteps(),
                        data.getTypeOfExercise(), data.getDate(), data.getDuration(),
                        data.getAverageSpeed(), data.getUserID()));
                }

                RecyclerView rv = (RecyclerView) view.findViewById(R.id.workouts_list);
                rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                rv.setAdapter(new WorkoutAdapter(workouts, this));
            }
            else
                Toast.makeText(getContext(),"error getting docs", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
