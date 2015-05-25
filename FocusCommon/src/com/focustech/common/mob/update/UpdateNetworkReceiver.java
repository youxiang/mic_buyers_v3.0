package com.focustech.common.mob.update;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**********************************************************
 * @�ļ����ƣ�UpdateNetworkReceiver.java
 * @�ļ����ߣ�xiongjiangwei
 * @����ʱ�䣺2014��10��28�� ����3:15:31
 * @�ļ�����������������״̬����
 * @�޸���ʷ��2014��10��28�մ�����ʼ�汾
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
