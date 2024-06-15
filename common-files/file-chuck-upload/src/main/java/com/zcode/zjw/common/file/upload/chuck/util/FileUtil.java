package com.zcode.zjw.common.file.upload.chuck.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;

/**
 * @author zhangjiwei
 * @description 文件工具类
 * @date 2022年11月15日 下午 11:27
 */
@Slf4j
public class FileUtil {

    /**
     * 根据当前年月创建目录
     *
     * @return yyyy_mm
     */
    public static String createDirPathByYearMonth() {
        final LocalDateTime now = LocalDateTime.now();
        final int year = now.getYear();
        final Month month = now.getMonth();
        final int monthValue = month.getValue();
        return year + "_" + monthValue;
    }

    /**
     * 获取文件类型后缀
     *
     * @param filename 文件名称
     * @return 文件类型
     */
    public static String getFileTypeSuffix(String filename) {
        final int pointIndex = filename.lastIndexOf(".");
        final String suffix = filename.substring(pointIndex + 1);
        return StringUtils.isBlank(suffix) ? "" : suffix;
    }

    /**
     * 获取不带类型后缀的文件名
     *
     * @param filename 文件名称
     * @return 不带类型后缀的文件名
     */
    public static String getFileNameWithoutTypeSuffix(String filename) {
        final int pointIndex = filename.lastIndexOf(".");
        final String pureName = filename.substring(0, pointIndex);
        return StringUtils.isBlank(pureName) ? "" : pureName;
    }

    /**
     * 设置文件下载的响应信息
     *
     * @param response       响应对象
     * @param file           文件
     * @param originFilename 源文件名
     */
    public static void setDownloadResponseInfo(HttpServletResponse response, File file, String originFilename) {
        try {
            response.setCharacterEncoding("UTF-8");
            /* 文件名 */
            final String fileName = StringUtils.isBlank(originFilename) ? file.getName() : originFilename;

            /* 获取要下载的文件类型， 设置文件类型声明 */
            String mimeType = new Tika().detect(file);
            response.setContentType(mimeType);

            /* 设置响应头，告诉该文件用于下载而非展示  attachment;filename 类型：附件，文件名称 */
            final String header = response.getHeader("User-Agent");
            if (StringUtils.isNotBlank(header) && header.contains("Firefox")) {
                /* 对火狐浏览器单独设置 */
                response.setHeader("Content-Disposition", "attachment;filename==?UTF-8?B?" + new BASE64Encoder().encode(fileName.getBytes(StandardCharsets.UTF_8)) + "?=");
            } else
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        } catch (Exception e) {
            log.info("设置响应信息异常：{}", e.getMessage());
        }
    }

}