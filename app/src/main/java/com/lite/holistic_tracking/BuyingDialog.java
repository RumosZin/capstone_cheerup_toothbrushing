package com.lite.holistic_tracking;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.lite.holistic_tracking.Database.BuyingDB;
import com.lite.holistic_tracking.Database.BuyingDao;
import com.lite.holistic_tracking.Database.ChildDB;
import com.lite.holistic_tracking.Entity.Animal;
import com.lite.holistic_tracking.Entity.Buying;
import com.lite.holistic_tracking.Entity.Child;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BuyingDialog extends Dialog {

    private Child child;
    private int afterSeed;

    private Animal clickedAnimal;

    private TextView animalTextView;
    private ImageView animalImageView;
    private TextView commentTextView;
    private static final Map<String, Integer> labelImageMap;

    static {
        // image
        labelImageMap = new HashMap<>();
        labelImageMap.put("고양이", R.drawable.cat_image); // 예시 이미지 리소스 ID
        labelImageMap.put("강아지", R.drawable.dog_image);
        labelImageMap.put("여우", R.drawable.fox_image);
        labelImageMap.put("토끼", R.drawable.rabbit_image);
        labelImageMap.put("사자", R.drawable.lion_image);
        labelImageMap.put("돼지", R.drawable.pig_image);
        labelImageMap.put("양", R.drawable.sheep_image);
    }

    public BuyingDialog(@NonNull Context context, Child child, Animal clickedAnimal) {
        super(context);
        setContentView(R.layout.activity_buying_dialog);

        this.clickedAnimal = clickedAnimal;
        this.child = child;

        commentTextView = findViewById(R.id.commentTextView);

        class InsertRunnable implements Runnable{

            @Override
            public void run() {

                // 구매 DB에 추가
                Buying buying = new Buying();
                buying.setChildName(child.getChildName());
                buying.setAnimalName(clickedAnimal.getName());
                BuyingDB.getInstance(getContext()).buyingDao().insert(buying);

                // child DB에서 씨앗 가격만큼 빼기
                int minus_seed = -1 * clickedAnimal.getRequiredSeed();
                afterSeed = child.getSeed() + minus_seed;

                Log.v("rrrrrrrrrrrrrrrrrrr", String.valueOf(afterSeed));
                ChildDB.getInstance(getContext()).childDao().updateChildSeed(child.getChildName(), minus_seed);

                // 구매 완료 메시지를 토스트로 띄우기
                ((Activity) context).runOnUiThread(() -> {
                    Toast.makeText(context, "<" + clickedAnimal.getName() + "> 구매 완료!", Toast.LENGTH_SHORT).show();
                });

                Intent intent = new Intent(context.getApplicationContext(), ShopActivity.class);
                intent.putExtra("childName", child.getChildName()); // "childName"이라는 키로 ChildName 전달
                intent.putExtra("birthDate", child.getBirthDate());
                intent.putExtra("gender", child.getGender());
                intent.putExtra("seed", afterSeed); // "seed"라는 키로 seed 전달
                Log.v("lllllllllllllllllllllll", String.valueOf(afterSeed));

                context.startActivity(intent);
                dismiss();
            }
        }

        Button buyButton = findViewById(R.id.buyButton);
        buyButton.setOnClickListener(v -> {
            // When the "구매하기" (buy) button is clicked
            // Insert a new entry into BuyingDB with child's name and animal's name
            //insertBuyingData(clickedAnimal.getName());
            dismiss();
            InsertRunnable insertRunnable = new InsertRunnable();
            Thread t = new Thread(insertRunnable);
            t.start();


        });

        // Find the "확인" (confirm) button
        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(v -> {
            dismiss();
        });

        // Check if the child's seed is sufficient to buy the animal
        int requiredSeed = clickedAnimal.getRequiredSeed() - child.getSeed();
        int leftSeed = child.getSeed() - clickedAnimal.getRequiredSeed();
        if (requiredSeed <= 0) { // 자녀의 씨앗이 충분할 때
            // Child's seed is sufficient, show the "구매하기" (buy) button
            findViewById(R.id.buyButton).setVisibility(View.VISIBLE);
            commentTextView.setText("씨앗이 충분해서 살 수 있어!\n" + "사고 나면 " +
                     leftSeed + "개의 씨앗이 남아.");
        } else { // 자녀의 씨앗이 모자랄 때
            // Child's seed is not sufficient, hide the "구매하기" (buy) button
            findViewById(R.id.buyButton).setVisibility(View.GONE);
            commentTextView.setText("씨앗이 모자라서 아직 살 수 없어!\n"
            + requiredSeed + "개의 씨앗이 더 필요해.");
        }

        animalTextView = findViewById(R.id.labelTextView);
        animalTextView.setText(clickedAnimal.getName());

        animalImageView = findViewById(R.id.detail_animal_image);
        if(labelImageMap.containsKey(clickedAnimal.getName())) {
            animalImageView.setImageResource(labelImageMap.get(clickedAnimal.getName()));
        }
    }

}



    private void startAnimation() {
        Log.d("combo2", "startAnimation() called");
        if (!stopAnimation) {
//            checkHeights = new ArrayList<>(); // 이거 upper 구분임
            Arrays.fill(action_seq, 0);

            for(int i = 0; i < (howManyBeatsPerArea/2); i++) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        makeComboFast();
                    }
                }, setBPM() * (i*2));
            }
            addMedalImage((int)totalScore);
            setToothImage();     // set tooth image and ball location
            setBallAnimation(); // set the ball animation according to tooth image
            toothIndex = (toothIndex + 1) % (toothImages.length+1); // loop, +1 = 양치 뱉기화면 시간추가
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startAnimation(); // loop
                }
            }, setBPM() * howManyBeatsPerArea);
        } else {
            Log.d("MyTag", "startAnimation() else called");
            toothImageView.setVisibility(View.INVISIBLE);
            ballImageView.setVisibility(View.INVISIBLE);
            circularballImageView.setVisibility(View.INVISIBLE);
        }
    }