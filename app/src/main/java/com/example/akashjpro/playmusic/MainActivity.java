package com.example.akashjpro.playmusic;

import android.graphics.drawable.StateListDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import static com.example.akashjpro.playmusic.R.id.seekBar;

public class MainActivity extends AppCompatActivity {

    ImageButton ibtnPlay, ibtnPrev, ibtNext, ibtShuffle, ibnRepeat;
    ImageView imgDiaTron;
    SeekBar skSong;
    ArrayList<Integer> arraySong;
    TextView txtTimeStart, txtTimeTotal;
    MediaPlayer mediaPlayer;
    int vitri = 0;
    boolean shuffle = false;
    boolean repeat  = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        khoiTaoMang();
        final StateListDrawable stateListDrawablec = new StateListDrawable();
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.xoay_dia);
        mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(vitri));
                ibtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    ibtnPlay.setImageResource(R.drawable.play1_1);
                    imgDiaTron.clearAnimation();
                   // imgDiaTron.clearAnimation();
                    //stateListDrawablec.addState(new int[] {android.R.attr.state_pressed}, getResources().getDrawable(R.drawable.play1_1) );
                }
                else{
                    imgDiaTron.startAnimation(animation);
                    mediaPlayer.start();
                    ibtnPlay.setImageResource(R.drawable.pause1);
                   // stateListDrawablec.addState(new int[] {android.R.attr.state_pressed}, getResources().getDrawable(R.drawable.pause1) );

                }
                timeSong();
                upDateCurrentTime();
            }
        });
        ibtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtnPlay.setImageResource(R.drawable.pause1);
                xuLyNext();
            }
        });

        ibtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtnPlay.setImageResource(R.drawable.pause1);
                vitri--;
                if (vitri < 0){
                    vitri = arraySong.size() - 1 ;
                }
                skSong.setProgress(0);
                mediaPlayer.stop();
                mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(vitri));
                mediaPlayer.start();
                timeSong();
            }
        });

        ibtShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shuffle){
                    ibtShuffle.setImageResource(R.drawable.shuffle);
                    shuffle = false;
                    ibnRepeat.setEnabled(true);
                }
                else {
                    Toast.makeText(MainActivity.this, "Phát ngẫu nhiên", Toast.LENGTH_SHORT).show();
                    ibtShuffle.setImageResource(R.drawable.shuffle1);
                    shuffle = true;
                    ibnRepeat.setEnabled(false);
                }

            }
        });

        ibnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(repeat){
                    ibnRepeat.setImageResource(R.drawable.ic_repeat);
                    repeat = false;
                    ibtShuffle.setEnabled(true);
                }
                else {
                    Toast.makeText(MainActivity.this, "Phát lặp lại", Toast.LENGTH_SHORT).show();
                    ibtShuffle.setEnabled(false);
                    ibnRepeat.setImageResource(R.drawable.repeat1);
                    repeat = true;
                }

            }
        });

//        ibnStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mediaPlayer.stop();
//                ibtnPlay.setImageResource(R.drawable.img_btn_play);
//                mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(vitri));
//            }
//        });

        skSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(!fromUser){
                    return;
                }

               mediaPlayer.seekTo(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(seekBar.getProgress() >= mediaPlayer.getDuration()){
                    if(repeat){
                        playMusic();
                    }
                    else if (shuffle){
                        Random randomBaiHat = new Random();
                        vitri = randomBaiHat.nextInt(arraySong.size());
                        playMusic();
                    }
                    else {
                        xuLyNext();
                    }
                }
                mediaPlayer.seekTo(seekBar.getProgress());// seekTo la nhay toi dau
            }
        });

    }

    private void xuLyNext() {
        vitri++;
        if (vitri >= arraySong.size()){
            vitri = 0;
        }
        playMusic();

    }

    private void playMusic() {
        skSong.setProgress(0);
        mediaPlayer.stop();
        mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(vitri));
        mediaPlayer.start();
        timeSong();
    }

    private void upDateCurrentTime(){
        final Handler handler = new Handler();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtTimeStart.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                skSong.setProgress(mediaPlayer.getCurrentPosition());
                if ( mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition() <= 500){
                    if(repeat){
//                        ibnRepeat.setImageResource(R.drawable.ic_repeat);
//                        repeat = false;
//                        ibtShuffle.setEnabled(true);
                        playMusic();
                    }else if(shuffle){
                        Random randomBaiHat = new Random();
                        vitri = randomBaiHat.nextInt(arraySong.size());
//                        ibtShuffle.setImageResource(R.drawable.shuffle);
//                        shuffle = false;
//                        ibnRepeat.setEnabled(true);
                        playMusic();
                    }
                    else {
                        xuLyNext();
                    }
                }
                handler.postDelayed(this, 500);
            }
        }, 0);
    }



    private void  timeSong(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        txtTimeTotal.setText(simpleDateFormat.format(mediaPlayer.getDuration() )+ "");
        //gan max cho seekbar bang tong thoi gian cua bai hat
        skSong.setMax(mediaPlayer.getDuration());

    }
    private void khoiTaoMang() {
        arraySong = new ArrayList<Integer>();
        arraySong.add(R.raw.a);
        arraySong.add(R.raw.b);
        arraySong.add(R.raw.c);
        arraySong.add(R.raw.d);
    }

    private void addControls() {
        ibtnPlay    = (ImageButton) findViewById(R.id.imageButtonPlay);
        ibtNext     = (ImageButton) findViewById(R.id.imageButtonNext);
        ibtnPrev    = (ImageButton) findViewById(R.id.imageButtonpPevious);
        ibtShuffle  = (ImageButton) findViewById(R.id.imageButtonShuffle);
        ibnRepeat   = (ImageButton) findViewById(R.id.imageButtonRepeat);

        imgDiaTron = (ImageView) findViewById(R.id.imageViewDiaTron);

        txtTimeTotal = (TextView) findViewById(R.id.txtThoiGianKetThuc);
        txtTimeStart = (TextView) findViewById(R.id.txtThoiGianBatDau);

        skSong = (SeekBar) findViewById(seekBar);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
    }
}
