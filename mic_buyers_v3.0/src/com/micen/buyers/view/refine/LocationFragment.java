package com.micen.buyers.view.refine;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.micen.buyers.activity.R;
import com.micen.buyers.adapter.refine.RefineLocationAdapter;
import com.micen.buyers.module.search.SearchLocation;

/**********************************************************
 * @文件名称：LocationFragment.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月20日 下午7:06:34
 * @文件描述：地区选择Fragment
 * @修改历史：2015年3月20日创建初始版本
 **********************************************************/
public class LocationFragment extends BaseRefineFragment implements OnItemClickListener
{
	protected ArrayList<SearchLocation> siftLocationsList;
	protected RefineLocationAdapter locationAdapter;
	protected ListView locationListView;

	public LocationFragment()
	{
	}

	public LocationFragment(ArrayList<SearchLocation> siftLocationsList, SiftAllListener listener)
	{
		this.siftLocationsList = siftLocationsList;
		this.listener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (contentView == null)
		{
			contentView = inflater.inflate(R.layout.refine_category_fragment_layout, null);
			initView();
		}

		ViewGroup parent = (ViewGroup) contentView.getParent();
		if (parent != null)
		{
			parent.removeView(contentView);
		}
		return contentView;
	}

	private void initView()
	{
		locationListView = (ListView) contentView.findViewById(R.id.refine_category_list_view);
		locationAdapter = new RefineLocationAdapter(mContext, siftLocationsList);
		locationListView.setAdapter(locationAdapter);
		locationListView.setOnItemClickListener(this);
		initChoiceData();
	}

	private void initChoiceData()
	{
		String str = listener.getValue("location");
		if (str != null)
		{
			SearchLocation searchLocation = new SearchLocation();
			searchLocation.key = str;
			locationAdapter.choiceLocation(searchLocation);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		SearchLocation searchLocation = (SearchLocation) locationAdapter.getItem(position);
		searchLocation = locationAdapter.choiceLocation(searchLocation);
		if (searchLocation == null)
		{
			listener.sift(3, "");
		}
		else
		{
			listener.sift(3, searchLocation.key);
		}
	}

	@Override
	public void resetData()
	{
		if (locationAdapter != null)
		{
			locationAdapter.resetData();
		}
	}
}
