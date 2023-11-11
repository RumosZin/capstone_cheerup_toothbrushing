package com.lite.holistic_tracking;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class BrushingDialog extends Dialog {

    private TextView labelTextView;
    // private TextView valueTextView;
    private ImageView imageView;
    private ImageView openImageView;
    private TextView commentTextView; // 추가된 부분

    private TextView additionalCommentTextView;
    private static final Map<String, Integer> labelImageMap;
    private static final Map<String, String> labelCommentMap;
    private static final Map<String, String> labelDescriptionMap;

    private Button addButton;  // "양치 시간 추가" 버튼 추가
    private Button confirmButton;

    static {

        // image
        labelImageMap = new HashMap<>();
        labelImageMap.put("left_circular", R.drawable.left_circular_image); // 예시 이미지 리소스 ID
        labelImageMap.put("mid_circular", R.drawable.mid_circular_image);
        labelImageMap.put("right_circular", R.drawable.right_circular_image);
        labelImageMap.put("left_upper", R.drawable.left_upper_image);
        labelImageMap.put("left_lower", R.drawable.left_lower_image);
        labelImageMap.put("right_upper", R.drawable.right_upper_image);
        labelImageMap.put("right_lower", R.drawable.right_lower_image);
        labelImageMap.put("mid_vertical_upper", R.drawable.mid_vertical_upper_image);
        labelImageMap.put("mid_vertical_lower", R.drawable.mid_vertical_lower_image);

        // comment
        labelCommentMap = new HashMap<>(); // 추가된 부분
        labelCommentMap.put("left_circular"
                , "왼쪽 바깥을 닦는데 어려움이 있어요. \n칫솔을 정확히 쥐고 둥글게 닦도록 지도해주세요.");
        labelCommentMap.put("mid_circular"
                , "가운데 바깥을 닦는데 어려움이 있어요. \n칫솔을 정확히 쥐고 둥글게 닦도록 지도해주세요.");
        labelCommentMap.put("right_circular"
                , "오른쪽 바깥을 닦는데 어려움이 있어요. \n칫솔을 정확히 쥐고 둥글게 닦도록 지도해주세요.");
        labelCommentMap.put("left_upper"
                , "왼쪽 위를 잘 닦도록 지도해주세요. \n칫솔모가 위로 향하게 한 뒤 왼쪽 윗면과 안쪽면을 닦도록 지도해주세요.");
        labelCommentMap.put("left_lower"
                , "왼쪽 아래를 잘 닦도록 지도해주세요. \n칫솔모가 아래로 향하게 한 뒤 왼쪽 아랫면과 안쪽면을 닦도록 지도해주세요.");
        labelCommentMap.put("right_upper"
                , "오른쪽 위를 잘 닦도록 지도해주세요. \n칫솔모가 위로 향하게 한 뒤 오른쪽 윗면과 안쪽면을 닦도록 지도해주세요.");
        labelCommentMap.put("right_lower"
                , "오른쪽 아래를 잘 닦도록 지도해주세요. \n칫솔모가 아래로 향하게 한 뒤 오른쪽 아랫면과 안쪽면을 닦도록 지도해주세요.");
        labelCommentMap.put("mid_vertical_upper",
                "가운데 위의 안쪽을 꼼꼼히 닦도록 지도가 필요해요. \n칫솔을 세로로 향하게 한 뒤 잇몸부터 쓸어 내리듯이 닦도록 지도해주세요.");
        labelCommentMap.put("mid_vertical_lower"
                , "가운데 아래의 안쪽을 꼼꼼히 닦도록 지도가 필요해요. \n칫솔을 세로로 향하게 한 뒤 잇몸부터 쓸어 내리듯이 닦도록 지도해주세요.");

        labelDescriptionMap = new HashMap<>(); // 추가된 부분
        labelDescriptionMap.put("left_circular", "왼쪽 바깥");
        labelDescriptionMap.put("mid_circular", "가운데 바깥");
        labelDescriptionMap.put("right_circular", "오른쪽 바깥");
        labelDescriptionMap.put("left_upper", "왼쪽 위");
        labelDescriptionMap.put("left_lower", "왼쪽 아래");
        labelDescriptionMap.put("right_upper", "오른쪽 위");
        labelDescriptionMap.put("right_lower", "오른쪽 아래");
        labelDescriptionMap.put("mid_vertical_upper", "가운데 위 안쪽");
        labelDescriptionMap.put("mid_vertical_lower", "가운데 아래 안쪽");

    }

    public BrushingDialog(@NonNull Context context, String label, int value) {
        super((Context) context);
        setContentView(R.layout.popup_layout);

        labelTextView = findViewById(R.id.labelTextView);
        labelTextView.setText(label);

//        valueTextView = findViewById(R.id.valueTextView);
//        valueTextView.setText(String.valueOf(value));

        imageView = findViewById(R.id.imageView);
        openImageView = findViewById(R.id.open_mouth_image);
        commentTextView = findViewById(R.id.commentTextView);
        additionalCommentTextView =findViewById(R.id.additionalCommentTextView);
        addButton = findViewById(R.id.addButton);

        // 애니메이션을 적용하기 위한 코드
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // 확인 버튼을 누르면 pop-up 창이 닫힘
            }
        });

        // label에 대응하는 이미지 리소스 ID를 가져와서 설정
        if (labelImageMap.containsKey(label)) {
            imageView.setImageResource(labelImageMap.get(label));
            Log.v("!!!!!!1", label);
            if(label == "mid_circular" || label == "left_circular" || label == "right_circular") {
                openImageView.setVisibility(View.GONE);
                Log.v("!!!!!!!!!!!!!!!!", "!!!!!!!!!!!!");
            }
        } else {
            // label에 대응하는 이미지가 없는 경우에 대한 처리
            Log.w("BrushingDialog", "No image found for label: " + label);
        }

        if (labelCommentMap.containsKey(label)) {
            commentTextView.setText(labelCommentMap.get(label));
        } else {
            // label에 대응하는 코멘트가 없는 경우에 대한 처리
            Log.w("BrushingDialog", "No comment found for label: " + label);
        }

        if (labelDescriptionMap.containsKey(label)) {
            labelTextView.setText(labelDescriptionMap.get(label));
        } else {
            Log.w("BrushingDialog", "No description found for label: " + label);
        }

        // value가 14를 넘으면 "잘 닦고 있어요!" 텍스트를 띄우고 "양치 시간 추가" 버튼을 숨김
        if (value > 14) {
            commentTextView.setText("잘 닦고 있어요!");
            addButton.setVisibility(View.GONE);
            additionalCommentTextView.setVisibility(View.GONE);
        } else {
            commentTextView.setText(labelCommentMap.get(label)); // 기존 코멘트 표시
            addButton.setVisibility(View.VISIBLE);  // "양치 시간 추가" 버튼 표시
            additionalCommentTextView.setVisibility(View.VISIBLE);  // 추가 코멘트 표시
        }

    }

}
