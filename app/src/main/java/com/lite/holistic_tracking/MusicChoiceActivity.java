package com.lite.holistic_tracking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lite.holistic_tracking.Database.SongDB;
import com.lite.holistic_tracking.Entity.Child;
import com.lite.holistic_tracking.Entity.Song;
import com.lite.holistic_tracking.Entity.SongAdapter;

import java.util.List;

public class MusicChoiceActivity extends AppCompatActivity {

    private String childName;
    private String birthDate;
    private String gender;
    private int seed;
    private Context mContext;
    private RecyclerView songRecyclerView;
    private SongAdapter songAdapter;
    private List<Song> songList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_choice);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setSubtitle("");

        // Main menu activity에서 받아온 데이터
        Intent intent = getIntent();
        childName = intent.getStringExtra("childName");
        birthDate = intent.getStringExtra("birthDate");
        gender = intent.getStringExtra("gender");
        seed = intent.getIntExtra("seed", 0);
        
        
        // DB에서 노래 받아와야 함
        class InsertRunnable implements Runnable {
            @Override
            public void run() {
                try {

                    // 노래 목록 가져오고 child 정보 설정
                    songList = SongDB.getInstance(mContext).songDao().getAllSongs();
                    Child child = new Child();
                    child.setChildName(childName);
                    child.setBirthDate(birthDate);
                    child.setGender(gender);
                    child.setSeed(seed);
                    
                    // music adapter로 설정 / 노래 제목, bpm, 노래 총 길이 sec

                    songAdapter = new SongAdapter(songList, getApplicationContext(), child);
                    Log.v("testestestsetsetstt", String.valueOf(songList.size()));
                    songAdapter.notifyDataSetChanged();

                    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                    songRecyclerView = findViewById(R.id.recyclerView);
                    songRecyclerView.setAdapter(songAdapter);
                    songRecyclerView.setLayoutManager(mLinearLayoutManager);

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

    private void showMusicPopup(int position) {
        // Implement the logic to show a popup with music details
        // You can use a DialogFragment or any other approach you prefer
        // Access the selected music using musicList.get(position)
        // Display the title, bpm, and song length in the popup
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); // 뒤로가기 동작 수행
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}