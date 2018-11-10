package com.sombright.vizix.simultanea.MobCharacters;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.sombright.vizix.simultanea.R;

class MobPool implements ListAdapter {
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

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
