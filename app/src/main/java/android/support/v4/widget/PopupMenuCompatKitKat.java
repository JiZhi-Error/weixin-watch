/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.view.View$OnTouchListener
 *  android.widget.PopupMenu
 */
package android.support.v4.widget;

import android.view.View;
import android.widget.PopupMenu;

class PopupMenuCompatKitKat {
    PopupMenuCompatKitKat() {
    }

    public static View.OnTouchListener getDragToOpenListener(Object object) {
        return ((PopupMenu)object).getDragToOpenListener();
    }
}

