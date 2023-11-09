package com.lite.holistic_tracking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lite.holistic_tracking.Database.ChildDB;
import com.lite.holistic_tracking.Database.ChildDao;
import com.lite.holistic_tracking.Database.ToothbrushingDB;
import com.lite.holistic_tracking.Database.TotalBrushingDB;
import com.lite.holistic_tracking.Entity.Child;
import com.lite.holistic_tracking.Entity.ChildAdapter;
import com.lite.holistic_tracking.Entity.Toothbrushing;
import com.lite.holistic_tracking.Entity.TotalBrushing;

import java.util.ArrayList;
import java.util.List;

public class ChildRegisterActivity extends Activity {

    //private List<Child> childList;
    private ChildDB childDB = null;
    private Context mContext = null;

    private EditText childNameEditText;
    private EditText birthdateEditText;
    private RadioGroup genderRadioGroup;
    private ChildAdapter childAdapter;
//    private EditText childNameEditText;
//    private EditText birthdateEditText;
//    private RadioGroup genderRadioGroup;
    private Button saveButton;
    private Button tempButton;
    private RecyclerView mRecyclerView;
    private String childName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_register);

        // 버튼과 EditText, RadioGroup 초기화
        saveButton = findViewById(R.id.saveButton);
        tempButton = findViewById(R.id.tempButton);
        childNameEditText = (EditText) findViewById(R.id.childNameEditText);
        birthdateEditText = (EditText) findViewById(R.id.birthdateEditText);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);

        childDB = ChildDB.getInstance(this);
        mContext = getApplicationContext();

        class InsertRunnable implements Runnable {
            @Override
            public void run() {
                Child child = new Child();
//                child.setChildName("김윤진");
//                child.setBirthDate("2001.11.14");
//                child.setGender("여자");
//                child.setSeed(0);
                // name = child.childName;

                child.childName = childNameEditText.getText().toString();
                child.birthdate = birthdateEditText.getText().toString();
                int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
                if(selectedGenderId == R.id.maleRadioButton) child.gender = "남자";
                else child.gender = "여자";
                ChildDB.getInstance(mContext).childDao().insertChild(child);

                TotalBrushing totalBrushing = new TotalBrushing();
                totalBrushing.childName = childNameEditText.getText().toString();
                totalBrushing.left_circular = 0;
                totalBrushing.mid_circular = 0;
                totalBrushing.right_circular = 0;
                totalBrushing.left_upper = 0;
                totalBrushing.left_lower = 0;
                totalBrushing.right_upper = 0;
                totalBrushing.right_lower = 0;
                totalBrushing.mid_vertical_upper = 0;
                totalBrushing.mid_vertical_lower = 0;

                TotalBrushingDB.getInstance(mContext).totalBrushingDao().insertTotalBrushing(totalBrushing);


            }
        }

        saveButton.setOnClickListener(v -> {
            InsertRunnable insertRunnable = new InsertRunnable();
            Thread t = new Thread(insertRunnable);
            t.start();
            childName = childNameEditText.getText().toString(); // 사용자 입력으로 childName 설정
            Log.v("name check1", childName);

            Log.v("name check2", String.valueOf(childName));

            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
            intent.putExtra("childName", String.valueOf(childName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ChildDB의 값을 전부 삭제하고 싶을 때 활성화
                // Thread을 생성하고 접근해서 main Thread에서 DB에 접근하지 않도록 함
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ChildDao childDao = childDB.childDao();
//                        childDao.deleteAllChildren();
//
//                    }
//                }).start();
                // ToothBrushing에 임의의 toothbrushing 값을 저장하고 싶을 때 활성화
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 임의의 Toothbrushing 값 생성
//                        Toothbrushing toothbrushing = new Toothbrushing("임의로", "2023-11-09", "13h23m21s"
//                                , 0, 1, 2, 3, 4, 5, 6, 7, 8
//                                , 70);
//                        toothbrushing.setChildName("aaaa"); // 원하는 자녀 이름으로 설정
//                        toothbrushing.setLeft_circular(1);
//                        toothbrushing.setMid_circular(2);
//                        toothbrushing.setRight_circular(3);
//                        toothbrushing.setLeft_upper(4);
//                        toothbrushing.setLeft_lower(5);
//                        toothbrushing.setRight_upper(6);
//                        toothbrushing.setRight_lower(7);
//                        toothbrushing.setMid_vertical_upper(8);
//                        toothbrushing.setMid_vertical_lower(9);
//
//                        //Toothbrushing 값을 DB에 저장
//                        ToothbrushingDB.getDatabase(getApplicationContext()).toothbrushingDao().insert(toothbrushing);
//
//                    }
//                }).start();
                startActivity(new Intent(ChildRegisterActivity.this, MainMenuActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChildDB.destroyInstance();
    }
}
