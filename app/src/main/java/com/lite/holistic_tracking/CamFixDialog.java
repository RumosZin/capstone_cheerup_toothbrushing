package com.lite.holistic_tracking;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

public class CamFixDialog extends Dialog {
    private Button confirmButton;

    public CamFixDialog(@NonNull Context context) {
        super((Context) context);
        setContentView(R.layout.activity_warning_dialog);

        confirmButton = findViewById(R.id.confirmButton);

        // TubeDialog 열기
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}