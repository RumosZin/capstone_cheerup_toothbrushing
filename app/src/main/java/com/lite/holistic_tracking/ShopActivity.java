package com.lite.holistic_tracking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lite.holistic_tracking.Database.AnimalDB;
import com.lite.holistic_tracking.Database.BuyingDB;
import com.lite.holistic_tracking.Database.ChildDB;
import com.lite.holistic_tracking.Entity.Animal;
import com.lite.holistic_tracking.Entity.AnimalAdapter;
import com.lite.holistic_tracking.Entity.Child;
import com.lite.holistic_tracking.Entity.ChildAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {

    private ImageView genderImageView;
    TextView detail_childName;
    TextView detail_seed;

    private RecyclerView recyclerView;
    private AnimalAdapter animalAdapter;
    private List<Animal> animalList;
    private Context mContext;
    private AnimalDB animalDB = null;
    private RecyclerView animalRecyclerView;

    private String childName;
    private String birthDate;
    private int seed;
    private String gender;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setSubtitle("");

        // Initialize MediaPlayer with the background music
        mediaPlayer = MediaPlayer.create(this, R.raw.background_item_and_shop);
        mediaPlayer.setLooping(true); // Loop the music

        // 이전 화면(MainMenuActivity)에서 전달받은 데이터 가져오기
        Intent intent = getIntent();
        childName = intent.getStringExtra("childName");
        birthDate = intent.getStringExtra("birthDate");
        seed = intent.getIntExtra("seed", 100);
        gender = intent.getStringExtra("gender");

        genderImageView = findViewById(R.id.detail_gender_image);
        detail_childName = findViewById(R.id.detail_childName);
        detail_seed = findViewById(R.id.detail_seed);

        detail_childName.setText(childName);
        detail_seed.setText(String.valueOf(seed));

        // 성별에 따라 이미지 설정
        if ("남자".equals(gender)) {
            genderImageView.setImageResource(R.drawable.boy_image);
        } else if ("여자".equals(gender)) {
            genderImageView.setImageResource(R.drawable.girl_image);
        }

        mContext = getApplicationContext();

        class InsertRunnable implements Runnable {
            @Override
            public void run() {
                try {

                    // 자녀가 구매한 동물 목록 가져오기
                    List<String> boughtAnimals = BuyingDB.getInstance(mContext).buyingDao().getBoughtAnimals(childName);
                    Log.v("hhhhhhhhhhhhhhh", String.valueOf(boughtAnimals.size()));
                    // 모든 동물 목록 가져오기
                    animalList = AnimalDB.getInstance(mContext).animalDao().getAll();
                    // 구매하지 않은 동물 목록 구성
                    List<Animal> availableAnimals = new ArrayList<>();
                    for (Animal animal : animalList) {
                        if (!boughtAnimals.contains(animal.getName())) {
                            availableAnimals.add(animal);
                        }
                    }

                    Child child = new Child();
                    child.setChildName(childName);
                    child.setBirthDate(birthDate);
                    child.setGender(gender);
                    child.setSeed(seed);

                    animalAdapter = new AnimalAdapter(availableAnimals, getApplicationContext(), child);
                    animalAdapter.notifyDataSetChanged();

                    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                    animalRecyclerView = findViewById(R.id.recyclerView);
                    animalRecyclerView.setAdapter(animalAdapter);
                    animalRecyclerView.setLayoutManager(mLinearLayoutManager);

                }
                catch (Exception e) {
                    Log.v("test", e.getMessage());
                }
            }
        }
        InsertRunnable insertRunnable = new InsertRunnable();
        Thread t = new Thread(insertRunnable);
        t.start();
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
    public void onBackPressed() {
        super.onBackPressed();

        // MainMenuActivity를 다시 시작
        Intent intent = new Intent(ShopActivity.this, MainMenuActivity.class);
        intent.putExtra("childName", childName); // 자녀 이름 추가
        startActivity(intent);
        finish(); // 현재 액티비티 종료
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


}
