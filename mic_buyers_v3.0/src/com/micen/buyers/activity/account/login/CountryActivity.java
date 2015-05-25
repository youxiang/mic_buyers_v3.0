package com.micen.buyers.activity.account.login;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.focustech.common.widget.IndexableSideBar;
import com.focustech.common.widget.IndexableSideBar.OnTouchingLetterChangedListener;
import com.micen.buyers.activity.BaseActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.adapter.CountryListAdapter;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.db.CountryDBHelper;
import com.micen.buyers.module.db.Country;
import com.micen.buyers.util.BitmapUtil;

/**********************************************************
 * @文件名称：CountryActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年7月22日 下午1:58:02
 * @文件描述：注册选择国家页
 * @修改历史：2014年7月22日创建初始版本
 **********************************************************/
@EActivity
public class CountryActivity extends BaseActivity implements OnItemClickListener
{
	@ViewById(R.id.country_list)
	protected ListView countryList;
	@ViewById(R.id.country_select_dialog)
	protected TextView countrySelectDialog;
	@ViewById(R.id.country_list_sidebar)
	protected IndexableSideBar countrySideBar;

	private CountryListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_country_list);
	}

	@AfterViews
	protected void initView()
	{
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleLeftButton.setOnClickListener(this);
		titleText.setText(R.string.mic_country_region);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);

		initData();
		countryList.setOnItemClickListener(this);
		countrySideBar.setTextView(countrySelectDialog);
		// 设置右侧触摸监听
		countrySideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener()
		{
			@Override
			public void onTouchingLetterChanged(String s)
			{
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1)
				{
					countryList.setSelection(position);
				}
			}
		});

	}

	private void initData()
	{
		ArrayList<Country> dataList = new ArrayList<Country>();
		LinkedList<Country> dbDataList = CountryDBHelper.getCountryList(this);
		Collections.sort(dbDataList);
		// 初始化常用国家列表
		Country country = new Country();
		country.setIndexChar(getString(R.string.popular_countries));
		country.setGroup(true);
		dataList.add(country);
		for (int i = 0; i < Constants.POPULAR_COUNTRY.length; i++)
		{
			country = new Country();
			country.setCountryName(Constants.POPULAR_COUNTRY[i]);
			country.setCountryCode(Constants.POPULAR_COUNTRY_CODE[i]);
			country.setCountryCodeValue(Constants.POPULAR_COUNTRY_VALUE[i]);
			country.setCountryTelNum(Constants.POPULAR_TEL_COUNTRY_CODE[i]);
			country.setFlagDrawable((BitmapDrawable) getResources().getDrawable(Constants.POPULAR_COUNTRY_FLAG[i]));
			dataList.add(country);
		}
		String lastChar = "";
		Country groupCountry;
		for (int i = 0; i < dbDataList.size(); i++)
		{
			country = dbDataList.get(i);
			// 添加组名称
			if (!lastChar.equals(country.getIndexChar()))
			{
				groupCountry = new Country();
				groupCountry.setIndexChar(country.getIndexChar());
				groupCountry.setGroup(true);
				dataList.add(groupCountry);
			}
			dataList.add(country);
			lastChar = country.getIndexChar();
		}

		adapter = new CountryListAdapter(this, dataList);
		countryList.setAdapter(adapter);
	}

	@Override
	public void onClick(View view)
	{
		super.onClick(view);
		switch (view.getId())
		{
		case R.id.common_title_back_button:
			finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		Country country = (Country) arg0.getAdapter().getItem(arg2);
		if (!country.isGroup())
		{
			Intent intent = new Intent();
			intent.setClass(this, RegisterActivity_.class);
			Bundle bundle = new Bundle();
			bundle.putString("country", country.getCountryName());
			bundle.putString("countrycode", country.getCountryCode());
			bundle.putString("countrycodevalue", country.getCountryCodeValue());
			bundle.putString("countryTelNum", country.getCountryTelNum());
			bundle.putString("countryAreaNum", country.getCountryAreaNum());
			if (country.getFlagDrawable() != null)
			{
				bundle.putByteArray("countryFlag", BitmapUtil.bitmap2Bytes(country.getFlagDrawable().getBitmap()));
			}
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			finish();
		}
	}
}