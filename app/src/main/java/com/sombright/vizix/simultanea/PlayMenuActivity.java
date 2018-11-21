package com.sombright.vizix.simultanea;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class PlayMenuActivity extends AppCompatActivity {

    private Button singlePlayerButton, multiplayerButton, battleSettingsButton, gameplayInformationButton;
    private PreferencesProxy mPrefs;

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

        setContentView(R.layout.activity_play_menu);
        mPrefs = new PreferencesProxy(this);
        singlePlayerButton = findViewById(R.id.buttonSinglePlayerGM);
        multiplayerButton = findViewById(R.id.buttonMultiplayerGM);
        battleSettingsButton = findViewById(R.id.buttonBattleSettings);
        gameplayInformationButton = findViewById(R.id.buttonGameplayInformation);
    }

    public void onClickPlaySingle(View view) {
        Intent intent = new Intent(this, PlayActivitySingle.class);
        startActivity(intent);
    }

    public void onClickPlayMulti(View view) {
        Intent intent;
        if (mPrefs.isMultiPlayerMaster()){
                intent = new Intent(this, TaskMasterActivity.class);
        }
        else{
                intent = new Intent(this, PlayActivityMulti.class);
        }
        startActivity(intent);
    }

    public void onClickBattleSettings(View view) {
//        Intent intent = new Intent();
//        startActivity(intent);
        Toast.makeText(this, "Currently Unavailable.\nPlease wait for the next update.", Toast.LENGTH_SHORT).show();
    }

    public void onClickGameplayInformation(View view) {
//        Intent intent = new Intent();
//        startActivity(intent);
        Toast.makeText(this, "Currently Unavailable.\nPlease wait for the next update.", Toast.LENGTH_SHORT).show();
    }
}
