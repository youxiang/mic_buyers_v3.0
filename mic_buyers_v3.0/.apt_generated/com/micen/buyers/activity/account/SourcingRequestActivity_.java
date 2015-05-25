//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.micen.buyers.activity.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import com.focustech.common.widget.viewpagerindictor.UnderlinePageIndicator;
import com.micen.buyers.activity.R.id;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class SourcingRequestActivity_
    extends SourcingRequestActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static SourcingRequestActivity_.IntentBuilder_ intent(Context context) {
        return new SourcingRequestActivity_.IntentBuilder_(context);
    }

    public static SourcingRequestActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new SourcingRequestActivity_.IntentBuilder_(fragment);
    }

    public static SourcingRequestActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new SourcingRequestActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (((SdkVersionHelper.getSdkInt()< 5)&&(keyCode == KeyEvent.KEYCODE_BACK))&&(event.getRepeatCount() == 0)) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        titleRightButton2 = ((ImageView) hasViews.findViewById(id.common_title_right_button2));
        titleText = ((TextView) hasViews.findViewById(id.common_title_name));
        titleRightButton3 = ((ImageView) hasViews.findViewById(id.common_title_right_button3));
        titleRightButton1 = ((ImageView) hasViews.findViewById(id.common_title_right_button1));
        titleLeftButton = ((ImageView) hasViews.findViewById(id.common_title_back_button));
        viewPager = ((ViewPager) hasViews.findViewById(id.vp_sourcing_request));
        tvClosed = ((TextView) hasViews.findViewById(id.tv_closed));
        titleLine = ((UnderlinePageIndicator) hasViews.findViewById(id.vo_title_line));
        tvPending = ((TextView) hasViews.findViewById(id.tv_pending));
        tvRejected = ((TextView) hasViews.findViewById(id.tv_rejected));
        initView();
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;
        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, SourcingRequestActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            fragment_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, SourcingRequestActivity_.class);
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, SourcingRequestActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public SourcingRequestActivity_.IntentBuilder_ flags(int flags) {
            intent_.setFlags(flags);
            return this;
        }

        public void start() {
            context_.startActivity(intent_);
        }

        public void startForResult(int requestCode) {
            if (fragmentSupport_!= null) {
                fragmentSupport_.startActivityForResult(intent_, requestCode);
            } else {
                if (fragment_!= null) {
                    fragment_.startActivityForResult(intent_, requestCode);
                } else {
                    if (context_ instanceof Activity) {
                        ((Activity) context_).startActivityForResult(intent_, requestCode);
                    } else {
                        context_.startActivity(intent_);
                    }
                }
            }
        }

    }

}