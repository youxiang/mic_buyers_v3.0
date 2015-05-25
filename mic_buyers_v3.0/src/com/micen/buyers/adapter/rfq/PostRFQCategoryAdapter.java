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
 * @文件名称：PostRFQCategoryAdapter.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014-2-17 下午01:36:33
 * @文件描述：RFQ发布页目录弹出列表适配器
 * @修改历史：2014-2-17创建初始版本
 **********************************************************/
public class PostRFQCategoryAdapter extends BaseAdapter
{
	private ArrayList<SearchCategoryContent> dataList;
	private LayoutInflater inflater;
	private Context context;
	private SearchCategoryContent category;
	private ViewHolder holder;

	public PostRFQCategoryAdapter(Context context, ArrayList<SearchCategoryContent> dataList)
	{
		this.context = context;
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
			contentView = inflater.inflate(R.layout.mic_rfq_category_popup_item, null);
			holder.tv = (TextView) contentView.findViewById(R.id.mic_rfq_category_popup_text);
			contentView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) contentView.getTag();
		}
		holder.tv.setTextSize(arg0 == getCount() - 1 ? 14 : 12);
		holder.tv.setTextColor(context.getResources().getColor(
				arg0 == getCount() - 1 ? R.color.mic_common_title_bg : R.color.mic_home_menu_text));
		category = (SearchCategoryContent) getItem(arg0);
		holder.tv.setText(category.getAllCatNames());
		return contentView;
	}

	private static class ViewHolder
	{
		private TextView tv;
	}

}
