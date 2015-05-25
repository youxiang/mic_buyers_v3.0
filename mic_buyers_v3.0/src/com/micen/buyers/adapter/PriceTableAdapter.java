package com.micen.buyers.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.product.ProductKeyValuePair;

/**********************************************************
 * @文件名称：PriceTableAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2014年9月9日 上午10:54:24
 * @文件描述：嵌套表格ListView中的Adapter
 * @修改历史：2014年9月9日创建初始版本
 **********************************************************/
public class PriceTableAdapter extends BaseAdapter
{
	private ArrayList<ProductKeyValuePair> list;
	private Context context;
	private ViewHolder holder;
	/**
	 * 从activity传来的样式数据
	 */
	private HashMap<String, Object> styleList;

	public PriceTableAdapter(Context context, ArrayList<ProductKeyValuePair> list, HashMap<String, Object> styleList)
	{
		this.context = context;
		this.list = list;
		this.styleList = styleList;
	}

	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public Object getItem(int position)
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.productdetail_pricelist_item, null);
			holder.tvPrice = (TextView) convertView.findViewById(R.id.price);
			holder.tvUnit = (TextView) convertView.findViewById(R.id.unit);
			holder.view = (View) convertView.findViewById(R.id.divider);
			initStyle();
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		ProductKeyValuePair pair = (ProductKeyValuePair) getItem(position);
		holder.tvPrice.setText(pair.value.toString());
		holder.tvUnit.setText(pair.key);
		return convertView;
	}

	private void initStyle()
	{
		holder.tvPrice.setTextColor((Integer) styleList.get("rightTextColor"));
		holder.tvUnit.setTextColor((Integer) styleList.get("rightTextColor"));
		holder.view.setBackgroundColor((Integer) styleList.get("dividerColor"));
		holder.tvPrice.setTextSize((Integer) styleList.get("rightTextSize"));
		holder.tvUnit.setTextSize((Integer) styleList.get("rightTextSize"));
	}

	public class ViewHolder
	{
		public TextView tvUnit;
		public TextView tvPrice;
		public View view;
	}
}
