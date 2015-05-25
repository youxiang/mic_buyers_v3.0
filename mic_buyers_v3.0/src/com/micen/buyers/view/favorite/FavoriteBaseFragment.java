package com.micen.buyers.view.favorite;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.widget.dialog.CommonDialog;
import com.focustech.common.widget.dialog.CommonDialog.DialogClickListener;
import com.focustech.common.widget.dialog.CommonProgressDialog;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase.OnPullEventListener;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase.State;
import com.focustech.common.widget.pulltorefresh.PullToRefreshListView;
import com.micen.buyers.activity.R;
import com.micen.buyers.adapter.FavouriteListAdapter;
import com.micen.buyers.db.SharedPreferenceManager;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.FavouriteManager.FavouriteType;
import com.micen.buyers.module.favorite.Favorite;
import com.micen.buyers.module.favorite.Favorites;
import com.micen.buyers.util.Util;
import com.micen.buyers.view.SearchListProgressBar;

/**********************************************************
 * @文件名称：FavoriteBaseFragment.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月25日 上午11:00:25
 * @文件描述：收藏父Fragment
 * @修改历史：2015年3月25日创建初始版本
 **********************************************************/
@EFragment
public abstract class FavoriteBaseFragment extends Fragment implements OnItemClickListener, OnItemLongClickListener,
		OnRefreshListener2<ListView>, DisposeDataListener
{
	protected RelativeLayout contentLayout;
	@ViewById(R.id.lv_favourite_list)
	protected PullToRefreshListView pullToListView;
	protected ListView listView;
	@ViewById(R.id.progressbar_layout)
	protected SearchListProgressBar progressBar;
	@ViewById(R.id.favourite_list_empty)
	protected ImageView ivEmpty;
	protected boolean isRefresh = false;
	protected boolean isGetMore = false;
	protected int pageNum = 1;
	protected int pageSize = 20;
	protected FavouriteListAdapter adapter;

	private CommonDialog dialog;
	private Favorite willDel;

	protected abstract String getChildFragmentTag();

	@Override
	public abstract void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3);

	public abstract void onLoadData();

	public FavoriteBaseFragment()
	{

	}

	@AfterViews
	protected void initView()
	{
		pullToListView.setVisibility(View.GONE);
		pullToListView.getLoadingLayoutProxy().setLastUpdatedLabel(
				SharedPreferenceManager.getInstance().getString(getChildFragmentTag(), ""));
		pullToListView.setOnRefreshListener(this);
		pullToListView.setOnPullEventListener(pullEventListener);
		// 只启动下拉刷新
		pullToListView.setMode(Mode.BOTH);
		// 不显示指示器
		pullToListView.setShowIndicator(false);
		listView = pullToListView.getRefreshableView();
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
	}

	private OnPullEventListener<ListView> pullEventListener = new OnPullEventListener<ListView>()
	{
		@Override
		public void onPullEvent(PullToRefreshBase<ListView> refreshView, State state, Mode direction)
		{
			switch (direction)
			{
			case PULL_FROM_START:
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
						SharedPreferenceManager.getInstance().getString(getChildFragmentTag(), ""));
				break;
			default:
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("");
				break;
			}
		}
	};

	protected void startGetFavourite(int pageNum, int pageSize)
	{
		RequestCenter.getFavoritesData(getChildFragmentTag(), pageNum, pageSize, this);
	}

	protected void startDeleteFavourite(String sourceId)
	{
		RequestCenter.delFavoriteData(sourceId, getChildFragmentTag(), new DisposeDataListener()
		{
			@Override
			public void onSuccess(Object result)
			{
				if (getActivity() == null || (getActivity() != null && getActivity().isFinishing()))
					return;
				CommonProgressDialog.getInstance().dismissProgressDialog();
				isRefresh = true;
				startGetFavourite(1, pageNum * pageSize);
				ToastUtil.toast(getActivity(), R.string.mic_delsuccess);
			}

			@Override
			public void onFailure(Object failedReason)
			{
				if (getActivity() == null || (getActivity() != null && getActivity().isFinishing()))
					return;
				CommonProgressDialog.getInstance().dismissProgressDialog();
				ToastUtil.toast(getActivity(), failedReason);
			}
		});
	}

	/**
	 * 刷新完成
	 */
	protected void refreshComplete()
	{
		if (pullToListView != null)
		{
			pullToListView.onRefreshComplete();
		}
	}

	/**
	 * 保存最后更新的时间
	 */
	protected void saveChildLastRefreshTime()
	{
		// Last Updated:7/31/2013 18:30
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm", Util.getLocale());
		Date dt = new Date(System.currentTimeMillis());
		String label = getActivity().getString(R.string.mic_last_updated) + sdf.format(dt);
		SharedPreferenceManager.getInstance().putString(getChildFragmentTag(), label);
	}

	@Override
	public void onFailure(Object failedReason)
	{
		if (getActivity() == null || (getActivity() != null && getActivity().isFinishing()))
			return;
		isRefresh = false;
		refreshComplete();
		CommonProgressDialog.getInstance().dismissProgressDialog();
		progressBar.setVisibility(View.GONE);
		if (adapter == null)
		{
			ivEmpty.setVisibility(View.VISIBLE);
			pullToListView.setVisibility(View.GONE);
		}
		ToastUtil.toast(getActivity(), failedReason);
	}

	/**
	 * 请求成功回调
	 * @param obj
	 * @param type
	 */
	protected void onGetDataSuccess(Object obj, FavouriteType type)
	{
		if (getActivity() == null || (getActivity() != null && getActivity().isFinishing()))
			return;
		Favorites datas = (Favorites) obj;
		refreshComplete();
		CommonProgressDialog.getInstance().dismissProgressDialog();
		progressBar.setVisibility(View.GONE);
		if (datas.content != null && datas.content.size() != 0)
		{
			ivEmpty.setVisibility(View.GONE);
			pullToListView.setVisibility(View.VISIBLE);
			if (adapter == null || isRefresh)
			{
				adapter = new FavouriteListAdapter(datas.content, getActivity(), type);
				listView.setAdapter(adapter);
			}
			else
			{
				adapter.addData(datas.content);
			}
		}
		else
		{
			if (adapter != null && isGetMore)
			{
				ivEmpty.setVisibility(View.GONE);
				pullToListView.setVisibility(View.VISIBLE);
			}
			else
			{
				ivEmpty.setVisibility(View.VISIBLE);
				pullToListView.setVisibility(View.GONE);
			}
		}
		isRefresh = false;
		isGetMore = false;
	}

	protected void onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, String flagName)
	{
		willDel = (Favorite) arg0.getItemAtPosition(arg2);
		dialog = new CommonDialog(getActivity(), getString(R.string.delete_item) + flagName
				+ getString(R.string.question_mark), getString(R.string.mic_ok), getString(R.string.cancel),
				Util.dip2px(258), new DialogClickListener()
				{
					@Override
					public void onDialogClick()
					{
						CommonProgressDialog.getInstance().showProgressDialog(getActivity(),
								getString(R.string.mic_loading));
						startDeleteFavourite(willDel.sourceId);
					}
				});
		dialog.show();
	}
}
