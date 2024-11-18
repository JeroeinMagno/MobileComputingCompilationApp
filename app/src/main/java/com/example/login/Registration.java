package com.example.login;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.FirebaseApp;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Registration extends AppCompatActivity {

    // Declare UI elements
    TextInputEditText editTextEmail, editTextPassword, editTextPhone, editTextInterest, editTextBirthDate;
    Button ButtonReg;
    Spinner spinnerProvince;
    RadioButton radioMale, radioFemale;
    ProgressBar progressBar;
    TextView textView;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    MediaPlayer mediaPlayer, errorPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mediaPlayer = MediaPlayer.create(this, R.raw.startup_sound);
        errorPlayer = MediaPlayer.create(this, R.raw.error_sound);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // Set the color using a resource or a color value
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.spotify_dark_gray));

        // Initialize FirebaseApp and FirebaseAuth
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Bind the UI components
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextPhone = findViewById(R.id.phone);
        editTextInterest = findViewById(R.id.Interest);
        editTextBirthDate = findViewById(R.id.BirthDate);
        ButtonReg = findViewById(R.id.btn_registration);
        spinnerProvince = findViewById(R.id.spinner_province);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.LogInNow);

        // Set up the spinner for provinces
        List<String> provinces = Arrays.asList(
                "Abra", "Agusan del Norte", "Agusan del Sur", "Aklan", "Albay",
                "Antique", "Apayao", "Aurora", "Basilan", "Bataan",
                "Batanes", "Batangas", "Benguet", "Biliran", "Bohol",
                "Bukidnon", "Bulacan", "Cagayan", "Camarines Norte", "Camarines Sur",
                "Camiguin", "Capiz", "Catanduanes", "Cavite", "Cebu",
                "Cotabato", "Davao de Oro", "Davao del Norte", "Davao del Sur", "Davao Occidental",
                "Davao Oriental", "Dinagat Islands", "Eastern Samar", "Guimaras", "Ifugao",
                "Ilocos Norte", "Ilocos Sur", "Iloilo", "Isabela", "Kalinga",
                "La Union", "Laguna", "Lanao del Norte", "Lanao del Sur", "Leyte",
                "Maguindanao del Norte", "Maguindanao del Sur", "Marinduque", "Masbate", "Metro Manila",
                "Misamis Occidental", "Misamis Oriental", "Mountain Province", "Negros Occidental", "Negros Oriental",
                "Northern Samar", "Nueva Ecija", "Nueva Vizcaya", "Occidental Mindoro", "Oriental Mindoro",
                "Palawan", "Pampanga", "Pangasinan", "Quezon", "Quirino",
                "Rizal", "Romblon", "Samar", "Sarangani", "Siquijor",
                "Sorsogon", "South Cotabato", "Southern Leyte", "Sultan Kudarat", "Sulu",
                "Surigao del Norte", "Surigao del Sur", "Tarlac", "Tawi-Tawi", "Zambales",
                "Zamboanga del Norte", "Zamboanga del Sur", "Zamboanga Sibugay"
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinces);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(adapter);

        // Handle Login redirect
        textView.setOnClickListener(view -> {
            // Navigate to Login activity
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        });

        // Handle Registration button click
        ButtonReg.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);

            // Gather all input values
            String email = String.valueOf(editTextEmail.getText());
            String password = String.valueOf(editTextPassword.getText());
            String phone = String.valueOf(editTextPhone.getText());
            String interest = String.valueOf(editTextInterest.getText());
            String birthDate = String.valueOf(editTextBirthDate.getText());
            String province = spinnerProvince.getSelectedItem().toString();
            String gender = radioMale.isChecked() ? "Male" : "Female";

            // Check for empty inputs
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(Registration.this, "Enter email", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                errorPlayer.start(); // Play error sound
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(Registration.this, "Enter password", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                errorPlayer.start(); // Play error sound
                return;
            }

            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(Registration.this, "Enter phone number", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                errorPlayer.start(); // Play error sound
                return;
            }

            if (TextUtils.isEmpty(interest)) {
                Toast.makeText(Registration.this, "Enter interest", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                errorPlayer.start();
                return;
            }

            if (TextUtils.isEmpty(birthDate)) {
                Toast.makeText(Registration.this, "Enter birth date", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                errorPlayer.start();
                return;
            }

            if (TextUtils.isEmpty(interest)) {
                Toast.makeText(Registration.this, "Enter interest", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                errorPlayer.start(); // Play error sound
                return;
            }

            if (TextUtils.isEmpty(birthDate)) {
                Toast.makeText(Registration.this, "Enter birth date", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                errorPlayer.start(); // Play error sound
                return;
            }

            // Create user with email and password
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String uid = user.getUid();
                                saveUserInfo(uid, email, phone, gender, province, interest, birthDate);
                            }
                        } else {
                            // Log the error message for FirebaseAuth failure
                            Log.e("Registration", "Registration failed: " + Objects.requireNonNull(task.getException()).getMessage());
                            Toast.makeText(Registration.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            errorPlayer.start();
                        }
                    });
        });
    }

    private void saveUserInfo(String uid, String email, String phone, String gender, String province, String interest, String birthDate) {
        // Validate fields to ensure they are not null
        if (email == null || phone == null || gender == null || province == null || interest == null || birthDate == null) {
            Toast.makeText(Registration.this, "All fields must be filled out", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            errorPlayer.start();
            return;
        }

        // Create user object
        User user = new User(email, phone, gender, province, interest, birthDate);

        // Save user info in Firestore
        db.collection("users").document(uid).set(user)
                .addOnSuccessListener(aVoid -> {
                    // Data successfully saved, proceed to redirect to Login
                    Toast.makeText(Registration.this, "User data saved.", Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();

                    // Clear form fields after successful registration
                    clearFormFields();

                    // After registration, navigate to Login activity
                    Intent intent = new Intent(Registration.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the back stack
                    startActivity(intent);
                    finish(); // Close the Registration activity
                })
                .addOnFailureListener(e -> {
                    Log.e("Registration", "Failed to save user data: " + e.getMessage());
                    Toast.makeText(Registration.this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    errorPlayer.start();
                });
    }




    private void clearFormFields() {
        // Clear all form fields
        editTextEmail.setText("");
        editTextPassword.setText("");
        editTextPhone.setText("");
        editTextInterest.setText("");
        editTextBirthDate.setText("");
        spinnerProvince.setSelection(0); // Reset to default (first province)
        radioMale.setChecked(false);
        radioFemale.setChecked(false);
    }

    public class User {
        private String email, phone, gender, province, interest, birthDate;

        // Constructor
        public User(String email, String phone, String gender, String province, String interest, String birthDate) {
            this.email = email;
            this.phone = phone;
            this.gender = gender;
            this.province = province;
            this.interest = interest;
            this.birthDate = birthDate;
        }

        // Getters and Setters
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getGender() { return gender; }
        public String getProvince() { return province; }
        public String getInterest() { return interest; }
        public String getBirthDate() { return birthDate; }
    }

    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        if (errorPlayer != null) {
            errorPlayer.release();
        }
    }
}

