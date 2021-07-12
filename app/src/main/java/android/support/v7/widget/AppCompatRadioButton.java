/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.widget.CompoundButton
 *  android.widget.RadioButton
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TintableCompoundButton;
import android.support.v7.appcompat.R;
import android.support.v7.widget.AppCompatCompoundButtonHelper;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.TintContextWrapper;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.RadioButton;

public class AppCompatRadioButton
extends RadioButton
implements TintableCompoundButton {
    private AppCompatCompoundButtonHelper mCompoundButtonHelper;
    private AppCompatDrawableManager mDrawableManager = AppCompatDrawableManager.get();

    public AppCompatRadioButton(Context context) {
        this(context, null);
    }

    public AppCompatRadioButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.radioButtonStyle);
    }

    public AppCompatRadioButton(Context context, AttributeSet attributeSet, int n2) {
        super(TintContextWrapper.wrap(context), attributeSet, n2);
        this.mCompoundButtonHelper = new AppCompatCompoundButtonHelper((CompoundButton)this, this.mDrawableManager);
        this.mCompoundButtonHelper.loadFromAttributes(attributeSet, n2);
    }

    public int getCompoundPaddingLeft() {
        int n2;
        int n3 = n2 = super.getCompoundPaddingLeft();
        if (this.mCompoundButtonHelper != null) {
            n3 = this.mCompoundButtonHelper.getCompoundPaddingLeft(n2);
        }
        return n3;
    }

    @Override
    @Nullable
    public ColorStateList getSupportButtonTintList() {
        if (this.mCompoundButtonHelper != null) {
            return this.mCompoundButtonHelper.getSupportButtonTintList();
        }
        return null;
    }

    @Override
    @Nullable
    public PorterDuff.Mode getSupportButtonTintMode() {
        if (this.mCompoundButtonHelper != null) {
            return this.mCompoundButtonHelper.getSupportButtonTintMode();
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void setButtonDrawable(@DrawableRes int n2) {
        Drawable drawable2 = this.mDrawableManager != null ? this.mDrawableManager.getDrawable(this.getContext(), n2) : ContextCompat.getDrawable(this.getContext(), n2);
        this.setButtonDrawable(drawable2);
    }

    public void setButtonDrawable(Drawable drawable2) {
        super.setButtonDrawable(drawable2);
        if (this.mCompoundButtonHelper != null) {
            this.mCompoundButtonHelper.onSetButtonDrawable();
        }
    }

    @Override
    public void setSupportButtonTintList(@Nullable ColorStateList colorStateList) {
        if (this.mCompoundButtonHelper != null) {
            this.mCompoundButtonHelper.setSupportButtonTintList(colorStateList);
        }
    }

    @Override
    public void setSupportButtonTintMode(@Nullable PorterDuff.Mode mode) {
        if (this.mCompoundButtonHelper != null) {
            this.mCompoundButtonHelper.setSupportButtonTintMode(mode);
        }
    }
}

