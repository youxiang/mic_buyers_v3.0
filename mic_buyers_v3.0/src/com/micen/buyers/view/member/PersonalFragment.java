package com.micen.buyers.view.member;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import com.micen.buyers.activity.R;

/**********************************************************
 * @文件名称：PersonalFragment.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月23日 下午2:25:03
 * @文件描述：个人信息Fragment
 * @修改历史：2015年3月23日创建初始版本
 **********************************************************/
@EFragment(R.layout.member_content)
public class PersonalFragment extends MemberBaseFragment
{
	public PersonalFragment()
	{
	}

	@AfterViews
	@Override
	protected void initView()
	{
		addItem(contentLayout, "Email", user.content.userInfo.email);
		addItem(contentLayout, "Backup Email", user.content.userInfo.backEmail);
		addItem(contentLayout, "Department", user.content.userInfo.department);
		addItem(contentLayout, "Position", user.content.userInfo.position);
		addItem(contentLayout, "Telephone", user.content.companyInfo.telephone);
		addItem(contentLayout, "Mobile", user.content.userInfo.mobile);
		addItem(contentLayout, "Fax", user.content.companyInfo.fax);
	}

}
