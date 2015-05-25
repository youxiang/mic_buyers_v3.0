package com.micen.buyers.widget.other;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.focustech.common.util.LogUtil;
import com.micen.buyers.activity.R;
import com.micen.buyers.widget.other.ViewDragHelper.Callback;

public class DragLayout extends FrameLayout
{

	public enum State
	{
		NORMAL, MAX, MIN
	}

	public State state = State.NORMAL;

	/**
	 * The range for change state
	 */
	private int range;

	private View topView;

	private View layout1;

	private View layout2;

	private ScrollView mScrollView;

	private ViewDragHelper dragHelper;

	private int topViewHeight;

	private AbsListView absListView;

	private int mode = 0;

	private Callback dragHelperCallback = new ViewDragHelper.Callback()
	{
		@Override
		public boolean tryCaptureView(View child, int pointerId)
		{
			return child.getId() == R.id.layout2;
		}

		public void onViewDragStateChanged(int state)
		{
		};

		@Override
		public int clampViewPositionVertical(View child, int top, int dy)
		{
			if (top < getPaddingTop())
				return getPaddingTop();
			else if (top > getHeight() - getPaddingBottom())
				return getHeight() - getPaddingBottom();
			return top;
		}

		@Override
		public void onEdgeDragStarted(int edgeFlags, int pointerId)
		{
			super.onEdgeDragStarted(edgeFlags, pointerId);
		}

		@Override
		public void onEdgeTouched(int edgeFlags, int pointerId)
		{
			super.onEdgeTouched(edgeFlags, pointerId);
		}

		@Override
		public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy)
		{
			super.onViewPositionChanged(changedView, left, top, dx, dy);
		}

		public int getViewVerticalDragRange(View child)
		{
			if (child == layout2 && (state == State.NORMAL || canScrollToNormal))
			{
				return 1;
			}
			return 0;
		};

		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel)
		{
			super.onViewReleased(releasedChild, xvel, yvel);
			int top = releasedChild.getTop();
			int dy = 0;
			switch (state)
			{
			case NORMAL:
				dy = top - topViewHeight;
				break;
			case MAX:
				dy = top;
				break;
			case MIN:
				dy = top - height;
				break;
			}
			if (dy > range)
			{
				transferState(true);
			}
			else if (dy < -range)
			{
				transferState(false);
			}
			else
			{
				stayOriginalState();
			}

		}

	};

	public interface OnStateChangeListener
	{
		public void onMax();

		public void onMin();

		public void onNormal();
	}

	private OnStateChangeListener listener;

	public void setOnStateChangedListener(OnStateChangeListener listener)
	{
		this.listener = listener;
	}

	@Override
	public void computeScroll()
	{
		if (dragHelper.continueSettling(true))
		{
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	protected void stayOriginalState()
	{
		int top = 0;
		switch (state)
		{
		case NORMAL:
			top = topViewHeight;
			break;
		case MAX:
			top = 0;
			break;
		case MIN:
			top = height;
			break;
		}

		if (dragHelper.smoothSlideViewTo(layout2, 0, top))
		{
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	/**
	 * 
	 * @param direction
	 *            true: positive direction, false: negative direction
	 */
	protected void transferState(boolean direction)
	{
		if (direction)
		{
			if (state == State.MAX)
			{
				normal();
			}
			else if (state == State.NORMAL)
			{
				min();
			}
		}
		else
		{
			if (state == State.MIN)
			{
				normal();
			}
			else if (state == State.NORMAL)
			{
				max();
			}
		}
	}

	private void max()
	{
		if (state == State.NORMAL)
		{
			state = State.MAX;
			layout2.layout(layout2.getLeft(), layout2.getTop(), layout2.getRight(), layout2.getTop() + height);

			if (dragHelper.smoothSlideViewTo(layout2, 0, 0))
			{
				ViewCompat.postInvalidateOnAnimation(this);
			}
			if (listener != null)
			{
				listener.onMax();
			}
		}
	}

	public void normal()
	{
		if (state == State.MAX || state == State.MIN)
		{
			state = State.NORMAL;
			if (mScrollView != null)
			{
				mScrollView.fullScroll(FOCUS_UP);
			}
			if (dragHelper.smoothSlideViewTo(layout2, 0, topViewHeight))
			{
				ViewCompat.postInvalidateOnAnimation(this);
			}
			if (listener != null)
			{
				listener.onNormal();
			}
		}
	}

	private void min()
	{
		if (state == State.NORMAL)
		{
			state = State.MIN;
			if (dragHelper.smoothSlideViewTo(layout2, 0, height))
			{
				ViewCompat.postInvalidateOnAnimation(this);
			}
			if (listener != null)
			{
				listener.onMin();
			}
		}
	}

	private int height;

	private OnTouchListener mScrollViewOnTouchListener = new OnTouchListener()
	{

		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			return state == State.NORMAL;
		}
	};

	public DragLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		dragHelper = ViewDragHelper.create(this, 10F, dragHelperCallback);
	}

	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		topView = findViewById(R.id.top_layout);
		layout1 = findViewById(R.id.layout1);
		layout2 = findViewById(R.id.layout2);
		mScrollView = (ScrollView) findViewById(R.id.scroll_view);
		mScrollView.setOnTouchListener(mScrollViewOnTouchListener);
		requestLayout();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);
		int layout2Top = 0;
		switch (state)
		{
		case NORMAL:
			layout2Top = topViewHeight;
			break;
		case MAX:
			layout2Top = 0;
			break;
		case MIN:
			layout2Top = this.height;
			break;
		}
		if (layout2 != null)
		{
			layout2.layout(left, layout2Top, right, layout2Top + this.height);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (topView != null && layout1 != null)
		{
			topViewHeight = mScrollView.getTop() + topView.getMeasuredHeight();
			range = topViewHeight / 2;
			height = layout1.getMeasuredHeight();
		}
	}

	private float downY;

	private float dY;

	private boolean canScrollToNormal;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		int y = (int) ev.getRawY();
		switch (ev.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			canScrollToNormal = false;
			downY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			dY = y - downY;
			if (state == State.MAX)
			{
				if (absListView != null)
				{
					final int childCount = absListView.getChildCount();
					if (childCount == 0)
					{
						return dY > 12;
					}
					else
					{
						View firstChild = absListView.getChildAt(0);
						if (dY > 12)
						{
							if (firstChild != null && firstChild.getTop() == absListView.getPaddingTop())
							{
								canScrollToNormal = true;
								return true;
							}
							else
							{
								return false;
							}
						}
						else if (dY < -12)
						{
							return false;
						}
					}
				}
				else if (dY > 12)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			break;
		}

		if (state == State.NORMAL && ev.getAction() == MotionEvent.ACTION_MOVE)
		{
			return dragHelper.handleSpecialShouldInterceptTouchEvent(ev);
		}
		// Log.d("yyx", "onInterceptTouchEvent " + ev);
		return dragHelper.shouldInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		try
		{
			dragHelper.processTouchEvent(e);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return true;
	}

	public void shake()
	{
		if (state == State.NORMAL && layout2 != null)
		{
			Animation animationDown = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
					0.2f);
			animationDown.setDuration(600);
			animationDown.setInterpolator(new AccelerateInterpolator());
			final Animation animationUp = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_PARENT, 0.2f, Animation.RELATIVE_TO_PARENT,
					0.0f);
			animationUp.setDuration(600);
			animationUp.setInterpolator(new BounceInterpolator());
			animationDown.setAnimationListener(new AnimationListener()
			{

				@Override
				public void onAnimationStart(Animation animation)
				{

				}

				@Override
				public void onAnimationRepeat(Animation animation)
				{

				}

				@Override
				public void onAnimationEnd(Animation animation)
				{
					layout2.setAnimation(animationUp);
					animationUp.startNow();
				}
			});
			layout2.setAnimation(animationDown);
			animationDown.startNow();
		}

	}

	public void changeMode(int mode)
	{
		this.mode = mode;
		refreshAbsListView();
	}

	private void refreshAbsListView()
	{
		final int absListViewId = (mode == 0) ? R.id.drag_layout_gridview : R.id.drag_layout_listview;
		absListView = (AbsListView) findViewById(absListViewId);
		LogUtil.d("dragLayout", "refreshAbsListView " + absListView);
	}

}
