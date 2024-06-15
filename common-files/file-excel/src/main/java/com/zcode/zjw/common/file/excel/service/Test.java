package com.zcode.zjw.common.file.excel.service;

import com.alibaba.excel.ExcelWriter;
import com.zcode.zjw.common.file.excel.entity.Data1;
import com.zcode.zjw.common.file.excel.handler.ExcelHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        Test tt = new Test();
        tt.test01();
    }

    /**
     * 模拟百万以下数据导入到excel
     */
    private void test00() {
        int num = 0;
        ExcelHandler handler = null;
        ExcelWriter writer = null;
        try {
            // 创建handler对象 -- 参数文件夹名
            handler = new ExcelHandler("test_00");
            writer = handler.create("记录", Data1.class);

            List<Object> list = new ArrayList<>(1024);
            // 方式一：一次性导出
            for (int i = 0; i < 1000000; i++) {
                num++;
                list.add(new Data1("张三" + num, "123abc" + num));
            }
            handler.write(writer, list);


            // 方式二：分多次导出，为了降低导出过程中的内存资源，可以分多次导出
            for (int a = 0; a < 100; a++) { // 模拟拿100次数据，每次拿10000条数据
                for (int i = 0; i < 10000; i++) { // 模拟一次拿10000条数据
                    num++;
                    list.add(new Data1("张三" + num, "123abc" + num));
                }
                handler.write(writer, list);
                // 防止数据重复，及时情况list集合
                list.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                handler.finish(writer);
            }
        }
    }

    /**
     * 模拟百万以上数据导入到excel
     */
    private void test01() {
        int num = 0;
        ExcelHandler handler = null;
        ExcelWriter writer = null;
        try {
            // 创建handler对象 -- 参数文件夹名
            handler = new ExcelHandler("test_01");
            writer = handler.create("记录", Data1.class, 10);

            List<Object> list = new ArrayList<>(1024);
            // 此处依旧可以模仿test00去优化
            // TODO 考虑能否做成异步方式，用阻塞队列，生产和消费同事进行，一边从数据库查询数据，一边导入数据到excel

            for (int a = 0; a < 10; a++) { // 模拟分页页数， 每页50W数据
                for (int i = 0; i < 500000; i++) { // 模拟每页数据量
                    num++;
                    list.add(new Data1("张三" + num, "123abc" + num));
                }
                handler.write(writer, list, a);
                // 防止数据重复，及时情况list集合
                list.clear();
            }

            // 考虑从数据库查询数据这种情况
            // 先查询数据总量，然后计算分页数，按照每页50W
            int allCount = 7500000; // 数据总条数 750W
            int pageSize = (int) Math.ceil(allCount / 1000000.0);
            writer = handler.create("MySQL数据", Data1.class, pageSize);
            // TODO 效率问题，可以考虑多线程
            // TODO 每个线程处理部分数据，处理 步进的数据量，然后再可以根据下面的分页查询数据
            // TODO 每个处理线程CountDownLaunch处理，主线程 ctl.await()阻塞，所有处理线程完成之后，再进行下面的业务
            for (int k = 0; k < pageSize; k++) {
                // 从数据库分页查询数据
                List<Data1> datasByMySQL = selectByMySQL(new HashMap<>(), k * 500000, 500000);
                handler.write(writer, datasByMySQL, k);
                datasByMySQL.clear();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                handler.finish(writer);
            }
        }
    }

    /**
     * 从数据库查询数据
     *
     * @param params 条件查询的params
     * @param offset 偏移量
     * @param rows   行数
     * @return
     */
    private List<Data1> selectByMySQL(HashMap<Object, Object> params, int offset, int rows) {
        // TODO 从数据库查询数据  select * from table where param=#{param} limit offset,rows
        return new ArrayList<>();
    }
}
