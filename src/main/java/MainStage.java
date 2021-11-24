import database.entities.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainStage {
    final private User user;

    public MainStage(User user) {
        this.user = user;
        try {
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("mainWindow.fxml"));
            Parent root = mainLoader.load();
            MainController mainController = mainLoader.getController();
            mainController.setUser(user);
            Stage stage = new Stage();
            stage.setTitle("Library Management System");
            stage.setScene(new Scene(root, 940, 410));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
