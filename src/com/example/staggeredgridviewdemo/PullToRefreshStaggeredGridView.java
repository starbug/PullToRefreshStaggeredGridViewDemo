package com.example.staggeredgridviewdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.handmark.pulltorefresh.library.OverscrollHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.origamilabs.library.views.StaggeredGridView;

public class PullToRefreshStaggeredGridView extends PullToRefreshBase<StaggeredGridView> {

	public PullToRefreshStaggeredGridView(Context context) {
		super(context);
	}

	public PullToRefreshStaggeredGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshStaggeredGridView(Context context, Mode mode) {
		super(context, mode);
	}

	public PullToRefreshStaggeredGridView(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	@Override
	protected StaggeredGridView createRefreshableView(Context context, AttributeSet attrs) {
		return new InternalStaggeredGridViewSDK9(context, attrs);
	}

	@Override
	protected boolean isReadyForPullStart() {
		return getRefreshableView().isEdgeStart(); // .getScrollY() == 0;
	}

	@Override
	protected boolean isReadyForPullEnd() {
		/*
		View viewChild = getRefreshableView().getChildAt(0);
		if (null != viewChild) {
			return getRefreshableView().getScrollY() >= (viewChild.getHeight() - getHeight());
		}
		return false;
		*/
		return getRefreshableView().isEdgeEnd();
	}

	public final class InternalStaggeredGridViewSDK9 extends StaggeredGridView {

		public InternalStaggeredGridViewSDK9(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
				int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

			final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
					scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

			// Does all of the hard work...
			OverscrollHelper.overScrollBy(PullToRefreshStaggeredGridView.this, deltaX, scrollX, deltaY, scrollY,
					getScrollRange(), isTouchEvent);

			return returnValue;
		}

		/**
		 * Taken from the AOSP ScrollView source
		 */
		private int getScrollRange() {
			int scrollRange = 0;
			if (getChildCount() > 0) {
				View child = getChildAt(0);
				scrollRange = Math.max(0, child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
			}
			return scrollRange;
		}
	}
}
