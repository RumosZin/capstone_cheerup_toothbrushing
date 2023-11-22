package com.lite.holistic_tracking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ParentMenuActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private ParentMenuChartFragment fragmentChart;
    private ParentMenuSearchFragment fragmentSearch;
    private ParentMenuMoreFragment fragmentMore;

    private String childName;
    private String birthDate;
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
        birthDate = intent.getStringExtra("birthDate");
        seed = intent.getIntExtra("seed", 100);
        gender = intent.getStringExtra("gender");
        Log.v("activity check", birthDate);

        // 기존에 생성된 Fragment가 없다면 생성하고 Argument를 설정
        if (fragmentChart == null) {
            fragmentChart = new ParentMenuChartFragment();
            Bundle chartArgs = new Bundle();
            chartArgs.putString("childName", childName);
            chartArgs.putString("birthDate", birthDate);
            chartArgs.putInt("seed", seed);
            chartArgs.putString("gender", gender);
            fragmentChart.setArguments(chartArgs);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.menu_frame_layout, fragmentChart).commit();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (menuItem.getItemId()) {
                case R.id.menu_chart:
                    if(fragmentChart == null) {
                        fragmentChart = new ParentMenuChartFragment();
                        Bundle args = new Bundle();
                        args.putString("childName", childName);
                        args.putString("birthDate", birthDate);
                        args.putInt("seed", seed);
                        args.putString("gender", gender);
                        fragmentChart.setArguments(args);
                        fragmentManager.beginTransaction().add(R.id.menu_frame_layout, fragmentChart);
                    }
                    fragmentManager.beginTransaction().show(fragmentChart).commit();
                    // else if(fragmentChart != null) fragmentManager.beginTransaction().show(fragmentChart).commit();
                    if(fragmentSearch != null) {
                        Log.v("test menu chart click", "hhhhhhhhhhhhhhhhhh");
                        fragmentManager.beginTransaction().hide(fragmentSearch).commit();
                    }
                    if(fragmentMore != null) {
                        Log.v("test menu chart click", "nnnnnnnnnnnnnnn");
                        fragmentManager.beginTransaction().hide(fragmentMore).commit();
                    }

                    break;
                case R.id.menu_search:

                    if(fragmentSearch == null) {
                        Log.v("test menu search click", "sssssssssssssssss");
                        fragmentSearch = new ParentMenuSearchFragment();
                        Bundle args = new Bundle();
                        args.putString("childName", childName);
                        args.putString("birthDate", birthDate);
                        args.putInt("seed", seed);
                        args.putString("gender", gender);
                        fragmentSearch.setArguments(args);
                        fragmentManager.beginTransaction().add(R.id.menu_frame_layout, fragmentSearch).commit();
                    }
                    fragmentManager.beginTransaction().show(fragmentSearch).commit();

                    if(fragmentChart != null) {
                        Log.v("test menu search click", "llllllllllllllll chart1");
                        //transaction.hide(fragmentChart).commit();
                        fragmentManager.beginTransaction().hide(fragmentChart).commit();
                        Log.v("test menu search click", "llllllllllllllll chart2");
                    }

                    if(fragmentMore != null) {
                        Log.v("test menu search click", "llllllllllllllll more");
                        //transaction.hide(fragmentMore).commit();
                        fragmentManager.beginTransaction().hide(fragmentMore).commit();
                    }
                    break;

                case R.id.menu_more:
                    if(fragmentMore == null) {
                        fragmentMore = new ParentMenuMoreFragment();
                        Bundle args = new Bundle();
                        args.putString("childName", childName);
                        args.putString("birthDate", birthDate);
                        args.putInt("seed", seed);
                        args.putString("gender", gender);
                        fragmentMore.setArguments(args);
                        fragmentManager.beginTransaction().add(R.id.menu_frame_layout, fragmentMore).commit();
                    }
                    fragmentManager.beginTransaction().show(fragmentMore).commit();
                    if(fragmentChart != null) fragmentManager.beginTransaction().hide(fragmentChart).commit();
                    if(fragmentSearch != null) fragmentManager.beginTransaction().hide(fragmentSearch).commit();
                    //if(fragmentMore != null) fragmentManager.beginTransaction().show(fragmentMore).commit();
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
