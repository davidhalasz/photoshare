module com.swehd {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.swehd to javafx.fxml;
    exports com.swehd;
}