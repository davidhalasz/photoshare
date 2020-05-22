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
import java.util.Optional;

public class UserController {

    private UserDao userDao;
    public boolean checkUser;

    @FXML
    private Label errorLabel;
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
            if (nameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                if (nameField.getText().isEmpty()) {
                    errorLabel.setText("Username is empty!");
                }
                if (passwordField.getText().isEmpty()) {
                    errorLabel.setText("Password is empty!");
                }
            } else {
                if (!nameField.getText().isEmpty() && !passwordField.getText().isEmpty()){
                    userDao = UserDao.getInstance();
                    Optional<User> user = userDao.findUser(nameField.getText(), passwordField.getText());
                    checkUser = false;

                    if (user == null) {
                        errorLabel.setText("Name and Password not accepted!");
                    } else {
                        System.out.println(user.get());
                    }
                }
            }

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
                App.setRoot("loginwindow");
            }

        } else if (e.getSource().equals(switchToLoginButton)) {
            App.setRoot("loginwindow");
        }
    }
}
