/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.ViewOutlineProvider
 */
package android.support.design.widget;

import android.view.View;
import android.view.ViewOutlineProvider;

class ViewUtilsLollipop {
    ViewUtilsLollipop() {
    }

    static void setBoundsViewOutlineProvider(View view) {
        view.setOutlineProvider(ViewOutlineProvider.BOUNDS);
    }
}

