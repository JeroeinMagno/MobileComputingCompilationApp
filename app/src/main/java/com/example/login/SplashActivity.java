package com.example.login; // Replace with your package

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display
        EdgeToEdge.enable(this);

        // Hide the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Set the content view
        setContentView(R.layout.activity_splash);

        // Find the ImageView and apply the zoom-in animation
        ImageView splashImage = findViewById(R.id.splash_image);

        // Load and start the zoom-in animation
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        splashImage.startAnimation(zoomIn);

        // Find the root view to apply window insets
        View rootView = findViewById(R.id.SplashActivity); // Replace with your root view ID

        // Apply window insets to handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Delay to move to Login activity after the animation ends
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, Login.class);
            startActivity(intent);
            finish();
        }, 2500); // Adjust delay as needed
    }
}
