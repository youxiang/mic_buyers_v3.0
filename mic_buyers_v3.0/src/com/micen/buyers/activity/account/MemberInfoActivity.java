package com.micen.buyers.activity.account;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.focustech.common.util.Utils;
import com.focustech.common.widget.WrapContentHeightViewPager;
import com.focustech.common.widget.viewpagerindictor.UnderlinePageIndicator;
import com.focustech.common.widget.viewpagerindictor.UnderlinePageIndicator.ChangeTitle;
import com.micen.buyers.activity.BaseFragmentActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.adapter.CommonFragmentAdapter;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.util.BuyerApplication;
import com.micen.buyers.view.member.CompanyFragment_;
import com.micen.buyers.view.member.PersonalFragment_;

/**********************************************************
 * @文件名称：MemberInfoActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月20日 上午11:46:07
 * @文件描述：个人中心页
 * @修改历史：2015年3月20日创建初始版本
 **********************************************************/
@EActivity
public class MemberInfoActivity extends BaseFragmentActivity implements ChangeTitle
{
	@ViewById(R.id.rl_member_title)
	protected RelativeLayout memberTitle;
	@ViewById(R.id.tv_member_name)
	protected TextView personalName;
	@ViewById(R.id.tv_member_company)
	protected TextView companyName;
	@ViewById(R.id.iv_member_type)
	protected ImageView companyType;
	@ViewById(R.id.tv_personal)
	protected TextView tvPersonal;
	@ViewById(R.id.tv_company)
	protected TextView tvCompany;
	@ViewById(R.id.vo_title_line)
	protected UnderlinePageIndicator titleLine;
	@ViewById(R.id.vp_member)
	protected WrapContentHeightViewPager viewPager;

	private CommonFragmentAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_info);
	}

	@AfterViews
	protected void initView()
	{
		titleLeftButton.setImageResource(R.drawable.ic_title_back);
		titleText.setText(R.string.mic_memberinfo);
		titleRightButton1.setVisibility(View.GONE);
		titleRightButton2.setVisibility(View.GONE);
		titleRightButton3.setVisibility(View.GONE);

		String gender = BuyerApplication.getInstance().getUser().content.userInfo.gender;
		Integer inte = Integer.parseInt(gender);
		if (inte == 0)
		{
			gender = "Mr.";
		}
		else if (inte == 1)
		{
			gender = "Mrs.";
		}
		else if (inte == 2)
		{
			gender = "Ms.";
		}
		else if (inte == 3)
		{
			gender = "Miss ";
		}
		personalName.setText(gender + BuyerApplication.getInstance().getUser().content.userInfo.fullName);
		companyName.setText(BuyerApplication.getInstance().getUser().content.companyInfo.companyName);
		if (64 == Integer.parseInt(BuyerApplication.getInstance().getUser().content.companyInfo.memberType))
		{
			companyType.setImageResource(R.drawable.ic_member_global);
		}
		else if (128 == Integer.parseInt(BuyerApplication.getInstance().getUser().content.companyInfo.memberType))
		{
			companyType.setImageResource(R.drawable.ic_member_pro);
		}
		else
		{
			companyType.setVisibility(View.GONE);
		}

		Drawable drawable = getResources().getDrawable(R.drawable.bg_member_title);
		float scale = drawable.getMinimumWidth() / (float) Utils.getWindowWidthPix(this);
		memberTitle.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				(int) (drawable.getMinimumHeight() / scale)));

		ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
		fragmentList.add(new PersonalFragment_());
		fragmentList.add(new CompanyFragment_());
		adapter = new CommonFragmentAdapter(this, getSupportFragmentManager(), fragmentList);
		viewPager.setAdapter(adapter);
		titleLine.setViewPager(viewPager);
		titleLine.setChangeTitleListener(this);
		titleLine.setFades(false);
		titleLine.setSelectedColor(getResources().getColor(R.color.color_e62e2e));
		changeTitleStatus(0);

		titleLeftButton.setOnClickListener(this);
		tvPersonal.setOnClickListener(this);
		tvCompany.setOnClickListener(this);
	}

	@Override
	public void changeTitleStatus(int position)
	{
		switch (position)
		{
		case 0:
			tvPersonal.setTextColor(getResources().getColor(R.color.page_indicator_title_selected));
			tvCompany.setTextColor(getResources().getColor(R.color.page_indicator_title));
			break;
		case 1:
			tvCompany.setTextColor(getResources().getColor(R.color.page_indicator_title_selected));
			tvPersonal.setTextColor(getResources().getColor(R.color.page_indicator_title));
			break;
		}
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
		case R.id.tv_personal:
			if (viewPager.getCurrentItem() != 0)
			{
				viewPager.setCurrentItem(0);
			}
			break;
		case R.id.tv_company:
			if (viewPager.getCurrentItem() != 1)
			{
				// 事件统计 75 查看公司信息 点击事件
				SysManager.analysis(R.string.a_type_click, R.string.c75);
				viewPager.setCurrentItem(1);
			}
			break;
		}
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();

		// 事件统计 $10018 会员信息页 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10018);
	}

}
