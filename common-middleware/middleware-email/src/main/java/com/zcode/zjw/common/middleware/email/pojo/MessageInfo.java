package com.zcode.zjw.common.middleware.email.pojo;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MessageInfo {

    /**
     * 收件人邮箱
     **/
    private String receiver;

    /**
     * 一对多群发收件人邮箱
     **/
    private String receivers;

    /**
     * 邮件标题
     **/
    private String subject;

    /**
     * 邮件内容
     **/
    private String content;

    /**
     * 图片路径
     **/
    private String imgPath;

    /**
     * 文件路径
     **/
    private String filePath;

    /**
     * 发件人姓名
     **/
    private String fromName;

    /**
     * 收件人姓名
     **/
    private String receiverName;

    /**
     * 附件
     */
    private MultipartFile multipartFile;

    /**
     * 多个附件
     */
    private MultipartFile[] multipartFiles;

}