/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 */
package android.support.v4.view;

import android.os.Build;
import android.support.v4.view.ScaleGestureDetectorCompatKitKat;

public final class ScaleGestureDetectorCompat {
    static final ScaleGestureDetectorImpl IMPL = Build.VERSION.SDK_INT >= 19 ? new ScaleGestureDetectorCompatKitKatImpl() : new BaseScaleGestureDetectorImpl();

    private ScaleGestureDetectorCompat() {
    }

    public static boolean isQuickScaleEnabled(Object object) {
        return IMPL.isQuickScaleEnabled(object);
    }

    public static void setQuickScaleEnabled(Object object, boolean bl2) {
        IMPL.setQuickScaleEnabled(object, bl2);
    }

    private static class BaseScaleGestureDetectorImpl
    implements ScaleGestureDetectorImpl {
        BaseScaleGestureDetectorImpl() {
        }

        @Override
        public boolean isQuickScaleEnabled(Object object) {
            return false;
        }

        @Override
        public void setQuickScaleEnabled(Object object, boolean bl2) {
        }
    }

    private static class ScaleGestureDetectorCompatKitKatImpl
    implements ScaleGestureDetectorImpl {
        ScaleGestureDetectorCompatKitKatImpl() {
        }

        @Override
        public boolean isQuickScaleEnabled(Object object) {
            return ScaleGestureDetectorCompatKitKat.isQuickScaleEnabled(object);
        }

        @Override
        public void setQuickScaleEnabled(Object object, boolean bl2) {
            ScaleGestureDetectorCompatKitKat.setQuickScaleEnabled(object, bl2);
        }
    }

    static interface ScaleGestureDetectorImpl {
        public boolean isQuickScaleEnabled(Object var1);

        public void setQuickScaleEnabled(Object var1, boolean var2);
    }
}

