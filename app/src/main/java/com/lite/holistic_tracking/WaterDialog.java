package com.lite.holistic_tracking;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import android.os.Handler;
public class WaterDialog extends Dialog {
    //private Button confirmButton;

    private final Handler handler = new Handler();
    public WaterDialog(@NonNull Context context) {
        super((Context) context);
        setContentView(R.layout.activity_water_dialog);

        // Dismiss the dialog after 5 seconds
        // 입 헹구기 일단 5초
        // 나중에 길게 변경해야 할 듯!!
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 5000);

//        confirmButton = findViewById(R.id.confirmButton);
//
//        // 상점 페이지로 이동하는 리스너 설정
//        confirmButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
    }
}