//package com.lite.holistic_tracking;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.widget.ImageView;
//
//
//public class HeeJunActivity extends AppCompatActivity {
//    private ImageView movingImage;
//    private Handler handler;
//    private int areaIdx = 0;
//    private float angle = 0;
//    private final float radius = 5;
//
//    private final int[] layoutResIds = {
//            R.layout.h_left_circular,
//            R.layout.h_mid_circular,
//            R.layout.h_right_circular
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(layoutResIds[areaIdx]); // 초기 레이아웃 설정
//
//        movingImage = findViewById(R.id.ballImage);
//        handler = new Handler(Looper.getMainLooper());
//
//    }
//}
//


package com.lite.holistic_tracking;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;


public class HeeJunActivity extends AppCompatActivity {
    private ImageView movingImage;
    private Handler handler;
    private int areaIdx = 0;
    private float angle = 0;
    private final float radius = 5;

    private final int[] layoutResIds = {
            R.layout.h_left_circular,
            R.layout.h_mid_circular,
            R.layout.h_right_circular
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResIds[areaIdx]); // 초기 레이아웃 설정

        movingImage = findViewById(R.id.ballImage);
        handler = new Handler(Looper.getMainLooper());

        // 여기에 시간마다 적용할 animation 함수들 추가
        startCircularAnimation();
        startLayoutChange();
    }

    private void startCircularAnimation() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateCircularPosition();
                if (angle >= 360) {
                    resetCircularAnimation();
                } else {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private void updateCircularPosition() {
        float cx = movingImage.getWidth() / 2.0f + movingImage.getX();
        float cy = movingImage.getHeight() / 2.0f + movingImage.getY();

        // 각도를 증가시켜서 원을 따라 움직이도록 함
        angle += 5.0f;

        // 좌표 업데이트
        float x = (float) (cx + (radius * Math.sin(Math.toRadians(angle))));
        float y = (float) (cy + (radius * Math.cos(Math.toRadians(angle))));

        // 이미지 위치 설정
        movingImage.setX(x - movingImage.getWidth() / 2.0f);
        movingImage.setY(y - movingImage.getHeight() / 2.0f);
    }

    private void resetCircularAnimation() {
        angle = 0;
        startCircularAnimation(); // 애니메이션 재시작
    }

    private void startLayoutChange() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeLayout();
                startCircularAnimation();
                startLayoutChange(); // 레이아웃 변경 후 다시 시작
            }
        }, 5000);
    }

    private void changeLayout() {
        areaIdx = (areaIdx + 1) % layoutResIds.length; // 다음 레이아웃 인덱스 계산
        setContentView(layoutResIds[areaIdx]); // 레이아웃 변경
    }
}