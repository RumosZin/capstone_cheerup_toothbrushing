package com.lite.holistic_tracking;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;


public class SpitTimeDialog extends Dialog {
    public SpitTimeDialog(@NonNull Context context) {
        super((Context) context);
        setContentView(R.layout.activity_bubble_dialog);
    }
}