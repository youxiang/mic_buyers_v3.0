package com.focustech.common.mob.update;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import android.content.Context;

import com.focustech.common.http.FocusClient;
import com.focustech.common.http.download.IDownloadListener;
import com.focustech.common.mob.BaseUrl;
import com.focustech.common.util.Utils;
import com.loopj.android.http.RequestParams;

/**********************************************************
 * @文件名称：UpdateManager.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年10月24日 上午10:14:55
 * @文件描述：检查更新管理类
 * @修改历史：2014年10月24日创建初始版本
 **********************************************************/
public class UpdateManager
{
	private static UpdateManager manager;
	private UpdateDownloadRequest thread;
	private ThreadPoolExecutor threadPool;
	private UpdateDownloadRequest downloadRequest;

	private UpdateManager()
	{
		threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
	}

	static
	{
		manager = new UpdateManager();
	}

	public static UpdateManager getInstance()
	{
		return manager;
	}

	/**
	 * 检查更新请求
	 * @param context
	 * @param productName		产品名称
	 * @param productChannel	渠道(默认为self)
	 * @param listener
	 */
	public static void checkUpdate(Context context, String productName, String productChannel, UpdateListener listener)
	{
		RequestParams params = new RequestParams();
		params.add("productName", productName);
		params.add("version", Utils.getAppVersionName(context));
		params.add("channelName", productChannel);
		params.add("platName", "android");
		FocusClient.get(BaseUrl.BASE_URL_R + "/product/checkVersion", params, new UpdateResponseHandler(listener));
	}

	public void startDownload(int startPos, String downloadUrl, String localFilePath, long contentLength,
			IDownloadListener downloadListener)
	{
		if (downloadRequest != null && downloadRequest.isDownloading())
		{
			return;
		}
		checkLocalFilePath(localFilePath);
		downloadRequest = new UpdateDownloadRequest(startPos, downloadUrl, localFilePath, contentLength,
				downloadListener);
		Future<?> request = threadPool.submit(downloadRequest);
		new WeakReference<Future<?>>(request);
	}

	private void checkLocalFilePath(String localFilePath)
	{
		File path = new File(localFilePath.substring(0, localFilePath.lastIndexOf("/") + 1));
		File file = new File(localFilePath);
		if (!path.exists())
		{
			path.mkdirs();
		}
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void pause()
	{
		if (thread != null)
		{
			thread.stopDownloading();
		}
	}

	public boolean isDownloading()
	{
		if (thread != null)
		{
			return thread.isDownloading();
		}
		return false;
	}

}
