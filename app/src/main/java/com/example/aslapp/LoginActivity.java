package com.example.aslapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aslapp.databinding.ActivityLoginBinding;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();
    public static final int AUTH_UI_REQUEST_CODE = 12;

    // Declare variables
    private FirebaseAuth mAuth;
    private ActivityLoginBinding binding;
    private Context context = LoginActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set up instance of binding class
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Remove app bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Get an instance of Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = binding.etUsername.getText().toString();
                String pass = binding.etUsername.getText().toString();
                // Check if e-mail is valid
                if (isValidEmail(user)) {
                    // if valid -> sign in
                    onSignIn(user, pass);
                } else {
                    Toasty.error(context, "Invalid email/password.", Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        binding.rlSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SignupActivity.class);
                startActivity(i);
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

    public void onSignIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            goHome();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toasty.info(context, "Account not found - please sign up.", Toast.LENGTH_SHORT, true).show();
                            // hideKeyboard(binding.getRoot());
                            Intent i = new Intent(context, SignupActivity.class);
                            startActivity(i);
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            goHome();
        }
    }

    public void goHome(){
        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        this.finish();
    }

    // Method that hides soft keyboard
    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // TODO: Delete after we implement our own layouts
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
                .build(), AUTH_UI_REQUEST_CODE);
    }

    // TODO: We might need to delete this too
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AUTH_UI_REQUEST_CODE){
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