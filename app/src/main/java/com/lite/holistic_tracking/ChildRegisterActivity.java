package com.lite.holistic_tracking;

import static java.security.AccessController.getContext;

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

import com.lite.holistic_tracking.Database.AnimalDB;
import com.lite.holistic_tracking.Database.BuyingDB;
import com.lite.holistic_tracking.Database.ChildDB;
import com.lite.holistic_tracking.Database.ChildDao;
import com.lite.holistic_tracking.Database.MorebrushingDB;
import com.lite.holistic_tracking.Database.SeedDB;
import com.lite.holistic_tracking.Database.SongDB;
import com.lite.holistic_tracking.Database.ToothbrushingDB;
import com.lite.holistic_tracking.Entity.Buying;
import com.lite.holistic_tracking.Entity.Child;
import com.lite.holistic_tracking.Entity.ChildAdapter;
import com.lite.holistic_tracking.Entity.Morebrushing;
import com.lite.holistic_tracking.Entity.Seed;
import com.lite.holistic_tracking.Entity.Toothbrushing;

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
    
        
        // background thread
        class InsertRunnable implements Runnable {
            @Override
            public void run() {

                // 1. 자녀 정보 Child DB에 추가
                Child child = new Child();
                child.childName = childNameEditText.getText().toString(); // 이름
                child.birthdate = birthdateEditText.getText().toString(); // 생년월일
                int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId(); // 성별
                if(selectedGenderId == R.id.maleRadioButton) child.gender = "남자";
                else child.gender = "여자";
                ChildDB.getInstance(mContext).childDao().insertChild(child); // ChildDB에 자녀 정보 등록

                // 2. SeedDB 추가 - 기본으로 plant 1, flower 1, tree 1
                Seed seed = new Seed(childNameEditText.getText().toString()
                , 1, 1, 1, 0, 0, 0);
                SeedDB.getDatabase(getApplicationContext()).seedDao().insert(seed);

                // 3. BuyingDB 추가 - 기본으로 dog, cat 테마 구매
                
                // 강아지 구매
                Buying buying1 = new Buying();
                buying1.setChildName(childNameEditText.getText().toString()); // 이름
                buying1.setAnimalName("강아지");
                BuyingDB.getInstance(getApplicationContext()).buyingDao().insert(buying1);
                
                
                // 고양이 구매
                Buying buying2 = new Buying();
                buying2.setChildName(childNameEditText.getText().toString()); // 이름
                buying2.setAnimalName("고양이");
                BuyingDB.getInstance(getApplicationContext()).buyingDao().insert(buying2);
                
                // 4. MoreDB 추가 - 기본으로 모든 구역 0 세팅
                Morebrushing morebrushing = new Morebrushing(
                        childNameEditText.getText().toString(), 0, 0, 0,
                        0, 0, 0, 0, 0, 0);
                MorebrushingDB.getDatabase(getApplicationContext()).morebrushingDao().insert(morebrushing);
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

            childName = childNameEditText.getText().toString(); // 사용자 입력으로 childName 설정

            // ChildDB에 자녀 등록
            InsertRunnable insertRunnable = new InsertRunnable();
            Thread t = new Thread(insertRunnable);
            t.start();
            
            // 다음 activity에 argument 넘기기
            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
            intent.putExtra("childName", String.valueOf(childName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        tempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //ChildDB.getInstance(getApplicationContext()).childDao().updateChildSeed("김윤진", 80);
                        // 자녀 DB 전부 삭제하기
                        ChildDB.getInstance(getApplicationContext()).childDao().deleteAllChildren();

                        // 노래 DB 전부 삭제하기
                        SongDB.getInstance(getApplicationContext()).songDao().deleteAllSongs();

                        // 동물 DB 전부 삭제하기
                        AnimalDB.getInstance(getApplicationContext()).animalDao().deleteAll();

                        // 씨앗 DB 전부 삭제하기
                        SeedDB.getDatabase(getApplicationContext()).seedDao().deleteAllSeeds();

                        // 구매 DB 전부 삭제하기
                        BuyingDB.getInstance(getApplicationContext()).buyingDao().deleteAllBuyings();

                        // 양치 DB 전부 삭제하기
                        ToothbrushingDB.getDatabase(getApplicationContext()).toothbrushingDao().deleteAll();

//                        ToothbrushingDB.getDatabase(getApplicationContext()).toothbrushingDao().deleteAll();
//                        Toothbrushing toothbrushing1 = new Toothbrushing("kim", "2023-11-17", "9시 08분"
//                                , 10, 8, 12, 5, 9, 7, 8, 10, 10
//                                , 88);
//
//                        Toothbrushing toothbrushing2 = new Toothbrushing("kim", "2023-11-16", "18시 20분"
//                                , 6, 7, 10, 8, 6, 9, 10, 9, 9
//                                , 72);
//
//                        Toothbrushing toothbrushing3 = new Toothbrushing("kim", "2023-11-16", "12시 10분"
//                                , 4, 9, 9, 5, 10, 6, 7, 12, 13
//                                , 75);
//
//                        Toothbrushing toothbrushing4 = new Toothbrushing("kim", "2023-11-16", "9시 00분"
//                                , 6, 7, 10, 8, 6, 9, 10, 9, 9
//                                , 72);
//
//                        Toothbrushing toothbrushing5 = new Toothbrushing("kim", "2023-11-15", "20시 00분"
//                                , 12, 9, 8, 7, 9, 7, 8, 11, 10
//                                , 80);
//
//                        Toothbrushing toothbrushing6 = new Toothbrushing("kim", "2023-11-15", "13시 20분"
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
//                        ChildDB.getInstance(getApplicationContext()).childDao().updateChildSeed("kim", 8);
//                        ChildDB.getInstance(getApplicationContext()).childDao().updateChildSeed("kim", 7);
//                        ChildDB.getInstance(getApplicationContext()).childDao().updateChildSeed("kim", 7);
//                        ChildDB.getInstance(getApplicationContext()).childDao().updateChildSeed("kim", 7);
//                        ChildDB.getInstance(getApplicationContext()).childDao().updateChildSeed("kim", 8);
//                        ChildDB.getInstance(getApplicationContext()).childDao().updateChildSeed("kim", 6);
                    }
                }).start();
            }
        });

//        tempButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                // ChildDB의 값을 전부 삭제하고 싶을 때 활성화
////                // Thread을 생성하고 접근해서 main Thread에서 DB에 접근하지 않도록 함
////                new Thread(new Runnable() {
////                    @Override
////                    public void run() {
//
//                        // 자녀 정보 전부 지우기
////                        ChildDao childDao = childDB.childDao();
////                        childDao.deleteAllChildren();
//////
//////                       // Forest 정보 지우기
////                        SeedDB.getDatabase(getApplicationContext()).seedDao().deleteAllSeeds();
////
////                        // 동물 DB 전부 삭제하기
////                        AnimalDB.getInstance(getApplicationContext()).animalDao().deleteAll();
////
////                        // 노래 DB 전부 삭제하기
////                        SongDB.getInstance(getApplicationContext()).songDao().deleteAllSongs();
////
//////                        SeedDB.getDatabase(getApplicationContext()).seedDao().updatePlantCount("a", 10);
//////                        SeedDB.getDatabase(getApplicationContext()).seedDao().updateFlowerCount("a", 10);
//////                        SeedDB.getDatabase(getApplicationContext()).seedDao().updateTreeCount("a", 10);
////
////                    }
////                }).start();
//                // ToothBrushing에 임의의 toothbrushing 값을 저장하고 싶을 때 활성화
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
//                        Toothbrushing toothbrushing1 = new Toothbrushing("김윤진", "2023-11-17", "9시 08분"
//                                , 10, 8, 12, 5, 9, 7, 8, 10, 10
//                                , 88);
//
//                        Toothbrushing toothbrushing2 = new Toothbrushing("김윤진", "2023-11-16", "18시 20분"
//                                , 6, 7, 10, 8, 6, 9, 10, 9, 9
//                                , 72);
//
//                        Toothbrushing toothbrushing3 = new Toothbrushing("김윤진", "2023-11-16", "12시 10분"
//                                , 4, 9, 9, 5, 10, 6, 7, 12, 13
//                                , 75);
//
//                        Toothbrushing toothbrushing4 = new Toothbrushing("김윤진", "2023-11-16", "9시 00분"
//                                , 6, 7, 10, 8, 6, 9, 10, 9, 9
//                                , 72);
//
//                        Toothbrushing toothbrushing5 = new Toothbrushing("김윤진", "2023-11-15", "20시 00분"
//                                , 12, 9, 8, 7, 9, 7, 8, 11, 10
//                                , 80);
//
//                        Toothbrushing toothbrushing6 = new Toothbrushing("김윤진", "2023-11-15", "13시 20분"
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
//                        ChildDB.getInstance(getApplicationContext()).childDao().updateChildSeed("김윤진", 8);
//                        ChildDB.getInstance(getApplicationContext()).childDao().updateChildSeed("김윤진", 7);
//                        ChildDB.getInstance(getApplicationContext()).childDao().updateChildSeed("김윤진", 7);
//                        ChildDB.getInstance(getApplicationContext()).childDao().updateChildSeed("김윤진", 7);
//                        ChildDB.getInstance(getApplicationContext()).childDao().updateChildSeed("김윤진", 8);
//                        ChildDB.getInstance(getApplicationContext()).childDao().updateChildSeed("김윤진", 6);
//
//
//                    }
//                }).start();
////
////                // toothbrushing에 있는 값을 전부 삭제하고 싶을 때 활성화
////                new Thread(new Runnable() {
////                    @Override
////                    public void run() {
////                        ToothbrushingDB.getDatabase(getApplicationContext()).toothbrushingDao().deleteAll();
////
////                    }
////                }).start();
////
//                 //animal DB 비우고 싶을 때 활성화
////                new Thread(new Runnable() {
////                    @Override
////                    public void run() {
////                        AnimalDB.getInstance(mContext).animalDao().deleteAll();
////                        ChildDao childDao = childDB.childDao();
////                        childDao.deleteAllChildren();
////                    }
////                }).start();
//
//                startActivity(new Intent(ChildRegisterActivity.this, MainMenuActivity.class));
//            }
//        });
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
