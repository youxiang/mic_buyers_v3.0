package com.focustech.common.mob.update;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**********************************************************
 * @文件名称：UpdateNetworkReceiver.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年10月28日 下午3:15:31
 * @文件描述：检查更新网络状态监听
 * @修改历史：2014年10月28日创建初始版本
 **********************************************************/
public class UpdateNetworkReceiver extends BroadcastReceiver
{
	private static final String LOGTAG = UpdateNetworkReceiver.class.getSimpleName();

	private UpdateService updateService;

	public UpdateNetworkReceiver(UpdateService updateService)
	{
		this.updateService = updateService;
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.d(LOGTAG, "UpdateNetworkReceiver.onReceive()...");
		String action = intent.getAction();
		Log.d(LOGTAG, "action=" + action);

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		if (networkInfo != null)
		{
			Log.d(LOGTAG, "Network Type  = " + networkInfo.getTypeName());
			Log.d(LOGTAG, "Network State = " + networkInfo.getState());
			if (networkInfo.isConnected())
			{
				Log.i(LOGTAG, "Network connected");
				updateService.startDownload();
			}
		}
		else
		{
			Log.e(LOGTAG, "Network unavailable");
			UpdateManager.getInstance().pause();
		}
	}

}
