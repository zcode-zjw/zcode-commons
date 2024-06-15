package com.zcode.zjw.log.trace.domain.core.subject.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

public class MethodFindInvokeVisitor extends ClassVisitor {
    private final String methodName;
    private final String methodDesc;
    private final String basePackage;

    private final List<AsmMethodPojo> dp;

    public MethodFindInvokeVisitor(int api, ClassVisitor classVisitor, String methodName, String methodDesc, String basePackage, List<AsmMethodPojo> dp) {
        super(api, classVisitor);
        this.methodName = methodName;
        this.methodDesc = methodDesc;
        this.basePackage = basePackage;
        this.dp = dp;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (methodName.equals(name) && methodDesc.equals(descriptor)) {
            return new MethodFindInvokeAdapter(api, null, basePackage, dp);
        }
        return null;
    }

    private static class MethodFindInvokeAdapter extends MethodVisitor {
        private final List<AsmMethodPojo> list;

        private final String basePackage;

        public MethodFindInvokeAdapter(int api, MethodVisitor methodVisitor, String basePackage, List<AsmMethodPojo> list) {
            super(api, methodVisitor);
            this.basePackage = basePackage;
            this.list = list;
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            AsmMethodPojo asmMethodPojo = new AsmMethodPojo(opcode, owner, name, descriptor, isInterface);
            // 首先，处理自己的代码逻辑
            //String info = String.format("%s %s.%s%s", Printer.OPCODES[opcode], owner, name, descriptor);
            if (!name.equals("<init>") && !list.contains(asmMethodPojo) && owner.contains(basePackage)) {
                //list.add(info);
                list.add(asmMethodPojo);
            }

            // 其次，调用父类的方法实现
            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }
}
