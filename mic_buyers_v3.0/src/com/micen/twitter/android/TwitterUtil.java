package com.micen.twitter.android;

import oauth.signpost.OAuth;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.product.ProductDetailContent;

public class TwitterUtil
{
	private Context context;
	private SharedPreferences prefs;

	public TwitterUtil(Context context)
	{
		this.context = context;
		this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	/*
	 * public boolean isAuthenticated() { String token = prefs.getString(OAuth.OAUTH_TOKEN, ""); String secret =
	 * prefs.getString(OAuth.OAUTH_TOKEN_SECRET, ""); AccessToken a = new AccessToken(token,secret); Twitter twitter =
	 * new TwitterFactory().getInstance(); twitter.setOAuthConsumer(ShareData.CONSUMER_KEY, ShareData.CONSUMER_SECRET);
	 * twitter.setOAuthAccessToken(a); try { twitter.getAccountSettings(); return true; } catch (TwitterException e) {
	 * return false; } }
	 */
	// 判断是否已经登录
	public boolean isAuthenticated()
	{
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		if (null == token || "".equals(token.trim()))
		{
			return false;
		}
		if (null == secret || "".equals(secret.trim()))
		{
			return false;
		}
		return true;
	}

	public void sendTweet(String msg, String productName, String productPicture, String productURl) throws Exception
	{
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		AccessToken a = new AccessToken(token, secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(context.getString(R.string.twitter_consumer_key),
				context.getString(R.string.twitter_consumer_secret));
		twitter.setOAuthAccessToken(a);
		StatusUpdate status = new StatusUpdate(msg + "\n" + productURl);
		twitter.updateStatus(status);
	}

	public void login()
	{
		Intent i = new Intent(this.context, PrepareRequestTokenActivity.class);
		context.startActivity(i);
	}

	// logout
	public void clearCredentials()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		final Editor edit = prefs.edit();
		edit.remove(OAuth.OAUTH_TOKEN);
		edit.remove(OAuth.OAUTH_TOKEN_SECRET);
		edit.commit();
		TwitterSessionEvents.onLogoutFinish();
	}
}
