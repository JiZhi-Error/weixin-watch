/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.view.accessibility.AccessibilityNodeInfo$AccessibilityAction
 *  android.view.accessibility.AccessibilityNodeInfo$CollectionInfo
 *  android.view.accessibility.AccessibilityNodeInfo$CollectionItemInfo
 */
package android.support.v4.view.accessibility;

import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import java.util.List;

class AccessibilityNodeInfoCompatApi21 {
    AccessibilityNodeInfoCompatApi21() {
    }

    static void addAction(Object object, Object object2) {
        ((AccessibilityNodeInfo)object).addAction((AccessibilityNodeInfo.AccessibilityAction)object2);
    }

    static int getAccessibilityActionId(Object object) {
        return ((AccessibilityNodeInfo.AccessibilityAction)object).getId();
    }

    static CharSequence getAccessibilityActionLabel(Object object) {
        return ((AccessibilityNodeInfo.AccessibilityAction)object).getLabel();
    }

    static List<Object> getActionList(Object object) {
        return ((AccessibilityNodeInfo)object).getActionList();
    }

    public static CharSequence getError(Object object) {
        return ((AccessibilityNodeInfo)object).getError();
    }

    public static int getMaxTextLength(Object object) {
        return ((AccessibilityNodeInfo)object).getMaxTextLength();
    }

    public static Object getWindow(Object object) {
        return ((AccessibilityNodeInfo)object).getWindow();
    }

    static Object newAccessibilityAction(int n2, CharSequence charSequence) {
        return new AccessibilityNodeInfo.AccessibilityAction(n2, charSequence);
    }

    public static Object obtainCollectionInfo(int n2, int n3, boolean bl2, int n4) {
        return AccessibilityNodeInfo.CollectionInfo.obtain((int)n2, (int)n3, (boolean)bl2, (int)n4);
    }

    public static Object obtainCollectionItemInfo(int n2, int n3, int n4, int n5, boolean bl2, boolean bl3) {
        return AccessibilityNodeInfo.CollectionItemInfo.obtain((int)n2, (int)n3, (int)n4, (int)n5, (boolean)bl2, (boolean)bl3);
    }

    public static boolean removeAction(Object object, Object object2) {
        return ((AccessibilityNodeInfo)object).removeAction((AccessibilityNodeInfo.AccessibilityAction)object2);
    }

    public static boolean removeChild(Object object, View view) {
        return ((AccessibilityNodeInfo)object).removeChild(view);
    }

    public static boolean removeChild(Object object, View view, int n2) {
        return ((AccessibilityNodeInfo)object).removeChild(view, n2);
    }

    public static void setError(Object object, CharSequence charSequence) {
        ((AccessibilityNodeInfo)object).setError(charSequence);
    }

    public static void setMaxTextLength(Object object, int n2) {
        ((AccessibilityNodeInfo)object).setMaxTextLength(n2);
    }

    static class CollectionInfo {
        CollectionInfo() {
        }

        public static int getSelectionMode(Object object) {
            return ((AccessibilityNodeInfo.CollectionInfo)object).getSelectionMode();
        }
    }

    static class CollectionItemInfo {
        CollectionItemInfo() {
        }

        public static boolean isSelected(Object object) {
            return ((AccessibilityNodeInfo.CollectionItemInfo)object).isSelected();
        }
    }
}

