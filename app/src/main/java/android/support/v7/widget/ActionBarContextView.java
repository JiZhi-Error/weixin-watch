/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 *  android.text.TextUtils
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.accessibility.AccessibilityEvent
 *  android.widget.LinearLayout
 *  android.widget.TextView
 */
package android.support.v7.widget;

import android.content.Context;
import android.os.Build;
import android.support.v7.appcompat.R;
import android.support.v7.view.ActionMode;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.AbsActionBarView;
import android.support.v7.widget.ActionMenuPresenter;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.ViewUtils;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActionBarContextView
extends AbsActionBarView {
    private static final String TAG = "ActionBarContextView";
    private View mClose;
    private int mCloseItemLayout;
    private View mCustomView;
    private CharSequence mSubtitle;
    private int mSubtitleStyleRes;
    private TextView mSubtitleView;
    private CharSequence mTitle;
    private LinearLayout mTitleLayout;
    private boolean mTitleOptional;
    private int mTitleStyleRes;
    private TextView mTitleView;

    public ActionBarContextView(Context context) {
        this(context, null);
    }

    public ActionBarContextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.actionModeStyle);
    }

    public ActionBarContextView(Context object, AttributeSet attributeSet, int n2) {
        super((Context)object, attributeSet, n2);
        object = TintTypedArray.obtainStyledAttributes((Context)object, attributeSet, R.styleable.ActionMode, n2, 0);
        this.setBackgroundDrawable(((TintTypedArray)object).getDrawable(R.styleable.ActionMode_background));
        this.mTitleStyleRes = ((TintTypedArray)object).getResourceId(R.styleable.ActionMode_titleTextStyle, 0);
        this.mSubtitleStyleRes = ((TintTypedArray)object).getResourceId(R.styleable.ActionMode_subtitleTextStyle, 0);
        this.mContentHeight = ((TintTypedArray)object).getLayoutDimension(R.styleable.ActionMode_height, 0);
        this.mCloseItemLayout = ((TintTypedArray)object).getResourceId(R.styleable.ActionMode_closeItemLayout, R.layout.abc_action_mode_close_item_material);
        ((TintTypedArray)object).recycle();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void initTitle() {
        TextView textView;
        int n2;
        block8: {
            block7: {
                int n3 = 8;
                if (this.mTitleLayout == null) {
                    LayoutInflater.from((Context)this.getContext()).inflate(R.layout.abc_action_bar_title_item, (ViewGroup)this);
                    this.mTitleLayout = (LinearLayout)this.getChildAt(this.getChildCount() - 1);
                    this.mTitleView = (TextView)this.mTitleLayout.findViewById(R.id.action_bar_title);
                    this.mSubtitleView = (TextView)this.mTitleLayout.findViewById(R.id.action_bar_subtitle);
                    if (this.mTitleStyleRes != 0) {
                        this.mTitleView.setTextAppearance(this.getContext(), this.mTitleStyleRes);
                    }
                    if (this.mSubtitleStyleRes != 0) {
                        this.mSubtitleView.setTextAppearance(this.getContext(), this.mSubtitleStyleRes);
                    }
                }
                this.mTitleView.setText(this.mTitle);
                this.mSubtitleView.setText(this.mSubtitle);
                n2 = !TextUtils.isEmpty((CharSequence)this.mTitle) ? 1 : 0;
                boolean bl2 = !TextUtils.isEmpty((CharSequence)this.mSubtitle);
                textView = this.mSubtitleView;
                int n4 = bl2 ? 0 : 8;
                textView.setVisibility(n4);
                textView = this.mTitleLayout;
                if (n2 != 0) break block7;
                n2 = n3;
                if (!bl2) break block8;
            }
            n2 = 0;
        }
        textView.setVisibility(n2);
        if (this.mTitleLayout.getParent() == null) {
            this.addView((View)this.mTitleLayout);
        }
    }

    public void closeMode() {
        if (this.mClose == null) {
            this.killMode();
        }
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.MarginLayoutParams(-1, -2);
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new ViewGroup.MarginLayoutParams(this.getContext(), attributeSet);
    }

    public CharSequence getSubtitle() {
        return this.mSubtitle;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    @Override
    public boolean hideOverflowMenu() {
        if (this.mActionMenuPresenter != null) {
            return this.mActionMenuPresenter.hideOverflowMenu();
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void initForMode(ActionMode object) {
        if (this.mClose == null) {
            this.mClose = LayoutInflater.from((Context)this.getContext()).inflate(this.mCloseItemLayout, (ViewGroup)this, false);
            this.addView(this.mClose);
        } else if (this.mClose.getParent() == null) {
            this.addView(this.mClose);
        }
        this.mClose.findViewById(R.id.action_mode_close_button).setOnClickListener(new View.OnClickListener((ActionMode)object){
            final /* synthetic */ ActionMode val$mode;
            {
                this.val$mode = actionMode;
            }

            public void onClick(View view) {
                this.val$mode.finish();
            }
        });
        object = (MenuBuilder)((ActionMode)object).getMenu();
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.dismissPopupMenus();
        }
        this.mActionMenuPresenter = new ActionMenuPresenter(this.getContext());
        this.mActionMenuPresenter.setReserveOverflow(true);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -1);
        ((MenuBuilder)object).addMenuPresenter(this.mActionMenuPresenter, this.mPopupContext);
        this.mMenuView = (ActionMenuView)this.mActionMenuPresenter.getMenuView(this);
        this.mMenuView.setBackgroundDrawable(null);
        this.addView((View)this.mMenuView, layoutParams);
    }

    @Override
    public boolean isOverflowMenuShowing() {
        if (this.mActionMenuPresenter != null) {
            return this.mActionMenuPresenter.isOverflowMenuShowing();
        }
        return false;
    }

    public boolean isTitleOptional() {
        return this.mTitleOptional;
    }

    public void killMode() {
        this.removeAllViews();
        this.mCustomView = null;
        this.mMenuView = null;
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.hideOverflowMenu();
            this.mActionMenuPresenter.hideSubMenus();
        }
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        block3: {
            block2: {
                if (Build.VERSION.SDK_INT < 14) break block2;
                if (accessibilityEvent.getEventType() != 32) break block3;
                accessibilityEvent.setSource((View)this);
                accessibilityEvent.setClassName((CharSequence)((Object)((Object)this)).getClass().getName());
                accessibilityEvent.setPackageName((CharSequence)this.getContext().getPackageName());
                accessibilityEvent.setContentDescription(this.mTitle);
            }
            return;
        }
        super.onInitializeAccessibilityEvent(accessibilityEvent);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onLayout(boolean bl2, int n2, int n3, int n4, int n5) {
        bl2 = ViewUtils.isLayoutRtl((View)this);
        int n6 = bl2 ? n4 - n2 - this.getPaddingRight() : this.getPaddingLeft();
        int n7 = this.getPaddingTop();
        int n8 = n5 - n3 - this.getPaddingTop() - this.getPaddingBottom();
        n3 = n6;
        if (this.mClose != null) {
            n3 = n6;
            if (this.mClose.getVisibility() != 8) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)this.mClose.getLayoutParams();
                n3 = bl2 ? marginLayoutParams.rightMargin : marginLayoutParams.leftMargin;
                n5 = bl2 ? marginLayoutParams.leftMargin : marginLayoutParams.rightMargin;
                n3 = ActionBarContextView.next(n6, n3, bl2);
                n3 = ActionBarContextView.next(n3 + this.positionChild(this.mClose, n3, n7, n8, bl2), n5, bl2);
            }
        }
        n5 = n3;
        if (this.mTitleLayout != null) {
            n5 = n3;
            if (this.mCustomView == null) {
                n5 = n3;
                if (this.mTitleLayout.getVisibility() != 8) {
                    n5 = n3 + this.positionChild((View)this.mTitleLayout, n3, n7, n8, bl2);
                }
            }
        }
        if (this.mCustomView != null) {
            this.positionChild(this.mCustomView, n5, n7, n8, bl2);
        }
        n2 = bl2 ? this.getPaddingLeft() : n4 - n2 - this.getPaddingRight();
        if (this.mMenuView != null) {
            ActionMenuView actionMenuView = this.mMenuView;
            bl2 = !bl2;
            this.positionChild((View)actionMenuView, n2, n7, n8, bl2);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void onMeasure(int n2, int n3) {
        ViewGroup.MarginLayoutParams marginLayoutParams;
        if (View.MeasureSpec.getMode((int)n2) != 0x40000000) {
            throw new IllegalStateException(((Object)((Object)this)).getClass().getSimpleName() + " can only be used " + "with android:layout_width=\"match_parent\" (or fill_parent)");
        }
        if (View.MeasureSpec.getMode((int)n3) == 0) {
            throw new IllegalStateException(((Object)((Object)this)).getClass().getSimpleName() + " can only be used " + "with android:layout_height=\"wrap_content\"");
        }
        int n4 = View.MeasureSpec.getSize((int)n2);
        int n5 = this.mContentHeight > 0 ? this.mContentHeight : View.MeasureSpec.getSize((int)n3);
        int n6 = this.getPaddingTop() + this.getPaddingBottom();
        n2 = n4 - this.getPaddingLeft() - this.getPaddingRight();
        int n7 = n5 - n6;
        int n8 = View.MeasureSpec.makeMeasureSpec((int)n7, (int)Integer.MIN_VALUE);
        n3 = n2;
        if (this.mClose != null) {
            n2 = this.measureChildView(this.mClose, n2, n8, 0);
            marginLayoutParams = (ViewGroup.MarginLayoutParams)this.mClose.getLayoutParams();
            n3 = n2 - (marginLayoutParams.leftMargin + marginLayoutParams.rightMargin);
        }
        n2 = n3;
        if (this.mMenuView != null) {
            n2 = n3;
            if (this.mMenuView.getParent() == this) {
                n2 = this.measureChildView((View)this.mMenuView, n3, n8, 0);
            }
        }
        n3 = n2;
        if (this.mTitleLayout != null) {
            n3 = n2;
            if (this.mCustomView == null) {
                if (this.mTitleOptional) {
                    n3 = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
                    this.mTitleLayout.measure(n3, n8);
                    int n9 = this.mTitleLayout.getMeasuredWidth();
                    n8 = n9 <= n2 ? 1 : 0;
                    n3 = n2;
                    if (n8 != 0) {
                        n3 = n2 - n9;
                    }
                    marginLayoutParams = this.mTitleLayout;
                    n2 = n8 != 0 ? 0 : 8;
                    marginLayoutParams.setVisibility(n2);
                } else {
                    n3 = this.measureChildView((View)this.mTitleLayout, n2, n8, 0);
                }
            }
        }
        if (this.mCustomView != null) {
            marginLayoutParams = this.mCustomView.getLayoutParams();
            n2 = marginLayoutParams.width != -2 ? 0x40000000 : Integer.MIN_VALUE;
            if (marginLayoutParams.width >= 0) {
                n3 = Math.min(marginLayoutParams.width, n3);
            }
            n8 = marginLayoutParams.height != -2 ? 0x40000000 : Integer.MIN_VALUE;
            if (marginLayoutParams.height >= 0) {
                n7 = Math.min(marginLayoutParams.height, n7);
            }
            this.mCustomView.measure(View.MeasureSpec.makeMeasureSpec((int)n3, (int)n2), View.MeasureSpec.makeMeasureSpec((int)n7, (int)n8));
        }
        if (this.mContentHeight > 0) {
            this.setMeasuredDimension(n4, n5);
            return;
        }
        n3 = 0;
        n7 = this.getChildCount();
        n2 = 0;
        while (true) {
            if (n2 >= n7) {
                this.setMeasuredDimension(n4, n3);
                return;
            }
            n8 = this.getChildAt(n2).getMeasuredHeight() + n6;
            n5 = n3;
            if (n8 > n3) {
                n5 = n8;
            }
            ++n2;
            n3 = n5;
        }
    }

    @Override
    public void setContentHeight(int n2) {
        this.mContentHeight = n2;
    }

    public void setCustomView(View view) {
        if (this.mCustomView != null) {
            this.removeView(this.mCustomView);
        }
        this.mCustomView = view;
        if (view != null && this.mTitleLayout != null) {
            this.removeView((View)this.mTitleLayout);
            this.mTitleLayout = null;
        }
        if (view != null) {
            this.addView(view);
        }
        this.requestLayout();
    }

    public void setSubtitle(CharSequence charSequence) {
        this.mSubtitle = charSequence;
        this.initTitle();
    }

    public void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        this.initTitle();
    }

    public void setTitleOptional(boolean bl2) {
        if (bl2 != this.mTitleOptional) {
            this.requestLayout();
        }
        this.mTitleOptional = bl2;
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    public boolean showOverflowMenu() {
        if (this.mActionMenuPresenter != null) {
            return this.mActionMenuPresenter.showOverflowMenu();
        }
        return false;
    }
}

