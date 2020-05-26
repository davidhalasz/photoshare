package com.swehd.controller;

import com.swehd.App;
import com.swehd.user.User;
import com.swehd.user.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

import static com.swehd.user.UserService.isValid;
import static com.swehd.user.UserService.shortThanThree;

@Slf4j
public class UserController {

    private UserDao userDao;

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

    /**
     * Login window.
     * Display error, if any field is empty.
     * If username and password accepted, update logged true.
     * @param e
     * @throws IOException
     */
    @FXML
    public void loginWindow(ActionEvent e) throws IOException {
        if (e.getSource().equals(loginButton)) {
            if (nameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                if (nameField.getText().isEmpty()) {
                    errorLabel.setText("Username is empty!");
                }
                if (passwordField.getText().isEmpty()) {
                    errorLabel.setText("Password is empty!");
                }
            } else {
                if (!nameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
                    userDao = UserDao.getInstance();
                    Optional<User> user = userDao.findUser(nameField.getText(), passwordField.getText());

                    if (user == null) {
                        errorLabel.setText("Name and Password not accepted!");
                    } else {
                        User loggedUser = user.get();
                        loggedUser.setLogged(true);
                        userDao.update(loggedUser);
                        log.info("User is logged.");
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("mainwindow.fxml"));
                            Parent root = fxmlLoader.load();
                            fxmlLoader.<Controller>getController().initdata(user.get());
                            Stage stage = new Stage();
                            stage.setTitle("PhotoShare");
                            stage.setScene(new Scene(root, 489, 710));
                            stage.show();
                            ((Node) e.getSource()).getScene().getWindow();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
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

    /**
     * Register new user. Display error if any fields in a form are empty.
     * Check if email address valid or no.
     * @param e
     * @throws IOException
     */
    @FXML
    public void registerWindow(ActionEvent e) throws IOException {

        if (e.getSource().equals(regButton)) {
            if (regName.getText().isEmpty() || regEmail.getText().isEmpty() || regPass.getText().isEmpty()) {
                if (regName.getText().isEmpty()) {
                    errorLabel.setText("Username cannot be empty!");
                    throw new IllegalArgumentException("Username cannot be empty!");
                } else if (regEmail.getText().isEmpty()) {
                    errorLabel.setText("Email address cannot be empty!");
                    throw new IllegalArgumentException("Email address cannot be empty!");
                } else if (regPass.getText().isEmpty()) {
                    errorLabel.setText("Password cannot be empty!");
                    throw new IllegalArgumentException("Password cannot be empty!");
                }
            } else if (shortThanThree(regName.getText())) {
                errorLabel.setText("Username is too short!");
                throw new IllegalArgumentException("Username is too short!");
            } else if (isValid(regEmail.getText().trim())) {
                    userDao = UserDao.getInstance();
                    userDao.persist(createUser());
                    App.setRoot("loginwindow");
                    log.info("User ( {} ) registered.", regName.getText());
                } else {
                    errorLabel.setText("This is not a valid email address!");
                    throw new IllegalArgumentException("Password cannot be empty!");
                }
        } else if (e.getSource().equals(switchToLoginButton)) {
            App.setRoot("loginwindow");
            log.info("Switch to Login Window");
        }
    }
}
