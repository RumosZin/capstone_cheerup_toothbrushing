package com.lite.holistic_tracking;

import android.content.Context;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

public class GrabDialog extends Dialog {

    public GrabDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.activity_grab_dialog);

        // Find the "확인" (confirm) button
        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(v -> {
            dismiss();
        });
    }

}
