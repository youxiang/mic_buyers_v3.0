package com.micen.buyers.activity.account.login;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.focustech.common.util.Utils;
import com.focustech.common.widget.associatemail.MailBoxAssociateView;
import com.micen.buyers.activity.BaseActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.adapter.rfq.PostRFQStringAdapter;
import com.micen.buyers.constant.Constants;
import com.micen.buyers.manager.PopupManager;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：UserBaseActivity.java
 * @文件作者：renzhiqiang
 * @创建时间：2014年9月18日 下午4:12:51
 * @文件描述：FeedBack、Login、Register的公共类
 * @修改历史：2014年9月18日创建初始版本
 **********************************************************/
@EActivity
public abstract class UserBaseActivity extends BaseActivity
{
	@ViewById(R.id.associate_email_input)
	protected MailBoxAssociateView emailInput;
	@ViewById(R.id.tv_gender)
	protected TextView textGender;
	@ViewById(R.id.send_button)
	protected TextView btnSend;

	protected int popupViewWidth = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Constants.currentActivity = this;
		popupViewWidth = Utils.getWindowWidthPix(this) / 4 * 3;
	}

	protected RelativeLayout createPopupView(View contentView, OnClickListener bgClick)
	{
		RelativeLayout popupLayout = new RelativeLayout(this);
		popupLayout.addView(createPopupBgView(bgClick));
		popupLayout.addView(contentView);
		return popupLayout;
	}

	/**
	 * 创建数量弹出框列表布局
	 * @param adapter
	 * @return
	 */
	protected LinearLayout createStringListPopupView(PostRFQStringAdapter adapter, OnItemClickListener itemClick)
	{
		LinearLayout contentLayout = new LinearLayout(this);
		contentLayout.setBackgroundResource(R.color.mic_white);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		contentLayout.setLayoutParams(params);
		contentLayout.setOrientation(LinearLayout.VERTICAL);

		ListView listView = new ListView(this);
		LayoutParams listViewParams = new LayoutParams(popupViewWidth, LayoutParams.WRAP_CONTENT);
		listView.setLayoutParams(listViewParams);
		listView.setCacheColorHint(0);
		listView.setSelector(getResources().getDrawable(R.color.transparent));
		listView.setDivider(getResources().getDrawable(R.color.mic_menu_line));
		listView.setDividerHeight(Util.dip2px(0.5f));
		listView.setOnItemClickListener(itemClick);
		listView.setAdapter(adapter);
		contentLayout.addView(listView);
		return contentLayout;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected ImageView createPopupBgView(OnClickListener click)
	{
		ImageView bgImage = new ImageView(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		bgImage.setLayoutParams(params);
		bgImage.setImageResource(R.color.black);
		if (Build.VERSION.SDK_INT > 10)
		{
			bgImage.setAlpha(0.6f);
		}
		else
		{
			bgImage.setAlpha(153);
		}
		bgImage.setOnClickListener(click);
		return bgImage;
	}

	/*
	 * 根据数组资源ID创建列表适配器
	 * @param arrayResId
	 * @return
	 */
	protected PostRFQStringAdapter createStringListPopupAdapter(int arrayResId)
	{
		PostRFQStringAdapter adapter = new PostRFQStringAdapter(this, getResources().getStringArray(arrayResId));
		return adapter;
	}

	protected OnClickListener popupBgClick = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			PopupManager.getInstance().dismissPopup();
		}
	};

	protected OnItemClickListener usdItemClick = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			PopupManager.getInstance().dismissPopup();
			textGender.setText(arg0.getAdapter().getItem(arg2).toString());
		}
	};

	protected void showGenderDialog()
	{
		SysManager.getInstance().dismissInputKey(this);
		PopupManager.getInstance().showRFQPopup(
				createPopupView(createStringListPopupView(createStringListPopupAdapter(R.array.gender), usdItemClick),
						popupBgClick));
	}

	/**
	 * 初始化用户性别信息
	 * @return
	 */
	protected String initGender(String gender)
	{
		String genderValue = "";
		if ("Mr.".equals(gender))
			genderValue = "0";
		else if ("Mrs.".equals(gender))
			genderValue = "1";
		else if ("Ms.".equals(gender))
			genderValue = "2";
		else if ("Miss".equals(gender))
			genderValue = "3";
		else if ("0".equals(gender))
			genderValue = "Mr.";
		else if ("1".equals(gender))
			genderValue = "Mrs.";
		else if ("2".equals(gender))
			genderValue = "Ms.";
		else if ("3".equals(gender))
			genderValue = "Miss";
		return genderValue;
	}
}
