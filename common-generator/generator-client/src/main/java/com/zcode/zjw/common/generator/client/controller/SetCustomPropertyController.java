package com.zcode.zjw.common.generator.client.controller;

import com.zcode.zjw.common.generator.client.api.ZcodeGeneratorRunner;
import com.zcode.zjw.common.generator.client.common.ConfigUtil;
import com.zcode.zjw.common.generator.client.common.Constant;
import com.zcode.zjw.common.generator.client.common.LanguageKey;
import com.zcode.zjw.common.generator.client.common.StrUtil;
import com.zcode.zjw.common.generator.client.models.TableAttributeKeyValue;
import com.zcode.zjw.common.generator.client.models.TableAttributeKeyValueEditingCell;
import com.zcode.zjw.common.generator.client.options.CustomPropertyConfig;
import com.zcode.zjw.common.generator.client.view.AlertUtil;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Assist属性的配置文件
 *
 * @author zhangjiwei
 */
public class SetCustomPropertyController extends BaseController {
    private Logger LOG = Logger.getLogger(this.getClass());
    /**
     * 首页的控制器
     */
    private IndexController indexController;
    /**
     * 存储信息table里面的所有属性
     */
    private ObservableList<TableAttributeKeyValue> tblPropertyValues;

    // =======================控件区域===========================
    /**
     * 提示语句
     */
    @FXML
    private Label lblTips;
    /**
     * 添加自定义属性
     */
    @FXML
    private Label lblAddCustomProperty;
    /**
     * 描述
     */
    @FXML
    private Label lblDescribe;
    /**
     * 属性表
     */
    @FXML
    private TableView<TableAttributeKeyValue> tblProperty;
    /**
     * 属性表的key列
     */
    @FXML
    private TableColumn<TableAttributeKeyValue, String> tdKey;
    /**
     * 属性表的value列
     */
    @FXML
    private TableColumn<TableAttributeKeyValue, String> tdValue;
    /**
     * 属性表的value列
     */
    @FXML
    private TableColumn<TableAttributeKeyValue, String> tdDescribe;
    /**
     * 自定义key输入框
     */
    @FXML
    private TextField txtKey;
    /**
     * 自定义value输入框
     */
    @FXML
    private TextField txtValue;
    /**
     * 自定义描述输入框
     */
    @FXML
    private TextField txtDescribe;
    /**
     * 添加自定义属性按钮
     */
    @FXML
    private Button btnAddProperty;
    /**
     * 保存配置文件
     */
    @FXML
    private Button btnSaveConfig;
    /**
     * 确定按钮
     */
    @FXML
    private Button btnConfirm;
    /**
     * 取消按钮
     */
    @FXML
    private Button btnCancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tblProperty.setEditable(true);
        tblProperty.setStyle("-fx-font-size:14px");
        StringProperty property = ZcodeGeneratorRunner.LANGUAGE.get(LanguageKey.SET_TBL_TIPS);
        String title = property == null ? "可以在右边自定义添加属性..." : property.get();
        tblProperty.setPlaceholder(new Label(title));
        tblPropertyValues = FXCollections.observableArrayList();
        // 设置列的大小自适应
        tblProperty.setColumnResizePolicy(resize -> {
            double width = resize.getTable().getWidth();
            tdKey.setPrefWidth(width / 3);
            tdValue.setPrefWidth(width / 3);
            tdDescribe.setPrefWidth(width / 3);
            return true;
        });
        btnConfirm.widthProperty().addListener(w -> {
            double x = btnConfirm.getLayoutX() + btnConfirm.getWidth() + 10;
            btnCancel.setLayoutX(x);
        });
    }

    /**
     * 初始化
     */
    public void init() {
        LOG.debug("初始化SetCustomPropertyController...");
        LOG.debug("初始化SetCustomPropertyController->初始化属性...");
        // 添加右键删除属性
        StringProperty property = ZcodeGeneratorRunner.LANGUAGE.get(LanguageKey.SET_TBL_MENU_ITEM_DELETE);
        String delMenu = property.get() == null ? "删除该属性" : property.get();
        MenuItem item = new MenuItem(delMenu);
        item.setOnAction(event -> {
            TableViewSelectionModel<TableAttributeKeyValue> model = tblProperty.selectionModelProperty().get();
            StringProperty delConfirmP = ZcodeGeneratorRunner.LANGUAGE.get(LanguageKey.SET_TBL_MENU_ITEM_DELETE_CONFIRM);
            String delConfirm = delConfirmP.get() == null ? "确定删除该属性吗" : delConfirmP.get();
            boolean isDel = AlertUtil.showConfirmAlert(delConfirm);
            if (isDel) {
                tblPropertyValues.remove(model.getSelectedItem());
            }
        });
        ContextMenu menu = new ContextMenu(item);
        Property<ContextMenu> tblCM = new SimpleObjectProperty<ContextMenu>(menu);
        tblProperty.contextMenuProperty().bind(tblCM);
        // 添加列
        Callback<TableColumn<TableAttributeKeyValue, String>, TableCell<TableAttributeKeyValue, String>> cellFactory = (
                TableColumn<TableAttributeKeyValue, String> p) -> new TableAttributeKeyValueEditingCell();
        tdKey.setCellValueFactory(new PropertyValueFactory<>("key"));
        tdKey.setCellFactory(cellFactory);
        tdKey.setOnEditCommit((CellEditEvent<TableAttributeKeyValue, String> t) -> {
            ((TableAttributeKeyValue) t.getTableView().getItems().get(t.getTablePosition().getRow())).setKey(t.getNewValue());
        });
        tdValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        tdValue.setCellFactory(cellFactory);
        tdValue.setOnEditCommit((CellEditEvent<TableAttributeKeyValue, String> t) -> {
            System.out.println(t.getNewValue());
            ((TableAttributeKeyValue) t.getTableView().getItems().get(t.getTablePosition().getRow())).setValue(t.getNewValue());
        });
        tdDescribe.setCellValueFactory(new PropertyValueFactory<>("describe"));
        tdDescribe.setCellFactory(cellFactory);
        tdDescribe.setOnEditCommit((CellEditEvent<TableAttributeKeyValue, String> t) -> {
            System.out.println(t.getNewValue());
            ((TableAttributeKeyValue) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDescribe(t.getNewValue());
        });
        tblProperty.setItems(tblPropertyValues);
        LOG.debug("初始化SetCustomPropertyController->初始化配置信息...");
        if (indexController.getHistoryConfig() != null) {
            if (indexController.getHistoryConfig().getCustomPropertyConfig() == null) {
                loadConfig(getConfig());
            } else {
                loadConfig(indexController.getHistoryConfig().getCustomPropertyConfig());
            }
        } else {
            String configName = indexController.getHistoryConfigName();
            if (StrUtil.isNullOrEmpty(configName)) {
                configName = Constant.DEFAULT;
            }
            loadConfig(getConfig(configName));
        }
        initLanguage();
        LOG.debug("初始化SetCustomPropertyController-->成功!");
    }

    /**
     * 初始化语言
     */
    private void initLanguage() {
        lblTips.textProperty().bind(ZcodeGeneratorRunner.LANGUAGE.get(LanguageKey.SET_LBL_TIPS));
        tdDescribe.textProperty().bind(ZcodeGeneratorRunner.LANGUAGE.get(LanguageKey.SET_TD_DESCRIBE));
        lblAddCustomProperty.textProperty().bind(ZcodeGeneratorRunner.LANGUAGE.get(LanguageKey.SET_LBL_ADD_CUSTOM_PROPERTY));
        lblDescribe.textProperty().bind(ZcodeGeneratorRunner.LANGUAGE.get(LanguageKey.SET_LBL_DESCRIBE));
        btnSaveConfig.textProperty().bind(ZcodeGeneratorRunner.LANGUAGE.get(LanguageKey.SET_BTN_SAVE_CONFIG));
        btnAddProperty.textProperty().bind(ZcodeGeneratorRunner.LANGUAGE.get(LanguageKey.SET_BTN_ADD_PROPERTY));
        btnConfirm.textProperty().bind(ZcodeGeneratorRunner.LANGUAGE.get(LanguageKey.SET_BTN_CONFIRM));
        btnCancel.textProperty().bind(ZcodeGeneratorRunner.LANGUAGE.get(LanguageKey.SET_BTN_CANCEL));
        txtKey.promptTextProperty().bind(ZcodeGeneratorRunner.LANGUAGE.get(LanguageKey.SET_TXT_KEY));
        txtValue.promptTextProperty().bind(ZcodeGeneratorRunner.LANGUAGE.get(LanguageKey.SET_TXT_VALUE));
        txtDescribe.promptTextProperty().bind(ZcodeGeneratorRunner.LANGUAGE.get(LanguageKey.SET_TXT_DESCRIBE));
    }

    /**
     * 从数据库中获取配置文件,使用默认值获取
     *
     * @return
     */
    public CustomPropertyConfig getConfig() {
        return getConfig(Constant.DEFAULT);
    }

    /**
     * 从数据库中获取配置文件
     *
     * @param name
     * @return
     */
    public CustomPropertyConfig getConfig(String name) {
        LOG.debug("执行从数据库中获取配置文件...");
        try {
            CustomPropertyConfig config = ConfigUtil.getCustomPropertyConfig(name);
            LOG.debug("执行获取配置文件-->成功!");
            if (config != null) {
                return config;
            }
        } catch (Exception e) {
            LOG.error("执行从数据库中获取配置文件-->失败:", e);
            AlertUtil.showErrorAlert("执行获得配置文件-->失败:" + e);
        }
        return new CustomPropertyConfig().initDefaultValue();
    }

    /**
     * 获取当前控制器配置文件
     *
     * @param name
     * @return
     */
    public CustomPropertyConfig getThisConfig() {
        LOG.debug("执行获取当前页面配置文件...");
        CustomPropertyConfig config = new CustomPropertyConfig(tblPropertyValues);
        LOG.debug("执行获取当前页面配置文件-->成功!");
        return config;
    }

    /**
     * 加载配置文件到当前页面
     *
     * @param config
     */
    public void loadConfig(CustomPropertyConfig config) {
        LOG.debug("执行加载配置文件到当前页面...");
        tblPropertyValues.clear();
        if (config != null && config.getTableItem() != null) {
            config.getTableItem().forEach(v -> {
                tblPropertyValues.add(new TableAttributeKeyValue(v.getKey(), v.getValue(), v.getDescribe()));
            });
        }
        LOG.debug("执行加载配置文件到当前页面->成功!");
    }

    // =======================事件=================================

    /**
     * 保存配置
     *
     * @param event
     */
    public void onSaveConfig(ActionEvent event) {
        LOG.debug("执行将配置文件保存到数据库...");
        try {
            String configName = indexController.getHistoryConfigName();
            if (StrUtil.isNullOrEmpty(configName)) {
                configName = Constant.DEFAULT;
            }
            ConfigUtil.saveCustomPropertyConfig(getThisConfig(), configName);
            LOG.debug("执行将配置文件保存到数据库-->成功!");
            AlertUtil.showInfoAlert("保存配置信息成功!");
        } catch (Exception e) {
            LOG.error("执行将配置文件保存到数据库-->失败:", e);
            AlertUtil.showErrorAlert("执行获得配置文件-->失败:" + e);
        }
    }

    /**
     * 添加自定义属性
     *
     * @param event
     */
    public void onAddPropertyToTable(ActionEvent event) {
        LOG.debug("执行添加自定义属性...");
        TableAttributeKeyValue attribute = new TableAttributeKeyValue(txtKey.getText(), txtValue.getText(), txtDescribe.getText());
        tblPropertyValues.add(attribute);
        LOG.debug("添加自定义属性-->成功!");
    }

    /**
     * 取消关闭该窗口
     *
     * @param event
     */
    public void onCancel(ActionEvent event) {
        StringProperty property = ZcodeGeneratorRunner.LANGUAGE.get(LanguageKey.SET_BTN_CANCEL_TIPS);
        String tips = property == null ? "如果取消,全部的设置都将恢复到默认值,确定取消吗?" : property.get();
        boolean result = AlertUtil.showConfirmAlert(tips);
        if (result) {
            getDialogStage().close();
        }
    }

    /**
     * 确定事件
     *
     * @param event
     */
    public void onConfirm(ActionEvent event) {
        indexController.getHistoryConfig().setCustomPropertyConfig(getThisConfig());
        getDialogStage().close();
    }

    // ==================get/set=======================

    /**
     * 获得首页控制器
     *
     * @return
     */
    public IndexController getIndexController() {
        return indexController;
    }

    /**
     * 设置首页控制器
     *
     * @param indexController
     */
    public void setIndexController(IndexController indexController) {
        this.indexController = indexController;
    }

}