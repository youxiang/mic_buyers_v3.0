package com.micen.buyers.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.product.ProductKeyValuePair;

public class AuditProfileListAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<ProductKeyValuePair> dataList;
	private ProductKeyValuePair pair;
	private ViewHolder holder;

	public AuditProfileListAdapter(Context context, ArrayList<ProductKeyValuePair> dataList)
	{
		this.context = context;
		this.dataList = dataList;
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
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2)
	{
		if (arg1 == null)
		{
			arg1 = LayoutInflater.from(context).inflate(R.layout.audit_profile_list_item, null);
			holder = new ViewHolder();
			holder.tvName = (TextView) arg1.findViewById(R.id.ar_item_name);
			holder.tvValue = (TextView) arg1.findViewById(R.id.ar_item_value);
			arg1.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) arg1.getTag();
		}
		pair = dataList.get(arg0);
		holder.tvName.setText(pair.key);
		holder.tvValue.setText(pair.value.toString());
		return arg1;
	}

	static class ViewHolder
	{
		TextView tvName;
		TextView tvValue;
	}

}
