package com.agony.demo.service;

import com.agony.demo.common.BasePayload;
import com.agony.demo.utils.Exp_list;
import com.agony.demo.utils.Result;
import com.agony.demo.utils.Tools;
import com.github.kevinsawicki.http.HttpRequest;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;

/**
 * @author itchen
 * @date 2024/9/22 16:18
 * @description 批量检测任务类，继承Task任务类，用于批量检测漏洞
 */
public class BatchCheckTask extends Task<Void> {

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private final ComboBox<String> comboBox;
    private final TextArea checkInfo;
    List<String> list_url;

    public BatchCheckTask(List<String> list_url, ComboBox<String> comboBox, TextArea checkInfo) {
        this.list_url = list_url;
        this.comboBox = comboBox;
        this.checkInfo = checkInfo;
    }
    public void loginfo(String info) {
        Platform.runLater(() -> this.checkInfo.appendText(info + "\r\n"));
    }

    /**
     * 任务执行入口
     * @return
     * @throws Exception
     */
    @Override
    protected Void call() throws Exception {
        batch_check_url();
        return null;
    }
    void batch_check_url() throws Exception {
        String vul_version = comboBox.getValue();
        if (list_url.size() > 0) {
            if (vul_version.startsWith("ALL")) {
                ArrayList<String> versions = (ArrayList<String>) Exp_list.get_expList();
                for (int j = 0; j < list_url.size(); j++) {
                    loginfo("检测URL " + list_url.get(j) + "中......");

//                    if (!Tools.check_url(list_url.get(j))){
//                        loginfo(list_url.get(j) + "未存活,跳过!");
//                        continue;
//                    }
                    try {
                        HttpRequest.get(list_url.get(j)).connectTimeout(5000).code();
                    } catch (Exception e) {
                        loginfo(list_url.get(j) + " 未存活,跳过!");
                        loginfo("=====================================================================");

                        continue;
                    }
                    for (String v : versions) {
                        BasePayload bp = Tools.getPayload(v);
                        Result vul = bp.checkVUL(list_url.get(j));
                        if (vul.isRes()) {
                            loginfo("[+] 存在" + vul.getVuln());
                            loginfo("Payload: " + vul.getPayload());
                        } else {
                            loginfo("[-] 不存在" + vul.getVuln());
                        }
                    }
                    loginfo("=====================================================================");

                }
            }else {
                BasePayload bp = Tools.getPayload(vul_version);
                for (int j = 0; j < list_url.size(); j++) {
                    if (j!=0) {
                        loginfo("");
                    }
                    loginfo("检测URL " + list_url.get(j) + "中......");

//                    if (!Tools.check_url(list_url.get(j))){
//                        loginfo(list_url.get(j) + "未存活,跳过!");
//                        continue;
//                    }
                    try {
                        HttpRequest.get(list_url.get(j)).connectTimeout(5000).code();
                    } catch (Exception e) {
                        loginfo(list_url.get(j) + " 未存活,跳过!");
                        loginfo("=====================================================================");

                        continue;
                    }
                    Result isvul = bp.checkVUL(list_url.get(j));
                    if (isvul.isRes()) {
                        loginfo("[+] 存在" + isvul.getVuln());
                        loginfo("Payload: " + isvul.getPayload());
                    } else {
                        loginfo("[-] 不存在" + isvul.getVuln());
                    }
                    loginfo("=====================================================================");

                }
            }

        } else {
//            JOptionPane.showMessageDialog(null, "请先导入URL！", "URL批量检查", JOptionPane.WARNING_MESSAGE);
            alert.setTitle("提示:");
            alert.setHeaderText("URL批量检查");
            alert.setContentText("请先导入URL！");
            alert.showAndWait();
        }
    }
    @Override
    protected void succeeded() {
        Platform.runLater(() -> {
            // 在任务完成时执行的代码，比如弹窗提示用户任务已经完成。
            alert.setTitle("提示:");
            alert.setHeaderText("任务提示");
            alert.setContentText("批量检测任务执行完成");
            alert.showAndWait();
        });
    }
}
