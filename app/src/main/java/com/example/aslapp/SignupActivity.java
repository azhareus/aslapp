package com.example.aslapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aslapp.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class SignupActivity extends AppCompatActivity {

    public static final String TAG = SignupActivity.class.getSimpleName();

    // Declare variables
    private FirebaseAuth mAuth;
    private ActivitySignupBinding binding;
    private Context context = SignupActivity.this;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up instance of binding class
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Remove app bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Get an instance of Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Retrieve email from login activity
        Intent i = getIntent();
        user = i.getStringExtra("email");
        // Pass email to text field
        binding.etUsername.setText(user);

        binding.btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = binding.etUsername.getText().toString();
                String pass = binding.etUsername.getText().toString();
                // Check if e-mail is valid
                if (isValidEmail(user)) {
                    // if valid -> sign in
                    onEmailSignup(user, pass);
                } else {
                    Toasty.error(context, "Invalid email/password.", Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    // Method to check if provided e-mail address is valid
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void onEmailSignup(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toasty.success(context, "Account created!", Toast.LENGTH_SHORT, true).show();
                            goHome();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toasty.error(context, "Authentication failed.", Toast.LENGTH_SHORT, true).show();
                            // Maybe go back??

                        }
                    }
                });
    }

    public void goHome(){
        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        this.finish();
    }
}