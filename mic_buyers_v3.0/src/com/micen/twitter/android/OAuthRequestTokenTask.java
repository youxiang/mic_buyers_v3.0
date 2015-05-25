package com.micen.twitter.android;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.focustech.common.util.ToastUtil;
import com.micen.buyers.activity.R;
import com.micen.buyers.constant.UrlConstants;

/**
 * An asynchronous task that communicates with Twitter to retrieve a request token. (OAuthGetRequestToken)
 * 
 * After receiving the request token from Twitter, pop a browser to the user to authorize the Request Token.
 * (OAuthAuthorizeToken)
 * 
 */
public class OAuthRequestTokenTask extends AsyncTask<Void, Void, Void>
{
	final String TAG = getClass().getName();
	private Context context;
	private OAuthProvider provider;
	private OAuthConsumer consumer;
	private ProgressDialog waitingDialog;

	/**
	 * 
	 * We pass the OAuth consumer and provider.
	 * 
	 * @param context Required to be able to start the intent to launch the browser.
	 * @param provider The OAuthProvider object
	 * @param consumer The OAuthConsumer object
	 */
	public OAuthRequestTokenTask(Context context, OAuthConsumer consumer, OAuthProvider provider)
	{
		this.context = context;
		this.consumer = consumer;
		this.provider = provider;
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		if (null == waitingDialog)
		{
			waitingDialog = new ProgressDialog(context);
			// 设置为圆形
			waitingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// 设置ProgressDialog 标题
			// waitingDialog.setTitle("请稍后");
			waitingDialog.setCanceledOnTouchOutside(false);
			waitingDialog.setMessage(context.getString(R.string.mic_loading));
			waitingDialog.show();
		}
	}

	/**
	 * 
	 * Retrieve the OAuth Request Token and present a browser to the user to authorize the token.
	 * 
	 */
	@Override
	protected Void doInBackground(Void... params)
	{
		try
		{
			Log.i(TAG, "Retrieving request token from Google servers");
			final String url = provider.retrieveRequestToken(consumer, UrlConstants.OAUTH_CALLBACK_URL);
			waitingDialog.dismiss();
			Log.e("+++++++++++++++++++++++++", url + "");
			Log.i(TAG, "Popping a browser with the authorize URL : " + url);
			// Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url))
			// .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
			// | Intent.FLAG_ACTIVITY_NO_HISTORY
			// | Intent.FLAG_FROM_BACKGROUND);
			Intent intent = new Intent(context, WebsiteActivity.class);
			intent.putExtra("url", url);
			context.startActivity(intent);
		}
		catch (Exception e)
		{
			Message msg = new Message();
			Log.e(TAG, "Error during OAUth retrieve request token", e);
			waitingDialog.dismiss();
			msg.what = 0;
			handler.sendMessage(msg);
		}
		return null;
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 0:
				ToastUtil.toast(context, R.string.service_error_again);
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};
}