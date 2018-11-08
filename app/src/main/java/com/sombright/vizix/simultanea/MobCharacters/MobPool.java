package com.sombright.vizix.simultanea.MobCharacters;

import com.sombright.vizix.simultanea.R;

class MobPool {
    static final com.sombright.vizix.simultanea.MobCharacters.MobModel[] mobList = {
            new com.sombright.vizix.simultanea.MobCharacters.MobModel(
                    R.string.skeleton_name,
                    R.drawable.skeleton_stand,
                    R.drawable.bluepanda_hurt,
                    R.drawable.bluepanda_death, R.drawable.bluepanda_death21,
                    R.drawable.skeleton_attack,
                    R.string.Skeleton_Description,
                    30, 10),
            new com.sombright.vizix.simultanea.MobCharacters.MobModel(
                    R.string.goblin_name,
                    R.drawable.goblin_stand,
                    R.drawable.bluepanda_hurt,
                    R.drawable.bluepanda_death, R.drawable.bluepanda_death21,
                    R.drawable.goblin_attack,
                    R.string.Skeleton_Description,
                    30, 10),
            new com.sombright.vizix.simultanea.MobCharacters.MobModel(
                    R.string.slime_name,
                    R.drawable.slime_stand,
                    R.drawable.bluepanda_hurt,
                    R.drawable.slime_death, R.drawable.slime_death22,
                    R.drawable.bluepanda_attack,
                    R.string.Slime_Description,
                    30, 10)
            };
    private static final int DEFAULT_MOB_INDEX = 0;
    static com.sombright.vizix.simultanea.MobCharacters.MobModel getDefaultMob() {
        return mobList[DEFAULT_MOB_INDEX];
    }
}
