package com.lite.holistic_tracking;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ParentMenuActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private ParentMenuChartFragment fragmentChart = new ParentMenuChartFragment();
    private ParentMenuSearchFragment fragmentSearch = new ParentMenuSearchFragment();
    private ParentMenuMoreFragment fragmentMore = new ParentMenuMoreFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_menu);

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
                    transaction.replace(R.id.menu_frame_layout, fragmentChart).commitAllowingStateLoss();
                    break;
                case R.id.menu_search:
                    transaction.replace(R.id.menu_frame_layout, fragmentSearch).commitAllowingStateLoss();
                    break;
                case R.id.menu_more:
                    transaction.replace(R.id.menu_frame_layout, fragmentMore).commitAllowingStateLoss();
                    break;

            }

            return true;
        }
    }
}
