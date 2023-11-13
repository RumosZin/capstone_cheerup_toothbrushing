package com.lite.holistic_tracking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ParentMenuActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private ParentMenuChartFragment fragmentChart = new ParentMenuChartFragment();
    private ParentMenuSearchFragment fragmentSearch = new ParentMenuSearchFragment();
    private ParentMenuMoreFragment fragmentMore = new ParentMenuMoreFragment();

    private String childName;
    private int seed;

    private String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setSubtitle("");

        // 이전 화면(MainMenuActivity)에서 전달받은 데이터 가져오기
        Intent intent = getIntent();
        childName = intent.getStringExtra("childName");
        seed = intent.getIntExtra("seed", 100);
        gender = intent.getStringExtra("gender");
        Log.v("activity check", childName);

        // 기존에 생성된 Fragment가 없다면 생성하고 Argument를 설정
        if (fragmentChart == null) {
            fragmentChart = new ParentMenuChartFragment();
            Bundle chartArgs = new Bundle();
            chartArgs.putString("childName", childName);
            chartArgs.putInt("seed", seed);
            chartArgs.putString("gender", gender);
            fragmentChart.setArguments(chartArgs);
        }

        ParentMenuChartFragment fragmentChart = new ParentMenuChartFragment();
        Bundle chartArgs = new Bundle();
        chartArgs.putString("childName", childName);
        chartArgs.putInt("seed", seed);
        chartArgs.putString("gender", gender);
        fragmentChart.setArguments(chartArgs);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.menu_frame_layout, fragmentChart).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.menu_chart:
                    if (fragmentChart == null) {
                        fragmentChart = new ParentMenuChartFragment();
                        Bundle chartArgs = new Bundle();
                        chartArgs.putString("childName", childName);
                        chartArgs.putInt("seed", seed);
                        chartArgs.putString("gender", gender);
                        fragmentChart.setArguments(chartArgs);
                    }
                    transaction.replace(R.id.menu_frame_layout, fragmentChart).commitAllowingStateLoss();
                    break;
                case R.id.menu_search:
                    if (fragmentSearch == null) {
                        fragmentSearch = new ParentMenuSearchFragment();
                    }
                    transaction.replace(R.id.menu_frame_layout, fragmentSearch).commitAllowingStateLoss();
                    break;
                case R.id.menu_more:
                    if (fragmentMore == null) {
                        fragmentMore = new ParentMenuMoreFragment();
                    }
                    transaction.replace(R.id.menu_frame_layout, fragmentMore).commitAllowingStateLoss();
                    break;

            }

            return true;
        }
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
