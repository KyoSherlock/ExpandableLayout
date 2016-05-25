/**
 * Copyright 2015, KyoSherlock
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kyo.expandablelayout;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ExpandableLayout extends LinearLayout {
	private int mWidthMeasureSpec;
	private int mHeightMeasureSpec;
	private boolean mAttachedToWindow;
	private boolean mFirstLayout = true;
	private boolean mInLayout;
	private ObjectAnimator mExpandAnimator;
	private OnExpandListener mListener;

	public ExpandableLayout(Context context) {
		super(context);
		this.init(context);
	}

	public ExpandableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context);
	}

	public ExpandableLayout(Context context, AttributeSet attrs,
							int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.init(context);
	}

	@TargetApi(21)
	public ExpandableLayout(Context context, AttributeSet attrs,
							int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		this.init(context);
	}

	private void init(Context c) {
		this.setOrientation(LinearLayout.VERTICAL);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		mWidthMeasureSpec = widthMeasureSpec;
		mHeightMeasureSpec = heightMeasureSpec;
		View child = findExpandableView();
		if (child != null) {
			LayoutParams p = (LayoutParams) child.getLayoutParams();

			if (p.weight != 0) {
				throw new IllegalArgumentException(
					"ExpandableView can't use weight");
			}

			if (!p.isExpanded && !p.isExpanding) {
				child.setVisibility(View.GONE);
			} else {
				child.setVisibility(View.VISIBLE);
			}
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mInLayout = true;
		super.onLayout(changed, l, t, r, b);
		mInLayout = false;
		mFirstLayout = false;
	}

	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		return super.drawChild(canvas, child, drawingTime);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		mAttachedToWindow = true;
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mAttachedToWindow = false;
		View child = findExpandableView();
		if (mExpandAnimator != null && mExpandAnimator.isRunning()) {
			mExpandAnimator.end();
			mExpandAnimator = null;
		}
		if (child != null) {
			LayoutParams p = (LayoutParams) child.getLayoutParams();
			if (p.isExpanded) {
				p.height = p.originalHeight;
				child.setVisibility(View.VISIBLE);
			} else {
				p.height = p.originalHeight;
				child.setVisibility(View.GONE);
			}
			p.isExpanding = false;
		}
	}

	@Override
	public void requestLayout() {
		if (!mInLayout) {
			super.requestLayout();
		}
	}

	public View findExpandableView() {
		for (int i = 0; i < this.getChildCount(); i++) {
			LayoutParams p = (LayoutParams) this.getChildAt(i)
				.getLayoutParams();
			if (p.canExpand) {
				return this.getChildAt(i);
			}
		}
		return null;
	}

	boolean checkExpandableView(View expandableView) {
		LayoutParams p = (LayoutParams) expandableView.getLayoutParams();
		return p.canExpand;
	}

	public boolean isExpanded() {
		View child = findExpandableView();
		if (child != null) {
			LayoutParams p = (LayoutParams) child.getLayoutParams();
			if (p.isExpanded) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return
	 */
	public boolean toggleExpansion() {
		return this.setExpanded(!isExpanded(), true);
	}

	/**
	 * @param isExpanded
	 * @return
	 */
	public boolean setExpanded(boolean isExpanded) {
		return this.setExpanded(isExpanded, false);
	}

	/**
	 * @param isExpanded
	 * @param shouldAnimate
	 * @return
	 */
	public boolean setExpanded(boolean isExpanded, boolean shouldAnimate) {
		boolean result = false;
		View child = findExpandableView();
		if (child != null) {
			if (isExpanded != this.isExpanded()) {
				if (isExpanded) {
					result = this.expand(child, shouldAnimate);
				} else {
					result = this.collapse(child, shouldAnimate);
				}
			}
		}
		this.requestLayout();
		return result;
	}

	public void setOnExpandListener(OnExpandListener listenr) {
		this.mListener = listenr;
	}

	/**
	 * @param child
	 * @param shouldAnimate
	 * @return
	 */
	private boolean expand(View child, boolean shouldAnimate) {
		boolean result = false;
		if (!checkExpandableView(child)) {
			throw new IllegalArgumentException(
				"expand(), View is not expandableView");
		}
		LayoutParams p = (LayoutParams) child.getLayoutParams();
		if (mFirstLayout || mAttachedToWindow == false || !shouldAnimate) {
			p.isExpanded = true;
			p.isExpanding = false;
			p.height = p.originalHeight;
			child.setVisibility(View.VISIBLE);
			result = true;
		} else {
			if (!p.isExpanded && !p.isExpanding) {
				this.playExpandAnimation(child);
				result = true;
			}
		}
		return result;
	}

	private void playExpandAnimation(final View child) {
		final LayoutParams p = (LayoutParams) child.getLayoutParams();
		if (p.isExpanding) {
			return;
		}
		child.setVisibility(View.VISIBLE);
		p.isExpanding = true;
		this.measure(mWidthMeasureSpec, mHeightMeasureSpec);
		final int measuredHeight = child.getMeasuredHeight();
		p.height = 0;

		mExpandAnimator = ObjectAnimator.ofInt(p, "height", 0, measuredHeight);
		mExpandAnimator.setDuration(getContext().getResources().getInteger(
			android.R.integer.config_shortAnimTime));
		mExpandAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				dispatchOffset(child);
				child.requestLayout();
			}
		});
		mExpandAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				performToggleState(child);
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});
		mExpandAnimator.start();
	}

	/**
	 * @param child
	 * @param shouldAnimation
	 * @return
	 */
	private boolean collapse(View child, boolean shouldAnimation) {
		boolean result = false;
		if (!checkExpandableView(child)) {
			throw new IllegalArgumentException(
				"collapse(), View is not expandableView");
		}
		LayoutParams p = (LayoutParams) child.getLayoutParams();
		if (mFirstLayout || mAttachedToWindow == false || !shouldAnimation) {
			p.isExpanded = false;
			p.isExpanding = false;
			p.height = p.originalHeight;
			child.setVisibility(View.GONE);
			result = true;
		} else {
			if (p.isExpanded && !p.isExpanding) {
				this.playCollapseAnimation(child);
				result = true;
			}
		}
		return result;
	}

	private void playCollapseAnimation(final View child) {
		final LayoutParams p = (LayoutParams) child.getLayoutParams();
		if (p.isExpanding) {
			return;
		}
		child.setVisibility(View.VISIBLE);
		p.isExpanding = true;
		this.measure(mWidthMeasureSpec, mHeightMeasureSpec);
		final int measuredHeight = child.getMeasuredHeight();

		mExpandAnimator = ObjectAnimator.ofInt(p, "height", measuredHeight, 0);
		mExpandAnimator.setDuration(getContext().getResources().getInteger(
			android.R.integer.config_shortAnimTime));
		mExpandAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				dispatchOffset(child);
				child.requestLayout();
			}
		});
		mExpandAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				performToggleState(child);
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});
		mExpandAnimator.start();
	}

	public boolean isRunningAnimation() {
		View child = findExpandableView();
		LayoutParams p = (LayoutParams) child.getLayoutParams();
		if (p.isExpanding == true) {
			return true;
		}
		return false;
	}

	private void dispatchOffset(View child) {
		if (mListener != null) {
			mListener.onExpandOffset(this, child, child.getHeight(),
				!isExpanded());
		}
	}

	private void performToggleState(View child) {
		LayoutParams p = (LayoutParams) child.getLayoutParams();
		if (p.isExpanded) {
			p.isExpanded = false;
			if (mListener != null) {
				mListener.onToggle(this, child, false);
			}
			child.setVisibility(View.GONE);
			p.height = p.originalHeight;
		} else {
			p.isExpanded = true;
			if (mListener != null) {
				mListener.onToggle(this, child, true);
			}
		}
		p.isExpanding = false;
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		SavedState ss = new SavedState(super.onSaveInstanceState());
		if (isExpanded()) {
			ss.isExpanded = true;
		}
		return ss;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());
		if (ss.isExpanded) {
			View child = findExpandableView();
			if (child != null) {
				setExpanded(true);
			}
		}
	}

	private static class SavedState extends BaseSavedState {

		boolean isExpanded;

		public SavedState(Parcel source) {
			super(source);
			isExpanded = source.readInt() == 1;
		}

		public SavedState(Parcelable superState) {
			super(superState);
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(isExpanded ? 1 : 0);
		}

		@SuppressWarnings("unused")
		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

			@Override
			public SavedState createFromParcel(Parcel source) {
				return new SavedState(source);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(this.getContext(), attrs);
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.MATCH_PARENT,
			LayoutParams.WRAP_CONTENT);
	}

	@Override
	protected LayoutParams generateLayoutParams(
		android.view.ViewGroup.LayoutParams p) {
		return new LayoutParams(p);
	}

	@Override
	protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
		return super.checkLayoutParams(p) && (p instanceof LayoutParams);
	}

	public static class LayoutParams extends LinearLayout.LayoutParams {
		private static final int NO_MESURED_HEIGHT = -10;
		int originalHeight = NO_MESURED_HEIGHT;
		boolean isExpanded;
		boolean canExpand;
		boolean isExpanding;

		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
			TypedArray a = c.obtainStyledAttributes(attrs,
				R.styleable.ExpandableLayout);
			canExpand = a.getBoolean(R.styleable.ExpandableLayout_canExpand,
				false);
			isExpanded = a.getBoolean(R.styleable.ExpandableLayout_startExpanded,
				false);
			originalHeight = this.height;
			a.recycle();
		}

		public LayoutParams(int width, int height, float weight) {
			super(width, height, weight);
			originalHeight = this.height;
		}

		public LayoutParams(int width, int height) {
			super(width, height);
			originalHeight = this.height;
		}

		public LayoutParams(android.view.ViewGroup.LayoutParams source) {
			super(source);
			originalHeight = this.height;
		}

		@TargetApi(Build.VERSION_CODES.KITKAT)
		public LayoutParams(LinearLayout.LayoutParams source) {
			super(source);
			originalHeight = this.height;
		}

		public LayoutParams(MarginLayoutParams source) {
			super(source);
			originalHeight = this.height;
		}

		public void setHeight(int height) {
			this.height = height;
		}
	}

	public static interface OnExpandListener {
		public void onToggle(ExpandableLayout view, View child, boolean isExpanded);

		public void onExpandOffset(ExpandableLayout view, View child,
								   float offset, boolean isExpanding);
	}
}
