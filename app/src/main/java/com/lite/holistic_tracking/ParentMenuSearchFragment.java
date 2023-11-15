package com.lite.holistic_tracking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.lite.holistic_tracking.Database.ToothbrushingDB;
import com.lite.holistic_tracking.Entity.Toothbrushing;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParentMenuSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParentMenuSearchFragment extends Fragment {

    private ImageView girlImageView;
    private ImageView boyImageView;
    private TextView childNameTextView;
    private TextView scoreavgTextView;
    private String childName;
    private String gender;

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
                Log.v("check the number", String.valueOf(toothbrushingList.size()));
                // 데이터가 존재하는 경우
                if(toothbrushingList.size() != 0) {
                    final ArrayList<BarEntry> barEntryArrayList = new ArrayList<>();
                    BarData barData = new BarData(); // 차트에 담길 데이터
                    
                    for (int i = 0; i < toothbrushingList.size(); i++) {
                        // 예시: 날짜 및 시간을 이용해서 X축에 표시
                        String datetime = toothbrushingList.get(i).getDate() + " " + toothbrushingList.get(i).getTime();
                        // score를 Y축에 표시
                        float score = toothbrushingList.get(i).getScore();

                        barEntryArrayList.add(new BarEntry(i, score));
                    }
                    //BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "bardataset");

                    // UI 업데이트는 메인 스레드에서 진행되어야 합니다.
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // BarChart 초기화
                            barChart = view.findViewById(R.id.detail_barchart);
                            BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "양치 점수");
                            barData.addDataSet(barDataSet);
                            barChart.setData(barData);

                            // X축 설정
                            XAxis xAxis = barChart.getXAxis();
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setGranularity(1f); // 레이블 간격

                            // 차트 업데이트
                            barChart.invalidate();
                        }
                    });

                }
                // 데이터가 존재하지 않는 경우
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


                    }
                });
            }
        }).start();
        return view;
    }
}