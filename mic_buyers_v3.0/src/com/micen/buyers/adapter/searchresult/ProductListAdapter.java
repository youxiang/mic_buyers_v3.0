package com.micen.buyers.adapter.searchresult;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.focustech.common.util.Utils;
import com.micen.buyers.activity.R;
import com.micen.buyers.module.search.SearchProduct;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：ProductListAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月18日 下午3:16:05
 * @文件描述：
 * @修改历史：2015年3月18日创建初始版本
 **********************************************************/
public class ProductListAdapter extends ProductBaseAdapter
{
	private ViewHolder holder;

	public ProductListAdapter(Context context, ArrayList<SearchProduct> productList)
	{
		super(context, productList, true);
	}
	
	public ProductListAdapter(Context context, ArrayList<SearchProduct> productList, boolean showSupplierType)
	{
		super(context, productList, showSupplierType);
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2)
	{
		if (null == view)
		{
			view = LayoutInflater.from(mContext).inflate(R.layout.productlist_item, null);
			holder = new ViewHolder();
			holder.productImg = (ImageView) view.findViewById(R.id.product_item_image);
			holder.mProductGold = (LinearLayout) view.findViewById(R.id.ll_product_item_gold);
			holder.ll_product_item_as = (LinearLayout) view.findViewById(R.id.ll_product_item_as);
			holder.mProductAs = (ImageView) view.findViewById(R.id.product_item_as);
			holder.productName = (TextView) view.findViewById(R.id.product_item_name);
			holder.mProductName = (TextView) view.findViewById(R.id.product_tv_as);
			holder.rl_product_item_price = (RelativeLayout) view.findViewById(R.id.rl_product_item_price);
			holder.rl_product_item_order = (RelativeLayout) view.findViewById(R.id.rl_product_item_order);
			holder.rl_product_item_term = (RelativeLayout) view.findViewById(R.id.rl_product_item_term);
			holder.productPrice = (TextView) view.findViewById(R.id.product_item_price);
			holder.productUnit = (TextView) view.findViewById(R.id.product_item_unit);
			holder.productMinOrder = (TextView) view.findViewById(R.id.product_item_minorder);
			holder.productTrade = (TextView) view.findViewById(R.id.product_item_trade);
			view.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) view.getTag();
		}

		product = (SearchProduct) mProductList.get(position);

		/**
		 * 显示对话框
		 */
		showFitImage(product.catCode, product.image.trim(), holder.productImg);

		if (showSupplierType && !TextUtils.isEmpty(product.memberType) && product.memberType.equals("1"))
		{
			holder.mProductGold.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.mProductGold.setVisibility(View.GONE);
		}
		if (!showSupplierType || TextUtils.isEmpty(product.auditType))
		{
			holder.ll_product_item_as.setVisibility(View.GONE);
		}
		else if (product.auditType.equals("1"))
		{
			holder.ll_product_item_as.setVisibility(View.VISIBLE);
			// ic_supplier_as
			holder.mProductAs.setImageResource(R.drawable.ic_supplier_as);
			holder.mProductName.setText(mContext.getString(R.string.audited_supplier));
		}
		else if (product.auditType.equals("0"))
		{
			holder.ll_product_item_as.setVisibility(View.GONE);
		}
		else if (product.auditType.equals("2"))
		{
			holder.ll_product_item_as.setVisibility(View.VISIBLE);
			holder.mProductAs.setImageResource(R.drawable.ic_supplier_oc);
			holder.mProductName.setText(mContext.getString(R.string.onsite_check));
		}
		else if (product.auditType.equals("3"))
		{
			holder.ll_product_item_as.setVisibility(View.VISIBLE);
			holder.mProductAs.setImageResource(R.drawable.ic_supplier_lv);
			// holder.mProductName
			holder.mProductName.setText(mContext.getString(R.string.license_verified));
		}
		holder.productName.setText(product.name);
		/*
		 * if (null != product.unitPrice) { if (!"".equals(product.unitPrice.trim())) {
		 * holder.rl_product_item_price.setVisibility(View.VISIBLE); holder.productPrice.setText(product.unitPrice); }
		 * else { holder.rl_product_item_price.setVisibility(View.GONE); } } else {
		 * holder.rl_product_item_price.setVisibility(View.GONE); }
		 */

		if (null != product.splitUnitPrice)
		{
			String price = Util.getPrice(product.splitUnitPrice);

			if (null != price)
			{
				holder.rl_product_item_price.setVisibility(View.VISIBLE);
				holder.productPrice.setText(price);
				if (!Utils.isEmpty(product.prodPriceUnit))
				{
					holder.productUnit.setText("/" + product.prodPriceUnit);
				}
			}
			else
			{
				holder.rl_product_item_price.setVisibility(View.GONE);
			}
		}
		else
		{
			holder.rl_product_item_price.setVisibility(View.GONE);
		}
		if (null != product.minOrder)
		{
			if (!"".equals(product.minOrder.trim()))
			{
				holder.rl_product_item_order.setVisibility(View.VISIBLE);
				holder.productMinOrder.setText(product.minOrder);
			}
			else
			{
				holder.rl_product_item_order.setVisibility(View.GONE);
			}
		}
		else
		{
			holder.rl_product_item_order.setVisibility(View.GONE);
		}
		if (null != product.tradeTerm)
		{
			if (!"".equals(product.tradeTerm.trim()))
			{
				holder.rl_product_item_term.setVisibility(View.VISIBLE);
				holder.productTrade.setText(product.tradeTerm);
			}
			else
			{
				holder.rl_product_item_term.setVisibility(View.GONE);
			}
		}
		else
		{
			holder.rl_product_item_term.setVisibility(View.GONE);
		}

		changeItemColor(view, holder.productName, product);
		return view;
	}

	private static class ViewHolder
	{
		ImageView productImg;
		LinearLayout mProductGold;
		LinearLayout ll_product_item_as;
		ImageView mProductAs;
		TextView mProductName;
		TextView productName;
		RelativeLayout rl_product_item_price;
		RelativeLayout rl_product_item_order;
		RelativeLayout rl_product_item_term;
		TextView productPrice;
		TextView productUnit;
		TextView productMinOrder;
		TextView productTrade;
	}

}