package com.example.moove;


import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;


import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;


public class myWorkoutSummary extends AppCompatActivity {


    TextView name, mail, time;
    Button logout;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    public workout myWokout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
      //FirebaseAuth user= FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        //FirebaseUser user = mAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance();
        myWokout=new workout(123, 345,"JOGGING", new Date(), 23,20,"0393888487");
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
        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);
       time=findViewById(R.id.timer);

        mail.setText("Time elapsed:  "+ stepCounter.duration);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);





       /* logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                System.out.println("WWWWWWWWW=====>");
            }
        });
*/

    }
}