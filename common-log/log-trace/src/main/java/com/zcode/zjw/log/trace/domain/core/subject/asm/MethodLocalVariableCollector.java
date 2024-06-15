package com.zcode.zjw.log.trace.domain.core.subject.asm;

import com.zcode.zjw.log.trace.infrastructure.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.objectweb.asm.Opcodes.ASM9;

/**
 * 方法局部变量收集者
 *
 * @author zhangjiwei
 * @since 2023/8/3
 */
@Slf4j
public class MethodLocalVariableCollector {

    private static int count = 1;

    public List<Map<String, Object>> collect(ClassReader classReader) {
        List<Map<String, Object>> res = new ArrayList<>();
        ClassVisitor visitor = new ClassVisitor(ASM9) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                return new MethodVisitor(ASM9) {
                    @Override
                    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
                        if (!"this".equals(name)) {
                            //log.info("{} - {} - {} - {} - {} - {} - {}", name, descriptor, signature, start, end, index, count);
                            res.add(new HashMap<String, Object>() {{
                                put("name", name);
                                put("descriptor", descriptor);
                                put("signature", signature);
                                put("start", start);
                                put("end", end);
                                put("index", index);
                                put("var", count);
                            }});
                            count++;
                        }
                    }
                };
            }
        };
        classReader.accept(visitor, 0);
        return res;
    }


    public static void main(String[] args) {
        String classPath = "com/zcode/zjw/log/trace/core/TestClass.class";
        String filePath = FileUtils.getFilePath(classPath);
        byte[] sourceBytes = FileUtils.readBytes(filePath);
        //（1）构建ClassReader
        ClassReader cr = new ClassReader(sourceBytes);
        //System.out.println(collect(cr));
    }

}
