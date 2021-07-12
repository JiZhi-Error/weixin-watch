/*
 * Decompiled with CFR 0.151.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.internal.zzb;
import java.util.concurrent.TimeUnit;

public final class zzr<R extends Result>
extends OptionalPendingResult<R> {
    private final zzb<R> zzaiy;

    public zzr(PendingResult<R> pendingResult) {
        if (!(pendingResult instanceof zzb)) {
            throw new IllegalArgumentException("OptionalPendingResult can only wrap PendingResults generated by an API call.");
        }
        this.zzaiy = (zzb)pendingResult;
    }

    @Override
    public R await() {
        return this.zzaiy.await();
    }

    @Override
    public R await(long l2, TimeUnit timeUnit) {
        return this.zzaiy.await(l2, timeUnit);
    }

    @Override
    public void cancel() {
        this.zzaiy.cancel();
    }

    @Override
    public R get() {
        if (this.isDone()) {
            return this.await(0L, TimeUnit.MILLISECONDS);
        }
        throw new IllegalStateException("Result is not available. Check that isDone() returns true before calling get().");
    }

    @Override
    public boolean isCanceled() {
        return this.zzaiy.isCanceled();
    }

    @Override
    public boolean isDone() {
        return this.zzaiy.isReady();
    }

    @Override
    public void setResultCallback(ResultCallback<? super R> resultCallback) {
        this.zzaiy.setResultCallback(resultCallback);
    }

    @Override
    public void setResultCallback(ResultCallback<? super R> resultCallback, long l2, TimeUnit timeUnit) {
        this.zzaiy.setResultCallback(resultCallback, l2, timeUnit);
    }

    @Override
    public void zza(PendingResult.zza zza2) {
        this.zzaiy.zza(zza2);
    }

    @Override
    public Integer zzpa() {
        return this.zzaiy.zzpa();
    }
}

