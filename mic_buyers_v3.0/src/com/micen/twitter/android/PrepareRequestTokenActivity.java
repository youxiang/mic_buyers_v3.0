package com.micen.twitter.android;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.micen.buyers.activity.productdetail.ShareOnTwitterActicity_;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.constant.UrlConstants;

/**
 * Prepares a OAuthConsumer and OAuthProvider
 * 
 * OAuthConsumer is configured with the consumer key & consumer secret. OAuthProvider is configured with the 3 OAuth
 * endpoints.
 * 
 * Execute the OAuthRequestTokenTask to retrieve the request, and authorize the request.
 * 
 * After the request is authorized, a callback is made here.
 * 
 */
public class PrepareRequestTokenActivity extends Activity
{
	final String TAG = getClass().getName();
	private OAuthConsumer consumer;
	private OAuthProvider provider;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		try
		{
			this.consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
			this.provider = new CommonsHttpOAuthProvider(UrlConstants.TWITTER_REQUEST_URL,
					UrlConstants.TWITTER_ACCESS_URL, UrlConstants.TWITTER_AUTHORIZE_URL);
		}
		catch (Exception e)
		{
			Log.e(TAG, "Error creating consumer / provider", e);
		}
		Log.i(TAG, "Starting task to retrieve request token.");
		new OAuthRequestTokenTask(this, consumer, provider).execute();
	}

	/**
	 * Called when the OAuthRequestTokenTask finishes (user has authorized the request token). The callback URL will be
	 * intercepted here.
	 */
	@Override
	public void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Bundle b = intent.getExtras();
		String url = b.getString("url");
		final Uri uri = Uri.parse(url);
		// Log.e("******************************", uri + "");
		if (uri != null)
		{
			// Log.i(TAG, "Callback received : " + uri);
			// Log.i(TAG, "Retrieving Access Token");
			new RetrieveAccessTokenTask(this, consumer, provider, prefs).execute(uri);
		}
	}

	public class RetrieveAccessTokenTask extends AsyncTask<Uri, Void, Void>
	{
		private Context context;
		private OAuthProvider provider;
		private OAuthConsumer consumer;
		private SharedPreferences prefs;

		public RetrieveAccessTokenTask(Context context, OAuthConsumer consumer, OAuthProvider provider,
				SharedPreferences prefs)
		{
			this.context = context;
			this.consumer = consumer;
			this.provider = provider;
			this.prefs = prefs;
		}

		/**
		 * Retrieve the oauth_verifier, and store the oauth and oauth_token_secret for future API calls.
		 */
		@Override
		protected Void doInBackground(Uri... params)
		{
			final Uri uri = params[0];
			final String oauth_verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
			try
			{
				provider.retrieveAccessToken(consumer, oauth_verifier);
				final Editor edit = prefs.edit();
				edit.putString(OAuth.OAUTH_TOKEN, consumer.getToken());
				edit.putString(OAuth.OAUTH_TOKEN_SECRET, consumer.getTokenSecret());
				edit.commit();
				String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
				String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
				consumer.setTokenWithSecret(token, secret);
				// context.startActivity(new
				// Intent(context,AndroidTwitterSample.class));
				// executeAfterAccessTokenRetrieval();
				Log.i(TAG, "OAuth - Access Token Retrieved");
				TwitterSessionEvents.onLoginSuccess();
				PrepareRequestTokenActivity.this.finish();
				Intent intent = new Intent(PrepareRequestTokenActivity.this, ShareOnTwitterActicity_.class);
				startActivity(intent);
			}
			catch (Exception e)
			{
				Log.e(TAG, "OAuth - Access Token Retrieval Error", e);
				TwitterSessionEvents.onLoginError(e.getMessage());
			}
			return null;
		}
		/*
		 * private void executeAfterAccessTokenRetrieval() { String msg =
		 * getIntent().getExtras().getString("tweet_msg"); try { TwitterUtils.sendTweet(prefs, msg); } catch (Exception
		 * e) { Log.e(TAG, "OAuth - Error sending to Twitter", e); } }
		 */
	}
}
