package com.lite.holistic_tracking;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.lite.holistic_tracking.Database.BuyingDB;
import com.lite.holistic_tracking.Database.BuyingDao;
import com.lite.holistic_tracking.Database.ChildDB;
import com.lite.holistic_tracking.Entity.Animal;
import com.lite.holistic_tracking.Entity.Buying;
import com.lite.holistic_tracking.Entity.Child;

import java.util.HashMap;
import java.util.Map;

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
