package com.lite.holistic_tracking;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class HeeJunActivity extends AppCompatActivity {

    private final int[] toothImages = {
            R.drawable.left_circular_image,
            R.drawable.mid_circular_image,
            R.drawable.right_circular_image,
            R.drawable.left_lower_image,
            R.drawable.right_lower_image,
            R.drawable.left_upper_image,
            R.drawable.right_upper_image,
            R.drawable.left_lower_inner_image,
            R.drawable.mid_lower_inner_image,
            R.drawable.right_lower_inner_image,
            R.drawable.left_upper_inner_image,
            R.drawable.mid_upper_inner_image,
            R.drawable.right_upper_inner_image,
    };

    private ImageView toothImageView;
    private ImageView ballImageView;
    private ImageView toothImageOpened;

    private int currentToothImageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holistic);

        toothImageView = findViewById(R.id.toothImage);
        ballImageView = findViewById(R.id.ballImage);
        toothImageOpened = findViewById(R.id.toothImageOpened);

        // Start the animation loop
        startAnimationLoop();
    }

    private void startAnimationLoop() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Change toothImage every 16 beats
                toothImageView.setImageResource(toothImages[currentToothImageIndex]);
                if (3 <= currentToothImageIndex && currentToothImageIndex <= 6) toothImageOpened.setVisibility(View.VISIBLE);
                else toothImageOpened.setVisibility(View.INVISIBLE);
                // Update ballImage animation
                updateBallAnimation();

                // Increment counters
                currentToothImageIndex = (currentToothImageIndex + 1) % toothImages.length;

                // Repeat the animation loop
                startAnimationLoop();
            }
        }, calculateDelay());  // Set a delay based on BPM
    }

    private void updateBallAnimation() {
        int animationResId;

//        if (currentToothImageIndex >= 0 && currentToothImageIndex <= 2) {
//            // Circular animations for index 0, 1, 2
//            animationResId = R.anim.circular;
//        } else {
            // Diagonal animations for other indices
            animationResId = R.animator.h_left_lower;
//        }

        switch(currentToothImageIndex){
            case 0:
                animationResId = R.animator.h_left_lower;
                break;
            case 1:
                animationResId = R.animator.h_left_lower;
                break;
            case 2:
                animationResId = R.animator.h_left_lower_inner;
                break;
            case 3:
                animationResId = R.animator.h_left_lower;
                break;
            case 4:
                animationResId = R.animator.h_right_lower;
                break;
            case 5:
                animationResId = R.animator.h_left_upper;
                break;
            case 6:
                animationResId = R.animator.h_right_upper;
                break;
            case 7:
                animationResId = R.animator.h_left_lower_inner;
                break;
            case 8:
                animationResId = R.animator.h_mid_vertical_lower_inner;
                break;
            case 9:
                animationResId = R.animator.h_right_lower_inner;
                break;
            case 10:
                animationResId = R.animator.h_left_upper_inner;
                break;
            case 11:
                animationResId = R.animator.h_mid_vertical_upper_inner;
                break;
            case 12:
                animationResId = R.animator.h_right_upper_inner;
                break;
        }

        @SuppressLint("ResourceType") Animation animation = AnimationUtils.loadAnimation(this, animationResId);
        ballImageView.startAnimation(animation);

    }


    private long calculateDelay() {
        // TODO: Implement delay calculation based on BPM
        // Example: return (long) (60000 / bpm); for beats per minute
        return 1000;  // Default delay of 1 second
    }
}













//
//public class HeeJunActivity extends AppCompatActivity {
//
//    private final float radius = 5;
//    private float angle = 0.0f;
//    private boolean direction = true;
//    private ImageView toothImage, toothImageOpened, ballImage;
//    final Handler handler = new Handler();
//    private final int[] toothimages = {
//
//            R.drawable.left_circular_image,
//            R.drawable.mid_circular_image,
//            R.drawable.right_circular_image,
//
//            R.drawable.left_lower_image,    // need
//            R.drawable.right_lower_image,   // need
//            R.drawable.left_upper_image,    // need
//            R.drawable.right_upper_image,   // need
//
//            R.drawable.left_lower_inner_image,
//            R.drawable.mid_lower_inner_image,
//            R.drawable.right_lower_inner_image,
//            R.drawable.left_upper_inner_image,
//            R.drawable.mid_upper_inner_image,
//            R.drawable.right_upper_inner_image,
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.holistic); // 초기 레이아웃 설정
//
//        ballImage = findViewById(R.id.ballImage);
//        toothImage = findViewById(R.id.toothImage);
//        toothImageOpened = findViewById(R.id.toothImageOpened);
//        handler.postDelayed(new Runnable() {
////            private int index = 1;
//            private int index = 3;
//            public void run() {
//                toothImage.setImageResource((toothimages[index]));
//
//                if (3 <= index && index <= 6) toothImageOpened.setVisibility(View.VISIBLE);
//                else toothImageOpened.setVisibility(View.INVISIBLE);
//
//                switchPosition(ballImage, index);
//
////                index = (index + 1) % toothimages.length;
//                handler.postDelayed(this, 5000);
//            }
//        }, 5000);
//    }
//
//
//    private void switchPosition(ImageView ballImage, int index) {
//
//        switch (index) {
//            case 0:
//                ballImage.setX(toothImage.getX() + toothImage.getWidth() * 0.25f);
//                ballImage.setY(toothImage.getY() + (toothImage.getHeight() - ballImage.getHeight()) / 2.0f);
//                activateCircularMotion();
//                break;
//            case 1:
//                ballImage.setX(toothImage.getX() + toothImage.getWidth() * 0.5f);
//                ballImage.setY(toothImage.getY() + (toothImage.getHeight() - ballImage.getHeight()) / 2.0f);
//                activateCircularMotion();
//                break;
//            case 2:
//                ballImage.setX(toothImage.getX() + toothImage.getWidth() * 0.75f);
//                ballImage.setY(toothImage.getY() + (toothImage.getHeight() - ballImage.getHeight()) / 2.0f);
//                activateCircularMotion();
//                break;
//            default:
//                // AnimatorSet을 로드하여 애니메이션 설정
//                AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.anim.diagonal);
//
//                // 애니메이션 시작
//                animatorSet.start();
//                break;
//        }
//    }
//
//    private void activateCircularMotion(){
//        handler.postDelayed(new Runnable() {
//            public void run() {
//
//                updateCircularPosition();
//                if (angle >= 360) angle = 0;
//                else handler.postDelayed(this, 16);
//            }
//        }, 16);
//    }
//
//    private void updateCircularPosition() {
//        float cx = ballImage.getWidth() / 2.0f + ballImage.getX();
//        float cy = ballImage.getHeight() / 2.0f + ballImage.getY();
//
//        angle += 5.0f;
//
//        float x = (float) (cx + (radius * Math.sin(Math.toRadians(angle))));
//        float y = (float) (cy + (radius * Math.cos(Math.toRadians(angle))));
//
//        ballImage.setX(x - ballImage.getWidth() / 2.0f);
//        ballImage.setY(y - ballImage.getHeight() / 2.0f);
//    }
//}