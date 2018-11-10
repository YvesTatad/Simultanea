package com.sombright.vizix.simultanea.MobCharacters;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.sombright.vizix.simultanea.CharacterDetailsActivity;
import com.sombright.vizix.simultanea.R;

public class MobSelectionActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    MobPool mAdapter;
    private GridView gridview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mob_selection);


        // Instead of manually filling the grid with character icons, we connect
        // the GridView to an "adapter" whose job is to prepare each character
        // icon in a uniform way.
        gridview = findViewById(R.id.characterSelectionGridView);
        mAdapter = new MobSelectionActivity(this, R.layout.activity_mob_selection,MobPool.mobList);
        gridview.setAdapter(mAdapter);
        gridview.setOnItemClickListener(this);
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
