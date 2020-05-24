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

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

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

                    if (user == null) {
                        errorLabel.setText("Name and Password not accepted!");
                    } else {
                        User loggedUser = user.get();
                        loggedUser.setLogged(true);
                        userDao.update(loggedUser);

                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("mainwindow.fxml"));
                            Parent root = fxmlLoader.load();
                            fxmlLoader.<Controller>getController().initdata(loggedUser.getId());
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

    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }


    private User createUser() {
        User user = User.builder()
                .name(regName.getText())
                .email(regEmail.getText())
                .password(regPass.getText())
                .build();
        return user;
    }

    /**
     * Register new user. Write error is some field is empty.
     * @param e
     * @throws IOException
     */
    @FXML
    public void registerWindow(ActionEvent e) throws IOException {

        if (e.getSource().equals(regButton)){
            if (regName.getText().isEmpty() || regEmail.getText().isEmpty() || regPass.getText().isEmpty()){
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
