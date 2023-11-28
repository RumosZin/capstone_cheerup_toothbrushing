package com.lite.holistic_tracking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.lite.holistic_tracking.Database.AnimalDB;
import com.lite.holistic_tracking.Database.AnimalDao;
import com.lite.holistic_tracking.Database.ChildDB;
import com.lite.holistic_tracking.Database.SongDB;
import com.lite.holistic_tracking.Database.SongDao;
import com.lite.holistic_tracking.Entity.Animal;
import com.lite.holistic_tracking.Entity.Child;
import com.lite.holistic_tracking.Entity.ChildAdapter;
import com.lite.holistic_tracking.Entity.Song;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private ChildDB childDB = null;
    private AnimalDB animalDB = null;
    private SongDB songDB = null;
    private List<Child> childList;
    private int childNum = -1; // 초기에 1로 설정
    private int animalNum = -1; // 초기에 -1로 설정
    private int songNum = -1; // 초기에 -1로 설정
    private Context mContext = null;
    private ChildAdapter childAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // context, adapter 연결
        mContext = getApplicationContext();
        childAdapter = new ChildAdapter(childList, this);

        // DB 생성
        childDB = ChildDB.getInstance(this);

        // main thread에서 db 접근 불가, crud 시 thread 이용하기
        class InsertRunnable implements Runnable {

            @Override
            public void run() {
                try {
                    childNum = ChildDB.getInstance(mContext).childDao().getChildCount();
                    animalNum = AnimalDB.getInstance(mContext).animalDao().getAnimalCount();
                    songNum = SongDB.getInstance(mContext).songDao().getSongCount();

                    
                    // 동물 없을 때만 동물 정보들 추가
                    if(animalNum <= 0) {
                        AnimalDao animalDao = AnimalDB.getInstance(mContext).animalDao();

                        Animal cat = new Animal(R.drawable.cat_image, "고양이", 10);
                        Animal dog = new Animal(R.drawable.dog_image, "강아지", 15);
                        Animal fox = new Animal(R.drawable.fox_image, "여우", 20);
                        Animal rabbit = new Animal(R.drawable.rabbit_image, "토끼", 10);
                        Animal lion = new Animal(R.drawable.lion_image, "사자", 15);

                        animalDao.insert(cat);
                        animalDao.insert(dog);
                        animalDao.insert(fox);
                        animalDao.insert(rabbit);
                        animalDao.insert(lion);
                    }
                    
                    // 노래 없을 때만 노래 추가
                    if(songNum <= 0) {
                        Log.v("hihihiihhih", "2222222222222222222222222");
                        // mContext가 null인 경우 처리
                        if (mContext != null) {
                            Log.v("hihihiihhih", "333333333333333333333333333");
                            //musicDB = MusicDB.getInstance(mContext);
                            SongDao songDao = SongDB.getInstance(mContext).songDao();
                            Log.v("hihihiihhih", "4444444444444444444444");

                            Song music1 = new Song(R.drawable.dog_image, "music1", 70, 120, 1);
                            Song music2 = new Song(R.drawable.cat_image, "music2", 70, 120, 2);
                            Song music3 = new Song(R.drawable.fox_image, "music3", 70, 120, 3);
                            Song music4 = new Song(R.drawable.lion_image, "music4", 70, 120, 4);
                            Song music5 = new Song(R.drawable.plant_image, "music5", 70, 120, 5);
                            Song music6 = new Song(R.drawable.flower_image, "music6", 70, 120, 6);
                            Song music7 = new Song(R.drawable.tree_image, "music7", 70, 120, 7);
                            Log.v("hihihiihhih", "5555555555555555555");

                            songDao.insert(music1);
                            songDao.insert(music2);
                            songDao.insert(music3);
                            songDao.insert(music4);
                            songDao.insert(music5);
                            songDao.insert(music6);
                            songDao.insert(music7);

                            Log.v("hihihiihhih", "66666666666666666666666666");
                        } else {
                            Log.e("Error", "mContext is null");
                        }
                    }

                    // DB에서 자녀 정보가 있는지 확인
                    if (childNum <= 0) {
                        // 자녀 정보가 없는 경우 InitialActivity로 이동
                        startActivity(new Intent(MainActivity.this, InitialActivity.class));
                    } else {
                        // 자녀 정보가 있는 경우 ShowAllChildActivity로 이동
                        Log.v("in mainactivity", "next page is showall...");
                        startActivity(new Intent(MainActivity.this, ShowAllChildActivity.class));
                    }
                    // 현재 액티비티 종료
                    finish();
                }
                catch (Exception e) {
                    Log.v("testsestestesttet", "1111111111111111111111111111111");
                }
            }
        }
        
        InsertRunnable insertRunnable = new InsertRunnable();
        Thread t = new Thread(insertRunnable);
        t.start();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChildDB.destroyInstance();
        childDB = null;
    }

}
