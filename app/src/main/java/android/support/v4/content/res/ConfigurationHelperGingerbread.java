/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.content.res.Resources
 */
package android.support.v4.content.res;

import android.content.res.Resources;
import android.support.annotation.NonNull;

class ConfigurationHelperGingerbread {
    ConfigurationHelperGingerbread() {
    }

    static int getDensityDpi(@NonNull Resources resources) {
        return resources.getDisplayMetrics().densityDpi;
    }

    static int getScreenHeightDp(@NonNull Resources resources) {
        resources = resources.getDisplayMetrics();
        return (int)((float)resources.heightPixels / resources.density);
    }

    static int getScreenWidthDp(@NonNull Resources resources) {
        resources = resources.getDisplayMetrics();
        return (int)((float)resources.widthPixels / resources.density);
    }

    static int getSmallestScreenWidthDp(@NonNull Resources resources) {
        return Math.min(ConfigurationHelperGingerbread.getScreenWidthDp(resources), ConfigurationHelperGingerbread.getScreenHeightDp(resources));
    }
}

