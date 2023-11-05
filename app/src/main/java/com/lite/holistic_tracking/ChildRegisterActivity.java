package com.lite.holistic_tracking;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChildRegisterActivity extends Activity {
    private EditText childNameEditText;
    private EditText birthdateEditText;
    private EditText genderEditText;
    private Button saveButton;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_register);

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("ChildInfo", MODE_PRIVATE);

        childNameEditText = findViewById(R.id.childNameEditText);
        birthdateEditText = findViewById(R.id.birthdateEditText);
        genderEditText = findViewById(R.id.genderEditText);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력된 자녀 정보 가져오기
                String childName = childNameEditText.getText().toString();
                String birthdate = birthdateEditText.getText().toString();
                String gender = genderEditText.getText().toString();

                // SharedPreferences에 자녀 정보 저장
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ChildName", childName);
                editor.putString("Birthdate", birthdate);
                editor.putString("Gender", gender);
                editor.apply();

                // 자녀 정보 저장 후 MainMenu 화면으로 이동
                startActivity(new Intent(ChildRegisterActivity.this, MainMenuActivity.class));
            }
        });
    }
}
