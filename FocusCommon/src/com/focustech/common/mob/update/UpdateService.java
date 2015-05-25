package com.focustech.common.mob.update;

import java.io.File;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.RemoteViews;

import com.focustech.common.R;
import com.focustech.common.http.download.IDownloadListener;

/**********************************************************
 * @文件名称：UpdateService.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年10月22日 下午4:56:49
 * @文件描述：检查更新服务
 * @修改历史：2014年10月22日创建初始版本
 **********************************************************/
public class UpdateService extends Service
{
	private NotificationManager notificationManager;
	private Notification mNotification;
	private String filePath;
	private String downloadUrl;
	private int startPos = 0;
	private long contentLength;
	private UpdatePhoneStateListener phoneStateListener;
	private BroadcastReceiver connectivityReceiver;
	private TelephonyManager telephonyManager;

	public UpdateService()
	{
		phoneStateListener = new UpdatePhoneStateListener(this);
		connectivityReceiver = new UpdateNetworkReceiver(this);
	}

	@Override
	public void onCreate()
	{
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		notificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		filePath = Environment.getExternalStorageDirectory() + "/focustech/UnknownProduct.apk";
		// registerConnectivityReceiver();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		if (intent == null || (intent != null && !intent.hasExtra("downloadUrl")))
		{
			notifySchedule(getString(R.string.download_failed), getString(R.string.download_failed_msg), false, false);
			stopSelf();// 停掉服务自身
			return super.onStartCommand(intent, flags, startId);
		}
		downloadUrl = intent.getStringExtra("downloadUrl");
		if (intent.hasExtra("filePath") && !"".equals(intent.getStringExtra("filePath")))
		{
			filePath = intent.getStringExtra("filePath");
		}
		if (intent.hasExtra("contentLength") && !"".equals(intent.getStringExtra("contentLength")))
		{
			contentLength = intent.getLongExtra("contentLength", 0);
		}
		notifySchedule(getString(R.string.download_start), getString(R.string.download_start), true, false);
		startDownload();
		return super.onStartCommand(intent, flags, startId);
	}

	public void startDownload()
	{
		UpdateManager.getInstance().startDownload(startPos, downloadUrl, filePath, contentLength,
				new IDownloadListener()
				{
					@Override
					public void onStarted()
					{

					}

					@Override
					public void onProgressChanged(int progress, String downloadUrl)
					{
						RemoteViews contentview = mNotification.contentView;
						contentview.setTextViewText(R.id.name, getString(R.string.app_name));
						contentview.setProgressBar(R.id.progressbar, 100, progress, false);
						notificationManager.notify(0, mNotification);
					}

					@Override
					public void onPrepared(long contentLength, String downloadUrl)
					{

					}

					@Override
					public void onPaused(int progress, int completeSize, String downloadUrl)
					{
						// startPos += completeSize;
						// RemoteViews contentview = mNotification.contentView;
						// contentview.setTextViewText(R.id.name, getString(R.string.app_name) + "(已暂停)");
						// contentview.setProgressBar(R.id.progressbar, 100, progress, false);
						// notificationManager.notify(0, mNotification);
						notifySchedule(getString(R.string.download_failed), getString(R.string.download_failed_msg),
								false, false);
						stopSelf();// 停掉服务自身
					}

					@Override
					public void onFinished(int completeSize, String downloadUrl)
					{
						notifySchedule(getString(R.string.download_complete), getString(R.string.download_complete),
								false, true);
						stopSelf();// 停掉服务自身
						startActivity(getInstallApkIntent());
					}

					@Override
					public void onFailure()
					{
						notifySchedule(getString(R.string.download_failed), getString(R.string.download_failed_msg),
								false, false);
						stopSelf();// 停掉服务自身
					}
				});
	}

	private Intent getInstallApkIntent()
	{
		File apkfile = new File(filePath);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		return intent;
	}

	public void notifySchedule(String tickerMsg, String message, boolean isNeedProgress, boolean isNeedContentIntent)
	{
		if (VERSION.SDK_INT >= 11)
		{
			notifyThatExceedLv11(tickerMsg, message, isNeedProgress, isNeedContentIntent);
		}
		else
		{
			notifyThatUnderLv11(tickerMsg, message, isNeedProgress, isNeedContentIntent);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void notifyThatExceedLv11(String tickerMsg, String message, boolean isNeedProgress,
			boolean isNeedContentIntent)
	{
		// Notification
		Notification.Builder notification = new Notification.Builder(this);
		notification.setSmallIcon(R.drawable.ic_launcher);
		if (isNeedProgress)
		{
			RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.update_notification);
			notification.setContent(contentView);
		}
		else
		{
			notification.setContentTitle(getString(R.string.app_name));
			notification.setContentText(message);
		}
		notification.setAutoCancel(true);
		notification.setWhen(System.currentTimeMillis());
		notification.setTicker(tickerMsg);
		notification.setContentIntent(isNeedContentIntent ? getContentIntent() : PendingIntent.getActivity(this, 0,
				new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
		mNotification = notification.getNotification();
		notificationManager.notify(0, mNotification);
	}

	@SuppressWarnings("deprecation")
	private void notifyThatUnderLv11(String tickerMsg, String message, boolean isNeedProgress,
			boolean isNeedContentIntent)
	{
		mNotification = new Notification();
		mNotification.icon = R.drawable.ic_launcher;
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		mNotification.when = System.currentTimeMillis();
		mNotification.tickerText = tickerMsg;
		if (isNeedProgress)
		{
			RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.update_notification);
			mNotification.contentView = contentView;
		}
		else
		{
			mNotification.setLatestEventInfo(
					this,
					getString(R.string.app_name),
					message,
					isNeedContentIntent ? getContentIntent() : PendingIntent.getActivity(this, 0, new Intent(),
							PendingIntent.FLAG_UPDATE_CURRENT));

		}
		notificationManager.notify(0, mNotification);
	}

	private PendingIntent getContentIntent()
	{
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, getInstallApkIntent(),
				PendingIntent.FLAG_UPDATE_CURRENT);
		return contentIntent;
	}

	@Override
	public void onDestroy()
	{
		// unregisterConnectivityReceiver();
	}

	public void registerReceiver()
	{
		telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
		IntentFilter filter = new IntentFilter();
		// filter.addAction(android.net.wifi.WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(connectivityReceiver, filter);
	}

	public void unregisterReceiver()
	{
		telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
		unregisterReceiver(connectivityReceiver);
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onRebind(Intent intent)
	{

	}

	@Override
	public boolean onUnbind(Intent intent)
	{
		return true;
	}

}
