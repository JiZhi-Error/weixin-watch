/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.os.AsyncTask
 *  android.os.Build$VERSION
 */
package android.support.v4.os;

import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.os.AsyncTaskCompatHoneycomb;

public final class AsyncTaskCompat {
    private AsyncTaskCompat() {
    }

    public static <Params, Progress, Result> AsyncTask<Params, Progress, Result> executeParallel(AsyncTask<Params, Progress, Result> asyncTask, Params ... ParamsArray) {
        if (asyncTask == null) {
            throw new IllegalArgumentException("task can not be null");
        }
        if (Build.VERSION.SDK_INT >= 11) {
            AsyncTaskCompatHoneycomb.executeParallel(asyncTask, ParamsArray);
            return asyncTask;
        }
        asyncTask.execute((Object[])ParamsArray);
        return asyncTask;
    }
}

