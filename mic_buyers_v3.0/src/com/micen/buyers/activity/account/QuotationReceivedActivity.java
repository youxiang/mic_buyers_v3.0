package com.micen.buyers.activity.account;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.micen.buyers.activity.BaseFragmentActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.activity.rfq.RFQAddActivity_;
import com.micen.buyers.view.rfq.QuotationFragment;
import com.micen.buyers.view.rfq.QuotationFragment_;

/**********************************************************
 * @文件名称：QuotationReceivedActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月20日 上午9:15:32
 * @文件描述：报价RFQ列表页
 * @修改历史：2015年3月20日创建初始版本
 **********************************************************/
@EActivity
public class QuotationReceivedActivity extends BaseFragmentActivity
{
	protected QuotationFragment quotationFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quotation_received);
		if (findViewById(R.id.quotation_fragment_container) != null)
		{
			if (savedInstanceState != null)
			{
				return;
			}
			quotationFragment = new QuotationFragment_();
			getSupportFragmentManager().beginTransaction().add(R.id.quotation_fragment_container, quotationFragment)
					.commit();
			quotationFragment.onLoadData();
		}
	}

	@AfterViews
	protected void initView()
	{
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleText.setText(R.string.vo_quotation);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setImageResource(R.drawable.ic_add);

		titleLeftButton.setOnClickListener(this);
		titleRightButton3.setOnClickListener(this);

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
		case R.id.common_title_right_button3:
			startActivity(new Intent(this, RFQAddActivity_.class));
			break;
		}
	}
}
