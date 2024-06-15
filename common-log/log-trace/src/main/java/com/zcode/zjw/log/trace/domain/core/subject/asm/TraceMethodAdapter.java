package com.zcode.zjw.log.trace.domain.core.subject.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;


public class TraceMethodAdapter extends MethodVisitor {

    private List<String> localVariableNames;

    public TraceMethodAdapter(int i, MethodVisitor methodVisitor, List<String> localVariableNames) {
        super(i, methodVisitor);
        this.localVariableNames = localVariableNames;
    }

    @Override
    public void visitCode() {
        // 获取该方法的所有局部变量名称
        for (int i = 0; i < localVariableNames.size(); i++) {
            // 添加自己的逻辑代码
            super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

            //super.visitLdcInsn("var" + (i + 1));
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
        // 调用父类的实现
        super.visitCode();
    }
}