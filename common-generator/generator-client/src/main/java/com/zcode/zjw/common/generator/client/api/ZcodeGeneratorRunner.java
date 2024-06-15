package com.zcode.zjw.common.generator.client.api;

import com.zcode.zjw.common.generator.client.common.ConfigUtil;
import com.zcode.zjw.common.generator.client.common.SpringGenerator;
import com.zcode.zjw.common.generator.client.common.TemplateUtil;
import com.zcode.zjw.common.generator.client.controller.IndexController;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.net.URL;
import java.util.*;

/**
 * Zcode代码生成器（客户端）运行者
 *
 * @author zhangjiwei
 * @since 2023/8/29
 */
public class ZcodeGeneratorRunner extends Application {

    private final static Logger LOG = Logger.getLogger(ZcodeGeneratorRunner.class.getName());
    /**
     * 国际化控件的文字
     */
    public static Map<String, StringProperty> LANGUAGE = new HashMap<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        ConfigUtil.existsConfigDB();// 创建配置文件
        // LanguageUtil.existsTemplate();// 国际化文件夹创建
        TemplateUtil.existsTemplate();// 创建模板
        loadLanguage(Locale.getDefault());// 加载本地语言资源
        // loadLanguage(Locale.ENGLISH);// 加载英语资源

        URL url = Thread.currentThread().getContextClassLoader().getResource("FXML/Index.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent root = fxmlLoader.load();
        primaryStage.setResizable(true);
        primaryStage.setTitle(SpringGenerator.NAME_VERSION);
        primaryStage.getIcons().add(new Image("image/icon.png"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        IndexController controller = fxmlLoader.getController();
        controller.setPrimaryStage(primaryStage);
    }

    /**
     * 根据Locale加载控件文本
     *
     * @param locale
     */
    public static void loadLanguage(Locale locale) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("config/language/language", locale);
        Enumeration<String> keys = resourceBundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (LANGUAGE.get(key) == null) {
                LANGUAGE.put(key, new SimpleStringProperty(resourceBundle.getString(key)));
            } else {
                LANGUAGE.get(key).set(resourceBundle.getString(key));
            }
        }
    }

    /**
     * 启动方法
     *
     * @param args 主方法启动参数
     */
    public static void startRun(String[] args) {
        URL logResource = Thread.currentThread().getContextClassLoader().getResource("config/log4j.properties");
        PropertyConfigurator.configure(logResource);
        try {
            LOG.debug("运行Zcode-Generator...");
            launch(args);
            LOG.debug("关闭Zcode-Generator!!!");
        } catch (Exception e) {
            LOG.error("运行Zcode-Generator-->失败:", e);
        }
    }

}
