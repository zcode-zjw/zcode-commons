package com.zcode.zjw.log.trace.domain.core.subject.asm;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

/**
 * 方法访问者
 *
 * @author zhangjiwei
 * @since 2023/8/3
 */
public class TraceMethodVisitor extends ClassVisitor {

    private List<String> localVariableNameList;


    public TraceMethodVisitor(int i, ClassVisitor classVisitor, List<String> localVariableNameList) {
        super(i, classVisitor);
        this.localVariableNameList = localVariableNameList;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (mv != null && !"<init>".equals(name)) {
            mv = new TraceMethodAdapter(api, mv, localVariableNameList);

        }

        return mv;
    }


}
