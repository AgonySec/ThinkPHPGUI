module com.agony.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires http.request;
    requires java.desktop;


    opens com.agony.demo to javafx.fxml;
    exports com.agony.demo;
    exports com.agony.demo.controller;
    opens com.agony.demo.controller to javafx.fxml;
}