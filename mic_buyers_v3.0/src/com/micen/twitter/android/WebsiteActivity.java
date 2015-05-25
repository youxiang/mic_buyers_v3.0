package com.micen.twitter.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.micen.buyers.activity.R;

public class WebsiteActivity extends Activity
{
	private WebView twitter;
	private String url;
	private ProgressDialog progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.website);
		initIntent();
		initView();
	}

	private void initIntent()
	{
		Intent intent = getIntent();
		Bundle bundle = new Bundle();
		bundle = intent.getExtras();
		url = bundle.getString("url");
	}

	private void initView()
	{
		twitter = (WebView) findViewById(R.id.website);
		twitter.loadUrl(url);
		twitter.setWebViewClient(new MyWebViewClient());
		progressBar = ProgressDialog.show(WebsiteActivity.this, "Please wait", "Loading...");
	}

	private class MyWebViewClient extends WebViewClient
	{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			Log.e("--------------", "url");
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url)
		{
			super.onPageFinished(view, url);
			if (progressBar.isShowing())
			{
				progressBar.dismiss();
			}
			if (null != url && url.contains("oauth_verifier"))
			{
				Intent i = new Intent(WebsiteActivity.this, PrepareRequestTokenActivity.class);
				i.putExtra("url", url);
				startActivity(i);
			}
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			super.onPageStarted(view, url, favicon);
		}
	}
}
