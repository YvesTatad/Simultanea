package com.sombright.vizix.simultanea;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.sombright.vizix.simultanea.ConnectionsActivity.Endpoint;
import com.sombright.vizix.simultanea.MobCharacters.MobModel;
import com.sombright.vizix.simultanea.MobCharacters.MobPool;
import com.sombright.vizix.simultanea.MobCharacters.SPBattleLevels.LevelOne;
import com.sombright.vizix.simultanea.MobCharacters.SPBattleLevels.LevelThree;
import com.sombright.vizix.simultanea.MobCharacters.SPBattleLevels.LevelTwo;

import java.util.UUID;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Player {
    private static final String TAG = "Player";
    static final int COMBAT_MODE_NONE = 0;
    static final int COMBAT_MODE_ATTACK = 1;
    static final int COMBAT_MODE_DEFEND = 2;
    static final int COMBAT_MODE_HEAL = 3;

    static final int ANIMATION_NORMAL = 0;
    static final int ANIMATION_ATTACK = 1;
    static final int ANIMATION_HURT = 2;
    static final int ANIMATION_DYING = 3;

    private final Context mContext;
    private Endpoint mEndpoint;
    private String mUniqueID;
    private String mName;
    private MobModel mMob;
    private Character mCharacter;
    private AnimationDrawable mAnimation, mAnimationAttack, mAnimationHurt, mAnimationDying;
    private int mCurrentAnimation;
    private int mHealth;
    private boolean mAnswered;
    private int mPoints;
    private int mCombatMode;

    Player(Context context) {
        mContext = context;
        mUniqueID = UUID.randomUUID().toString();
        mHealth = 100;
        mAnswered = false;
        mPoints = 0;
        mCombatMode = COMBAT_MODE_NONE;
        mCurrentAnimation = ANIMATION_NORMAL;
    }

    Endpoint getEndpoint() {
        return mEndpoint;
    }

    void setEndpoint(Endpoint endpoint) {
        mEndpoint = endpoint;
    }

    String getUniqueID() {
        return mUniqueID;
    }

    void setUniqueID(String uniqueID) {
        mUniqueID = uniqueID;
    }

    public @NonNull String getName() {
        String name = mName;
        if (name != null) {
            return name;
        }
        name = getCharacterName();
        if (name != null) {
            return name;
        }
        name = getMobName();
        if (name != null) {
            return name;
        }
        name = getMobsLevelOneName();
        if (name != null) {
            return name;
        }
        name = getMobsLevelTwoName();
        if (name != null) {
            return name;
        }
        name = getMobsLevelThreeName();
        if (name != null) {
            return name;
        }
        return "unnamed";
    }

    public void setName(String name) {
        mName = name;
    }

    public Character getCharacter() {
        return mCharacter;
    }

    public void setCharacter(String name) {
        for (Character character : CharacterPool.charactersList) {
            if (character.getName(mContext).equals(name)) {
                mCharacter = character;
                return;
            }
        }
        if (mCharacter == null) {
            Log.e(TAG, "Unknown character " + name + ". Using default instead.");
            mCharacter = CharacterPool.getDefaultCharacter();
        }
    }

    public String getCharacterName() {
        return mCharacter.getName(mContext);
    }

    public MobModel getMob() {
        return mMob;
    }

    public void setMob(String name) {
        for (MobModel mob : MobPool.mobList) {
            if (mob.getName(mContext).equals(name)) {
                mMob = mob;
                return;
            }
        }
        if (mMob == null) {
            Log.e(TAG, "Unknown mob " + name + ". Using default instead.");
            mMob = MobPool.getDefaultMob();
        }
    }

    public String getMobName() {
        return mMob.getName(mContext);
    }

    public MobModel getMobsLevelOne() {
        return mMob;
    }

    public void setMobsLevelOne(String name) {
        for (MobModel mob : LevelOne.mobListLevelOne) {
            if (mob.getName(mContext).equals(name)) {
                mMob = mob;
                return;
            }
        }
        if (mMob == null) {
            Log.e(TAG, "Unknown mob " + name + ". Using default instead.");
            mMob = LevelOne.getDefaultMobLevelOne();
        }
    }

    public String getMobsLevelOneName() {
        return mMob.getName(mContext);
    }

    public MobModel getMobsLevelTwo() {
        return mMob;
    }

    public void setMobsLevelTwo(String name) {
        for (MobModel mob : LevelTwo.mobListLevelTwo) {
            if (mob.getName(mContext).equals(name)) {
                mMob = mob;
                return;
            }
        }
        if (mMob == null) {
            Log.e(TAG, "Unknown mob " + name + ". Using default instead.");
            mMob = MobPool.getDefaultMob();
        }
    }

    public String getMobsLevelTwoName() {
        return mMob.getName(mContext);
    }

    public MobModel getMobsLevelThree() {
        return mMob;
    }

    public void setMobsLevelThree(String name) {
        for (MobModel mob : LevelThree.mobListLevelThree) {
            if (mob.getName(mContext).equals(name)) {
                mMob = mob;
                return;
            }
        }
        if (mMob == null) {
            Log.e(TAG, "Unknown mob " + name + ". Using default instead.");
            mMob = MobPool.getDefaultMob();
        }
    }

    public String getMobsLevelThreeName() {
        return mMob.getName(mContext);
    }

    public int getHealth() {
        return mHealth;
    }

    void setHealth(int health) { mHealth = health;
    }

    public boolean hasAnswered() {
        return mAnswered;
    }

    public void setAnswered(boolean answered) {
        mAnswered = answered;
    }

    public int getPoints() {
        return mPoints;
    }

    public void setPoints(int points) {
        mPoints = points;
    }

    public AnimationDrawable getAnimation() {
        if (mMob != null) {
            // One of the other players
            switch (mCurrentAnimation) {
                case ANIMATION_NORMAL:
                    if (mAnimation == null) {
                        mAnimation = (AnimationDrawable) ContextCompat.getDrawable(mContext, mMob.getImageResource());
                    }
                    return mAnimation;
                case ANIMATION_ATTACK:
                    if (mAnimationAttack == null) {
                        mAnimationAttack = (AnimationDrawable) ContextCompat.getDrawable(mContext, mMob.getImageResourceAttack());
                    }
                    return mAnimationAttack;
                case ANIMATION_HURT:
                    if (mAnimationHurt == null) {
                        mAnimationHurt = (AnimationDrawable) ContextCompat.getDrawable(mContext, mMob.getImageResourceHurt());
                    }
                    return mAnimationHurt;
                case ANIMATION_DYING:
                    if (mAnimationDying == null) {
                        mAnimationDying = (AnimationDrawable) ContextCompat.getDrawable(mContext, mMob.getDeathAnimationId());
                    }
                    return mAnimationDying;
            }
        } else if (mCharacter != null) {
            // The local player
            switch (mCurrentAnimation) {
                case ANIMATION_NORMAL:
                    if (mAnimation == null) {
                        mAnimation = (AnimationDrawable) ContextCompat.getDrawable(mContext, mCharacter.getImageResource());
                    }
                    return mAnimation;
                case ANIMATION_ATTACK:
                    if (mAnimationAttack == null) {
                        mAnimationAttack = (AnimationDrawable) ContextCompat.getDrawable(mContext, mCharacter.getImageResourceAttack());
                    }
                    return mAnimationAttack;
                case ANIMATION_HURT:
                    if (mAnimationHurt == null) {
                        mAnimationHurt = (AnimationDrawable) ContextCompat.getDrawable(mContext, mCharacter.getImageResourceHurt());
                    }
                    return mAnimationHurt;
                case ANIMATION_DYING:
                    if (mAnimationDying == null) {
                        mAnimationDying = (AnimationDrawable) ContextCompat.getDrawable(mContext, mCharacter.getDeathAnimationId());
                    }
                    return mAnimationDying;
            }
        }

        Log.wtf(TAG, "No animation?");
        return null;
    }

    public boolean animationInProgress() {
        return mCurrentAnimation != ANIMATION_NORMAL;
    }

    public void setAnimation(int a) {
        mCurrentAnimation = a;
    }

    int getCombatMode() {
        return mCombatMode;
    }

    void setCombatMode(int mode) {
        switch (mode) {
            case COMBAT_MODE_ATTACK:
                mCombatMode = mode;
                break;
            case COMBAT_MODE_DEFEND:
                mCombatMode = mode;
                break;
            case COMBAT_MODE_HEAL:
                mCombatMode = mode;
                mHealth = min(mHealth + mCharacter.getHeal(), 100);
                break;
            default:
                mCombatMode = COMBAT_MODE_NONE;
        }
    }

    void attack(Player opponent) {
        int damage = mCharacter.getAttack() - opponent.getCharacter().getDefense();
        if (opponent.getCombatMode() == COMBAT_MODE_DEFEND) {
            damage = damage / 2;
        }
        Log.d(TAG, "Player " + mName + " attacking " + opponent.mCharacter.getName(mContext) + " with damage=" + damage + " on health=" + opponent.getHealth());
        opponent.setHealth(max(0, opponent.getHealth() - damage));
    }

    GameMessage getPlayerDetails() {
        GameMessage msg = new GameMessage();
        msg.setType(GameMessage.GAME_MESSAGE_TYPE_PLAYER_INFO);
        msg.playerInfo.uniqueId = mUniqueID;
        msg.playerInfo.name = mName;
        msg.playerInfo.character = mCharacter.getName(mContext);
        msg.playerInfo.health = mHealth;
        msg.playerInfo.points = mPoints;
        msg.playerInfo.battle = mCombatMode;
        return msg;
    }

    GameMessage getMobDetails() {
        GameMessage msg = new GameMessage();
        msg.setType(GameMessage.GAME_MESSAGE_TYPE_PLAYER_INFO);
        msg.playerInfo.uniqueId = mUniqueID;
        msg.playerInfo.name = mName;
        msg.playerInfo.character = mMob.getName(mContext);
        msg.playerInfo.health = mHealth;
        msg.playerInfo.points = mPoints;
        msg.playerInfo.battle = mCombatMode;
        return msg;
    }

    void setPlayerDetails(GameMessage msg) {
        setUniqueID(msg.playerInfo.uniqueId);
        setName(msg.playerInfo.name);
        setCharacter("invalid");
        setMob(msg.playerInfo.character);
        setMob(msg.playerInfo.character);
        setHealth(msg.playerInfo.health);
        setPoints(msg.playerInfo.points);
        setCombatMode(msg.playerInfo.battle);
    }

    void setMobDetails(GameMessage msg) {
        setUniqueID(msg.playerInfo.uniqueId);
        setName(msg.playerInfo.name);
        setCharacter(msg.playerInfo.character);
        setMob(msg.playerInfo.character);
        setHealth(msg.playerInfo.health);
        setPoints(msg.playerInfo.points);
        setCombatMode(msg.playerInfo.battle);
    }
}