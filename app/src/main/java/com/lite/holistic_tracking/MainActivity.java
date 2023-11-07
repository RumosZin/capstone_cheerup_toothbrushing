package com.lite.holistic_tracking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.lite.holistic_tracking.Database.ChildDao;
import com.lite.holistic_tracking.Database.ChildDB;
import com.lite.holistic_tracking.Entity.Child;
import com.lite.holistic_tracking.Entity.ChildAdapter;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private ChildDB childDB = null;
    private int childNum = -1; // 초기에 1로 설정
    private List<Child> childList;
    private Context mContext = null;
    private ChildAdapter childAdapter;
    private Button mAddButton;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAddButton = (Button) findViewById(R.id.mAddButton);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);

        mContext = getApplicationContext();
        childAdapter = new ChildAdapter(childList);

        // DB 생성
        childDB = ChildDB.getInstance(this);

        // main thread에서 db 접근 불가, crud 시 thread 이용하기
        class InsertRunnable implements Runnable {

            @Override
            public void run() {
                try {
                    childList = ChildDB.getInstance(mContext).childDao().getAllChildren();
                    childAdapter = new ChildAdapter(childList);
                    childAdapter.notifyDataSetChanged();

                    mRecyclerView.setAdapter(childAdapter);
                    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                    mRecyclerView.setLayoutManager(mLinearLayoutManager);
                }
                catch (Exception e) {

                }
            }
        }
        
        InsertRunnable insertRunnable = new InsertRunnable();
        Thread t = new Thread(insertRunnable);
        t.start();

        mAddButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ChildRegisterActivity.class);
            startActivity(intent);
        });

//        // 자녀 정보가 없는 경우 최초 접속
//        if (childDao.getChildCount() > 0) {
//            // 최초 접속 화면으로 이동 (ChildRegistrationActivity)
//            startActivity(new Intent(this, ShowAllChildActivity.class));
//        } else {
//            // 자녀 정보가 이미 저장된 경우 등록된 자녀 보여주는 화면으로 이동
//            startActivity(new Intent(this, InitialActivity.class));
//        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChildDB.destroyInstance();
        childDB = null;
    }

}
