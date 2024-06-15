package com.zcode.zjw.log.trace.domain.core.subject.asm;

import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.List;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

@Slf4j
public class MethodParameterVisitor extends ClassVisitor {

    private static List<Map<String, Object>> localVariableList;

    private static String flowId;

    private static String methodName;

    public MethodParameterVisitor(int api, ClassVisitor classVisitor, List<Map<String, Object>> localVariableList,
                                  String flowId, String methodName) {
        super(api, classVisitor);
        MethodParameterVisitor.localVariableList = localVariableList;
        MethodParameterVisitor.flowId = flowId;
        MethodParameterVisitor.methodName = methodName;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (mv != null && !name.equals("<init>") && name.equals(methodName)) {
            boolean isAbstractMethod = (access & ACC_ABSTRACT) != 0;
            boolean isNativeMethod = (access & ACC_NATIVE) != 0;
            if (!isAbstractMethod && !isNativeMethod) {
                mv = new MethodParameterAdapter(api, mv, access, name, descriptor);
            }
        }
        return mv;
    }

    private static class MethodParameterAdapter extends MethodVisitor {
        private final int methodAccess;
        private final String methodName;
        private final String methodDesc;

        public MethodParameterAdapter(int api, MethodVisitor mv, int methodAccess, String methodName, String methodDesc) {
            super(api, mv);
            this.methodAccess = methodAccess;
            this.methodName = methodName;
            this.methodDesc = methodDesc;
        }

        private String findVarNameByVarIndex(int var) {
            for (Map<String, Object> objectMap : localVariableList) {
                if (var == Integer.parseInt(String.valueOf(objectMap.get("var")))) {
                    return String.valueOf(objectMap.get("name"));
                }
            }

            return null;
        }

        private Map<String, Object> findVarObjByVarIndex(int var) {
            for (Map<String, Object> objectMap : localVariableList) {
                if (var == Integer.parseInt(String.valueOf(objectMap.get("index")))) {
                    return objectMap;
                }
            }

            return null;
        }


        @Override
        public void visitVarInsn(int opcode, int var) {
            Map<String, Object> varObj = findVarObjByVarIndex(var);
            if (Opcodes.ASTORE == opcode && null != varObj) {
                super.visitInsn(DUP);
                printValueOnStack("(Ljava/lang/Object;)V", String.valueOf(varObj.get("name")), "local");
            } else if (Opcodes.ISTORE == opcode && null != varObj) {
                String descriptor = String.valueOf(varObj.get("descriptor"));
                if (descriptor.length() == 1) {
                    super.visitInsn(DUP);
                    String methodDesc = String.format("(%s)V", varObj.get("descriptor"));
                    printValueOnStack(methodDesc, String.valueOf(varObj.get("name")), "local");
                }
            }

            super.visitVarInsn(opcode, var);
        }


        @Override
        public void visitCode() {
            // 首先，处理自己的代码逻辑
            boolean isStatic = ((methodAccess & ACC_STATIC) != 0);
            int slotIndex = isStatic ? 0 : 1;

            printMessage("方法开始: " + methodDesc + "." + methodName, "start");

            Type methodType = Type.getMethodType(methodDesc);
            Type[] argumentTypes = methodType.getArgumentTypes();
            for (int i = 0; i < argumentTypes.length; i++) {
                Type t = argumentTypes[i];
                int sort = t.getSort();
                int size = t.getSize();
                String descriptor = t.getDescriptor();
                int opcode = t.getOpcode(ILOAD);
                super.visitVarInsn(opcode, slotIndex);
                if (sort >= Type.BOOLEAN && sort <= Type.DOUBLE) {
                    String methodDesc = String.format("(%s)V", descriptor);
                    printValueOnStack(methodDesc, "参数" + (i + 1), "arg");
                } else {
                    printValueOnStack("(Ljava/lang/Object;)V", "参数" + (i + 1), "arg");
                }

                slotIndex += size;
            }

            // 其次，调用父类的方法实现
            super.visitCode();
        }

        @Override
        public void visitInsn(int opcode) {
            // 首先，处理自己的代码逻辑
            if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
                printMessage("方法退出: " + methodDesc + "." + methodName, "return");
                if (opcode <= DRETURN) {
                    Type methodType = Type.getMethodType(methodDesc);
                    Type returnType = methodType.getReturnType();
                    int size = returnType.getSize();
                    String descriptor = returnType.getDescriptor();

                    if (size == 1) {
                        super.visitInsn(DUP);
                    } else {
                        super.visitInsn(DUP2);
                    }
                    String methodDesc = String.format("(%s)V", descriptor);
                    printValueOnStack(methodDesc, "返回值", "return");
                } else if (opcode == ARETURN) {
                    super.visitInsn(DUP);
                    printValueOnStack("(Ljava/lang/Object;)V", "返回值", "return");
                } else if (opcode == RETURN) {
                    printMessage("    return void", "return");
                } else {
                    printMessage("    abnormal return", "return");
                }
            }

            // 其次，调用父类的方法实现
            super.visitInsn(opcode);
        }

        private void printMessage(String str, String varScope) {
            super.visitLdcInsn(flowId);
            super.visitMethodInsn(INVOKESTATIC, "com/zcode/zjw/log/trace/infrastructure/utils/ParameterUtils", "setFlowId", "(Ljava/lang/String;)V", false);
            super.visitLdcInsn(methodName);
            super.visitMethodInsn(INVOKESTATIC, "com/zcode/zjw/log/trace/infrastructure/utils/ParameterUtils", "setMethodName", "(Ljava/lang/String;)V", false);
            super.visitLdcInsn(varScope);
            super.visitMethodInsn(INVOKESTATIC, "com/zcode/zjw/log/trace/infrastructure/utils/ParameterUtils", "setVarScope", "(Ljava/lang/String;)V", false);
            super.visitLdcInsn(str);
            super.visitMethodInsn(INVOKESTATIC, "com/zcode/zjw/log/trace/infrastructure/utils/ParameterUtils", "printText", "(Ljava/lang/String;)V", false);
        }

        private void printValueOnStack(String descriptor, String varName, String varScope) {
            super.visitLdcInsn(flowId);
            super.visitMethodInsn(INVOKESTATIC, "com/zcode/zjw/log/trace/infrastructure/utils/ParameterUtils", "setFlowId", "(Ljava/lang/String;)V", false);
            super.visitLdcInsn(methodName);
            super.visitMethodInsn(INVOKESTATIC, "com/zcode/zjw/log/trace/infrastructure/utils/ParameterUtils", "setMethodName", "(Ljava/lang/String;)V", false);
            super.visitLdcInsn(varScope);
            super.visitMethodInsn(INVOKESTATIC, "com/zcode/zjw/log/trace/infrastructure/utils/ParameterUtils", "setVarScope", "(Ljava/lang/String;)V", false);
            super.visitLdcInsn(varName);
            super.visitMethodInsn(INVOKESTATIC, "com/zcode/zjw/log/trace/infrastructure/utils/ParameterUtils", "setVarName", "(Ljava/lang/String;)V", false);
            super.visitMethodInsn(INVOKESTATIC, "com/zcode/zjw/log/trace/infrastructure/utils/ParameterUtils", "printValueOnStack", descriptor, false);
        }
    }
}
