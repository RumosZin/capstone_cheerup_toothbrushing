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

    int score;

    private TextView seedView;
    private Button confirmButton;
    private Toothbrushing toothbrushing;
//    private Button shopButton;
//    private Button endButton;

    public GetSeedDialog(@NonNull Context context, Toothbrushing toothbrushing) {
        super((Context) context);
        setContentView(R.layout.getseed_layout);
        this.toothbrushing = toothbrushing;

        // toothbrushing에서 score 가져와서 씨앗 표시
        score = toothbrushing.getScore() / 10;
        seedView = findViewById(R.id.seedText);
        seedView.setText(String.valueOf(score));

        // 확인 버튼
        confirmButton = findViewById(R.id.confirmButton);

        class InsertRunnable implements  Runnable {

            @Override
            public void run() {
                try {
                    Log.v("test insert", toothbrushing.getChildName());

                    // Toothbrushing DB에 저장
                    ToothbrushingDB toothbrushingDB = ToothbrushingDB.getDatabase(getContext());
                    toothbrushingDB.toothbrushingDao().insert(toothbrushing);

                    // Child DB 업데이트 - 잠시 뺌
                    ChildDB childDB = ChildDB.getInstance(getContext());
                    childDB.childDao().updateChildSeed(toothbrushing.getChildName(), score);

                } catch (Exception e) {
                    Log.e("DB Error", "Database operation failed", e);
                }
            }
        }

        // 확인 버튼 - 팝업 닫기
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Child DB 업데이트
                InsertRunnable insertRunnable = new InsertRunnable();
                Thread t = new Thread(insertRunnable);
                t.start();

//                RandomRewardDialog randomRewardDialog = new RandomRewardDialog(getContext(), toothbrushing);
//                randomRewardDialog.show();
                dismiss(); // 지금 dialog는 종료

//                Intent intent = new Intent(getContext(), RandomRewardActivity.class);
//                getContext().startActivity(intent);
//                // 다이얼로그 종료
//                dismiss();
            }
        });

    }
}