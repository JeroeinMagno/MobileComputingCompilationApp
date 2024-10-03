package com.example.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button, helloWorldButton, calculatorButton;
    TextView textView;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        helloWorldButton = findViewById(R.id.HelloWordAct); // Assuming this ID is in your layout
        calculatorButton = findViewById(R.id.CalculatorAct); // Assuming this ID is in your layout
        textView = findViewById(R.id.user_Details);
        VideoView videoView = findViewById(R.id.background_video);
        user = auth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        button.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        });

        helloWorldButton.setOnClickListener(view -> {
            // Navigate to HelloWorldActivity
            Intent intent = new Intent(MainActivity.this, HelloWorldActivity.class);
            startActivity(intent);
        });

        calculatorButton.setOnClickListener(view -> {
            // Navigate to CalculatorActivity
            Intent intent = new Intent(MainActivity.this, CalculatorActivity.class);
            startActivity(intent);
        });

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.background_video);

        videoView.setVideoURI(videoUri);
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true); // Loop the video
            videoView.start();   // Start the video playback
        });
    }
}
