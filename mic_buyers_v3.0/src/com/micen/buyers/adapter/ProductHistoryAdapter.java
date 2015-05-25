package com.micen.buyers.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.focustech.common.widget.dialog.CommonDialog;
import com.focustech.common.widget.dialog.CommonDialog.DialogClickListener;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.account.setting.SettingActivity_;
import com.micen.buyers.db.SharedPreferenceManager;
import com.micen.buyers.module.db.ProductHistory;
import com.micen.buyers.util.ImageUtil;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：ProductHistoryAdapter.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年4月22日 上午9:46:21
 * @文件描述：浏览产品历史记录列表适配器
 * @修改历史：2015年4月22日创建初始版本
 **********************************************************/
public class ProductHistoryAdapter extends BaseAdapter
{
	protected Context mContext;
	protected ArrayList<ProductHistory> mProductList;
	protected CommonDialog dialog;
	protected ArrayList<String> mSelectItem;
	protected ProductHistory product;
	private ViewHolder holder;

	public ProductHistoryAdapter(Context context, ArrayList<ProductHistory> productList)
	{
		this.mContext = context;
		this.mProductList = productList;
		this.mSelectItem = new ArrayList<String>();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.product_gridlist_item, parent, false);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.product_item_image);
			holder.name = (TextView) convertView.findViewById(R.id.product_item_name);
			holder.tv1 = (TextView) convertView.findViewById(R.id.product_item1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.product_item2);
			holder.tv2.setVisibility(View.GONE);
			holder.iv1 = (ImageView) convertView.findViewById(R.id.product_item_image1);
			holder.iv2 = (ImageView) convertView.findViewById(R.id.product_item_image2);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		product = (ProductHistory) mProductList.get(position);

		showFitImage(product.categoryId, product.productImageUrl.trim(), holder.imageView);

		if (product.goldMember.equals("1"))
		{
			holder.iv1.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.iv1.setVisibility(View.GONE);
		}
		if (product.as_.equals("1"))
		{
			holder.iv2.setVisibility(View.VISIBLE);
			holder.iv2.setBackgroundResource(R.drawable.ic_supplier_as);
		}
		else if (product.as_.equals("0"))
		{
			holder.iv2.setVisibility(View.GONE);
		}
		else if (product.as_.equals("2"))
		{
			holder.iv2.setVisibility(View.VISIBLE);
			holder.iv2.setBackgroundResource(R.drawable.ic_supplier_oc);
		}
		else if (product.as_.equals("3"))
		{
			holder.iv2.setVisibility(View.VISIBLE);
			holder.iv2.setBackgroundResource(R.drawable.ic_supplier_lv);
		}
		holder.name.setText(product.productName);
		if (null != product.unitPrice && !"".equals(product.unitPrice.trim()))
		{
			holder.tv1.setVisibility(View.VISIBLE);
			holder.tv1.setText(product.unitPrice);
		}
		else
		{
			holder.tv1.setVisibility(View.GONE);
		}
		return convertView;
	}

	static class ViewHolder
	{
		ImageView imageView;
		TextView name;
		TextView tv1;
		TextView tv2;
		ImageView iv1;
		ImageView iv2;
	}

	public void addProducts(ArrayList<ProductHistory> productList)
	{
		this.mProductList.addAll(productList);
		notifyDataSetChanged();
	}
	
	public void setData(ArrayList<ProductHistory> productList) {
		this.mProductList = productList;
		notifyDataSetChanged();
	}

	public void addSelectItem(String productId)
	{
		mSelectItem.add(productId);
		notifyDataSetChanged();
	}

	public void addSelectItem(ArrayList<String> productIds)
	{
		if (productIds != null && productIds.size() != 0)
		{
			mSelectItem.addAll(productIds);
			notifyDataSetChanged();
		}
	}

	public ArrayList<ProductHistory> getAdapterData()
	{
		return this.mProductList;
	}

	public void updateSetting()
	{
		notifyDataSetChanged();
	}

	protected void showDialog()
	{
		dialog = new CommonDialog(mContext, mContext.getString(R.string.safe_image_msg),
				mContext.getString(R.string.confirm), mContext.getString(R.string.cancel), Util.dip2px(278),
				new DialogClickListener()
				{

					@Override
					public void onDialogClick()
					{
						Intent intent = new Intent(mContext, SettingActivity_.class);
						mContext.startActivity(intent);

					}
				});
		dialog.show();
	}

	public void showFitImage(String isShowImage, String imagePath, ImageView imageView)
	{
		/**
		* 判断图片是否显示
		*/
		if ("2201000000".equals(isShowImage) || "1713000000".equals(isShowImage))
		{// 是限制级
			// 判断 是否开启浏览图片
			/**
			* 首次出设置对话框
			*/
			if (SharedPreferenceManager.getInstance().getBoolean("isFirstScanUnsavedImage", true))
			{
				showDialog();
				SharedPreferenceManager.getInstance().putBoolean("isFirstScanUnsavedImage", false);
			}
			if (!SharedPreferenceManager.getInstance().getBoolean("isDisplaySafeImage", false))
			{// 默认关闭
				imageView.setImageResource(R.drawable.product_fort_adult);
			}
			else
			{
				ImageUtil.getImageLoader().displayImage(imagePath, imageView, ImageUtil.getSafeImageOptions());
			}
		}
		else
		{
			ImageUtil.getImageLoader().displayImage(imagePath, imageView, ImageUtil.getSafeImageOptions());
		}
	}

	@Override
	public int getCount()
	{
		return mProductList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mProductList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

}
