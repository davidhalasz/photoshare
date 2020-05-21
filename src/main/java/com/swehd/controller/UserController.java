package com.swehd.controller;

import com.swehd.App;
import com.swehd.user.User;
import com.swehd.user.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class UserController {

    private UserDao userDao;

    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button switchToRegButton;


    @FXML
    public void loginWindow(ActionEvent e) throws IOException {
        if (e.getSource().equals(loginButton)){
            System.out.println("Your name is " + nameField.getText());
            System.out.println("Your name password is " + passwordField.getText());
        } else if (e.getSource().equals(switchToRegButton)) {
            App.setRoot("registerwindow");
        }
    }


    @FXML
    private TextField regName;
    @FXML
    private TextField regEmail;
    @FXML
    private TextField regPass;
    @FXML
    private Button regButton;
    @FXML
    private Button switchToLoginButton;
    @FXML
    private Label errorLabel;

    private User createUser() {
        User user = User.builder()
                .name(regName.getText())
                .email(regEmail.getText())
                .password(regPass.getText())
                .build();
        return user;
    }


    @FXML
    public void registerWindow(ActionEvent e) throws IOException {

        if (e.getSource().equals(regButton)){
            if (regName.getText().isEmpty() || regEmail.getText().isEmpty() || regPass.getText().isEmpty()) {
                if (regName.getText().isEmpty()) {
                    errorLabel.setText("Username is empty!");
                } else if (regEmail.getText().isEmpty()) {
                    errorLabel.setText("Email is empty!");
                } else if (regPass.getText().isEmpty()) {
                    errorLabel.setText("Username is empty!");
                }
            } else {
                userDao = UserDao.getInstance();
                userDao.persist(createUser());
                System.out.println(createUser());
            }

        } else if (e.getSource().equals(switchToLoginButton)) {
            App.setRoot("loginwindow");
        }
    }
}
