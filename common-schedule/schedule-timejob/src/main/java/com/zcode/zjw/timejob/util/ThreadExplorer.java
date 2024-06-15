package com.zcode.zjw.timejob.util;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ThreadExplorer {


    public static Thread[] listThreads() {
        int nThreads = Thread.activeCount();
        Thread[] ret = new Thread[nThreads];
        Thread.enumerate(ret);
        return ret;
    }

    /**
     * Helper function to access a thread per name (ignoring case)
     *
     * @param name
     * @return
     */
    public static Thread fetchThread(String name) {
        Thread[] threadArray = listThreads();
        // for (Thread t : threadArray)
        for (int i = 0; i < threadArray.length; i++) {
            Thread t = threadArray[i];
            if (t.getName().equalsIgnoreCase(name))
                return t;
        }
        return null;
    }

    /**
     * Allow for killing threads
     *
     * @param threadName
     * @param isStarredExp (regular expressions with *)
     */
    @SuppressWarnings("deprecation")
    public static int kill(String threadName, boolean isStarredExp) {
        String me = "ThreadExplorer.kill: ";
        if (log.isDebugEnabled()) {
            log.debug("Entering " + me + " with " + threadName + " isStarred: " + isStarredExp);
        }
        int ret = 0;
        Pattern mypattern = null;
        if (isStarredExp) {
            String realreg = threadName.toLowerCase().replaceAll("\\*", "\\.\\*");
            mypattern = Pattern.compile(realreg);
        }
        Thread[] threads = listThreads();
        for (int i = 0; i < threads.length; i++) {
            Thread thread = threads[i];
            if (thread == null)
                continue;
            // kill the thread unless it is not current thread
            boolean matches = false;
            if (isStarredExp) {
                Matcher matcher = mypattern.matcher(thread.getName().toLowerCase());
                matches = matcher.matches();
            } else {
                matches = (thread.getName().equalsIgnoreCase(threadName));
            }
            if (matches && (Thread.currentThread() != thread) && !thread.getName().equals("main")) {
                if (log.isInfoEnabled())
                    log.info("Killing thread named [" + thread.getName() + "]"); // , removing its uncaught
                ret++;
                try {
                    thread.stop();
                } catch (ThreadDeath e) {
                    log.warn("Thread already death.", e);
                }

            }
        }
        return ret;
    }

    public static String show(String title) {
        StringBuilder out = new StringBuilder();
        Thread[] threadArray = ThreadExplorer.listThreads();
        out.append(title).append("\n");
        for (int i = 0; i < threadArray.length; i++) {
            Thread thread = threadArray[i];
            if (thread != null) {
                out.append("* [" + thread.getName() + "] " + (thread.isDaemon() ? "(Daemon)" : "")
                        + " Group: " + thread.getThreadGroup().getName() + "\n");
            } else {
                out.append("* ThreadDeath: " + thread + "\n");
            }

        }
        return out.toString();
    }

    public static int active() {
        int count = 0;
        Thread[] threadArray = ThreadExplorer.listThreads();
        for (int i = 0; i < threadArray.length; i++) {
            Thread thread = threadArray[i];
            if (thread != null) {
                count++;
            }
        }

        return count;
    }

}