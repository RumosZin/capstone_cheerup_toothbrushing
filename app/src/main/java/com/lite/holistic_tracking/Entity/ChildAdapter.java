package com.lite.holistic_tracking.Entity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lite.holistic_tracking.Database.ChildDB;
import com.lite.holistic_tracking.R;

import java.util.ArrayList;
import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {
    private List<Child> childList;
    private String name = "";

    public ChildAdapter(List<Child> childList) {

        this.childList = childList;
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
        Log.v("test in ChildAdapter", String.valueOf(child.childName));
        holder.childName.setText(child.getChildName());
        holder.birthDate.setText(child.getBirthDate());
        holder.gender.setText(child.getGender());
        holder.seed.setText(String.valueOf(child.getSeed()));
        name = child.getChildName();

//        // 여기에서 각 항목에 대한 클릭 이벤트 처리를 추가할 수 있습니다.
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.v("test in click", "button 눌렀어" + name);
//                // 클릭 이벤트 처리 (원하는 동작 추가)
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return childList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView childName;
        TextView birthDate;
        TextView gender;
        TextView seed;

        public ViewHolder(@NonNull View view) {
            super(view);

            childName = view.findViewById(R.id.childNameEditText);
            birthDate = view.findViewById(R.id.birthdateEditText);
            gender = view.findViewById(R.id.genderEditText);
            seed = view.findViewById(R.id.seed);

            Log.v("in viewholder", String.valueOf(childName.getText()));
        }
    }
}
