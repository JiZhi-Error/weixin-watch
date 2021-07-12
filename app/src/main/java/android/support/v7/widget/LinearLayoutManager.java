/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.PointF
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.View
 *  android.view.accessibility.AccessibilityEvent
 */
package android.support.v7.widget;

import android.content.Context;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ScrollbarHelper;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import java.util.List;

public class LinearLayoutManager
extends RecyclerView.LayoutManager
implements ItemTouchHelper.ViewDropHandler {
    private static final boolean DEBUG = false;
    public static final int HORIZONTAL = 0;
    public static final int INVALID_OFFSET = Integer.MIN_VALUE;
    private static final float MAX_SCROLL_FACTOR = 0.33333334f;
    private static final String TAG = "LinearLayoutManager";
    public static final int VERTICAL = 1;
    final AnchorInfo mAnchorInfo;
    private boolean mLastStackFromEnd;
    private LayoutState mLayoutState;
    int mOrientation;
    OrientationHelper mOrientationHelper;
    SavedState mPendingSavedState = null;
    int mPendingScrollPosition = -1;
    int mPendingScrollPositionOffset = Integer.MIN_VALUE;
    private boolean mRecycleChildrenOnDetach;
    private boolean mReverseLayout = false;
    boolean mShouldReverseLayout = false;
    private boolean mSmoothScrollbarEnabled = true;
    private boolean mStackFromEnd = false;

    public LinearLayoutManager(Context context) {
        this(context, 1, false);
    }

    public LinearLayoutManager(Context context, int n2, boolean bl2) {
        this.mAnchorInfo = new AnchorInfo();
        this.setOrientation(n2);
        this.setReverseLayout(bl2);
        this.setAutoMeasureEnabled(true);
    }

    public LinearLayoutManager(Context object, AttributeSet attributeSet, int n2, int n3) {
        this.mAnchorInfo = new AnchorInfo();
        object = LinearLayoutManager.getProperties(object, attributeSet, n2, n3);
        this.setOrientation(object.orientation);
        this.setReverseLayout(object.reverseLayout);
        this.setStackFromEnd(object.stackFromEnd);
        this.setAutoMeasureEnabled(true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int computeScrollExtent(RecyclerView.State state) {
        boolean bl2 = false;
        if (this.getChildCount() == 0) {
            return 0;
        }
        this.ensureLayoutState();
        OrientationHelper orientationHelper = this.mOrientationHelper;
        boolean bl3 = !this.mSmoothScrollbarEnabled;
        View view = this.findFirstVisibleChildClosestToStart(bl3, true);
        bl3 = bl2;
        if (!this.mSmoothScrollbarEnabled) {
            bl3 = true;
        }
        return ScrollbarHelper.computeScrollExtent(state, orientationHelper, view, this.findFirstVisibleChildClosestToEnd(bl3, true), this, this.mSmoothScrollbarEnabled);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int computeScrollOffset(RecyclerView.State state) {
        boolean bl2 = false;
        if (this.getChildCount() == 0) {
            return 0;
        }
        this.ensureLayoutState();
        OrientationHelper orientationHelper = this.mOrientationHelper;
        boolean bl3 = !this.mSmoothScrollbarEnabled;
        View view = this.findFirstVisibleChildClosestToStart(bl3, true);
        bl3 = bl2;
        if (!this.mSmoothScrollbarEnabled) {
            bl3 = true;
        }
        return ScrollbarHelper.computeScrollOffset(state, orientationHelper, view, this.findFirstVisibleChildClosestToEnd(bl3, true), this, this.mSmoothScrollbarEnabled, this.mShouldReverseLayout);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int computeScrollRange(RecyclerView.State state) {
        boolean bl2 = false;
        if (this.getChildCount() == 0) {
            return 0;
        }
        this.ensureLayoutState();
        OrientationHelper orientationHelper = this.mOrientationHelper;
        boolean bl3 = !this.mSmoothScrollbarEnabled;
        View view = this.findFirstVisibleChildClosestToStart(bl3, true);
        bl3 = bl2;
        if (!this.mSmoothScrollbarEnabled) {
            bl3 = true;
        }
        return ScrollbarHelper.computeScrollRange(state, orientationHelper, view, this.findFirstVisibleChildClosestToEnd(bl3, true), this, this.mSmoothScrollbarEnabled);
    }

    private View findFirstReferenceChild(RecyclerView.Recycler recycler, RecyclerView.State state) {
        return this.findReferenceChild(recycler, state, 0, this.getChildCount(), state.getItemCount());
    }

    private View findFirstVisibleChildClosestToEnd(boolean bl2, boolean bl3) {
        if (this.mShouldReverseLayout) {
            return this.findOneVisibleChild(0, this.getChildCount(), bl2, bl3);
        }
        return this.findOneVisibleChild(this.getChildCount() - 1, -1, bl2, bl3);
    }

    private View findFirstVisibleChildClosestToStart(boolean bl2, boolean bl3) {
        if (this.mShouldReverseLayout) {
            return this.findOneVisibleChild(this.getChildCount() - 1, -1, bl2, bl3);
        }
        return this.findOneVisibleChild(0, this.getChildCount(), bl2, bl3);
    }

    private View findLastReferenceChild(RecyclerView.Recycler recycler, RecyclerView.State state) {
        return this.findReferenceChild(recycler, state, this.getChildCount() - 1, -1, state.getItemCount());
    }

    private View findReferenceChildClosestToEnd(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mShouldReverseLayout) {
            return this.findFirstReferenceChild(recycler, state);
        }
        return this.findLastReferenceChild(recycler, state);
    }

    private View findReferenceChildClosestToStart(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mShouldReverseLayout) {
            return this.findLastReferenceChild(recycler, state);
        }
        return this.findFirstReferenceChild(recycler, state);
    }

    private int fixLayoutEndGap(int n2, RecyclerView.Recycler recycler, RecyclerView.State state, boolean bl2) {
        int n3 = this.mOrientationHelper.getEndAfterPadding() - n2;
        if (n3 > 0) {
            n3 = -this.scrollBy(-n3, recycler, state);
            if (bl2 && (n2 = this.mOrientationHelper.getEndAfterPadding() - (n2 + n3)) > 0) {
                this.mOrientationHelper.offsetChildren(n2);
                return n2 + n3;
            }
        } else {
            return 0;
        }
        return n3;
    }

    private int fixLayoutStartGap(int n2, RecyclerView.Recycler recycler, RecyclerView.State state, boolean bl2) {
        int n3 = n2 - this.mOrientationHelper.getStartAfterPadding();
        if (n3 > 0) {
            n3 = -this.scrollBy(n3, recycler, state);
            if (bl2 && (n2 = n2 + n3 - this.mOrientationHelper.getStartAfterPadding()) > 0) {
                this.mOrientationHelper.offsetChildren(-n2);
                return n3 - n2;
            }
        } else {
            return 0;
        }
        return n3;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private View getChildClosestToEnd() {
        int n2;
        if (this.mShouldReverseLayout) {
            n2 = 0;
            return this.getChildAt(n2);
        }
        n2 = this.getChildCount() - 1;
        return this.getChildAt(n2);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private View getChildClosestToStart() {
        int n2;
        if (this.mShouldReverseLayout) {
            n2 = this.getChildCount() - 1;
            return this.getChildAt(n2);
        }
        n2 = 0;
        return this.getChildAt(n2);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void layoutForPredictiveAnimations(RecyclerView.Recycler recycler, RecyclerView.State state, int n2, int n3) {
        if (!state.willRunPredictiveAnimations() || this.getChildCount() == 0 || state.isPreLayout() || !this.supportsPredictiveItemAnimations()) {
            return;
        }
        int n4 = 0;
        int n5 = 0;
        List<RecyclerView.ViewHolder> list = recycler.getScrapList();
        int n6 = list.size();
        int n7 = this.getPosition(this.getChildAt(0));
        for (int i2 = 0; i2 < n6; ++i2) {
            RecyclerView.ViewHolder viewHolder = list.get(i2);
            if (viewHolder.isRemoved()) continue;
            boolean bl2 = viewHolder.getLayoutPosition() < n7;
            int n8 = bl2 != this.mShouldReverseLayout ? -1 : 1;
            if (n8 == -1) {
                n4 += this.mOrientationHelper.getDecoratedMeasurement(viewHolder.itemView);
                continue;
            }
            n5 += this.mOrientationHelper.getDecoratedMeasurement(viewHolder.itemView);
        }
        this.mLayoutState.mScrapList = list;
        if (n4 > 0) {
            this.updateLayoutStateToFillStart(this.getPosition(this.getChildClosestToStart()), n2);
            this.mLayoutState.mExtra = n4;
            this.mLayoutState.mAvailable = 0;
            this.mLayoutState.assignPositionFromScrapList();
            this.fill(recycler, this.mLayoutState, state, false);
        }
        if (n5 > 0) {
            this.updateLayoutStateToFillEnd(this.getPosition(this.getChildClosestToEnd()), n3);
            this.mLayoutState.mExtra = n5;
            this.mLayoutState.mAvailable = 0;
            this.mLayoutState.assignPositionFromScrapList();
            this.fill(recycler, this.mLayoutState, state, false);
        }
        this.mLayoutState.mScrapList = null;
    }

    private void logChildren() {
        Log.d((String)TAG, (String)"internal representation of views on the screen");
        for (int i2 = 0; i2 < this.getChildCount(); ++i2) {
            View view = this.getChildAt(i2);
            Log.d((String)TAG, (String)("item " + this.getPosition(view) + ", coord:" + this.mOrientationHelper.getDecoratedStart(view)));
        }
        Log.d((String)TAG, (String)"==============");
    }

    private void recycleByLayoutState(RecyclerView.Recycler recycler, LayoutState layoutState) {
        if (!layoutState.mRecycle || layoutState.mInfinite) {
            return;
        }
        if (layoutState.mLayoutDirection == -1) {
            this.recycleViewsFromEnd(recycler, layoutState.mScrollingOffset);
            return;
        }
        this.recycleViewsFromStart(recycler, layoutState.mScrollingOffset);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void recycleChildren(RecyclerView.Recycler recycler, int n2, int n3) {
        if (n2 == n3) return;
        if (n3 > n2) {
            --n3;
            while (n3 >= n2) {
                this.removeAndRecycleViewAt(n3, recycler);
                --n3;
            }
            return;
        } else {
            while (n2 > n3) {
                this.removeAndRecycleViewAt(n2, recycler);
                --n2;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void recycleViewsFromEnd(RecyclerView.Recycler recycler, int n2) {
        int n3 = this.getChildCount();
        if (n2 < 0) return;
        int n4 = this.mOrientationHelper.getEnd() - n2;
        if (this.mShouldReverseLayout) {
            for (n2 = 0; n2 < n3; ++n2) {
                View view = this.getChildAt(n2);
                if (this.mOrientationHelper.getDecoratedStart(view) >= n4) continue;
                this.recycleChildren(recycler, 0, n2);
                return;
            }
            return;
        }
        for (n2 = n3 - 1; n2 >= 0; --n2) {
            View view = this.getChildAt(n2);
            if (this.mOrientationHelper.getDecoratedStart(view) >= n4) continue;
            this.recycleChildren(recycler, n3 - 1, n2);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void recycleViewsFromStart(RecyclerView.Recycler recycler, int n2) {
        if (n2 < 0) return;
        int n3 = this.getChildCount();
        if (this.mShouldReverseLayout) {
            for (int i2 = n3 - 1; i2 >= 0; --i2) {
                View view = this.getChildAt(i2);
                if (this.mOrientationHelper.getDecoratedEnd(view) <= n2) continue;
                this.recycleChildren(recycler, n3 - 1, i2);
                return;
            }
            return;
        }
        for (int i3 = 0; i3 < n3; ++i3) {
            View view = this.getChildAt(i3);
            if (this.mOrientationHelper.getDecoratedEnd(view) <= n2) continue;
            this.recycleChildren(recycler, 0, i3);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void resolveShouldLayoutReverse() {
        boolean bl2 = true;
        if (this.mOrientation == 1 || !this.isLayoutRTL()) {
            this.mShouldReverseLayout = this.mReverseLayout;
            return;
        }
        if (this.mReverseLayout) {
            bl2 = false;
        }
        this.mShouldReverseLayout = bl2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean updateAnchorFromChildren(RecyclerView.Recycler recycler, RecyclerView.State state, AnchorInfo anchorInfo) {
        if (this.getChildCount() == 0) return false;
        View view = this.getFocusedChild();
        if (view != null && anchorInfo.isViewValidAsAnchor(view, state)) {
            anchorInfo.assignFromViewAndKeepVisibleRect(view);
            return true;
        }
        if (this.mLastStackFromEnd != this.mStackFromEnd) return false;
        recycler = anchorInfo.mLayoutFromEnd ? this.findReferenceChildClosestToEnd(recycler, state) : this.findReferenceChildClosestToStart(recycler, state);
        if (recycler == null) {
            return false;
        }
        anchorInfo.assignFromView((View)recycler);
        if (state.isPreLayout()) return true;
        if (!this.supportsPredictiveItemAnimations()) return true;
        if (this.mOrientationHelper.getDecoratedStart((View)recycler) < this.mOrientationHelper.getEndAfterPadding()) {
            if (this.mOrientationHelper.getDecoratedEnd((View)recycler) >= this.mOrientationHelper.getStartAfterPadding()) return true;
        }
        int n2 = 1;
        if (n2 == 0) return true;
        n2 = anchorInfo.mLayoutFromEnd ? this.mOrientationHelper.getEndAfterPadding() : this.mOrientationHelper.getStartAfterPadding();
        anchorInfo.mCoordinate = n2;
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean updateAnchorFromPendingData(RecyclerView.State state, AnchorInfo anchorInfo) {
        boolean bl2 = false;
        if (state.isPreLayout()) return false;
        if (this.mPendingScrollPosition == -1) {
            return false;
        }
        if (this.mPendingScrollPosition < 0 || this.mPendingScrollPosition >= state.getItemCount()) {
            this.mPendingScrollPosition = -1;
            this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
            return false;
        }
        anchorInfo.mPosition = this.mPendingScrollPosition;
        if (this.mPendingSavedState != null && this.mPendingSavedState.hasValidAnchor()) {
            anchorInfo.mLayoutFromEnd = this.mPendingSavedState.mAnchorLayoutFromEnd;
            if (anchorInfo.mLayoutFromEnd) {
                anchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mPendingSavedState.mAnchorOffset;
                return true;
            }
            anchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mPendingSavedState.mAnchorOffset;
            return true;
        }
        if (this.mPendingScrollPositionOffset == Integer.MIN_VALUE) {
            state = this.findViewByPosition(this.mPendingScrollPosition);
            if (state != null) {
                if (this.mOrientationHelper.getDecoratedMeasurement((View)state) > this.mOrientationHelper.getTotalSpace()) {
                    anchorInfo.assignCoordinateFromPadding();
                    return true;
                }
                if (this.mOrientationHelper.getDecoratedStart((View)state) - this.mOrientationHelper.getStartAfterPadding() < 0) {
                    anchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding();
                    anchorInfo.mLayoutFromEnd = false;
                    return true;
                }
                if (this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd((View)state) < 0) {
                    anchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding();
                    anchorInfo.mLayoutFromEnd = true;
                    return true;
                }
                int n2 = anchorInfo.mLayoutFromEnd ? this.mOrientationHelper.getDecoratedEnd((View)state) + this.mOrientationHelper.getTotalSpaceChange() : this.mOrientationHelper.getDecoratedStart((View)state);
                anchorInfo.mCoordinate = n2;
                return true;
            }
            if (this.getChildCount() > 0) {
                int n3 = this.getPosition(this.getChildAt(0));
                boolean bl3 = this.mPendingScrollPosition < n3;
                if (bl3 == this.mShouldReverseLayout) {
                    bl2 = true;
                }
                anchorInfo.mLayoutFromEnd = bl2;
            }
            anchorInfo.assignCoordinateFromPadding();
            return true;
        }
        anchorInfo.mLayoutFromEnd = this.mShouldReverseLayout;
        if (this.mShouldReverseLayout) {
            anchorInfo.mCoordinate = this.mOrientationHelper.getEndAfterPadding() - this.mPendingScrollPositionOffset;
            return true;
        }
        anchorInfo.mCoordinate = this.mOrientationHelper.getStartAfterPadding() + this.mPendingScrollPositionOffset;
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateAnchorInfoForLayout(RecyclerView.Recycler recycler, RecyclerView.State state, AnchorInfo anchorInfo) {
        if (this.updateAnchorFromPendingData(state, anchorInfo) || this.updateAnchorFromChildren(recycler, state, anchorInfo)) {
            return;
        }
        anchorInfo.assignCoordinateFromPadding();
        int n2 = this.mStackFromEnd ? state.getItemCount() - 1 : 0;
        anchorInfo.mPosition = n2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateLayoutState(int n2, int n3, boolean bl2, RecyclerView.State object) {
        int n4 = -1;
        int n5 = 1;
        this.mLayoutState.mInfinite = this.resolveIsInfinite();
        this.mLayoutState.mExtra = this.getExtraLayoutSpace((RecyclerView.State)object);
        this.mLayoutState.mLayoutDirection = n2;
        if (n2 == 1) {
            object = this.mLayoutState;
            ((LayoutState)object).mExtra += this.mOrientationHelper.getEndPadding();
            object = this.getChildClosestToEnd();
            LayoutState layoutState = this.mLayoutState;
            n2 = this.mShouldReverseLayout ? n4 : 1;
            layoutState.mItemDirection = n2;
            this.mLayoutState.mCurrentPosition = this.getPosition((View)object) + this.mLayoutState.mItemDirection;
            this.mLayoutState.mOffset = this.mOrientationHelper.getDecoratedEnd((View)object);
            n2 = this.mOrientationHelper.getDecoratedEnd((View)object) - this.mOrientationHelper.getEndAfterPadding();
        } else {
            object = this.getChildClosestToStart();
            LayoutState layoutState = this.mLayoutState;
            layoutState.mExtra += this.mOrientationHelper.getStartAfterPadding();
            layoutState = this.mLayoutState;
            n2 = this.mShouldReverseLayout ? n5 : -1;
            layoutState.mItemDirection = n2;
            this.mLayoutState.mCurrentPosition = this.getPosition((View)object) + this.mLayoutState.mItemDirection;
            this.mLayoutState.mOffset = this.mOrientationHelper.getDecoratedStart((View)object);
            n2 = -this.mOrientationHelper.getDecoratedStart((View)object) + this.mOrientationHelper.getStartAfterPadding();
        }
        this.mLayoutState.mAvailable = n3;
        if (bl2) {
            object = this.mLayoutState;
            ((LayoutState)object).mAvailable -= n2;
        }
        this.mLayoutState.mScrollingOffset = n2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateLayoutStateToFillEnd(int n2, int n3) {
        this.mLayoutState.mAvailable = this.mOrientationHelper.getEndAfterPadding() - n3;
        LayoutState layoutState = this.mLayoutState;
        int n4 = this.mShouldReverseLayout ? -1 : 1;
        layoutState.mItemDirection = n4;
        this.mLayoutState.mCurrentPosition = n2;
        this.mLayoutState.mLayoutDirection = 1;
        this.mLayoutState.mOffset = n3;
        this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
    }

    private void updateLayoutStateToFillEnd(AnchorInfo anchorInfo) {
        this.updateLayoutStateToFillEnd(anchorInfo.mPosition, anchorInfo.mCoordinate);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateLayoutStateToFillStart(int n2, int n3) {
        this.mLayoutState.mAvailable = n3 - this.mOrientationHelper.getStartAfterPadding();
        this.mLayoutState.mCurrentPosition = n2;
        LayoutState layoutState = this.mLayoutState;
        n2 = this.mShouldReverseLayout ? 1 : -1;
        layoutState.mItemDirection = n2;
        this.mLayoutState.mLayoutDirection = -1;
        this.mLayoutState.mOffset = n3;
        this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
    }

    private void updateLayoutStateToFillStart(AnchorInfo anchorInfo) {
        this.updateLayoutStateToFillStart(anchorInfo.mPosition, anchorInfo.mCoordinate);
    }

    @Override
    public void assertNotInLayoutOrScroll(String string2) {
        if (this.mPendingSavedState == null) {
            super.assertNotInLayoutOrScroll(string2);
        }
    }

    @Override
    public boolean canScrollHorizontally() {
        return this.mOrientation == 0;
    }

    @Override
    public boolean canScrollVertically() {
        return this.mOrientation == 1;
    }

    @Override
    public int computeHorizontalScrollExtent(RecyclerView.State state) {
        return this.computeScrollExtent(state);
    }

    @Override
    public int computeHorizontalScrollOffset(RecyclerView.State state) {
        return this.computeScrollOffset(state);
    }

    @Override
    public int computeHorizontalScrollRange(RecyclerView.State state) {
        return this.computeScrollRange(state);
    }

    /*
     * Enabled aggressive block sorting
     */
    public PointF computeScrollVectorForPosition(int n2) {
        boolean bl2 = false;
        if (this.getChildCount() == 0) {
            return null;
        }
        if (n2 < this.getPosition(this.getChildAt(0))) {
            bl2 = true;
        }
        n2 = bl2 != this.mShouldReverseLayout ? -1 : 1;
        if (this.mOrientation == 0) {
            return new PointF((float)n2, 0.0f);
        }
        return new PointF(0.0f, (float)n2);
    }

    @Override
    public int computeVerticalScrollExtent(RecyclerView.State state) {
        return this.computeScrollExtent(state);
    }

    @Override
    public int computeVerticalScrollOffset(RecyclerView.State state) {
        return this.computeScrollOffset(state);
    }

    @Override
    public int computeVerticalScrollRange(RecyclerView.State state) {
        return this.computeScrollRange(state);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    int convertFocusDirectionToLayoutDirection(int n2) {
        int n3 = -1;
        int n4 = 1;
        int n5 = Integer.MIN_VALUE;
        int n6 = n3;
        switch (n2) {
            default: {
                n6 = Integer.MIN_VALUE;
            }
            case 1: {
                return n6;
            }
            case 2: {
                return 1;
            }
            case 33: {
                n6 = n3;
                if (this.mOrientation == 1) return n6;
                return Integer.MIN_VALUE;
            }
            case 130: {
                n2 = n5;
                if (this.mOrientation != 1) return n2;
                return 1;
            }
            case 17: {
                n6 = n3;
                if (this.mOrientation == 0) return n6;
                return Integer.MIN_VALUE;
            }
            case 66: 
        }
        if (this.mOrientation != 0) return Integer.MIN_VALUE;
        return n4;
    }

    LayoutState createLayoutState() {
        return new LayoutState();
    }

    void ensureLayoutState() {
        if (this.mLayoutState == null) {
            this.mLayoutState = this.createLayoutState();
        }
        if (this.mOrientationHelper == null) {
            this.mOrientationHelper = OrientationHelper.createOrientationHelper(this, this.mOrientation);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    int fill(RecyclerView.Recycler recycler, LayoutState layoutState, RecyclerView.State state, boolean bl2) {
        int n2 = layoutState.mAvailable;
        if (layoutState.mScrollingOffset != Integer.MIN_VALUE) {
            if (layoutState.mAvailable < 0) {
                layoutState.mScrollingOffset += layoutState.mAvailable;
            }
            this.recycleByLayoutState(recycler, layoutState);
        }
        int n3 = layoutState.mAvailable + layoutState.mExtra;
        LayoutChunkResult layoutChunkResult = new LayoutChunkResult();
        while (true) {
            int n4;
            block15: {
                block14: {
                    block13: {
                        block12: {
                            if (!layoutState.mInfinite && n3 <= 0 || !layoutState.hasMore(state)) break block12;
                            layoutChunkResult.resetInternal();
                            this.layoutChunk(recycler, state, layoutState, layoutChunkResult);
                            if (!layoutChunkResult.mFinished) break block13;
                        }
                        return n2 - layoutState.mAvailable;
                    }
                    layoutState.mOffset += layoutChunkResult.mConsumed * layoutState.mLayoutDirection;
                    if (!layoutChunkResult.mIgnoreConsumed || this.mLayoutState.mScrapList != null) break block14;
                    n4 = n3;
                    if (state.isPreLayout()) break block15;
                }
                layoutState.mAvailable -= layoutChunkResult.mConsumed;
                n4 = n3 - layoutChunkResult.mConsumed;
            }
            if (layoutState.mScrollingOffset != Integer.MIN_VALUE) {
                layoutState.mScrollingOffset += layoutChunkResult.mConsumed;
                if (layoutState.mAvailable < 0) {
                    layoutState.mScrollingOffset += layoutState.mAvailable;
                }
                this.recycleByLayoutState(recycler, layoutState);
            }
            n3 = n4;
            if (!bl2) continue;
            n3 = n4;
            if (layoutChunkResult.mFocusable) return n2 - layoutState.mAvailable;
        }
    }

    public int findFirstCompletelyVisibleItemPosition() {
        View view = this.findOneVisibleChild(0, this.getChildCount(), true, false);
        if (view == null) {
            return -1;
        }
        return this.getPosition(view);
    }

    public int findFirstVisibleItemPosition() {
        View view = this.findOneVisibleChild(0, this.getChildCount(), false, true);
        if (view == null) {
            return -1;
        }
        return this.getPosition(view);
    }

    public int findLastCompletelyVisibleItemPosition() {
        View view = this.findOneVisibleChild(this.getChildCount() - 1, -1, true, false);
        if (view == null) {
            return -1;
        }
        return this.getPosition(view);
    }

    public int findLastVisibleItemPosition() {
        View view = this.findOneVisibleChild(this.getChildCount() - 1, -1, false, true);
        if (view == null) {
            return -1;
        }
        return this.getPosition(view);
    }

    /*
     * Enabled aggressive block sorting
     */
    View findOneVisibleChild(int n2, int n3, boolean bl2, boolean bl3) {
        this.ensureLayoutState();
        int n4 = this.mOrientationHelper.getStartAfterPadding();
        int n5 = this.mOrientationHelper.getEndAfterPadding();
        int n6 = n3 > n2 ? 1 : -1;
        View view = null;
        while (n2 != n3) {
            View view2 = this.getChildAt(n2);
            int n7 = this.mOrientationHelper.getDecoratedStart(view2);
            int n8 = this.mOrientationHelper.getDecoratedEnd(view2);
            View view3 = view;
            if (n7 < n5) {
                view3 = view;
                if (n8 > n4) {
                    if (!bl2 || n7 >= n4 && n8 <= n5) {
                        return view2;
                    }
                    view3 = view;
                    if (bl3) {
                        view3 = view;
                        if (view == null) {
                            view3 = view2;
                        }
                    }
                }
            }
            n2 += n6;
            view = view3;
        }
        return view;
    }

    /*
     * Enabled aggressive block sorting
     */
    View findReferenceChild(RecyclerView.Recycler object, RecyclerView.State object2, int n2, int n3, int n4) {
        this.ensureLayoutState();
        object2 = null;
        object = null;
        int n5 = this.mOrientationHelper.getStartAfterPadding();
        int n6 = this.mOrientationHelper.getEndAfterPadding();
        int n7 = n3 > n2 ? 1 : -1;
        while (n2 != n3) {
            View view = this.getChildAt(n2);
            int n8 = this.getPosition(view);
            Object object3 = object2;
            Object object4 = object;
            if (n8 >= 0) {
                object3 = object2;
                object4 = object;
                if (n8 < n4) {
                    if (((RecyclerView.LayoutParams)view.getLayoutParams()).isItemRemoved()) {
                        object3 = object2;
                        object4 = object;
                        if (object2 == null) {
                            object4 = object;
                            object3 = view;
                        }
                    } else {
                        if (this.mOrientationHelper.getDecoratedStart(view) < n6) {
                            object3 = view;
                            if (this.mOrientationHelper.getDecoratedEnd(view) >= n5) return object3;
                        }
                        object3 = object2;
                        object4 = object;
                        if (object == null) {
                            object3 = object2;
                            object4 = view;
                        }
                    }
                }
            }
            n2 += n7;
            object2 = object3;
            object = object4;
        }
        if (object != null) return object;
        object = object2;
        return object;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public View findViewByPosition(int n2) {
        View view;
        int n3 = this.getChildCount();
        if (n3 == 0) {
            return null;
        }
        int n4 = n2 - this.getPosition(this.getChildAt(0));
        if (n4 < 0) return super.findViewByPosition(n2);
        if (n4 >= n3) return super.findViewByPosition(n2);
        View view2 = view = this.getChildAt(n4);
        if (this.getPosition(view) == n2) return view2;
        return super.findViewByPosition(n2);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(-2, -2);
    }

    protected int getExtraLayoutSpace(RecyclerView.State state) {
        if (state.hasTargetScrollPosition()) {
            return this.mOrientationHelper.getTotalSpace();
        }
        return 0;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public boolean getRecycleChildrenOnDetach() {
        return this.mRecycleChildrenOnDetach;
    }

    public boolean getReverseLayout() {
        return this.mReverseLayout;
    }

    public boolean getStackFromEnd() {
        return this.mStackFromEnd;
    }

    protected boolean isLayoutRTL() {
        return this.getLayoutDirection() == 1;
    }

    public boolean isSmoothScrollbarEnabled() {
        return this.mSmoothScrollbarEnabled;
    }

    /*
     * Enabled aggressive block sorting
     */
    void layoutChunk(RecyclerView.Recycler recycler, RecyclerView.State object, LayoutState layoutState, LayoutChunkResult layoutChunkResult) {
        int n2;
        int n3;
        int n4;
        int n5;
        if ((recycler = layoutState.next(recycler)) == null) {
            layoutChunkResult.mFinished = true;
            return;
        }
        object = (RecyclerView.LayoutParams)recycler.getLayoutParams();
        if (layoutState.mScrapList == null) {
            boolean bl2 = this.mShouldReverseLayout;
            boolean bl3 = layoutState.mLayoutDirection == -1;
            if (bl2 == bl3) {
                this.addView((View)recycler);
            } else {
                this.addView((View)recycler, 0);
            }
        } else {
            boolean bl4 = this.mShouldReverseLayout;
            boolean bl5 = layoutState.mLayoutDirection == -1;
            if (bl4 == bl5) {
                this.addDisappearingView((View)recycler);
            } else {
                this.addDisappearingView((View)recycler, 0);
            }
        }
        this.measureChildWithMargins((View)recycler, 0, 0);
        layoutChunkResult.mConsumed = this.mOrientationHelper.getDecoratedMeasurement((View)recycler);
        if (this.mOrientation == 1) {
            if (this.isLayoutRTL()) {
                n5 = this.getWidth() - this.getPaddingRight();
                n4 = n5 - this.mOrientationHelper.getDecoratedMeasurementInOther((View)recycler);
            } else {
                n4 = this.getPaddingLeft();
                n5 = n4 + this.mOrientationHelper.getDecoratedMeasurementInOther((View)recycler);
            }
            if (layoutState.mLayoutDirection == -1) {
                n3 = layoutState.mOffset;
                n2 = layoutState.mOffset - layoutChunkResult.mConsumed;
            } else {
                n2 = layoutState.mOffset;
                n3 = layoutState.mOffset + layoutChunkResult.mConsumed;
            }
        } else {
            n2 = this.getPaddingTop();
            n3 = n2 + this.mOrientationHelper.getDecoratedMeasurementInOther((View)recycler);
            if (layoutState.mLayoutDirection == -1) {
                n5 = layoutState.mOffset;
                n4 = layoutState.mOffset - layoutChunkResult.mConsumed;
            } else {
                n4 = layoutState.mOffset;
                n5 = layoutState.mOffset + layoutChunkResult.mConsumed;
            }
        }
        this.layoutDecorated((View)recycler, n4 + ((RecyclerView.LayoutParams)object).leftMargin, n2 + ((RecyclerView.LayoutParams)object).topMargin, n5 - ((RecyclerView.LayoutParams)object).rightMargin, n3 - ((RecyclerView.LayoutParams)object).bottomMargin);
        if (((RecyclerView.LayoutParams)((Object)object)).isItemRemoved() || ((RecyclerView.LayoutParams)((Object)object)).isItemChanged()) {
            layoutChunkResult.mIgnoreConsumed = true;
        }
        layoutChunkResult.mFocusable = recycler.isFocusable();
    }

    void onAnchorReady(RecyclerView.Recycler recycler, RecyclerView.State state, AnchorInfo anchorInfo, int n2) {
    }

    @Override
    public void onDetachedFromWindow(RecyclerView recyclerView, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(recyclerView, recycler);
        if (this.mRecycleChildrenOnDetach) {
            this.removeAndRecycleAllViews(recycler);
            recycler.clear();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public View onFocusSearchFailed(View view, int n2, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.resolveShouldLayoutReverse();
        if (this.getChildCount() == 0) {
            return null;
        }
        if ((n2 = this.convertFocusDirectionToLayoutDirection(n2)) == Integer.MIN_VALUE) {
            return null;
        }
        this.ensureLayoutState();
        View view2 = n2 == -1 ? this.findReferenceChildClosestToStart(recycler, state) : this.findReferenceChildClosestToEnd(recycler, state);
        if (view2 == null) {
            return null;
        }
        this.ensureLayoutState();
        this.updateLayoutState(n2, (int)(0.33333334f * (float)this.mOrientationHelper.getTotalSpace()), false, state);
        this.mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
        this.mLayoutState.mRecycle = false;
        this.fill(recycler, this.mLayoutState, state, true);
        view = n2 == -1 ? this.getChildClosestToStart() : this.getChildClosestToEnd();
        if (view == view2) return null;
        recycler = view;
        if (view.isFocusable()) return recycler;
        return null;
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent object) {
        super.onInitializeAccessibilityEvent((AccessibilityEvent)object);
        if (this.getChildCount() > 0) {
            object = AccessibilityEventCompat.asRecord((AccessibilityEvent)object);
            ((AccessibilityRecordCompat)object).setFromIndex(this.findFirstVisibleItemPosition());
            ((AccessibilityRecordCompat)object).setToIndex(this.findLastVisibleItemPosition());
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int n2;
        int n3;
        int n4;
        if ((this.mPendingSavedState != null || this.mPendingScrollPosition != -1) && state.getItemCount() == 0) {
            this.removeAndRecycleAllViews(recycler);
            return;
        }
        if (this.mPendingSavedState != null && this.mPendingSavedState.hasValidAnchor()) {
            this.mPendingScrollPosition = this.mPendingSavedState.mAnchorPosition;
        }
        this.ensureLayoutState();
        this.mLayoutState.mRecycle = false;
        this.resolveShouldLayoutReverse();
        this.mAnchorInfo.reset();
        this.mAnchorInfo.mLayoutFromEnd = this.mShouldReverseLayout ^ this.mStackFromEnd;
        this.updateAnchorInfoForLayout(recycler, state, this.mAnchorInfo);
        int n5 = this.getExtraLayoutSpace(state);
        if (this.mLayoutState.mLastScrollDelta >= 0) {
            n4 = 0;
        } else {
            n4 = n5;
            n5 = 0;
        }
        int n6 = n4 + this.mOrientationHelper.getStartAfterPadding();
        n5 = n3 = n5 + this.mOrientationHelper.getEndPadding();
        n4 = n6;
        if (state.isPreLayout()) {
            n5 = n3;
            n4 = n6;
            if (this.mPendingScrollPosition != -1) {
                n5 = n3;
                n4 = n6;
                if (this.mPendingScrollPositionOffset != Integer.MIN_VALUE) {
                    View view = this.findViewByPosition(this.mPendingScrollPosition);
                    n5 = n3;
                    n4 = n6;
                    if (view != null) {
                        if (this.mShouldReverseLayout) {
                            n5 = this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(view) - this.mPendingScrollPositionOffset;
                        } else {
                            n5 = this.mOrientationHelper.getDecoratedStart(view);
                            n4 = this.mOrientationHelper.getStartAfterPadding();
                            n5 = this.mPendingScrollPositionOffset - (n5 - n4);
                        }
                        if (n5 > 0) {
                            n4 = n6 + n5;
                            n5 = n3;
                        } else {
                            n5 = n3 - n5;
                            n4 = n6;
                        }
                    }
                }
            }
        }
        n6 = this.mAnchorInfo.mLayoutFromEnd ? (this.mShouldReverseLayout ? 1 : -1) : (this.mShouldReverseLayout ? -1 : 1);
        this.onAnchorReady(recycler, state, this.mAnchorInfo, n6);
        this.detachAndScrapAttachedViews(recycler);
        this.mLayoutState.mInfinite = this.resolveIsInfinite();
        this.mLayoutState.mIsPreLayout = state.isPreLayout();
        if (this.mAnchorInfo.mLayoutFromEnd) {
            this.updateLayoutStateToFillStart(this.mAnchorInfo);
            this.mLayoutState.mExtra = n4;
            this.fill(recycler, this.mLayoutState, state, false);
            n6 = this.mLayoutState.mOffset;
            n2 = this.mLayoutState.mCurrentPosition;
            n4 = n5;
            if (this.mLayoutState.mAvailable > 0) {
                n4 = n5 + this.mLayoutState.mAvailable;
            }
            this.updateLayoutStateToFillEnd(this.mAnchorInfo);
            this.mLayoutState.mExtra = n4;
            LayoutState layoutState = this.mLayoutState;
            layoutState.mCurrentPosition += this.mLayoutState.mItemDirection;
            this.fill(recycler, this.mLayoutState, state, false);
            n5 = n3 = this.mLayoutState.mOffset;
            n4 = n6;
            if (this.mLayoutState.mAvailable > 0) {
                n5 = this.mLayoutState.mAvailable;
                this.updateLayoutStateToFillStart(n2, n6);
                this.mLayoutState.mExtra = n5;
                this.fill(recycler, this.mLayoutState, state, false);
                n4 = this.mLayoutState.mOffset;
                n5 = n3;
            }
        } else {
            this.updateLayoutStateToFillEnd(this.mAnchorInfo);
            this.mLayoutState.mExtra = n5;
            this.fill(recycler, this.mLayoutState, state, false);
            n6 = this.mLayoutState.mOffset;
            n2 = this.mLayoutState.mCurrentPosition;
            n5 = n4;
            if (this.mLayoutState.mAvailable > 0) {
                n5 = n4 + this.mLayoutState.mAvailable;
            }
            this.updateLayoutStateToFillStart(this.mAnchorInfo);
            this.mLayoutState.mExtra = n5;
            LayoutState layoutState = this.mLayoutState;
            layoutState.mCurrentPosition += this.mLayoutState.mItemDirection;
            this.fill(recycler, this.mLayoutState, state, false);
            n3 = this.mLayoutState.mOffset;
            n5 = n6;
            n4 = n3;
            if (this.mLayoutState.mAvailable > 0) {
                n5 = this.mLayoutState.mAvailable;
                this.updateLayoutStateToFillEnd(n2, n6);
                this.mLayoutState.mExtra = n5;
                this.fill(recycler, this.mLayoutState, state, false);
                n5 = this.mLayoutState.mOffset;
                n4 = n3;
            }
        }
        n6 = n5;
        n3 = n4;
        if (this.getChildCount() > 0) {
            if (this.mShouldReverseLayout ^ this.mStackFromEnd) {
                n6 = this.fixLayoutEndGap(n5, recycler, state, true);
                n3 = n4 + n6;
                n4 = this.fixLayoutStartGap(n3, recycler, state, false);
                n3 += n4;
                n6 = n5 + n6 + n4;
            } else {
                n6 = this.fixLayoutStartGap(n4, recycler, state, true);
                n2 = this.fixLayoutEndGap(n5 += n6, recycler, state, false);
                n3 = n4 + n6 + n2;
                n6 = n5 + n2;
            }
        }
        this.layoutForPredictiveAnimations(recycler, state, n3, n6);
        if (!state.isPreLayout()) {
            this.mPendingScrollPosition = -1;
            this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
            this.mOrientationHelper.onLayoutComplete();
        }
        this.mLastStackFromEnd = this.mStackFromEnd;
        this.mPendingSavedState = null;
    }

    @Override
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            this.mPendingSavedState = (SavedState)parcelable;
            this.requestLayout();
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        if (this.mPendingSavedState != null) {
            return new SavedState(this.mPendingSavedState);
        }
        SavedState savedState = new SavedState();
        if (this.getChildCount() > 0) {
            boolean bl2;
            this.ensureLayoutState();
            savedState.mAnchorLayoutFromEnd = bl2 = this.mLastStackFromEnd ^ this.mShouldReverseLayout;
            if (bl2) {
                View view = this.getChildClosestToEnd();
                savedState.mAnchorOffset = this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(view);
                savedState.mAnchorPosition = this.getPosition(view);
                return savedState;
            }
            View view = this.getChildClosestToStart();
            savedState.mAnchorPosition = this.getPosition(view);
            savedState.mAnchorOffset = this.mOrientationHelper.getDecoratedStart(view) - this.mOrientationHelper.getStartAfterPadding();
            return savedState;
        }
        savedState.invalidateAnchor();
        return savedState;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void prepareForDrop(View view, View view2, int n2, int n3) {
        this.assertNotInLayoutOrScroll("Cannot drop a view during a scroll or layout calculation");
        this.ensureLayoutState();
        this.resolveShouldLayoutReverse();
        n2 = this.getPosition(view);
        n3 = this.getPosition(view2);
        n2 = n2 < n3 ? 1 : -1;
        if (this.mShouldReverseLayout) {
            if (n2 == 1) {
                this.scrollToPositionWithOffset(n3, this.mOrientationHelper.getEndAfterPadding() - (this.mOrientationHelper.getDecoratedStart(view2) + this.mOrientationHelper.getDecoratedMeasurement(view)));
                return;
            }
            this.scrollToPositionWithOffset(n3, this.mOrientationHelper.getEndAfterPadding() - this.mOrientationHelper.getDecoratedEnd(view2));
            return;
        }
        if (n2 == -1) {
            this.scrollToPositionWithOffset(n3, this.mOrientationHelper.getDecoratedStart(view2));
            return;
        }
        this.scrollToPositionWithOffset(n3, this.mOrientationHelper.getDecoratedEnd(view2) - this.mOrientationHelper.getDecoratedMeasurement(view));
    }

    boolean resolveIsInfinite() {
        return this.mOrientationHelper.getMode() == 0 && this.mOrientationHelper.getEnd() == 0;
    }

    /*
     * Enabled aggressive block sorting
     */
    int scrollBy(int n2, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int n3;
        int n4;
        int n5;
        block5: {
            block4: {
                if (this.getChildCount() == 0 || n2 == 0) break block4;
                this.mLayoutState.mRecycle = true;
                this.ensureLayoutState();
                n5 = n2 > 0 ? 1 : -1;
                n4 = Math.abs(n2);
                this.updateLayoutState(n5, n4, true, state);
                n3 = this.mLayoutState.mScrollingOffset + this.fill(recycler, this.mLayoutState, state, false);
                if (n3 >= 0) break block5;
            }
            return 0;
        }
        if (n4 > n3) {
            n2 = n5 * n3;
        }
        this.mOrientationHelper.offsetChildren(-n2);
        this.mLayoutState.mLastScrollDelta = n2;
        return n2;
    }

    @Override
    public int scrollHorizontallyBy(int n2, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 1) {
            return 0;
        }
        return this.scrollBy(n2, recycler, state);
    }

    @Override
    public void scrollToPosition(int n2) {
        this.mPendingScrollPosition = n2;
        this.mPendingScrollPositionOffset = Integer.MIN_VALUE;
        if (this.mPendingSavedState != null) {
            this.mPendingSavedState.invalidateAnchor();
        }
        this.requestLayout();
    }

    public void scrollToPositionWithOffset(int n2, int n3) {
        this.mPendingScrollPosition = n2;
        this.mPendingScrollPositionOffset = n3;
        if (this.mPendingSavedState != null) {
            this.mPendingSavedState.invalidateAnchor();
        }
        this.requestLayout();
    }

    @Override
    public int scrollVerticallyBy(int n2, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 0) {
            return 0;
        }
        return this.scrollBy(n2, recycler, state);
    }

    public void setOrientation(int n2) {
        if (n2 != 0 && n2 != 1) {
            throw new IllegalArgumentException("invalid orientation:" + n2);
        }
        this.assertNotInLayoutOrScroll(null);
        if (n2 == this.mOrientation) {
            return;
        }
        this.mOrientation = n2;
        this.mOrientationHelper = null;
        this.requestLayout();
    }

    public void setRecycleChildrenOnDetach(boolean bl2) {
        this.mRecycleChildrenOnDetach = bl2;
    }

    public void setReverseLayout(boolean bl2) {
        this.assertNotInLayoutOrScroll(null);
        if (bl2 == this.mReverseLayout) {
            return;
        }
        this.mReverseLayout = bl2;
        this.requestLayout();
    }

    public void setSmoothScrollbarEnabled(boolean bl2) {
        this.mSmoothScrollbarEnabled = bl2;
    }

    public void setStackFromEnd(boolean bl2) {
        this.assertNotInLayoutOrScroll(null);
        if (this.mStackFromEnd == bl2) {
            return;
        }
        this.mStackFromEnd = bl2;
        this.requestLayout();
    }

    @Override
    boolean shouldMeasureTwice() {
        return this.getHeightMode() != 0x40000000 && this.getWidthMode() != 0x40000000 && this.hasFlexibleChildInBothOrientations();
    }

    @Override
    public void smoothScrollToPosition(RecyclerView object, RecyclerView.State state, int n2) {
        object = new LinearSmoothScroller(object.getContext()){

            @Override
            public PointF computeScrollVectorForPosition(int n2) {
                return LinearLayoutManager.this.computeScrollVectorForPosition(n2);
            }
        };
        ((RecyclerView.SmoothScroller)object).setTargetPosition(n2);
        this.startSmoothScroll((RecyclerView.SmoothScroller)object);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return this.mPendingSavedState == null && this.mLastStackFromEnd == this.mStackFromEnd;
    }

    /*
     * Enabled aggressive block sorting
     */
    void validateChildOrder() {
        boolean bl2 = true;
        boolean bl3 = true;
        Log.d((String)TAG, (String)("validating child count " + this.getChildCount()));
        if (this.getChildCount() < 1) {
            return;
        }
        int n2 = this.getPosition(this.getChildAt(0));
        int n3 = this.mOrientationHelper.getDecoratedStart(this.getChildAt(0));
        if (this.mShouldReverseLayout) {
            int n4 = 1;
            while (n4 < this.getChildCount()) {
                Object object = this.getChildAt(n4);
                int n5 = this.getPosition((View)object);
                int n6 = this.mOrientationHelper.getDecoratedStart((View)object);
                if (n5 < n2) {
                    this.logChildren();
                    object = new StringBuilder().append("detected invalid position. loc invalid? ");
                    if (n6 < n3) {
                        throw new RuntimeException(((StringBuilder)object).append(bl3).toString());
                    }
                    bl3 = false;
                    throw new RuntimeException(((StringBuilder)object).append(bl3).toString());
                }
                if (n6 > n3) {
                    this.logChildren();
                    throw new RuntimeException("detected invalid location");
                }
                ++n4;
            }
            return;
        }
        int n7 = 1;
        while (n7 < this.getChildCount()) {
            Object object = this.getChildAt(n7);
            int n8 = this.getPosition((View)object);
            int n9 = this.mOrientationHelper.getDecoratedStart((View)object);
            if (n8 < n2) {
                this.logChildren();
                object = new StringBuilder().append("detected invalid position. loc invalid? ");
                if (n9 < n3) {
                    bl3 = bl2;
                    throw new RuntimeException(((StringBuilder)object).append(bl3).toString());
                }
                bl3 = false;
                throw new RuntimeException(((StringBuilder)object).append(bl3).toString());
            }
            if (n9 < n3) {
                this.logChildren();
                throw new RuntimeException("detected invalid location");
            }
            ++n7;
        }
    }

    class AnchorInfo {
        int mCoordinate;
        boolean mLayoutFromEnd;
        int mPosition;

        AnchorInfo() {
        }

        private boolean isViewValidAsAnchor(View object, RecyclerView.State state) {
            return !((RecyclerView.LayoutParams)((Object)(object = (RecyclerView.LayoutParams)object.getLayoutParams()))).isItemRemoved() && ((RecyclerView.LayoutParams)((Object)object)).getViewLayoutPosition() >= 0 && ((RecyclerView.LayoutParams)((Object)object)).getViewLayoutPosition() < state.getItemCount();
        }

        /*
         * Enabled aggressive block sorting
         */
        void assignCoordinateFromPadding() {
            int n2 = this.mLayoutFromEnd ? LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding() : LinearLayoutManager.this.mOrientationHelper.getStartAfterPadding();
            this.mCoordinate = n2;
        }

        /*
         * Enabled aggressive block sorting
         */
        public void assignFromView(View view) {
            this.mCoordinate = this.mLayoutFromEnd ? LinearLayoutManager.this.mOrientationHelper.getDecoratedEnd(view) + LinearLayoutManager.this.mOrientationHelper.getTotalSpaceChange() : LinearLayoutManager.this.mOrientationHelper.getDecoratedStart(view);
            this.mPosition = LinearLayoutManager.this.getPosition(view);
        }

        /*
         * Enabled aggressive block sorting
         */
        public void assignFromViewAndKeepVisibleRect(View view) {
            int n2 = LinearLayoutManager.this.mOrientationHelper.getTotalSpaceChange();
            if (n2 >= 0) {
                this.assignFromView(view);
                return;
            } else {
                this.mPosition = LinearLayoutManager.this.getPosition(view);
                if (this.mLayoutFromEnd) {
                    int n3 = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding() - n2 - LinearLayoutManager.this.mOrientationHelper.getDecoratedEnd(view);
                    this.mCoordinate = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding() - n3;
                    if (n3 <= 0) return;
                    n2 = LinearLayoutManager.this.mOrientationHelper.getDecoratedMeasurement(view);
                    int n4 = this.mCoordinate;
                    int n5 = LinearLayoutManager.this.mOrientationHelper.getStartAfterPadding();
                    if ((n2 = n4 - n2 - (n5 + Math.min(LinearLayoutManager.this.mOrientationHelper.getDecoratedStart(view) - n5, 0))) >= 0) return;
                    this.mCoordinate += Math.min(n3, -n2);
                    return;
                } else {
                    int n6 = LinearLayoutManager.this.mOrientationHelper.getDecoratedStart(view);
                    int n7 = n6 - LinearLayoutManager.this.mOrientationHelper.getStartAfterPadding();
                    this.mCoordinate = n6;
                    if (n7 <= 0) return;
                    int n8 = LinearLayoutManager.this.mOrientationHelper.getDecoratedMeasurement(view);
                    int n9 = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding();
                    int n10 = LinearLayoutManager.this.mOrientationHelper.getDecoratedEnd(view);
                    n2 = LinearLayoutManager.this.mOrientationHelper.getEndAfterPadding() - Math.min(0, n9 - n2 - n10) - (n6 + n8);
                    if (n2 >= 0) return;
                    this.mCoordinate -= Math.min(n7, -n2);
                    return;
                }
            }
        }

        void reset() {
            this.mPosition = -1;
            this.mCoordinate = Integer.MIN_VALUE;
            this.mLayoutFromEnd = false;
        }

        public String toString() {
            return "AnchorInfo{mPosition=" + this.mPosition + ", mCoordinate=" + this.mCoordinate + ", mLayoutFromEnd=" + this.mLayoutFromEnd + '}';
        }
    }

    protected static class LayoutChunkResult {
        public int mConsumed;
        public boolean mFinished;
        public boolean mFocusable;
        public boolean mIgnoreConsumed;

        protected LayoutChunkResult() {
        }

        void resetInternal() {
            this.mConsumed = 0;
            this.mFinished = false;
            this.mIgnoreConsumed = false;
            this.mFocusable = false;
        }
    }

    static class LayoutState {
        static final int INVALID_LAYOUT = Integer.MIN_VALUE;
        static final int ITEM_DIRECTION_HEAD = -1;
        static final int ITEM_DIRECTION_TAIL = 1;
        static final int LAYOUT_END = 1;
        static final int LAYOUT_START = -1;
        static final int SCOLLING_OFFSET_NaN = Integer.MIN_VALUE;
        static final String TAG = "LinearLayoutManager#LayoutState";
        int mAvailable;
        int mCurrentPosition;
        int mExtra = 0;
        boolean mInfinite;
        boolean mIsPreLayout = false;
        int mItemDirection;
        int mLastScrollDelta;
        int mLayoutDirection;
        int mOffset;
        boolean mRecycle = true;
        List<RecyclerView.ViewHolder> mScrapList = null;
        int mScrollingOffset;

        LayoutState() {
        }

        /*
         * Enabled aggressive block sorting
         */
        private View nextViewFromScrapList() {
            int n2 = this.mScrapList.size();
            int n3 = 0;
            while (n3 < n2) {
                View view = this.mScrapList.get((int)n3).itemView;
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view.getLayoutParams();
                if (!layoutParams.isItemRemoved() && this.mCurrentPosition == layoutParams.getViewLayoutPosition()) {
                    this.assignPositionFromScrapList(view);
                    return view;
                }
                ++n3;
            }
            return null;
        }

        public void assignPositionFromScrapList() {
            this.assignPositionFromScrapList(null);
        }

        public void assignPositionFromScrapList(View view) {
            if ((view = this.nextViewInLimitedList(view)) == null) {
                this.mCurrentPosition = -1;
                return;
            }
            this.mCurrentPosition = ((RecyclerView.LayoutParams)view.getLayoutParams()).getViewLayoutPosition();
        }

        boolean hasMore(RecyclerView.State state) {
            return this.mCurrentPosition >= 0 && this.mCurrentPosition < state.getItemCount();
        }

        void log() {
            Log.d((String)TAG, (String)("avail:" + this.mAvailable + ", ind:" + this.mCurrentPosition + ", dir:" + this.mItemDirection + ", offset:" + this.mOffset + ", layoutDir:" + this.mLayoutDirection));
        }

        View next(RecyclerView.Recycler recycler) {
            if (this.mScrapList != null) {
                return this.nextViewFromScrapList();
            }
            recycler = recycler.getViewForPosition(this.mCurrentPosition);
            this.mCurrentPosition += this.mItemDirection;
            return recycler;
        }

        /*
         * Enabled aggressive block sorting
         */
        public View nextViewInLimitedList(View view) {
            int n2 = this.mScrapList.size();
            View view2 = null;
            int n3 = Integer.MAX_VALUE;
            int n4 = 0;
            while (true) {
                View view3 = view2;
                if (n4 >= n2) return view3;
                View view4 = this.mScrapList.get((int)n4).itemView;
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)view4.getLayoutParams();
                view3 = view2;
                int n5 = n3;
                if (view4 != view) {
                    if (layoutParams.isItemRemoved()) {
                        n5 = n3;
                        view3 = view2;
                    } else {
                        int n6 = (layoutParams.getViewLayoutPosition() - this.mCurrentPosition) * this.mItemDirection;
                        view3 = view2;
                        n5 = n3;
                        if (n6 >= 0) {
                            view3 = view2;
                            n5 = n3;
                            if (n6 < n3) {
                                view2 = view4;
                                n5 = n6;
                                view3 = view2;
                                if (n6 == 0) {
                                    return view2;
                                }
                            }
                        }
                    }
                }
                ++n4;
                view2 = view3;
                n3 = n5;
            }
        }
    }

    public static class SavedState
    implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int n2) {
                return new SavedState[n2];
            }
        };
        boolean mAnchorLayoutFromEnd;
        int mAnchorOffset;
        int mAnchorPosition;

        public SavedState() {
        }

        /*
         * Enabled aggressive block sorting
         */
        SavedState(Parcel parcel) {
            boolean bl2 = true;
            this.mAnchorPosition = parcel.readInt();
            this.mAnchorOffset = parcel.readInt();
            if (parcel.readInt() != 1) {
                bl2 = false;
            }
            this.mAnchorLayoutFromEnd = bl2;
        }

        public SavedState(SavedState savedState) {
            this.mAnchorPosition = savedState.mAnchorPosition;
            this.mAnchorOffset = savedState.mAnchorOffset;
            this.mAnchorLayoutFromEnd = savedState.mAnchorLayoutFromEnd;
        }

        public int describeContents() {
            return 0;
        }

        boolean hasValidAnchor() {
            return this.mAnchorPosition >= 0;
        }

        void invalidateAnchor() {
            this.mAnchorPosition = -1;
        }

        /*
         * Enabled aggressive block sorting
         */
        public void writeToParcel(Parcel parcel, int n2) {
            parcel.writeInt(this.mAnchorPosition);
            parcel.writeInt(this.mAnchorOffset);
            n2 = this.mAnchorLayoutFromEnd ? 1 : 0;
            parcel.writeInt(n2);
        }
    }
}

