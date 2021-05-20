package com.example.moove;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class myAdapter extends RecyclerView.Adapter<com.example.moove.myAdapter.MyViewHolder> {

    private final ArrayList workouts;

    public myAdapter(ArrayList workouts) {
        this.workouts = workouts;
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

        private workout currentEtab;

        public MyViewHolder(final View itemView) {
            super(itemView);
            // label = itemView.findViewById(R.id.label);
            steps = itemView.findViewById(R.id.num_steps);
            distance=  itemView.findViewById(R.id.num_distance);
            //quand on click sur l'etablissement
  itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context=view.getContext();
                    Intent intent = new Intent(view.getContext() , pathMap.class);
                    context.startActivity(intent);


                }});

        }



    }




    @Override
    //crée la vu d'une cellule
    // parent pour créer la vu et viewType pour spécifier  la cellule
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //pour créer un laouyt depuis un XML
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    //associe les données aux  vues
    public void onBindViewHolder(MyViewHolder holder, int position) {
        workout Etab = (workout) workouts.get(position);

        holder.steps.setText( "Steps :"+  String.valueOf(Etab.getNumSteps())+"      "+ "AVG Speed :"+Etab.getAverageSpeed()+ " km/h");
        holder.distance.setText("Timer :"+String.valueOf( Etab.getDuration()));
        // holder.label.setText(Etab.getlabel());
        //holder.distance.setImageResource(Etab.getImage());


    }
}