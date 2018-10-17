package com.sombright.vizix.simultanea;

class CharacterPool {
    static final Character[] charactersList = {
            new Character(
                    R.string.bluepanda_name,
                    R.drawable.bluepanda,
                    R.drawable.bluepanda_hurt,
                    R.drawable.bluepanda_death, R.drawable.bluepanda_death21,
                    R.drawable.bluepanda_attack,
                    R.string.BluePanda_Lore,
                    100, 5, 30, 10 , true),
            new Character(
                    R.string.dueliest_name,
                    R.drawable.duel,
                    R.drawable.duel_hurt,
                    R.drawable.duel_death, R.drawable.duel_death10,
                    R.drawable.duel_attack,
                    R.string.Duelist_Lore,
                    80, 10, 50, 7, true),
            new Character(
                    R.string.pumpkin_name,
                    R.drawable.pump,
                    R.drawable.pump_hurt,
                    R.drawable.pump_death, R.drawable.pump_death34,
                    R.drawable.pump_attack,
                    R.string.Pumpkin_Lore,
                    70, 10, 15, 10, true),
            new Character(R.string.warrior_name,
                    R.drawable.war,
                    R.drawable.war_hurt,
                    R.drawable.war_death, R.drawable.war_death7,
                    R.drawable.war_attack,
                    R.string.Warrior_Lore,
                    100, 5, 30, 13, true),
            new Character(R.string.cyclops_name,
                    R.drawable.cyclops,
                    R.drawable.war_hurt,
                    R.drawable.war_death, R.drawable.war_death7,
                    R.drawable.war_attack,
                    R.string.Cyclops_Lore,
                    100, 5, 30, 13,false),
            new Character(R.string.minotaur_name,
                    R.drawable.minotaur,
                    R.drawable.war_hurt,
                    R.drawable.war_death, R.drawable.war_death7,
                    R.drawable.war_attack,
                    R.string.Minotaur_Lore,
                    100, 5, 30, 13, false),
            new Character(R.string.redpanda_name,
                    R.drawable.redpanda,
                    R.drawable.war_hurt,
                    R.drawable.war_death, R.drawable.war_death7,
                    R.drawable.war_attack,
                    R.string.RedPanda_Lore,
                    100, 5, 30, 13,true),
            new Character(R.string.siren_name,
                    R.drawable.siren,
                    R.drawable.war_hurt,
                    R.drawable.war_death, R.drawable.war_death7,
                    R.drawable.war_attack,
                    R.string.RedPanda_Lore,
                    100, 5, 30, 13, false)
    };
    private static final int DEFAULT_CHARACTER_INDEX = 0;
    static Character getDefaultCharacter() {
        return charactersList[DEFAULT_CHARACTER_INDEX];
    }
}
