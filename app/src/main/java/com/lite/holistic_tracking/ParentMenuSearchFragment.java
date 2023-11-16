package com.lite.holistic_tracking;

import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.lite.holistic_tracking.Database.ToothbrushingDB;
import com.lite.holistic_tracking.Entity.Toothbrushing;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParentMenuSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParentMenuSearchFragment extends Fragment {

    private int threshold;
    private float avg_score;
    private TextView danger_area_text;
    private TextView danger_area_text_change;

    private ImageView girlImageView;
    private ImageView boyImageView;
    private TextView childNameTextView;
    private TextView scoreavgTextView;
    private String childName;
    private String gender;
    LineChart lineChart;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private BarChart barChart;

    public ParentMenuSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ParentMenuSearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParentMenuSearchFragment newInstance(String param1, String param2) {
        ParentMenuSearchFragment fragment = new ParentMenuSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parent_menu_search, container, false);
        lineChart = view.findViewById(R.id.line_chart);
        // 데이터 추출
        Bundle args = getArguments();
        if (args != null) {
            childName = args.getString("childName", "");
            //seed = args.getInt("seed", 100);
            gender = args.getString("gender", "");
        }
        else {
            Log.v("check heyyyyyyyyyyyyyyyy", "none");
        }
        // 데이터베이스에서 정보를 가져오는 작업을 백그라운드 스레드에서 실행
        new Thread(new Runnable() {
            @Override
            public void run() {
                //childName으로 toothbrushing table을 검색해서 정보를 return 함
                // gwak 자녀의 양치 정보가 list로 주어지면, 
                // score만 가져와서 막대 그래프 형식으로 보여줌
                List<Toothbrushing> toothbrushingList = ToothbrushingDB.getDatabase(getContext()).toothbrushingDao().getChildToothbrushingAll(childName);
                //Log.v("check the number", String.valueOf(toothbrushingList.size()));
                float total_score = 0;
                // 데이터가 존재하는 경우
                if(toothbrushingList.size() != 0) {
                    final ArrayList<Entry> entryArrayList = new ArrayList<>();
                    LineData lineData = new LineData(); // 차트에 담길 데이터
                    
                    for (int i = 0; i < toothbrushingList.size(); i++) {

                        // score를 Y축에 표시
                        float score = toothbrushingList.get(i).getScore();
                        entryArrayList.add(new BarEntry(i , score));
                        total_score += toothbrushingList.get(i).getScore(); // 전체 점수 계산 for 평균 점수

                    }
                    avg_score = total_score / toothbrushingList.size();

                    // UI 업데이트는 메인 스레드에서 진행
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // lineChart 초기화

                            LineDataSet lineDataSet = new LineDataSet(entryArrayList, "양치 점수");
                            lineDataSet.setDrawHorizontalHighlightIndicator(false);

                            // text 색 설정
                            int navyColor = Color.parseColor("#27489C");
                            //lineDataSet.setColor(navyColor);

                            // 바 색 설정
                            int lineColor = Color.parseColor("#FC7967");
                            lineDataSet.setColor(lineColor);
                            lineDataSet.setLineWidth(4f); // 선 두께 설정

                            // 각 포인트에 동그라미 표시
                            lineDataSet.setDrawCircles(true);

                            // 동그라미의 반지름 설정
                            lineDataSet.setCircleRadius(6f);
                            // 동그라미 내부를 채우도록 설정
                            //lineDataSet.setDrawFilled(true);
                            lineDataSet.setCircleColor(navyColor);

                            lineData.addDataSet(lineDataSet);
                            lineChart.setData(lineData);

                            // Y축 설정
                            YAxis yAxis = lineChart.getAxisLeft(); // 왼쪽 Y축을 사용하려면 getAxisLeft(), 오른쪽은 getAxisRight()
                            yAxis.setAxisMinimum(0f); // 최솟값 설정
                            yAxis.setAxisMaximum(100f); // 최댓값 설정
                            yAxis.setGranularity(5f); // Y축 간격 설정
                            lineChart.getAxisRight().setEnabled(false); // 오른쪽 Y축을 비활성화

                            // X축 설정
                            XAxis xAxis = lineChart.getXAxis();
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setGranularity(1f); // 레이블 간격
                            xAxis.setSpaceMin(0.5f); // 맨 앞 간격 설정
                            xAxis.setSpaceMax(0.5f); // 맨 뒤 간격 설정

                            // 라벨 설정
                            final String[] labels = new String[toothbrushingList.size()];

                            for (int i = 0; i < toothbrushingList.size(); i++) {
                                //labels[i] = toothbrushingList.get(i).getDate() + "\n" + toothbrushingList.get(i).getTime();
                                labels[i] = "";
                            }
                            if(toothbrushingList.size() >= 2) {
                                labels[0] = "new 양치";
                                labels[toothbrushingList.size() - 1] = "old 양치";
                            }
                            else if(toothbrushingList.size() == 1){
                                labels[0] = "가장 최근의 양치";
                            }

                            xAxis.setValueFormatter(new ValueFormatter() {
                                @Override
                                public String getFormattedValue(float value) {
                                    int index = (int) value;
                                    if (index >= 0 && index < labels.length) {
                                        return labels[index];
                                    }
                                    return "";
                                }
                            });


                            // 값의 글씨 크기 설정
                            lineDataSet.setValueTextSize(20f); // 원하는 크기로 조절

                            // 소수점 아래를 절삭하고 정수로 나타내기 위한 ValueFormatter 설정
                            lineDataSet.setValueFormatter(new ValueFormatter() {
                                @Override
                                public String getFormattedValue(float value) {
                                    return String.valueOf((int) value); // 소수점 아래를 절삭하고 정수로 변환
                                }
                            });

                            // 범례 숨기기
                            Legend legend = lineChart.getLegend();
                            legend.setEnabled(false);

                            // Description Label 숨기기
                            Description description = new Description();
                            description.setEnabled(false);
                            lineChart.setDescription(description);

                            // 더블 탭으로 인한 확대 비활성화
                            lineChart.setDoubleTapToZoomEnabled(false);
                            lineChart.setPinchZoom(false); // 두손가락으로 줌 설정

//                          // 한번에 볼 수 있는 화면 조정
                            lineChart.setVisibleXRangeMaximum(3);

                            // 차트 업데이트
                            lineChart.setVisibility(View.VISIBLE);
                            lineChart.invalidate();
                            
                            // 평균 점수 설정
                            scoreavgTextView = view.findViewById(R.id.detail_avg_score);
                            scoreavgTextView.setText("평균 " + String.valueOf(avg_score) + "점");

                            // BarChart에 클릭 리스너 추가
                            lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                @Override
                                public void onValueSelected(Entry e, Highlight h) {
                                    // 막대를 클릭했을 때의 동작을 여기에 추가
                                    float selectedScore = e.getY();
                                    int selectedIndex = (int) e.getX();
                                    Toothbrushing toothbrushing = toothbrushingList.get(selectedIndex);

                                    // mid_circular 값이 N 이상이면 보이도록 설정 - 할 예정!
                                    ImageView midCircularImageView = view.findViewById(R.id.mid_circular_image);
                                    if (toothbrushing.getMid_circular() < threshold) {
                                        midCircularImageView.setVisibility(View.VISIBLE);
                                    } else {
                                        midCircularImageView.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onNothingSelected() {
                                    // 아무것도 선택되지 않았을 때의 동작을 여기에 추가
                                }
                            });
                            
                        }
                    });
                } else { // 데이터가 없는 경우에,,,
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // showNoDataAlert();
                            hideNoDataUI();
                        }
                    });
                }

                // 데이터에 관계 없이 자녀 이름, 성별에 따른 이미지는 띄워야 함
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // 자녀의 이름 받아와서 띄우기
                        childNameTextView = view.findViewById(R.id.detail_childName);
                        childNameTextView.setText(childName);

                        // gender에 따라서 girl/boy image 적절히 설정
                        girlImageView = view.findViewById(R.id.detail_girl_image);
                        boyImageView = view.findViewById(R.id.detail_boy_image);
                        if ("여자".equals(gender)) {
                            //Log.v("check boy image1", gender);
                            girlImageView.setVisibility(View.VISIBLE);
                            boyImageView.setVisibility(View.GONE);
                        } else {
                            //Log.v("check boy image", gender);
                            girlImageView.setVisibility(View.GONE);
                            boyImageView.setVisibility(View.VISIBLE);
                        }

                        // 이미지 겹치게 하는 것 설정
                        // mid_circular 값이 안보이도록 설정
                        ImageView midCircularImageView = view.findViewById(R.id.mid_circular_image);
                        midCircularImageView.setVisibility(View.GONE);

                        // left_circular 값이 안보이도록 설정
                        ImageView leftCircularImageView = view.findViewById(R.id.left_circular_image);
                        leftCircularImageView.setVisibility(View.GONE);

                        ImageView rightCircularImageView = view.findViewById(R.id.right_circular_image);
                        rightCircularImageView.setVisibility(View.GONE);

                        ImageView leftUpperImageView = view.findViewById(R.id.left_upper_image);
                        leftUpperImageView.setVisibility(View.GONE);

                        ImageView leftLowerImageView = view.findViewById(R.id.left_lower_image);
                        leftLowerImageView.setVisibility(View.GONE);

                        ImageView rightUpperImageView = view.findViewById(R.id.right_upper_image);
                        rightUpperImageView.setVisibility(View.GONE);

                        ImageView rightLowerImageView = view.findViewById(R.id.right_lower_image);
                        rightLowerImageView.setVisibility(View.GONE);

                        ImageView midverticalLowerImageView = view.findViewById(R.id.mid_vertical_lower_image);
                        midverticalLowerImageView.setVisibility(View.GONE);

                        ImageView midverticalUpperImageView = view.findViewById(R.id.mid_vertical_upper_image);
                        midverticalUpperImageView.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
        return view;
    }

    // 데이터가 없는 경우에 대한 알림을 표시하는 메서드
    private void hideNoDataUI() {
        // 해당 UI를 찾아서 숨김 처리
        View rootView = getView();
        if (rootView != null) {
            rootView.findViewById(R.id.open_mouth_all).setVisibility(View.GONE);
            rootView.findViewById(R.id.smile_mouth_all).setVisibility(View.GONE);
            rootView.findViewById(R.id.horizontal_scroll_view).setVisibility(View.GONE);

            danger_area_text = rootView.findViewById(R.id.danger_area_text);
            danger_area_text_change = rootView.findViewById(R.id.danger_area_text_change);
            danger_area_text.setText("아직 양치 정보가 없어요! 즐겁게 양치를 해볼까요?");
            danger_area_text_change.setText("아직 양치 정보가 없어요! 즐겁게 양치를 해볼까요?");

            lineChart.setVisibility(View.GONE); // 양치 정보가 없으니까 line chart 감추기
        }
    }


}