package com.sombright.vizix.simultanea.MobCharacters.SPBattleLevels;

import com.sombright.vizix.simultanea.MobCharacters.MobModel;
import com.sombright.vizix.simultanea.R;

public class LevelThree{
    static final MobModel[] mobListLevelThree = {
            new com.sombright.vizix.simultanea.MobCharacters.MobModel(
                    R.string.skeleton_name,
                    R.drawable.skeleton_stand,
                    R.drawable.bluepanda_hurt,
                    R.drawable.bluepanda_death, R.drawable.bluepanda_death21,
                    R.drawable.skeleton_attack,
                    R.string.Skeleton_Description,
                    100, 5,30, 10, true),
            new com.sombright.vizix.simultanea.MobCharacters.MobModel(
                    R.string.skeleton_name,
                    R.drawable.skeleton_stand,
                    R.drawable.bluepanda_hurt,
                    R.drawable.bluepanda_death, R.drawable.bluepanda_death21,
                    R.drawable.skeleton_attack,
                    R.string.Skeleton_Description,
                    100, 5,30, 10, true)
    };
    private static final int DEFAULT_MOB_INDEX = 0;
    static com.sombright.vizix.simultanea.MobCharacters.MobModel getDefaultMobLevelThree() {
        return mobListLevelThree[DEFAULT_MOB_INDEX];
    }
}
