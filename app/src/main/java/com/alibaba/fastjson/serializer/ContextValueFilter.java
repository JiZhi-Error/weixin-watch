/*
 * Decompiled with CFR 0.151.
 */
package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.SerializeFilter;

public interface ContextValueFilter
extends SerializeFilter {
    public Object process(BeanContext var1, Object var2, String var3, Object var4);
}

