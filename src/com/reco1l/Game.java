package com.reco1l;

import androidx.annotation.Nullable;

import com.reco1l.management.BeatmapCollection;
import com.reco1l.management.BitmapManager;
import com.reco1l.management.BoardManager;
import com.reco1l.management.InputManager;
import com.reco1l.management.ModManager;
import com.reco1l.management.MusicManager;
import com.reco1l.management.TimingWrapper;
import com.reco1l.scenes.LoaderScene;
import com.reco1l.scenes.SelectorScene;
import com.reco1l.scenes.MainScene;
import com.reco1l.scenes.SummaryScene;
import com.reco1l.interfaces.IReferences;
import com.reco1l.ui.FragmentPlatform;

import ru.nsu.ccfit.zuev.audio.serviceAudio.SongService;
import ru.nsu.ccfit.zuev.osu.MainActivity;
import ru.nsu.ccfit.zuev.osu.game.GameScene;

// Created by Reco1l on 26/9/22 19:10

public final class Game implements IReferences {

    public static final GameEngine engine = GameEngine.instance;
    public static final MainActivity activity = MainActivity.instance;
    public static final FragmentPlatform platform = FragmentPlatform.instance;

    public static final ModManager modManager = ModManager.instance;
    public static final BoardManager boardManager = BoardManager.instance;
    public static final MusicManager musicManager = MusicManager.instance;
    public static final InputManager inputManager = InputManager.instance;
    public static final BitmapManager bitmapManager = BitmapManager.instance;
    public static final TimingWrapper timingWrapper = TimingWrapper.instance;
    public static final BeatmapCollection beatmapCollection = BeatmapCollection.instance;

    public static final GameScene gameScene = GameScene.instance;
    public static final MainScene mainScene = MainScene.instance;
    public static final LoaderScene loaderScene = LoaderScene.instance;
    public static final SummaryScene summaryScene = SummaryScene.instance;
    public static final SelectorScene selectorScene = SelectorScene.instance;

    @Nullable
    public static SongService songService;
}
