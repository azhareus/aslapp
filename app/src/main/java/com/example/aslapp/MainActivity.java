package com.example.aslapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.aslapp.fragments.HomeFragment;
import com.example.aslapp.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        // define your fragments here
        final Fragment fragment1 = new HomeFragment();
        final Fragment fragment2 = new ProfileFragment();
//        final Fragment fragment3 = new ProfileFragment();

        BottomNavigationView bottomNavigationView =
                (BottomNavigationView) findViewById(R.id.bottom_navigation);

        // handle navigation selection
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment;
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                fragment = fragment1;
                                fragmentManager.beginTransaction().replace(R.id.flContainer,
                                        fragment)
                                        .commit();
                                break;
                            case R.id.action_profile:
                                fragment = fragment2;
                                fragmentManager.beginTransaction().replace(R.id.flContainer,
                                        fragment)
                                        .commit();
                                break;
                            case R.id.action_camera:
                                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                                startActivity(intent);
                                break;
                            default:
                                fragment = fragment1;
                                fragmentManager.beginTransaction().replace(R.id.flContainer,
                                        fragment)
                                        .commit();
                                break;

                        }
//                        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment)
//                                .commit();
                        return true;
                    }
                });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);

    }

    public void onLaunchCamera(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Create a File reference for future access
//        photoFile = getPhotoFileUri(photoFileName);
//
//        // wrap File object into a content provider
//        // required for API >= 24
//        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files
//        // -with-api-24-or-higher
//        Uri fileProvider = FileProvider.getUriForFile(MainActivity.this, "com.codepath" +
//                        ".fileprovider",
//                photoFile);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app
        // will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }


}