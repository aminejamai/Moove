package com.example.moove.utilities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moove.R;
import com.example.moove.fragments.MapPathFragment;
import com.example.moove.models.Workout;
import com.example.moove.navigation.NavigationHost;

import java.util.ArrayList;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.MyViewHolder> {
    private final ArrayList<Workout> workouts;
    private final Fragment fragment;

    public WorkoutAdapter(ArrayList<Workout> workouts, Fragment fragment) {
        this.workouts = workouts;
        this.fragment = fragment;
    }

    @Override
    // retourne le nb total de cellule que contiendra la liste
    public int getItemCount() {
        return workouts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // private final TextView label;
        private final TextView steps;
        private final TextView distance;

        private Workout currentEtab;

        public MyViewHolder(final View itemView) {
            super(itemView);
            // label = itemView.findViewById(R.id.label);
            steps = itemView.findViewById(R.id.num_steps);
            distance=  itemView.findViewById(R.id.num_distance);
            //quand on click sur l'etablissement
            itemView.setOnClickListener(view -> {
                ((NavigationHost) fragment.getActivity()).navigateTo(new MapPathFragment(),
                    true);
            });
        }
    }

    @Override
    //crée la vu d'une cellule
    // parent pour créer la vu et viewType pour spécifier  la cellule
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //pour créer un layout depuis un XML
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.workout_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    //associe les données aux  vues
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Workout Etab = (Workout) workouts.get(position);

        holder.steps.setText( "Steps :" + String.valueOf(Etab.getNumSteps()) +
            "      AVG Speed :" + Etab.getAverageSpeed() + " km/h");
        holder.distance.setText("Timer :"+String.valueOf(Etab.getDuration()));
    }
}
