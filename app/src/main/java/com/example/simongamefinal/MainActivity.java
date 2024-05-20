package com.example.simongamefinal;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.Manifest;

public class MainActivity extends AppCompatActivity {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    private TextView sequenceDisplay; // Text view to display the sequence
    private Button startButton; // Button to start the game
    private Button redButton; // Button for the color red
    private Button greenButton; // Button for the color green
    private Button blueButton; // Button for the color blue
    private Button whiteButton; // Button for the color white

    private List<String> sequence; // List to store the sequence of colors
    private int sequenceIndex; // Index to keep track of the current position in the sequence
    private int playerIndex; // Index to keep track of the player's input position
    private TextToSpeech tts;

    Random random; // Create a random number generator

    private static final List<String> COLORS = Arrays.asList("RED", "GREEN", "BLUE", "WHITE"); // List of available colors

    private void startGame() {
        sequence = new ArrayList<>(); // Initialize the sequence list
        sequenceIndex = 0; // Initialize the sequence index
        playerIndex = 0; // Initialize the player index
        generateNextSequenceItem(); // Generate the first item in the sequence
        displaySequence(); // Display the sequence
        startTimer();
    }

    // Method to display the sequence
    private void displaySequence() {
        sequenceDisplay.setText(""); // Clear the sequence display
        final Handler handler = new Handler(); // Create a handler to delay execution
        for (int i = 0; i < sequence.size(); i++) { // Iterate through each color in the sequence
            final String color = sequence.get(i); // Get the color at the current index
            handler.postDelayed(new Runnable() { // Post a delayed action to display the color
                @Override
                public void run() {
                    sequenceDisplay.setText(color); // Display the color
                    new Handler().postDelayed(new Runnable() { // Post a delayed action to clear the display
                        @Override
                        public void run() {
                            sequenceDisplay.setText(""); // Clear the display after a short delay
                        }
                    }, 500); // Delay duration (in milliseconds)
                }
            }, i * 1000 + 500); // Delay before displaying each color (in milliseconds)
        }
    }

    // Method to generate the next item in the sequence
    private void generateNextSequenceItem() {

        int randomIndex = random.nextInt(COLORS.size()); // Generate a random index within the range of available colors
        String color = COLORS.get(randomIndex); // Get the color at the random index
        sequence.add(color); // Add the color to the sequence
        Log.d("XXXXX", "sequence " + sequence);
    }


    // Method to handle color button clicks
    private void handleColorClick(String color) {
        Log.d("XXXXX", "color = " + color);
        Log.d("XXXXX", "sequence = " + sequence);
        Log.d("XXXXX", "player Index = " + playerIndex);
        Log.d("XXXXX", "get player Index = " + sequence.get(playerIndex));
        if (sequence.get(playerIndex).equals(color)) { // Check if the clicked color matches the color in the sequence
            playerIndex++; // Move to the next position in the sequence
            if (playerIndex == sequence.size()) { // Check if the player has completed the sequence
                playerIndex = 0; // Reset the player index
                generateNextSequenceItem(); // Generate the next item in the sequence
                displaySequence(); // Display the updated sequence
            }
        } else {
            // Player clicked the wrong color, end game
            endGame(sequence.size() - 1); // End the game with the current score
        }
    }

    private void startTimer(){
        TextView textView = findViewById(R.id.textView);

        long timerDuration = TimeUnit.MINUTES.toMillis(3);
        long ticksInterval = 100;

        new CountDownTimer(timerDuration, ticksInterval) {
            long millis = 1000;
            @Override
            public void onTick(long millisUntilFinished) {
                millis=millis - ticksInterval;
                if (millis==0)
                    millis= 1000;

                String timerText = String.format(Locale.getDefault(),"%02d:%02d:%03d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)),
                        millis

                );

                textView.setText(timerText);
            }

            @Override
            public void onFinish() { textView.setText("finished");
            }
        }.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the layout for the activity
        random = new Random();

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

        Log.d("XXXXX", "oncraete = ");

        sequenceDisplay = findViewById(R.id.sequence_display); // Get the sequence display text view
        startButton = findViewById(R.id.start_button); // Get the start button
        redButton = findViewById(R.id.red_button); // Get the red color button
        greenButton = findViewById(R.id.green_button); // Get the green color button
        blueButton = findViewById(R.id.blue_button); // Get the blue color button
        whiteButton = findViewById(R.id.white_button); // Get the white color button

        Log.d("XXXXX", "line 45 = ");
        // Set click listeners for buttons

        Log.d("XXXXX", "start button = " + startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(); // Start the game when the start button is clicked
            }
        });

        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleColorClick("RED"); // Handle the click on the red color button
            }
        });

        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleColorClick("GREEN"); // Handle the click on the green color button
            }
        });

        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleColorClick("BLUE"); // Handle the click on the blue color button
            }
        });

        whiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleColorClick("WHITE"); // Handle the click on the white color button
            }
        });
    }




    // Register the permissions callback, which handles the user's response to the
// system permissions dialog. Save the return value, an instance of
// ActivityResultLauncher, as an instance variable.
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    scheduleNotification(getNotification( "go back to the game" ) , 5000 ) ;
                    Log.e("XXXXX", "line 203");
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {

                    Log.e("XXXXX", "line 208");
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });




    // Method to end the game
    private void endGame(int score) {
        Intent intent = new Intent(MainActivity.this, ResultActivity.class); // Create an intent to start the result activity
        intent.putExtra("SCORE", score); // Pass the score as an extra to the intent
        startActivity(intent); // Start the result activity
        requestPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS);
        finish(); // Finish the main activity

    }


    private void scheduleNotification (Notification notification , int delay) {
        Intent notificationIntent = new Intent( this, MyBroadcastReceiver. class ) ;
        notificationIntent.putExtra(MyBroadcastReceiver. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(MyBroadcastReceiver. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_IMMUTABLE ) ;
        long futureInMillis = SystemClock. elapsedRealtime () + delay ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent) ;
    }
    private Notification getNotification (String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "Scheduled Notification" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }
}

