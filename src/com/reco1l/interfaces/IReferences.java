package com.reco1l.interfaces;

import com.reco1l.GameEngine;
import com.reco1l.scenes.LoaderScene;
import com.reco1l.scenes.MainScene;
import com.reco1l.scenes.SelectorScene;
import com.reco1l.scenes.SummaryScene;
import com.reco1l.ui.FragmentPlatform;
import com.reco1l.management.BeatmapCollection;
import com.reco1l.management.TimingWrapper;
import com.reco1l.management.BitmapManager;
import com.reco1l.management.MusicManager;

import ru.nsu.ccfit.zuev.osu.GlobalManager;
import ru.nsu.ccfit.zuev.osu.LibraryManager;
import ru.nsu.ccfit.zuev.osu.MainActivity;
import ru.nsu.ccfit.zuev.osu.PropertiesLibrary;
import ru.nsu.ccfit.zuev.osu.ResourceManager;
import ru.nsu.ccfit.zuev.osu.game.GameScene;
import ru.nsu.ccfit.zuev.osu.menu.ModMenu;
import ru.nsu.ccfit.zuev.osu.online.OnlineManager;
import ru.nsu.ccfit.zuev.osu.online.OnlineScoring;
import ru.nsu.ccfit.zuev.osu.scoring.ScoreLibrary;
import ru.nsu.ccfit.zuev.skins.SkinManager;

// Created by Reco1l on 25/6/22 00:39

public interface IReferences {

    // OLD
    ModMenu modMenu = ModMenu.getInstance();

    SkinManager skinManager = SkinManager.getInstance();
    GlobalManager globalManager = GlobalManager.getInstance();
    LibraryManager libraryManager = LibraryManager.getInstance();
    ResourceManager resourcesManager = ResourceManager.getInstance();

    ScoreLibrary scoreLibrary = ScoreLibrary.getInstance();
    PropertiesLibrary propertiesLibrary = PropertiesLibrary.getInstance();

    OnlineManager onlineManager = OnlineManager.getInstance();
    OnlineScoring onlineScoring = OnlineScoring.getInstance();
}
