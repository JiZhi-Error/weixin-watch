/*
 * Decompiled with CFR 0.151.
 */
package com.mobvoi.android.wearable;

public interface MessageEvent {
    public byte[] getData();

    public String getPath();

    public int getRequestId();

    public String getSourceNodeId();
}

