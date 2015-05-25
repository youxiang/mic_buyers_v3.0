package com.micen.buyers.activity.showroom;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.micen.buyers.activity.BaseActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.constant.UrlConstants;
import com.micen.buyers.view.SearchListProgressBar;

@EActivity(R.layout.contactus)
public class ArAsActivity extends BaseActivity
{
	@ViewById(R.id.contactus_webview)
	protected WebView mWebView;
	private WebSettings mWebSettings;
	private String mString;
	@ViewById(R.id.progressbar_layout)
	protected SearchListProgressBar progressLayout;

	private void initIntent()
	{
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		mString = bundle.getString(Constants.AR_AS_KEYWORD);
	}

	private void showWebView()
	{
		if (mString.equals(Constants.AR_VALUES))
		{
			mWebView.loadUrl(UrlConstants.AS_PAGE_URL);
			titleText.setText(R.string.mic_home_banner_as);
		}
		else if (mString.equals(Constants.AS_VALUES))
		{
			mWebView.loadUrl(UrlConstants.AR_PAGE_URL);
			titleText.setText(R.string.mic_home_banner_ar);
		}
		else if (mString.equals(Constants.SE_VALUES))
		{
			mWebView.loadUrl(UrlConstants.SE_PAGE_URL);
			titleText.setText(R.string.mic_home_banner_service);
		}
	}

	@AfterViews
	protected void initView()
	{
		initIntent();

		titleLeftButton.setOnClickListener(this);
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleText.setText(R.string.mic_home_meun_text6);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);
		mWebSettings = mWebView.getSettings();
		mWebSettings.setJavaScriptEnabled(true);

		mWebView.setWebViewClient(new WebViewClient()
		{
			@Override
			public void onPageFinished(WebView view, String url)
			{
				if (progressLayout.isShown())
				{
					progressLayout.setVisibility(View.GONE);
				}
			}
		});
		showWebView();
	}

	@Override
	public void onClick(View view)
	{
		super.onClick(view);
		switch (view.getId())
		{
		case R.id.common_title_back_button:
			finish();
			break;
		}
	}
}