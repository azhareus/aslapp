package com.example.aslapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aslapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {

    public static final String TAG = WelcomeActivity.class.getSimpleName();

    // Declare binding variable
    private FirebaseAuth mAuth;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up instance of binding class
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Get an instance of Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        // Check if the app has been run before
        if (!isFirstRun) {
            // show start activity
            if (mAuth.getCurrentUser() != null) {
                Intent i = new Intent(WelcomeActivity.this, HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                WelcomeActivity.this.finish();
            } else {
                Intent i = new Intent(WelcomeActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                WelcomeActivity.this.finish();
            }
        }

        // Set shared preference
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

        // Remove app bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // btLogin.setOnClickListener(v -> goLoginRegister());
        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() == null) {
                    Intent i = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(WelcomeActivity.this, HomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    WelcomeActivity.this.finish();
                }
            }
        });

    }
}