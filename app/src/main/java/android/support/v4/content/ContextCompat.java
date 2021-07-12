/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.content.res.ColorStateList
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Environment
 *  android.os.Process
 *  android.util.Log
 *  android.util.TypedValue
 */
package android.support.v4.content;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompatApi21;
import android.support.v4.content.ContextCompatApi23;
import android.support.v4.content.ContextCompatApi24;
import android.support.v4.content.ContextCompatHoneycomb;
import android.support.v4.content.ContextCompatJellybean;
import android.support.v4.content.ContextCompatKitKat;
import android.support.v4.os.BuildCompat;
import android.util.Log;
import android.util.TypedValue;
import java.io.File;

public class ContextCompat {
    private static final String DIR_ANDROID = "Android";
    private static final String DIR_OBB = "obb";
    private static final String TAG = "ContextCompat";
    private static final Object sLock = new Object();
    private static TypedValue sTempValue;

    @Deprecated
    public ContextCompat() {
    }

    /*
     * Enabled aggressive block sorting
     */
    private static File buildPath(File file, String ... stringArray) {
        int n2 = stringArray.length;
        int n3 = 0;
        while (n3 < n2) {
            String string2 = stringArray[n3];
            if (file == null) {
                file = new File(string2);
            } else if (string2 != null) {
                file = new File(file, string2);
            }
            ++n3;
        }
        return file;
    }

    public static int checkSelfPermission(@NonNull Context context, @NonNull String string2) {
        if (string2 == null) {
            throw new IllegalArgumentException("permission is null");
        }
        return context.checkPermission(string2, Process.myPid(), Process.myUid());
    }

    @Deprecated
    public static Context createDeviceEncryptedStorageContext(Context context) {
        return ContextCompat.createDeviceProtectedStorageContext(context);
    }

    public static Context createDeviceProtectedStorageContext(Context context) {
        if (BuildCompat.isAtLeastN()) {
            return ContextCompatApi24.createDeviceProtectedStorageContext(context);
        }
        return null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static File createFilesDir(File file) {
        synchronized (ContextCompat.class) {
            block5: {
                File file2 = file;
                if (file.exists()) return file2;
                file2 = file;
                if (file.mkdirs()) return file2;
                boolean bl2 = file.exists();
                if (!bl2) break block5;
                return file;
            }
            Log.w((String)TAG, (String)("Unable to create files subdir " + file.getPath()));
            return null;
        }
    }

    public static File getCodeCacheDir(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {
            return ContextCompatApi21.getCodeCacheDir(context);
        }
        return ContextCompat.createFilesDir(new File(context.getApplicationInfo().dataDir, "code_cache"));
    }

    @ColorInt
    public static final int getColor(Context context, @ColorRes int n2) {
        if (Build.VERSION.SDK_INT >= 23) {
            return ContextCompatApi23.getColor(context, n2);
        }
        return context.getResources().getColor(n2);
    }

    public static final ColorStateList getColorStateList(Context context, @ColorRes int n2) {
        if (Build.VERSION.SDK_INT >= 23) {
            return ContextCompatApi23.getColorStateList(context, n2);
        }
        return context.getResources().getColorStateList(n2);
    }

    public static File getDataDir(Context object) {
        if (BuildCompat.isAtLeastN()) {
            return ContextCompatApi24.getDataDir(object);
        }
        object = object.getApplicationInfo().dataDir;
        if (object != null) {
            return new File((String)object);
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static final Drawable getDrawable(Context context, @DrawableRes int n2) {
        int n3 = Build.VERSION.SDK_INT;
        if (n3 >= 21) {
            return ContextCompatApi21.getDrawable(context, n2);
        }
        if (n3 >= 16) {
            return context.getResources().getDrawable(n2);
        }
        Object object = sLock;
        synchronized (object) {
            if (sTempValue == null) {
                sTempValue = new TypedValue();
            }
            context.getResources().getValue(n2, sTempValue, true);
            n2 = ContextCompat.sTempValue.resourceId;
            return context.getResources().getDrawable(n2);
        }
    }

    public static File[] getExternalCacheDirs(Context context) {
        if (Build.VERSION.SDK_INT >= 19) {
            return ContextCompatKitKat.getExternalCacheDirs(context);
        }
        return new File[]{context.getExternalCacheDir()};
    }

    public static File[] getExternalFilesDirs(Context context, String string2) {
        if (Build.VERSION.SDK_INT >= 19) {
            return ContextCompatKitKat.getExternalFilesDirs(context, string2);
        }
        return new File[]{context.getExternalFilesDir(string2)};
    }

    public static final File getNoBackupFilesDir(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {
            return ContextCompatApi21.getNoBackupFilesDir(context);
        }
        return ContextCompat.createFilesDir(new File(context.getApplicationInfo().dataDir, "no_backup"));
    }

    /*
     * WARNING - void declaration
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static File[] getObbDirs(Context object) {
        void var0_2;
        int n2 = Build.VERSION.SDK_INT;
        if (n2 >= 19) {
            return ContextCompatKitKat.getObbDirs(object);
        }
        if (n2 >= 11) {
            File file = ContextCompatHoneycomb.getObbDir(object);
            return new File[]{var0_2};
        }
        File file = ContextCompat.buildPath(Environment.getExternalStorageDirectory(), DIR_ANDROID, DIR_OBB, object.getPackageName());
        return new File[]{var0_2};
    }

    @Deprecated
    public static boolean isDeviceEncryptedStorage(Context context) {
        return ContextCompat.isDeviceProtectedStorage(context);
    }

    public static boolean isDeviceProtectedStorage(Context context) {
        if (BuildCompat.isAtLeastN()) {
            return ContextCompatApi24.isDeviceProtectedStorage(context);
        }
        return false;
    }

    public static boolean startActivities(Context context, Intent[] intentArray) {
        return ContextCompat.startActivities(context, intentArray, null);
    }

    public static boolean startActivities(Context context, Intent[] intentArray, Bundle bundle) {
        int n2 = Build.VERSION.SDK_INT;
        if (n2 >= 16) {
            ContextCompatJellybean.startActivities(context, intentArray, bundle);
            return true;
        }
        if (n2 >= 11) {
            ContextCompatHoneycomb.startActivities(context, intentArray);
            return true;
        }
        return false;
    }
}

