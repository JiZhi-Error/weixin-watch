/*
 * Decompiled with CFR 0.151.
 */
package com.google.android.gms.common.api;

import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.ResultTransform;

public abstract class TransformedResult<R extends Result> {
    public abstract void andFinally(@NonNull ResultCallbacks<? super R> var1);

    @NonNull
    public abstract <S extends Result> TransformedResult<S> then(@NonNull ResultTransform<? super R, ? extends S> var1);
}

