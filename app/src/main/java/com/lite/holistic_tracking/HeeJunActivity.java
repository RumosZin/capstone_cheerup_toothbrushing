package com.lite.holistic_tracking;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class HeeJunActivity extends AppCompatActivity {
    public ImageView movingImage;
    public Handler handler;
    public float angle = 0;
    public final float radius = 5;
    private float initialX;
    private float initialY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heejun);

        movingImage = findViewById(R.id.ballImage);
        handler = new Handler(Looper.getMainLooper());

        initialX = movingImage.getX();
        initialY = movingImage.getY();

        // 주기적으로 이미지 위치 업데이트
        startMovingAnimation();
    }

    private void startMovingAnimation() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateImagePosition();
                handler.postDelayed(this, 16); // 60fps 기준으로 업데이트 (1000ms / 60)
            }
        });
    }

    private void updateImagePosition() {
        float cx = movingImage.getWidth() / 2.0f + movingImage.getX();
        float cy = movingImage.getHeight() / 2.0f + movingImage.getY();

        // 각도를 증가시켜서 원을 따라 움직이도록 함
        angle += 5.0f;
        if (angle > 360) resetAnimation();

        // 좌표 업데이트
        float x = (float) (cx + (radius * Math.sin(Math.toRadians(angle))));
        float y = (float) (cy + (radius * Math.cos(Math.toRadians(angle))));

        // 이미지 위치 설정
        movingImage.setX(x - movingImage.getWidth() / 2.0f);
        movingImage.setY(y - movingImage.getHeight() / 2.0f);
    }

    private void resetAnimation() {
        angle = 0;
        movingImage.setX(initialX);
        movingImage.setY(initialY);
    }

}
