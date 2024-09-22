package com.agony.demo.service;

import com.agony.demo.common.BasePayload;
import com.agony.demo.utils.Exp_list;
import com.agony.demo.utils.Result;
import com.agony.demo.utils.Tools;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;

/**
 * @author itchen
 * @date 2024/9/22 16:17
 * @description 使用 Task 封装一个多线程任务
 */
public class CheckTask extends Task<Void> {

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private final ComboBox<String> comboBox;
    private final TextField url;
    private final TextArea checkInfo;
    public void loginfo(String info) {
        Platform.runLater(() -> this.checkInfo.appendText(info + "\r\n"));
    }
    /**
     * 构造方法
     */
    public CheckTask(ComboBox<String> comboBox, TextField url, TextArea checkInfo) {
        this.comboBox = comboBox;
        this.url = url;
        this.checkInfo = checkInfo;
    }

    /**
     * 任务执行入口，会自动执行call方法
     * @return
     * @throws Exception
     */
    @Override
    protected Void call() throws Exception {
        CheckVul();
        return null;
    }

    void CheckVul() throws Exception {
        String url = this.url.getText();
        String version = this.comboBox.getSelectionModel().getSelectedItem();

        if (version.startsWith("ALL")) {
            loginfo("检测所有漏洞中......");
            loginfo("=====================================================================");
            ArrayList<String> versions = (ArrayList<String>) Exp_list.get_expList();

            for (String v : versions) {
                BasePayload bp = Tools.getPayload(v);
                Result vul = bp.checkVUL(url);
                if (vul.isRes()) {
                    loginfo("[+] 存在" + vul.getVuln());
                    loginfo("Payload: " + vul.getPayload());
                } else {
                    loginfo("[-] 不存在" + vul.getVuln());
                }
            }
        } else {
            loginfo("检测漏洞 " + version + "中......");
            loginfo("=====================================================================");
            BasePayload bp = Tools.getPayload(version);
            Result vul = bp.checkVUL(url);
            if (vul.isRes()) {
                loginfo("[+] 存在" + vul.getVuln());
                loginfo("Payload: " + vul.getPayload());
            } else {
                loginfo("[-] 不存在" + vul.getVuln());
            }
        }
    }
    @Override
    protected void succeeded() {
        Platform.runLater(() -> {
            // 在任务完成时执行的代码，比如弹窗提示用户任务已经完成。
            alert.setTitle("提示:");
            alert.setHeaderText("任务提示");
            alert.setContentText("检测任务执行完成");
            alert.showAndWait();
        });
    }
}
