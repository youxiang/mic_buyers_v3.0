package com.micen.buyers.view.mail.message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.focustech.common.mob.DisposeDataListener;
import com.focustech.common.util.ToastUtil;
import com.focustech.common.widget.dialog.CommonProgressDialog;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase.OnPullEventListener;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.focustech.common.widget.pulltorefresh.PullToRefreshBase.State;
import com.focustech.common.widget.pulltorefresh.PullToRefreshListView;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.message.MailDetailActivity_;
import com.micen.buyers.adapter.mail.MailListAdapter;
import com.micen.buyers.db.SharedPreferenceManager;
import com.micen.buyers.http.RequestCenter;
import com.micen.buyers.module.mail.Mail;
import com.micen.buyers.module.mail.Mails;
import com.micen.buyers.util.BuyerApplication;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：MailBaseFragment.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月25日 上午9:11:22
 * @文件描述：邮件列表父Fragment
 * @修改历史：2015年3月25日创建初始版本
 **********************************************************/
@EFragment
public abstract class MailBaseFragment extends Fragment implements DisposeDataListener, OnRefreshListener2<ListView>,
		OnItemClickListener, OnItemLongClickListener, OnClickListener
{
	@ViewById(R.id.mail_list_empty)
	protected ImageView ivEmpty;
	@ViewById(R.id.lv_mail_list)
	protected PullToRefreshListView pullToListView;
	@ViewById(R.id.mail_delete_layout)
	protected RelativeLayout deleteLayout;
	@ViewById(R.id.progress_bar)
	protected RelativeLayout progressBar;
	protected int pageIndex = 1;
	protected MailListAdapter adapter;
	protected ListView lvMailList;
	protected boolean isRefresh = false;
	protected ArrayList<Mail> mailDataList;
	protected boolean isShowRefreshToast = true;

	public MailBaseFragment()
	{

	}

	/**
	 * 获取子类的标示
	 * @return
	 */
	protected abstract String getChildTag();

	/**
	 * 获取子类的行为
	 * @return
	 */
	protected abstract String getAction();

	@AfterViews
	protected void initView()
	{
		pullToListView.getLoadingLayoutProxy().setLastUpdatedLabel(
				SharedPreferenceManager.getInstance().getString(getChildTag(), ""));
		pullToListView.setOnRefreshListener(this);
		pullToListView.setOnPullEventListener(pullEventListener);
		pullToListView.setMode(Mode.BOTH);
		// 不显示指示器
		pullToListView.setShowIndicator(false);
		mailDataList = new ArrayList<Mail>();
		lvMailList = pullToListView.getRefreshableView();
		lvMailList.setOnItemClickListener(this);
		lvMailList.setOnItemLongClickListener(this);
	}

	/**
	 * 请求邮件列表
	 */
	public void startGetMailList()
	{
		if (isRefresh)
		{
			pageIndex = 1;
		}
		startGetMailList(pageIndex, getAction());
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		Intent intent = new Intent(getActivity(), MailDetailActivity_.class);
		intent.putExtra("position", String.valueOf(arg2));
		intent.putExtra("action", getAction());
		getActivity().startActivity(intent);
	}

	@Override
	public void onSuccess(Object arg0)
	{
		if (getActivity() != null && getActivity().isFinishing())
			return;
		progressBar.setVisibility(View.GONE);
		CommonProgressDialog.getInstance().dismissProgressDialog();
		Mails mails = (Mails) arg0;
		refreshComplete();
		if (mails.content != null && mails.content.size() != 0)
		{
			ivEmpty.setVisibility(View.GONE);
			if (isRefresh)
			{
				mailDataList.clear();
			}
			for (int i = 0; i < mails.content.size(); i++)
			{
				mailDataList.add(mails.content.get(i));
			}
			BuyerApplication.getInstance().setMailDataList(mailDataList);// 保存收件箱列表
			if (adapter == null || isRefresh)
			{
				adapter = new MailListAdapter(getActivity(), mails.content, getAction(), handler);
				lvMailList.setAdapter(adapter);
			}
			else
			{
				adapter.addData(mails.content);
			}
		}
		else
		{
			if (adapter == null)
			{
				ivEmpty.setVisibility(View.VISIBLE);
				pullToListView.setVisibility(View.GONE);
			}
			else
			{
				ivEmpty.setVisibility(View.GONE);
				pullToListView.setVisibility(View.VISIBLE);
			}
		}
		if (isRefresh)
		{
			if (isShowRefreshToast)
			{
				ToastUtil.toast(getActivity(), R.string.refresh_success);
			}
			isRefresh = false;
		}
	}

	/**
	 * 刷新邮件列表
	 * @param isShowRefreshToast	是否显示刷新成功Toast
	 */
	public void startRefreshMailList(boolean isShowRefreshToast)
	{
		this.isRefresh = true;
		this.isShowRefreshToast = isShowRefreshToast;
		/**
		 * 底层进度条显示,则不显示Dialog,底层进度条显示过后才提示对话框
		 */
		if (getActivity() != null && !getActivity().isFinishing() && !progressBar.isShown()
				&& !CommonProgressDialog.getInstance().isShowing())
		{
			CommonProgressDialog.getInstance().showCancelableProgressDialog(getActivity(),
					getActivity().getString(R.string.mic_loading));
		}
		startGetMailList();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
	{
		this.isRefresh = true;
		cancelDeleteMode();
		saveChildLastRefreshTime();
		startGetMailList();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
	{
		pageIndex++;
		startGetMailList();
	}

	@Override
	public void onFailure(Object failedReason)
	{
		if (getActivity() != null && getActivity().isFinishing())
			return;
		CommonProgressDialog.getInstance().dismissProgressDialog();
		refreshComplete();
		progressBar.setVisibility(View.GONE);
		if (adapter == null)
		{
			ivEmpty.setVisibility(View.VISIBLE);
			pullToListView.setVisibility(View.GONE);
		}
		ToastUtil.toast(getActivity(), failedReason);
	}

	/**
	 * 退出删除模式
	 * @return
	 */
	public boolean cancelDeleteMode()
	{
		if (deleteLayout != null && deleteLayout.getVisibility() == View.VISIBLE)
		{
			deleteLayout.setVisibility(View.GONE);
			adapter.setDeleteMode(false);
			adapter.selectMailList.clear();
			return false;
		}
		else
		{
			return true;
		}
	}

	protected void startGetMailList(int pageIndex, String action)
	{
		RequestCenter.getMailList(this, "", String.valueOf(pageIndex), "20", "-1", action);
	}

	/**
	 * 删除收件箱或者发件箱中的邮件
	 * @param mailId	邮件ID，多个以","隔开
	 */
	protected void startDeleteMail(String mailId, String action)
	{
		RequestCenter.deleteMail(new DisposeDataListener()
		{
			@Override
			public void onSuccess(Object arg0)
			{
				startRefreshMailList(false);
			}

			@Override
			public void onFailure(Object failedReason)
			{
				CommonProgressDialog.getInstance().dismissProgressDialog();
				ToastUtil.toast(getActivity(), failedReason);
			}
		}, mailId, action);
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
						SharedPreferenceManager.getInstance().getString(getChildTag(), ""));
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
		SharedPreferenceManager.getInstance().putString(getChildTag(), label);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		adapter.setDeleteMode(true);
		deleteLayout.setVisibility(View.VISIBLE);
		return false;
	}

	protected Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 1:
				if (deleteLayout != null)
				{
					deleteLayout.setEnabled(false);
					((ImageView) deleteLayout.findViewById(R.id.iv_delete)).setImageResource(R.drawable.ic_delete_gray);
				}
				break;
			case 2:
				if (deleteLayout != null)
				{
					deleteLayout.setEnabled(true);
					deleteLayout.setOnClickListener(MailBaseFragment.this);
					((ImageView) deleteLayout.findViewById(R.id.iv_delete)).setImageResource(R.drawable.ic_delete);
				}
				break;
			}
		};
	};

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.mail_delete_layout:
			if (adapter.selectMailList != null && adapter.selectMailList.size() > 0)
			{
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < adapter.selectMailList.size(); i++)
				{
					sb.append(adapter.selectMailList.get(i));
					if (i != adapter.selectMailList.size() - 1)
						sb.append(",");
				}
				if (getActivity() != null && !getActivity().isFinishing() && !progressBar.isShown()
						&& !CommonProgressDialog.getInstance().isShowing())
				{
					CommonProgressDialog.getInstance().showCancelableProgressDialog(getActivity(),
							getActivity().getString(R.string.mic_loading));
				}
				startDeleteMail(sb.toString(), getAction());
			}
			cancelDeleteMode();
			break;
		}
	}
}
