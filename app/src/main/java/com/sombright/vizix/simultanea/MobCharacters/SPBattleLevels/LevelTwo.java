package com.sombright.vizix.simultanea.MobCharacters.SPBattleLevels;

import com.sombright.vizix.simultanea.MobCharacters.MobModel;
import com.sombright.vizix.simultanea.R;

public class LevelTwo{
    public static final MobModel[] mobListLevelTwo = {
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
                    100, 5,30, 10, true)
    };
    private static final int DEFAULT_MOB_INDEX = 0;
    public static com.sombright.vizix.simultanea.MobCharacters.MobModel getDefaultMobLevelTwo() {
        return mobListLevelTwo[DEFAULT_MOB_INDEX];
    }
}