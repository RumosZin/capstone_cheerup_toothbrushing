package com.lite.holistic_tracking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParentMenuMoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParentMenuMoreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView girlImageView;
    private ImageView boyImageView;
    private TextView childNameTextView;
    private TextView childBirthDateTextView;

    private String childName;
    private int seed;
    private String gender;
    private String birthDate;

    public ParentMenuMoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ParentMenuMoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParentMenuMoreFragment newInstance(String param1, String param2) {
        ParentMenuMoreFragment fragment = new ParentMenuMoreFragment();
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
        View view = inflater.inflate(R.layout.fragment_parent_menu_more, container, false);

        // 데이터 추출
        Bundle args = getArguments();
        if (args != null) {
            childName = args.getString("childName", "");
            birthDate = args.getString("birthDate", "");
            seed = args.getInt("seed", 100);
            gender = args.getString("gender", "");
            Log.v("check heyyyyyyyyyyyyyyyy", birthDate);
            Log.v("check heyyyyyyyyyyyyyyyy", String.valueOf(seed));
        }
        else {
            Log.v("check heyyyyyyyyyyyyyyyy", "none");
        }
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 백그라운드 스레드에서 DB 정보 검색하도록 설정

                // 데이터의 유무에 관계 없이 자녀 이름, 자녀 생년월일, 이미지 띄우기
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // 자녀의 이름 받아와서 띄우기
                        childNameTextView = view.findViewById(R.id.detail_childName);
                        childNameTextView.setText(childName);

                        // 자녀의 생년월일 받아와서 띄우기
                        childBirthDateTextView = view.findViewById(R.id.detail_birthDate);
                        childBirthDateTextView.setText(birthDate);

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