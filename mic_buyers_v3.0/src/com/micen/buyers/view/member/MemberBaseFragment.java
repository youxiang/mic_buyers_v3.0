package com.micen.buyers.view.member;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.module.user.User;
import com.micen.buyers.util.BuyerApplication;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：MemberBaseFragment.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年9月16日 下午1:43:45
 * @文件描述：个人中心父页
 * @修改历史：2014年9月16日创建初始版本
 **********************************************************/
@EFragment
public abstract class MemberBaseFragment extends Fragment
{
	@ViewById(R.id.member_content_layout)
	protected LinearLayout contentLayout;
	protected User user;

	public MemberBaseFragment()
	{
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		user = BuyerApplication.getInstance().getUser();
	}

	protected abstract void initView();

	protected void addItem(LinearLayout layout, String key, String value)
	{
		if (value == null || value.trim().length() == 0)
			return;
		LinearLayout itemLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.member_item, null);
		TextView tvName = (TextView) itemLayout.findViewById(R.id.member_item_name);
		TextView tvValue = (TextView) itemLayout.findViewById(R.id.member_item_value);
		TextView tvAttribute = (TextView) itemLayout.findViewById(R.id.member_item_attribute);
		tvName.setText(key);
		tvValue.setText(Html.fromHtml(value));
		// 如果邮件没有认证，则显示红色标示
		if ("Email".equals(key) && "false".equals(user.content.userInfo.isEmailConfirmed))
		{
			tvAttribute.setVisibility(View.VISIBLE);
			Drawable drawable = getActivity().getResources().getDrawable(R.drawable.ic_member_email_not_comfirmed);
			drawable.setBounds(0, 0, Util.dip2px(12), Util.dip2px(12));
			tvAttribute.setCompoundDrawablePadding(Util.dip2px(5));
			tvAttribute.setCompoundDrawables(drawable, null, null, null);
		}
		layout.addView(itemLayout);
	}

}
