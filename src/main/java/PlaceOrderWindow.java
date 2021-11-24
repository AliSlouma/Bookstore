import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class PlaceOrderWindow {
    @FXML
    private TextField orderNo;
    @FXML
    private TextField ISBN;
    @FXML
    private TextField dateOut;
    @FXML
    private TextField quantityOrdered;
    @FXML
    private Button order;
    @FXML
    private Button cancel;
    public ArrayList<String> result;

    @FXML
    public void initialize() {
        result = new ArrayList<>();
    }

    public void onOrderClick(ActionEvent actionEvent) {
        if (orderNo.getCharacters().toString().equals("") ||
                ISBN.getCharacters().toString().equals("")
                || dateOut.getCharacters().toString().equals("")
                || quantityOrdered.getCharacters().toString().equals("") ){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("some text fields are empty");
            a.show();
        }
        else {
            result.add(orderNo.getCharacters().toString());
            result.add(ISBN.getCharacters().toString());
            result.add(dateOut.getCharacters().toString());
            result.add(quantityOrdered.getCharacters().toString());
            ((Stage)(order.getScene().getWindow())).close();

        }
    }

    public void onCancelClick(ActionEvent actionEvent) {
        ((Stage)(cancel.getScene().getWindow())).close();
    }

}
