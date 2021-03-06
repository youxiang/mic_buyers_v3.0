//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.micen.buyers.activity.searchresult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.micen.buyers.activity.R.array;
import com.micen.buyers.activity.R.id;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class SearchActivity_
    extends SearchActivity
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
        Resources resources_ = this.getResources();
        searchTypes = resources_.getStringArray(array.search_type);
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

    public static SearchActivity_.IntentBuilder_ intent(Context context) {
        return new SearchActivity_.IntentBuilder_(context);
    }

    public static SearchActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new SearchActivity_.IntentBuilder_(fragment);
    }

    public static SearchActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new SearchActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        titleRightButton1 = ((ImageView) hasViews.findViewById(id.common_title_right_button1));
        titleRightButton2 = ((ImageView) hasViews.findViewById(id.common_title_right_button2));
        titleLeftButton = ((ImageView) hasViews.findViewById(id.common_title_back_button));
        titleRightButton3 = ((ImageView) hasViews.findViewById(id.common_title_right_button3));
        titleText = ((TextView) hasViews.findViewById(id.common_title_name));
        searchButton = ((TextView) hasViews.findViewById(id.search_btn));
        recentSearchList = ((ListView) hasViews.findViewById(id.search_recent_list));
        clearImage = ((ImageView) hasViews.findViewById(id.clear_keyword));
        suggestLayout = ((LinearLayout) hasViews.findViewById(id.search_suggest_layout));
        recentLayout = ((LinearLayout) hasViews.findViewById(id.search_recent_layout));
        suggestList = ((ListView) hasViews.findViewById(id.search_suggest_list));
        searchLayout = ((RelativeLayout) hasViews.findViewById(id.search_title_layout));
        searchTypeTx = ((TextView) hasViews.findViewById(id.search_type));
        recentCategoryLayout = ((LinearLayout) hasViews.findViewById(id.recent_layout));
        searchEditText = ((EditText) hasViews.findViewById(id.keyword_edit_text));
        titleLeftButton = ((ImageView) hasViews.findViewById(id.title_back_button));
        initData();
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;
        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, SearchActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            fragment_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, SearchActivity_.class);
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, SearchActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public SearchActivity_.IntentBuilder_ flags(int flags) {
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
