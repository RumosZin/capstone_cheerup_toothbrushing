package com.lite.holistic_tracking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
public class MusicChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_choice);
        Button button = findViewById(R.id.btnGoToNextScreen);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNextScreen();
            }
        });
    }
    private void goToNextScreen() {
        // Create an Intent to start the next activity
        Intent intent = new Intent(this, AnimalChoiceActivity.class);
        // Start the activity
        startActivity(intent);
    }

}