package com.lite.holistic_tracking.Entity;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lite.holistic_tracking.R;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private List<Song> songList;
    private Context mContext;
    private AdapterView.OnItemClickListener mListener;
    private Child child;
    private ImageView songImageView;

    public SongAdapter(List<Song> songList, Context context, Child child) {
        this.songList = songList;
        this.mContext = context;
        this.child = child;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item, parent, false);
        SongAdapter.SongViewHolder vh = new SongAdapter.SongViewHolder(view);

        Log.v("in adpater", String.valueOf(songList.size()));
        Log.v("backpress check", "song adapter's onCreateViewHolder " + " " + child.getChildName());

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.imageView.setImageResource(song.getImageResource());
        holder.titleTextView.setText(song.getTitle());
        holder.levelTextView.setText(String.valueOf(song.getLevel()));

        Log.v("backpress check", "song adapter's onBindViewHolder " + " " + song.getTitle());

        // 클릭 이벤트 리스너 - 해당 음악 재생하는 리스너 추가
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 여기에서 노래 재생할 수 있는 화면 띄우기
                // (노래 선택) / (다시 고르기)
                
                // 1. 노래 선택 - 해당 노래의 DB 정보를 가지고 와서, 동물 선택 화면으로 객체를 넘김
                // 2. 동물 모자 선택 - 해당 동물 모자의 DB 정보를 가지고 와서, 시작 화면으로 이동
                
                // 바인드 홀더에서 position으로 가지고 song 객체를 가지고 옴
                // 노래 듣기 화면
                SongDialog songDialog = new SongDialog(view.getContext(), song, child);
                songDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView; // 노래 앨범 커버
        TextView titleTextView; // 노래 타이틀

        TextView levelTextView; // 리듬 게임 난이도

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewMusic);
            titleTextView = itemView.findViewById(R.id.textViewMusicTitle);
            levelTextView = itemView.findViewById(R.id.textViewMusicLevel);
        }
    }
}
