package com.example.login;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.util.Log;

public class FlagActivity extends AppCompatActivity {

    // ListView for displaying the countries
    private ListView countryListView;

    // Array of country names
    private String[] countryNames = {
            "Australia", "China", "India", "Indonesia", "Japan", "New Zealand", "Russia", "South Korea"
    };

    // Array of flag resource IDs
    private int[] flagIds = {
            R.drawable.flag_australia, R.drawable.flag_china, R.drawable.flag_india,
            R.drawable.flag_indonesia, R.drawable.flag_japan, R.drawable.flag_new_zealand,
            R.drawable.flag_russia, R.drawable.flag_south_korea
    };

    // Array of sound resource IDs (ensure you have these files in res/raw)
    private int[] soundIds = {
            R.raw.australia, R.raw.china, R.raw.india, R.raw.indonesia, R.raw.japan,
            R.raw.new_zealand, R.raw.russia, R.raw.south_korea
    };

    // MediaPlayer for background music
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flag);  // Ensure activity_flag layout exists and has the ListView

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // Set the color using a resource or a color value
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.spotify_dark_gray));

        // Bind ListView with its ID
        countryListView = findViewById(R.id.countryListView);

        // Check if countryListView is not null (to avoid a NullPointerException)
        if (countryListView == null) {
            throw new NullPointerException("countryListView not found in the layout.");
        }

        // Set up the ArrayAdapter for the ListView
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countryNames);
        countryListView.setAdapter(countryAdapter);

        // Set up the onItemClick listener for the ListView
        countryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Stop the currently playing music, if any
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

                // Create an Intent to start CountryActivity and pass the selected flag ID and country name
                Intent intent = new Intent(FlagActivity.this, CountryActivity.class);
                intent.putExtra("flagId", flagIds[position]);
                intent.putExtra("countryName", countryNames[position]); // Pass the country name
                startActivity(intent);

                // Play the corresponding country sound
                mediaPlayer = MediaPlayer.create(FlagActivity.this, soundIds[position]);
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mp -> {
                        mp.release();
                    });
                } else {
                    Log.e("FlagActivity", "MediaPlayer is null, sound may not play.");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
