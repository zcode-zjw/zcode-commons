package com.zcode.zjw.common.utils.id;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * 测试类
 */
@SpringBootTest
class PrimaryKeyUtilTest {

    @Autowired
    private PrimaryKeyUtil primaryKeyUtil;

    /**
     * 使用redis生成唯一id
     */
    @Test
    public void testRedis() {
        long startMillis = System.currentTimeMillis();
        String orderIdPrefix = primaryKeyUtil.getIdPrefix(new Date());
        //生成单个id
        //Long aLong = primaryKeyUtil.orderId(orderIdPrefix);
        for (int i = 0; i < 1000; i++) {
            Long aLong = primaryKeyUtil.flowId("ORDER", orderIdPrefix);
            System.out.println(aLong);
        }
        long endMillis = System.currentTimeMillis();
        System.out.println("测试生成1000个使用时间：" + (endMillis - startMillis) + ",单位毫秒");
        //测试生成1000个使用时间：514,单位毫秒
    }

}

