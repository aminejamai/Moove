package com.example.moove;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChallengesActivity extends AppCompatActivity {

    int numOfGuests=1, nHour, nMin;
    Dialog addChallenge;
    Dialog guestsNumber;

    List<Test> listOfTests = new ArrayList<Test>();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.challenges);
        addChallenge = new Dialog(this);

        listOfTests.add(new Test("amine"));
        listOfTests.add(new Test("hamza"));
        listOfTests.add(new Test("soufiane"));
        listOfTests.add(new Test("smail"));

        /*
        *   // code to read data from firebase
        *   FirebaseFirestore db = FirebaseFirestore.getInstance();
        *
        *   db.collection("Challenges").get().
        *                  .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
        *                       @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                        //progressBar.setVisibility(View.GONE);

                                    if(!queryDocumentSnapshots.isEmpty()){

                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                                    for(DocumentSnapshot d : list){

                                        Test p = d.toObject(Test.class);
                                        testList.add(p);

                                    }

                                    //adapter.notifyDataSetChanged();
        *                   }
        *
        * */

        ListView lsView = findViewById(R.id.listView1);
        lsView.setAdapter(new TestAdapter(this, listOfTests));



    }

    public void ShowPopup(View v) {
        Button closeBtn;
        addChallenge.setContentView(R.layout.create_new_challenge);
        closeBtn = addChallenge.findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChallenge.dismiss();
            }
        });
        addChallenge.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addChallenge.show();
    }
    public void ShowPopupNumberGuests(View v){
        //addChallenge.dismiss();
        Button next = findViewById(R.id.guestsNumberNext);
        addChallenge.setContentView(R.layout.guests);

        addChallenge.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addChallenge.show();
    }

    public void increment(View view){
        numOfGuests++;
        TextView num = (TextView) findViewById(R.id.showNumber);
        try{num.setText("TEST");}
        catch(Exception e){
            System.out.println("There is a prblm ");
        }

        System.out.println("test : "+numOfGuests);
    }
    public void dismiss(View v){
        //addChallenge.dismiss();
        addChallenge.setContentView(R.layout.activity_details_dialog);

        addChallenge.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addChallenge.show();

    }

    public void showTimePicker(View view){
        ImageView time = findViewById(R.id.imageView16);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                nHour = c.get(Calendar.HOUR_OF_DAY);
                nMin = c.get(Calendar.MINUTE);

                TimePickerDialog timePicker = new TimePickerDialog(ChallengesActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    }
                }, nHour, nMin, false);
            }
        });
    }
}
