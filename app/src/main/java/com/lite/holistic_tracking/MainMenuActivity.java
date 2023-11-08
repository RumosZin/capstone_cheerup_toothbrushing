package com.lite.holistic_tracking;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {
    TextView detail_childName;
    String childName;

    private Button shopButton;

    private Button toothbrushTimeButton;

    private Button parentsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        detail_childName = findViewById(R.id.childNameEditText);

        // main activity에서 받아온 데이터
        Intent intent = getIntent();
        childName = intent.getStringExtra("childName");

        //childName = intent.getExtras().getString("name"); //
        detail_childName.setText(childName);

        // 상점 버튼
        shopButton = findViewById(R.id.shopButton);
        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 상점 화면으로 이동
                Intent intent = new Intent(MainMenuActivity.this, ShopActivity.class);
                startActivity(intent);
            }
        });

        // 즐거운 양치시간 버튼 누르면 holistic activity로 이동
        // 곽희준 한예준은 holistic activity에서 활동한다
        Button toothbrushTimeButton = findViewById(R.id.toothbrushTimeButton);
        toothbrushTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 즐거운 양치시간 화면으로 이동
                Intent intent = new Intent(MainMenuActivity.this, HolisticActivity.class);
                startActivity(intent);
            }
        });

        // 부모님 버튼
        Button parentsButton = findViewById(R.id.parentsButton);
        parentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 부모님 화면으로 이동
                Intent intent = new Intent(MainMenuActivity.this, ParentMenuActivity.class);
                startActivity(intent);
            }
        });

        // 자녀 등록 확인용 버튼
        Button tempButton = findViewById(R.id.tempButton);
        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 부모님 화면으로 이동
                Intent intent = new Intent(MainMenuActivity.this, ShowAllChildActivity.class);
                startActivity(intent);
            }
        });
    }
}
