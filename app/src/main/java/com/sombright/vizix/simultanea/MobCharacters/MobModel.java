package com.sombright.vizix.simultanea.MobCharacters;

import android.content.Context;

public class MobModel {
    private final int mStringResourceName;
    private final int mImageResource;
    private final int mImageResourceHurt;
    private final int mDeathAnimationId;
    private final int mDeadImageId;
    private final int mImageResourceAttack;
    private final int mStringResourceLore;
    private final int mHeal, mRecovery, mAttack, mDefense;
    private boolean mPlayable;

    public MobModel(int strResName,
                    int imgRes,
                    int imgResHurt,
                    int deathAnimationId, int deadImageId,
                    int imgResAttack,
                    int strResLore,
                    int heal, int recovery, int attack, int defense, boolean playable) {
        mStringResourceName = strResName;
        mImageResource = imgRes;
        mImageResourceHurt = imgResHurt;
        mDeathAnimationId = deathAnimationId;
        mDeadImageId = deadImageId;
        mImageResourceAttack = imgResAttack;
        mStringResourceLore = strResLore;
        mHeal = heal;
        mRecovery = recovery;
        mAttack = attack;
        mDefense = defense;
        mPlayable = playable;


    }

    int getStringResourceName() {
        return mStringResourceName;
    }

    private static String getMobName(Context context, com.sombright.vizix.simultanea.MobCharacters.MobModel character) {
        int resId = character.getStringResourceName();
        return context.getString(resId);
    }

    public String getName(Context context) {
        return getMobName(context, this);
    }

    public int getImageResource() {
        return mImageResource;
    }
    public int getImageResourceHurt() {
        return mImageResourceHurt;
    }
    public int getDeathAnimationId() {
        return mDeathAnimationId;
    }
    public int getDeadImageId() {
        return mDeadImageId;
    }
    public int getImageResourceAttack() {
        return mImageResourceAttack;
    }
    int getStringResourceLore() {
        return mStringResourceLore;
    }
    int getHeal() {
        return mHeal;
    }
    int getRecovery() {
        return mRecovery;
}
    int getAttack() {
        return mAttack;
    }
    int getDefense() {
        return mDefense;
    }
    public boolean isPlayable() { return mPlayable; }
}
