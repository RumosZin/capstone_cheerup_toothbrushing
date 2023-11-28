package com.lite.holistic_tracking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RandomRewardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.random_reward);

        Button btnNextActivity = findViewById(R.id.backToMainScreen);

        btnNextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼이 클릭되었을 때 다음 액티비티로 이동하는 코드
                Intent intent = new Intent(RandomRewardActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }
}
