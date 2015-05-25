package com.micen.buyers.view.rfq;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.micen.buyers.adapter.rfq.RFQListAdapter;
import com.micen.buyers.db.SharedPreferenceManager;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.rfq.RFQ;
import com.micen.buyers.module.rfq.RFQContent;
import com.micen.buyers.module.rfq.RFQStatus;
import com.micen.buyers.util.Util;
import com.micen.buyers.view.SearchListProgressBar;

/**********************************************************
 * @文件名称：RFQBaseFragment.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月25日 上午9:17:10
 * @文件描述：RFQ列表页父Fragment
 * @修改历史：2015年3月25日创建初始版本
 **********************************************************/
@EFragment
public abstract class RFQBaseFragment extends Fragment implements DisposeDataListener, OnRefreshListener2<ListView>,
		OnItemClickListener, OnClickListener, OnItemLongClickListener
{
	@ViewById(R.id.rfq_list_empty)
	protected ImageView emptyView;
	@ViewById(R.id.rfq_listView)
	protected PullToRefreshListView pullToListView;
	@ViewById(R.id.progressbar_layout)
	protected SearchListProgressBar progressBarLayout;
	protected View arrowView;
	protected TextView tvDate;
	protected RelativeLayout fragmentLayout;
	protected ListView listView;
	protected RFQListAdapter adapter;
	protected RFQOrderType orderByType = RFQOrderType.DESC;
	/**
	 * 每页数据条数
	 */
	protected int pageSize;
	/**
	 * 当前列表数据页数
	 */
	protected int pageNo;
	/**
	 * 请求类型标识
	 */
	protected int requestTypeState;
	/**
	 * 未发生请求（默认）
	 */
	protected static final int REQUEST_TYPE_DEFAULT = 0;
	/**
	 * 刷新列表
	 */
	protected static final int REQUEST_TYPE_REFRESH_LIST = 1;
	/**
	 * 获取更多
	 */
	protected static final int REQUEST_TYPE_GET_MORE = 2;
	/**
	 * 初始化列表
	 */
	protected static final int REQUEST_TYPE_FIRST_REQUEST = 3;

	protected CommonDialog dialog;

	public RFQBaseFragment()
	{

	}

	@Override
	public abstract void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3);

	/**
	 * RFQ列表排序字段
	 * @return
	 */
	public abstract String getRFQListOrderByField();

	protected abstract String getChildFragmentTag();

	protected abstract RFQStatus getRFQStatus();

	public enum RFQOrderType
	{
		ASC("asc"), DESC("desc");

		private String value;

		private RFQOrderType(String value)
		{
			this.value = value;
		}

		public String toString()
		{
			return value;
		}
	}

	protected void initCache()
	{
		requestTypeState = REQUEST_TYPE_DEFAULT;
		pageSize = 20;
		pageNo = 1;
	}

	/**
	 * 网络请求数据成功
	 * @param result
	 */
	@Override
	public void onSuccess(Object result)
	{
		if (getActivity() == null || (getActivity() != null && getActivity().isFinishing()))
			return;
		dismissProgress();
		pullToListView.setVisibility(View.VISIBLE);
		CommonProgressDialog.getInstance().dismissProgressDialog();
		RFQ data = (RFQ) result;
		switch (requestTypeState)
		{
		case REQUEST_TYPE_FIRST_REQUEST:
		case REQUEST_TYPE_REFRESH_LIST:
			// 无数据则显示无数据底图
			if (data.content != null && data.content.size() == 0)
			{
				pullToListView.setVisibility(View.GONE);
				emptyView.setVisibility(View.VISIBLE);
			}
			else
			{
				pullToListView.setVisibility(View.VISIBLE);
				emptyView.setVisibility(View.GONE);
				adapter = new RFQListAdapter(getActivity(), getRFQStatus(), data.content);
				listView.setAdapter(adapter);
			}
			break;
		case REQUEST_TYPE_GET_MORE:
			// 关闭点击加载更多加载状态
			if (adapter != null && data != null && data.content.size() != 0)
			{
				adapter.addAdapterData(data.content);
			}
			break;
		default:
			break;
		}
		requestTypeState = REQUEST_TYPE_DEFAULT;
		refreshComplete();
	}

	@Override
	public void onFailure(Object failedReason)
	{
		if (getActivity() == null || (getActivity() != null && getActivity().isFinishing()))
			return;
		dismissProgress();
		pullToListView.setVisibility(View.VISIBLE);
		ToastUtil.toast(getActivity(), failedReason);
		requestTypeState = REQUEST_TYPE_DEFAULT;
		CommonProgressDialog.getInstance().dismissProgressDialog();
	}

	protected void dismissProgress()
	{
		if (progressBarLayout != null)
		{
			progressBarLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.rfq_list_updated_date_layout:
			switch (orderByType)
			{
			case DESC:
				orderByType = RFQOrderType.ASC;
				arrowView.setBackgroundResource(R.drawable.ic_rfq_arrow_up);
				break;
			case ASC:
				orderByType = RFQOrderType.DESC;
				arrowView.setBackgroundResource(R.drawable.ic_rfq_arrow_down);
				break;
			}
			// 事件统计 57 Sourcing Request列表排序 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c57);
			resetListByTime();
			break;
		}
	}

	/**
	 * 初始化排序类型
	 */
	protected void initOrderType()
	{
		orderByType = RFQOrderType.DESC;
		if (arrowView != null)
		{
			arrowView.setBackgroundResource(R.drawable.ic_rfq_arrow_down);
		}
	}

	protected void setListViewParams(LayoutInflater inflater)
	{
		pullToListView.getLoadingLayoutProxy().setLastUpdatedLabel(
				SharedPreferenceManager.getInstance().getString(getChildFragmentTag(), ""));
		pullToListView.setOnRefreshListener(this);
		pullToListView.setOnPullEventListener(pullEventListener);
		// 只启动下拉刷新
		pullToListView.setMode(Mode.BOTH);
		// 不显示指示器
		pullToListView.setShowIndicator(false);
		pullToListView.setVisibility(View.GONE);
		listView = pullToListView.getRefreshableView();
		listView.setOnItemClickListener(this);
		listView.addHeaderView(initHeaderView(inflater));
	}

	protected View initHeaderView(LayoutInflater inflater)
	{
		View view = inflater.inflate(R.layout.rfq_list_head, null);
		LinearLayout updatedDateLayout = (LinearLayout) view.findViewById(R.id.rfq_list_updated_date_layout);
		arrowView = updatedDateLayout.findViewById(R.id.iv_rfq_list_updated_date_arrow);
		tvDate = (TextView) updatedDateLayout.findViewById(R.id.tv_rfq_list_date);
		updatedDateLayout.setOnClickListener(this);
		return view;
	}

	/**
	 * 开始请求数据
	 * @param status
	 * @param pageIndex
	 * @param pageNum
	 */
	protected void startRequest(RFQStatus status, int pageIndex, int pageNum, String orderByType)
	{
		RequestCenter.getRFQList(this, status.toString(), pageIndex, pageNum, orderByType, getRFQListOrderByField());
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

	/**
	 * 第一次获取数据列表
	 */
	private void requestQRList()
	{
		requestTypeState = REQUEST_TYPE_FIRST_REQUEST;
		startRequest(getRFQStatus(), pageNo, pageSize, orderByType.toString());
	}

	/**
	 * 刷新数据列表
	 */
	private void requestRefreshQRList()
	{
		requestTypeState = REQUEST_TYPE_REFRESH_LIST;
		startRequest(getRFQStatus(), 1, pageSize * pageNo, orderByType.toString());
	}

	/**
	 * 获取下一页数据列表
	 */
	public void onGetMoreRefresh()
	{
		requestTypeState = REQUEST_TYPE_GET_MORE;
		startRequest(getRFQStatus(), pageNo, pageSize, orderByType.toString());
	}

	/**
	 * 加载网络请求数据
	 */
	public void onLoadData()
	{
		Log.e(getChildFragmentTag(), "==onLoadData==");
		if (getActivity() != null && !getActivity().isFinishing() && progressBarLayout != null
				&& !progressBarLayout.isShown() && !CommonProgressDialog.getInstance().isShowing())
		{
			CommonProgressDialog.getInstance().showCancelableProgressDialog(getActivity(),
					getActivity().getString(R.string.mic_loading));
		}
		initCache();
		requestQRList();
	}

	/**
	 * 清除数据
	 */
	public void onClearData()
	{
		Log.e(getChildFragmentTag(), "==onClearData==");
		dismissProgress();
		initOrderType();
	}

	/**
	 * 下拉刷新回调
	 * @param refreshView
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
	{
		initCache();
		saveChildLastRefreshTime();
		requestRefreshQRList();
	}

	/**
	 * 上拉刷新回调
	 * @param refreshView
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
	{
		pageNo++;
		onGetMoreRefresh();
	}

	/**
	 * 根据日期重置列表书序(升序或倒序)
	 */
	protected void resetListByTime()
	{
		if (getActivity() != null && !getActivity().isFinishing() && !CommonProgressDialog.getInstance().isShowing())
		{
			CommonProgressDialog.getInstance().showCancelableProgressDialog(getActivity(),
					getActivity().getString(R.string.mic_loading));
		}
		initCache();
		requestRefreshQRList();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		final RFQContent willDel = (RFQContent) arg0.getItemAtPosition(arg2);
		if ("stopped".equals(willDel.status))
			return true;
		if (dialog != null)
		{
			dialog.show();
			return true;
		}
		dialog = new CommonDialog(getActivity(), getString(R.string.mic_rfq_delete), getString(R.string.mic_ok),
				getString(R.string.cancel), Util.dip2px(258), new DialogClickListener()
				{
					@Override
					public void onDialogClick()
					{
						delRFQItem(willDel.rfqId);
					}
				});
		dialog.show();
		return true;
	}

	private void delRFQItem(String rfqId)
	{
		CommonProgressDialog.getInstance().showProgressDialog(getActivity(), getString(R.string.mic_loading));
		RequestCenter.delRFQByRFQId(new DisposeDataListener()
		{
			@Override
			public void onSuccess(Object result)
			{
				if (getActivity() == null || (getActivity() != null && getActivity().isFinishing()))
					return;
				onLoadData();
			}

			@Override
			public void onFailure(Object failedReason)
			{
				if (getActivity() == null || (getActivity() != null && getActivity().isFinishing()))
					return;
				CommonProgressDialog.getInstance().dismissProgressDialog();
				ToastUtil.toast(getActivity(), failedReason);
			}
		}, rfqId);
	}

}
