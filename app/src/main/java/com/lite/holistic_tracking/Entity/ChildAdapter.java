package com.lite.holistic_tracking.Entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lite.holistic_tracking.R;

import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder>{
    private List<Child> children;

    public ChildAdapter(List<Child> children) {
        this.children = children;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.child_list_item, parent, false);
        ChildAdapter.ViewHolder vh = new ChildAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Child item = children.get(position);
        holder.childName.setText(item.childName);
    }

    @Override
    public int getItemCount() {
        return children.size();
    }

    public void setOnItemClickListener(ChildAdapter.OnItemClickListener onItemClickListener) {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView childName;

        public ViewHolder(View itemView) {
            super(itemView);
            childName = itemView.findViewById(R.id.childNameTextView);
        }
    }

    public class OnItemClickListener {
    }
}
