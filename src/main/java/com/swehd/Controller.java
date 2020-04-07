package com.swehd;

import javafx.fxml.FXML;

import java.io.IOException;

public class Controller {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}
