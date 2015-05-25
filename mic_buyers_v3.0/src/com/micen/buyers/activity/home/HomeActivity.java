package com.micen.buyers.activity.home;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.focustech.common.widget.dialog.CommonDialog;
import com.focustech.common.widget.dialog.CommonDialog.DialogClickListener;
import com.micen.buyers.activity.BaseFragmentActivity;
import com.micen.buyers.activity.R;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.util.Util;
import com.micen.buyers.view.home.EasySourcingFragment;
import com.micen.buyers.view.home.EasySourcingFragment_;
import com.micen.buyers.view.home.HomeBaseFragment;
import com.micen.buyers.view.home.HomeFragment;
import com.micen.buyers.view.home.MyAccountFragment;
import com.micen.buyers.view.home.MyAccountFragment_;
import com.micen.buyers.view.home.SearchFragment;
import com.micen.buyers.view.home.SearchFragment_;

/**********************************************************
 * @文件名称：HomeActivity.java
 * @文件作者：xiongjiangwei
 * @创建时间：2015年3月11日 下午5:01:20
 * @文件描述：首页
 * @修改历史：2015年3月11日创建初始版本
 **********************************************************/
@EActivity
public class HomeActivity extends BaseFragmentActivity implements OnClickListener
{
	@ViewById(R.id.ll_common_toolbar_layout)
	protected LinearLayout toolBarLayout;
	@ViewById(R.id.common_toolbar_home)
	protected TextView homeToolBar;
	@ViewById(R.id.common_toolbar_search)
	protected TextView searchToolBar;
	@ViewById(R.id.common_toolbar_esourcing)
	protected TextView eSourcingToolBar;
	@ViewById(R.id.common_toolbar_vo)
	protected TextView myAccountToolBar;

	protected SearchFragment searchFragment;
	protected HomeFragment homeFragment;
	protected EasySourcingFragment eSourcingFragment;
	protected MyAccountFragment myAccountFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		if (findViewById(R.id.home_fragment_container) != null)
		{
			if (savedInstanceState != null)
			{
				return;
			}
			homeFragment = new HomeFragment();
			addFragment(homeFragment);
		}
	}

	@AfterViews
	protected void initView()
	{
		setChildSelected(R.id.common_toolbar_home);
		homeToolBar.setOnClickListener(this);
		searchToolBar.setOnClickListener(this);
		eSourcingToolBar.setOnClickListener(this);
		myAccountToolBar.setOnClickListener(this);
	}

	/**
	 * 设置首页底部工具栏状态
	 * @param selectViewId
	 */
	private void setChildSelected(int selectViewId)
	{
		if (searchFragment != null)
			searchFragment.onBackPressedFragment();
		for (int i = 0; i < toolBarLayout.getChildCount(); i++)
		{
			if (toolBarLayout.getChildAt(i) instanceof TextView)
			{
				if (toolBarLayout.getChildAt(i).getId() == selectViewId)
				{
					switch (selectViewId)
					{
					case R.id.common_toolbar_home:
						setTextViewCompoundDrawable(R.drawable.ic_bottom_bar_home_selected,
								(TextView) toolBarLayout.getChildAt(i));
						break;
					case R.id.common_toolbar_search:
						setTextViewCompoundDrawable(R.drawable.ic_bottom_bar_search_selected,
								(TextView) toolBarLayout.getChildAt(i));
						break;
					case R.id.common_toolbar_esourcing:
						setTextViewCompoundDrawable(R.drawable.ic_bottom_bar_easy_sourcing_selected,
								(TextView) toolBarLayout.getChildAt(i));
						break;
					case R.id.common_toolbar_vo:
						setTextViewCompoundDrawable(R.drawable.ic_bottom_bar_my_account_selected,
								(TextView) toolBarLayout.getChildAt(i));
						break;
					}
					((TextView) toolBarLayout.getChildAt(i))
							.setTextColor(getResources().getColor(R.color.color_e62e2e));
				}
				else
				{
					switch (toolBarLayout.getChildAt(i).getId())
					{
					case R.id.common_toolbar_home:
						setTextViewCompoundDrawable(R.drawable.ic_bottom_bar_home,
								(TextView) toolBarLayout.getChildAt(i));
						break;
					case R.id.common_toolbar_search:
						setTextViewCompoundDrawable(R.drawable.ic_bottom_bar_search,
								(TextView) toolBarLayout.getChildAt(i));
						break;
					case R.id.common_toolbar_esourcing:
						setTextViewCompoundDrawable(R.drawable.ic_bottom_bar_easy_sourcing,
								(TextView) toolBarLayout.getChildAt(i));
						break;
					case R.id.common_toolbar_vo:
						setTextViewCompoundDrawable(R.drawable.ic_bottom_bar_my_account,
								(TextView) toolBarLayout.getChildAt(i));
						break;
					}
					((TextView) toolBarLayout.getChildAt(i))
							.setTextColor(getResources().getColor(R.color.color_333333));
				}
			}
		}
	}

	private void setTextViewCompoundDrawable(int drawableResId, TextView tv)
	{
		Drawable drawable = getResources().getDrawable(drawableResId);
		drawable.setBounds(0, 0, Util.dip2px(30), Util.dip2px(30));
		tv.setCompoundDrawables(null, drawable, null, null);
	}

	private void switchFragment(Fragment targetFragment, Fragment... otherFragments)
	{
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if (!targetFragment.isAdded())
		{
			transaction.add(R.id.home_fragment_container, targetFragment);
		}
		else
		{
			transaction.show(targetFragment);
		}
		for (Fragment fragment : otherFragments)
		{
			if (fragment != null)
			{
				transaction.hide(fragment);
			}
		}
		transaction.commit();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.common_toolbar_home:
			// 事件统计 11 返回首页 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c11);
			if (homeFragment == null)
			{
				homeFragment = new HomeFragment();
				addFragment(homeFragment);
			}
			else
			{
				switchFragment(homeFragment, searchFragment, eSourcingFragment, myAccountFragment);
			}
			setChildSelected(v.getId());
			break;
		case R.id.common_toolbar_search:
			// 事件统计 12 进入搜索（首页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c12);
			if (searchFragment == null)
			{
				searchFragment = new SearchFragment_();
				addFragment(searchFragment);
			}
			else
			{
				searchFragment.clearSearchText();
				switchFragment(searchFragment, homeFragment, eSourcingFragment, myAccountFragment);
			}
			setChildSelected(v.getId());
			break;
		case R.id.common_toolbar_esourcing:
			// 事件统计 13 查看Easy Sourcing（首页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c13);
			if (eSourcingFragment == null)
			{
				eSourcingFragment = new EasySourcingFragment_();
				addFragment(eSourcingFragment);
			}
			else
			{
				switchFragment(eSourcingFragment, homeFragment, searchFragment, myAccountFragment);
			}
			setChildSelected(v.getId());
			break;
		case R.id.common_toolbar_vo:
			// 事件统计14 进入My Account (首页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c14);
			if (myAccountFragment == null)
			{
				myAccountFragment = new MyAccountFragment_();
				addFragment(myAccountFragment);
			}
			else
			{
				switchFragment(myAccountFragment, homeFragment, searchFragment, eSourcingFragment);
			}
			setChildSelected(v.getId());
			break;
		}
	}

	/**
	 * 跳转到搜索页
	 * @param isShowHistory 是否直接显示搜索历史页
	 */
	public void jumpToSearchFragment(boolean isShowHistory)
	{
		if (searchFragment == null)
		{
			searchFragment = new SearchFragment_();
			searchFragment.setShowHistory(isShowHistory);
			addFragment(searchFragment);
		}
		else
		{
			switchFragment(searchFragment, homeFragment, eSourcingFragment, myAccountFragment);
			searchFragment.setShowHistory(isShowHistory);
		}
		setChildSelected(R.id.common_toolbar_search);
		searchFragment.clearSearchText();
	}

	private void addFragment(HomeBaseFragment targetFragment)
	{
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.home_fragment_container, targetFragment);
		// transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (searchFragment != null && searchFragment.onBackPressedFragment() || searchFragment == null)
			{
				CommonDialog dialog = new CommonDialog(this, getString(R.string.Are_you_sure_to_exit),
						getString(R.string.mic_yes), getString(R.string.cancel), Util.dip2px(258),
						new DialogClickListener()
						{
							@Override
							public void onDialogClick()
							{
								SysManager.exitSystem(HomeActivity.this);
							}
						});
				dialog.show();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
