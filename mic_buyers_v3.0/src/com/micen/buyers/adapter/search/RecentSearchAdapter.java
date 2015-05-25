package com.micen.buyers.adapter.search;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.db.SearchRecord;

/**********************************************************
 * @文件名称：RecentSearchAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月10日 上午11:47:05
 * @文件描述：关键词搜索列表Adapter
 * @修改历史：2015年3月10日创建初始版本
 **********************************************************/
public class RecentSearchAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<SearchRecord> recentSearchDataList;

	public RecentSearchAdapter(Context context, ArrayList<SearchRecord> recentSearchDataList)
	{
		this.context = context;
		this.recentSearchDataList = recentSearchDataList;
	}

	@Override
	public int getCount()
	{
		if (recentSearchDataList == null)
		{
			return 0;
		}
		return recentSearchDataList.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		return recentSearchDataList.get(arg0);
	}

	@Override
	public long getItemId(int arg0)
	{
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2)
	{
		ViewHolder holder;
		if (arg1 == null)
		{
			holder = new ViewHolder();
			arg1 = LayoutInflater.from(context).inflate(R.layout.search_recent_listview_item_layout, null);
			holder.keywords = (TextView) arg1.findViewById(R.id.mic_search_keywords_item_text);

			arg1.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) arg1.getTag();
		}
		SearchRecord recentSearchKeywords = (SearchRecord) getItem(arg0);
		holder.keywords.setText(recentSearchKeywords.recentKeywords);
		return arg1;
	}

	public ArrayList<SearchRecord> getRecentSearchDataList()
	{
		return recentSearchDataList;
	}

	public void setRecentSearchDataList(ArrayList<SearchRecord> recentSearchDataList)
	{
		this.recentSearchDataList = recentSearchDataList;
		this.notifyDataSetChanged();
	}

	private static class ViewHolder
	{
		TextView keywords;
	}

}
