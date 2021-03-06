package com.sombright.vizix.simultanea;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class is just a thin layer to hide the details of the SharedPreferences api
 */

class PreferencesProxy {

    // keys
    private static final String PREF_MULTI_PLAYER_MODE = "multiPlayerMode";
    private static final String PREF_MULTI_PLAYER_MASTER = "multiPlayerMaster";
    private static final String PREF_MULTI_PLAYER_ALIAS = "multiPlayerAlias";
    private static final String PREF_CHARACTER = "character";
    private static final String PREF_USE_OPEN_TRIVIA_DB = "useOpenTriviaDatabase";
    private static final String PREF_HIGH_SCORE = "highScore";

    // Default values
    private static final boolean DEFAULT_MULTI_PLAYER_MODE = false;
    private static final boolean DEFAULT_MULTI_PLAYER_MASTER = false;
    private static final String DEFAULT_MULTI_PLAYER_ALIAS = "Player";
    private static final String DEFAULT_CHARACTER = null;
    private static final boolean DEFAULT_USE_OPEN_TRIVIA_DB = false;

    private Context mContext;
    private int currentGameLevel = 0;


    public int getCurrentGameLevel() {
        return currentGameLevel;
    }

    public void setCurrentGameLevel(int currentGameLevel) {
        this.currentGameLevel = currentGameLevel;
    }

    private SharedPreferences mPrefs;

    PreferencesProxy(Context context) {
        mContext = context;
        String name = mContext.getString(R.string.preference_file_key);
        mPrefs = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    boolean isMultiPlayerMode() {
        return mPrefs.getBoolean(PREF_MULTI_PLAYER_MODE, DEFAULT_MULTI_PLAYER_MODE);
    }

    void setMultiplayerMode(boolean enabled) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(PREF_MULTI_PLAYER_MODE, enabled);
        editor.apply();
    }

    boolean isMultiPlayerMaster() {
        return mPrefs.getBoolean(PREF_MULTI_PLAYER_MASTER, DEFAULT_MULTI_PLAYER_MASTER);
    }

    void setMultiplayerMaster(boolean isMaster) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(PREF_MULTI_PLAYER_MASTER, isMaster);
        editor.apply();
    }

    String getMultiPlayerAlias() {
        return mPrefs.getString(PREF_MULTI_PLAYER_ALIAS, DEFAULT_MULTI_PLAYER_ALIAS);
    }

    void setMultiplayerAlias(String alias) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_MULTI_PLAYER_ALIAS, alias);
        editor.apply();
    }

    String getCharacter() {
        String character = mPrefs.getString(PREF_CHARACTER, null);
        if (character == null) {
            return mContext.getString(CharacterPool.charactersList[0].getStringResourceName());
        }

        // Make sure the character name exists
        for (Character c: CharacterPool.charactersList) {
            String name = mContext.getString(c.getStringResourceName());
            if (name.equals(character)) {
                return character;
            }
        }

        return mContext.getString(CharacterPool.charactersList[0].getStringResourceName());
    }

    void setCharacter(String alias) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_CHARACTER, alias);
        editor.apply();
    }

    boolean shouldUseOpenTriviaDatabase() {
        return mPrefs.getBoolean(PREF_USE_OPEN_TRIVIA_DB, DEFAULT_USE_OPEN_TRIVIA_DB);
    }

    void setUseOpenTriviaDatabase(boolean enabled) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(PREF_USE_OPEN_TRIVIA_DB, enabled);
        editor.apply();
    }

    int getHighScore() {
        return mPrefs.getInt(PREF_HIGH_SCORE, 0);
    }

    void setHighScore(int points) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt(PREF_HIGH_SCORE, points);
        editor.apply();
    }

}