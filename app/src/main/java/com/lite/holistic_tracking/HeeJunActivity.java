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
    public final float radius = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heejun);

        movingImage = findViewById(R.id.ballImage);
        handler = new Handler(Looper.getMainLooper());

        // 주기적으로 이미지 위치 업데이트
        startMovingAnimation();
    }

    private void startMovingAnimation() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateImagePosition();
                rotateImage();
                handler.postDelayed(this, 16); // 60fps 기준으로 업데이트 (1000ms / 60)
            }
        });
    }

    private void updateImagePosition() {
        float cx = movingImage.getWidth() / 2.0f + movingImage.getX();
        float cy = movingImage.getHeight() / 2.0f + movingImage.getY();

        // 각도를 증가시켜서 원을 따라 움직이도록 함
        angle += 1.0f;
        if (angle > 360) {
            angle = 0;
        }

        // 좌표 업데이트
        float x = (float) (cx + (radius * Math.sin(Math.toRadians(angle))));
        float y = (float) (cy + (radius * Math.cos(Math.toRadians(angle))));

        // 이미지 위치 설정
        movingImage.setX(x - movingImage.getWidth() / 2.0f);
        movingImage.setY(y - movingImage.getHeight() / 2.0f);
    }

    private void rotateImage() {
        // 이미지 회전 애니메이션
        RotateAnimation rotate = new RotateAnimation(angle, angle + 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(16); // 60fps 기준으로 업데이트 (1000ms / 60)
        rotate.setFillAfter(true);
        movingImage.startAnimation(rotate);
    }
}
