package com.micen.buyers.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.focustech.common.mob.update.Update;
import com.focustech.common.mob.update.UpdateListener;
import com.focustech.common.mob.update.UpdateManager;
import com.focustech.common.mob.update.UpdateService;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.widget.dialog.CommonProgressDialog;
import com.micen.buyers.activity.R;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.db.SharedPreferenceManager;
import com.micen.buyers.view.UpdateDialog;
import com.micen.buyers.view.UpdateDialog.ConfirmDialogListener;

/**********************************************************
 * @文件名称：CheckUpdateManager.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年10月27日 下午4:04:07
 * @文件描述：检查更新管理类
 * @修改历史：2014年10月27日创建初始版本
 **********************************************************/
public class CheckUpdateManager
{
	/**
	 * 检查更新
	 * @param context
	 * @param isNeedToast	是否弹出提示
	 */
	public static void checkUpdate(final Context context, final boolean isNeedToast)
	{
		if (isNeedToast)
		{
			CommonProgressDialog.getInstance().showCancelableProgressDialog(context,
					context.getString(R.string.mic_loading));
		}
		UpdateManager.checkUpdate(context, Constants.APP_KEY, "self", new UpdateListener()
		{
			@Override
			public void onSuccess(boolean isNewVersion, boolean isForceUpdate, final Update update)
			{
				CommonProgressDialog.getInstance().dismissProgressDialog();
				if (isNewVersion)
				{
					SharedPreferenceManager.getInstance().putString("latestVersion", update.versionInfo);
					SharedPreferenceManager.getInstance().putBoolean("isHaveNewVersion", true);
					final UpdateDialog dialog = new UpdateDialog(context, isForceUpdate);
					String msg = (update.remarksUpdate != null && !"".equals(update.remarksUpdate)) ? update.remarksUpdate
							: context.getString(R.string.update_not_have_msg);
					dialog.initDialogStyle1(msg, new ConfirmDialogListener()
					{
						@Override
						public void onConfirmDialog()
						{
							String filePath = Environment.getExternalStorageDirectory()
									+ "/focustech/mic/update/MIC_for_Buyer_V" + update.versionInfo + ".apk";
							Intent serviceIntent = new Intent(context, UpdateService.class);
							serviceIntent.putExtra("downloadUrl", update.upgradeUrl);
							serviceIntent.putExtra("filePath", filePath);
							serviceIntent.putExtra("contentLength", Long.parseLong(update.contentLength));
							context.startService(serviceIntent);
						}
					});
					dialog.show();
				}
				else
				{
					if (isNeedToast)
					{
						ToastUtil.toast(context, R.string.latest_version);
					}
				}
			}

			@Override
			public void onFailure()
			{
				CommonProgressDialog.getInstance().dismissProgressDialog();
//				if (isNeedToast)
//				{
//					switch (failureCode)
//					{
//					case ConnectTimeout:
//					case Socket:
//					case SocketTimeout:
//						ToastUtil.toast(context, ConnectFailtureCode.Timeout.toString());
//						break;
//					case UnknownHost:
//						ToastUtil.toast(context, ConnectFailtureCode.UnknownHost.toString());
//						break;
//					case Interrupted:
//						ToastUtil.toast(context, ConnectFailtureCode.Interrupted.toString());
//						break;
//					default:
//						ToastUtil.toast(context, ConnectFailtureCode.Error.toString());
//						break;
//					}
//				}
			}
		});
	}
}
