package Functionalities;

import database.entities.ISimpleString;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.List;

public class Utils {
    public static String listToString(List<? extends ISimpleString> list, char separator) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ISimpleString o : list) {
            stringBuilder.append(o.getSimpleString());
            stringBuilder.append(separator);
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    public static Pair<Stage, FXMLLoader> createStage(String resource, String title, int width, int height) throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(Utils.class.getResource("../" + resource));
        Parent root = mainLoader.load();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root, width, height));
        return new Pair<>(stage, mainLoader);
    }
}
