package com.lite.holistic_tracking;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.lite.holistic_tracking.Database.ChildDB;
import com.lite.holistic_tracking.Database.ChildDao;
import com.lite.holistic_tracking.Database.SeedDB;
import com.lite.holistic_tracking.Database.TotalBrushingDB;
import com.lite.holistic_tracking.Entity.Child;
import com.lite.holistic_tracking.Entity.ChildAdapter;
import com.lite.holistic_tracking.Entity.Seed;
import com.lite.holistic_tracking.Entity.TotalBrushing;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ChildRegisterActivity extends AppCompatActivity {

    //private List<Child> childList;
    private ChildDB childDB = null;
    private Context mContext = null;

    private EditText childNameEditText;
    private EditText birthdateEditText;
    private RadioGroup genderRadioGroup;
    private ChildAdapter childAdapter;

    private Button saveButton;
    private Button tempButton;
    private ImageView genderImageView;
    private String childName;

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setSubtitle("");


        // 버튼과 EditText, RadioGroup 초기화
        saveButton = findViewById(R.id.saveButton);
        tempButton = findViewById(R.id.tempButton);
        childNameEditText = (EditText) findViewById(R.id.childNameEditText);
        birthdateEditText = (EditText) findViewById(R.id.birthdateEditText);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        genderRadioGroup.check(R.id.maleRadioButton); // 남자아이버튼 default로 클릭되어 있음
        //genderImageView = findViewById(R.id.genderImageView);

        birthdateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ChildRegisterActivity.this,
                        myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(ChildRegisterActivity.this,
//                        new TimePickerDialog.OnTimeSetListener() {
//                            @Override
//                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                                String state = "AM"; // 선택한 시간이 12를 넘을 경우 ...
//                            }
//                        });
            }
        });

        childDB = ChildDB.getInstance(this);
        mContext = getApplicationContext();

        class InsertRunnable implements Runnable {
            @Override
            public void run() {

                // 1. 자녀 정보 Child DB에 추가
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

                // Total Brushing 정보 추가
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

                // 3. SeedDB 추가
                Seed seed = new Seed(childNameEditText.getText().toString()
                , 2, 1, 1, 0, 0, 0);
                SeedDB.getDatabase(getApplicationContext()).seedDao().insert(seed);
            }
        }

        saveButton.setOnClickListener(v -> {
            // 사용자가 입력한 값을 가져오기
            String enteredChildName = childNameEditText.getText().toString().trim();
            String enteredBirthdate = birthdateEditText.getText().toString().trim();

            // 이름과 생일이 무조건 입력되어야 함
            if (enteredChildName.isEmpty()) {
                // 이름이나 생일 중 하나라도 입력되지 않은 경우
                Toast.makeText(ChildRegisterActivity.this, "자녀의 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }
            // 이름과 생일이 무조건 입력되어야 함
            if (enteredBirthdate.isEmpty()) {
                // 이름이나 생일 중 하나라도 입력되지 않은 경우
                Toast.makeText(ChildRegisterActivity.this, "자녀의 생일을 선택하세요", Toast.LENGTH_SHORT).show();
                return;
            }
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
//                // ChildDB의 값을 전부 삭제하고 싶을 때 활성화
//                // Thread을 생성하고 접근해서 main Thread에서 DB에 접근하지 않도록 함
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        // 자녀 정보 전부 지우기
                        ChildDao childDao = childDB.childDao();
                        childDao.deleteAllChildren();

                        // Forest 정보 지우기
                        SeedDB.getDatabase(getApplicationContext()).seedDao().deleteAllSeeds();

                    }
                }).start();
                // ToothBrushing에 임의의 toothbrushing 값을 저장하고 싶을 때 활성화
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 임의의 Toothbrushing 값 생성
////                        ChildDao childDao = childDB.childDao();
////                        childDao.deleteAllChildren();
//
//                        //ChildDB.getInstance(getApplicationContext()).childDao().insertChild();
//
//                        ToothbrushingDB.getDatabase(getApplicationContext()).toothbrushingDao().deleteAll();
//                        Toothbrushing toothbrushing1 = new Toothbrushing("곽희준", "2023-11-17", "9시 08분"
//                                , 10, 8, 12, 5, 9, 7, 8, 10, 10
//                                , 88);
//
//                        Toothbrushing toothbrushing2 = new Toothbrushing("곽희준", "2023-11-16", "18시 20분"
//                                , 6, 7, 10, 8, 6, 9, 10, 9, 9
//                                , 72);
//
//                        Toothbrushing toothbrushing3 = new Toothbrushing("곽희준", "2023-11-16", "12시 10분"
//                                , 4, 9, 9, 5, 10, 6, 7, 12, 13
//                                , 75);
//
//                        Toothbrushing toothbrushing4 = new Toothbrushing("곽희준", "2023-11-16", "9시 00분"
//                                , 6, 7, 10, 8, 6, 9, 10, 9, 9
//                                , 72);
//
//                        Toothbrushing toothbrushing5 = new Toothbrushing("곽희준", "2023-11-15", "20시 00분"
//                                , 12, 9, 8, 7, 9, 7, 8, 11, 10
//                                , 80);
//
//                        Toothbrushing toothbrushing6 = new Toothbrushing("곽희준", "2023-11-15", "13시 20분"
//                                , 6, 7, 5, 8, 6, 9, 7, 8, 7
//                                , 60);
//
//                        //Toothbrushing 값을 DB에 저장
//                        ToothbrushingDB.getDatabase(getApplicationContext()).toothbrushingDao().insert(toothbrushing1);
//                        ToothbrushingDB.getDatabase(getApplicationContext()).toothbrushingDao().insert(toothbrushing2);
//                        ToothbrushingDB.getDatabase(getApplicationContext()).toothbrushingDao().insert(toothbrushing3);
//                        ToothbrushingDB.getDatabase(getApplicationContext()).toothbrushingDao().insert(toothbrushing4);
//                        ToothbrushingDB.getDatabase(getApplicationContext()).toothbrushingDao().insert(toothbrushing5);
//                        ToothbrushingDB.getDatabase(getApplicationContext()).toothbrushingDao().insert(toothbrushing6);
//
//                        ChildDB.getInstance(getApplicationContext()).childDao().updateChildSeed("곽희준", 8);
//                        ChildDB.getInstance(getApplicationContext()).childDao().updateChildSeed("곽희준", 7);
//                        ChildDB.getInstance(getApplicationContext()).childDao().updateChildSeed("곽희준", 7);
//                        ChildDB.getInstance(getApplicationContext()).childDao().updateChildSeed("곽희준", 7);
//                        ChildDB.getInstance(getApplicationContext()).childDao().updateChildSeed("곽희준", 8);
//                        ChildDB.getInstance(getApplicationContext()).childDao().updateChildSeed("곽희준", 6);
//
//
//                    }
//                }).start();
//
//                // toothbrushing에 있는 값을 전부 삭제하고 싶을 때 활성화
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ToothbrushingDB.getDatabase(getApplicationContext()).toothbrushingDao().deleteAll();
//
//                    }
//                }).start();
//
                 //animal DB 비우고 싶을 때 활성화
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        AnimalDB.getInstance(mContext).animalDao().deleteAll();
//                        ChildDao childDao = childDB.childDao();
//                        childDao.deleteAllChildren();
//                    }
//                }).start();

                startActivity(new Intent(ChildRegisterActivity.this, MainMenuActivity.class));
            }
        });
    }

    private void updateLabel() {
        String format = "yyyy/MM/dd"; // 출력 형식
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);

        birthdateEditText.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChildDB.destroyInstance();
    }
}
