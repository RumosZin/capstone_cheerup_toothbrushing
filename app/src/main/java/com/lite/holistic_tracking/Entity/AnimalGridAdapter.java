package com.lite.holistic_tracking.Entity;

import static androidx.core.content.ContextCompat.startActivity;
import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lite.holistic_tracking.HolisticActivity;
import com.lite.holistic_tracking.R;

import java.util.List;

public class AnimalGridAdapter extends BaseAdapter {

    private List<Animal> animalList;
    private Context mContext;
    private Child child;

    public AnimalGridAdapter(List<Animal> animalList, Context context, Child child) {
        this.animalList = animalList;
        this.mContext = context;
        this.child = child;
    }
    @Override
    public int getCount() {
        return animalList.size();
    }

    @Override
    public Object getItem(int position) {
        return animalList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        AnimalGridViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.animal_grid_item, parent, false);
            holder = new AnimalGridViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (AnimalGridViewHolder) view.getTag();
        }

        Animal animal = animalList.get(position);
        holder.imageView.setImageResource(animal.getImageResource());
        //holder.nameTextView.setText(animal.getName());
        //holder.seedTextView.setText(String.valueOf(animal.getRequiredSeed()));

        // 클릭 이벤트 리스너 추가
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, HolisticActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });


        return view;
    }

    public static class AnimalGridViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView seedTextView;

        public AnimalGridViewHolder(View itemView) {
            imageView = itemView.findViewById(R.id.imageViewAnimal);
            nameTextView = itemView.findViewById(R.id.textViewAnimalName);
            seedTextView = itemView.findViewById(R.id.textViewRequiredSeed);
        }
    }
}
