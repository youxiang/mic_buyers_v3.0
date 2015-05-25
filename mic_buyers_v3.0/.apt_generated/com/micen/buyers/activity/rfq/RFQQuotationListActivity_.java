//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.micen.buyers.activity.rfq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.focustech.common.widget.pulltorefresh.PullToRefreshListView;
import com.micen.buyers.activity.R.id;
import com.micen.buyers.view.NormalTableLayout;
import com.micen.buyers.view.SearchListProgressBar;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class RFQQuotationListActivity_
    extends RFQQuotationListActivity
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

    public static RFQQuotationListActivity_.IntentBuilder_ intent(Context context) {
        return new RFQQuotationListActivity_.IntentBuilder_(context);
    }

    public static RFQQuotationListActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new RFQQuotationListActivity_.IntentBuilder_(fragment);
    }

    public static RFQQuotationListActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new RFQQuotationListActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        titleRightButton1 = ((ImageView) hasViews.findViewById(id.common_title_right_button1));
        titleLeftButton = ((ImageView) hasViews.findViewById(id.common_title_back_button));
        titleRightButton3 = ((ImageView) hasViews.findViewById(id.common_title_right_button3));
        titleRightButton2 = ((ImageView) hasViews.findViewById(id.common_title_right_button2));
        titleText = ((TextView) hasViews.findViewById(id.common_title_name));
        rfqTitleLayout = ((LinearLayout) hasViews.findViewById(id.mic_quotation_list_rfq_title_layout));
        supplierInfoLayout = ((NormalTableLayout) hasViews.findViewById(id.supplier_info_table));
        quotationDetailLayout = ((RelativeLayout) hasViews.findViewById(id.mic_quotation_detail_layout));
        rfqTitleText = ((TextView) hasViews.findViewById(id.mic_quotation_list_rfq_title));
        companyNameLayout = ((RelativeLayout) hasViews.findViewById(id.quotation_detail_company_layout));
        quotationAllLayout = ((LinearLayout) hasViews.findViewById(id.quotation_all_layout));
        pullToListView = ((PullToRefreshListView) hasViews.findViewById(id.mic_quotation_list_page_list));
        companyNameTv = ((TextView) hasViews.findViewById(id.mic_quotation_detail_company_name));
        progressBar = ((SearchListProgressBar) hasViews.findViewById(id.progressbar_layout));
        initView();
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;
        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, RFQQuotationListActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            fragment_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, RFQQuotationListActivity_.class);
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, RFQQuotationListActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public RFQQuotationListActivity_.IntentBuilder_ flags(int flags) {
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