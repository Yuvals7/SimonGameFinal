package com.example.simongamefinal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView sequenceDisplay; // Text view to display the sequence
    private Button startButton; // Button to start the game
    private Button redButton; // Button for the color red
    private Button greenButton; // Button for the color green
    private Button blueButton; // Button for the color blue
    private Button whiteButton; // Button for the color white

    private List<String> sequence; // List to store the sequence of colors
    private int sequenceIndex; // Index to keep track of the current position in the sequence
    private int playerIndex; // Index to keep track of the player's input position

    private static final List<String> COLORS = Arrays.asList("RED", "GREEN", "BLUE", "WHITE"); // List of available colors

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the layout for the activity

        Log.d("XXXXX", "oncraete = ");

        sequenceDisplay = findViewById(R.id.sequence_display); // Get the sequence display text view
        startButton = findViewById(R.id.start_button); // Get the start button
        redButton = findViewById(R.id.red_button); // Get the red color button
        greenButton = findViewById(R.id.green_button); // Get the green color button
        blueButton = findViewById(R.id.blue_button); // Get the blue color button
        whiteButton = findViewById(R.id.white_button); // Get the white color button

        Log.d("XXXXX", "line 45 = " );
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

    // Method to start the game
    private void startGame() {
        Log.e("XXXXX", "start game = ");
        sequence = new ArrayList<>(); // Initialize the sequence list
        sequenceIndex = 0; // Initialize the sequence index
        playerIndex = 0; // Initialize the player index
        generateNextSequenceItem(); // Generate the first item in the sequence
        displaySequence(); // Display the sequence
    }

    // Method to generate the next item in the sequence
    private void generateNextSequenceItem() {
        Log.e("XXXXX", "generateNextSequenceItem = ");
        Random random = new Random(); // Create a random number generator
        int randomIndex = random.nextInt(COLORS.size()); // Generate a random index within the range of available colors
        String color = COLORS.get(randomIndex); // Get the color at the random index
        sequence.add(color); // Add the color to the sequence
    }

    // Method to display the sequence
    private void displaySequence() {
        Log.e("XXXXX", "displaySequence = ");
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
    // Method to end the game
    private void endGame(int score) {
        Intent intent = new Intent(MainActivity.this, ResultActivity.class); // Create an intent to start the result activity
        intent.putExtra("SCORE", score); // Pass the score as an extra to the intent
        startActivity(intent); // Start the result activity
        finish(); // Finish the main activity
    }
}
