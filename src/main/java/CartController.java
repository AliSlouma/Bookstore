import Functionalities.Checkout;
import Functionalities.Utils;
import database.entities.Book;
import database.entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;

public class CartController {
    public Label price;
    @FXML
    TableView<Book> userBooks;


    User user;
    @FXML
    private TableColumn<Book, String> ISBNCol, titleCol, publisherCol, yearCol, priceCol, categoryCol, copiesCol;

    public CartController() {

    }


    @FXML
    public void initialize() {
        ISBNCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisherName"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        copiesCol.setCellValueFactory(new PropertyValueFactory<>("numberOfCopies"));
    }

    public void setUser(User user) {
        this.user = user;
        userBooks.getItems().setAll(user.getShoppingCart().getBooks());
        price.setText(String.valueOf(user.getShoppingCart().getTotalPrice()));

    }

    public void checkout(ActionEvent actionEvent) throws IOException {
        if (user.getShoppingCart().getBooks().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cart is empty");
            alert.setHeaderText("You have to add items into the cart first before checking out");
            alert.showAndWait();
            Checkout.checkoutCart(user);
        }
        Pair<Stage, FXMLLoader> pair = Utils.createStage("checkout.fxml", "Checkout", 250, 150);
        CheckoutController controller = pair.getValue().getController();
        controller.setUser(user);
        pair.getKey().showAndWait();
    }

    public void DeleteOrder(ActionEvent actionEvent) {
        if (userBooks.getSelectionModel().isEmpty())
            return;

        int index = userBooks.getSelectionModel().getSelectedIndex();
        user.getShoppingCart().removeBook(index);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Remove");
        alert.setHeaderText("Deleted Successfully");
        alert.showAndWait();
        userBooks.getItems().setAll(user.getShoppingCart().getBooks());
        price.setText(String.valueOf(user.getShoppingCart().getTotalPrice()));

    }
}
