package com.micen.buyers.adapter.rfq;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.category.SearchCategoryContent;

/**********************************************************
 * @文件名称：CategorySearchListAdapter.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年5月7日 下午5:13:06
 * @文件描述：RFQ目录搜索结果列表适配器
 * @修改历史：2015年5月7日创建初始版本
 **********************************************************/
public class CategorySearchListAdapter extends BaseAdapter
{
	private ArrayList<SearchCategoryContent> dataList;
	private LayoutInflater inflater;
	private SearchCategoryContent category;
	private ViewHolder holder;

	public CategorySearchListAdapter(Context context, ArrayList<SearchCategoryContent> dataList)
	{
		this.dataList = dataList;
		inflater = LayoutInflater.from(context);
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
	public View getView(int arg0, View contentView, ViewGroup arg2)
	{
		if (contentView == null)
		{
			holder = new ViewHolder();
			contentView = inflater.inflate(R.layout.mic_rfq_unit_popup_item, arg2, false);
			contentView.setBackgroundResource(R.drawable.bg_list_selector);
			holder.tv = (TextView) contentView.findViewById(R.id.mic_rfq_unit_popup_text);
			holder.tv.setTextSize(14);
			contentView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) contentView.getTag();
		}
		category = (SearchCategoryContent) getItem(arg0);
		holder.tv.setText(category.getAllCatNames());
		return contentView;
	}

	private static class ViewHolder
	{
		private TextView tv;
	}

}
