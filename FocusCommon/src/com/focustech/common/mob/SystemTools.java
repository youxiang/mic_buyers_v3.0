package com.focustech.common.mob;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;
public class SystemTools
{
	/***
	 * 获得产品的版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getProductVersion(Context context)
	{
		String versionName= "";
		try
		{
			PackageManager pm= context.getPackageManager();
			PackageInfo pi= pm.getPackageInfo(context.getPackageName(), 0);
			versionName= pi.versionName;
			if (versionName == null || versionName.length() < 0)
			{
				return "";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return versionName;
	}
	/***
	 * Check whether there are sd card, if return true,explain SD card existence, else is not
	 * 
	 * @return
	 */
	public static boolean isSdcardExist()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? true : false;
	}
	/***
	 * 获得屏幕参数数据
	 * 
	 * @return DisplayMetrics
	 */
	public static DisplayMetrics getDisplayMetrics(Context context)
	{
		DisplayMetrics dm= new DisplayMetrics();
		WindowManager wm= (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		return dm;
	}
}
