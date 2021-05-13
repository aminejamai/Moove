package com.example.moove;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;


public class myWorkoutSummary extends AppCompatActivity {


    TextView totalSteps, totalCalories, time;
    Button logout;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    public workout myWokout;
    public static final int RUNNING_METS = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
      //FirebaseAuth user= FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        //FirebaseUser user = mAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance();
        myWokout=new workout(123, 0,"JOGGING",  new Date(), stepCounter.duration,20,"0393888487");
        db.collection("workouts")
                .add(myWokout)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(myWorkoutSummary.this, "added with success", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                        Toast.makeText(myWorkoutSummary.this, "failure", Toast.LENGTH_SHORT).show();
                    }
                });
        //name = findViewById(R.id.Title);
        totalSteps = findViewById(R.id.totalSteps);
        totalCalories = findViewById(R.id.totalCalories);
       time=findViewById(R.id.timer);

        totalSteps.setText("Time elapsed:  "+ stepCounter.duration);
        totalCalories.setText("Burned calories :"+ 2*60*0.0175*10+"kcal" );
        //GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);





      

    }

    public double burnedCalories(int minutes, double weight){
        return minutes*weight*0.0175*10;
    }

    public void getWorkoutHistory(View v){
        Intent intent = new Intent(this, myWorkouts.class);
        startActivity(intent);

    }


}