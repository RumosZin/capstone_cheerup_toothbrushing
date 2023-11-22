package com.lite.holistic_tracking;

import android.content.Context;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.lite.holistic_tracking.Entity.Animal;

import java.util.HashMap;
import java.util.Map;

public class BuyingDialog extends Dialog {

    private int childSeed;
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
    }

    public BuyingDialog(@NonNull Context context, int childSeed, Animal clickedAnimal) {
        super(context);
        setContentView(R.layout.activity_buying_dialog);

        this.childSeed = childSeed;
        this.clickedAnimal = clickedAnimal;

        commentTextView = findViewById(R.id.commentTextView);

        // Find the "확인" (confirm) button
        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(v -> {
            dismiss();
        });

        // Check if the child's seed is sufficient to buy the animal
        int requiredSeed = clickedAnimal.getRequiredSeed() - childSeed;
        int leftSeed = childSeed - clickedAnimal.getRequiredSeed();
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
