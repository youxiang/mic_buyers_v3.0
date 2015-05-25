package com.micen.buyers.adapter.search;

import java.util.ArrayList;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.search.SearchCompanyProduct;
import com.micen.buyers.util.ImageUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * ********************************************************
 * @文件名称：CompanyProductAdapter.java
 * @文件作者：xuliucheng
 * @创建时间：2015年5月7日 下午2:13:09
 * @文件描述：公司搜索列表中，公司的主要产品图片
 * @修改历史：2015年5月7日创建初始版本
 *********************************************************
 */
public class CompanyProductIconAdapter extends BaseAdapter
{
	private Context mContext;
	private ArrayList<SearchCompanyProduct> dataList;
	private SearchCompanyProduct product;
	private ViewHolder holder;

	public CompanyProductIconAdapter(Context context, ArrayList<SearchCompanyProduct> list)
	{
		mContext = context;
		dataList = list;
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return dataList.get(position);
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.companyproduct_icon_list_item, parent, false);
			holder = new ViewHolder();
			holder.iv = (ImageView) convertView.findViewById(R.id.iv_product_icon_item);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		product = dataList.get(position);
		ImageUtil.getImageLoader().displayImage(product.productImgUrl, holder.iv, ImageUtil.getRecommendImageOptions());
		return convertView;
	}

	public static class ViewHolder
	{
		ImageView iv;
	}

}
