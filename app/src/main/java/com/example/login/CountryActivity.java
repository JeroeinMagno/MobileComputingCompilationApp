package com.example.login;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class CountryActivity extends AppCompatActivity {

    private ImageView flagImageView;
    private TextView countryNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // Set the color using a resource or a color value
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.spotify_dark_gray));

        // Initialize views
        flagImageView = findViewById(R.id.flagImageView);


        // Get the flag ID and country name from the Intent
        int flagId = getIntent().getIntExtra("flagId", -1);

        // Set the flag image and country name
        if (flagId != -1) {
            flagImageView.setImageResource(flagId);
        }

    }
}
