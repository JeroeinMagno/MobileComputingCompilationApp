package com.example.login;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button, helloWorldButton, calculatorButton, flagButton;
    Button infraredButton, bluetoothFileTransferButton, bluetoothWirelessTransferButton;
    TextView textView;
    FirebaseUser user;
    MediaPlayer mediaPlayer, clickMediaPlayer, backMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the status bar color
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.spotify_dark_gray));

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        helloWorldButton = findViewById(R.id.HelloWordAct);
        calculatorButton = findViewById(R.id.CalculatorAct);
        flagButton = findViewById(R.id.FlagAct);
        infraredButton = findViewById(R.id.InfraredCommunicationAct);
        bluetoothFileTransferButton = findViewById(R.id.BluetoothFileTransferAct);
        bluetoothWirelessTransferButton = findViewById(R.id.BluetoothDeviceWirelessTransferAct);
        textView = findViewById(R.id.user_Details);
        user = auth.getCurrentUser();

        // Initialize the click sound MediaPlayer
        clickMediaPlayer = MediaPlayer.create(this, R.raw.click_sound); // Use .wav file, e.g., click_sound.wav
        // Initialize the back sound MediaPlayer
        backMediaPlayer = MediaPlayer.create(this, R.raw.back_sound); // Use .wav file, e.g., back_sound.wav

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        button.setOnClickListener(view -> {
            onBackPressed();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        });

        helloWorldButton.setOnClickListener(view -> {
            playClickSound();
            Intent intent = new Intent(MainActivity.this, HelloWorldActivity.class);
            startActivity(intent);
        });

        calculatorButton.setOnClickListener(view -> {
            playClickSound();
            Intent intent = new Intent(MainActivity.this, CalculatorActivity.class);
            startActivity(intent);
        });

        flagButton.setOnClickListener(view -> {
            playClickSound();
            Intent intent = new Intent(MainActivity.this, FlagActivity.class);
            startActivity(intent);
        });

        infraredButton.setOnClickListener(view -> {
            playClickSound();
            Intent intent = new Intent(MainActivity.this, InfraredCommunicationActivity.class);
            startActivity(intent);
        });

        bluetoothFileTransferButton.setOnClickListener(view -> {
            playClickSound();
            Intent intent = new Intent(MainActivity.this, BluetoothFileTransferActivity.class);
            startActivity(intent);
        });

        bluetoothWirelessTransferButton.setOnClickListener(view -> {
            playClickSound();
            Intent intent = new Intent(MainActivity.this, BluetoothDeviceWirelessTransferActivity.class);
            startActivity(intent);
        });
    }

    private void playClickSound() {
        if (clickMediaPlayer != null) {
            clickMediaPlayer.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.rick_and_morty_bgm);
            mediaPlayer.setLooping(true); // Set looping
        }
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (clickMediaPlayer != null) {
            clickMediaPlayer.release();
            clickMediaPlayer = null;
        }
        if (backMediaPlayer != null) {
            backMediaPlayer.release();
            backMediaPlayer = null;
        }
    }

    @Override
    public void onBackPressed() {
        // Play the back sound when back button is pressed
        if (backMediaPlayer != null) {
            backMediaPlayer.start();
        }
        super.onBackPressed(); // Optional: If you want the default behavior (going back)
    }
}
