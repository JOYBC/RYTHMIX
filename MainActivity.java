package com.coder1.rhythmix;

import static com.coder1.rhythmix.R.layout.activity_main;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button b1, b2, b3, b4;
    TextView title_txt, time_txt,txt1;
    SeekBar seek_bar;
    MediaPlayer mediaPlayer;

    Handler handler = new Handler();
    double startTime = 0;
    double finalTime = 0;
    int forwardTime = 10000;
    int backwardTime = 10000;
    static int oneTimeOnly = 0;


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.play_arrow);
        b2 = findViewById(R.id.pause);
        b3 = findViewById(R.id.frontfast);
        b4 = findViewById(R.id.backfast);
        txt1=findViewById(R.id.txt1);
        title_txt = findViewById(R.id.Song_title);
        time_txt = findViewById(R.id.time);
        seek_bar = findViewById(R.id.seek_bar);
        mediaPlayer = MediaPlayer.create(this, R.raw.terevaaste);
        seek_bar.setClickable(false);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayMusic();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int temp = (int) startTime;
                if (temp + forwardTime < +finalTime) {
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                } else {
                    Toast.makeText(MainActivity.this, "Can't jump forward", Toast.LENGTH_SHORT).show();
                }
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) startTime;
                if (temp - backwardTime > 0) {
                    startTime -= backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                } else {
                    Toast.makeText(MainActivity.this, "Can't go back!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        time_txt.setText(String.format("%d min,%d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime))));
        seek_bar.setProgress((int) startTime);
        handler.postDelayed(UpdateSongTime, 10000);

    }

    private void PlayMusic() {
        mediaPlayer.start();
        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();
        if (oneTimeOnly == 0) {
            seek_bar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }
    }

    private final Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            time_txt.setText(String.format("%d min %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))
            ));
            seek_bar.setProgress((int) startTime);
            handler.postDelayed(this, 100);
        }


    };
}
