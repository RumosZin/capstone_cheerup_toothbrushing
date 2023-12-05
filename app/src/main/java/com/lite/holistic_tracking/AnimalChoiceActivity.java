package com.lite.holistic_tracking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lite.holistic_tracking.Database.AnimalDB;
import com.lite.holistic_tracking.Database.BuyingDB;
import com.lite.holistic_tracking.Entity.Animal;
import com.lite.holistic_tracking.Entity.AnimalAdapter;
import com.lite.holistic_tracking.Entity.AnimalGridAdapter;
import com.lite.holistic_tracking.Entity.Child;

import java.util.ArrayList;
import java.util.List;

public class AnimalChoiceActivity extends AppCompatActivity {

    private String songTitle; // 일단 노래 제목은 가지고 있음
    private Button pickButton;
    private Button reButton;
    private Context mContext;
    private List<Animal> animalList;
    private AnimalGridAdapter animalGridAdapter;
    private String childName;
    private String birthDate;
    private int seed;

    private String gender;
    private RecyclerView animalRecyclerView;
    private GridView animalGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animal_choice);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setSubtitle("");

        // Intent로부터 데이터 추출
        Intent intent = getIntent();
        this.songTitle = intent.getStringExtra("songTitle");
        childName = intent.getStringExtra("childName");
        birthDate = intent.getStringExtra("birthDate");
        seed = intent.getIntExtra("seed", 0);
        gender = intent.getStringExtra("gender");

        mContext = getApplicationContext();
        animalGridView = findViewById(R.id.gridview);

        Log.v("backpress check", "animal choice's onCreate " + " " + songTitle);


        class InsertRunnable implements Runnable {
            @Override
            public void run() {
                try {

                    // 자녀가 구매한 동물 목록 가져오기
                    List<String> boughtAnimals = BuyingDB.getInstance(mContext).buyingDao().getBoughtAnimals(childName);

                    // 모든 동물 목록 가져와서, 구매한 동물 목록 구성
                    animalList = AnimalDB.getInstance(mContext).animalDao().getAll();
                    List<Animal> availableAnimals = new ArrayList<>();
                    for (Animal animal : animalList) {
                        if (boughtAnimals.contains(animal.getName())) {
                            availableAnimals.add(animal);
                        }
                    }

                    Log.v("test animal", String.valueOf(availableAnimals.size()));
                    Log.v("backpress check", "animal choice's in thread " + " " + songTitle + " " + availableAnimals.size());

                    Child child = new Child();
                    child.setChildName(childName);
                    child.setBirthDate(birthDate);
                    child.setGender(gender);
                    child.setSeed(seed);

                    animalGridAdapter = new AnimalGridAdapter(availableAnimals, getApplicationContext(), child, songTitle);
                    animalGridAdapter.notifyDataSetChanged();

                    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                    animalGridView = findViewById(R.id.gridview);
                    animalGridView.setAdapter(animalGridAdapter);

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
                // onBackPressed(); // 현재 액티비티 종료
                // Create an intent to start the MusicChoiceActivity
                Intent intent = new Intent(this, MusicChoiceActivity.class);

                // Start the MusicChoiceActivity
                startActivity(intent);

                // Finish the current activity (AnimalChoiceActivity)
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}