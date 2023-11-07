package com.lite.holistic_tracking;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.room.Room;

import com.lite.holistic_tracking.Database.ChildDB;
import com.lite.holistic_tracking.Database.ChildDao;
import com.lite.holistic_tracking.Entity.Child;

public class ChildRegisterActivity extends Activity {
    private EditText childNameEditText;
    private EditText birthdateEditText;
    private RadioGroup genderRadioGroup;
    private Button saveButton;

    private Button tempButton;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_register);

        // 버튼과 EditText, RadioGroup 초기화
        saveButton = findViewById(R.id.saveButton);
        tempButton = findViewById(R.id.tempButton);
        childNameEditText = findViewById(R.id.childNameEditText);
        birthdateEditText = findViewById(R.id.birthdateEditText);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        
        // save btn 누르면 room DB에 자녀 정보 저장
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력된 자녀 정보 가져오기
                String childName = childNameEditText.getText().toString();
                String birthdate = birthdateEditText.getText().toString();
                int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
                String gender = "";
                
                // 선택한 radio button로 아이의 gender 설정
                if (selectedGenderId == R.id.maleRadioButton) {
                    gender = "남자";
                } else if (selectedGenderId == R.id.femaleRadioButton) {
                    gender = "여자";
                }

                // 자녀 정보를 Room Database에 저장
                ChildDB db = Room.databaseBuilder(getApplicationContext(), ChildDB.class, "child-database").build();
                ChildDao childDao = db.childDao();

                Child child = new Child();
                child.childName = childName;
                child.birthdate = birthdate;
                child.gender = gender;
                child.seed = 0;
                childDao.insertChild(child);

                // 자녀 정보 저장 후 MainMenu 화면으로 이동
                startActivity(new Intent(ChildRegisterActivity.this, MainMenuActivity.class));
            }
        });
        tempButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChildRegisterActivity.this, MainMenuActivity.class));
            }
        });
    }


}
