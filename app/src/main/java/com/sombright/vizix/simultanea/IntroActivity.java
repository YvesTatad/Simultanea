package com.sombright.vizix.simultanea;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class IntroActivity extends AppCompatActivity /*implements MediaPlayer.OnCompletionListener*/ {
    private static final String TAG = "IntroActivity";
//    private static final int videoSequence[] = {
//            R.raw.load,
//            R.raw.pyre,
//    };
//    private int videoCounter;
    private ImageView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);
        scrollView = findViewById(R.id.ScrollFrame);
        scrollView.setImageResource(R.drawable.scroll_1_2);
//        videoCounter = 0;
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
//        if (!scrollView.isPlaying()) {
//            scrollView.resume();
//        }
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
//        String videoPath = "android.resource://" + getPackageName() + "/" + currentVideo;
//        Uri uri = Uri.parse(videoPath);
//        scrollView.setVideoURI(uri);
//        scrollView.setOnCompletionListener(this);
//        scrollView.start();
//    }
//
//    @Override
//    public void onCompletion(MediaPlayer mp) {
//        videoCounter++;
//        if (videoCounter < videoSequence.length) {
//            startNextVideo();
//        } else {
//            finish();
//        }
//    }
}