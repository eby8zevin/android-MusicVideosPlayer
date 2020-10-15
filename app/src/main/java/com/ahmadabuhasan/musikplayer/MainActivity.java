package com.ahmadabuhasan.musikplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ahmad Abu Hasan on 15/10/2020.
 */

public class MainActivity extends AppCompatActivity {

    ImageView play, prev, next, imageView;
    TextView songTitle;
    SeekBar mSeekBarTime;
    private MediaPlayer mediaPlayer;
    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.play);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        imageView = findViewById(R.id.imageView);

        songTitle = findViewById(R.id.songTitle);

        mSeekBarTime = findViewById(R.id.seekBar);

        final ArrayList<Integer> songs = new ArrayList<>();
        songs.add(0, R.raw.totalitas_perjuangan);
        songs.add(1, R.raw.buruh_tani);
        songs.add(2, R.raw.dpr_gblk);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBarTime.setMax(mediaPlayer.getDuration());
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    play.setImageResource(R.drawable.ic_play);
                } else {
                    assert mediaPlayer != null;
                    mediaPlayer.start();
                    play.setImageResource(R.drawable.ic_pause);
                }
                songDetails();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    play.setImageResource(R.drawable.ic_pause);
                }
                if (currentIndex > 0) {
                    currentIndex--;
                } else {
                    currentIndex = songs.size() - 1;
                }
                assert mediaPlayer != null;
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));
                mediaPlayer.start();
                songDetails();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    play.setImageResource(R.drawable.ic_pause);
                }

                if (currentIndex < songs.size() - 1) {
                    currentIndex++;
                } else {
                    currentIndex = 0;
                }
                assert mediaPlayer != null;
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));
                mediaPlayer.start();
                songDetails();
            }
        });

        mSeekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    mSeekBarTime.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mSeekBarTime.setMax(mediaPlayer.getDuration());
                //mediaPlayer.start();
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    try {
                        if (mediaPlayer.isPlaying()) {
                            Message message = new Message();
                            message.what = mediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @SuppressLint("SetTextI18n")
    private void songDetails() {
        if (currentIndex == 0) {
            songTitle.setText("Mars Mahasiswa - Totalitas Perjuangan"); //Judul Lagu 1
            imageView.setImageResource(R.drawable.preview); //Gambar Lagu 1
        }
        if (currentIndex == 1) {
            songTitle.setText("Mars Mahasiswa - Buruh Tani"); //Judul Lagu 2
            imageView.setImageResource(R.drawable.preview1); //Gambar Lagu 2
        }
        if (currentIndex == 2) {
            songTitle.setText("STM - DPR GBLK"); //Judul Lagu 3
            imageView.setImageResource(R.drawable.pekok); //Gambar Lagu 3
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            mSeekBarTime.setProgress(msg.what);
        }
    };
}