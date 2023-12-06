package com.lite.holistic_tracking;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lite.holistic_tracking.Database.ChildDB;
import com.lite.holistic_tracking.Database.MorebrushingDB;
import com.lite.holistic_tracking.Database.ToothbrushingDB;
import com.lite.holistic_tracking.Entity.Morebrushing;
import com.lite.holistic_tracking.Entity.Toothbrushing;

import org.w3c.dom.Text;

public class RhythmDialog extends Dialog {

    private int perfect;
    private int great;
    private int good;
    private int miss;
    private int combo;

    private TextView perfectTextView;
    private TextView greatTextView;
    private TextView goodTextView;
    private TextView missTextView;
    private TextView comboTextView;
    private Button confirmButton;

    public RhythmDialog(@NonNull Context context, int perfect, int great, int good, int miss, int combo) {
        super((Context) context);
        setContentView(R.layout.rhythm_layout);

        this.perfect = perfect;
        this.great = great;
        this.good = good;
        this.miss = miss;
        this.combo = combo;
        
        // layout과 잇기
        perfectTextView = findViewById(R.id.perfectText);
        greatTextView = findViewById(R.id.greatText);
        goodTextView = findViewById(R.id.goodText);
        missTextView = findViewById(R.id.missText);
        comboTextView = findViewById(R.id.comboText);
        
        // 넘겨 받은 숫자로 세팅함
        perfectTextView.setText(perfect);
        greatTextView.setText(great);
        goodTextView.setText(good);
        missTextView.setText(miss);
        comboTextView.setText(combo);

        // 확인 버튼
        confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // 지금 dialog는 종료
            }
        });

    }
}