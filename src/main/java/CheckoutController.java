import Functionalities.Checkout;
import database.entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CheckoutController {
    private User user;

    @FXML
    private TextField cardText, expiryDateText;

    @FXML
    public void initialize() {

    }

    public void setUser(User user) {
        this.user = user;
    }

    public void checkout(ActionEvent actionEvent) {

        if (!cardText.getText().isEmpty() && !expiryDateText.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Checkout Done");
            alert.setHeaderText("Checkout has been executed successfully");
            alert.showAndWait();
            Checkout.checkoutCart(user);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
