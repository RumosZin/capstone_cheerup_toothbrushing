package com.lite.holistic_tracking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setSubtitle("");

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
                    animalList = AnimalDB.getInstance(mContext).animalDao().getAll();
                    Child child = new Child();
                    child.setChildName(childName);
                    child.setBirthDate(birthDate);
                    child.setGender(gender);
                    child.setSeed(seed);
                    animalAdapter = new AnimalAdapter(animalList, getApplicationContext(), child);
                    animalAdapter.notifyDataSetChanged();
                    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                    animalRecyclerView = findViewById(R.id.recyclerView);
                    animalRecyclerView.setAdapter(animalAdapter);
                    //.getInstance(getApplicationContext()).childDao().updateChildSeed("kim", 17);
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

}
