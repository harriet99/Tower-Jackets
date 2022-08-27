package com.cs2340.towerjackets.views;

import androidx.appcompat.app.AppCompatActivity;
import com.cs2340.towerjackets.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.startGameButton);
        Button quitButton = findViewById(R.id.quitGameButton);

        // Add event listeners for these 2 buttons
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intention = new Intent(MainActivity.this, InitialConfiguration.class);
                startActivity(intention);
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });
    }
}