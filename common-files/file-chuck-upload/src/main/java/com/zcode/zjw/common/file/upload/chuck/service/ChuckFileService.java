package com.zcode.zjw.common.file.upload.chuck.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcode.zjw.common.file.upload.chuck.entity.ChuckFile;
import com.zcode.zjw.common.file.upload.chuck.mapper.ChuckFileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author zhangjiwei
 * @description 控制器
 * @date 2022年11月18日 下午 11:08
 */
@Slf4j
@Service
public class ChuckFileService extends ServiceImpl<ChuckFileMapper, ChuckFile> {

    public void mergeChuckFile(String storagePath, ChuckFile chuckFile) {
        try {
            final String finalFile = storagePath + File.separator + chuckFile.getFilename();

            Files.createFile(Paths.get(finalFile));
            Files.list(Paths.get(storagePath))
                    /* 1、过滤非合并文件 */
                    .filter(path -> path.getFileName().toString().contains("-"))
                    /* 2、升序排序 0 - 1 - 2 。。。 */
                    .sorted((o1, o2) -> {
                        String p1 = o1.getFileName().toString();
                        String p2 = o2.getFileName().toString();
                        int i1 = p1.lastIndexOf("-") + 1;
                        int i2 = p2.lastIndexOf("-") + 1;
                        return Integer.valueOf(p1.substring(i1)).compareTo(Integer.valueOf(p2.substring(i2)));
                    })
                    /* 3、合并文件 */
                    .forEach(path -> {
                        try {
                            /* 以追加的形式写入文件 */
                            Files.write(Paths.get(finalFile), Files.readAllBytes(path), StandardOpenOption.APPEND);
                            /* 合并后删除该块 */
                            Files.delete(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            log.error("{}合并写入异常：{}", chuckFile.getFilename(), e.getMessage());
        }
    }


    public void addChuckFile(ChuckFile chuckFile) {
        baseMapper.insert(chuckFile);
    }
}