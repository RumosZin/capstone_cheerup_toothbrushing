package com.lite.holistic_tracking;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;



public class HeeJunActivity extends AppCompatActivity {

    private final float radius = 5;
    private float angle = 0.0f;
    private boolean direction = true;
    private ImageView toothImage, toothImageOpened, ballImage;
    final Handler handler = new Handler();
    private final int[] toothimages = {

            R.drawable.left_circular_image,
            R.drawable.mid_circular_image,
            R.drawable.right_circular_image,

            R.drawable.left_lower_image,    // need
            R.drawable.right_lower_image,   // need
            R.drawable.left_upper_image,    // need
            R.drawable.right_upper_image,   // need

            R.drawable.left_lower_inner_image,
            R.drawable.mid_lower_inner_image,
            R.drawable.right_lower_inner_image,
            R.drawable.left_upper_inner_image,
            R.drawable.mid_upper_inner_image,
            R.drawable.right_upper_inner_image,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holistic); // 초기 레이아웃 설정

        ballImage = findViewById(R.id.ballImage);
        toothImage = findViewById(R.id.toothImage);
        toothImageOpened = findViewById(R.id.toothImageOpened);
        handler.postDelayed(new Runnable() {
//            private int index = 1;
            private int index = 3;
            public void run() {
                toothImage.setImageResource((toothimages[index]));

                if (3 <= index && index <= 6) toothImageOpened.setVisibility(View.VISIBLE);
                else toothImageOpened.setVisibility(View.INVISIBLE);

                switchPosition(ballImage, index);

//                index = (index + 1) % toothimages.length;
                handler.postDelayed(this, 5000);
            }
        }, 5000);
    }


    private void switchPosition(ImageView ballImage, int index) {

        switch (index) {
            case 0:
                ballImage.setX(toothImage.getX() + toothImage.getWidth() * 0.25f);
                ballImage.setY(toothImage.getY() + (toothImage.getHeight() - ballImage.getHeight()) / 2.0f);
                activateCircularMotion();
                break;
            case 1:
                ballImage.setX(toothImage.getX() + toothImage.getWidth() * 0.5f);
                ballImage.setY(toothImage.getY() + (toothImage.getHeight() - ballImage.getHeight()) / 2.0f);
                activateCircularMotion();
                break;
            case 2:
                ballImage.setX(toothImage.getX() + toothImage.getWidth() * 0.75f);
                ballImage.setY(toothImage.getY() + (toothImage.getHeight() - ballImage.getHeight()) / 2.0f);
                activateCircularMotion();
                break;
            default:
                // AnimatorSet을 로드하여 애니메이션 설정
                AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.anim.diagonal);

                // 애니메이션 시작
                animatorSet.start();
                break;
        }
    }

    private void activateCircularMotion(){
        handler.postDelayed(new Runnable() {
            public void run() {

                updateCircularPosition();
                if (angle >= 360) angle = 0;
                else handler.postDelayed(this, 16);
            }
        }, 16);
    }

    private void updateCircularPosition() {
        float cx = ballImage.getWidth() / 2.0f + ballImage.getX();
        float cy = ballImage.getHeight() / 2.0f + ballImage.getY();

        angle += 5.0f;

        float x = (float) (cx + (radius * Math.sin(Math.toRadians(angle))));
        float y = (float) (cy + (radius * Math.cos(Math.toRadians(angle))));

        ballImage.setX(x - ballImage.getWidth() / 2.0f);
        ballImage.setY(y - ballImage.getHeight() / 2.0f);
    }
}