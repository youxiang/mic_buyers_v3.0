package com.micen.buyers.view.favorite;

import org.androidannotations.annotations.EFragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.focustech.common.widget.dialog.CommonProgressDialog;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.productdetail.ProductMessageActivity_;
import com.micen.buyers.manager.FavouriteManager.FavouriteType;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.favorite.Favorite;

/**********************************************************
 * @文件名称：ProductFragment.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月25日 上午10:54:54
 * @文件描述：产品收藏Fragment
 * @修改历史：2015年3月25日创建初始版本
 **********************************************************/
@EFragment(R.layout.favourite_content)
public class ProductFragment extends FavoriteBaseFragment
{
	public ProductFragment()
	{
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
	{
		isRefresh = true;
		saveChildLastRefreshTime();
		startGetFavourite(1, pageNum * pageSize);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
	{
		isGetMore = true;
		pageNum++;
		startGetFavourite(pageNum, pageSize);
	}

	@Override
	protected String getChildFragmentTag()
	{
		return FavouriteType.Product.toString();
	}

	@Override
	public void onLoadData()
	{
		if (getActivity() != null && !getActivity().isFinishing() && listView != null && listView.getAdapter() != null
				&& !CommonProgressDialog.getInstance().isShowing())
		{
			CommonProgressDialog.getInstance().showCancelableProgressDialog(getActivity(),
					getActivity().getString(R.string.mic_loading));
		}
		pageNum = 1;
		isRefresh = true;
		startGetFavourite(pageNum, pageSize);
	}

	@Override
	public void onSuccess(Object obj)
	{
		onGetDataSuccess(obj, FavouriteType.Product);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		// 事件统计 128 点击查看产品详情（收藏列表） 点击事件
		SysManager.analysis(R.string.a_type_click, R.string.c128);

		Favorite data = (Favorite) arg0.getAdapter().getItem(arg2);
		Intent intent = new Intent(getActivity(), ProductMessageActivity_.class);
		intent.putExtra("productId", data.sourceId);
		getActivity().startActivity(intent);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		onItemLongClick(arg0, arg1, arg2, arg3, getString(R.string.favourite_product));
		return true;
	}
}
