package com.zcode.zjw.log.trace.domain.core.subject.assist;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LoadClassInfo extends ClassLoader {
    private ClassLoader classLoader;
    private String pack_name;

    public LoadClassInfo(ClassLoader parent, String pack_name) {
        super(parent);
        this.classLoader = parent;
        this.pack_name = pack_name;
    }

    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (!name.contains(pack_name)) return super.loadClass(name);
        try {
            if (!name.matches("(.+)\\/(.+)\\/(.+)")) return null;
            InputStream input = classLoader.getResourceAsStream(String.format("%s.class", name.replace(".", "/")));
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int data = input.read();
            while (data != -1) {
                buffer.write(data);
                data = input.read();
            }
            input.close();
            byte[] classData = buffer.toByteArray();
            System.out.println("name " + name);
            return defineClass(name, classData, 0, classData.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}