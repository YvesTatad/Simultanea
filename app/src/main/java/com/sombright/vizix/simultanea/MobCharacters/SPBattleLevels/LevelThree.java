package com.sombright.vizix.simultanea.MobCharacters.SPBattleLevels;

import com.sombright.vizix.simultanea.MobCharacters.MobModel;
import com.sombright.vizix.simultanea.R;

public class LevelThree{
    public static final MobModel[] mobListLevelThree = {
            new com.sombright.vizix.simultanea.MobCharacters.MobModel(
                    R.string.skeleton_name,
                    R.drawable.skeleton_stand,
                    R.drawable.skeleton_hurt,
                    R.drawable.skeleton_death, R.drawable.skeleton_death15,
                    R.drawable.skeleton_attack,
                    R.string.Skeleton_Description,
                    100, 5,30, 10, true),
            new com.sombright.vizix.simultanea.MobCharacters.MobModel(
                    R.string.skeleton_name,
                    R.drawable.skeleton_stand,
                    R.drawable.skeleton_hurt,
                    R.drawable.skeleton_death, R.drawable.skeleton_death15,
                    R.drawable.skeleton_attack,
                    R.string.Skeleton_Description,
                    100, 5,30, 10, true)
    };
    private static final int DEFAULT_MOB_INDEX = 0;
    public static com.sombright.vizix.simultanea.MobCharacters.MobModel getDefaultMobLevelThree() {
        return mobListLevelThree[DEFAULT_MOB_INDEX];
    }
}
