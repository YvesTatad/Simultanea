package com.sombright.vizix.simultanea;

import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, TextView.OnEditorActionListener {
    private Switch mMultiPlayerMasterView;
    private Switch mOpenTriviaSwitch;
    private EditText mMultiPlayerAliasView;
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

        setContentView(R.layout.activity_settings);

        mPrefs = new PreferencesProxy(this);

        // Restore preferences
        boolean multiPlayerMaster = mPrefs.isMultiPlayerMaster();
        String multiPlayerAlias = mPrefs.getMultiPlayerAlias();
        boolean useOpenTriviaDatabase = mPrefs.shouldUseOpenTriviaDatabase();

        // Modify UI to show the current values
        mMultiPlayerMasterView = findViewById(R.id.multiPlayerMasterSwitch);
        mMultiPlayerMasterView.setChecked(multiPlayerMaster);
        mMultiPlayerMasterView.setOnCheckedChangeListener(this);

        mMultiPlayerAliasView = findViewById(R.id.multiPlayerAlias);
        mMultiPlayerAliasView.setText(multiPlayerAlias);
        mMultiPlayerAliasView.setOnEditorActionListener(this);

        mOpenTriviaSwitch = findViewById(R.id.openTriviaSwitch);
        mOpenTriviaSwitch.setChecked(useOpenTriviaDatabase);
        mOpenTriviaSwitch.setOnCheckedChangeListener(this);
    }

    // --------------- Get notified when UI changes -----------------------------

    // This gets called when a Switch button was toggled
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.multiPlayerMasterSwitch:
                mPrefs.setMultiplayerMaster(b);
                break;
            case R.id.openTriviaSwitch:
                mPrefs.setUseOpenTriviaDatabase(b);
                break;
        }
    }

    // This gets called when we interact with the TextView
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v != mMultiPlayerAliasView) {
            return false;
        }
        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                event != null &&
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (event == null || !event.isShiftPressed()) {
                mPrefs.setMultiplayerAlias(mMultiPlayerAliasView.getText().toString());
                return false;
            }
        }
        return false;
    }

}
