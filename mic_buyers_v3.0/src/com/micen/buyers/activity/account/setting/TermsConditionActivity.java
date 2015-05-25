package com.micen.buyers.activity.account.setting;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.micen.buyers.activity.BaseActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.constant.UrlConstants;

/**********************************************************
 * @文件名称：TermsConditionActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月30日 下午3:29:37
 * @文件描述：版权以及条款页
 * @修改历史：2015年3月30日创建初始版本
 **********************************************************/
@EActivity
public class TermsConditionActivity extends BaseActivity
{
	@ViewById(R.id.wv_terms_condition)
	protected WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terms_condition);
	}

	@AfterViews
	protected void initView()
	{
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleText.setText(R.string.mic_setting_terms);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);

		titleLeftButton.setOnClickListener(this);
		webView.loadUrl(UrlConstants.TERM_CONDITION);
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

}
