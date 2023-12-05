package com.lite.holistic_tracking;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.lite.holistic_tracking.Database.BuyingDB;
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
    private Button itemButton;

    private Button toothbrushTimeButton;

    private Button parentsButton;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ImageView temp = (ImageView) findViewById(R.id.temp);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(temp);
        Glide.with(this).load(R.drawable.temp).into(gifImage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setSubtitle("");

        genderImageView = findViewById(R.id.detail_gender_image);
        detail_childName = findViewById(R.id.detail_childName);
        detail_seed = findViewById(R.id.detail_seed);

        // Initialize MediaPlayer with the background music
        mediaPlayer = MediaPlayer.create(this, R.raw.background_main);
        mediaPlayer.setLooping(true); // Loop the music

        // 자녀 정보 가져오기
        // main menu 화면으로 올 때 무조건 call됨
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 자녀 정보 가져오는 코드 (getChildByName 메서드 사용)
                Child child = ChildDB.getInstance(getApplicationContext()).childDao().getChildByName(childName);
                //ChildDB.getInstance(getApplicationContext()).childDao().updateChildSeed("kim", 50);
                //BuyingDB.getInstance(getApplicationContext()).buyingDao().deleteAllBuyings();
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

        // 아이템 버튼
        itemButton = findViewById(R.id.itemButton);
        itemButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // 아이템 화면으로 이동
                Intent intent = new Intent(getApplicationContext(), ItemActivity.class);
                intent.putExtra("childName", childName); // "childName"이라는 키로 ChildName 전달
                intent.putExtra("birthDate", birthDate);
                intent.putExtra("gender", gender);
                intent.putExtra("seed", seed); // "seed"라는 키로 seed 전달

                startActivity(intent);
            }
        });

        // 상점 버튼
        shopButton = findViewById(R.id.shopButton);
        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 상점 화면으로 이동
                Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
                intent.putExtra("childName", childName); // "childName"이라는 키로 ChildName 전달
                intent.putExtra("birthDate", birthDate);
                intent.putExtra("gender", gender);
                intent.putExtra("seed", seed); // "seed"라는 키로 seed 전달

                startActivity(intent);
            }
        });

        // 즐거운 양치시간 -> 음악 선택 화면
        Button toothbrushTimeButton = findViewById(R.id.toothbrushTimeButton);
        toothbrushTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 음악 선택 화면으로 이동
                // 자녀 이름으로 toothbrushing arg 넘겨야 할 듯
                Intent intent = new Intent(MainMenuActivity.this, MusicChoiceActivity.class);
                intent.putExtra("childName", childName); // "childName"이라는 키로 ChildName 전달
                intent.putExtra("birthDate", birthDate);
                intent.putExtra("gender", gender);
                intent.putExtra("seed", seed); // "seed"라는 키로 seed 전달
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
                startActivity(intent);
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

    @Override
    protected void onResume() {
        super.onResume();
        // Start playing background music when the activity resumes
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause background music when the activity is paused
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release resources when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onBackPressed() {
        // 항상 ShowAllChildActivity로 이동
        Intent intent = new Intent(this, ShowAllChildActivity.class);
        startActivity(intent);
        finish(); // 현재 액티비티 종료
    }

}
