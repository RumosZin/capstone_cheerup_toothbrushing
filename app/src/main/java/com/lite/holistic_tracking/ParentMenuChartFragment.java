package com.lite.holistic_tracking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lite.holistic_tracking.Database.ToothbrushingDB;
import com.lite.holistic_tracking.Entity.Toothbrushing;

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

        // TotalBrushing 정보를 가져와서 보여줄 TextView 등을 찾음
        TextView childNameTextView = view.findViewById(R.id.childNameTextView);
        TextView leftCircularTextView = view.findViewById(R.id.leftCircularTextView);
        // ... (다른 필드에 대한 TextView들)

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
                            Toothbrushing toothbrushing = toothbrushingList.get(0); // 첫 번째 Toothbrushing 객체를 가져옴
                            childNameTextView.setText("Child Name: " + toothbrushing.getChildName());
                            leftCircularTextView.setText("Left Circular: " + toothbrushing.getLeft_circular());
                            // ... (다른 필드에 대한 설정)
                        } else {
                            // 데이터가 없을 때의 처리
                            childNameTextView.setText("No data available");
                            // ... (다른 필드에 대한 처리)
                        }
                    }
                });
            }
        }).start();
        return view;
    }
}