package com.micen.buyers.activity.rfq.category;

import java.io.IOException;
import java.io.InputStream;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.focustech.common.http.ResponseEntityToModule;
import com.micen.buyers.activity.BaseActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.category.CategorySecondActivity_;
import com.micen.buyers.adapter.search.AllCategoryAdapter;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.module.category.Categories;
import com.micen.buyers.module.category.Category;

/**********************************************************
 * @文件名称：RFQCategoryActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年5月7日 下午3:14:52
 * @文件描述：RFQ目录选择页
 * @修改历史：2015年5月7日创建初始版本
 **********************************************************/
@EActivity
public class RFQCategoryActivity extends BaseActivity implements OnItemClickListener
{
	@ViewById(R.id.search_title_layout)
	protected RelativeLayout searchLayout;
	@ViewById(R.id.category_list_view)
	protected ListView listView;

	protected AllCategoryAdapter allCategoryAdapter;

	/**
	 * 读取本地Json文件
	 */
	private InputStream is;
	protected Categories categories;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rfq_category);
	}

	@AfterViews
	protected void initView()
	{
		Constants.selectedCategories = null;
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleText.setText(R.string.choose_category);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);

		titleLeftButton.setOnClickListener(this);
		searchLayout.setOnClickListener(this);

		initAllCategory();
		listView.setOnItemClickListener(this);
	}

	private void initAllCategory()
	{
		try
		{
			is = this.getResources().openRawResource(R.raw.category);
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			String cateJson = new String(buffer);
			categories = (Categories) ResponseEntityToModule.parseJsonToModule(cateJson, Categories.class);
			allCategoryAdapter = new AllCategoryAdapter(this, categories.content);
			listView.setAdapter(allCategoryAdapter);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (is != null)
			{
				try
				{
					is.close();
					is = null;
				}
				catch (IOException e)
				{
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 0)
		{
			if (resultCode == RESULT_OK)
			{
				finish();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Category tempCat = (Category) parent.getAdapter().getItem(position);
		Intent intent = new Intent(this, CategorySecondActivity_.class);
		intent.putExtra("Category", tempCat);
		intent.putExtra("isSearchCategory", true);
		startActivityForResult(intent, 0);
	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		switch (v.getId())
		{
		case R.id.common_title_back_button:
			finish();
			break;
		case R.id.search_title_layout:
			startActivityForResult(new Intent(this, RFQCategorySearchActivity_.class), 0);
			break;
		}
	}
}
