package com.lite.holistic_tracking;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
public class WaterDialog extends Dialog {
    private Button confirmButton;
    public WaterDialog(@NonNull Context context) {
        super((Context) context);
        setContentView(R.layout.activity_water_dialog);

        confirmButton = findViewById(R.id.confirmButton);

        // 확인 버튼 누르면 dismiss
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}