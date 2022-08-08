package com.reco1l.utils.interfaces;

import com.reco1l.EngineMirror;
import com.reco1l.ui.data.OnlineHelper;
import com.reco1l.ui.platform.FragmentPlatform;

import ru.nsu.ccfit.zuev.osu.GlobalManager;
import ru.nsu.ccfit.zuev.osu.LibraryManager;
import ru.nsu.ccfit.zuev.osu.MainActivity;
import ru.nsu.ccfit.zuev.osu.PropertiesLibrary;
import ru.nsu.ccfit.zuev.osu.ResourceManager;
import ru.nsu.ccfit.zuev.osu.online.OnlineManager;
import ru.nsu.ccfit.zuev.skins.SkinManager;

// Created by Reco1l on 25/6/22 00:39

public interface IMainClasses {

    // Be sure of call this variables once their classes get initialized.
    // Most of them get initialized once they are called or when the activity is created.
    FragmentPlatform platform = FragmentPlatform.getInstance();
    GlobalManager global = GlobalManager.getInstance();
    MainActivity mActivity = global.getMainActivity();
    LibraryManager library = LibraryManager.getInstance();
    ResourceManager resources = ResourceManager.getInstance();
    OnlineManager online = OnlineManager.getInstance();
    OnlineHelper onlineHelper = OnlineHelper.getInstance();
    EngineMirror engine = (EngineMirror) mActivity.getEngine();
    SkinManager skinManager = SkinManager.getInstance();
    PropertiesLibrary properties = PropertiesLibrary.getInstance();
}
