/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package com.google.android.gms.common.data;

import android.os.Bundle;
import com.google.android.gms.common.api.Releasable;
import java.util.Iterator;

public interface DataBuffer<T>
extends Releasable,
Iterable<T> {
    @Deprecated
    public void close();

    public T get(int var1);

    public int getCount();

    @Deprecated
    public boolean isClosed();

    @Override
    public Iterator<T> iterator();

    @Override
    public void release();

    public Iterator<T> singleRefIterator();

    public Bundle zzpZ();
}

