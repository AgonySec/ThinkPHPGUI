package com.agony.demo.controller;

import com.agony.demo.service.BatchCheckTask;
import com.agony.demo.service.CheckTask;
import com.agony.demo.common.BasePayload;
import com.agony.demo.utils.Exp_list;
import com.agony.demo.utils.Result;
import com.agony.demo.utils.Tools;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class TpController {
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Button chech_vul_btn,import_btn;


    @FXML
    private TextField url,command,file_txt;
    @FXML
    private TextArea checkInfo,commandInfo;
    public static Map<String, String> currentProxy = new HashMap<>();

    List<String> list_url = new ArrayList();
    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    /**
     * 初始化下拉框,这个方法会自己运行，我也不知道什么原理服了，有这个方法就很方便。。。。
     */
    public void initialize() {
        comboBox.setValue("ALL");
        comboBox.getItems().add("ALL");
        comboBox.getItems().addAll(Exp_list.get_expList());
    }


    public void loginfo(String info) {
//        checkInfo.appendText(info + "\r\n");
        Platform.runLater(() -> this.checkInfo.appendText(info + "\r\n"));
    }

    public void logcmd(String info) {
//        commandInfo.appendText(info );
        Platform.runLater(() -> this.commandInfo.appendText(info));
    }

    /**
     * 检测漏洞事件
     * @param event
     * @throws Exception
     */

    @FXML
    void CheckVul(ActionEvent event) throws Exception {
        // 设置按钮 chech_vul_btn 为不可用状态，即禁用该按钮。
        chech_vul_btn.setDisable(true);

        // 1.检测url书写是否有问题
        String url_text = url.getText();
        if(Objects.equals(url_text, "")){
            alert.setTitle("提示:");
            alert.setHeaderText("URL填写提示");
            alert.setContentText("注意：URL不能为空");
            alert.showAndWait();
        }
        boolean check_url = Tools.checkTheURL(url_text);
        if (!check_url){
            checkInfo.setText("URL书写有问题，请检查，示例：http://127.0.0.1:7001");
            chech_vul_btn.setDisable(false);
            return;
        }

        // 2.检测漏洞
        // 创建 一个线程，并启动，自动执行设置的任务
        CheckTask ct =new CheckTask(comboBox, url, checkInfo);
        new Thread(ct).start();

        // 设置按钮 chech_vul_btn 为启用状态，即启用该按钮。
        chech_vul_btn.setDisable(false);


    }

    /**
     * 命令执行
     * @param event
     * @throws Exception
     */
    @FXML
    void Command(ActionEvent event) throws Exception {
        chech_vul_btn.setDisable(true);
        String url_text = url.getText();
        String vul_version = comboBox.getValue();
        String cmd = command.getText();
        String res = null;

        if (vul_version.startsWith("ALL")) {
//            JOptionPane.showMessageDialog(null, "请选择漏洞对应版本!", "信息", JOptionPane.WARNING_MESSAGE);
            alert.setTitle("提示:");
            alert.setHeaderText("信息");
            alert.setContentText("请选择漏洞对应版本!");
            alert.showAndWait();
        } else {
            BasePayload bp = Tools.getPayload(vul_version);
            Result vul = bp.exeVUL(url_text, cmd);
            if (vul.isRes()) {
                res = vul.getPayload();
                logcmd("[+] " + res);
            } else {
//                JOptionPane.showMessageDialog(null, "命令执行失败!", "信息", JOptionPane.WARNING_MESSAGE);
                alert.setTitle("提示:");
                alert.setHeaderText("信息");
                alert.setContentText("命令执行失败!");
                alert.showAndWait();
            }
        }
        chech_vul_btn.setDisable(false);

    }

    /**
     * 一键getshell
     * @param event
     * @throws Exception
     */
    @FXML
    void getShell(ActionEvent event) throws Exception {
        chech_vul_btn.setDisable(true);
        String url_text = url.getText();
        String vul_version = comboBox.getValue();
        String res = null;
        boolean check_url = Tools.checkTheURL(url_text);
        if(url_text.equals("")){
            // 对话框写法
            // 第一种写法 javafx的写法
            alert.setTitle("提示:");
            alert.setHeaderText("URL填写提示");
            alert.setContentText("注意：URL不能为空");
            alert.showAndWait();
            // 第二种写法 swing的方法就不用了，虽然简单
//            JOptionPane.showMessageDialog(null, "注意：URL不能为空", "URL填写提示", JOptionPane.INFORMATION_MESSAGE);

        }
        if (!check_url){
            checkInfo.setText("URL书写有问题，请检查，示例：http://127.0.0.1:7001");
            chech_vul_btn.setDisable(false);
            return;
        }
        if (vul_version.startsWith("ALL")) {
//            JOptionPane.showMessageDialog(null, "请选择漏洞对应版本!", "信息", JOptionPane.WARNING_MESSAGE);
            alert.setTitle("提示:");
            alert.setHeaderText("信息");
            alert.setContentText("请选择漏洞对应版本!");
            alert.showAndWait();
        } else {
            BasePayload bp = Tools.getPayload(vul_version);
            Result vul = bp.getShell(url_text);
            if (vul.isRes()) {
                res = vul.getPayload();
                loginfo("[+] " + res);
            } else {
//                JOptionPane.showMessageDialog(null, "Getshell失败", "信息", JOptionPane.WARNING_MESSAGE);
                alert.setTitle("提示:");
                alert.setHeaderText("信息");
                alert.setContentText("Getshell失败");
                alert.showAndWait();
            }
        }
        chech_vul_btn.setDisable(false);

    }



    /**
     * 批量导入url
     * @param event
     */
    @FXML
    void batch_import_url(ActionEvent event) throws IOException {
//        JFileChooser jf = new JFileChooser();
//        jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
//        jf.showDialog(new JLabel(), "选择");
//        File file = jf.getSelectedFile();

        Stage stage = (Stage) import_btn.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select One URL File");
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            String file_path = file.getAbsolutePath();
            file_txt.setText(file_path);
            list_url = Tools.read_file(file_path);
//            System.out.println(list_url);
            alert.setTitle("提示:");
            alert.setHeaderText("导入提示");
            alert.setContentText("成功导入" + list_url.size() + "个URL！");
            alert.showAndWait();

//            JOptionPane.showMessageDialog(null, "成功导入" + list_url.size() + "个URL！", "导入URL", JOptionPane.INFORMATION_MESSAGE);

        }
    }

    /**
     * 批量检测url
     * @param event
     */
    @FXML
    void batch_check_url(ActionEvent event) throws Exception {
        chech_vul_btn.setDisable(true);
        BatchCheckTask bct = new BatchCheckTask(list_url, comboBox, checkInfo);
        new Thread(bct).start();
        chech_vul_btn.setDisable(false);

    }

    /**
     * 清空
     * @param event
     */
    @FXML
    void clear(ActionEvent event) {
        checkInfo.setText("");
        commandInfo.setText("");
    }

    /**
     * 代理设置
     * @param event
     */
    @FXML
    void proxy_set(ActionEvent event) {
        final Alert inputDialog = new Alert(Alert.AlertType.NONE);
        inputDialog.setResizable(true);
        final Window window = inputDialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> window.hide());

        ToggleGroup statusGroup = new ToggleGroup();
        RadioButton enableRadio = new RadioButton("启用");
        RadioButton disableRadio = new RadioButton("禁用");
        enableRadio.setToggleGroup(statusGroup);
        disableRadio.setToggleGroup(statusGroup);
        HBox statusHbox = new HBox(10.0D, enableRadio, disableRadio);
        GridPane proxyGridPane = new GridPane();
        proxyGridPane.setVgap(15.0D);
        proxyGridPane.setPadding(new Insets(20.0D, 20.0D, 0.0D, 10.0D));
        Label typeLabel = new Label("类型：");
        Label type = new Label("HTTP");
        Label IPLabel = new Label("IP地址：");
        TextField IPText = new TextField();
        Label PortLabel = new Label("端口：");
        TextField PortText = new TextField();
        Label userNameLabel = new Label("用户名：");
        TextField userNameText = new TextField();
        Label passwordLabel = new Label("密码：");
        TextField passwordText = new TextField();
        Button cancelBtn = new Button("取消");
        Button saveBtn = new Button("保存");
        saveBtn.setDefaultButton(true);

        // Set values if currentProxy is not null
        IPText.setText( currentProxy.getOrDefault("ipAddress", ""));
        PortText.setText(currentProxy.getOrDefault("port", ""));
        userNameText.setText(currentProxy.getOrDefault("username", ""));
        passwordText.setText(currentProxy.getOrDefault("password", ""));
        enableRadio.setSelected(currentProxy.get("proxy") != null && currentProxy.get("proxy").equals("Y"));

        saveBtn.setOnAction(e -> {
            if (disableRadio.isSelected()) {
                currentProxy.put("proxy", "N");
                Tools.removeGlobalProxy();
            } else {
                String ipAddress = IPText.getText().trim();
                String port = PortText.getText().trim();
                String username = userNameText.getText().trim();
                String password = passwordText.getText().trim();
                if (!username.isEmpty()) {
                    Tools.setGlobalProxy(ipAddress, port, username, password);
                } else {
                    Tools.setGlobalProxy(ipAddress, port);
                }
                currentProxy.put("ipAddress", ipAddress);
                currentProxy.put("port", port);
                currentProxy.put("username", username);
                currentProxy.put("password", password);
                currentProxy.put("proxy", "Y");
            }
            inputDialog.getDialogPane().getScene().getWindow().hide();
        });

        cancelBtn.setOnAction(e -> inputDialog.getDialogPane().getScene().getWindow().hide());

        proxyGridPane.add(statusHbox, 1, 0);
        proxyGridPane.add(typeLabel, 0, 1);
        proxyGridPane.add(type, 1, 1);
        proxyGridPane.add(IPLabel, 0, 2);
        proxyGridPane.add(IPText, 1, 2);
        proxyGridPane.add(PortLabel, 0, 3);
        proxyGridPane.add(PortText, 1, 3);
        proxyGridPane.add(userNameLabel, 0, 4);
        proxyGridPane.add(userNameText, 1, 4);
        proxyGridPane.add(passwordLabel, 0, 5);
        proxyGridPane.add(passwordText, 1, 5);
        HBox buttonBox = new HBox(20.0D, cancelBtn, saveBtn);
        buttonBox.setAlignment(Pos.CENTER);
        GridPane.setColumnSpan(buttonBox, 2);
        proxyGridPane.add(buttonBox, 0, 6);
        inputDialog.getDialogPane().setContent(proxyGridPane);
        inputDialog.showAndWait();
    }

    /**
     * 关于
     * @param event
     */
    @FXML
    void about(ActionEvent event) {
        alert.setTitle("提示:");
        alert.setHeaderText("by Agony");
        alert.setContentText("博主使用javafx重新写的UI,增添了最新的poc,优化代码结构!");
        alert.showAndWait();
    }
}