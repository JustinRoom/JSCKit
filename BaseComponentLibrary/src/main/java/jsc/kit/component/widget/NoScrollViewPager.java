package jsc.kit.component.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPager extends ViewPager {

	private boolean mNoScroll = true;

	public NoScrollViewPager(@NonNull Context context) {
		super(context);
	}

	public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public void setNoScroll(boolean noScroll) {
		this.mNoScroll = noScroll;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mNoScroll)
			return false;
		else
			return super.onTouchEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (mNoScroll)
			return false;
		else
			return super.onInterceptTouchEvent(event);
	}
}
