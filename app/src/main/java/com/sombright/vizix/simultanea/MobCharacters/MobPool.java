package com.sombright.vizix.simultanea.MobCharacters;

import com.sombright.vizix.simultanea.R;

public class MobPool{
    public static final MobModel[] mobList = {
            new com.sombright.vizix.simultanea.MobCharacters.MobModel(
                    R.string.goblin_name,
                    R.drawable.goblin_stand,
                    R.drawable.bluepanda_hurt,
                    R.drawable.bluepanda_death, R.drawable.bluepanda_death21,
                    R.drawable.goblin_attack,
                    R.string.Goblin_Description,
                    100, 5,30, 10, true),
            new com.sombright.vizix.simultanea.MobCharacters.MobModel(
                    R.string.skeleton_name,
                    R.drawable.skeleton_stand,
                    R.drawable.bluepanda_hurt,
                    R.drawable.bluepanda_death, R.drawable.bluepanda_death21,
                    R.drawable.skeleton_attack,
                    R.string.Skeleton_Description,
                    100, 5,30, 10, true),
            new com.sombright.vizix.simultanea.MobCharacters.MobModel(
                    R.string.slime_name,
                    R.drawable.slime_stand,
                    R.drawable.bluepanda_hurt,
                    R.drawable.slime_death, R.drawable.slime_death22,
                    R.drawable.bluepanda_attack,
                    R.string.Slime_Description,
                    100, 5,30, 10, true)
            };
    private static final int DEFAULT_MOB_INDEX = 0;
    public static MobModel getDefaultMob() {
        return mobList[DEFAULT_MOB_INDEX];
    }
}
