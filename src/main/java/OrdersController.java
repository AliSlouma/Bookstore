import Functionalities.BookInteractor;
import Functionalities.Orderer;
import database.entities.Author;
import database.entities.Book;
import database.entities.BookOrder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrdersController {
    @FXML
    private Button close;
    @FXML
    private TableColumn<BookOrder, String> orderNumCol;
    @FXML
    private TableColumn<BookOrder, String> isbnCol;
    @FXML
    private TableColumn<BookOrder, String> dateoutCol;
    @FXML
    private TableColumn<BookOrder, String> quantityCol;
    @FXML
    private TableView<BookOrder> ordersTable;
    private final ObservableList<BookOrder> data = FXCollections.observableArrayList();
    private Orderer orderer;
    private BookInteractor bookInteractor;

    @FXML
    public void initialize() {

        orderNumCol.setCellValueFactory(
                new PropertyValueFactory<>("orderNum"));
        isbnCol.setCellValueFactory(
                new PropertyValueFactory<>("ISBN"));
        dateoutCol.setCellValueFactory(
                new PropertyValueFactory<>("dateOut"));
        quantityCol.setCellValueFactory(
                new PropertyValueFactory<>("quantityOrdered"));
        orderNumCol.setCellFactory(TextFieldTableCell.forTableColumn());
        isbnCol.setCellFactory(TextFieldTableCell.forTableColumn());
        dateoutCol.setCellFactory(TextFieldTableCell.forTableColumn());
        quantityCol.setCellFactory(TextFieldTableCell.forTableColumn());
        orderer = new Orderer();
        bookInteractor = new BookInteractor();
        refresh();
    }

    private void refresh() {
        List<BookOrder> bookOrders = orderer.getOrders();
        data.clear();
        data.addAll(bookOrders);
        ordersTable.setItems(data);
    }

    public void onRefreshClick() {
        refresh();
    }

    private void confirmOrder(BookOrder bookOrder) throws SQLException {

        ArrayList<String> attr = new ArrayList<>();
        attr.add(bookOrder.getISBN());
        for (int i = 0; i < 7; i++) attr.add("");
        ArrayList<Book> books = bookInteractor.searchByAttribute(attr, "");
        if (books.isEmpty()) {
            Alert alert = (new Alert(Alert.AlertType.ERROR));
            alert.setContentText("no Books Found with this ISBN" + bookOrder.getISBN());
            alert.showAndWait();
            refresh();
            return;
        }
        orderer.confirmOrder(bookOrder);
    }

    public void onConfirmClick() throws SQLException {
        TableView.TableViewSelectionModel<BookOrder> orderSelected = ordersTable.getSelectionModel();
        BookOrder bookOrder = ordersTable.getItems().get(orderSelected.getFocusedIndex());
        confirmOrder(bookOrder);
        refresh();
    }

    public void onConfirmAllClick() throws SQLException {
        ObservableList<BookOrder> bookOrders = ordersTable.getItems();
        for (BookOrder bookOrder : bookOrders) {
            confirmOrder(bookOrder);
        }
        refresh();
    }

    @FXML
    private void onAddOrderClick() throws IOException, SQLException {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("placeOrderWindow.fxml"));
        Parent root = mainLoader.load();
        PlaceOrderWindow mainController = mainLoader.getController();
        Stage stage = new Stage();
        stage.setTitle("Place New Order");
        stage.setScene(new Scene(root, 447,341 ));
        stage.showAndWait();
        if (!mainController.result.isEmpty()) {
            ArrayList<String> result = mainController.result;
            BookOrder bookOrder = new BookOrder();
            bookOrder.setISBN(result.get(1));
            bookOrder.setOrderNum(result.get(0));
            bookOrder.setDateOut(result.get(2));
            bookOrder.setQuantityOrdered(result.get(3));
            orderer.orderBooks(bookOrder);
        }
        refresh();
    }

    public void onCloseClick() {
        ((Stage) (close.getScene().getWindow())).close();
    }
}
