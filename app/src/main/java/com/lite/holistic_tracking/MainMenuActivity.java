package com.lite.holistic_tracking;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.lite.holistic_tracking.Database.ChildDB;
import com.lite.holistic_tracking.Entity.Child;
import com.lite.holistic_tracking.Entity.Toothbrushing;

public class MainMenuActivity extends AppCompatActivity {

    private ImageView genderImageView;
    TextView detail_childName;
    TextView detail_birthdate;
    TextView detail_gender;
    TextView detail_seed;
    String childName;
    String birthDate;
    int seed;
    String gender;

    private Button shopButton;

    private Button toothbrushTimeButton;

    private Button parentsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setSubtitle("");

        genderImageView = findViewById(R.id.detail_gender_image);
        detail_childName = findViewById(R.id.detail_childName);
        detail_seed = findViewById(R.id.detail_seed);

        // 자녀 정보 가져오기
        // main menu 화면으로 올 때 무조건 call됨
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 자녀 정보 가져오는 코드 (getChildByName 메서드 사용)
                Child child = ChildDB.getInstance(getApplicationContext()).childDao().getChildByName(childName);
                //child.setSeed(17);
                // UI 업데이트를 위해 메인 스레드로 전환
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (child != null) {
                            detail_childName.setText(child.childName);
                            detail_seed.setText(String.valueOf(child.seed));

                            childName = child.getChildName();
                            birthDate = child.getBirthDate();
                            seed = child.getSeed();
                            gender = child.getGender();

                            // 성별에 따라 이미지 설정
                            if ("남자".equals(child.gender)) {
                                genderImageView.setImageResource(R.drawable.boy_image);
                            } else if ("여자".equals(child.gender)) {
                                genderImageView.setImageResource(R.drawable.girl_image);
                            }
                        }
                    }
                });
            }
        }).start();

        // main activity에서 받아온 데이터
        Intent intent = getIntent();
        childName = intent.getStringExtra("childName");
        detail_childName.setText(childName);

        // 상점 버튼
        shopButton = findViewById(R.id.shopButton);
        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 상점 화면으로 이동
                Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
                intent.putExtra("childName", childName); // "childName"이라는 키로 ChildName 전달
                intent.putExtra("gender", gender);
                intent.putExtra("seed", seed); // "seed"라는 키로 seed 전달

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
                Intent intent = new Intent(getApplicationContext(), ParentMenuActivity.class);
                intent.putExtra("childName", childName); // "childName"이라는 키로 ChildName 전달
                intent.putExtra("birthDate", birthDate);
                intent.putExtra("gender", gender);
                intent.putExtra("seed", seed); // "seed"라는 키로 seed 전달
                Log.v("check in mainmenu", birthDate);
                startActivity(intent);

            }
        });

        // 자녀 등록 확인용 버튼
        Button tempButton = findViewById(R.id.tempButton);
        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 부모님 화면으로 이동
                // get seed dialog 확인용 버튼
//                Toothbrushing toothbrushing = new Toothbrushing(childName,
//                        "date", "time", 5, 7, 8, 9, 10, 11, 11, 15, 10, 45);
//                GetSeedDialog getSeedDialog = new GetSeedDialog(MainMenuActivity.this, toothbrushing);
//                getSeedDialog.show();

                // 자녀에게 씨앗 17개 부여하기
                ChildDB.getInstance(getApplicationContext()).childDao().updateChildSeed("kim", 17);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); // 현재 액티비티 종료
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
