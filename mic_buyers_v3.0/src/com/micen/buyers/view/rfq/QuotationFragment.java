package com.micen.buyers.view.rfq;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.micen.buyers.activity.R;
import com.micen.buyers.activity.rfq.RFQQuotationListActivity_;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.rfq.RFQContent;
import com.micen.buyers.module.rfq.RFQStatus;

/**********************************************************
 * @文件名称：QuotationFragment.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月24日 下午3:25:35
 * @文件描述：responding,quoted状态的RFQ列表Fragment
 * @修改历史：2015年3月24日创建初始版本
 **********************************************************/
@EFragment(R.layout.rfq_list)
public class QuotationFragment extends RFQBaseFragment
{
	protected String TAG = "QuotationFragment";

	public QuotationFragment()
	{

	}

	@AfterViews
	protected void initView()
	{
		emptyView.setBackgroundResource(R.drawable.rfq_quotation_list_empty);
		setListViewParams(getActivity().getLayoutInflater());
		listView.setOnItemLongClickListener(this);
	}

	@Override
	protected String getChildFragmentTag()
	{
		return TAG;
	}

	/**
	 * 数据列表点击事件
	 * 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		if (arg2 != 0)
		{
			RFQContent rfqContent = (RFQContent) adapter.getItem(arg2 - 1);
			String rfqId = rfqContent.rfqId;
			if (rfqId != null && !"".equals(rfqId))
			{
				// 事件统计64 点击查看某个RFQ报价列表 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c64);

				Intent intent = new Intent(getActivity(), RFQQuotationListActivity_.class);
				intent.putExtra("rfqID", rfqId);
				intent.putExtra("rfqSubject", rfqContent.subject);
				startActivity(intent);
			}
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
			// 事件统计 63 点击排序（RFQ报价列表页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c63);
			resetListByTime();
			break;
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
		return RFQStatus.Quotation;
	}

	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		// 事件统计 $10014	RFQ报价列表页	页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10014);
	}
	
	
}
