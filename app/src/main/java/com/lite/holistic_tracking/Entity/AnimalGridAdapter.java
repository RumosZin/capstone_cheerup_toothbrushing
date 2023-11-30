package com.lite.holistic_tracking.Entity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lite.holistic_tracking.BuyingDialog;
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
                // 클릭한 동물 정보를 넘김!
//                BuyingDialog buyingDialog = new BuyingDialog(view.getContext(), child, animal);
//                buyingDialog.show();
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

//    @NonNull
//    @Override
//    public AnimalGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.animal_item, parent, false);
//        AnimalGridAdapter.AnimalGridViewHolder vh = new AnimalGridAdapter.AnimalGridViewHolder(view);
//
//        return vh;
//    }
//


//    @Override
//    public void onBindViewHolder(@NonNull AnimalGridViewHolder holder, int position) {
//        Animal animal = animalList.get(position);
//        holder.imageView.setImageResource(animal.getImageResource());
//        holder.nameTextView.setText(animal.getName());
//        holder.seedTextView.setText(String.valueOf(animal.getRequiredSeed()));
//        Log.v("ddddddddddddddddddddddddddddddddddd", animal.getName());
//
//        // 클릭 이벤트 리스너 추가
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 클릭한 동물 정보를 BuyingDialog 액티비티로 전달하여 시작
////                Intent intent = new Intent(mContext, BuyingDialog.class);
////                intent.putExtra("animalName", animal.getName());
////                intent.putExtra("animalDescription", animal.getRequiredSeed());
////                mContext.startActivity(intent);
////                BuyingDialog buyingDialog = new BuyingDialog(view.getContext(), child, animal);
////                buyingDialog.show();
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return animalList.size();
//    }
//
//    public static class AnimalGridViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageView;
//        TextView nameTextView;
//        TextView seedTextView;
//
//        public AnimalGridViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imageView = itemView.findViewById(R.id.imageViewAnimal);
//            nameTextView = itemView.findViewById(R.id.textViewAnimalName);
//            seedTextView = itemView.findViewById(R.id.textViewRequiredSeed);
//        }
//    }
}
