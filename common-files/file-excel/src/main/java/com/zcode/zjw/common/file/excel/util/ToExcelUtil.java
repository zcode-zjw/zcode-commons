package com.zcode.zjw.common.file.excel.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.apache.poi.ss.formula.functions.T;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class ToExcelUtil {
    //报表导出处理
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void toExcel(HttpServletResponse response, List listData, String filename, Class clazz) {
        try {
            //设置响应格式
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String name = URLEncoder.encode(filename, "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + name + ".xlsx");

            //获取输出流
            ServletOutputStream outputStream = response.getOutputStream();

            //获取工作簿对象
            ExcelWriter excelWriter = EasyExcel.write(outputStream, clazz).build();

            //获取工作表数量
            int sheetNum;
            if (listData.size() < 1000000) {
                sheetNum = 1;
            } else {
                sheetNum = listData.size() % 1000000 == 0 ? listData.size() / 1000000 : listData.size() / 1000000 + 1;//这里我是根据自身工作需要设置每一百万条数据一个sheet
            }

            // 去调用写入,这里我调用了sheetnum次，实际使用时根据数据库分页的总的页数来。这里最终会写到sheetnum个sheet里面
            List<T> subList;
            for (int i = 0; i < sheetNum; i++) {
                // 每次都要创建writeSheet 这里注意必须指定sheetNo
                WriteSheet writeSheet = EasyExcel.writerSheet(i, filename + i).build();
                //这里是将查询的数据分为若干个sheet页
                subList = listData.subList(i * 1000000, i == sheetNum - 1 ? listData.size() : (i + 1) * 1000000);
                excelWriter.write(subList, writeSheet);
            }

            //finish 会帮忙关闭流
            excelWriter.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}