package com.micen.buyers.manager;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.NetworkUtils;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.util.Utils;
import com.micen.buyers.activity.R;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.module.CheckResult;
import com.micen.buyers.util.BuyerApplication;

/**********************************************************
 * @文件名称：FavouriteManager.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年8月12日 上午9:27:33
 * @文件描述：收藏管理类
 * @修改历史：2014年8月12日创建初始版本
 **********************************************************/
public class FavouriteManager
{
	private static FavouriteManager manager = null;

	static
	{
		manager = new FavouriteManager();
	}

	/**********************************************************
	 * @文件名称：FavouriteManager.java
	 * @文件作者：xiongjiangwei
	 * @创建时间：2014年8月12日 上午10:19:34
	 * @文件描述：收藏回调
	 * @修改历史：2014年8月12日创建初始版本
	 **********************************************************/
	public interface FavouriteCallback
	{
		public void onCallback(boolean result);
	}

	/**********************************************************
	 * @文件名称：FavouriteManager.java
	 * @文件作者：xiongjiangwei
	 * @创建时间：2014年8月12日 上午10:19:21
	 * @文件描述：收藏的类型
	 * @修改历史：2014年8月12日创建初始版本
	 **********************************************************/
	public enum FavouriteType
	{
		/**
		 * 产品
		 */
		Product("prod"),
		/**
		 * 公司
		 */
		Company("com"),
		/**
		 * 目录
		 */
		Category("category");
		private String value;

		private FavouriteType(String value)
		{
			this.value = value;
		}

		@Override
		public String toString()
		{
			return value;
		}
	}

	private FavouriteManager()
	{

	}

	public static FavouriteManager getInstance()
	{
		return manager;
	}

	/**
	 * 检查是否收藏
	 * @param context
	 * @param displayView
	 * @param type
	 * @param soureId
	 * @param callback
	 */
	public void checkIsFavourite(final Context context, final View displayView, final FavouriteType type,
			final String soureId, final FavouriteCallback callback)
	{
		if (!NetworkUtils.isConnectInternet(context))
		{
			ToastUtil.toast(context, R.string.request_error1);
			return;
		}
		if (BuyerApplication.getInstance().getUser() == null || displayView == null)
		{
			return;
		}
		RequestCenter.checkFavourite(new DisposeDataListener()
		{
			@Override
			public void onSuccess(Object arg0)
			{
				CheckResult value = (CheckResult) arg0;
				if (value.result)
				{
					setDisplayViewStatus(context, displayView, value.result, type);
				}
				callback.onCallback(value.result);
			}

			@Override
			public void onFailure(Object failedReason)
			{
				ToastUtil.toast(context, failedReason);
			}
		}, soureId, type.toString());
	}

	/**
	 * 添加或删除收藏
	 * @param context
	 * @param displayView
	 * @param type			收藏的类型
	 * @param soureId		收藏对象ID
	 * @param soureSubject	收藏对象内容
	 * @param isAddFavourite	是否添加收藏
	 * @param callback			添加或取消收藏完成的回调
	 */
	public void addOrDelFavourite(Context context, View displayView, FavouriteType type, String soureId,
			String soureSubject, boolean isAddFavourite, FavouriteCallback callback)
	{
		if (displayView == null)
		{
			return;
		}
		if (!NetworkUtils.isConnectInternet(context))
		{
			ToastUtil.toast(context, R.string.request_error1);
			return;
		}
		if (displayView.getBackground() instanceof AnimationDrawable)
		{
			// 如果已经开始加载动画了，则直接跳出，避免重复加载
			if (((AnimationDrawable) displayView.getBackground()).isRunning())
			{
				return;
			}
		}
		int favoriteRes = -1;
		if (!isAddFavourite)
		{
			switch (type)
			{
			case Product:
				favoriteRes = R.drawable.star_favourite;
				SysManager
						.analysis(context, context.getString(R.string.a_type_click), context.getString(R.string.a126));
				break;
			case Category:
				favoriteRes = R.drawable.star_favourite;
				SysManager
						.analysis(context, context.getString(R.string.a_type_click), context.getString(R.string.a115));
				break;
			case Company:
				favoriteRes = R.drawable.star_favourite_company;
				SysManager
						.analysis(context, context.getString(R.string.a_type_click), context.getString(R.string.a101));
				break;
			}
			displayView.setBackgroundResource(favoriteRes);
			((AnimationDrawable) displayView.getBackground()).start();
			favourite(context, displayView, type, soureId, soureSubject, callback, isAddFavourite);
		}
		else
		{
			switch (type)
			{
			case Product:
				favoriteRes = R.drawable.star_unfavourite;
				SysManager
						.analysis(context, context.getString(R.string.a_type_click), context.getString(R.string.a127));
				break;
			case Category:
				favoriteRes = R.drawable.star_unfavourite;
				SysManager
						.analysis(context, context.getString(R.string.a_type_click), context.getString(R.string.a116));
				break;
			case Company:
				favoriteRes = R.drawable.star_unfavourite_company;
				SysManager
						.analysis(context, context.getString(R.string.a_type_click), context.getString(R.string.a102));
				break;
			}
			displayView.setBackgroundResource(favoriteRes);
			((AnimationDrawable) displayView.getBackground()).start();
			unFavourite(context, displayView, type, soureId, callback, isAddFavourite);
		}
	}

	/**
	 * 添加收藏请求
	 * @param context
	 * @param displayView
	 * @param type
	 * @param favourite
	 * @param callback
	 * @param isAdd
	 */
	private void favourite(Context context, View displayView, FavouriteType type, String soureId, String soureSubject,
			FavouriteCallback callback, boolean isAddFavourite)
	{
		RequestCenter.addFavoriteData(type.toString(), soureId, "0", "", soureSubject, "1",
				getResponseListener(context, displayView, callback, isAddFavourite, type));
	}

	/**
	 * 取消收藏请求
	 * @param context
	 * @param displayView
	 * @param type
	 * @param favourite
	 * @param callback
	 * @param isAdd
	 */
	private void unFavourite(Context context, View displayView, FavouriteType type, String soureId,
			FavouriteCallback callback, boolean isAddFavourite)
	{
		RequestCenter.delFavoriteData(soureId, type.toString(),
				getResponseListener(context, displayView, callback, isAddFavourite, type));
	}

	/**
	 * 获取收藏或取消收藏的请求回调 
	 * @param context
	 * @param displayView
	 * @param callback
	 * @param isAdd
	 * @return
	 */
	private DisposeDataListener getResponseListener(final Context context, final View displayView,
			final FavouriteCallback callback, final boolean isAddFavourite, final FavouriteType type)
	{
		DisposeDataListener listener = new DisposeDataListener()
		{
			@Override
			public void onSuccess(Object obj)
			{
				boolean result = !isAddFavourite ? true : false;
				setDisplayViewStatus(context, displayView, result, type);
				callback.onCallback(result);
			}

			@Override
			public void onFailure(Object failedReason)
			{
				boolean result = !isAddFavourite ? false : true;
				setDisplayViewStatus(context, displayView, result, type);
				ToastUtil.toast(context, failedReason);
				callback.onCallback(result);
			}
		};
		return listener;
	}

	/**
	 * 设置显示网络请求过程View的状态
	 * @param context
	 * @param displayView
	 * @param isFavourite
	 */
	private void setDisplayViewStatus(Context context, View displayView, boolean isFavourite, FavouriteType type)
	{
		if (displayView == null || displayView.getVisibility() == View.INVISIBLE
				|| displayView.getVisibility() == View.GONE)
			return;
		if (displayView.getBackground() instanceof AnimationDrawable)
		{
			((AnimationDrawable) displayView.getBackground()).stop();
		}
		if (isFavourite)
		{
			if (type == FavouriteType.Company)
			{
				displayView.setBackgroundResource(R.drawable.btn_showroom_favorited);
			}
			else
			{
				displayView.setBackgroundDrawable(Utils.setImageButtonState(R.drawable.icon_favourit_down,
						R.drawable.icon_favourite, context));
			}
		}
		else
		{
			if (type == FavouriteType.Company)
			{
				displayView.setBackgroundResource(R.drawable.btn_showroom_favorite);
			}
			else
			{
				displayView.setBackgroundDrawable(Utils.setImageButtonState(R.drawable.icon_unfavourit_down,
						R.drawable.icon_unfavourit, context));
			}
		}
	}
}
