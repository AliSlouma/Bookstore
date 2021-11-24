import Functionalities.BookInteractor;
import Functionalities.Utils;
import database.entities.Book;
import database.entities.OnLogin;
import database.entities.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML
    private Button addToCart;
    @FXML
    private Button orders;
    @FXML
    private Button addbook;
    @FXML
    private Button modifyBook;
    private User user;
    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField bookNameText, yearText, authorNameText, ISBNText, publisherNameText;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TableView<Book> searchTable;
    @FXML
    private TableColumn<Book, String> ISBNCol, titleCol, authorCol,
            publisherCol, yearCol, priceCol, categoryCol, copiesCol;

    public MainController() {
    }

    public void search() {
        BookInteractor bookInteractor = new BookInteractor();
        ArrayList<String> attributes = new ArrayList<>();
        if (!ISBNText.getText().isEmpty())
            attributes.add(ISBNText.getText());
        else attributes.add("");
        if (!bookNameText.getText().isEmpty())
            attributes.add(bookNameText.getText());
        else attributes.add("");
        if (!publisherNameText.getText().isEmpty())
            attributes.add(publisherNameText.getText());
        else attributes.add("");
        if (!yearText.getText().isEmpty())
            attributes.add(yearText.getText());
        else attributes.add("");
        attributes.add(""); // Added to escape selling_price
        if (!categoryComboBox.getSelectionModel().getSelectedItem().equals("All"))
            attributes.add(categoryComboBox.getSelectionModel().getSelectedItem());
        else attributes.add("");
        attributes.add("");
        attributes.add("");
        String author = authorNameText.getText();
        searchTable.getItems().setAll(bookInteractor.searchByAttribute(attributes, author));
        if (searchTable.getItems().isEmpty()) {
            modifyBook.setDisable(true);
            addToCart.setDisable(true);
        } else {
            modifyBook.setDisable(false);
            addToCart.setDisable(false);
        }
    }

    public void addToCart() {
        if (searchTable.getSelectionModel().isEmpty())
            return;
        Book book = searchTable.getSelectionModel().getSelectedItem();
        user.getShoppingCart().addBook(book);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Added To Cart!");
        alert.setHeaderText(String.format("The Item with ID:%s has been added to the cart!", book.getISBN()));
        alert.showAndWait();


    }

    public void openCart() {
        try {
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("cart.fxml"));
            Parent root = mainLoader.load();
            CartController cartController = mainLoader.getController();
            cartController.setUser(this.user);
            Stage stage = new Stage();
            stage.setTitle("Shopping Cart");
            stage.setScene(new Scene(root, 860, 410));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setUser(User user) {
        this.user = user;
        welcomeLabel.setText(String.format("Welcome %s %s", user.getFirstName(), user.getLastName()));
        if (this.user.isManager()) {
            modifyBook.setVisible(true);
            orders.setVisible(true);
            addbook.setVisible(true);
        } else {
            modifyBook.setVisible(false);
            orders.setVisible(false);
            addbook.setVisible(false);
        }
    }

    @FXML
    public void initialize() {
        categoryComboBox.getItems().setAll("All", "Science", "Art", "Religion", "History", "Geography");
        categoryComboBox.getSelectionModel().select("All");
        ISBNCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(
                param -> new SimpleStringProperty(Utils.listToString(param.getValue().getAuthors(), ',')));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisherName"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        copiesCol.setCellValueFactory(new PropertyValueFactory<>("numberOfCopies"));

    }

    public void logout(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        Pair<Stage, FXMLLoader> openingData = Utils.createStage("login.fxml", "Login", 450, 195);
        LoginController loginController = openingData.getValue().getController();
        Stage primaryStage = openingData.getKey();
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
        File file = new File("data.txt");
        if (file.exists()) {
            BufferedReader fileInputStream = new BufferedReader(new FileReader(file));
            String userName = fileInputStream.readLine();
            String password = fileInputStream.readLine();
            fileInputStream.close();
            loginController.setFields(userName, password);
        }
        primaryStage.showAndWait();
    }

    @FXML
    private void onModifyBookClick() throws IOException, SQLException {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("addBookWindow.fxml"));
        Parent root = null;
        try {
            root = mainLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        AddBookWindowController addBookWindowController = mainLoader.getController();
        int rowChosen = searchTable.getSelectionModel().getFocusedIndex();
        addBookWindowController.setAttr(searchTable.getItems().get(rowChosen).getAttributes(), searchTable.getItems().get(rowChosen).getAuthors());
        Book oldBook = searchTable.getSelectionModel().getSelectedItem();
        openWindow(root, "Modify Existing Book", 780, 400);
        if (!addBookWindowController.getResult().isEmpty()) {
            List<String> result = addBookWindowController.getResult();
            Book newBook = new Book(result);
            BookInteractor bookInteractor = new BookInteractor();
            bookInteractor.modifyBook(oldBook, newBook);
        }
    }

    public void onOrdersClick() {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("ordersWindow.fxml"));
        Parent root = null;
        try {
            root = mainLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        OrdersController addBookWindowController = mainLoader.getController();

        openWindow(root, "Orders", 570, 410);
    }

    @FXML
    private void onAddBookClick() {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("addBookWindow.fxml"));
        Parent root = null;
        try {
            root = mainLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        AddBookWindowController mainController = mainLoader.getController();
        openWindow(root, "Add New Book", 780, 400);
        if (!mainController.getResult().isEmpty()) {
            List<String> result = mainController.getResult();
            Book book = new Book(result);
            BookInteractor bookInteractor = new BookInteractor();
            bookInteractor.addBook(book);
        }
    }

    private void openWindow(Parent root, String title, int width, int height) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root, width, height));
        stage.showAndWait();
    }

    public void reportOnClick(ActionEvent actionEvent) {
        try {
            FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("reports.fxml"));
            Parent root = mainLoader.load();
            ReportsController reportsController = mainLoader.getController();
            reportsController.begin();
            Stage stage = new Stage();
            stage.setTitle("Reports");
            stage.setScene(new Scene(root, 629, 450));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
