package com.micen.buyers.view.product;

import java.io.IOException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.micen.buyers.activity.R;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.module.product.ProductDetailContent;
import com.micen.buyers.util.CreateFiles;

public class ProductDescriptionFragment extends Fragment
{
	private ProductDetailContent detail;

	private WebView descWebView;

	public ProductDescriptionFragment()
	{
	}

	public ProductDescriptionFragment(ProductDetailContent detail)
	{
		this.detail = detail;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = LayoutInflater.from(this.getActivity()).inflate(R.layout.product_description_layout, null);

		CreateFiles cf = new CreateFiles(getActivity());
		try
		{
			cf.createCacheFile();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		cf.print(detail.descriptionInfo);

		descWebView = (WebView) v.findViewById(R.id.wb_description);
		// descWebView.loadUrl("file:///android_asset/ar_as/11.html");
		descWebView.getSettings().setSupportZoom(true);
		descWebView.getSettings().setBuiltInZoomControls(true);
		// descWebView.getSettings().setUseWideViewPort(true);
		// descWebView.getSettings().setLoadWithOverviewMode(true);
		descWebView.loadUrl("file:///android_asset/ar_as/no-product-description.html");
		if (!TextUtils.isEmpty(detail.descriptionInfo))
		{
			descWebView.loadUrl("file:///" + cf.getCacheFilePath());
		}
		else
		{
			descWebView.loadUrl("file:///android_asset/ar_as/no-product-description.html");
		}
		return v;
	}

	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		// 事件统计99 查看产品详细描述 点击事件
		SysManager.analysis(R.string.a_type_click, R.string.c99);

		// 事件统计$10024 产品属性页 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10024);
	}

}
