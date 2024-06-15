package com.zcode.zjw.ssh.config;

import com.jcraft.jsch.Session;

public class SessionThreadLocal {

    private static ThreadLocal<Session> threadLocal = new ThreadLocal<>();

    public static synchronized void set(Session session) {
        threadLocal.set(session);
    }

    public static synchronized Session get() {
        return threadLocal.get();
    }

    public static synchronized void remove() {
        threadLocal.remove();
    }
}

