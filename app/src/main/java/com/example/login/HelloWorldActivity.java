package com.example.login;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HelloWorldActivity extends AppCompatActivity {

    private TextView clockTextView;
    private TextView greetingTextView;
    private TextView messageTextView;
    private final Handler handler = new Handler();
    private Runnable runnable;
    private static final String TAG = "HelloWorldActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_helloworld);

        clockTextView = findViewById(R.id.clockTextView);
        greetingTextView = findViewById(R.id.greetingTextView);
        messageTextView = findViewById(R.id.messageTextView);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                updateTime();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }

    private void updateTime() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Manila");
        Log.d(TAG, "TimeZone: " + timeZone.getID());

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        sdf.setTimeZone(timeZone);
        String currentTime = sdf.format(new Date());

        clockTextView.setText(currentTime);

        SimpleDateFormat hourFormat = new SimpleDateFormat("HH", Locale.getDefault());
        hourFormat.setTimeZone(timeZone);
        int hour = Integer.parseInt(hourFormat.format(new Date()));

        String greeting;
        String message;

        if (hour >= 0 && hour < 12) {
            greeting = "Good Morning!";
            message = "Start your day with a smile!";
        } else if (hour >= 12 && hour < 18) {
            greeting = "Good Afternoon!";
            message = "Keep up the great work!";
        } else {
            greeting = "Good Evening!";
            message = "Relax and unwind!";
        }

        greetingTextView.setText(greeting);
        messageTextView.setText(message);

        Log.d(TAG, "Current Time: " + currentTime + " | Greeting: " + greeting + " | Message: " + message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
