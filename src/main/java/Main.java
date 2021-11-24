import com.itextpdf.text.DocumentException;
import database.entities.OnLogin;
import database.entities.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        LoginController loginController = loader.getController();
        loginController.setLoginCallback(new OnLogin() {
            @Override
            public void onLoginSuccess(User user) {
                primaryStage.close();
                MainStage mainStage = new MainStage(user);
            }

            @Override
            public void onLoginFailed() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login Failed");
                alert.setHeaderText("The username or password you've provided is incorrect");
                alert.showAndWait();
            }
        });
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 450, 195));
        primaryStage.show();
        File file = new File("data.txt");
        if (file.exists()) {
            BufferedReader fileInputStream = new BufferedReader(new FileReader(file));
            String userName = fileInputStream.readLine();
            String password = fileInputStream.readLine();
            fileInputStream.close();
            loginController.setFields(userName, password);
        }
    }


    public static void main(String[] args) throws FileNotFoundException, DocumentException {
        launch(args);
    }
}
