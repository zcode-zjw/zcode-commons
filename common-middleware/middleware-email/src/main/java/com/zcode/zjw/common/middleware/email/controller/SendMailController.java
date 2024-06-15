package com.zcode.zjw.common.middleware.email.controller;

import com.zcode.zjw.common.middleware.email.pojo.MessageInfo;
import com.zcode.zjw.common.middleware.email.service.impl.MailServiceImpl;
import com.zcode.zjw.common.middleware.email.util.SpringUtils;
import com.zcode.zjw.log.api.annotation.ApiLog;
import com.zcode.zjw.web.common.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 发送邮件接口
 */
@RestController
@RequestMapping("/mail")
@ApiLog
public class SendMailController {

    /**
     * 发送文本邮件
     *
     * @param messageInfo 邮件信息
     */
    @PostMapping("sendSimpleMail")
    public Result<?> sendSimpleMail(MessageInfo messageInfo) {
        MailServiceImpl mailServiceImpl = SpringUtils.getBean(MailServiceImpl.class);
        return mailServiceImpl.sendSimpleMail(messageInfo.getReceiver(), messageInfo.getSubject(), messageInfo.getContent());
    }

    /**
     * 发送带附件的邮件
     *
     * @param messageInfo   邮件信息
     * @param multipartFile 上传的文件
     */
    @PostMapping("sendAttachmentsMail")
    public Result<?> sendAttachmentsMail(MessageInfo messageInfo, MultipartFile multipartFile) {
        MailServiceImpl mailServiceImpl = SpringUtils.getBean(MailServiceImpl.class);
        // 发送带附件的邮件( 路径中的 \ 通常需要使用 \\, 如果是 / 就不需要使用转义了, \ 常用于本地，而/ 常用于网络连接地址 )
        messageInfo.setMultipartFile(multipartFile);
        return mailServiceImpl.sendAttachmentsMail(messageInfo);
    }

    /**
     * 发送模板邮件
     *
     * @param messageInfo 邮件信息
     * @return
     */
    @PostMapping("sendTemplateMail")
    public Result<?> sendTemplateMail(MessageInfo messageInfo) {
        MailServiceImpl mailServiceImpl = SpringUtils.getBean(MailServiceImpl.class);
        // 发送Html模板邮件
        return mailServiceImpl.sendTemplateMail(messageInfo.getReceiver(), messageInfo.getSubject(), messageInfo.getContent(),
                messageInfo.getFromName(), messageInfo.getReceiverName());
    }

}