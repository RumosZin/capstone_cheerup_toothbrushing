package com.lite.holistic_tracking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.lite.holistic_tracking.Database.ChildDB;
import com.lite.holistic_tracking.Database.ChildDao;
import com.lite.holistic_tracking.Entity.Child;
import com.lite.holistic_tracking.Entity.ChildAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShowAllChildActivity extends Activity {
    private RecyclerView childRecyclerView;
    private ChildAdapter adapter;
    private List<Child> children;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_child);

        childRecyclerView = findViewById(R.id.childRecyclerView);
        childRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 자녀 정보 가져오기
        ChildDB db = Room.databaseBuilder(getApplicationContext(), ChildDB.class, "child-database").build();
        ChildDao childDao = db.childDao();
        children = childDao.getAllChildren();

        // 어댑터 생성 및 연결
        adapter = new ChildAdapter(children);
        childRecyclerView.setAdapter(adapter);

//        // 아이템 클릭 리스너 추가
//        adapter.setOnItemClickListener(new ChildAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                // 클릭한 자녀 정보를 사용하여 MainMenuActivity로 이동
//                Child selectedChild = children.get(position);
//                int selectedChildId = selectedChild.getId();
//                String selectedChildName = selectedChild.getChildName();
//                // 이 정보를 MainMenuActivity로 전달하거나 필요한 동작을 수행
//            }
//        });
    }

    private class OnItemClickListener {
    }
}
