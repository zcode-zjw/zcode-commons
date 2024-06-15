package com.zcode.zjw.log.trace.domain.core.subject.assist;


import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class TraceMethodCorrector {

    public static void correct(String classifyName, String methodName) throws Exception {
        // 1. 获取 ClassPool 对象
        ClassPool classPool = ClassPool.getDefault();
        // 2. 获取目标类的 CtClass 对象
        CtClass targetClass = classPool.get(classifyName);
        // 3. 获取目标方法的 CtMethod 对象
        CtMethod targetMethod = targetClass.getDeclaredMethod(methodName);

        // 4. 使用 ExprEditor 编辑方法体，删除指定方法调用
        targetMethod.instrument(new ExprEditor() {
            public void edit(MethodCall m) throws CannotCompileException {
                // 判断方法调用的方法名和类名
                if (m.getClassName().equals("com.zcode.zjw.log.trace.utils.ParameterUtils")
                        && m.getMethodName().equals("printValueOnStack")
                        && m.getSignature().equals("(Z)V")
                ) {
                    // 删除方法调用
                    m.replace("{ /* Do nothing */ }");
                }
            }
        });
        targetMethod.insertBefore("{ System.out.println(\"Modified by JavaAssist\"); }");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        targetClass.toClass(classLoader, null);

        // 5. 保存修改后的类文件
        targetClass.writeFile();
    }

    public static void main(String[] args) throws Exception {
        TraceMethodCorrector.correct("com.zcode.zjw.log.trace.test.TestClass", "testMethod");
    }
}
