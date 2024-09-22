package com.agony.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class TpApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(TpApplication.class.getResource("tp-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 766, 661);
//        ComboBox<String> comboBox = (ComboBox<String>) fxmlLoader.getNamespace().get("comboBox");
//        comboBox.setItems(Exp_list.get_exp());
//        comboBox.setValue("ALL");
        stage.setTitle("Thinkphp漏洞检测工具 by Agony");
        stage.setScene(scene);

        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }
}