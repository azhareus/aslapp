package com.example.aslapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    public static final String TAG = WelcomeActivity.class.getSimpleName();
    public static final int AUTHUI_REQUEST_CODE = 12;
    Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        btLogin = findViewById(R.id.btLogin);
        btLogin.setOnClickListener(v -> goLoginRegister());

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            Log.d(TAG, "User not null " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
            Intent i = new Intent(this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            this.finish();
        }else{
            Log.d(TAG, "User is null");
        }
    }

    public void goLoginRegister(){
        List<AuthUI.IdpConfig> provider = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                //new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build()
        );

        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(provider)
                .setIsSmartLockEnabled(false)
                .setTheme(R.style.GreenTheme)
                .build(), AUTHUI_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AUTHUI_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                // Successfully created a new user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "OnActivityResult: " + user.getEmail());
                // Check if user is existing or new
                if(user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()){
                    // Toast.makeText(this, "Thank you for signing up!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "Thank you for signing up!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show();
                }
                // Go to main
                Intent i = new Intent(this, HomeActivity.class);
                startActivity(i);
                this.finish();
            }
            else {
                Log.d(TAG, "Unable to login: ");
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if(response == null){
                    Log.d(TAG, "Cancelled sign in request");
                }
                else{
                    Log.e(TAG, "Error: " + response.getError().getErrorCode());
                }
            }
        }
    }
}