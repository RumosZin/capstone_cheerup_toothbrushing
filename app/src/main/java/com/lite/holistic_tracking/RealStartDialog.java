package com.lite.holistic_tracking;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

public class RealStartDialog extends Dialog {
    private Button confirmButton;

    public RealStartDialog(@NonNull Context context) {
        super((Context) context);
        setContentView(R.layout.activity_realstart_dialog);

        confirmButton = findViewById(R.id.confirmButton);

        // 확인 버튼 누르면 이제 진짜 시작!!
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
    }
}