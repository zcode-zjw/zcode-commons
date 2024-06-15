package com.zcode.zjw.log.trace.infrastructure.utils;

import com.zcode.zjw.common.utils.common.DateUtils;
import com.zcode.zjw.common.utils.db.JdbcUtils;
import com.zcode.zjw.common.utils.file.SpringConfigFileUtil;
import com.zcode.zjw.log.trace.domain.core.repository.po.TraceLogInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Slf4j
public class ParameterUtils {
    private static final DateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void printValueOnStack(boolean value) {
        TraceLogInfo traceLogInfo = new TraceLogInfo();
        traceLogInfo.setVarValue(value);
        traceLogInfo.setVarType("boolean");
        traceLogInfo.setVarName(varName);
        printToLogFile(traceLogInfo);
        //System.out.println("变量名：" + varName + " - 值：" + value);
    }

    public static void printValueOnStack(byte value) {
        TraceLogInfo traceLogInfo = new TraceLogInfo();
        traceLogInfo.setVarValue(value);
        traceLogInfo.setVarType("byte");
        traceLogInfo.setVarName(varName);
        printToLogFile(traceLogInfo);
        //System.out.println("变量名：" + varName + " - 值：" + value);
    }

    public static void printValueOnStack(char value) {
        TraceLogInfo traceLogInfo = new TraceLogInfo();
        traceLogInfo.setVarValue(value);
        traceLogInfo.setVarType("char");
        traceLogInfo.setVarName(varName);
        printToLogFile(traceLogInfo);
        //System.out.println("变量名：" + varName + " - 值：" + value);
    }

    public static void printValueOnStack(short value) {
        TraceLogInfo traceLogInfo = new TraceLogInfo();
        traceLogInfo.setVarValue(value);
        traceLogInfo.setVarType("short");
        traceLogInfo.setVarName(varName);
        printToLogFile(traceLogInfo);
        //System.out.println("变量名：" + varName + " - 值：" + value);
    }

    public static void printValueOnStack(int value) {
        TraceLogInfo traceLogInfo = new TraceLogInfo();
        traceLogInfo.setVarValue(value);
        traceLogInfo.setVarType("int");
        traceLogInfo.setVarName(varName);
        printToLogFile(traceLogInfo);
        //System.out.println("变量名：" + varName + " - 值：" + value);
    }

    public static void printValueOnStack(float value) {
        TraceLogInfo traceLogInfo = new TraceLogInfo();
        traceLogInfo.setVarValue(value);
        traceLogInfo.setVarName(varName);
        traceLogInfo.setVarType("float");
        printToLogFile(traceLogInfo);
        //System.out.println("变量名：" + varName + " - 值：" + value);
    }

    public static void printValueOnStack(long value) {
        TraceLogInfo traceLogInfo = new TraceLogInfo();
        traceLogInfo.setVarValue(value);
        traceLogInfo.setVarName(varName);
        traceLogInfo.setVarType("long");
        printToLogFile(traceLogInfo);
        //System.out.println("变量名：" + varName + " - 值：" + value);
    }

    public static void printValueOnStack(double value) {
        TraceLogInfo traceLogInfo = new TraceLogInfo();
        traceLogInfo.setVarValue(value);
        traceLogInfo.setVarType("double");
        traceLogInfo.setVarName(varName);
        printToLogFile(traceLogInfo);
        //System.out.println("变量名：" + varName + " - 值：" + value);
    }

    private static String varName;

    private static String flowId;

    public static void setVarScope(String varScope) {
        ParameterUtils.varScope = varScope;
    }

    public static void setMethodName(String methodName) {
        ParameterUtils.methodName = methodName;
    }

    private static String varScope;

    private static String methodName;

    private static final String logRootPath = SpringConfigFileUtil.getValueByKeyOnSpringConfigFile("trace.log.local.file.path", System.getProperty("user.home") + File.separator + "trace/logs/");

    private static void printToLogFile(TraceLogInfo traceLogInfo) {
        // 生成日志文件的名称
        String currentDatetime = DateUtils.getCurrentDateStringFormat();
        String logFileName = logRootPath + File.separator + "trace-" + flowId + "." + DateUtils.getCurrentDateNumber() + ".log";
        String datetimeLog = "[" + currentDatetime + "]";
        String logContent = datetimeLog + "变量名：" + traceLogInfo.getVarName() + " - " + "变量值：" + traceLogInfo.getVarValue() + " - " + "变量类型：" + traceLogInfo.getVarType();
        // 数据库记录
        recordDb(traceLogInfo);
        // 输出日志字符串
        try (FileWriter fileWriter = new FileWriter(logFileName, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(logContent);
            bufferedWriter.newLine(); // 换行
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void recordDb(TraceLogInfo traceLogInfo) {
        try {
            Map<String, String> fileInfoMap = SpringConfigFileUtil.getSpringConfigFileInfoMap();
            JdbcUtils.executeUpdateAndCloseBySecurity(fileInfoMap.get("name"),
                    String.format("insert into zcode_trace_log_info(flow_id, method_name, var_scope, var_name, var_type, var_value, create_datetime) values('%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                            flowId, methodName, varScope, varName, traceLogInfo.getVarType(),
                            traceLogInfo.getVarValue(), DateUtils.getCurrentDateStringFormat())
            );
        } catch (IOException e) {
            log.error("追踪记录入库失败", e);
        }
    }

    public static void printValueOnStack(Object value) {
        TraceLogInfo traceLogInfo = new TraceLogInfo();
        traceLogInfo.setVarName(varName);
        if (value == null) {
            traceLogInfo.setVarValue(null);
            traceLogInfo.setVarType("null");
            //System.out.println("变量名：" + varName + " - 值：" + null);
        } else if (value instanceof String) {
            traceLogInfo.setVarValue(value);
            traceLogInfo.setVarType("String");
            //System.out.println("变量名：" + varName + " - 值：" + value);
        } else if (value instanceof Date) {
            traceLogInfo.setVarValue(fm.format(value));
            traceLogInfo.setVarType("Date");
            //System.out.println("变量名：" + varName + " - 值：" + fm.format(value));
        } else if (value instanceof char[]) {
            traceLogInfo.setVarValue(Arrays.toString((char[]) value));
            traceLogInfo.setVarType("char[]");
            //System.out.println("变量名：" + varName + " - 值：" + Arrays.toString((char[])value));
        } else if (value.getClass().isArray()) {
            traceLogInfo.setVarValue(Collections.singletonList(value));
            traceLogInfo.setVarType("Array");
        } else {
            traceLogInfo.setVarValue(value.toString());
            traceLogInfo.setVarType(value.getClass().getTypeName());
            //System.out.println("变量名：" + varName + " - 值：" + value.getClass() + ": " + value);
        }
        printToLogFile(traceLogInfo);
        varName = null;
    }

    public static void printText(String str) {
        TraceLogInfo traceLogInfo = new TraceLogInfo();
        traceLogInfo.setVarValue(str);
        traceLogInfo.setVarType(str.getClass().getTypeName());
        printToLogFile(traceLogInfo);
        //System.out.println(str);
    }

    public static void output(PrintStream printStream, int val) {
        printStream.println("ParameterUtils: " + val);
    }

    public static void setVarName(String varName) {
        ParameterUtils.varName = varName;
    }

    public static void setFlowId(String flowId) {
        ParameterUtils.flowId = flowId;
    }
}
