/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Rect
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.os.SystemClock
 *  android.util.AttributeSet
 *  android.view.KeyEvent
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewParent
 *  android.view.accessibility.AccessibilityEvent
 */
package android.support.v4.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.DrawerLayoutCompatApi21;
import android.support.v4.widget.DrawerLayoutImpl;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import java.util.ArrayList;
import java.util.List;

public class DrawerLayout
extends ViewGroup
implements DrawerLayoutImpl {
    private static final boolean ALLOW_EDGE_LOCK = false;
    static final boolean CAN_HIDE_DESCENDANTS;
    private static final boolean CHILDREN_DISALLOW_INTERCEPT = true;
    private static final int DEFAULT_SCRIM_COLOR = -1728053248;
    private static final int DRAWER_ELEVATION = 10;
    static final DrawerLayoutCompatImpl IMPL;
    static final int[] LAYOUT_ATTRS;
    public static final int LOCK_MODE_LOCKED_CLOSED = 1;
    public static final int LOCK_MODE_LOCKED_OPEN = 2;
    public static final int LOCK_MODE_UNDEFINED = 3;
    public static final int LOCK_MODE_UNLOCKED = 0;
    private static final int MIN_DRAWER_MARGIN = 64;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final int PEEK_DELAY = 160;
    private static final boolean SET_DRAWER_SHADOW_FROM_ELEVATION;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "DrawerLayout";
    private static final float TOUCH_SLOP_SENSITIVITY = 1.0f;
    private final ChildAccessibilityDelegate mChildAccessibilityDelegate = new ChildAccessibilityDelegate();
    private boolean mChildrenCanceledTouch;
    private boolean mDisallowInterceptRequested;
    private boolean mDrawStatusBarBackground;
    private float mDrawerElevation;
    private int mDrawerState;
    private boolean mFirstLayout = true;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private Object mLastInsets;
    private final ViewDragCallback mLeftCallback;
    private final ViewDragHelper mLeftDragger;
    @Nullable
    private DrawerListener mListener;
    private List<DrawerListener> mListeners;
    private int mLockModeEnd = 3;
    private int mLockModeLeft = 3;
    private int mLockModeRight = 3;
    private int mLockModeStart = 3;
    private int mMinDrawerMargin;
    private final ArrayList<View> mNonDrawerViews;
    private final ViewDragCallback mRightCallback;
    private final ViewDragHelper mRightDragger;
    private int mScrimColor = -1728053248;
    private float mScrimOpacity;
    private Paint mScrimPaint = new Paint();
    private Drawable mShadowEnd = null;
    private Drawable mShadowLeft = null;
    private Drawable mShadowLeftResolved;
    private Drawable mShadowRight = null;
    private Drawable mShadowRightResolved;
    private Drawable mShadowStart = null;
    private Drawable mStatusBarBackground;
    private CharSequence mTitleLeft;
    private CharSequence mTitleRight;

    /*
     * Enabled aggressive block sorting
     */
    static {
        boolean bl2 = true;
        LAYOUT_ATTRS = new int[]{16842931};
        boolean bl3 = Build.VERSION.SDK_INT >= 19;
        CAN_HIDE_DESCENDANTS = bl3;
        bl3 = Build.VERSION.SDK_INT >= 21 ? bl2 : false;
        SET_DRAWER_SHADOW_FROM_ELEVATION = bl3;
        if (Build.VERSION.SDK_INT >= 21) {
            IMPL = new DrawerLayoutCompatImplApi21();
            return;
        }
        IMPL = new DrawerLayoutCompatImplBase();
    }

    public DrawerLayout(Context context) {
        this(context, null);
    }

    public DrawerLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DrawerLayout(Context context, AttributeSet attributeSet, int n2) {
        super(context, attributeSet, n2);
        this.setDescendantFocusability(262144);
        float f2 = this.getResources().getDisplayMetrics().density;
        this.mMinDrawerMargin = (int)(64.0f * f2 + 0.5f);
        float f3 = 400.0f * f2;
        this.mLeftCallback = new ViewDragCallback(3);
        this.mRightCallback = new ViewDragCallback(5);
        this.mLeftDragger = ViewDragHelper.create(this, 1.0f, this.mLeftCallback);
        this.mLeftDragger.setEdgeTrackingEnabled(1);
        this.mLeftDragger.setMinVelocity(f3);
        this.mLeftCallback.setDragger(this.mLeftDragger);
        this.mRightDragger = ViewDragHelper.create(this, 1.0f, this.mRightCallback);
        this.mRightDragger.setEdgeTrackingEnabled(2);
        this.mRightDragger.setMinVelocity(f3);
        this.mRightCallback.setDragger(this.mRightDragger);
        this.setFocusableInTouchMode(true);
        ViewCompat.setImportantForAccessibility((View)this, 1);
        ViewCompat.setAccessibilityDelegate((View)this, new AccessibilityDelegate());
        ViewGroupCompat.setMotionEventSplittingEnabled(this, false);
        if (ViewCompat.getFitsSystemWindows((View)this)) {
            IMPL.configureApplyInsets((View)this);
            this.mStatusBarBackground = IMPL.getDefaultStatusBarBackground(context);
        }
        this.mDrawerElevation = 10.0f * f2;
        this.mNonDrawerViews = new ArrayList();
    }

    static String gravityToString(int n2) {
        if ((n2 & 3) == 3) {
            return "LEFT";
        }
        if ((n2 & 5) == 5) {
            return "RIGHT";
        }
        return Integer.toHexString(n2);
    }

    private static boolean hasOpaqueBackground(View view) {
        boolean bl2 = false;
        view = view.getBackground();
        boolean bl3 = bl2;
        if (view != null) {
            bl3 = bl2;
            if (view.getOpacity() == -1) {
                bl3 = true;
            }
        }
        return bl3;
    }

    private boolean hasPeekingDrawer() {
        int n2 = this.getChildCount();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (!((LayoutParams)this.getChildAt((int)i2).getLayoutParams()).isPeeking) continue;
            return true;
        }
        return false;
    }

    private boolean hasVisibleDrawer() {
        return this.findVisibleDrawer() != null;
    }

    static boolean includeChildForAccessibility(View view) {
        return ViewCompat.getImportantForAccessibility(view) != 4 && ViewCompat.getImportantForAccessibility(view) != 2;
    }

    private boolean mirror(Drawable drawable2, int n2) {
        if (drawable2 == null || !DrawableCompat.isAutoMirrored(drawable2)) {
            return false;
        }
        DrawableCompat.setLayoutDirection(drawable2, n2);
        return true;
    }

    private Drawable resolveLeftShadow() {
        int n2 = ViewCompat.getLayoutDirection((View)this);
        if (n2 == 0) {
            if (this.mShadowStart != null) {
                this.mirror(this.mShadowStart, n2);
                return this.mShadowStart;
            }
        } else if (this.mShadowEnd != null) {
            this.mirror(this.mShadowEnd, n2);
            return this.mShadowEnd;
        }
        return this.mShadowLeft;
    }

    private Drawable resolveRightShadow() {
        int n2 = ViewCompat.getLayoutDirection((View)this);
        if (n2 == 0) {
            if (this.mShadowEnd != null) {
                this.mirror(this.mShadowEnd, n2);
                return this.mShadowEnd;
            }
        } else if (this.mShadowStart != null) {
            this.mirror(this.mShadowStart, n2);
            return this.mShadowStart;
        }
        return this.mShadowRight;
    }

    private void resolveShadowDrawables() {
        if (SET_DRAWER_SHADOW_FROM_ELEVATION) {
            return;
        }
        this.mShadowLeftResolved = this.resolveLeftShadow();
        this.mShadowRightResolved = this.resolveRightShadow();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void updateChildrenImportantForAccessibility(View view, boolean bl2) {
        int n2 = this.getChildCount();
        int n3 = 0;
        while (n3 < n2) {
            View view2 = this.getChildAt(n3);
            if (!bl2 && !this.isDrawerView(view2) || bl2 && view2 == view) {
                ViewCompat.setImportantForAccessibility(view2, 1);
            } else {
                ViewCompat.setImportantForAccessibility(view2, 4);
            }
            ++n3;
        }
        return;
    }

    public void addDrawerListener(@NonNull DrawerListener drawerListener) {
        if (drawerListener == null) {
            return;
        }
        if (this.mListeners == null) {
            this.mListeners = new ArrayList<DrawerListener>();
        }
        this.mListeners.add(drawerListener);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void addFocusables(ArrayList<View> arrayList, int n2, int n3) {
        View view;
        int n4;
        if (this.getDescendantFocusability() == 393216) {
            return;
        }
        int n5 = this.getChildCount();
        int n6 = 0;
        for (n4 = 0; n4 < n5; ++n4) {
            view = this.getChildAt(n4);
            if (this.isDrawerView(view)) {
                if (!this.isDrawerOpen(view)) continue;
                n6 = 1;
                view.addFocusables(arrayList, n2, n3);
                continue;
            }
            this.mNonDrawerViews.add(view);
        }
        if (n6 == 0) {
            n6 = this.mNonDrawerViews.size();
            for (n4 = 0; n4 < n6; ++n4) {
                view = this.mNonDrawerViews.get(n4);
                if (view.getVisibility() != 0) continue;
                view.addFocusables(arrayList, n2, n3);
            }
        }
        this.mNonDrawerViews.clear();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void addView(View view, int n2, ViewGroup.LayoutParams layoutParams) {
        super.addView(view, n2, layoutParams);
        if (this.findOpenDrawer() != null || this.isDrawerView(view)) {
            ViewCompat.setImportantForAccessibility(view, 4);
        } else {
            ViewCompat.setImportantForAccessibility(view, 1);
        }
        if (!CAN_HIDE_DESCENDANTS) {
            ViewCompat.setAccessibilityDelegate(view, this.mChildAccessibilityDelegate);
        }
    }

    void cancelChildViewTouch() {
        if (!this.mChildrenCanceledTouch) {
            long l2 = SystemClock.uptimeMillis();
            MotionEvent motionEvent = MotionEvent.obtain((long)l2, (long)l2, (int)3, (float)0.0f, (float)0.0f, (int)0);
            int n2 = this.getChildCount();
            for (int i2 = 0; i2 < n2; ++i2) {
                this.getChildAt(i2).dispatchTouchEvent(motionEvent);
            }
            motionEvent.recycle();
            this.mChildrenCanceledTouch = true;
        }
    }

    boolean checkDrawerViewAbsoluteGravity(View view, int n2) {
        return (this.getDrawerViewAbsoluteGravity(view) & n2) == n2;
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams && super.checkLayoutParams(layoutParams);
    }

    public void closeDrawer(int n2) {
        this.closeDrawer(n2, true);
    }

    public void closeDrawer(int n2, boolean bl2) {
        View view = this.findDrawerWithGravity(n2);
        if (view == null) {
            throw new IllegalArgumentException("No drawer view found with gravity " + DrawerLayout.gravityToString(n2));
        }
        this.closeDrawer(view, bl2);
    }

    public void closeDrawer(View view) {
        this.closeDrawer(view, true);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void closeDrawer(View view, boolean bl2) {
        if (!this.isDrawerView(view)) {
            throw new IllegalArgumentException("View " + view + " is not a sliding drawer");
        }
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (this.mFirstLayout) {
            layoutParams.onScreen = 0.0f;
            layoutParams.openState = 0;
        } else if (bl2) {
            layoutParams.openState |= 4;
            if (this.checkDrawerViewAbsoluteGravity(view, 3)) {
                this.mLeftDragger.smoothSlideViewTo(view, -view.getWidth(), view.getTop());
            } else {
                this.mRightDragger.smoothSlideViewTo(view, this.getWidth(), view.getTop());
            }
        } else {
            this.moveDrawerToOffset(view, 0.0f);
            this.updateDrawerState(layoutParams.gravity, 0, view);
            view.setVisibility(4);
        }
        this.invalidate();
    }

    public void closeDrawers() {
        this.closeDrawers(false);
    }

    /*
     * Enabled aggressive block sorting
     */
    void closeDrawers(boolean bl2) {
        int n2 = 0;
        int n3 = this.getChildCount();
        for (int i2 = 0; i2 < n3; ++i2) {
            View view = this.getChildAt(i2);
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            int n4 = n2;
            if (this.isDrawerView(view)) {
                if (bl2 && !layoutParams.isPeeking) {
                    n4 = n2;
                } else {
                    n4 = view.getWidth();
                    n2 = this.checkDrawerViewAbsoluteGravity(view, 3) ? (n2 |= this.mLeftDragger.smoothSlideViewTo(view, -n4, view.getTop())) : (n2 |= this.mRightDragger.smoothSlideViewTo(view, this.getWidth(), view.getTop()));
                    layoutParams.isPeeking = false;
                    n4 = n2;
                }
            }
            n2 = n4;
        }
        this.mLeftCallback.removeCallbacks();
        this.mRightCallback.removeCallbacks();
        if (n2 != 0) {
            this.invalidate();
        }
    }

    public void computeScroll() {
        int n2 = this.getChildCount();
        float f2 = 0.0f;
        for (int i2 = 0; i2 < n2; ++i2) {
            f2 = Math.max(f2, ((LayoutParams)this.getChildAt((int)i2).getLayoutParams()).onScreen);
        }
        this.mScrimOpacity = f2;
        if (this.mLeftDragger.continueSettling(true) | this.mRightDragger.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }

    void dispatchOnDrawerClosed(View view) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if ((layoutParams.openState & 1) == 1) {
            layoutParams.openState = 0;
            if (this.mListeners != null) {
                for (int i2 = this.mListeners.size() - 1; i2 >= 0; --i2) {
                    this.mListeners.get(i2).onDrawerClosed(view);
                }
            }
            this.updateChildrenImportantForAccessibility(view, false);
            if (this.hasWindowFocus() && (view = this.getRootView()) != null) {
                view.sendAccessibilityEvent(32);
            }
        }
    }

    void dispatchOnDrawerOpened(View view) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if ((layoutParams.openState & 1) == 0) {
            layoutParams.openState = 1;
            if (this.mListeners != null) {
                for (int i2 = this.mListeners.size() - 1; i2 >= 0; --i2) {
                    this.mListeners.get(i2).onDrawerOpened(view);
                }
            }
            this.updateChildrenImportantForAccessibility(view, true);
            if (this.hasWindowFocus()) {
                this.sendAccessibilityEvent(32);
            }
            view.requestFocus();
        }
    }

    void dispatchOnDrawerSlide(View view, float f2) {
        if (this.mListeners != null) {
            for (int i2 = this.mListeners.size() - 1; i2 >= 0; --i2) {
                this.mListeners.get(i2).onDrawerSlide(view, f2);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    protected boolean drawChild(Canvas canvas, View view, long l2) {
        int n2 = this.getHeight();
        boolean bl2 = this.isContentView(view);
        int n3 = 0;
        int n4 = 0;
        int n5 = this.getWidth();
        int n6 = canvas.save();
        int n7 = n5;
        if (bl2) {
            int n8 = this.getChildCount();
            n3 = n4;
            for (n7 = 0; n7 < n8; ++n7) {
                View view2 = this.getChildAt(n7);
                n4 = n3;
                int n9 = n5;
                if (view2 != view) {
                    n4 = n3;
                    n9 = n5;
                    if (view2.getVisibility() == 0) {
                        n4 = n3;
                        n9 = n5;
                        if (DrawerLayout.hasOpaqueBackground(view2)) {
                            n4 = n3;
                            n9 = n5;
                            if (this.isDrawerView(view2)) {
                                int n10;
                                if (view2.getHeight() < n2) {
                                    n9 = n5;
                                    n4 = n3;
                                } else if (this.checkDrawerViewAbsoluteGravity(view2, 3)) {
                                    n10 = view2.getRight();
                                    n4 = n3;
                                    n9 = n5;
                                    if (n10 > n3) {
                                        n4 = n10;
                                        n9 = n5;
                                    }
                                } else {
                                    n10 = view2.getLeft();
                                    n4 = n3;
                                    n9 = n5;
                                    if (n10 < n5) {
                                        n9 = n10;
                                        n4 = n3;
                                    }
                                }
                            }
                        }
                    }
                }
                n3 = n4;
                n5 = n9;
            }
            canvas.clipRect(n3, 0, n5, this.getHeight());
            n7 = n5;
        }
        boolean bl3 = super.drawChild(canvas, view, l2);
        canvas.restoreToCount(n6);
        if (this.mScrimOpacity > 0.0f && bl2) {
            n5 = (int)((float)((this.mScrimColor & 0xFF000000) >>> 24) * this.mScrimOpacity);
            n4 = this.mScrimColor;
            this.mScrimPaint.setColor(n5 << 24 | n4 & 0xFFFFFF);
            canvas.drawRect((float)n3, 0.0f, (float)n7, (float)this.getHeight(), this.mScrimPaint);
            return bl3;
        } else {
            if (this.mShadowLeftResolved != null && this.checkDrawerViewAbsoluteGravity(view, 3)) {
                n3 = this.mShadowLeftResolved.getIntrinsicWidth();
                n5 = view.getRight();
                n7 = this.mLeftDragger.getEdgeSize();
                float f2 = Math.max(0.0f, Math.min((float)n5 / (float)n7, 1.0f));
                this.mShadowLeftResolved.setBounds(n5, view.getTop(), n5 + n3, view.getBottom());
                this.mShadowLeftResolved.setAlpha((int)(255.0f * f2));
                this.mShadowLeftResolved.draw(canvas);
                return bl3;
            }
            if (this.mShadowRightResolved == null || !this.checkDrawerViewAbsoluteGravity(view, 5)) return bl3;
            n3 = this.mShadowRightResolved.getIntrinsicWidth();
            n5 = view.getLeft();
            n7 = this.getWidth();
            n4 = this.mRightDragger.getEdgeSize();
            float f3 = Math.max(0.0f, Math.min((float)(n7 - n5) / (float)n4, 1.0f));
            this.mShadowRightResolved.setBounds(n5 - n3, view.getTop(), n5, view.getBottom());
            this.mShadowRightResolved.setAlpha((int)(255.0f * f3));
            this.mShadowRightResolved.draw(canvas);
            return bl3;
        }
    }

    View findDrawerWithGravity(int n2) {
        int n3 = GravityCompat.getAbsoluteGravity(n2, ViewCompat.getLayoutDirection((View)this));
        int n4 = this.getChildCount();
        for (n2 = 0; n2 < n4; ++n2) {
            View view = this.getChildAt(n2);
            if ((this.getDrawerViewAbsoluteGravity(view) & 7) != (n3 & 7)) continue;
            return view;
        }
        return null;
    }

    View findOpenDrawer() {
        int n2 = this.getChildCount();
        for (int i2 = 0; i2 < n2; ++i2) {
            View view = this.getChildAt(i2);
            if ((((LayoutParams)view.getLayoutParams()).openState & 1) != 1) continue;
            return view;
        }
        return null;
    }

    View findVisibleDrawer() {
        int n2 = this.getChildCount();
        for (int i2 = 0; i2 < n2; ++i2) {
            View view = this.getChildAt(i2);
            if (!this.isDrawerView(view) || !this.isDrawerVisible(view)) continue;
            return view;
        }
        return null;
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams) {
            return new LayoutParams((LayoutParams)layoutParams);
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams)layoutParams);
        }
        return new LayoutParams(layoutParams);
    }

    public float getDrawerElevation() {
        if (SET_DRAWER_SHADOW_FROM_ELEVATION) {
            return this.mDrawerElevation;
        }
        return 0.0f;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int getDrawerLockMode(int n2) {
        int n3 = ViewCompat.getLayoutDirection((View)this);
        switch (n2) {
            case 3: {
                if (this.mLockModeLeft != 3) {
                    return this.mLockModeLeft;
                }
                n2 = n3 == 0 ? this.mLockModeStart : this.mLockModeEnd;
                if (n2 == 3) return 0;
                return n2;
            }
            case 5: {
                if (this.mLockModeRight != 3) {
                    return this.mLockModeRight;
                }
                n2 = n3 == 0 ? this.mLockModeEnd : this.mLockModeStart;
                if (n2 == 3) return 0;
                return n2;
            }
            case 0x800003: {
                if (this.mLockModeStart != 3) {
                    return this.mLockModeStart;
                }
                n2 = n3 == 0 ? this.mLockModeLeft : this.mLockModeRight;
                if (n2 == 3) return 0;
                return n2;
            }
            default: {
                return 0;
            }
            case 0x800005: 
        }
        if (this.mLockModeEnd != 3) {
            return this.mLockModeEnd;
        }
        n2 = n3 == 0 ? this.mLockModeRight : this.mLockModeLeft;
        if (n2 == 3) return 0;
        return n2;
    }

    public int getDrawerLockMode(View view) {
        if (!this.isDrawerView(view)) {
            throw new IllegalArgumentException("View " + view + " is not a drawer");
        }
        return this.getDrawerLockMode(((LayoutParams)view.getLayoutParams()).gravity);
    }

    @Nullable
    public CharSequence getDrawerTitle(int n2) {
        if ((n2 = GravityCompat.getAbsoluteGravity(n2, ViewCompat.getLayoutDirection((View)this))) == 3) {
            return this.mTitleLeft;
        }
        if (n2 == 5) {
            return this.mTitleRight;
        }
        return null;
    }

    int getDrawerViewAbsoluteGravity(View view) {
        return GravityCompat.getAbsoluteGravity(((LayoutParams)view.getLayoutParams()).gravity, ViewCompat.getLayoutDirection((View)this));
    }

    float getDrawerViewOffset(View view) {
        return ((LayoutParams)view.getLayoutParams()).onScreen;
    }

    public Drawable getStatusBarBackgroundDrawable() {
        return this.mStatusBarBackground;
    }

    boolean isContentView(View view) {
        return ((LayoutParams)view.getLayoutParams()).gravity == 0;
    }

    public boolean isDrawerOpen(int n2) {
        View view = this.findDrawerWithGravity(n2);
        if (view != null) {
            return this.isDrawerOpen(view);
        }
        return false;
    }

    public boolean isDrawerOpen(View view) {
        if (!this.isDrawerView(view)) {
            throw new IllegalArgumentException("View " + view + " is not a drawer");
        }
        return (((LayoutParams)view.getLayoutParams()).openState & 1) == 1;
    }

    boolean isDrawerView(View view) {
        int n2 = GravityCompat.getAbsoluteGravity(((LayoutParams)view.getLayoutParams()).gravity, ViewCompat.getLayoutDirection(view));
        if ((n2 & 3) != 0) {
            return true;
        }
        return (n2 & 5) != 0;
    }

    public boolean isDrawerVisible(int n2) {
        View view = this.findDrawerWithGravity(n2);
        if (view != null) {
            return this.isDrawerVisible(view);
        }
        return false;
    }

    public boolean isDrawerVisible(View view) {
        if (!this.isDrawerView(view)) {
            throw new IllegalArgumentException("View " + view + " is not a drawer");
        }
        return ((LayoutParams)view.getLayoutParams()).onScreen > 0.0f;
    }

    /*
     * Enabled aggressive block sorting
     */
    void moveDrawerToOffset(View view, float f2) {
        float f3 = this.getDrawerViewOffset(view);
        int n2 = view.getWidth();
        int n3 = (int)((float)n2 * f3);
        n2 = (int)((float)n2 * f2) - n3;
        if (!this.checkDrawerViewAbsoluteGravity(view, 3)) {
            n2 = -n2;
        }
        view.offsetLeftAndRight(n2);
        this.setDrawerViewOffset(view, f2);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mFirstLayout = true;
    }

    public void onDraw(Canvas canvas) {
        int n2;
        super.onDraw(canvas);
        if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null && (n2 = IMPL.getTopInset(this.mLastInsets)) > 0) {
            this.mStatusBarBackground.setBounds(0, 0, this.getWidth(), n2);
            this.mStatusBarBackground.draw(canvas);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean bl2 = false;
        int n2 = MotionEventCompat.getActionMasked(motionEvent);
        boolean bl3 = this.mLeftDragger.shouldInterceptTouchEvent(motionEvent);
        boolean bl4 = this.mRightDragger.shouldInterceptTouchEvent(motionEvent);
        int n3 = 0;
        int n4 = 0;
        switch (n2) {
            default: {
                n2 = n4;
                break;
            }
            case 0: {
                float f2 = motionEvent.getX();
                float f3 = motionEvent.getY();
                this.mInitialMotionX = f2;
                this.mInitialMotionY = f3;
                n2 = n3;
                if (this.mScrimOpacity > 0.0f) {
                    motionEvent = this.mLeftDragger.findTopChildUnder((int)f2, (int)f3);
                    n2 = n3;
                    if (motionEvent != null) {
                        n2 = n3;
                        if (this.isContentView((View)motionEvent)) {
                            n2 = 1;
                        }
                    }
                }
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
                break;
            }
            case 2: {
                n2 = n4;
                if (!this.mLeftDragger.checkTouchSlop(3)) break;
                this.mLeftCallback.removeCallbacks();
                this.mRightCallback.removeCallbacks();
                n2 = n4;
                break;
            }
            case 1: 
            case 3: {
                this.closeDrawers(true);
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
                n2 = n4;
            }
        }
        if (bl3 | bl4) return true;
        if (n2 != 0) return true;
        if (this.hasPeekingDrawer()) return true;
        if (!this.mChildrenCanceledTouch) return bl2;
        return true;
    }

    public boolean onKeyDown(int n2, KeyEvent keyEvent) {
        if (n2 == 4 && this.hasVisibleDrawer()) {
            keyEvent.startTracking();
            return true;
        }
        return super.onKeyDown(n2, keyEvent);
    }

    public boolean onKeyUp(int n2, KeyEvent keyEvent) {
        if (n2 == 4) {
            keyEvent = this.findVisibleDrawer();
            if (keyEvent != null && this.getDrawerLockMode((View)keyEvent) == 0) {
                this.closeDrawers();
            }
            return keyEvent != null;
        }
        return super.onKeyUp(n2, keyEvent);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onLayout(boolean bl2, int n2, int n3, int n4, int n5) {
        this.mInLayout = true;
        int n6 = n4 - n2;
        int n7 = this.getChildCount();
        n4 = 0;
        while (true) {
            if (n4 >= n7) {
                this.mInLayout = false;
                this.mFirstLayout = false;
                return;
            }
            View view = this.getChildAt(n4);
            if (view.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                if (this.isContentView(view)) {
                    view.layout(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.leftMargin + view.getMeasuredWidth(), layoutParams.topMargin + view.getMeasuredHeight());
                } else {
                    float f2;
                    int n8;
                    int n9 = view.getMeasuredWidth();
                    int n10 = view.getMeasuredHeight();
                    if (this.checkDrawerViewAbsoluteGravity(view, 3)) {
                        n8 = -n9 + (int)((float)n9 * layoutParams.onScreen);
                        f2 = (float)(n9 + n8) / (float)n9;
                    } else {
                        n8 = n6 - (int)((float)n9 * layoutParams.onScreen);
                        f2 = (float)(n6 - n8) / (float)n9;
                    }
                    boolean bl3 = f2 != layoutParams.onScreen;
                    switch (layoutParams.gravity & 0x70) {
                        default: {
                            view.layout(n8, layoutParams.topMargin, n8 + n9, layoutParams.topMargin + n10);
                            break;
                        }
                        case 80: {
                            n2 = n5 - n3;
                            view.layout(n8, n2 - layoutParams.bottomMargin - view.getMeasuredHeight(), n8 + n9, n2 - layoutParams.bottomMargin);
                            break;
                        }
                        case 16: {
                            int n11 = n5 - n3;
                            int n12 = (n11 - n10) / 2;
                            if (n12 < layoutParams.topMargin) {
                                n2 = layoutParams.topMargin;
                            } else {
                                n2 = n12;
                                if (n12 + n10 > n11 - layoutParams.bottomMargin) {
                                    n2 = n11 - layoutParams.bottomMargin - n10;
                                }
                            }
                            view.layout(n8, n2, n8 + n9, n2 + n10);
                        }
                    }
                    if (bl3) {
                        this.setDrawerViewOffset(view, f2);
                    }
                    n2 = layoutParams.onScreen > 0.0f ? 0 : 4;
                    if (view.getVisibility() != n2) {
                        view.setVisibility(n2);
                    }
                }
            }
            ++n4;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onMeasure(int n2, int n3) {
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        block22: {
            block21: {
                n9 = View.MeasureSpec.getMode((int)n2);
                n8 = View.MeasureSpec.getMode((int)n3);
                n7 = View.MeasureSpec.getSize((int)n2);
                n6 = View.MeasureSpec.getSize((int)n3);
                if (n9 != 0x40000000) break block21;
                n5 = n6;
                n4 = n7;
                if (n8 == 0x40000000) break block22;
            }
            if (!this.isInEditMode()) {
                throw new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
            }
            if (n9 != Integer.MIN_VALUE && n9 == 0) {
                n7 = 300;
            }
            if (n8 == Integer.MIN_VALUE) {
                n4 = n7;
                n5 = n6;
            } else {
                n5 = n6;
                n4 = n7;
                if (n8 == 0) {
                    n5 = 300;
                    n4 = n7;
                }
            }
        }
        this.setMeasuredDimension(n4, n5);
        n8 = this.mLastInsets != null && ViewCompat.getFitsSystemWindows((View)this) ? 1 : 0;
        int n10 = ViewCompat.getLayoutDirection((View)this);
        n6 = 0;
        n7 = 0;
        int n11 = this.getChildCount();
        n9 = 0;
        while (n9 < n11) {
            View view = this.getChildAt(n9);
            if (view.getVisibility() != 8) {
                int n12;
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                if (n8 != 0) {
                    n12 = GravityCompat.getAbsoluteGravity(layoutParams.gravity, n10);
                    if (ViewCompat.getFitsSystemWindows(view)) {
                        IMPL.dispatchChildInsets(view, this.mLastInsets, n12);
                    } else {
                        IMPL.applyMarginInsets(layoutParams, this.mLastInsets, n12);
                    }
                }
                if (this.isContentView(view)) {
                    view.measure(View.MeasureSpec.makeMeasureSpec((int)(n4 - layoutParams.leftMargin - layoutParams.rightMargin), (int)0x40000000), View.MeasureSpec.makeMeasureSpec((int)(n5 - layoutParams.topMargin - layoutParams.bottomMargin), (int)0x40000000));
                } else {
                    int n13;
                    if (!this.isDrawerView(view)) {
                        throw new IllegalStateException("Child " + view + " at index " + n9 + " does not have a valid layout_gravity - must be Gravity.LEFT, " + "Gravity.RIGHT or Gravity.NO_GRAVITY");
                    }
                    if (SET_DRAWER_SHADOW_FROM_ELEVATION && ViewCompat.getElevation(view) != this.mDrawerElevation) {
                        ViewCompat.setElevation(view, this.mDrawerElevation);
                    }
                    if ((n12 = (n13 = this.getDrawerViewAbsoluteGravity(view) & 7) == 3 ? 1 : 0) != 0 && n6 != 0 || n12 == 0 && n7 != 0) {
                        throw new IllegalStateException("Child drawer has absolute gravity " + DrawerLayout.gravityToString(n13) + " but this " + TAG + " already has a " + "drawer view along that edge");
                    }
                    if (n12 != 0) {
                        n6 = 1;
                    } else {
                        n7 = 1;
                    }
                    view.measure(DrawerLayout.getChildMeasureSpec((int)n2, (int)(this.mMinDrawerMargin + layoutParams.leftMargin + layoutParams.rightMargin), (int)layoutParams.width), DrawerLayout.getChildMeasureSpec((int)n3, (int)(layoutParams.topMargin + layoutParams.bottomMargin), (int)layoutParams.height));
                }
            }
            ++n9;
        }
        return;
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        } else {
            View view;
            parcelable = (SavedState)parcelable;
            super.onRestoreInstanceState(parcelable.getSuperState());
            if (parcelable.openDrawerGravity != 0 && (view = this.findDrawerWithGravity(parcelable.openDrawerGravity)) != null) {
                this.openDrawer(view);
            }
            if (parcelable.lockModeLeft != 3) {
                this.setDrawerLockMode(parcelable.lockModeLeft, 3);
            }
            if (parcelable.lockModeRight != 3) {
                this.setDrawerLockMode(parcelable.lockModeRight, 5);
            }
            if (parcelable.lockModeStart != 3) {
                this.setDrawerLockMode(parcelable.lockModeStart, 0x800003);
            }
            if (parcelable.lockModeEnd == 3) return;
            this.setDrawerLockMode(parcelable.lockModeEnd, 0x800005);
            return;
        }
    }

    public void onRtlPropertiesChanged(int n2) {
        this.resolveShadowDrawables();
    }

    /*
     * Enabled aggressive block sorting
     */
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        int n2 = this.getChildCount();
        for (int i2 = 0; i2 < n2; ++i2) {
            LayoutParams layoutParams = (LayoutParams)this.getChildAt(i2).getLayoutParams();
            boolean bl2 = layoutParams.openState == 1;
            boolean bl3 = layoutParams.openState == 2;
            if (!bl2 && !bl3) continue;
            savedState.openDrawerGravity = layoutParams.gravity;
            break;
        }
        savedState.lockModeLeft = this.mLockModeLeft;
        savedState.lockModeRight = this.mLockModeRight;
        savedState.lockModeStart = this.mLockModeStart;
        savedState.lockModeEnd = this.mLockModeEnd;
        return savedState;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.mLeftDragger.processTouchEvent(motionEvent);
        this.mRightDragger.processTouchEvent(motionEvent);
        switch (motionEvent.getAction() & 0xFF) {
            default: {
                return true;
            }
            case 0: {
                float f2 = motionEvent.getX();
                float f3 = motionEvent.getY();
                this.mInitialMotionX = f2;
                this.mInitialMotionY = f3;
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
                return true;
            }
            case 1: {
                float f4 = motionEvent.getX();
                float f5 = motionEvent.getY();
                boolean bl2 = true;
                motionEvent = this.mLeftDragger.findTopChildUnder((int)f4, (int)f5);
                boolean bl3 = bl2;
                if (motionEvent != null) {
                    bl3 = bl2;
                    if (this.isContentView((View)motionEvent)) {
                        f4 -= this.mInitialMotionX;
                        f5 -= this.mInitialMotionY;
                        int n2 = this.mLeftDragger.getTouchSlop();
                        bl3 = bl2;
                        if (f4 * f4 + f5 * f5 < (float)(n2 * n2)) {
                            motionEvent = this.findOpenDrawer();
                            bl3 = bl2;
                            if (motionEvent != null) {
                                bl3 = this.getDrawerLockMode((View)motionEvent) == 2;
                            }
                        }
                    }
                }
                this.closeDrawers(bl3);
                this.mDisallowInterceptRequested = false;
                return true;
            }
            case 3: 
        }
        this.closeDrawers(true);
        this.mDisallowInterceptRequested = false;
        this.mChildrenCanceledTouch = false;
        return true;
    }

    public void openDrawer(int n2) {
        this.openDrawer(n2, true);
    }

    public void openDrawer(int n2, boolean bl2) {
        View view = this.findDrawerWithGravity(n2);
        if (view == null) {
            throw new IllegalArgumentException("No drawer view found with gravity " + DrawerLayout.gravityToString(n2));
        }
        this.openDrawer(view, bl2);
    }

    public void openDrawer(View view) {
        this.openDrawer(view, true);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void openDrawer(View view, boolean bl2) {
        if (!this.isDrawerView(view)) {
            throw new IllegalArgumentException("View " + view + " is not a sliding drawer");
        }
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (this.mFirstLayout) {
            layoutParams.onScreen = 1.0f;
            layoutParams.openState = 1;
            this.updateChildrenImportantForAccessibility(view, true);
        } else if (bl2) {
            layoutParams.openState |= 2;
            if (this.checkDrawerViewAbsoluteGravity(view, 3)) {
                this.mLeftDragger.smoothSlideViewTo(view, 0, view.getTop());
            } else {
                this.mRightDragger.smoothSlideViewTo(view, this.getWidth() - view.getWidth(), view.getTop());
            }
        } else {
            this.moveDrawerToOffset(view, 1.0f);
            this.updateDrawerState(layoutParams.gravity, 0, view);
            view.setVisibility(0);
        }
        this.invalidate();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void removeDrawerListener(@NonNull DrawerListener drawerListener) {
        if (drawerListener == null || this.mListeners == null) {
            return;
        }
        this.mListeners.remove(drawerListener);
    }

    public void requestDisallowInterceptTouchEvent(boolean bl2) {
        super.requestDisallowInterceptTouchEvent(bl2);
        this.mDisallowInterceptRequested = bl2;
        if (bl2) {
            this.closeDrawers(true);
        }
    }

    public void requestLayout() {
        if (!this.mInLayout) {
            super.requestLayout();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setChildInsets(Object object, boolean bl2) {
        this.mLastInsets = object;
        this.mDrawStatusBarBackground = bl2;
        bl2 = !bl2 && this.getBackground() == null;
        this.setWillNotDraw(bl2);
        this.requestLayout();
    }

    public void setDrawerElevation(float f2) {
        this.mDrawerElevation = f2;
        for (int i2 = 0; i2 < this.getChildCount(); ++i2) {
            View view = this.getChildAt(i2);
            if (!this.isDrawerView(view)) continue;
            ViewCompat.setElevation(view, this.mDrawerElevation);
        }
    }

    @Deprecated
    public void setDrawerListener(DrawerListener drawerListener) {
        if (this.mListener != null) {
            this.removeDrawerListener(this.mListener);
        }
        if (drawerListener != null) {
            this.addDrawerListener(drawerListener);
        }
        this.mListener = drawerListener;
    }

    public void setDrawerLockMode(int n2) {
        this.setDrawerLockMode(n2, 3);
        this.setDrawerLockMode(n2, 5);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setDrawerLockMode(int n2, int n3) {
        ViewDragHelper viewDragHelper;
        int n4 = GravityCompat.getAbsoluteGravity(n3, ViewCompat.getLayoutDirection((View)this));
        switch (n3) {
            case 3: {
                this.mLockModeLeft = n2;
                break;
            }
            case 5: {
                this.mLockModeRight = n2;
                break;
            }
            case 0x800003: {
                this.mLockModeStart = n2;
                break;
            }
            case 0x800005: {
                this.mLockModeEnd = n2;
            }
        }
        if (n2 != 0) {
            viewDragHelper = n4 == 3 ? this.mLeftDragger : this.mRightDragger;
            viewDragHelper.cancel();
        }
        switch (n2) {
            case 2: {
                viewDragHelper = this.findDrawerWithGravity(n4);
                if (viewDragHelper == null) return;
                this.openDrawer((View)viewDragHelper);
                return;
            }
            default: {
                return;
            }
            case 1: {
                viewDragHelper = this.findDrawerWithGravity(n4);
                if (viewDragHelper == null) return;
                this.closeDrawer((View)viewDragHelper);
                return;
            }
        }
    }

    public void setDrawerLockMode(int n2, View view) {
        if (!this.isDrawerView(view)) {
            throw new IllegalArgumentException("View " + view + " is not a " + "drawer with appropriate layout_gravity");
        }
        this.setDrawerLockMode(n2, ((LayoutParams)view.getLayoutParams()).gravity);
    }

    public void setDrawerShadow(@DrawableRes int n2, int n3) {
        this.setDrawerShadow(this.getResources().getDrawable(n2), n3);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setDrawerShadow(Drawable drawable2, int n2) {
        block4: {
            block7: {
                block2: {
                    block6: {
                        block5: {
                            block3: {
                                if (SET_DRAWER_SHADOW_FROM_ELEVATION) break block2;
                                if ((n2 & 0x800003) != 0x800003) break block3;
                                this.mShadowStart = drawable2;
                                break block4;
                            }
                            if ((n2 & 0x800005) != 0x800005) break block5;
                            this.mShadowEnd = drawable2;
                            break block4;
                        }
                        if ((n2 & 3) != 3) break block6;
                        this.mShadowLeft = drawable2;
                        break block4;
                    }
                    if ((n2 & 5) == 5) break block7;
                }
                return;
            }
            this.mShadowRight = drawable2;
        }
        this.resolveShadowDrawables();
        this.invalidate();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setDrawerTitle(int n2, CharSequence charSequence) {
        if ((n2 = GravityCompat.getAbsoluteGravity(n2, ViewCompat.getLayoutDirection((View)this))) == 3) {
            this.mTitleLeft = charSequence;
            return;
        } else {
            if (n2 != 5) return;
            this.mTitleRight = charSequence;
            return;
        }
    }

    void setDrawerViewOffset(View view, float f2) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (f2 == layoutParams.onScreen) {
            return;
        }
        layoutParams.onScreen = f2;
        this.dispatchOnDrawerSlide(view, f2);
    }

    public void setScrimColor(@ColorInt int n2) {
        this.mScrimColor = n2;
        this.invalidate();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setStatusBarBackground(int n2) {
        Drawable drawable2 = n2 != 0 ? ContextCompat.getDrawable(this.getContext(), n2) : null;
        this.mStatusBarBackground = drawable2;
        this.invalidate();
    }

    public void setStatusBarBackground(Drawable drawable2) {
        this.mStatusBarBackground = drawable2;
        this.invalidate();
    }

    public void setStatusBarBackgroundColor(@ColorInt int n2) {
        this.mStatusBarBackground = new ColorDrawable(n2);
        this.invalidate();
    }

    /*
     * Enabled aggressive block sorting
     */
    void updateDrawerState(int n2, int n3, View view) {
        n2 = this.mLeftDragger.getViewDragState();
        int n4 = this.mRightDragger.getViewDragState();
        n2 = n2 == 1 || n4 == 1 ? 1 : (n2 == 2 || n4 == 2 ? 2 : 0);
        if (view != null && n3 == 0) {
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (layoutParams.onScreen == 0.0f) {
                this.dispatchOnDrawerClosed(view);
            } else if (layoutParams.onScreen == 1.0f) {
                this.dispatchOnDrawerOpened(view);
            }
        }
        if (n2 != this.mDrawerState) {
            this.mDrawerState = n2;
            if (this.mListeners != null) {
                for (n3 = this.mListeners.size() - 1; n3 >= 0; --n3) {
                    this.mListeners.get(n3).onDrawerStateChanged(n2);
                }
            }
        }
    }

    class AccessibilityDelegate
    extends AccessibilityDelegateCompat {
        private final Rect mTmpRect = new Rect();

        AccessibilityDelegate() {
        }

        private void addChildrenForAccessibility(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, ViewGroup viewGroup) {
            int n2 = viewGroup.getChildCount();
            for (int i2 = 0; i2 < n2; ++i2) {
                View view = viewGroup.getChildAt(i2);
                if (!DrawerLayout.includeChildForAccessibility(view)) continue;
                accessibilityNodeInfoCompat.addChild(view);
            }
        }

        private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2) {
            Rect rect = this.mTmpRect;
            accessibilityNodeInfoCompat2.getBoundsInParent(rect);
            accessibilityNodeInfoCompat.setBoundsInParent(rect);
            accessibilityNodeInfoCompat2.getBoundsInScreen(rect);
            accessibilityNodeInfoCompat.setBoundsInScreen(rect);
            accessibilityNodeInfoCompat.setVisibleToUser(accessibilityNodeInfoCompat2.isVisibleToUser());
            accessibilityNodeInfoCompat.setPackageName(accessibilityNodeInfoCompat2.getPackageName());
            accessibilityNodeInfoCompat.setClassName(accessibilityNodeInfoCompat2.getClassName());
            accessibilityNodeInfoCompat.setContentDescription(accessibilityNodeInfoCompat2.getContentDescription());
            accessibilityNodeInfoCompat.setEnabled(accessibilityNodeInfoCompat2.isEnabled());
            accessibilityNodeInfoCompat.setClickable(accessibilityNodeInfoCompat2.isClickable());
            accessibilityNodeInfoCompat.setFocusable(accessibilityNodeInfoCompat2.isFocusable());
            accessibilityNodeInfoCompat.setFocused(accessibilityNodeInfoCompat2.isFocused());
            accessibilityNodeInfoCompat.setAccessibilityFocused(accessibilityNodeInfoCompat2.isAccessibilityFocused());
            accessibilityNodeInfoCompat.setSelected(accessibilityNodeInfoCompat2.isSelected());
            accessibilityNodeInfoCompat.setLongClickable(accessibilityNodeInfoCompat2.isLongClickable());
            accessibilityNodeInfoCompat.addAction(accessibilityNodeInfoCompat2.getActions());
        }

        @Override
        public boolean dispatchPopulateAccessibilityEvent(View object, AccessibilityEvent object2) {
            if (object2.getEventType() == 32) {
                int n2;
                object = object2.getText();
                object2 = DrawerLayout.this.findVisibleDrawer();
                if (object2 != null && (object2 = DrawerLayout.this.getDrawerTitle(n2 = DrawerLayout.this.getDrawerViewAbsoluteGravity((View)object2))) != null) {
                    object.add(object2);
                }
                return true;
            }
            return super.dispatchPopulateAccessibilityEvent((View)object, (AccessibilityEvent)object2);
        }

        @Override
        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)DrawerLayout.class.getName());
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            if (CAN_HIDE_DESCENDANTS) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            } else {
                AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2 = AccessibilityNodeInfoCompat.obtain(accessibilityNodeInfoCompat);
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat2);
                accessibilityNodeInfoCompat.setSource(view);
                ViewParent viewParent = ViewCompat.getParentForAccessibility(view);
                if (viewParent instanceof View) {
                    accessibilityNodeInfoCompat.setParent((View)viewParent);
                }
                this.copyNodeInfoNoChildren(accessibilityNodeInfoCompat, accessibilityNodeInfoCompat2);
                accessibilityNodeInfoCompat2.recycle();
                this.addChildrenForAccessibility(accessibilityNodeInfoCompat, (ViewGroup)view);
            }
            accessibilityNodeInfoCompat.setClassName(DrawerLayout.class.getName());
            accessibilityNodeInfoCompat.setFocusable(false);
            accessibilityNodeInfoCompat.setFocused(false);
            accessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_FOCUS);
            accessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLEAR_FOCUS);
        }

        @Override
        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            if (CAN_HIDE_DESCENDANTS || DrawerLayout.includeChildForAccessibility(view)) {
                return super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
            }
            return false;
        }
    }

    final class ChildAccessibilityDelegate
    extends AccessibilityDelegateCompat {
        ChildAccessibilityDelegate() {
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            if (!DrawerLayout.includeChildForAccessibility(view)) {
                accessibilityNodeInfoCompat.setParent(null);
            }
        }
    }

    static interface DrawerLayoutCompatImpl {
        public void applyMarginInsets(ViewGroup.MarginLayoutParams var1, Object var2, int var3);

        public void configureApplyInsets(View var1);

        public void dispatchChildInsets(View var1, Object var2, int var3);

        public Drawable getDefaultStatusBarBackground(Context var1);

        public int getTopInset(Object var1);
    }

    static class DrawerLayoutCompatImplApi21
    implements DrawerLayoutCompatImpl {
        DrawerLayoutCompatImplApi21() {
        }

        @Override
        public void applyMarginInsets(ViewGroup.MarginLayoutParams marginLayoutParams, Object object, int n2) {
            DrawerLayoutCompatApi21.applyMarginInsets(marginLayoutParams, object, n2);
        }

        @Override
        public void configureApplyInsets(View view) {
            DrawerLayoutCompatApi21.configureApplyInsets(view);
        }

        @Override
        public void dispatchChildInsets(View view, Object object, int n2) {
            DrawerLayoutCompatApi21.dispatchChildInsets(view, object, n2);
        }

        @Override
        public Drawable getDefaultStatusBarBackground(Context context) {
            return DrawerLayoutCompatApi21.getDefaultStatusBarBackground(context);
        }

        @Override
        public int getTopInset(Object object) {
            return DrawerLayoutCompatApi21.getTopInset(object);
        }
    }

    static class DrawerLayoutCompatImplBase
    implements DrawerLayoutCompatImpl {
        DrawerLayoutCompatImplBase() {
        }

        @Override
        public void applyMarginInsets(ViewGroup.MarginLayoutParams marginLayoutParams, Object object, int n2) {
        }

        @Override
        public void configureApplyInsets(View view) {
        }

        @Override
        public void dispatchChildInsets(View view, Object object, int n2) {
        }

        @Override
        public Drawable getDefaultStatusBarBackground(Context context) {
            return null;
        }

        @Override
        public int getTopInset(Object object) {
            return 0;
        }
    }

    public static interface DrawerListener {
        public void onDrawerClosed(View var1);

        public void onDrawerOpened(View var1);

        public void onDrawerSlide(View var1, float var2);

        public void onDrawerStateChanged(int var1);
    }

    public static class LayoutParams
    extends ViewGroup.MarginLayoutParams {
        private static final int FLAG_IS_CLOSING = 4;
        private static final int FLAG_IS_OPENED = 1;
        private static final int FLAG_IS_OPENING = 2;
        public int gravity = 0;
        boolean isPeeking;
        float onScreen;
        int openState;

        public LayoutParams(int n2, int n3) {
            super(n2, n3);
        }

        public LayoutParams(int n2, int n3, int n4) {
            this(n2, n3);
            this.gravity = n4;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            context = context.obtainStyledAttributes(attributeSet, LAYOUT_ATTRS);
            this.gravity = context.getInt(0, 0);
            context.recycle();
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams)layoutParams);
            this.gravity = layoutParams.gravity;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }
    }

    protected static class SavedState
    extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>(){

            @Override
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState[] newArray(int n2) {
                return new SavedState[n2];
            }
        });
        int lockModeEnd;
        int lockModeLeft;
        int lockModeRight;
        int lockModeStart;
        int openDrawerGravity = 0;

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.openDrawerGravity = parcel.readInt();
            this.lockModeLeft = parcel.readInt();
            this.lockModeRight = parcel.readInt();
            this.lockModeStart = parcel.readInt();
            this.lockModeEnd = parcel.readInt();
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override
        public void writeToParcel(Parcel parcel, int n2) {
            super.writeToParcel(parcel, n2);
            parcel.writeInt(this.openDrawerGravity);
            parcel.writeInt(this.lockModeLeft);
            parcel.writeInt(this.lockModeRight);
            parcel.writeInt(this.lockModeStart);
            parcel.writeInt(this.lockModeEnd);
        }
    }

    public static abstract class SimpleDrawerListener
    implements DrawerListener {
        @Override
        public void onDrawerClosed(View view) {
        }

        @Override
        public void onDrawerOpened(View view) {
        }

        @Override
        public void onDrawerSlide(View view, float f2) {
        }

        @Override
        public void onDrawerStateChanged(int n2) {
        }
    }

    private class ViewDragCallback
    extends ViewDragHelper.Callback {
        private final int mAbsGravity;
        private ViewDragHelper mDragger;
        private final Runnable mPeekRunnable = new Runnable(){

            @Override
            public void run() {
                ViewDragCallback.this.peekDrawer();
            }
        };

        ViewDragCallback(int n2) {
            this.mAbsGravity = n2;
        }

        private void closeOtherDrawer() {
            View view;
            int n2 = 3;
            if (this.mAbsGravity == 3) {
                n2 = 5;
            }
            if ((view = DrawerLayout.this.findDrawerWithGravity(n2)) != null) {
                DrawerLayout.this.closeDrawer(view);
            }
        }

        @Override
        public int clampViewPositionHorizontal(View view, int n2, int n3) {
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(view, 3)) {
                return Math.max(-view.getWidth(), Math.min(n2, 0));
            }
            n3 = DrawerLayout.this.getWidth();
            return Math.max(n3 - view.getWidth(), Math.min(n2, n3));
        }

        @Override
        public int clampViewPositionVertical(View view, int n2, int n3) {
            return view.getTop();
        }

        @Override
        public int getViewHorizontalDragRange(View view) {
            if (DrawerLayout.this.isDrawerView(view)) {
                return view.getWidth();
            }
            return 0;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onEdgeDragStarted(int n2, int n3) {
            View view = (n2 & 1) == 1 ? DrawerLayout.this.findDrawerWithGravity(3) : DrawerLayout.this.findDrawerWithGravity(5);
            if (view != null && DrawerLayout.this.getDrawerLockMode(view) == 0) {
                this.mDragger.captureChildView(view, n3);
            }
        }

        @Override
        public boolean onEdgeLock(int n2) {
            return false;
        }

        @Override
        public void onEdgeTouched(int n2, int n3) {
            DrawerLayout.this.postDelayed(this.mPeekRunnable, 160L);
        }

        @Override
        public void onViewCaptured(View view, int n2) {
            ((LayoutParams)view.getLayoutParams()).isPeeking = false;
            this.closeOtherDrawer();
        }

        @Override
        public void onViewDragStateChanged(int n2) {
            DrawerLayout.this.updateDrawerState(this.mAbsGravity, n2, this.mDragger.getCapturedView());
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onViewPositionChanged(View view, int n2, int n3, int n4, int n5) {
            n3 = view.getWidth();
            float f2 = DrawerLayout.this.checkDrawerViewAbsoluteGravity(view, 3) ? (float)(n3 + n2) / (float)n3 : (float)(DrawerLayout.this.getWidth() - n2) / (float)n3;
            DrawerLayout.this.setDrawerViewOffset(view, f2);
            n2 = f2 == 0.0f ? 4 : 0;
            view.setVisibility(n2);
            DrawerLayout.this.invalidate();
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onViewReleased(View view, float f2, float f3) {
            int n2;
            f3 = DrawerLayout.this.getDrawerViewOffset(view);
            int n3 = view.getWidth();
            if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(view, 3)) {
                n2 = f2 > 0.0f || f2 == 0.0f && f3 > 0.5f ? 0 : -n3;
            } else {
                n2 = DrawerLayout.this.getWidth();
                if (f2 < 0.0f || f2 == 0.0f && f3 > 0.5f) {
                    n2 -= n3;
                }
            }
            this.mDragger.settleCapturedViewAt(n2, view.getTop());
            DrawerLayout.this.invalidate();
        }

        /*
         * Enabled aggressive block sorting
         */
        void peekDrawer() {
            View view;
            int n2 = 0;
            int n3 = this.mDragger.getEdgeSize();
            boolean bl2 = this.mAbsGravity == 3;
            if (bl2) {
                view = DrawerLayout.this.findDrawerWithGravity(3);
                if (view != null) {
                    n2 = -view.getWidth();
                }
                n2 += n3;
            } else {
                view = DrawerLayout.this.findDrawerWithGravity(5);
                n2 = DrawerLayout.this.getWidth() - n3;
            }
            if (view != null && (bl2 && view.getLeft() < n2 || !bl2 && view.getLeft() > n2) && DrawerLayout.this.getDrawerLockMode(view) == 0) {
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                this.mDragger.smoothSlideViewTo(view, n2, view.getTop());
                layoutParams.isPeeking = true;
                DrawerLayout.this.invalidate();
                this.closeOtherDrawer();
                DrawerLayout.this.cancelChildViewTouch();
            }
        }

        public void removeCallbacks() {
            DrawerLayout.this.removeCallbacks(this.mPeekRunnable);
        }

        public void setDragger(ViewDragHelper viewDragHelper) {
            this.mDragger = viewDragHelper;
        }

        @Override
        public boolean tryCaptureView(View view, int n2) {
            return DrawerLayout.this.isDrawerView(view) && DrawerLayout.this.checkDrawerViewAbsoluteGravity(view, this.mAbsGravity) && DrawerLayout.this.getDrawerLockMode(view) == 0;
        }
    }
}

