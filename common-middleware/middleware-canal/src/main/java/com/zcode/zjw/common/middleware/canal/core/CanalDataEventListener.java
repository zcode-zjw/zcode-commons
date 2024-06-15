package com.zcode.zjw.common.middleware.canal.core;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@CanalEventListener
@ConditionalOnProperty(name = "canal.monitor.type", havingValue = "listener")
@Component
@Slf4j
public class CanalDataEventListener {

    @PostConstruct
    public void initializeListener() {
        log.info("canal数据同步模式开启 -> listener");
    }

    /**
     * 增加数据监听
     *
     * @param eventType
     * @param rowData
     */
    @InsertListenPoint
    public void onEventInsert(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        rowData.getAfterColumnsList()
                .forEach((c) -> System.out.println("By--Annotation: " + c.getName() + " ::   " + c.getValue()));
    }

    /**
     * 修改数据监听
     *
     * @param rowData
     */
    @UpdateListenPoint
    public void onEventUpdate(CanalEntry.RowData rowData) {
        System.out.println("UpdateListenPoint");
        rowData.getAfterColumnsList()
                .forEach((c) -> System.out.println("By--Annotation: " + c.getName() + " ::   " + c.getValue()));
    }

    /**
     * 删除数据监听
     *
     * @param eventType
     */
    @DeleteListenPoint
    public void onEventDelete(CanalEntry.EventType eventType) {
        System.out.println("DeleteListenPoint");
    }

    /**
     * 自定义数据监听
     *
     * @param eventType
     * @param rowData
     */
    @ListenPoint(destination = "example", schema = "canal", table = {"canal_test", "tb_order"},
            eventType = CanalEntry.EventType.UPDATE)
    public void onEventCustomUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        System.err.println("DeleteListenPoint");
        rowData.getAfterColumnsList()
                .forEach((c) -> System.out.println("By--Annotation: " + c.getName() + " ::   " + c.getValue()));
    }

    @ListenPoint(destination = "example", schema = "canal", // 所要监听的数据库名
            table = {"canal_test"}, // 所要监听的数据库表名
            eventType = {CanalEntry.EventType.UPDATE, CanalEntry.EventType.INSERT, CanalEntry.EventType.DELETE})
    public void onEventCustomUpdateForTbUser(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        getChangeValue(eventType, rowData);
    }

    public static void getChangeValue(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        if (eventType == CanalEntry.EventType.DELETE) {
            rowData.getBeforeColumnsList().forEach(column -> {
                // 获取删除前的数据
                System.out.println(column.getName() + " == " + column.getValue());
            });
        } else {
            rowData.getBeforeColumnsList().forEach(column -> {
                // 打印改变前的字段名和值
                System.out.println(column.getName() + " == " + column.getValue());
            });

            rowData.getAfterColumnsList().forEach(column -> {
                // 打印改变后的字段名和值
                System.out.println(column.getName() + " == " + column.getValue());
            });
        }
    }
}
