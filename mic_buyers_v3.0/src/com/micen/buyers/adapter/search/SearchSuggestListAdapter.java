package com.micen.buyers.adapter.search;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.search.Association;

/**********************************************************
 * @文件名称：SearchSuggestListAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月10日 上午11:40:34
 * @文件描述：搜索关键词联想Adapter
 * @修改历史：2015年3月10日创建初始版本
 **********************************************************/
public class SearchSuggestListAdapter extends BaseAdapter
{
	private ArrayList<Association> dataList;
	private Association association;
	private ViewHolder holder;
	private Context context;

	public SearchSuggestListAdapter(Context context, ArrayList<Association> dataList)
	{
		this.context = context;
		this.dataList = dataList;
	}

	@Override
	public int getCount()
	{
		if (dataList.size() > 5)
		{
			return 5;
		}
		else
		{
			return dataList.size();
		}
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
			contentView = LayoutInflater.from(context).inflate(R.layout.search_suggest_listview_item_layout, null);
			holder.suggestText = (TextView) contentView.findViewById(R.id.mic_search_suggest_text);
			holder.suggestNum = (TextView) contentView.findViewById(R.id.mic_search_suggest_num);
			contentView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) contentView.getTag();
		}
		association = (Association) getItem(arg0);
		holder.suggestText.setText(association.text);
		holder.suggestNum.setText(context.getString(R.string.mic_search_suggest_num_left) + association.result
				+ context.getString(R.string.mic_search_suggest_num_right1));
		return contentView;
	}

	private static class ViewHolder
	{
		private TextView suggestText;
		private TextView suggestNum;
	}
}