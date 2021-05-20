package com.example.moove;


import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.collection.ArraySet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.*;

public class myWorkouts extends AppCompatActivity {

    FirebaseFirestore db;




    private static  ArrayList<workout> mylist= new ArrayList<>();


    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_recent_workouts);




   //ArrayList<workout> mylist= new ArrayList<workout>(Arrays.asList(list));
        //System.out.println(mylist.isEmpty()+"   IS EMPTYYYY ????");

// get the reference of RecyclerView

    }


    @Override
    protected void onStart(){
        super.onStart();


        db = FirebaseFirestore.getInstance();
        //mylist=new ArrayList<workout>();



        db.collection("workouts").get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {


                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //workout n=new workout(document.getData());
                                workout data = document.toObject(workout.class);


                                mylist.add(  new workout(data.getDistance(), data.getNumSteps(),data.getTypeOfExercise(),data.getDate(),data.getDuration(),data.getAverageSpeed(),data.getUserID()));
                                Toast.makeText(myWorkouts.this,document.getId() + " => " + data.getTypeOfExercise() , Toast.LENGTH_SHORT).show();

                            }

                            //System.out.println("work =========="+mylist);


                            RecyclerView rv = (RecyclerView) findViewById(R.id.workouts_list);
// set a LinearLayoutManager with default vertical orientation
                            rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
// call the constructor of MyAdapter to send the reference and data to Adapter

                            System.out.println(mylist.toString()+"MMMYYYYY LIST IS HERE");
                            rv.setAdapter(new myAdapter(mylist));

                        } else {
                            Toast.makeText(myWorkouts.this,"error getting docs", Toast.LENGTH_SHORT).show();
                        }
                    }




                });



    }




}
