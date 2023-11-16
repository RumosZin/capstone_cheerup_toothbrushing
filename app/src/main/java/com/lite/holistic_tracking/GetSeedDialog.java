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
import com.lite.holistic_tracking.Database.ToothbrushingDB;
import com.lite.holistic_tracking.Entity.Toothbrushing;

public class GetSeedDialog extends Dialog {

    private TextView seedView;
    private Button shopButton;
    private Button endButton;

    public GetSeedDialog(@NonNull Context context, Toothbrushing toothbrushing) {
        super((Context) context);
        setContentView(R.layout.getseed_layout);

        // toothbrushing에서 score 가져와서 씨앗 표시
        int score = toothbrushing.getScore() / 10;
        seedView = findViewById(R.id.seedText);
        seedView.setText(String.valueOf(score));

        // 버튼 처리
        shopButton = findViewById(R.id.shopButton);
        endButton = findViewById(R.id.endButton);

        // 상점 페이지로 이동하는 리스너 설정
        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 상점 페이지로 이동
                Intent intent = new Intent(getContext(), ShopActivity.class);
                getContext().startActivity(intent);

                // 다이얼로그 종료
                dismiss();
            }
        });

        class InsertRunnable implements  Runnable {

            @Override
            public void run() {
                try {
                    Log.v("test insert", toothbrushing.getChildName());

                    // Toothbrushing DB에 저장
                    ToothbrushingDB toothbrushingDB = ToothbrushingDB.getDatabase(getContext());
                    toothbrushingDB.toothbrushingDao().insert(toothbrushing);

                    // Child DB 업데이트
                    ChildDB childDB = ChildDB.getInstance(getContext());
                    childDB.childDao().updateChildSeed(toothbrushing.getChildName(), (int) (toothbrushing.getScore() / 10));

                } catch (Exception e) {
                    Log.e("DB Error", "Database operation failed", e);
                }
            }
        }

        // endButton - 팝업을 닫는 리스너 설정
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Child DB 업데이트
                int newSeed = score; // 업데이트할 씨앗 정보
                InsertRunnable insertRunnable = new InsertRunnable();
                Thread t = new Thread(insertRunnable);
                t.start();

                // 다이얼로그 종료
                dismiss();
            }
        });

    }
}