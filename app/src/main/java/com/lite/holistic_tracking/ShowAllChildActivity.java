package com.lite.holistic_tracking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.circularreveal.CircularRevealHelper;
import com.lite.holistic_tracking.Database.ChildDB;
import com.lite.holistic_tracking.Database.ChildDao;
import com.lite.holistic_tracking.Entity.Child;
import com.lite.holistic_tracking.Entity.ChildAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShowAllChildActivity extends Activity {

    private List<Child> childList;
    private ChildDB childDB = null;
    private Context mContext;
    private ChildAdapter childAdapter;
    private RecyclerView childRecyclerView;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_child);

        mContext = getApplicationContext();

        class InsertRunnable implements Runnable {

            @Override
            public void run() {
                try {
                    childList = ChildDB.getInstance(mContext).childDao().getAll();
                    childAdapter = new ChildAdapter(childList, getApplicationContext());
                    childAdapter.notifyDataSetChanged();
                    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                    childRecyclerView = findViewById(R.id.mRecyclerView);
                    childRecyclerView.setAdapter(childAdapter);

                    childRecyclerView.setLayoutManager(mLinearLayoutManager);

                }
                catch (Exception e) {
                    Log.v("test", e.getMessage());
                }
            }
        }

        InsertRunnable insertRunnable = new InsertRunnable();
        Thread t = new Thread(insertRunnable);
        t.start();

        // 자녀 추가 버튼을 찾아 클릭 이벤트 처리
        Button addChildButton = findViewById(R.id.addChildButton);
        addChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 자녀 추가 버튼 클릭 시 ChildRegisterActivity로 이동
                startActivity(new Intent(ShowAllChildActivity.this, ChildRegisterActivity.class));
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChildDB.destroyInstance();
        childDB = null;
    }

}
