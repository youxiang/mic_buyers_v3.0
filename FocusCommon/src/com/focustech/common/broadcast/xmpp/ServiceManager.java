/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.focustech.common.broadcast.xmpp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.focustech.common.util.LogUtil;

/** 
 * This class is to manage the notificatin service and to load the configuration.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public final class ServiceManager
{
	private static final String LOGTAG = LogUtil.makeLogTag(ServiceManager.class);

	private Context context;

	private SharedPreferences sharedPrefs;

	private String callbackActivityPackageName;

	private String callbackActivityClassName;

	public ServiceManager(Context context)
	{
		this.context = context;

		if (context instanceof Activity)
		{
			Log.i(LOGTAG, "Callback Activity...");
			Activity callbackActivity = (Activity) context;
			callbackActivityPackageName = callbackActivity.getPackageName();
			callbackActivityClassName = callbackActivity.getClass().getName();
		}

		sharedPrefs = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPrefs.edit();
		editor.putString(Constants.CALLBACK_ACTIVITY_PACKAGE_NAME, callbackActivityPackageName);
		editor.putString(Constants.CALLBACK_ACTIVITY_CLASS_NAME, callbackActivityClassName);
		editor.commit();
	}

	public void startService()
	{
		Thread serviceThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				Intent intent = NotificationService.getIntent();
				context.startService(intent);
			}
		});
		serviceThread.start();
	}

	public void stopService()
	{
		Intent intent = NotificationService.getIntent();
		context.stopService(intent);
	}
}
