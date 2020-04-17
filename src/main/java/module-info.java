module com.swehd {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;

    opens com.swehd to javafx.fxml;
    opens com.swehd.controller to javafx.fxml;
    exports com.swehd;
    exports com.swehd.controller;
}