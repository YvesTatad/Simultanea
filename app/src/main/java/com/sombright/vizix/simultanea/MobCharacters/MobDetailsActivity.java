package com.sombright.vizix.simultanea.MobCharacters;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.sombright.vizix.simultanea.R;

public class MobDetailsActivity extends AppCompatActivity {

    private int mMobNum;
    private static final String TAG_STATS = "stats";
    private static final String TAG_LORE = "lore";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mob_details);
        // Setup the tabs
        TabHost tabHost = findViewById(R.id.tabHostStatsLore);
        tabHost.setup();
        TabHost.TabSpec spec = tabHost.newTabSpec(TAG_STATS);
        spec.setContent(R.id.tabStats);
        spec.setIndicator("Stats");
        tabHost.addTab(spec);
        spec = tabHost.newTabSpec(TAG_LORE);
        spec.setContent(R.id.tabLore);
        spec.setIndicator("Lore");
        tabHost.addTab(spec);

        // Retrieve the character selection sent by the main activity as part of the "intent"
        Intent intent = getIntent();
        mMobNum = intent.getIntExtra("index", 0);
        com.sombright.vizix.simultanea.MobCharacters.MobModel mob = MobPool.mobList[mMobNum];

        ImageView imageViewCharacter = findViewById(R.id.imageViewMob);
        imageViewCharacter.setImageResource(mob.getImageResource());
        TextView textViewCharacterName = findViewById(R.id.textViewMobName);
        textViewCharacterName.setText(mob.getStringResourceName());
        TextView textViewStats = findViewById(R.id.textViewStats);
        String mob_msg_stats = String.format(getString(R.string.mob_msg_stats),
                mob.getHeal(), mob.getRecovery(),
                mob.getAttack(), mob.getDefense());
        textViewStats.setText(mob_msg_stats);
        TextView textViewLore = findViewById(R.id.textViewLore);
        textViewLore.setText(mob.getStringResourceLore());
        textViewLore.setMovementMethod(new ScrollingMovementMethod());

//        Button pickButton = findViewById(R.id.buttonPick);
//        pickButton.setEnabled(character.isPlayable());
//        if (!character.isPlayable()){
//            pickButton.setText("Locked");
//        }
    }

//    public void onClickPick(View v) {
//        // Return the character selection back to the main activity
//        PreferencesProxy prefs = new PreferencesProxy(this);
//        String name = getString(CharacterPool.charactersList[mCharacterNum].getStringResourceName());
//        prefs.setCharacter(name);
//        finish();
//    }
}
