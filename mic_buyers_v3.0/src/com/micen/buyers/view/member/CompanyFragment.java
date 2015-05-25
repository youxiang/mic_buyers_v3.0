package com.micen.buyers.view.member;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.view.View;
import android.widget.ImageView;

import com.micen.buyers.activity.R;
import com.micen.buyers.util.ImageUtil;

/**********************************************************
 * @文件名称：CompanyFragment.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月23日 下午2:38:00
 * @文件描述：公司详情Fragment
 * @修改历史：2015年3月23日创建初始版本
 **********************************************************/
@EFragment(R.layout.member_content)
public class CompanyFragment extends MemberBaseFragment
{
	@ViewById(R.id.ivImage)
	protected ImageView companyImage;

	public CompanyFragment()
	{
	}

	@AfterViews
	@Override
	protected void initView()
	{
		companyImage.setVisibility(View.VISIBLE);
		if (user.content.companyInfo.logo != null && !"".equals(user.content.companyInfo.logo))
		{
			ImageUtil.getImageLoader().displayImage(user.content.companyInfo.logo, companyImage,
					ImageUtil.getRecommendImageOptions());
		}
		else
		{
			companyImage.setVisibility(View.GONE);
		}
		addItem(contentLayout, "Profile", user.content.companyInfo.description);
		addItem(contentLayout, "Website", user.content.companyInfo.homepage);
		addItem(contentLayout, "Trademark", user.content.companyInfo.trademark);
		addItem(contentLayout, "Number of Employees", user.content.companyInfo.employeeNumber);
		addItem(contentLayout, "Annual Turnover", user.content.companyInfo.annualTurnover);
		addItem(contentLayout, "Country", user.content.companyInfo.country);
		addItem(contentLayout, "City", user.content.companyInfo.city);
		addItem(contentLayout, "Address", user.content.companyInfo.companyAddress);
	}

}
