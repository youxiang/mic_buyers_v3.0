package com.micen.buyers.view.rfq;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.micen.buyers.activity.R;
import com.micen.buyers.activity.rfq.RFQDetailActivity_;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.rfq.RFQContent;
import com.micen.buyers.module.rfq.RFQStatus;

/**********************************************************
 * @文件名称：PendingFragment.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月24日 下午3:24:16
 * @文件描述：pending,processing状态的RFQ列表Fragment
 * @修改历史：2015年3月24日创建初始版本
 **********************************************************/
@EFragment(R.layout.rfq_list)
public class PendingFragment extends RFQBaseFragment
{
	protected String TAG = "PendingFragment";

	public PendingFragment()
	{

	}

	@AfterViews
	protected void initView()
	{
		setListViewParams(getActivity().getLayoutInflater());
		emptyView.setBackgroundResource(R.drawable.rfq_list_empty);
		listView.setOnItemLongClickListener(this);
	}

	@Override
	protected String getChildFragmentTag()
	{
		return TAG;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		if (arg2 != 0)
		{
			// 事件统计 58 浏览详情（Sourcing Request列表页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c58);
			Intent intent = new Intent();
			intent.setClass(getActivity(), RFQDetailActivity_.class);
			intent.putExtra("rfqStatus", getRFQStatus().toString());
			intent.putExtra("rfqId", ((RFQContent) arg0.getAdapter().getItem(arg2)).rfqId);
			startActivity(intent);
		}
	}

	@Override
	public String getRFQListOrderByField()
	{
		return "ADD_TIME";
	}

	@Override
	protected RFQStatus getRFQStatus()
	{
		return RFQStatus.Pending;
	}
}
