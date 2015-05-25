package com.micen.buyers.adapter.searchresult;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.focustech.common.util.Utils;
import com.micen.buyers.activity.R;
import com.micen.buyers.module.search.SearchProduct;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：ProductGridAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月18日 下午1:41:06
 * @文件描述：
 * @修改历史：2015年3月18日创建初始版本
 **********************************************************/
public class ProductGridAdapter extends ProductBaseAdapter
{
	private ViewHolder holder;

	public ProductGridAdapter(Context context, ArrayList<SearchProduct> productList)
	{
		this(context, productList, true);
	}

	public ProductGridAdapter(Context context, ArrayList<SearchProduct> productList, boolean showSupplierType)
	{
		super(context, productList, showSupplierType);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.product_gridlist_item_search, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.product_item_image);
			holder.name = (TextView) convertView.findViewById(R.id.product_item_name);
			holder.ll_price = (LinearLayout) convertView.findViewById(R.id.ll_product_price);
			holder.price = (TextView) convertView.findViewById(R.id.product_price);
			holder.priceunit = (TextView) convertView.findViewById(R.id.product_price_unit);

			holder.ll_other = (LinearLayout) convertView.findViewById(R.id.ll_product_other);
			holder.othername = (TextView) convertView.findViewById(R.id.product_other_name);
			holder.othervalue = (TextView) convertView.findViewById(R.id.product_other_value);

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

		if (showSupplierType && !TextUtils.isEmpty(product.memberType) && product.memberType.equals("1"))
		{
			holder.iv1.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.iv1.setVisibility(View.GONE);
		}
		if (!showSupplierType || TextUtils.isEmpty(product.auditType))
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
		/*
		 * if (null != product.unitPrice && !"".equals(product.unitPrice.trim())) {
		 * holder.price.setVisibility(View.VISIBLE); //
		 * holder.price.setText(getHtmlText(mContext.getString(R.string.unit_price), product.unitPrice));
		 * holder.price.setText(product.unitPrice); } else { if (null != product.tradeTerm &&
		 * !"".equals(product.tradeTerm.trim())) { holder.price.setVisibility(View.VISIBLE); //
		 * holder.price.setText(getHtmlText(mContext.getString(R.string.trade_terms), product.tradeTerm));
		 * holder.price.setText(product.tradeTerm); } else { holder.price.setVisibility(View.GONE); } }
		 */

		if (null != product.splitUnitPrice)
		{
			String price = Util.getPrice(product.splitUnitPrice);

			if (null != price)
			{
				holder.ll_price.setVisibility(View.VISIBLE);
				holder.ll_other.setVisibility(View.GONE);
				holder.price.setText("Us" + price);

				if (!Utils.isEmpty(product.prodPriceUnit))
				{
					holder.priceunit.setVisibility(View.VISIBLE);
					holder.priceunit.setText("/" + product.prodPriceUnit);
				}
				else
				{
					holder.priceunit.setVisibility(View.GONE);
				}
			}
			else
			{
				holder.ll_price.setVisibility(View.GONE);
				
				if (product.minOrder != null)
				{
					holder.othername.setText(mContext.getString(R.string.min_order));
					holder.othervalue.setText(product.minOrder);
					holder.ll_other.setVisibility(View.VISIBLE);
				}
				else if (product.tradeTerm != null)
				{
					holder.othername.setText(mContext.getString(R.string.trade_terms));
					holder.othervalue.setText(product.tradeTerm);
					holder.ll_other.setVisibility(View.VISIBLE);
				}
			}
		}
		else
		{
			holder.ll_price.setVisibility(View.GONE);

			if (product.minOrder != null)
			{
				holder.othername.setText(mContext.getString(R.string.min_order));
				holder.othervalue.setText(product.minOrder);
				holder.ll_other.setVisibility(View.VISIBLE);
			}
			else if (product.tradeTerm != null)
			{
				holder.othername.setText(mContext.getString(R.string.trade_terms));
				holder.othervalue.setText(product.tradeTerm);
				holder.ll_other.setVisibility(View.VISIBLE);
			}
		}

		/*
		 * if (null != product.minOrder && !"".equals(product.minOrder.trim())) {
		 * holder.priceunit.setVisibility(View.VISIBLE);
		 * holder.priceunit.setText(getHtmlText(mContext.getString(R.string.min_order), product.minOrder)); } else {
		 * holder.priceunit.setVisibility(View.GONE); }
		 */

		changeItemColor(convertView, holder.name, product);
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
		LinearLayout ll_price;
		TextView price;
		TextView priceunit;
		LinearLayout ll_other;
		TextView othername;
		TextView othervalue;
		ImageView iv1;
		ImageView iv2;
	}

}
