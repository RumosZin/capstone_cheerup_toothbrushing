package com.lite.holistic_tracking;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 백그라운드 스레드에서 데이터베이스에서 정보를 가져옴
                List<Toothbrushing> toothbrushingList = ToothbrushingDB.getDatabase(getContext()).toothbrushingDao().getAll();

                // UI 업데이트를 메인 스레드에서 처리
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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

                            RadarDataSet radarDataSet = new RadarDataSet(dataVals, "toothbrushing");
                            radarDataSet.setColor(Color.RED);
                            radarDataSet.setLineWidth(2f);
                            radarDataSet.setValueTextColor(Color.RED);
                            radarDataSet.setValueTextSize(14f);

                            RadarData radarData = new RadarData();
                            radarData.addDataSet(radarDataSet);

                            String[] labels = {"left_circular", "mid_circular", "right_circular", "left_upper"
                            , "left_upper", "left_lower", "right_upper", "right_lower"
                                    , "mid_vertical_upper", "mid_vertical_lower"};

                            XAxis xAxis = radarChart.getXAxis();
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

                            radarChart.setData(radarData);

                        } else {

                        }
                    }
                });
            }
        }).start();
        return view;
    }
}