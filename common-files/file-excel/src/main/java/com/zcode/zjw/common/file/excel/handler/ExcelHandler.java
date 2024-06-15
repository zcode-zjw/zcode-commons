package com.zcode.zjw.common.file.excel.handler;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExcelHandler {

    private List<WriteSheet> sheets;
    private String dirName;
    private static final Integer DEFAULT_SHEETS_NUM = 5;
    private static final Integer DEFAULT_PER_SHEET_NUM = 1000000;

    public ExcelHandler() {
    }

    public ExcelHandler(String folderName) {
        dirName = folderName;
    }

    /**
     * 创建excel和sheet，创建时可以指定sheet数量
     *
     * @param excelName
     * @param clazz
     * @param sumSheet
     * @return
     */
    public ExcelWriter create(String excelName, Class clazz, int sumSheet) {
        ExcelWriter excelWriter = EasyExcel.write(route(excelName), clazz.asSubclass(clazz)).build();
        createSheets(sumSheet);
        return excelWriter;
    }

    /**
     * 创建excel和sheet，sheet 数量默认 5， 最高可存放500W 行左右的数据，受每个sheet存放数据的限制
     *
     * @param excelName
     * @param clazz
     * @return
     */
    public ExcelWriter create(String excelName, Class clazz) {
        ExcelWriter excelWriter = EasyExcel.write(route(excelName), clazz.asSubclass(clazz)).build();
        createSheets(DEFAULT_SHEETS_NUM);
        return excelWriter;
    }

    /**
     * 写数据到excel， 仅使用一个sheet，不可用于百万以上数据
     *
     * @param excelWriter
     * @param list
     */
    public void write(ExcelWriter excelWriter, List list) {
        excelWriter.write(list, sheets.get(0));
    }

    /**
     * 写数据到excel
     *
     * @param excelWriter
     * @param list        每一次的数据
     * @param sheetNum    sheet页码
     */
    public void write(ExcelWriter excelWriter, List list, int sheetNum) {
        excelWriter.write(list, sheets.get(sheetNum));
    }

    /**
     * 写完数据关闭(finish 有关流操作)，必须的操作
     *
     * @param excelWriter
     */
    public void finish(ExcelWriter excelWriter) {
        excelWriter.finish();
    }

    /**
     * 创建指定数量的sheet
     *
     * @param num
     */
    private void createSheets(int num) {
        sheets = new ArrayList();
        for (int i = 0; i <= num; i++) {
            WriteSheet sheet = EasyExcel.writerSheet(i, "sheet" + i).build();
            sheets.add(sheet);
        }
    }

    /**
     * 获取excel存放路径
     *
     * @param excelName
     * @return
     */
    private String route(String excelName) {
        if (null == dirName) {
            dirName = "D:\\data\\excel";
        }
        String filePath = "D:\\data\\excel\\" + dirName + "/";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return filePath + excelName + ".xlsx";
    }
}
