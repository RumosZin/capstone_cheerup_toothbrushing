package com.lite.holistic_tracking;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

public class WarningDialog extends Dialog {

    public WarningDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.activity_warning_dialog);

        // Find the "확인" (confirm) button
        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(v -> {
            dismiss();
        });
    }

}
