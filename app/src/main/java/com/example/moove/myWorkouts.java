package com.example.moove;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class myWorkouts extends AppCompatActivity {


    private final ArrayList mylist = new ArrayList<>(Arrays.asList(
            new workout(123, 345,"JOGGING", new Date(), 23,20,"0393888487")));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_recent_workouts);

// get the reference of RecyclerView
        RecyclerView rv = (RecyclerView) findViewById(R.id.workouts_list);
// set a LinearLayoutManager with default vertical orientation
        rv.setLayoutManager(new LinearLayoutManager(this));
// call the constructor of MyAdapter to send the reference and data to Adapter
        rv.setAdapter(new myAdapter(this.mylist));
    }
}
