package com.zcode.zjw.log.trace.domain.core.subject.assist;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.asm.Advice;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class MethodCallLogger {

    public static void main(String[] args) throws Exception {
        ByteBuddyAgent.install();
        Instrumentation instrumentation = ByteBuddyAgent.getInstrumentation();
        instrumentation.addTransformer(new MethodCallTransformer());

        // 运行你的应用程序代码
        SourceCodeLoader sourceCodeLoader = new SourceCodeLoader();
        sourceCodeLoader.getAllMethod(MethodExtractor.class);
    }

    public static class MethodCallTransformer implements ClassFileTransformer {

        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                ProtectionDomain protectionDomain, byte[] classfileBuffer) {

            try {
                if (className.equals("com/zcode/zjw/log/trace/domain/core/subject/assist/test/MyClass")) {
                    return new ByteBuddy()
                            .redefine(Class.forName(className))
                            .visit(Advice.to(MethodCallLogger.class)
                                    .on(named("myMethod")))
                            .make()
                            .getBytes();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Advice.OnMethodEnter
    public static void onMethodEnter() {
        System.out.println("Method called!");
    }
}
