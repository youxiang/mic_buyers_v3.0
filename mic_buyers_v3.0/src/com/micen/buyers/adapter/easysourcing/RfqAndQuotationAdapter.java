package com.micen.buyers.adapter.easysourcing;

import java.util.ArrayList;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.easysourcing.RfqAndQuotation;
import com.micen.buyers.util.ImageUtil;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RfqAndQuotationAdapter extends BaseAdapter
{
	private ArrayList<RfqAndQuotation> list;
	private RfqAndQuotation item;
	private ViewHolder holder;
	private Activity mActivity;

	public RfqAndQuotationAdapter(Activity activity, ArrayList<RfqAndQuotation> datalist)
	{
		mActivity = activity;
		list = datalist;
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		if (convertView == null)
		{
			// rfqandquotation_list_item.xml
			convertView = LayoutInflater.from(mActivity).inflate(R.layout.rfqandquotation_list_item, parent, false);
			holder = new ViewHolder();
			holder.iv = (ImageView) convertView.findViewById(R.id.category_image);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_categoryname);
			holder.tv_rfqnum = (TextView) convertView.findViewById(R.id.tv_rfqnum);
			holder.tv_quotationnum = (TextView) convertView.findViewById(R.id.tv_quotationnum);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		item = list.get(position);
		holder.tv_name.setText(item.catNameEn);
		holder.tv_quotationnum.setText(item.quotationNum);
		holder.tv_rfqnum.setText(item.rfqNum);
		ImageUtil.getImageLoader().displayImage(item.categoryURL, holder.iv, ImageUtil.getEasysouringImageOptions());
		return convertView;
	}

	static class ViewHolder
	{
		TextView tv_name;
		TextView tv_rfqnum;
		TextView tv_quotationnum;
		ImageView iv;
	}

}
