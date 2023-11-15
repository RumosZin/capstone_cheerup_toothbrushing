package com.lite.holistic_tracking;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.tag.FieldKey;
import java.io.File;
import java.io.IOException;

public class HeeJunActivity extends AppCompatActivity {
    public ImageView movingImage;
    public Handler handler;
    public float angle = 0;
    public final float radius = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_right_circular);

        movingImage = findViewById(R.id.ballImage);
        handler = new Handler(Looper.getMainLooper());

        startMovingAnimation();
    }

    private void startMovingAnimation() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                updateImagePosition();
                if (angle >= 360) {
                    handler.removeCallbacks(this);
                    resetAnimation();
                } else {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private void updateImagePosition() {
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
    private void resetAnimation() {
        angle = 0;
        startMovingAnimation(); // 애니메이션 재시작
    }

}
