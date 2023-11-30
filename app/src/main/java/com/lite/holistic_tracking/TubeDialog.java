package com.lite.holistic_tracking;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import android.os.Handler;

public class TubeDialog extends Dialog {
    private final Handler handler = new Handler();

    public TubeDialog(@NonNull Context context) {
        super((Context) context);
        setContentView(R.layout.activity_tube_dialog);

        // Dismiss the dialog after 5 seconds
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 5000);


    }
}