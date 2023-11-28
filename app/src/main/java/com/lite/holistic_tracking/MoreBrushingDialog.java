package com.lite.holistic_tracking;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import android.os.Handler;
import android.os.Looper;

public class MoreBrushingDialog extends Dialog {
    public MoreBrushingDialog(@NonNull Context context) {
        super((Context) context);
        setContentView(R.layout.activity_more_brushing_dialog);
    }
}