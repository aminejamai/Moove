package com.example.moove;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    Button dashboard ;
    Button authBtn;
    Button challenges;
    TextView authText;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dashboard = findViewById(R.id.lunch_dashboard);

        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setContentView(R.layout.dashboard);
                //new DashboardActivity();
                startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            }
        });

        challenges = findViewById(R.id.lunch_challenges);

        challenges.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChallengesActivity.class));
            }
        });

        authBtn = findViewById(R.id.authBtn);
        authText = findViewById(R.id.authText);

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                authText.setText("Authentication error : "+errString);
                Toast.makeText(MainActivity.this, "Authentication error : "+errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                authText.setText("Authentication succeed ...! ");
                Toast.makeText(MainActivity.this, "Authentication succeed ...! ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                authText.setText("Authentication failed ...! : ");
                Toast.makeText(MainActivity.this, "Authentication Failed ...! ", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Login using fingerprint authentication")
                .setNegativeButtonText("User app password")
                .build();

        authBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }
}