package com.micen.buyers.adapter.searchresult;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
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
import com.micen.buyers.module.search.SearchProduct;
import com.micen.buyers.util.ImageUtil;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：ProductBaseAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月18日 下午1:28:30
 * @文件描述：
 * @修改历史：2015年3月18日创建初始版本
 **********************************************************/
public abstract class ProductBaseAdapter extends BaseAdapter
{
	protected Context mContext;
	protected ArrayList<SearchProduct> mProductList;
	protected CommonDialog dialog;
	protected ArrayList<String> mSelectItem;
	protected SearchProduct product;
	
	protected boolean showSupplierType;

	public ProductBaseAdapter(Context contenxt, ArrayList<SearchProduct> productList, boolean showSupplierType)
	{
		this.mContext = contenxt;
		this.mProductList = productList;
		this.showSupplierType = showSupplierType;
		this.mSelectItem = new ArrayList<String>();
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

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);

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

	protected void changeItemColor(View convertView, TextView view, SearchProduct product)
	{
		if (mSelectItem.contains(product.productId))
		{
			convertView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.search_list_item_bg));
			view.setTextColor(mContext.getResources().getColor(R.color.table_line));
		}
		else
		{
			convertView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.search_list_item_bg));
			view.setTextColor(mContext.getResources().getColor(R.color.mic_home_menu_text));
		}
	}

	public void addProducts(ArrayList<SearchProduct> productList)
	{
		this.mProductList.addAll(productList);
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

	public ArrayList<SearchProduct> getAdapterData()
	{
		return this.mProductList;
	}

	public void updateSetting()
	{
		notifyDataSetChanged();
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

}
