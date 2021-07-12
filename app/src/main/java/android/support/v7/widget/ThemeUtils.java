/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.Color
 *  android.util.TypedValue
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;
import android.util.TypedValue;

class ThemeUtils {
    static final int[] ACTIVATED_STATE_SET;
    static final int[] CHECKED_STATE_SET;
    static final int[] DISABLED_STATE_SET;
    static final int[] EMPTY_STATE_SET;
    static final int[] FOCUSED_STATE_SET;
    static final int[] NOT_PRESSED_OR_FOCUSED_STATE_SET;
    static final int[] PRESSED_STATE_SET;
    static final int[] SELECTED_STATE_SET;
    private static final int[] TEMP_ARRAY;
    private static final ThreadLocal<TypedValue> TL_TYPED_VALUE;

    static {
        TL_TYPED_VALUE = new ThreadLocal();
        DISABLED_STATE_SET = new int[]{-16842910};
        FOCUSED_STATE_SET = new int[]{16842908};
        ACTIVATED_STATE_SET = new int[]{16843518};
        PRESSED_STATE_SET = new int[]{16842919};
        CHECKED_STATE_SET = new int[]{0x10100A0};
        SELECTED_STATE_SET = new int[]{0x10100A1};
        NOT_PRESSED_OR_FOCUSED_STATE_SET = new int[]{-16842919, -16842908};
        EMPTY_STATE_SET = new int[0];
        TEMP_ARRAY = new int[1];
    }

    ThemeUtils() {
    }

    public static ColorStateList createDisabledStateList(int n2, int n3) {
        int[][] nArrayArray = new int[2][];
        int[] nArray = new int[2];
        nArrayArray[0] = DISABLED_STATE_SET;
        nArray[0] = n3;
        n3 = 0 + 1;
        nArrayArray[n3] = EMPTY_STATE_SET;
        nArray[n3] = n2;
        return new ColorStateList((int[][])nArrayArray, nArray);
    }

    public static int getDisabledThemeAttrColor(Context context, int n2) {
        ColorStateList colorStateList = ThemeUtils.getThemeAttrColorStateList(context, n2);
        if (colorStateList != null && colorStateList.isStateful()) {
            return colorStateList.getColorForState(DISABLED_STATE_SET, colorStateList.getDefaultColor());
        }
        colorStateList = ThemeUtils.getTypedValue();
        context.getTheme().resolveAttribute(0x1010033, (TypedValue)colorStateList, true);
        return ThemeUtils.getThemeAttrColor(context, n2, colorStateList.getFloat());
    }

    public static int getThemeAttrColor(Context context, int n2) {
        ThemeUtils.TEMP_ARRAY[0] = n2;
        context = context.obtainStyledAttributes(null, TEMP_ARRAY);
        try {
            n2 = context.getColor(0, 0);
            return n2;
        }
        finally {
            context.recycle();
        }
    }

    static int getThemeAttrColor(Context context, int n2, float f2) {
        n2 = ThemeUtils.getThemeAttrColor(context, n2);
        return ColorUtils.setAlphaComponent(n2, Math.round((float)Color.alpha((int)n2) * f2));
    }

    public static ColorStateList getThemeAttrColorStateList(Context context, int n2) {
        ThemeUtils.TEMP_ARRAY[0] = n2;
        context = context.obtainStyledAttributes(null, TEMP_ARRAY);
        try {
            ColorStateList colorStateList = context.getColorStateList(0);
            return colorStateList;
        }
        finally {
            context.recycle();
        }
    }

    private static TypedValue getTypedValue() {
        TypedValue typedValue;
        TypedValue typedValue2 = typedValue = TL_TYPED_VALUE.get();
        if (typedValue == null) {
            typedValue2 = new TypedValue();
            TL_TYPED_VALUE.set(typedValue2);
        }
        return typedValue2;
    }
}

