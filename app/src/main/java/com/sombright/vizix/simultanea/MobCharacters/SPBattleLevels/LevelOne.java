package com.sombright.vizix.simultanea.MobCharacters.SPBattleLevels;

import com.sombright.vizix.simultanea.MobCharacters.MobModel;
import com.sombright.vizix.simultanea.R;

public class LevelOne{
    public static final MobModel[] mobListLevelOne = {
            new com.sombright.vizix.simultanea.MobCharacters.MobModel(
                    R.string.goblin_name,
                    R.drawable.goblin_stand,
                    R.drawable.goblin_hurt,
                    R.drawable.goblin_death, R.drawable.goblin_death22,
                    R.drawable.goblin_attack,
                    R.string.Goblin_Description,
                    100, 5,30, 10, true),
            new com.sombright.vizix.simultanea.MobCharacters.MobModel(
                    R.string.goblin_name,
                    R.drawable.goblin_stand,
                    R.drawable.goblin_hurt,
                    R.drawable.goblin_death, R.drawable.goblin_death22,
                    R.drawable.goblin_attack,
                    R.string.Goblin_Description,
                    100, 5,30, 10, true)
    };
    private static final int DEFAULT_MOB_INDEX = 0;
    public static com.sombright.vizix.simultanea.MobCharacters.MobModel getDefaultMobLevelOne() {
        return mobListLevelOne[DEFAULT_MOB_INDEX];
    }
}
