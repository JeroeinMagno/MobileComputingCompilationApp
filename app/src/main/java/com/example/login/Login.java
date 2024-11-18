package com.example.login;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.login.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;
    Button ButtonLogin;
    TextView forgotPassword;

    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;

    // Declare MediaPlayer for startup sound and error sound
    MediaPlayer mediaPlayer, errorPlayer;

    @Override
    public void onStart() {
        super.onStart();
        // Initialize FirebaseAuth if null
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }

        // Check if the user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is logged in, navigate to MainActivity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set status bar color
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.spotify_dark_gray));

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Bind the UI elements
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        ButtonLogin = findViewById(R.id.btn_login);
        forgotPassword = findViewById(R.id.forgotPassword);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.RegisterNow);

        // Initialize MediaPlayer for startup and error sounds
        mediaPlayer = MediaPlayer.create(this, R.raw.startup_sound);
        errorPlayer = MediaPlayer.create(this, R.raw.error_sound);

        // Handle register now navigation
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Registration.class);
            startActivity(intent);
            finish();
        });

        // Handle forgot password functionality
        forgotPassword.setOnClickListener(view -> {
            String email = String.valueOf(editTextEmail.getText());

            // Ensure email is not empty
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(Login.this, "Enter your registered email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Display progress bar
            progressBar.setVisibility(View.VISIBLE);

            // Send password reset email
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Login.this, "Error in sending password reset email", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Handle login functionality
        ButtonLogin.setOnClickListener(view -> {
            // Get email and password
            String email = String.valueOf(editTextEmail.getText());
            String password = String.valueOf(editTextPassword.getText());

            // Check for empty fields
            if (TextUtils.isEmpty(email)) {
                progressBar.setVisibility(View.GONE);
                errorPlayer.start();  // Play error sound
                Toast.makeText(Login.this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                progressBar.setVisibility(View.GONE);
                errorPlayer.start();  // Play error sound
                Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Play the startup sound only when fields are not empty
            mediaPlayer.start();

            // Display progress bar
            progressBar.setVisibility(View.VISIBLE);

            // Sign in with Firebase
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Login successful
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Login failed
                            errorPlayer.start();  // Play error sound
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        if (errorPlayer != null) {
            errorPlayer.release();
        }
    }
}
