/*
 * Decompiled with CFR 0.151.
 */
package com.alibaba.fastjson.asm;

import com.alibaba.fastjson.asm.Label;

public interface MethodVisitor {
    public void visitEnd();

    public void visitFieldInsn(int var1, String var2, String var3, String var4);

    public void visitIincInsn(int var1, int var2);

    public void visitInsn(int var1);

    public void visitIntInsn(int var1, int var2);

    public void visitJumpInsn(int var1, Label var2);

    public void visitLabel(Label var1);

    public void visitLdcInsn(Object var1);

    public void visitMaxs(int var1, int var2);

    public void visitMethodInsn(int var1, String var2, String var3, String var4);

    public void visitTypeInsn(int var1, String var2);

    public void visitVarInsn(int var1, int var2);
}

