//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.micen.buyers.view.home;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.focustech.common.widget.HorizontalListView;
import com.micen.buyers.activity.R.layout;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class HomeTopFragment_
    extends HomeTopFragment
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    private View contentView_;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    public View findViewById(int id) {
        if (contentView_ == null) {
            return null;
        }
        return contentView_.findViewById(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView_ = super.onCreateView(inflater, container, savedInstanceState);
        if (contentView_ == null) {
            contentView_ = inflater.inflate(layout.home_top_layout, container, false);
        }
        return contentView_;
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static HomeTopFragment_.FragmentBuilder_ builder() {
        return new HomeTopFragment_.FragmentBuilder_();
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        specialPopularLayout = ((LinearLayout) hasViews.findViewById(com.micen.buyers.activity.R.id.home_special_popular_layout));
        quotationLayout = ((LinearLayout) hasViews.findViewById(com.micen.buyers.activity.R.id.mic_home_quotation));
        messageUnreadText = ((TextView) hasViews.findViewById(com.micen.buyers.activity.R.id.tv_home_message_unread));
        favouriteLayout = ((LinearLayout) hasViews.findViewById(com.micen.buyers.activity.R.id.mic_home_favourite));
        upperBannerLayout = ((LinearLayout) hasViews.findViewById(com.micen.buyers.activity.R.id.home_special_upper_banner_layout));
        inboxLayout = ((LinearLayout) hasViews.findViewById(com.micen.buyers.activity.R.id.mic_home_inbox));
        refreshLayout = ((SwipeRefreshLayout) hasViews.findViewById(com.micen.buyers.activity.R.id.mic_home_pull_refresh_scrollview));
        specialDMissLayout = ((LinearLayout) hasViews.findViewById(com.micen.buyers.activity.R.id.home_special_dmiss_layout));
        horizontalListView = ((HorizontalListView) hasViews.findViewById(com.micen.buyers.activity.R.id.home_recommend_list));
        recommendLayout = ((LinearLayout) hasViews.findViewById(com.micen.buyers.activity.R.id.mic_home_recommend_layout));
        initView();
    }

    public static class FragmentBuilder_ {

        private Bundle args_;

        private FragmentBuilder_() {
            args_ = new Bundle();
        }

        public HomeTopFragment build() {
            HomeTopFragment_ fragment_ = new HomeTopFragment_();
            fragment_.setArguments(args_);
            return fragment_;
        }

    }

}