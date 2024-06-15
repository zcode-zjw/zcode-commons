package com.zcode.zjw.common.middleware.email.service.impl;

import com.zcode.zjw.common.middleware.email.pojo.MessageInfo;
import com.zcode.zjw.common.middleware.email.service.MailService;
import com.zcode.zjw.web.common.Result;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    /**
     * 从配置文件中注入发件人的姓名
     */
    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * Spring官方提供的集成邮件服务的实现类，目前是Java后端发送邮件和集成邮件服务的主流工具。
     */
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 发送文本邮件
     *
     * @param receiver 收件人
     * @param subject  邮件标题
     * @param content  邮件内容
     */
    public Result<?> sendSimpleMail(String receiver, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            // 发件人
            message.setFrom(fromEmail);
            // 收件人
            message.setTo(receiver);
            // 邮件标题
            message.setSubject(subject);
            // 邮件内容
            message.setText(content);

            mailSender.send(message);
            return Result.success();
        } catch (MailException e) {
            e.printStackTrace();
            return Result.error("发送文本邮件失败！");
        }
    }

    /**
     * 发送带附件的邮件
     *
     * @param messageInfo 邮件内容
     */
    public Result<?> sendAttachmentsMail(MessageInfo messageInfo) {
        try {
            String receiver = messageInfo.getReceiver();
            String subject = messageInfo.getSubject();
            String content = messageInfo.getContent();

            MimeMessage message = mailSender.createMimeMessage();
            // 要带附件第二个参数设为true
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            // 发件人
            helper.setFrom(fromEmail);
            // 收件人
            helper.setTo(receiver);
            // 邮件标题
            helper.setSubject(subject);
            // 邮件内容，第二个参数：格式是否为html
            helper.setText(content, true);

            if (messageInfo.getFilePath() != null) {
                String[] filePaths = Optional.of(messageInfo.getFilePath().split(",")).orElse(new String[]{});
                // 添加文件附件
                for (String filePath : filePaths) {
                    FileSystemResource file = new FileSystemResource(new File(filePath));
                    helper.addAttachment(file.getFilename(), file);
                }
            }

            // 添加图片附件
            if (messageInfo.getImgPath() != null) {
                String[] imgPaths = Optional.of(messageInfo.getImgPath().split(",")).orElse(new String[]{});
                for (String imgPath : imgPaths) {
                    FileSystemResource image = new FileSystemResource(new File(imgPath));
                    helper.addAttachment(image.getFilename(), image);
                }
            }

            // 添加其他文件附件
            MultipartFile multipartFile = messageInfo.getMultipartFile();
            if (multipartFile != null) {
                File file = multipartFileToFile(multipartFile);
                if (file != null) {
                    FileSystemResource files = new FileSystemResource(file);
                    helper.addAttachment(files.getFilename(), files);
                }
            }
            MultipartFile[] multipartFiles = messageInfo.getMultipartFiles();
            for (MultipartFile file : multipartFiles) {
                File file1 = multipartFileToFile(file);
                if (file1 != null) {
                    FileSystemResource files = new FileSystemResource(file1);
                    helper.addAttachment(files.getFilename(), files);
                }
            }

            mailSender.send(message);
            return Result.success();
        } catch (MessagingException e) {
            e.printStackTrace();
            return Result.error("发送带附件的邮件失败！");
        }

    }

    /**
     * 把 multiFile 转换为 file
     *
     * @param multiFile
     * @return
     */
    private File multipartFileToFile(MultipartFile multiFile) {
        // 获取文件名
        String fileName = multiFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 若需要防止生成的临时文件重复,可以在文件名后添加随机码
        try {
            File file = File.createTempFile(fileName, prefix);
            multiFile.transferTo(file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送模板邮件
     *
     * @param receiver     收件人
     * @param subject      邮件标题
     * @param content      邮件内容
     * @param fromName     发件人姓名
     * @param receiverName 收件人姓名
     * @return
     */
    public Result<?> sendTemplateMail(String receiver, String subject, String content, String fromName, String receiverName) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 获得模板
        Template template;
        try {
            template = freeMarkerConfigurer.getConfiguration().getTemplate("message.ftl");
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("获取模板失败！");
        }
        // 使用Map作为数据模型，定义属性和值
        Map<String, Object> model = new HashMap<>();
        model.put("fromName", fromName);
        model.put("receiverName", receiverName);
        model.put("content", content);
        model.put("sendTime", simpleDateFormat.format(new Date()));

        // 传入数据模型到模板，替代模板中的占位符，并将模板转化为html字符串
        String templateHtml;
        try {
            templateHtml = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            return Result.error("转换模板失败！");
        }

        // 该方法本质上还是发送html邮件，调用之前发送html邮件的方法
        return sendHtmlMail(receiver, subject, templateHtml);
    }

    /**
     * 发送html邮件
     *
     * @param receiver 收件人
     * @param subject  邮件标题
     * @param content  邮件内容
     */
    public Result<?> sendHtmlMail(String receiver, String subject, String content) {
        try {
            // 注意这里使用的是MimeMessage
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            // 发件人
            helper.setFrom(fromEmail);
            // 收件人
            helper.setTo(receiver);
            // 邮件标题
            helper.setSubject(subject);
            // 邮件内容，第二个参数：格式是否为html
            helper.setText(content, true);
            mailSender.send(message);
            return Result.success();
        } catch (MessagingException e) {
            e.printStackTrace();
            return Result.error("发送html邮件失败！");
        }
    }


}