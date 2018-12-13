package com.sombright.vizix.simultanea;

import android.app.ActionBar;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class IntroActivity extends AppCompatActivity /*implements MediaPlayer.OnCompletionListener*/ {
    private static final String TAG = "IntroActivity";
    //    private static final int videoSequence[] = {
//            R.raw.load,
//            R.raw.pyre,
//    };
//    private int videoCounter;
    private ImageView introBG;
    private ImageView scrollView;
    private TextView script1;
    private TextView script2;
    private TextView script3;
    private TextView script4;
    private TextView script5;
    private TextView script6;
    private TextView script7;
    private TextView script8;
    private TextView script9;
    private TextView skipTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            ActionBar actionBar = getActionBar();
            if (actionBar != null)
                actionBar.hide();
        }

        setContentView(R.layout.activity_intro);
        scrollView = findViewById(R.id.ScrollFrame);
        scrollView.setImageResource(R.drawable.scroll_1_2);
        introBG = findViewById(R.id.IntroBackground);
        Glide.with(this).load(R.drawable.nightskybackground).into(introBG);
        script1 = findViewById(R.id.introText1);
        script2 = findViewById(R.id.introText2);
        script3 = findViewById(R.id.introText3);
        script4 = findViewById(R.id.introText4);
        script5 = findViewById(R.id.introText5);
        script6 = findViewById(R.id.introText6);
        script7 = findViewById(R.id.introText7);
        script8 = findViewById(R.id.introText8);
        script9 = findViewById(R.id.introText9);
        skipTextView = findViewById(R.id.skipText);

        script1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                script1.setVisibility(View.GONE);
                script2.setVisibility(View.VISIBLE);
            }
        });

        script2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                script2.setVisibility(View.GONE);
                script3.setVisibility(View.VISIBLE);
            }
        });

        script3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                script3.setVisibility(View.GONE);
                script4.setVisibility(View.VISIBLE);
            }
        });

        script4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                script4.setVisibility(View.GONE);
                script5.setVisibility(View.VISIBLE);
            }
        });

        script5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                script5.setVisibility(View.GONE);
                script6.setVisibility(View.VISIBLE);
            }
        });

        script6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                script6.setVisibility(View.GONE);
                script7.setVisibility(View.VISIBLE);
            }
        });

        script7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                script7.setVisibility(View.GONE);
                script8.setVisibility(View.VISIBLE);
            }
        });

        script8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                script8.setVisibility(View.GONE);
                script9.setVisibility(View.VISIBLE);
            }
        });

        script9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent, 0);
                finish();
            }
        });

        skipTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent, 0);
                finish();
            }
        });
    }

//    @Override
//    protected void onStart() {
//        Log.d(TAG, "onStart");
//        super.onStart();
//        startNextVideo();
//    }
//
//    @Override
//    protected void onRestart() {
//        Log.d(TAG, "onReStart");
//        videoCounter = 0;
//        super.onRestart();
//    }
//
//    @Override
//    protected void onResume() {
//        Log.d(TAG, "onResume");
//        if (scrollView.isPlaying()) {
//        }
//        else scrollView.resume();
//        super.onResume();
//    }
//
//    @Override
//    protected void onStop() {
//        Log.d(TAG, "onStop");
//        scrollView.stopPlayback();
//        super.onStop();
//    }
//
//    @Override
//    protected void onPause() {
//        Log.d(TAG, "onPause");
//        scrollView.suspend();
//        super.onPause();
//    }
//
//    private void startNextVideo() {
//        int currentVideo = videoSequence[videoCounter];
//        String videoPath = "android.resource://" +  getPackageName() + "/" + currentVideo;
//        Uri uri = Uri.parse(videoPath);
//        scrollView.setVideoURI(uri);
//        scrollView.setOnCompletionListener(this);
//        scrollView.start();
//    }
//
//    @Override
//    public void onCompletion(/*MediaPlayer mp*/) {
//        videoCounter++;
//        if (videoCounter < videoSequence.length) {
//            startNextVideo();
//        } else {
//            finish();
//        }
//    }
}