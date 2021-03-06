package com.sombright.vizix.simultanea;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private MediaPlayer mediaPlayer;
    private boolean playIntro = true;
    private PreferencesProxy mPrefs;
    private Button profileButton, settingsButton, charactersButton, playButton;
    private Boolean isProfileButtonHeld = false;
    private Boolean isSettingsButtonHeld = false;
    private Boolean isCharactersButtonHeld = false;
    private Boolean isPlayButtonHeld = false;
    private TextView instructionTextView;
    private RelativeLayout instructionView;
    RelativeLayout backgroundImage;

    AnimationDrawable backgroundAnimation;

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




        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        mPrefs = new PreferencesProxy(this);


        instructionTextView = findViewById(R.id.instruction_text);
        instructionView = findViewById(R.id.instruction_view);

        profileButton = findViewById(R.id.PROFILE);
        profileButton.setOnLongClickListener(profileHoldListener);
        profileButton.setOnTouchListener(profileTouchListener);

        settingsButton = findViewById(R.id.SETTINGS);
        settingsButton.setOnLongClickListener(settingsHoldListener);
        settingsButton.setOnTouchListener(settingsTouchListener);

        charactersButton = findViewById(R.id.CHARACTERS);
        charactersButton.setOnLongClickListener(charactersHoldListener);
        charactersButton.setOnTouchListener(charactersTouchListener);

        playButton = findViewById(R.id.PLAY);
        playButton.setOnLongClickListener(playHoldListener);
        playButton.setOnTouchListener(playTouchListener);

       RelativeLayout backgroundImage = (RelativeLayout) findViewById(R.id.background_lava);
        backgroundImage.setBackgroundResource(R.drawable.background_lava);
        backgroundAnimation = (AnimationDrawable)backgroundImage.getBackground();

        backgroundImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundAnimation.start();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        if (playIntro) {
            playIntro = false;
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
        }
    }

        @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.home);
            mediaPlayer.setLooping(true);
        }
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }

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
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onPause();
    }

    public void onClickCharacters(View view) {
        Intent intent;
        Log.d(TAG, "Launching character selection activity");
        intent = new Intent(this, CharacterSelectionActivity.class);
        startActivity(intent);
    }

    private View.OnLongClickListener charactersHoldListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View pView) {
            // Do something when your hold starts here.
            isCharactersButtonHeld = true;
            instructionView.setVisibility(View.VISIBLE);
            instructionTextView.setText(R.string.instruction_characters_button);
            return true;
        }
    };

    private View.OnTouchListener charactersTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View pView, MotionEvent pEvent) {
            pView.onTouchEvent(pEvent);
            // We're only interested in when the button is released.
            if (pEvent.getAction() == MotionEvent.ACTION_UP) {
                // We're only interested in anything if our speak button is currently pressed.
                if (isCharactersButtonHeld) {
                    // Do something when the button is released.
                    isCharactersButtonHeld = false;
                    instructionView.setVisibility(View.GONE);
                }
            }
            return false;
        }
    };

    public void onClickPlay(View view) {
        Intent intent = new Intent(this, PlayMenuActivity.class);
        startActivity(intent);
    }

//    public void onClickPlay(View view) {
//        Intent intent;
//        if (view.getId() == R.id.PLAY_MULTI) {
//            if (mPrefs.isMultiPlayerMaster())
//                intent = new Intent(this, TaskMasterActivity.class);
//            else
//                intent = new Intent(this, PlayActivityMulti.class);
//        } else {
//            intent = new Intent(this, PlayActivitySingle.class);
//        }
//        startActivity(intent);
//    }

    private View.OnLongClickListener playHoldListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View pView) {
            // Do something when your hold starts here.
            isPlayButtonHeld = true;
            instructionView.setVisibility(View.VISIBLE);
            instructionTextView.setText(R.string.instruction_play_button);
            return true;
        }
    };

    private View.OnTouchListener playTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View pView, MotionEvent pEvent) {
            pView.onTouchEvent(pEvent);
            // We're only interested in when the button is released.
            if (pEvent.getAction() == MotionEvent.ACTION_UP) {
                // We're only interested in anything if our speak button is currently pressed.
                if (isPlayButtonHeld) {
                    // Do something when the button is released.
                    isPlayButtonHeld = false;
                    instructionView.setVisibility(View.GONE);
                }
            }
            return false;
        }
    };

    public void onClickSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private View.OnLongClickListener settingsHoldListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View pView) {
            // Do something when your hold starts here.
            isSettingsButtonHeld = true;
            instructionView.setVisibility(View.VISIBLE);
            instructionTextView.setText(R.string.instruction_settings_button);
            return true;
        }
    };

    private View.OnTouchListener settingsTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View pView, MotionEvent pEvent) {
            pView.onTouchEvent(pEvent);
            // We're only interested in when the button is released.
            if (pEvent.getAction() == MotionEvent.ACTION_UP) {
                // We're only interested in anything if our speak button is currently pressed.
                if (isSettingsButtonHeld) {
                    // Do something when the button is released.
                    isSettingsButtonHeld = false;
                    instructionView.setVisibility(View.GONE);
                }
            }
            return false;
        }
    };

    public void onClickProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private View.OnLongClickListener profileHoldListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View pView) {
            // Do something when your hold starts here.
            isProfileButtonHeld = true;
            instructionView.setVisibility(View.VISIBLE);
            instructionTextView.setText(R.string.instruction_profile_button);
            return true;
        }
    };

    private View.OnTouchListener profileTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View pView, MotionEvent pEvent) {
            pView.onTouchEvent(pEvent);
            // We're only interested in when the button is released.
            if (pEvent.getAction() == MotionEvent.ACTION_UP) {
                // We're only interested in anything if our speak button is currently pressed.
                if (isProfileButtonHeld) {
                    // Do something when the button is released.
                    isProfileButtonHeld = false;
                    instructionView.setVisibility(View.GONE);
                }
            }
            return false;
        }
    };

}
