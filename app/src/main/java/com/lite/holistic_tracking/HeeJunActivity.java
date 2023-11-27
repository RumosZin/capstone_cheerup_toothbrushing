package com.lite.holistic_tracking;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.Gravity;


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
    private ImageView toothImageOpened;
    private ImageView ballImageView;
    private ImageView circularballImageView;

    private int toothIndex = 0;
    float initialX;
    float initialY;

    final Handler handler = new Handler();
    private float radius; // Adjust the radius as needed
    private float angle;
    private ValueAnimator circularAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holistic);



        toothImageView = findViewById(R.id.toothImage);
        ballImageView = findViewById(R.id.ballImage);
        circularballImageView = findViewById(R.id.circularBallImage);
        toothImageOpened = findViewById(R.id.toothImageOpened);

        radius = 50.0f;
        angle = 0.0f;
        initialX = toothImageView.getX() + toothImageView.getWidth() * 0.15f;
        initialY = toothImageView.getY() + (toothImageView.getHeight() - circularballImageView.getHeight()) / 2.0f;

        // Start the animation loop
        startAnimationLoop();
    }

    private void startAnimationLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Change toothImage every 16 beats
                toothImageView.setImageResource(toothImages[toothIndex]);

                initialY = toothImageView.getY() + (toothImageView.getHeight() - circularballImageView.getHeight()) / 2.0f;
                Log.d("HeeJunActivity", "2. X: " + circularballImageView.getX() + ", Y: " + circularballImageView.getY());
                Log.d("HeeJunActivity", "toothImageView. X: " + toothImageView.getX() + ", Y: " + toothImageView.getY());

                if (3 <= toothIndex && toothIndex <= 6) {
                    toothImageOpened.setVisibility(View.VISIBLE);
                } else {
                    toothImageOpened.setVisibility(View.INVISIBLE);
                }

                if (0 <= toothIndex && toothIndex <= 2) {
                    ballImageView.setVisibility(View.INVISIBLE);
                    circularballImageView.setVisibility(View.VISIBLE);
                } else {
                    ballImageView.setVisibility(View.VISIBLE);
                    circularballImageView.setVisibility(View.INVISIBLE);
                }

                // Update ballImage animation
                updateBallAnimation();

                // Increment counters
//                toothIndex = (toothIndex + 1) % toothImages.length;
                toothIndex = (toothIndex + 1) % 3;
                // Repeat the animation loop
                startAnimationLoop();
            }
        }, calculateDelay());  // Set a delay based on BPM
    }

    @SuppressLint("ResourceType")
    private void updateBallAnimation() {
        int animationResId;
        Animation animation;

        circularAnimator = ValueAnimator.ofFloat(0, 360);
        circularAnimator.setDuration(1000); // Set the duration of one complete rotation (in milliseconds)
        circularAnimator.setRepeatCount(ValueAnimator.INFINITE); // Infinite rotation
        circularAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                updateCircularPosition(animatedValue);
            }
        });

        circularAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                // Animation start
                Log.d("HeeJunActivity", "4. X: " + circularballImageView.getX() + ", Y: " + circularballImageView.getY());

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // Animation end
                Log.d("HeeJunActivity", "5. X: " + circularballImageView.getX() + ", Y: " + circularballImageView.getY());

            }
        });
        switch(toothIndex){
            case 0:
                initialX = toothImageView.getX() + toothImageView.getWidth() * 0.15f;
                Log.d("HeeJunActivity", "3. X: " + circularballImageView.getX() + ", Y: " + circularballImageView.getY());
                circularAnimator.start();

                break;
            case 1:

                initialX = toothImageView.getX() + toothImageView.getWidth() * 0.5f;
                circularAnimator.start();
                break;
            case 2:

                initialX = toothImageView.getX() + toothImageView.getWidth() * 0.75f;
                circularAnimator.start();
                break;
            case 3:
                animationResId = R.animator.h_left_lower;
                animation = AnimationUtils.loadAnimation(this, animationResId);
                ballImageView.startAnimation(animation);
                break;
            case 4:
                animationResId = R.animator.h_right_lower;
                animation = AnimationUtils.loadAnimation(this, animationResId);
                ballImageView.startAnimation(animation);
                break;
            case 5:
                animationResId = R.animator.h_left_upper;
                animation = AnimationUtils.loadAnimation(this, animationResId);
                ballImageView.startAnimation(animation);
                break;
            case 6:
                animationResId = R.animator.h_right_upper;
                animation = AnimationUtils.loadAnimation(this, animationResId);
                ballImageView.startAnimation(animation);
                break;
            case 7:
                animationResId = R.animator.h_left_lower_inner;
                animation = AnimationUtils.loadAnimation(this, animationResId);
                ballImageView.startAnimation(animation);
                break;
            case 8:
                animationResId = R.animator.h_mid_vertical_lower_inner;
                animation = AnimationUtils.loadAnimation(this, animationResId);
                ballImageView.startAnimation(animation);
                break;
            case 9:
                animationResId = R.animator.h_right_lower_inner;
                animation = AnimationUtils.loadAnimation(this, animationResId);
                ballImageView.startAnimation(animation);
                break;
            case 10:
                animationResId = R.animator.h_left_upper_inner;
                animation = AnimationUtils.loadAnimation(this, animationResId);
                ballImageView.startAnimation(animation);
                break;
            case 11:
                animationResId = R.animator.h_mid_vertical_upper_inner;
                animation = AnimationUtils.loadAnimation(this, animationResId);
                ballImageView.startAnimation(animation);
                break;
            case 12:
                animationResId = R.animator.h_right_upper_inner;
                animation = AnimationUtils.loadAnimation(this, animationResId);
                ballImageView.startAnimation(animation);
                break;
        }
    }


    private void updateCircularPosition(float animatedValue) {
        // Calculate the new position based on the angle
        float x = (float) (radius * Math.cos(Math.toRadians(animatedValue)));
        float y = (float) (radius * Math.sin(Math.toRadians(animatedValue)));

        // Set the new position for the ImageView
        circularballImageView.setX(initialX + x - circularballImageView.getWidth() / 2.0f);
        circularballImageView.setY(initialY + y - circularballImageView.getHeight() / 2.0f);
        Log.d("HeeJunActivity", "f. X: " + circularballImageView.getX() + ", Y: " + circularballImageView.getY());

    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release resources when the activity is destroyed
        if (circularAnimator != null) {
            circularAnimator.cancel();
            circularAnimator = null;
        }
    }
    private void activateCircularMotion(){
        handler.postDelayed(new Runnable() {
            public void run() {

                updateCircularPosition();
//                if (angle >= 360) angle -= 360;
//                else handler.postDelayed(this, 16);
                if (angle >= 360) {
                    angle = 0;
                    handler.post(this);
                } else {
                    handler.postDelayed(this, 16);
                }
            }
        }, 16);
    }

    private void updateCircularPosition() {
        float cx = circularballImageView.getWidth() / 2.0f + circularballImageView.getX();
        float cy = circularballImageView.getHeight() / 2.0f + circularballImageView.getY();

        angle += 5.0f;

        float x = (float) (cx + (radius * Math.sin(Math.toRadians(angle))));
        float y = (float) (cy + (radius * Math.cos(Math.toRadians(angle))));

        circularballImageView.setX(x - circularballImageView.getWidth() / 2.0f);
        circularballImageView.setY(y - circularballImageView.getHeight() / 2.0f);
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