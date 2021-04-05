package com.example.moove;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        setUsername();
        setDate();
        listenBackToHome();
    }

    public void setUsername(){
        // This founction must get the user name in order to use it
        TextView username = findViewById(R.id.profileName);
        username.setText("Amine Soufiane");
    }

    public void setDate(){
        SimpleDateFormat formater = null;
        Date aujourdhui = new Date();

        formater = new SimpleDateFormat("d MMM yyyy");
        //System.out.println(formater.format(aujourdhui));

        TextView username = findViewById(R.id.dash_date);
        username.setText(formater.format(aujourdhui));
    }

    public void listenBackToHome(){
        ImageView back = findViewById(R.id.back_icon);

        back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
//                System.out.println("Image clicked");
                startActivity(new Intent(DashboardActivity.this, MainActivity.class));
            }
        });
    }
}
