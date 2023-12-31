package com.lite.holistic_tracking;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import android.os.Handler;

public class TubeDialog extends Dialog {
    private Button confirmButton;

    public TubeDialog(@NonNull Context context) {
        super((Context) context);
        setContentView(R.layout.activity_tube_dialog);

        confirmButton = findViewById(R.id.confirmButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}