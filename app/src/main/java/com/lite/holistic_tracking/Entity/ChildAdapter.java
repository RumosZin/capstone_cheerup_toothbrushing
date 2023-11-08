package com.lite.holistic_tracking.Entity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lite.holistic_tracking.Database.ChildDB;
import com.lite.holistic_tracking.MainMenuActivity;
import com.lite.holistic_tracking.R;

import java.util.ArrayList;
import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {
    private List<Child> childList;
    private String name = "";
    //private onListItemSelectedInterface mListener;
    Context mContext;
    onChildClickListener listener;


    public ChildAdapter(List<Child> childList, Context context) {

        this.childList = childList;
        this.mContext = context;
        //notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Context context = parent.getContext();
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_list_item, parent, false);
        //View view = inflater.inflate(R.layout.child_list_item, parent, false);
        ChildAdapter.ViewHolder vh = new ChildAdapter.ViewHolder(view);
        Log.v("test", "viewHolder가 문제냐...");

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Child child = childList.get(position);
        //childDB = ChildDB.getInstance(context);
        //Log.v("test in ChildAdapter", String.valueOf(child.childName));

        // 화면에 데이터 표시하기
        holder.childName.setText(child.getChildName());
        holder.birthDate.setText(child.getBirthDate());
        holder.gender.setText(child.getGender());
        holder.seed.setText(String.valueOf(child.getSeed()));

        //리스트 클릭 이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String childName = holder.childName.getText().toString(); //holder로 가져온 값을 변수에 넣기

                Intent intent;//인텐트 선언
                intent = new Intent(mContext, MainMenuActivity.class); //
                intent.putExtra("childName", childName); //변수값 인텐트로 넘기기
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent); //액티비티 열기

            }

        });


    }

    @Override
    public int getItemCount() {
        return childList.size();
    }

    public void setOnItemClicklistener(onChildClickListener listener) {
        this.listener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView childName;
        TextView birthDate;
        TextView gender;
        TextView seed;
        // RecyclerView recyclerView;

        public ViewHolder(@NonNull View view) {
            super(view);

            childName = view.findViewById(R.id.childNameEditText);
            birthDate = view.findViewById(R.id.birthdateEditText);
            gender = view.findViewById(R.id.genderEditText);
            seed = view.findViewById(R.id.seed);
        }

    }

}
