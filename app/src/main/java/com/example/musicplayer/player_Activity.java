package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;

public class player_Activity extends AppCompatActivity {
    private ImageButton pause , left_buttom , right_buttom;
    private TextView namesong , start , stop;
    private ImageView imageView;
    private SeekBar seekBar ;
    private BarVisualizer barVisualizer;
    static MediaPlayer mediaPlayer ;
    private int position;
    private ArrayList<File> mysongs;
    private Thread updateseekbar;

//    private Handler handler;
//    private int delay = 1000;
    public static final String EXTRA_NAME = "song_name";


    public void unit(){
        pause = findViewById(R.id.pause_btn);
        left_buttom = findViewById(R.id.btn_next2);
        right_buttom = findViewById(R.id.btn_next);
        namesong = findViewById(R.id.name_song);
        start = findViewById(R.id.timestart);
        stop = findViewById(R.id.timestop);
        imageView = findViewById(R.id.imageview);
        seekBar = findViewById(R.id.seek_bar);
        barVisualizer = findViewById(R.id.blast1);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        if (barVisualizer != null)
        {
            barVisualizer.release();
        }
        super.onDestroy();
    }
    public void startAnimation(View view)
    {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f,360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        unit();

        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        mysongs = (ArrayList) bundle.getParcelableArrayList("songs");
        String songName = i.getStringExtra("songname");
        position = bundle.getInt("pos" ,0);
        namesong.setSelected(true);
        Uri uri = Uri.parse(mysongs.get(position).toString());
        String sname = mysongs.get(position).getName();
        namesong.setText(sname);
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        updateseekbar = new Thread(){

            public void  run(){
                int totalDuration = mediaPlayer.getDuration();
                int currentpositon = 0;
                while (currentpositon<totalDuration){
                    try {
                        sleep(500);
                        currentpositon = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentpositon);
                    } catch (InterruptedException | IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        seekBar.setMax(mediaPlayer.getDuration());
        updateseekbar.start();
        seekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        seekBar.getThumb().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        String endTime = createTime(mediaPlayer.getDuration());
        stop.setText(endTime);

       final Handler handler = new Handler();
       final int delay =1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = createTime(mediaPlayer.getCurrentPosition());
                start.setText(currentTime);
                handler.postDelayed(this,delay);
            }
        },delay);



        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    pause.setBackgroundResource(R.drawable.play_buttom);
                    mediaPlayer.pause();
                }else {
                    pause.setBackgroundResource(R.drawable.pause);
                    mediaPlayer.start();
                }
            }
        });
//        next listener
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                right_buttom.performClick();
            }
        });
        int audiosessionId = mediaPlayer.getAudioSessionId();
        if (audiosessionId != -1){
            barVisualizer.setAudioSessionId(audiosessionId);
        }
        right_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position+1)%mysongs.size());
                Uri u = Uri.parse(mysongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                String sname = mysongs.get(position).getName();
                namesong.setText(sname);
                mediaPlayer.start();
                pause.setBackgroundResource(R.drawable.pause);
                startAnimation(imageView);
                int audiosessionId = mediaPlayer.getAudioSessionId();
                if (audiosessionId != -1) {
                    barVisualizer.setAudioSessionId(audiosessionId);
                }
            }
        });
        left_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position-1)<0)?(mysongs.size()-1):(position-1);
                Uri u = Uri.parse(mysongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                String sname = mysongs.get(position).getName();
                namesong.setText(sname);
                mediaPlayer.start();
                pause.setBackgroundResource(R.drawable.pause);
                startAnimation(imageView);
                int audiosessionId = mediaPlayer.getAudioSessionId();
                if (audiosessionId != -1) {
                    barVisualizer.setAudioSessionId(audiosessionId);
                }
            }
        });
    }

    public String createTime(int duration)
    {
        String time = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;

        time+=min+":";

        if (sec<10)
        {
            time+="0";
        }
        time+=sec;

        return  time;
    }

}