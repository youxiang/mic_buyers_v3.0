package com.micen.buyers.view.home;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.micen.buyers.activity.R;
import com.micen.buyers.activity.home.HomeActivity;
import com.micen.buyers.manager.SysManager;
import com.micen.buyers.widget.other.OnItemSelectListener;
import com.micen.buyers.widget.other.PullToNextAdapter;
import com.micen.buyers.widget.other.PullToNextLayout;
import com.micen.buyers.widget.other.PullToNextLayout.LayoutType;

public class HomeFragment extends HomeBaseFragment implements OnClickListener
{

	protected ImageView btnCategory;

	protected TextView btnSearch;

	protected RelativeLayout brandLayout;

	protected ImageView topImage;

	protected PullToNextLayout pullToNextLayout;

	private ArrayList<Fragment> list;

	private PullToNextAdapter adapter;

	private LayoutTransition mTransitioner;

	private OnItemSelectListener onItemSelectListener = new OnItemSelectListener()
	{

		@Override
		public void onSelectItem(int position, View view)
		{
			topImage.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
			switch (position) {
			case 0:
				break;
			case 1:
				// 9 查看历史浏览（首页） 页面事件
				SysManager.analysis(R.string.a_type_page, R.string.c9);				
				// 事件统计$10002 历史记录页 页面事件（疑问）
				SysManager.analysis(R.string.a_type_page, R.string.p10002);
				break;
			}
		}
	};
	private static boolean isShowBrand = false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		init();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View root = inflater.inflate(R.layout.home_fragment_layout, null);
		btnCategory = (ImageView) root.findViewById(R.id.iv_home_category);
		btnSearch = (TextView) root.findViewById(R.id.mic_home_search_text);
		brandLayout = (RelativeLayout) root.findViewById(R.id.home_brand_layout);
		topImage = (ImageView) root.findViewById(R.id.btn_home_top);
		pullToNextLayout = (PullToNextLayout) root.findViewById(R.id.pull_to_next_layout);
		pullToNextLayout.setOnItemSelectListener(onItemSelectListener);
		btnCategory.setOnClickListener(this);
		btnSearch.setOnClickListener(this);
		topImage.setOnClickListener(this);
		if (!isShowBrand)
		{
			handler.sendEmptyMessageDelayed(0, 3000);
		}
		else
		{
			brandLayout.setVisibility(View.GONE);
		}
		return root;
	}

	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			mTransitioner = new LayoutTransition();
			((ViewGroup) brandLayout.getParent()).setLayoutTransition(mTransitioner);
			ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "translationY", 0f, -brandLayout.getHeight())
					.setDuration(mTransitioner.getDuration(LayoutTransition.DISAPPEARING));
			mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);
			animOut.addListener(new AnimatorListenerAdapter()
			{
				public void onAnimationEnd(Animator anim)
				{
					View view = (View) ((ObjectAnimator) anim).getTarget();
					view.setTranslationY(0f);
				}
			});
			mTransitioner.setDuration(500);
			brandLayout.setVisibility(View.GONE);
			isShowBrand = true;
		};
	};

	@Override
	public void onResume()
	{
		super.onResume();
		// 事件统计 $10001 首页 页面事件
		SysManager.analysis(R.string.a_type_page, R.string.p10001);
	}

	protected void init()
	{
		if (list == null)
		{
			list = new ArrayList<Fragment>();
			list.add(new HomeTopFragment_());
			list.add(new HomeBottomFragment_());
			adapter = new PullToNextAdapter(getActivity().getSupportFragmentManager(), list);
			pullToNextLayout.setLayoutType(LayoutType.HOME);
		}
		pullToNextLayout.setAdapter(adapter);

	}

	@Override
	public String getChildTag()
	{
		return HomeFragment.class.getName();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.iv_home_category:
			// 事件统计 2 查看目录（首页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c2);
			((HomeActivity) getActivity()).jumpToSearchFragment(false);
			break;
		case R.id.mic_home_search_text:
			// 事件统计 1 快速搜索（首页） 点击事件
			SysManager.analysis(R.string.a_type_click, R.string.c1);
			((HomeActivity) getActivity()).jumpToSearchFragment(true);
			break;
		case R.id.btn_home_top:
			pullToNextLayout.previous();
			break;
		}
	}

}
