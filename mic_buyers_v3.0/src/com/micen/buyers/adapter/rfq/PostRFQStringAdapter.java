package com.micen.buyers.adapter.rfq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.micen.buyers.activity.R;

/**********************************************************
 * @文件名称：PostRFQStringAdapter.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014-2-17 下午01:36:33
 * @文件描述：RFQ发布页目录弹出列表适配器
 * @修改历史：2014-2-17创建初始版本
 **********************************************************/
public class PostRFQStringAdapter extends BaseAdapter
{
	private String[] dataList;
	private LayoutInflater inflater;
	private String str;
	private ViewHolder holder;

	public PostRFQStringAdapter(Context context, String[] dataList)
	{
		this.dataList = dataList;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		return dataList.length;
	}

	@Override
	public Object getItem(int arg0)
	{
		return dataList[arg0];
	}

	@Override
	public long getItemId(int arg0)
	{
		return 0;
	}

	@Override
	public View getView(int arg0, View contentView, ViewGroup arg2)
	{
		if (contentView == null)
		{
			holder = new ViewHolder();
			contentView = inflater.inflate(R.layout.mic_rfq_unit_popup_item, null);
			holder.tv = (TextView) contentView.findViewById(R.id.mic_rfq_unit_popup_text);
			contentView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) contentView.getTag();
		}
		str = getItem(arg0).toString();
		holder.tv.setText(str);
		return contentView;
	}

	private static class ViewHolder
	{
		private TextView tv;
	}

}
