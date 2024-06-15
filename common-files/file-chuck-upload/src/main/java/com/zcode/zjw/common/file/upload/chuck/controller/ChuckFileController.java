package com.zcode.zjw.common.file.upload.chuck.controller;

import com.zcode.zjw.common.file.upload.chuck.entity.Chuck;
import com.zcode.zjw.common.file.upload.chuck.entity.ChuckFile;
import com.zcode.zjw.common.file.upload.chuck.service.ChuckFileService;
import com.zcode.zjw.common.file.upload.chuck.service.ChuckService;
import com.zcode.zjw.common.file.upload.chuck.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Objects;

/**
 * @author zhangjiwei
 * @description 大型文件上传
 * @date 2022年11月18日 下午 10:58
 */
@Slf4j
@RestController
@RequestMapping("/chuck-file")
public class ChuckFileController {

    private static final String CHUCK_DIR = "chuck";

    //@Resource
    //private FileProperty fileProperty;

    @Value("${file.base-directory}")
    private String baseDirectory;

    @Resource
    private ChuckFileService chuckFileService;

    @Resource
    private ChuckService chuckService;

    /**
     * 上传分块文件
     *
     * @param chuck 分块文件
     * @return 响应结果
     */
    @PostMapping(value = "/chuck", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object uploadFileChuck(@ModelAttribute Chuck chuck) {
        final MultipartFile mf = chuck.getFile();
        log.info("收到文件块 文件块名：{} 块编号：{}", mf.getOriginalFilename(), chuck.getChunkNumber());
        try {
            /* 1、准备存储位置  根目录 + 分块目录 + 无类型文件名的目录  */
            final String pureName = FileUtil.getFileNameWithoutTypeSuffix(Objects.requireNonNull(mf.getOriginalFilename()));
            //String storagePath = fileProperty.getBaseDirectory() + File.separator + CHUCK_DIR + File.separator + pureName;
            String storagePath = baseDirectory + File.separator + CHUCK_DIR + File.separator + pureName;
            final File storePath = new File(storagePath);
            if (!storePath.exists()) storePath.mkdirs();

            /* 2、准备分块文件的规范名称 [无类型文件名 -分块号] */
            String chuckFilename = pureName + "-" + chuck.getChunkNumber();

            /* 3、向存储位置写入文件 */
            final File targetFile = new File(storePath, chuckFilename);
            mf.transferTo(targetFile);
            log.debug("文件 {} 写入成功, uuid:{}", chuck.getFilename(), chuck.getIdentifier());

            chuck = chuckService.saveChuck(chuck);
        } catch (Exception e) {
            log.error("文件上传异常：{}, {}", chuck.getFilename(), e.getMessage());
            return e.getMessage();
        }
        return chuck;
    }

    /**
     * 检查该分块文件是否上传了
     *
     * @param chuck 分块文件
     * @return 304 | 分块文件 has-chucked
     */
    @GetMapping("/chuck")
    public Object checkHasChucked(@ModelAttribute Chuck chuck, HttpServletResponse response) {
        final boolean hasChucked = chuckService.checkHasChucked(chuck);
        if (hasChucked) response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        return chuck;
    }

    /**
     * 文件合并
     *
     * @param chuckFile 分块文件合并信息
     */
    @PostMapping("/merge")
    public void mergeChuckFile(@ModelAttribute ChuckFile chuckFile) {
        /* 获取存储位置 */
        final String pureName = FileUtil.getFileNameWithoutTypeSuffix(chuckFile.getFilename());
        //String storagePath = fileProperty.getBaseDirectory() + File.separator + CHUCK_DIR + File.separator + pureName;
        String storagePath = baseDirectory + File.separator + CHUCK_DIR + File.separator + pureName;
        chuckFileService.mergeChuckFile(storagePath, chuckFile);

        chuckFile.setLocation(storagePath);
        chuckFileService.addChuckFile(chuckFile);
    }
}