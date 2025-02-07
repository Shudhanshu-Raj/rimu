package com.reco1l.ui.fragments;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.edlplan.ui.TriangleEffectView;
import com.reco1l.Game;
import com.reco1l.UI;
import com.reco1l.enums.Screens;
import com.reco1l.tables.AnimationTable;
import com.reco1l.tables.Res;
import com.reco1l.ui.BaseFragment;
import com.reco1l.utils.Animation;
import com.reco1l.view.IconButton;

import com.reco1l.utils.helpers.OnlineHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.nsu.ccfit.zuev.osu.Config;
import ru.nsu.ccfit.zuev.osuplus.R;

// Created by Reco1l on 26/6/22 21:20

public final class TopBar extends BaseFragment {

    public static final TopBar instance = new TopBar();

    public UserBox userBox;

    private final Map<Screens, ArrayList<IconButton>> mButtons;

    private View
            mBody,
            mBackButton;

    private LinearLayout
            mLeftAnchorLayout,
            mButtonsContainer;

    private boolean mIsClosing = false;

    //--------------------------------------------------------------------------------------------//

    public TopBar() {
        super(Screens.Selector, Screens.Summary, Screens.Loader);
        mButtons = new HashMap<>();

        for (Screens screen : Screens.values()) {
            mButtons.put(screen, new ArrayList<>());
        }
    }

    //--------------------------------------------------------------------------------------------//

    @Override
    protected String getPrefix() {
        return "tb";
    }

    @Override
    protected int getLayout() {
        return R.layout.overlay_top_bar;
    }

    @Override
    protected boolean isOverlay() {
        return true;
    }

    @Override
    public int getHeight() {
        return Res.dimen(R.dimen.topBarHeight);
    }

    //--------------------------------------------------------------------------------------------//

    @Override
    protected void onLoad() {
        userBox = new UserBox(this);

        mBody = find("body");
        mBackButton = find("back");
        mButtonsContainer = find("buttons");
        mLeftAnchorLayout = find("container");

        rootView.post(() -> {
            Animation.of(mBody)
                    .fromY(-getHeight())
                    .toY(0)
                    .play(200);

            Game.platform.animate(true, true)
                    .toTopMargin(getHeight())
                    .play(200);
        });


        bindTouch(mBackButton, Game.inputManager::performBack);
        bindTouch(find("settings"), UI.settingsPanel::altShow);
        bindTouch(find("inbox"), UI.notificationCenter::altShow);

        userBox.loadUserData(false);
        handleButtons(Game.engine.getScreen());
    }

    @Override
    protected void onScreenChange(Screens pLastScreen, Screens pNewScreen) {
        if (isAdded()) {

            Animation.of(mLeftAnchorLayout)
                    .toX(-60)
                    .toAlpha(0)
                    .runOnEnd(() -> {
                        if (!mIsClosing) {
                            handleButtons(pNewScreen);

                            Animation.of(mLeftAnchorLayout)
                                    .toX(0)
                                    .toAlpha(1)
                                    .play(200);
                        }
                    })
                    .play(200);
        }
    }

    private void handleButtons(@NonNull Screens pCurrent) {
        mButtonsContainer.removeAllViews();

        if (pCurrent == Screens.Main || pCurrent == Screens.Loader) {
            mBackButton.setVisibility(View.GONE);
        } else {
            mBackButton.setVisibility(View.VISIBLE);
        }

        //noinspection ConstantConditions
        for (IconButton button : mButtons.get(pCurrent)) {
            mButtonsContainer.addView(button);
            bindTouch(button, button.getTouchListener());
        }
    }

    @Override
    public void close() {
        if (isAdded()) {
            mIsClosing = true;

            Animation.of(mBody)
                    .toY(-getHeight())
                    .runOnEnd(() -> {
                        mButtonsContainer.removeAllViews();
                        super.close();
                    })
                    .play(200);

            Game.platform.animate(true, true)
                    .toTopMargin(0)
                    .play(200);
        }
    }

    @Override
    public boolean show() {
        if (Game.engine.getScreen() == Screens.Loader) {
            if (Game.loaderScene.isImmersive()) {
                return false;
            }
        }
        mIsClosing = false;
        return super.show();
    }
    //--------------------------------------------------------------------------------------------//

    // Bind a button to be displayed on the desired screen
    public void bindButton(Screens pScreen, IconButton pButton) {
        if (pButton != null) {
            //noinspection ConstantConditions
            mButtons.get(pScreen).add(pButton);
        }
    }

    //--------------------------------------------------------------------------------------------//

    // TODO [TopBar] Replace this with a custom view
    @Deprecated
    public static class UserBox {

        private final TopBar parent;
        private final ImageView avatar;
        private final TextView rank, name;

        //----------------------------------------------------------------------------------------//

        public UserBox(TopBar parent) {
            this.parent = parent;

            View body = parent.find("userBox");
            rank = parent.find("playerRank");
            name = parent.find("playerName");
            avatar = parent.find("avatar");

            TriangleEffectView triangles = parent.find("userBoxTriangles");
            triangles.setTriangleColor(0xFFFFFFFF);

            parent.bindTouch(body, UI.userProfile::altShow);
        }

        //----------------------------------------------------------------------------------------//

        public void loadUserData(boolean clear) {
            if (!parent.isAdded())
                return;

            AnimationTable.fadeOutIn(avatar, () -> avatar.setImageResource(R.drawable.placeholder_avatar));

            AnimationTable.textChange(rank, Res.str(R.string.top_bar_offline));
            AnimationTable.textChange(name, Config.getLocalUsername());

            if (Game.onlineManager.isStayOnline() && !clear) {
                AnimationTable.textChange(name, Game.onlineManager.getUsername());
                AnimationTable.textChange(rank, "#" + Game.onlineManager.getRank());

                AnimationTable.fadeOutIn(avatar, () -> {
                    if (OnlineHelper.getPlayerAvatar() != null) {
                        avatar.setImageDrawable(OnlineHelper.getPlayerAvatar());
                    }
                });
            }
        }
    }
}
