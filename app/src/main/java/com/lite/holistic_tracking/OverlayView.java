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
    private float[][] points;
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
    public void setPoints(float[][] points){
        this.points = points;
        invalidate();
        Log.d("test", "invalidate");

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (landmarks == null) {
            Log.d("test", "not working");
            return;
        }
        if (points == null) {
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

        if(points.length > 1) {
            float[] point1 = points[0];
            float[] point2 = points[1];
            float[] point3 = points[2];

            canvas.drawCircle(point1[0]*getWidth(), point1[1]*getHeight(), 10, paint);
            canvas.drawCircle(point2[0]*getWidth(), point2[1]*getHeight(), 10, paint);

            canvas.drawCircle(point3[0]*getWidth(), point3[1]*getHeight(), 10, paint);


            Paint paintLine = new Paint();
            paintLine.setColor(Color.BLUE);
            paintLine.setStrokeWidth(15f);

            canvas.drawLine(point1[0]*getWidth(), point1[1]*getHeight(), point2[0]*getWidth(), point2[1]*getHeight(), paintLine);
//            Log.d("test", "brush result in overlay : " + String.valueOf(point1[0]) + ", " + String.valueOf(point1[0]));
//            Log.d("test", "brush result in overlay : " + String.valueOf(point2[0]) + ", " + String.valueOf(point2[0]));
        }
    }
}
