package com.micen.buyers.adapter;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.adapter.searchresult.ProductBaseAdapter;
import com.micen.buyers.module.search.SearchProduct;

/**********************************************************
 * @文件名称：SpecialDetailGridAdapter.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年4月30日 下午3:15:01
 * @文件描述：专题详情产品列表适配器
 * @修改历史：2015年4月30日创建初始版本
 **********************************************************/
public class SpecialDetailGridAdapter extends ProductBaseAdapter
{
	private ViewHolder holder;

	public SpecialDetailGridAdapter(Context context, ArrayList<SearchProduct> dataList)
	{
		super(context, dataList, true);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.product_gridlist_item_special, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.product_item_image);
			holder.name = (TextView) convertView.findViewById(R.id.product_item_name);
			holder.tv1 = (TextView) convertView.findViewById(R.id.product_item1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.product_item2);
			holder.iv1 = (ImageView) convertView.findViewById(R.id.product_item_image1);
			holder.iv2 = (ImageView) convertView.findViewById(R.id.product_item_image2);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		product = (SearchProduct) mProductList.get(position);

		/**
		 * 显示安全图片对话框
		 */
		showFitImage(product.catCode, product.image.trim(), holder.imageView);

		if (!TextUtils.isEmpty(product.memberType) && product.memberType.equals("1"))
		{
			holder.iv1.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.iv1.setVisibility(View.GONE);
		}
		if (TextUtils.isEmpty(product.auditType))
		{
			holder.iv2.setVisibility(View.GONE);
		}
		else if (product.auditType.equals("1"))
		{
			holder.iv2.setVisibility(View.VISIBLE);
			holder.iv2.setBackgroundResource(R.drawable.ic_supplier_as);
		}
		else if (product.auditType.equals("0"))
		{
			holder.iv2.setVisibility(View.GONE);
		}
		else if (product.auditType.equals("2"))
		{
			holder.iv2.setVisibility(View.VISIBLE);
			holder.iv2.setBackgroundResource(R.drawable.ic_supplier_oc);
		}
		else if (product.auditType.equals("3"))
		{
			holder.iv2.setVisibility(View.VISIBLE);
			holder.iv2.setBackgroundResource(R.drawable.ic_supplier_lv);
		}
		holder.name.setText(product.name);

		if (null != product.prodProps)
		{
			if (product.prodProps.size() == 0)
			{
				holder.tv1.setVisibility(View.GONE);
				holder.tv2.setVisibility(View.GONE);
			}
			else if (product.prodProps.size() == 1)
			{
				for (Map.Entry<String, Object> entry : product.prodProps.entrySet())
				{
					holder.tv1.setText(entry.getKey().toString() + ":" + entry.getValue().toString());
				}
				holder.tv2.setVisibility(View.GONE);
			}
			else if (product.prodProps.size() > 1)
			{
				int i = 0;
				for (Map.Entry<String, Object> entry : product.prodProps.entrySet())
				{
					if (0 == i)
					{
						holder.tv1.setText(entry.getKey().toString() + ":" + entry.getValue().toString());
						i = 1;
					}
					else
					{
						holder.tv2.setText(entry.getKey().toString() + ":" + entry.getValue().toString());
					}
				}
			}
		}
		else
		{
			holder.tv1.setVisibility(View.GONE);
			holder.tv2.setVisibility(View.GONE);
		}

		/*
		 * if (null != product.unitPrice && !"".equals(product.unitPrice.trim())) {
		 * holder.tv1.setVisibility(View.VISIBLE);
		 * holder.tv1.setText(getHtmlText(mContext.getString(R.string.unit_price), product.unitPrice)); } else { if
		 * (null != product.tradeTerm && !"".equals(product.tradeTerm.trim())) { holder.tv1.setVisibility(View.VISIBLE);
		 * holder.tv1.setText(getHtmlText(mContext.getString(R.string.trade_terms), product.tradeTerm)); } else {
		 * holder.tv1.setVisibility(View.GONE); } } if (null != product.minOrder && !"".equals(product.minOrder.trim()))
		 * { holder.tv2.setVisibility(View.VISIBLE);
		 * holder.tv2.setText(getHtmlText(mContext.getString(R.string.min_order), product.minOrder)); } else {
		 * holder.tv2.setVisibility(View.GONE); }
		 */
		return convertView;
	}

	private Spanned getHtmlText(String name, String value)
	{
		String businessTypeTitle = "<font color=\"" + mContext.getResources().getColor(R.color.search_list_item_right)
				+ "\">" + name + "</font>";
		String businessTypeValue = "<font color=\"" + mContext.getResources().getColor(R.color.mic_color666666) + "\">"
				+ value + "</font>";
		return Html.fromHtml(businessTypeTitle + businessTypeValue);
	}

	private static class ViewHolder
	{
		ImageView imageView;
		TextView name;
		TextView tv1;
		TextView tv2;
		ImageView iv1;
		ImageView iv2;
	}

}
