package com.example.simongamefinal;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private TextView scoreTextView;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result); // Set the layout for the activity

        scoreTextView = findViewById(R.id.score_text); // Initialize the score text view

        Intent intent = getIntent(); // Get the intent that started the activity
        if (intent != null && intent.hasExtra("SCORE")) { // Check if the intent has the score extra
            int score = intent.getIntExtra("SCORE", 0); // Get the score from the intent, default to 0 if not found
            scoreTextView.setText("Your score: " + score); // Display the score in the text view

            tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        // TTS engine is successfully initialized.
                        Log.e("XXXXX", "line 151 ");
                    } else {
                        // Failed to initialize TTS engine.
                        Log.e("XXXXX", "line 154 ");
                    }
                }
            });
        }
    }
}
