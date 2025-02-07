package com.reco1l.ui.fragments;

import static android.widget.RelativeLayout.ALIGN_PARENT_END;

import android.view.View;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.edlplan.framework.easing.Easing;
import com.factor.bouncy.BouncyRecyclerView;
import com.reco1l.Game;
import com.reco1l.UI;
import com.reco1l.data.GameNotification;
import com.reco1l.enums.Screens;
import com.reco1l.ui.BaseFragment;
import com.reco1l.utils.Animation;
import com.reco1l.utils.Views;

import com.reco1l.data.adapters.NotificationListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.nsu.ccfit.zuev.osuplus.R;

// Created by Reco1l on 27/6/22 17:17

public final class NotificationCenter extends BaseFragment {

    public static final NotificationCenter instance = new NotificationCenter();

    private final PopupFragment pPopupFragment;
    private final NotificationListAdapter mAdapter;

    private final ArrayList<GameNotification> mNotifications;
    private final Map<String, GameNotification> mNotificationMap;

    private View
            mBody,
            mLayer;

    private TextView
            mEmptyText,
            mCounterText;

    private BouncyRecyclerView mListView;

    //--------------------------------------------------------------------------------------------//

    public NotificationCenter() {
        super();
        mNotifications = new ArrayList<>();
        mNotificationMap = new HashMap<>();
        pPopupFragment = new PopupFragment();
        mAdapter = new NotificationListAdapter(mNotifications);

        closeOnBackgroundClick(true);
    }

    //--------------------------------------------------------------------------------------------//

    @Override
    protected String getPrefix() {
        return "nc";
    }

    @Override
    protected int getLayout() {
        return R.layout.extra_notification_center;
    }

    @Override
    protected boolean isOverlay() {
        return true;
    }

    @Override
    public int getWidth() {
        return dimen(R.dimen.notificationCenterWidth);
    }

    //--------------------------------------------------------------------------------------------//

    @Override
    protected void onLoad() {

        mBody = find("body");
        mLayer = find("layer");
        mListView = find("container");
        mCounterText = find("counter");
        mEmptyText = find("emptyText");

        Game.platform.animate(true, true)
                .toX(-50)
                .play(400);

        Animation.of(rootBackground)
                .fromAlpha(0)
                .toAlpha(1)
                .play(300);

        Animation.of(mLayer)
                .fromX(getWidth())
                .toX(0)
                .interpolate(Easing.OutExpo)
                .play(350);

        Animation.of(mBody)
                .fromX(getWidth())
                .toX(0)
                .interpolate(Easing.OutExpo)
                .delay(50)
                .play(400);

        mListView.setLayoutManager(new LinearLayoutManager(getContext()));
        mListView.setAdapter(mAdapter);

        bindTouch(find("clear"), this::clear);
    }

    @Override
    protected void onUpdate(float pSecElapsed) {
        if (mEmptyText != null) {
            Views.visibility(mNotifications.isEmpty(), mEmptyText);
        }

        if (mCounterText != null) {
            mCounterText.setText("" + mNotifications.size());
        }
    }

    //--------------------------------------------------------------------------------------------//

    public void clear() {
        for (int i = 0; i < mNotifications.size(); ++i) {
            GameNotification n = mNotifications.get(i);

            if (n != null && !n.hasPriority()) {
                remove(n);
            }
        }
    }

    public void add(GameNotification n) {
        if (mNotifications.contains(n)) {
            return;
        }

        if (n.hasPriority()) {
            mNotifications.add(0, n);
        } else {
            mNotifications.add(n);
        }
        mNotificationMap.put(n.getKey(), n);

        if (!n.isSilent() && !isAdded()) {
            pPopupFragment.load(n);
        }

        Game.activity.runOnUiThread(mAdapter::notifyDataSetChanged);
        n.onNotify();
    }

    public void remove(GameNotification n) {
        if (!mNotifications.remove(n)) {
            return;
        }
        mNotificationMap.remove(n.getKey());

        if (pPopupFragment.mNotification == n) {
            pPopupFragment.close();
        }
        Game.activity.runOnUiThread(mAdapter::notifyDataSetChanged);
        n.onDismiss();
    }

    public boolean contains(GameNotification n) {
        return mNotifications.contains(n);
    }

    public GameNotification get(String pKey) {
        for (String key : mNotificationMap.keySet()) {
            if (key.equals(pKey)) {
                return mNotificationMap.get(key);
            }
        }
        return null;
    }

    //--------------------------------------------------------------------------------------------//

    @Override
    public boolean show() {
        pPopupFragment.close();
        return super.show();
    }

    @Override
    public void close() {
        if (!isAdded()) {
            return;
        }

        Game.activity.runOnUiThread(() -> {
            Game.platform.animate(true, true)
                    .toX(0)
                    .play(400);

            Animation.of(mBody)
                    .toX(dimen(R.dimen.notificationCenterWidth))
                    .interpolate(Easing.InExpo)
                    .runOnStart(() ->
                            Animation.of(rootBackground)
                                    .toAlpha(0)
                                    .play(300)
                    )
                    .play(350);

            Animation.of(mLayer)
                    .toX(dimen(R.dimen.notificationCenterWidth))
                    .runOnEnd(super::close)
                    .interpolate(Easing.InExpo)
                    .delay(50)
                    .play(400);
        });
    }

    //--------------------------------------------------------------------------------------------/

    public static class PopupFragment extends BaseFragment {

        private GameNotification mNotification;
        private GameNotification.Holder mHolder;

        //----------------------------------------------------------------------------------------//

        @Override
        protected String getPrefix() {
            return "n";
        }

        @Override
        protected int getLayout() {
            return R.layout.item_notification;
        }

        @Override
        protected long getCloseTime() {
            return 8000;
        }

        @Override
        protected boolean getConditionToShow() {
            return Game.engine.getScreen() != Screens.Game;
        }

        //----------------------------------------------------------------------------------------//

        private void load(GameNotification n) {
            mNotification = n;

            if (isAdded()) {
                resetCloseTimer();

                Animation.of(rootView)
                        .runOnEnd(this::bind)
                        .toY(-50)
                        .toAlpha(0)
                        .play(150);
                return;
            }
            show();
        }

        //----------------------------------------------------------------------------------------//

        @Override
        protected void onLoad() {
            int xs = dimen(R.dimen.XS);

            rootView.setPadding(xs, xs, xs, xs);
            rootView.setElevation(dimen(R.dimen.XXL));
            bind();
        }

        private void bind() {
            mHolder = mNotification.build(this.rootView);

            LayoutParams params = (LayoutParams) mHolder.body.getLayoutParams();

            params.width = dimen(R.dimen.popupNotificationWidth);
            params.addRule(ALIGN_PARENT_END);
            mHolder.body.requestLayout();

            if (mHolder != null) {
                unbindTouchHandlers();

                bindTouch(mHolder.closeButton, this::close);
                bindTouch(mHolder.body, UI.notificationCenter::show);

                Animation.of(rootView)
                        .fromY(-50)
                        .toY(0)
                        .fromAlpha(0)
                        .toAlpha(1)
                        .play(150);
            }
        }

        //----------------------------------------------------------------------------------------//

        @Override
        public void close() {
            if (isAdded()) {
                Animation.of(rootView)
                        .runOnEnd(super::close)
                        .toY(-50)
                        .toAlpha(0)
                        .play(150);
            }
        }
    }
}
