package com.lite.holistic_tracking;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class HeeJunActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heejun);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.left_circular_animation);

        ImageView ballImage = findViewById(R.id.ballImage);

        ballImage.startAnimation(animation);
    }
}