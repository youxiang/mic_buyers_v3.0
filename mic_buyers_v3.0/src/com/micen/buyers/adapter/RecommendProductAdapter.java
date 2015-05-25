package com.micen.buyers.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.RecommendProduct;
import com.micen.buyers.util.ImageUtil;

/**********************************************************
 * @文件名称：RecommendProductAdapter.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年5月4日 下午2:05:38
 * @文件描述：推荐产品列表适配器
 * @修改历史：2015年5月4日创建初始版本
 **********************************************************/
public class RecommendProductAdapter extends BaseAdapter
{
	private Activity mActivity;
	private ArrayList<RecommendProduct> dataList;
	private RecommendProduct product;
	private ViewHolder holder;

	public RecommendProductAdapter(Activity activity, ArrayList<RecommendProduct> dataList)
	{
		this.mActivity = activity;
		this.dataList = dataList;
	}

	@Override
	public int getCount()
	{
		return dataList.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(mActivity).inflate(R.layout.recommend_list_item, parent, false);
			holder = new ViewHolder();
			holder.iv = (ImageView) convertView.findViewById(R.id.iv_recommend_item);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		product = dataList.get(position);
		ImageUtil.getImageLoader().displayImage(product.image, holder.iv, ImageUtil.getRecommendImageOptions());
		return convertView;
	}

	static class ViewHolder
	{
		ImageView iv;
	}

	@Override
	public Object getItem(int position)
	{
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

}
