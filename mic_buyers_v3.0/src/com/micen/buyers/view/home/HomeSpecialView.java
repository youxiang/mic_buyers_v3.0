package com.micen.buyers.view.home;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.special.SpecialObject;

/**********************************************************
 * @文件名称：HomeSpecialView.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年5月7日 下午6:51:09
 * @文件描述：首页专题布局
 * @修改历史：2015年5月7日创建初始版本
 **********************************************************/
public class HomeSpecialView extends RelativeLayout
{
	private LinearLayout specialLayout;
	private TextView specialNameText;

	private ArrayList<SpecialObject> dataList;
	private String specialName;

	public HomeSpecialView(Context context, String specialName, ArrayList<SpecialObject> dataList)
	{
		super(context);
		this.dataList = dataList;
		this.specialName = specialName;
		initView();
	}

	private void initView()
	{
		LayoutInflater.from(getContext()).inflate(R.layout.home_special_layout, this);
		specialLayout = (LinearLayout) findViewById(R.id.ll_special_layout);
		specialNameText = (TextView) findViewById(R.id.tv_special_name);
		specialNameText.setText(specialName);

		if (dataList == null || dataList != null && dataList.size() == 0)
		{
			specialLayout.addView(new HomeSpecialItemView(getContext(), new SpecialObject(), true));
		}
		else
		{
			for (int i = 0; i < dataList.size(); i++)
			{
				specialLayout.addView(new HomeSpecialItemView(getContext(), dataList.get(i), i == 0));
			}
		}
	}

}
