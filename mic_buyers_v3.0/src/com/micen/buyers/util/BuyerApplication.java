package com.micen.buyers.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;

import com.focustech.common.http.FocusClient;
import com.focustech.common.http.HttpConfiguration.Builder;
import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.mob.analysis.FocusAnalyticsTracker;
import com.micen.buyers.db.SharedPreferenceManager;
import com.micen.buyers.exception.MicCrashHandler;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.ResponseCode;
import com.micen.buyers.module.mail.Mail;
import com.micen.buyers.module.showroom.CompanyProductGroup;
import com.micen.buyers.module.user.User;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**********************************************************
 * @文件名称：BuyerApplication.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月20日 下午1:37:57
 * @文件描述：全局Application对象
 * @修改历史：2015年3月20日创建初始版本
 **********************************************************/
public class BuyerApplication extends Application
{
	private static BuyerApplication application = null;
	private static Context appContext;
	private User user = null;
	public Map<String, Object> applicationDataCache = new HashMap<String, Object>();
	private ArrayList<CompanyProductGroup> productGroup;
	private List<Mail> mailDataList;

	// Focus统计
	protected FocusAnalyticsTracker mbtracker;

	protected BroadcastReceiver receiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			// TODO Auto-generated method stub
			int code = intent.getIntExtra("code", 0);
			if (99999 == code)
			{
				// 重新登录
				// System.out.println("re login");
				RequestCenter.userLogin(SharedPreferenceManager.getInstance().getString("lastLoginName", ""),
						SharedPreferenceManager.getInstance().getString("lastLoginPassword", ""), loginListener);
			}
		}
	};

	private DisposeDataListener loginListener = new DisposeDataListener()
	{
		@Override
		public void onSuccess(Object result)
		{
			BuyerApplication.getInstance().setUser((User) result);
			SysManager.boundAccount(((User) result).sessionid);
			// ToastUtil.toast(BuyerApplication.this, "重新登录成功99999");
		}

		@Override
		public void onFailure(Object failedReason)
		{
			// 登录失败
			ResponseCode code = ResponseCode.getCodeValueByTag(failedReason.toString());
			switch (code)
			{
			case CODE_10002:
				// ToastUtil.toast(LoginActivity.this, R.string.mic_login_failed_10002);
				break;
			case CODE_10003:
				// ToastUtil.toast(LoginActivity.this, R.string.mic_login_failed_10003);
				break;
			case CODE_10004:
				// ToastUtil.toast(LoginActivity.this, R.string.mic_login_failed_10004);
				// 密码有误，这边需要弹窗
				SharedPreferenceManager.getInstance().putBoolean("isAutoLogon", false);
				FocusClient.removeCookieStore();
				BuyerApplication.getInstance().setUser(null);
				SysManager.boundAccount("");
				// 下面弹窗会报错的，application中不能弹窗
				/*
				 * CommonDialog dialog = new CommonDialog(BuyerApplication.this,
				 * BuyerApplication.this.getString(R.string.Incorrect_Email_Address_or_Password),
				 * BuyerApplication.this.getString(R.string.mic_ok), Util.dip2px(258), new DialogClickListener() {
				 * @Override public void onDialogClick() { Intent ins = new Intent(BuyerApplication.this,
				 * LoginActivity_.class); ins.putExtra("loginTarget", LoginTarget.getValue(LoginTarget.None));
				 * BuyerApplication.this.startActivity(ins); } }); dialog.show();
				 */
				break;
			case CODE_10005:
				// ToastUtil.toast(LoginActivity.this, R.string.mic_login_failed_10005);
				break;
			case CODE_10006:
				// ToastUtil.toast(LoginActivity.this, R.string.mic_login_failed_10006);
				break;
			default:
				// ToastUtil.toast(LoginActivity.this, failedReason);
				break;
			}
		}
	};

	public static BuyerApplication getInstance()
	{
		return application;
	}

	/**
	 * 返回APP的Context
	 * @return
	 */
	public static Context getAppContext()
	{
		return appContext;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		application = this;
		appContext = getApplicationContext();
		initImageLoader(getApplicationContext());
		// initExceptionHandler();
		startService();
		initHttpClient();
		registerBrocadcast();
	}

	/**
	 * 初始focusclient,传入context,用于发送广播 
	 */
	private void initHttpClient()
	{
		Builder builer = new Builder(this.getApplicationContext());
		FocusClient.init(builer.build());
	}

	private void registerBrocadcast()
	{
		IntentFilter filter = new IntentFilter("com.micen.buyers.httpcode");
		registerReceiver(receiver, filter);
	}

	/**
	 * 启动统计服务
	 * @author xuliucheng
	 */
	private void startService()
	{
		mbtracker = FocusAnalyticsTracker.getInstances();
		mbtracker.startNewSession("mic", "mic_download", 180000, this);

	}

	/**
	 * 初始化异常处理对象
	 */
	private void initExceptionHandler()
	{
		MicCrashHandler crashHandler = MicCrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}

	public static void initImageLoader(Context context)
	{
		int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);

		MemoryCacheAware<String, Bitmap> memoryCache;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
		{
			memoryCache = new LruMemoryCache(memoryCacheSize);
		}
		else
		{
			memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
		}

		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2).memoryCache(memoryCache)
				.denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging() // Not necessary in common
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public List<Mail> getMailDataList()
	{
		return mailDataList;
	}

	public void setMailDataList(List<Mail> mailDataList)
	{
		this.mailDataList = mailDataList;
	}

	public ArrayList<CompanyProductGroup> getProductGroup()
	{
		return productGroup;
	}

	public void setProductGroup(ArrayList<CompanyProductGroup> productGroup)
	{
		this.productGroup = productGroup;
	}

	/**
	 * 判断买家账号
	 * @return
	 */
	public boolean isBuyerSuspended()
	{
		boolean value = false;
		if (user == null)
			return value;
		if ("4".equals(user.content.companyInfo.companyStat))
		{
			value = true;
		}
		return value;
	}

}
