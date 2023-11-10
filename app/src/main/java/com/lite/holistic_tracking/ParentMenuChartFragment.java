package com.lite.holistic_tracking;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.lite.holistic_tracking.Database.ToothbrushingDB;
import com.lite.holistic_tracking.Entity.Toothbrushing;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParentMenuChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParentMenuChartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ParentMenuChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ParentMenuChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParentMenuChartFragment newInstance(String param1, String param2) {
        ParentMenuChartFragment fragment = new ParentMenuChartFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parent_menu_chart, container, false);
        RadarChart radarChart = view.findViewById(R.id.mapsearchdetail_radar_chart);
        // 라벨 및 범례 숨기기
        radarChart.getDescription().setEnabled(false);  // 라벨 숨기기
        Legend legend = radarChart.getLegend();
        legend.setEnabled(false);  // 범례 숨기기

        // 데이터베이스에서 정보를 가져오는 작업을 백그라운드 스레드에서 실행
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 백그라운드 스레드에서 데이터베이스에서 정보를 가져옴
                List<Toothbrushing> toothbrushingList = ToothbrushingDB.getDatabase(getContext()).toothbrushingDao().getAll();
                // 데이터가 존재하는 경우
                if (toothbrushingList.size() != 0) {
                    Toothbrushing toothbrushing = toothbrushingList.get(1); // 첫 번째 Toothbrushing 객체를 가져옴
                    ArrayList<RadarEntry> dataVals = new ArrayList<>();
                    dataVals.add(new RadarEntry(toothbrushing.getLeft_circular()));
                    dataVals.add(new RadarEntry(toothbrushing.getMid_circular()));
                    dataVals.add(new RadarEntry(toothbrushing.getRight_circular()));
                    dataVals.add(new RadarEntry(toothbrushing.getLeft_upper()));
                    dataVals.add(new RadarEntry(toothbrushing.getLeft_lower()));
                    dataVals.add(new RadarEntry(toothbrushing.getRight_upper()));
                    dataVals.add(new RadarEntry(toothbrushing.getRight_lower()));
                    dataVals.add(new RadarEntry(toothbrushing.getMid_vertical_upper()));
                    dataVals.add(new RadarEntry(toothbrushing.getMid_vertical_lower()));

                    // UI 업데이트를 메인 스레드에서 처리
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RadarDataSet radarDataSet = new RadarDataSet(dataVals, "toothbrushing");
                            radarDataSet.setColor(Color.BLUE);
                            radarDataSet.setLineWidth(2f);
                            radarDataSet.setValueTextColor(Color.RED);
                            radarDataSet.setValueTextSize(14f);
                            //radarDataSet.setDrawHighlightCircleEnabled(true);

                            // Set to draw highlight circles for all data points
                            radarDataSet.setDrawHighlightCircleEnabled(true);
                            radarDataSet.setHighlightCircleStrokeColor(Color.YELLOW); // Customize circle stroke color if needed
                            radarDataSet.setHighlightCircleStrokeAlpha(255); // Set alpha to fully visible

                            // 값(데이터)을 표시하지 않도록 설정
                            radarDataSet.setDrawValues(false);

                            RadarData radarData = new RadarData();
                            radarData.addDataSet(radarDataSet);

                            String[] labels = {"left_circular", "mid_circular", "right_circular"
                                    , "left_upper", "left_lower", "right_upper", "right_lower"
                                    , "mid_vertical_upper", "mid_vertical_lower"};

                            XAxis xAxis = radarChart.getXAxis();
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

                            radarChart.setData(radarData);

                            // 중앙값을 0으로 설정
                            radarChart.getYAxis().setAxisMinimum(0);

                            // 차트가 화면에 나타날 수 있도록 설정
                            radarChart.setVisibility(View.VISIBLE);
                            radarChart.invalidate(); // 차트 갱신

                            // RadarChart에 이벤트 리스너 추가
                            radarChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                @Override
                                public void onValueSelected(Entry e, Highlight h) {
                                    // 선택한 값(e)을 이용하여 원하는 동작 수행
                                    // 이 부분에서 선택한 값(e)을 가지고 아래에 값을 표시하는 등의 동작을 수행할 수 있습니다.
                                    int index = (int) h.getX(); // 선택한 피자 조각(영역)의 인덱스

                                    // 해당 인덱스에 대한 라벨 가져오기

                                    //IRadarDataSet dataSet = radarChart.getData().getDataSetByIndex(index); // 여기서 0은 dataSet의 index
                                    String label = labels[index];
                                    int value = (int) e.getY();

                                    Log.v("test", "Selected: " + label + ", Value: " + value);
                                    BrushingDialog brushingDialog = new BrushingDialog(getContext(), label, value);
                                    brushingDialog.show();

                                }

                                @Override
                                public void onNothingSelected() {
                                    // 아무 값도 선택되지 않았을 때의 동작 정의
                                }
                            });

                        }
                    });
                }
            }
        }).start();
        return view;
    }
}