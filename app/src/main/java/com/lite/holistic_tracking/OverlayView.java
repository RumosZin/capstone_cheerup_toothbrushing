package com.lite.holistic_tracking;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmark;

import java.util.ArrayList;
import java.util.List;

public class OverlayView extends View {
    private List<NormalizedLandmark> landmarks = new ArrayList<>();
    private ArrayList<float[]> points = new ArrayList<>();
    public OverlayView(Context context) {
        super(context);
        // 투명 배경 설정
        setBackgroundColor(Color.TRANSPARENT);
    }

    public void setLandmarks(List<NormalizedLandmark> landmarks) {
        this.landmarks = landmarks;
        invalidate(); // 뷰를 다시 그리도록 요청
        Log.d("test", "invalidate");

    }
    public void setPoints(ArrayList<float[]> points){
        this.points = points;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (landmarks == null) {
            Log.d("test", "not working");
            return;
        }
        Log.d("test", "working");
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(180);

//        for (NormalizedLandmark landmark : landmarks) {
//            // 랜드마크의 위치를 화면 좌표로 변환
//            float x = landmark.getX() * getWidth();
//            float y = landmark.getY() * getHeight();
////            Log.d("test", String.valueOf(x));
////            Log.d("test", String.valueOf(y));
//            canvas.drawCircle(x, y, 10, paint); // 랜드마크 그리기
//        }
        if(!(points.isEmpty())) {
            for (float[] point : points) {
                float x = point[0];
                float y = point[1];
                canvas.drawCircle(x, y, 10, paint);
            }

            Paint paintLine = new Paint();
            paintLine.setColor(Color.BLUE);
            paintLine.setStrokeWidth(5f);

            canvas.drawLine(points.get(0)[0], points.get(0)[1], points.get(1)[0], points.get(1)[1], paintLine);
        }
    }
}
