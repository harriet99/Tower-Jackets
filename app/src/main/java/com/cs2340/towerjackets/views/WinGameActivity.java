package com.cs2340.towerjackets.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cs2340.towerjackets.R;

public class WinGameActivity extends AppCompatActivity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_game);

        // Show statistics
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String finalMoney = bundle.getString("finalMoney");
        String finalTower = bundle.getString("finalTower");
        String finalUpgrade = bundle.getString("finalUpgrade");
        TextView tv = findViewById(R.id.textView8);
        tv.setText("Money Left: " + finalMoney);
        TextView tv2 = findViewById(R.id.textView9);
        tv2.setText("Number of Towers Purchased: " + finalTower);
        TextView tv3 = findViewById(R.id.textView10);
        tv3.setText("Number of Towers Upgraded: " + finalUpgrade);

        // Hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button playAgain = findViewById(R.id.playAgainB);
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intention = new Intent(WinGameActivity.this, InitialConfiguration.class);
                startActivity(intention);
            }
        });

        Button quitButton = findViewById(R.id.quitB);

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intention = new Intent(WinGameActivity.this, MainActivity.class);
                startActivity(intention);
            }
        });
    }
}

