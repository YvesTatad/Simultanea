package com.sombright.vizix.simultanea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.sombright.vizix.simultanea.PreferencesProxy;

public class ProfileActivity extends AppCompatActivity {

    private String profilePlayerName;
    private PreferencesProxy mPrefs;
    private TextView profileNameTextView;
    private String points;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mPrefs = new PreferencesProxy(this);
        profilePlayerName = mPrefs.getMultiPlayerAlias();
        profileNameTextView = findViewById(R.id.profileNameText);
        profileNameTextView.setText(profilePlayerName);
        TextView textView = findViewById(R.id.textViewHighScore);
        textView.setText("High Score: " + mPrefs.getHighScore());



    }
}
