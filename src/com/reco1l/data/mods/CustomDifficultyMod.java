package com.reco1l.data.mods;

// Created by Reco1l on 21/12/2022, 09:44

import com.reco1l.Game;
import com.reco1l.management.ModProperty;
import com.reco1l.preference.GameSekBarPreference;

import ru.nsu.ccfit.zuev.osu.TrackInfo;
import ru.nsu.ccfit.zuev.osuplus.R;

public class CustomDifficultyMod extends ModWrapper {


    //--------------------------------------------------------------------------------------------//

    @Override
    public String getName() {
        return "Custom Difficulty";
    }

    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public Properties createProperties() {
        return new Properties();
    }
    //--------------------------------------------------------------------------------------------//

    @Override
    public void onSelect(boolean isEnabled) {
        super.onSelect(isEnabled);
        Game.modMenu.setEnableForceAR(isEnabled);
    }

    //--------------------------------------------------------------------------------------------//

    public static class Properties extends ModWrapper.Properties {

        //----------------------------------------------------------------------------------------//

        @Override
        protected int getPreferenceXML() {
            return R.xml.mod_custom_difficulty;
        }

        //----------------------------------------------------------------------------------------//

        @Override
        protected void onLoad() {
            super.onLoad();
            GameSekBarPreference AR = find("mod_custom_ar");

            TrackInfo track = Game.musicManager.getTrack();

            AR.setValueFormatter(value -> "" + value / (float) 10);
            AR.setDefaultValue((int) (track.getApproachRate() * 10));
            AR.setMax(125);
            AR.setMin(1);

            float currentValue = (float) getProperty(ModProperty.CustomDiff_AR, track.getApproachRate());
            AR.setValue((int) (currentValue * 10));

            AR.setOnPreferenceChangeListener((p, v) -> {
                setProperty(ModProperty.CustomDiff_AR, ((int) v) / (float) 10);
                Game.modMenu.setForceAR(((int) v) / (float) 10);
                return true;
            });
        }
    }

}
