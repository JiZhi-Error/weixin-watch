/*
 * Decompiled with CFR 0.151.
 */
package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import java.io.IOException;
import java.lang.reflect.Type;

public interface ObjectSerializer {
    public void write(JSONSerializer var1, Object var2, Object var3, Type var4, int var5) throws IOException;
}

