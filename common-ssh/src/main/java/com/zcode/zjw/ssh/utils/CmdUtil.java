package com.zcode.zjw.ssh.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CmdUtil {
  private static final String COMMON_CHARACTER_LINUX = "UTF-8";

  private static final String COMMON_CHARACTER_WINDOWS = "GBK";

  private static final String COMMON_OS_LINUX = "linux";

  private static final String COMMON_OS_WINDOWS = "windows";

  public static String executeOnLinux(String cmd) throws Exception {
    try {
      return execute(cmd, "linux");
    } catch (IOException e) {
      e.printStackTrace();
      throw new IOException("" + e.getMessage());
    }
  }

  public static List<String> executeLinuxNewFlow(List<String> commands) {
    List<String> rspList = new ArrayList<>();
    Runtime run = Runtime.getRuntime();
    try {
      Process proc = run.exec("/bin/bash", (String[])null, (File)null);
      BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
      for (String line : commands)
        out.println(line);
      out.println("exit");
      String rspLine = "";
      while ((rspLine = in.readLine()) != null)
        rspList.add(rspLine);
      proc.waitFor();
      in.close();
      out.close();
      proc.destroy();
    } catch (IOException | InterruptedException e1) {
      e1.printStackTrace();
    }
    return rspList;
  }

  public static String executeOnWindows(String cmd) throws Exception {
    try {
      return execute(cmd, "windows");
    } catch (Exception e) {
      throw new Exception("" + e.getMessage());
    }
  }

  private static String execute(String cmd, String os) throws Exception {
    Process process = null;
    try {
      String[] command = { "/bin/sh", "-c", cmd };
      if (os.equals("windows")) {
        command[0] = "cmd";
        command[1] = "/c";
      }
      process = Runtime.getRuntime().exec(command);
      process.waitFor(5, TimeUnit.SECONDS);
      String v_rst = getCommonReturn(process, os);
      process.destroy();
      return v_rst;
    } catch (Exception e) {
      throw new Exception("" + e.getMessage());
    } finally {
      if (process != null)
        process.destroy();
    }
  }

  private static String getCommonReturn(Process process, String execTyp) throws IOException {
    BufferedReader br = null;
    try {
      String v_common_character = "UTF-8";
      if (execTyp.equals("windows"))
        v_common_character = "GBK";
      StringBuffer buffer = new StringBuffer();
      int catchSize = 4096;
      br = new BufferedReader(new InputStreamReader(process.getInputStream(), v_common_character), 4096);
      String line = null;
      while ((line = br.readLine()) != null) {
        buffer.append(line);
        buffer.append("\r\n");
      }
      process.destroy();
      return buffer.toString();
    } catch (UnsupportedEncodingException e) {
      throw new UnsupportedEncodingException("");
    } catch (IOException e) {
      throw new IOException("");
    } finally {
      if (br != null)
        try {
          br.close();
        } catch (IOException e) {
          throw new IOException("管理命令行读取流出错！");
        }
    }
  }

  public static void main(String[] args) throws Exception {
    String rst = executeOnLinux("ifconfig");
    System.out.println(rst);
  }
}
