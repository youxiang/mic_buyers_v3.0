//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations 3.0.1.
//


package com.micen.buyers.activity.account.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.focustech.common.widget.associatemail.MailBoxAssociateView;
import com.micen.buyers.activity.R.array;
import com.micen.buyers.activity.R.id;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class RegisterActivity_
    extends RegisterActivity
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
        recommendMailBox = resources_.getStringArray(array.recommend_mailbox);
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

    public static RegisterActivity_.IntentBuilder_ intent(Context context) {
        return new RegisterActivity_.IntentBuilder_(context);
    }

    public static RegisterActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new RegisterActivity_.IntentBuilder_(fragment);
    }

    public static RegisterActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new RegisterActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        emailInput = ((MailBoxAssociateView) hasViews.findViewById(id.associate_email_input));
        titleRightButton2 = ((ImageView) hasViews.findViewById(id.common_title_right_button2));
        titleText = ((TextView) hasViews.findViewById(id.common_title_name));
        titleLeftButton = ((ImageView) hasViews.findViewById(id.common_title_back_button));
        titleRightButton3 = ((ImageView) hasViews.findViewById(id.common_title_right_button3));
        btnSend = ((TextView) hasViews.findViewById(id.send_button));
        textGender = ((TextView) hasViews.findViewById(id.tv_gender));
        titleRightButton1 = ((ImageView) hasViews.findViewById(id.common_title_right_button1));
        areaCodeInput = ((EditText) hasViews.findViewById(id.register_areacode_input));
        companyNameInput = ((EditText) hasViews.findViewById(id.register_companyname_input));
        passwordConfirmInput = ((EditText) hasViews.findViewById(id.register_input_password_confirm));
        countryFlag = ((ImageView) hasViews.findViewById(id.iv_register_country_flag));
        fullNameInput = ((EditText) hasViews.findViewById(id.register_fullname_input));
        numberInput = ((EditText) hasViews.findViewById(id.register_number_input));
        genderLayout = ((RelativeLayout) hasViews.findViewById(id.ll_register_gender));
        countryCodeInput = ((EditText) hasViews.findViewById(id.register_countrycode_input));
        countryName = ((TextView) hasViews.findViewById(id.tv_register_country_name));
        countryLayout = ((RelativeLayout) hasViews.findViewById(id.rl_register_country_layout));
        passwordInput = ((EditText) hasViews.findViewById(id.register_input_password));
        initView();
    }

    public static class IntentBuilder_ {

        private Context context_;
        private final Intent intent_;
        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            context_ = context;
            intent_ = new Intent(context, RegisterActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            fragment_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, RegisterActivity_.class);
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            fragmentSupport_ = fragment;
            context_ = fragment.getActivity();
            intent_ = new Intent(context_, RegisterActivity_.class);
        }

        public Intent get() {
            return intent_;
        }

        public RegisterActivity_.IntentBuilder_ flags(int flags) {
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
