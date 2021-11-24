import database.entities.Authentication.Authentication;
import database.entities.OnLogin;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class LoginController {
    @FXML
    private TextField userNameField, passwordField;
    @FXML
    private Button loginButton;

    @FXML
    private CheckBox rememberMe;
    private OnLogin onLogin;

    public LoginController() {
    }

    @FXML
    public void initialize() {

    }

    public void setFields(String userName, String password) {
        userNameField.setText(userName);
        passwordField.setText(password);
    }

    public void setLoginCallback(OnLogin onLogin) {
        this.onLogin = onLogin;
    }

    private void saveToFile(String userName, String password) {
        File file = new File("data.txt");
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(userName.getBytes(StandardCharsets.UTF_8));
            outputStream.write("\n".getBytes(StandardCharsets.UTF_8));
            outputStream.write(password.getBytes(StandardCharsets.UTF_8));
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onLoginClicked(javafx.event.ActionEvent actionEvent) {
        String userName = userNameField.getText();
        String password = passwordField.getText();
        if (rememberMe.isSelected()) {
            saveToFile(userName, password);
        }
        Authentication authentication = new Authentication(userName, password);
        if (authentication.isAuthenticated()) {
            onLogin.onLoginSuccess(authentication.getUser());
        } else onLogin.onLoginFailed();
    }
}
