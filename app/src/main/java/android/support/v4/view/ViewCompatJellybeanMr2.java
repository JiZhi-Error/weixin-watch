/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.view.View
 */
package android.support.v4.view;

import android.graphics.Rect;
import android.view.View;

class ViewCompatJellybeanMr2 {
    ViewCompatJellybeanMr2() {
    }

    public static Rect getClipBounds(View view) {
        return view.getClipBounds();
    }

    public static boolean isInLayout(View view) {
        return view.isInLayout();
    }

    public static void setClipBounds(View view, Rect rect) {
        view.setClipBounds(rect);
    }
}

