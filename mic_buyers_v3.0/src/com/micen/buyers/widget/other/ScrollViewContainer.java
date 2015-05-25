package com.micen.buyers.widget.other;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.micen.buyers.listener.ScrollContainerListener;
import com.micen.buyers.widget.BorderScrollView;
import com.micen.buyers.widget.BorderScrollView.OnBorderListener;

/**********************************************************
 * @文件名称：ScrollViewContainer.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月31日 下午2:44:34
 * @文件描述：包含两个ScrollView的RelativeLayout
 * @修改历史：2015年3月31日创建初始版本
 **********************************************************/
public class ScrollViewContainer extends RelativeLayout
{
	protected enum ScrollState
	{
		AUTO_UP, AUTO_DOWN, DONE;
	}

	private ScrollState state = ScrollState.DONE;

	/**
	 * 切换两个ScrollView时的速率
	 */
	public static float SPEED = 6.5f;

	private int mViewHeight;
	private int mViewWidth;
	private View topView;
	private View bottomView;

	private int topViewHeight, bottomViewHeight;

	/**
	 * 上下，下拉标志位
	 */
	private boolean canPullDown;
	private boolean canPullUp;

	/**
	 * 滑动手势速度测量器
	 */
	private VelocityTracker vt;
	private ScrollTimer mTimer;

	/**
	 * 当前ScrollView标志
	 */
	private int mCurrentViewIndex = 0;
	/**
	 * 滑动偏移量
	 */
	private float mMoveLen;
	private float mLastY;
	/**
	 * 用于控制是否变动布局的另一个条件，mEvents==0时布局可以拖拽了，mEvents==-1时可以舍弃将要到来的第一个move事件，
	 * 这点是去除多点拖动剧变的关键
	 */
	private int mEvents;
	/**
	 * 第一个ScrollView的Y轴滑动距离
	 */
	private int mScrollLastY = 0;

	private ScrollContainerListener listener;

	protected void onFinishInflate()
	{
		super.onFinishInflate();
		requestLayout();
	};

	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			// 解决死循环刷新第二个View的问题
			// if (mMoveLen != 0)
			// {
			switch (state)
			{
			case AUTO_UP:
				mMoveLen -= SPEED;
				if (mMoveLen <= -mViewHeight)
				{
					changeState(-mViewHeight, 1);
					state = ScrollState.DONE;
				}
				if (listener != null)
				{
					listener.onScrollBottom();
				}
				break;
			case AUTO_DOWN:
				mMoveLen += SPEED;
				if (mMoveLen >= 0)
				{
					changeState(0, 0);
					state = ScrollState.DONE;
				}
				if (listener != null)
				{
					listener.onScrollTop();
				}
				break;
			default:
				mTimer.cancel();
				break;
			}
			// }
			requestLayout(); // 刷新布局
		};
	};

	public ScrollViewContainer(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
		mTimer = new ScrollTimer();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		switch (ev.getActionMasked())
		{
		case MotionEvent.ACTION_DOWN:
			if (vt == null)
				vt = VelocityTracker.obtain();

			mLastY = ev.getY();
			vt.addMovement(ev);
			mEvents = 0;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_POINTER_UP:
			// 多一只手指按下或抬起时舍弃将要到来的第一个事件move，防止多点拖拽的bug
			mEvents = -1;
			break;
		case MotionEvent.ACTION_MOVE:
			vt.addMovement(ev);
			switch (mCurrentViewIndex)
			{
			case 0:
				if (canPullUp && mEvents == 0)
				{
					mMoveLen += (ev.getY() - mLastY);
					// 防止上下越界
					if (mMoveLen > 0)
					{
						changeState(0, 0);
					}
					else if (mMoveLen < -mViewHeight)
					{
						changeState(-mViewHeight, 1);
					}
					if (mMoveLen < 0)
					{
						// 防止事件冲突
						ev.setAction(MotionEvent.ACTION_CANCEL);
					}
				}
				break;
			case 1:
				if (canPullDown && mEvents == 0)
				{
					mMoveLen += (ev.getY() - mLastY);
					// 防止上下越界
					if (mMoveLen < -mViewHeight)
					{
						changeState(-mViewHeight, 1);
					}
					else if (mMoveLen > 0)
					{
						changeState(0, 0);
					}
					if (mMoveLen > -mViewHeight)
					{
						// 防止事件冲突
						ev.setAction(MotionEvent.ACTION_CANCEL);
					}
				}
				break;
			default:
				mEvents++;
				break;
			}
			mLastY = ev.getY();
			requestLayout(); // 强制重新调用onMeasure, onLayout方法
			break;
		case MotionEvent.ACTION_UP:
			mLastY = ev.getY();
			vt.addMovement(ev);
			vt.computeCurrentVelocity(700);
			// 获取Y方向的速度
			float mYV = vt.getYVelocity();
			if (mMoveLen == 0 || mMoveLen == -mViewHeight)
				break;
			if (Math.abs(mYV) < 500)
			{
				// 速度小于一定值的时候当作静止释放，这时候两个View往哪移动取决于滑动的距离
				if (mMoveLen <= -mViewHeight / 2)
				{
					state = ScrollState.AUTO_UP;
				}
				else if (mMoveLen > -mViewHeight / 2)
				{
					state = ScrollState.AUTO_DOWN;
				}
			}
			else
			{
				// 抬起手指时速度方向决定两个View往哪移动
				if (mYV < 0)
					state = ScrollState.AUTO_UP;
				else state = ScrollState.AUTO_DOWN;
			}
			mTimer.schedule(1);
			try
			{
				vt.recycle();
				vt = null;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	private void changeState(float moveLength, int currentViewIndex)
	{
		mMoveLen = moveLength;
		mCurrentViewIndex = currentViewIndex;
	}

	public void scrollTop()
	{
		mMoveLen = -mViewHeight / 2;
		state = ScrollState.AUTO_DOWN;
		mTimer.schedule(1);
		if (getChildAt(0) instanceof SwipeRefreshLayout)
		{
			if (((ViewGroup) getChildAt(0)).getChildAt(0) instanceof BorderScrollView)
			{
				handler.postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						((BorderScrollView) ((ViewGroup) getChildAt(0)).getChildAt(0)).fullScroll(ScrollView.FOCUS_UP);
						mMoveLen = 0;
						canPullUp = false;
						// ((SwipeRefreshLayout) getChildAt(0)).scrollTo(0, 0);//fullScroll(ScrollView.FOCUS_UP);
					}
				}, 500);
			}
		}
	}

	public void setOnScrollContainerListener(ScrollContainerListener listener)
	{
		this.listener = listener;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		topView.layout(0, (int) mMoveLen, mViewWidth, topViewHeight + (int) mMoveLen);
		bottomView.layout(0, topViewHeight + (int) mMoveLen, mViewWidth, topViewHeight + (int) mMoveLen
				+ bottomViewHeight);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mViewHeight = getMeasuredHeight();
		mViewWidth = getMeasuredWidth();

		topView = getChildAt(0);
		bottomView = getChildAt(1);
		topViewHeight = topView.getMeasuredHeight();
		bottomViewHeight = bottomView.getMeasuredHeight();
		if (bottomView instanceof FrameLayout) {
			bottomView.setOnTouchListener(bottomViewTouchListener);
			View child = ((FrameLayout) bottomView).getChildAt(0);
			if (child != null) {
				child.setOnTouchListener(bottomViewTouchListener);
			}
		} else {
			bottomView.setOnTouchListener(bottomViewTouchListener);
		}
		setChildScrollViewTouch(getChildAt(0));
	}

	private void setChildScrollViewTouch(View firstChildView)
	{
		if (firstChildView instanceof SwipeRefreshLayout)
		{
			if (((ViewGroup) firstChildView).getChildAt(0) instanceof BorderScrollView)
			{
				((BorderScrollView) ((ViewGroup) firstChildView).getChildAt(0)).setOnBorderListener(borderListener);
			}
			else
			{
				((ViewGroup) firstChildView).getChildAt(0).setOnTouchListener(topViewTouchListener);
			}
		}
		else
		{
			firstChildView.setOnTouchListener(topViewTouchListener);
		}
	}

	private OnBorderListener borderListener = new OnBorderListener()
	{
		@Override
		public void onTop()
		{
			canPullUp = false;
		}

		@Override
		public void onBottom()
		{
			canPullUp = true;
		}

		@Override
		public void onMiddle()
		{
			canPullUp = false;
		}
	};

	private OnTouchListener topViewTouchListener = new OnTouchListener()
	{
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			if (mScrollLastY == v.getScrollY())
			{
				if (v.getScrollY() == 0)
					canPullUp = false;
				else if (mCurrentViewIndex == 0)
					canPullUp = true;
				else canPullUp = false;
			}
			else
			{
				mScrollLastY = v.getScrollY();
				canPullUp = false;
			}
			return false;
		}
	};

	private OnTouchListener bottomViewTouchListener = new OnTouchListener()
	{
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			if (v instanceof AbsListView)
			{
				AbsListView gv = (AbsListView) v;
				if (gv.getChildAt(0) != null && gv.getFirstVisiblePosition() == 0
						&& gv.getChildAt(0).getTop() == gv.getPaddingTop() && mCurrentViewIndex == 1)
					canPullDown = true;
				else canPullDown = false;
			}
			else
			{
				if (v.getScrollY() == 0 && mCurrentViewIndex == 1)
					canPullDown = true;
				else canPullDown = false;
			}
			return false;
		}
	};

	class ScrollTimer
	{
		private final Timer timer = new Timer();
		private TimerTask mTask;

		public void schedule(long period)
		{
			cancel();
			mTask = new TimerTask()
			{
				@Override
				public void run()
				{
					handler.obtainMessage().sendToTarget();
				}
			};
			timer.schedule(mTask, 0, period);
		}

		public void cancel()
		{
			if (mTask != null)
			{
				mTask.cancel();
				mTask = null;
			}
		}
	}
}