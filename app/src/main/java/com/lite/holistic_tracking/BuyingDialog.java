package com.lite.holistic_tracking;

import android.content.Context;
import android.app.Dialog;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BuyingDialog extends Dialog {

    public BuyingDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.activity_buying_dialog);

    }
}
