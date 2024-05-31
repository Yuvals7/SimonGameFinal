package com.example.simongamefinal;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ResultActivity extends AppCompatActivity {

    private TextView scoreTextView;
    private TextToSpeech tts;

    private void convertTextToSpeech(String text) {
        if (tts != null) {
            Log.e("XXXXX", text );
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void setLanguageAndVoice() {
        Locale desiredLocale = Locale.US; // Change to the desired language/locale
        tts.setLanguage(desiredLocale);

        Set<Voice> voices = tts.getVoices();
        List<Voice> voiceList = new ArrayList<>(voices);
        Voice selectedVoice = voiceList.get(10); // Change to the desired voice index
        tts.setVoice(selectedVoice);
    }

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
                        Log.e("XXXXX", "line 38 ");
                        setLanguageAndVoice();
                        convertTextToSpeech("Your score: " + score);

                    } else {
                        // Failed to initialize TTS engine.
                        Log.e("XXXXX", "line 43 ");
                    }
                }
            });
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
