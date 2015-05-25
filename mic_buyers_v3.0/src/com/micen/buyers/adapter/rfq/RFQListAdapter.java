package com.micen.buyers.adapter.rfq;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.rfq.RFQContent;
import com.micen.buyers.module.rfq.RFQStatus;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：RFQListAdapter.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014-1-28 下午04:20:20
 * @文件描述：RFQ管理页列表适配器
 * @修改历史：2014-1-28创建初始版本
 **********************************************************/
public class RFQListAdapter extends BaseAdapter
{
	private Context mContext;
	private ArrayList<RFQContent> dataList;
	private RFQContent module;
	private RFQStatus status;
	private ViewHolder holder;

	public RFQListAdapter(Context context, RFQStatus status, ArrayList<RFQContent> dataList)
	{
		this.mContext = context;
		this.dataList = dataList;
		this.status = status;
	}

	@Override
	public int getCount()
	{
		return dataList.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		return dataList.get(arg0);
	}

	@Override
	public long getItemId(int arg0)
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.rfq_list_item, null);
			holder = new ViewHolder();
			holder.unReadCount = (TextView) convertView.findViewById(R.id.tv_rfq_list_item_unread);
			holder.subjectsText = (TextView) convertView.findViewById(R.id.tv_rfq_list_item_subjects);
			holder.expiredDateText = (TextView) convertView.findViewById(R.id.tv_rfq_list_item_expiredDate);
			holder.rejectLayout = (RelativeLayout) convertView.findViewById(R.id.rl_rfq_reject_layout);
			holder.rejectText = (TextView) convertView.findViewById(R.id.tv_rfq_reject_reason);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		module = dataList.get(position);
		holder.subjectsText.setText(module.subject);
		switch (status)
		{
		case Rejected:
			holder.rejectLayout.setVisibility(View.VISIBLE);
			holder.rejectText.setText(module.returnAdvise);
			// holder.expiredDateText.setText(Util.formatDateToEn(module.validateTimeEnd.time));
			holder.expiredDateText.setText(Util.formatDateToEn(module.addTime.time));
			break;
		case Quotation:
			holder.rejectLayout.setVisibility(View.GONE);
			holder.expiredDateText.setText(Util.formatDateToEn(module.addTime.time));
			if (module.unreadCount != null && !"".equals(module.unreadCount) && !"0".equals(module.unreadCount))
			{
				holder.unReadCount.setVisibility(View.VISIBLE);
				holder.unReadCount.setText(module.unreadCount);
			}
			else
			{
				holder.unReadCount.setVisibility(View.GONE);
			}
			break;
		default:
			holder.rejectLayout.setVisibility(View.GONE);
			holder.expiredDateText.setText(Util.formatDateToEn(module.addTime.time));
			break;
		}
		return convertView;
	}

	static class ViewHolder
	{
		TextView unReadCount;
		TextView subjectsText;
		TextView expiredDateText;
		TextView rejectText;
		RelativeLayout rejectLayout;
	}

	public void addAdapterData(ArrayList<RFQContent> dataList)
	{
		if (dataList != null)
		{
			this.dataList.addAll(dataList);
			notifyDataSetChanged();
		}
	}
}
