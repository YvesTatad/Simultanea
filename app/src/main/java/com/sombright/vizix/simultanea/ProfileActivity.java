package com.sombright.vizix.simultanea;

import android.app.ActionBar;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private String profilePlayerName;
    private PreferencesProxy mPrefs;
    private TextView profileNameTextView;
    private ImageView profileCharacterImage;
    private String points;


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

        setContentView(R.layout.activity_profile);
        mPrefs = new PreferencesProxy(this);
        profilePlayerName = mPrefs.getMultiPlayerAlias();
        profileNameTextView = findViewById(R.id.profileNameText);
        profileNameTextView.setText(profilePlayerName);
        TextView textView = findViewById(R.id.profileHighScoreText);
        textView.setText("High Score: " + mPrefs.getHighScore());

        profileCharacterImage = findViewById(R.id.profileCharacterImage);
//        profileCharacterImage.setImageResource();
        //TODO -Yves: Set a dynamic profile avatar based on picked Character.
        //TODO -Yves: Enable animated images for Profile Avatar.
    }
}
