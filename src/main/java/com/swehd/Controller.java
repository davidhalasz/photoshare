package com.swehd;

import javafx.fxml.FXML;

import java.io.IOException;

public class Controller {

    @FXML
    private void switchToRegister() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("primary");
    }
}
