/*
 * Decompiled with CFR 0.151.
 */
package com.alibaba.fastjson;

class JSONStreamContext {
    static final int ArrayValue = 1005;
    static final int PropertyKey = 1002;
    static final int PropertyValue = 1003;
    static final int StartArray = 1004;
    static final int StartObject = 1001;
    protected final JSONStreamContext parent;
    protected int state;

    public JSONStreamContext(JSONStreamContext jSONStreamContext, int n2) {
        this.parent = jSONStreamContext;
        this.state = n2;
    }
}

