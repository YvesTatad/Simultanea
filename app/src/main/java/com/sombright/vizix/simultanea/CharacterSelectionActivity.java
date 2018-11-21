package com.sombright.vizix.simultanea;

import android.app.ActionBar;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

public class CharacterSelectionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private MediaPlayer mediaPlayer;
    CharacterSelectionAdapter mAdapter;
    private GridView gridview;

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

        setContentView(R.layout.activity_character_selection);

        // Instead of manually filling the grid with character icons, we connect
        // the GridView to an "adapter" whose job is to prepare each character
        // icon in a uniform way.
        gridview = findViewById(R.id.characterSelectionGridView);
        mAdapter = new CharacterSelectionAdapter(this, R.layout.character_selection_example_item, CharacterPool.charactersList);
        gridview.setAdapter(mAdapter);
        gridview.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

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

        mediaPlayer = MediaPlayer.create(CharacterSelectionActivity.this, R.raw.fight1);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    // Called when the player clicks an item in the grid view (position)
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        // Launch CharacterDetailsActivity to show the details of the character at "position"
        Intent intent;
        intent = new Intent(this, CharacterDetailsActivity.class);
        intent.putExtra("index", position);
        startActivity(intent);
    }
}
