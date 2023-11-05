package com.lite.holistic_tracking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("ChildInfo", MODE_PRIVATE);

        // SharedPreferences에서 자녀 정보 확인
        String childName = sharedPreferences.getString("ChildName", null);

        // 자녀 정보가 없는 경우 최초 접속
        if (childName == null) {
            // 최초 접속 화면으로 이동 (ChildRegistrationActivity)
            startActivity(new Intent(this, InitialActivity.class));
        } else {
            // 자녀 정보가 이미 저장된 경우 등록된 자녀 보여주는 화면으로 이동
            startActivity(new Intent(this, ShowAllChildActivity.class));
        }
    }


}
