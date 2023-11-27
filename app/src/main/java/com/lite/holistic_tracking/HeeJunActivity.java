package com.lite.holistic_tracking;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.animation.ValueAnimator;
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

        startAnimation();
    }


    private void startAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toothImageView.setImageResource(toothImages[toothIndex]);
                initialY = (toothImageView.getY() + (toothImageView.getHeight() - circularballImageView.getHeight()) / 2.0f)+50;

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

                startBallControl();
                toothIndex = (toothIndex + 1) % toothImages.length;

                // loop
                startAnimation();
            }
        }, calculateDelay());  // Set a delay based on BPM
    }

    @SuppressLint("ResourceType")
    private void startBallControl() {
        int animationIndex;
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
//
//        circularAnimator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                // Animation start
//            }
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                // Animation end
//            }
//        });

        switch(toothIndex){
            case 0:
                ballImageView.clearAnimation();
                initialX = toothImageView.getX() + toothImageView.getWidth() * 0.15f;
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
                animationIndex = R.animator.h_left_lower;
                animation = AnimationUtils.loadAnimation(this, animationIndex);
                ballImageView.startAnimation(animation);
                break;
            case 4:
                animationIndex = R.animator.h_right_lower;
                animation = AnimationUtils.loadAnimation(this, animationIndex);
                ballImageView.startAnimation(animation);
                break;
            case 5:
                animationIndex = R.animator.h_left_upper;
                animation = AnimationUtils.loadAnimation(this, animationIndex);
                ballImageView.startAnimation(animation);
                break;
            case 6:
                animationIndex = R.animator.h_right_upper;
                animation = AnimationUtils.loadAnimation(this, animationIndex);
                ballImageView.startAnimation(animation);
                break;
            case 7:
                animationIndex = R.animator.h_left_lower_inner;
                animation = AnimationUtils.loadAnimation(this, animationIndex);
                ballImageView.startAnimation(animation);
                break;
            case 8:
                animationIndex = R.animator.h_mid_vertical_lower_inner;
                animation = AnimationUtils.loadAnimation(this, animationIndex);
                ballImageView.startAnimation(animation);
                break;
            case 9:
                animationIndex = R.animator.h_right_lower_inner;
                animation = AnimationUtils.loadAnimation(this, animationIndex);
                ballImageView.startAnimation(animation);
                break;
            case 10:
                animationIndex = R.animator.h_left_upper_inner;
                animation = AnimationUtils.loadAnimation(this, animationIndex);
                ballImageView.startAnimation(animation);
                break;
            case 11:
                animationIndex = R.animator.h_mid_vertical_upper_inner;
                animation = AnimationUtils.loadAnimation(this, animationIndex);
                ballImageView.startAnimation(animation);
                break;
            case 12:
                animationIndex = R.animator.h_right_upper_inner;
                animation = AnimationUtils.loadAnimation(this, animationIndex);
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

    }

    private long calculateDelay() {
        // TODO: Implement delay calculation based on BPM
        // Example: return (long) (60000 / bpm); for beats per minute
        return 5000;  // Default delay of 1 second
    }
}