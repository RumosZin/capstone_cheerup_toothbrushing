package com.lite.holistic_tracking.Entity;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lite.holistic_tracking.AnimalChoiceActivity;
import com.lite.holistic_tracking.R;

public class SongDialog extends Dialog {

    // 노래 정보를 띄우는 다이아로그
    // 선택한 Song의 정보를 가져옴

    private Song song;
    private TextView titleTextView;
    private TextView levelTextView;
    private ImageView songImageView;

    private Button pickButton;
    private Button reButton;
    private Button songButton;
    private MediaPlayer mediaPlayer;
    private Child child;


    public SongDialog(@NonNull Context context, Song song, Child child) {
        super(context);
        setContentView(R.layout.activity_song_dialog); // 일단 노래를 들어야 하잖아,,,

        this.song = song;
        this.child = child;
        
        // xml이랑 code 연결
        titleTextView = findViewById(R.id.titleTextView);
        levelTextView = findViewById(R.id.levelTextView);
        pickButton = findViewById(R.id.pickButton);
        reButton = findViewById(R.id.reButton);
        songButton = findViewById(R.id.songButton);
        songImageView = findViewById(R.id.detail_song_image);
        
        // 넘겨 받은 song 정보
        titleTextView.setText(song.getTitle());
        levelTextView.setText(String.valueOf(song.getLevel()));
        songImageView.setImageResource(song.getImageResource());
        
        // 다시 고른다고 하면 다이얼로그 내리기
        reButton.setOnClickListener(v -> {
            mediaPlayer.stop();
            dismiss();
        });

        songButton.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    // 노래가 재생 중이면 멈추기
                    mediaPlayer.pause();
                    songButton.setBackgroundResource(R.drawable.playbutton);

                } else {
                    // 노래가 멈춘 상태이면 재생하기
                    mediaPlayer.start();
                    songButton.setBackgroundResource(R.drawable.pausebutton);
                }
            }
        });
        
        // 선택하면 다이얼로그 내리고, song 제목 넘기기
        pickButton.setOnClickListener(v -> {
            // 노래 멈추기
            mediaPlayer.stop();

            // 다이얼로그 내리기
            dismiss();

            // Song, 자녀 정보를 AnimalChoiceActivity로 전달
            Intent intent = new Intent(getContext(), AnimalChoiceActivity.class);
            intent.putExtra("songTitle", song.getTitle());  // "songTitle"이라는 키로 Song의 제목을 전달
            intent.putExtra("childName", child.getChildName()); // "childName"이라는 키로 ChildName 전달
            intent.putExtra("birthDate", child.getBirthDate());
            intent.putExtra("gender", child.getGender());
            intent.putExtra("seed", child.getSeed()); // "songLevel"이라는 키로 Song의 레벨을 전달

            // AnimalChoiceActivity를 시작
            getContext().startActivity(intent);
        });


        // song 이름에 따라서 노래 틀기
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.rabbit);
        mediaPlayer.setLooping(true); // 무한 재생 되도록 설정, 어차피 dialog가 내려가면
        mediaPlayer.start(); // 노래 시작

        setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                dismiss();
                return true;  // event handled
            }
            return false;  // event not handled
        });
    }

    @Override
    public void dismiss() {
        // MediaPlayer 종료
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }

        // 부모의 dismiss() 메서드 호출
        super.dismiss();
    }
}
