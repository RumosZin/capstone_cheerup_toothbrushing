package com.lite.holistic_tracking;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.lite.holistic_tracking.Database.ChildDB;
import com.lite.holistic_tracking.Database.SeedDB;
import com.lite.holistic_tracking.Database.ToothbrushingDB;
import com.lite.holistic_tracking.Entity.Toothbrushing;

import java.util.Random;

public class RandomRewardDialog extends Dialog {

    private TextView randomNumView;
    private Button confirmButton;
    private ImageView randomImageView;
    private int randomSeed;
    private int randomNum;

    private Context mContext;

    public RandomRewardDialog(@NonNull Context context, Toothbrushing toothbrushing) {
        super((Context) context);
        setContentView(R.layout.random_reward_layout);

        // 확인 버튼
        confirmButton = findViewById(R.id.confirmButton);
        mContext = getContext();

        randomImageView = findViewById(R.id.random_image);
        randomNumView = findViewById(R.id.random_num_text);

        randomSeed = new Random().nextInt(3) + 1;
        randomNum = new Random().nextInt(3) + 1;
        int imageResource;

        switch (randomSeed) {
            case 1:
                imageResource = R.drawable.plant_image; // Replace with your plant image resource
                break;
            case 2:
                imageResource = R.drawable.flower_image; // Replace with your flower image resource
                break;
            case 3:
                imageResource = R.drawable.tree_image; // Replace with your tree image resource
                break;
            default:
                return;
        }

        randomImageView.setImageResource(imageResource); // random하게 보여줌
        randomNumView.setText(String.valueOf(randomNum));
        
        // 양치를 종료하면 random으로 받은 것을 DB에 저장
        class InsertRunnable implements  Runnable {

            @Override
            public void run() {
                try {
                    Log.v("Tag1", toothbrushing.getChildName());

                    // SeedDB에 random 보상 업데이트
                    // 1. num = 1 / plant
                    if(randomSeed == 1) {
                        SeedDB.getDatabase(getContext()).seedDao().updatePlantCount(toothbrushing.getChildName(), randomNum);
                    } else if(randomSeed == 2) {
                        SeedDB.getDatabase(getContext()).seedDao().updateFlowerCount(toothbrushing.getChildName(), randomNum);
                    } else {
                        SeedDB.getDatabase(getContext()).seedDao().updateTreeCount(toothbrushing.getChildName(), randomNum);
                    }
                } catch (Exception e) {
                    Log.e("Tag1", "Database operation failed", e);
                }
            }
        }

        // 확인 버튼 - 팝업 닫으면서 MainMenu로 돌아가기
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Tag1","button clicked");

                // Seed DB 업데이트
                InsertRunnable insertRunnable = new InsertRunnable();
                Thread t = new Thread(insertRunnable);
                t.start();
                Log.d("Tag1","t.start() below");

                // MainMenuActivity로 돌아가기
                Intent intent;//인텐트 선언
                Log.d("Tag1","1");
                intent = new Intent(mContext, MainMenuActivity.class); //
                Log.d("Tag1","2");
                intent.putExtra("childName", toothbrushing.getChildName()); //변수값 인텐트로 넘기기
                Log.d("Tag1","childname = " + toothbrushing.getChildName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                dismiss();
            }
        });

    }
}