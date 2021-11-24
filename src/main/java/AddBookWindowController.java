import database.entities.Author;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class AddBookWindowController {
    @FXML
    private Label windowTitle;
    @FXML
    private TableView<Author> authors;
    @FXML
    private TableColumn<Author, String> authorsCol;
    @FXML
    private ComboBox category;
    @FXML
    private TextField isbn;
    @FXML
    private Button cancel;
    @FXML
    private Button finish;
    @FXML
    private TextField threshold;
    @FXML
    private TextField numberofcopies;
    @FXML
    private TextField bookTitle;
    @FXML
    private TextField publishername;
    @FXML
    private TextField publicationyear;
    @FXML
    private TextField sellingprice;
    private List<String> result;
    private final ObservableList<Author> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        result = new ArrayList<>();
        category.getSelectionModel().selectFirst();
        authorsCol.setCellValueFactory(
                new PropertyValueFactory<>("authorName"));
        authorsCol.setCellFactory(TextFieldTableCell.forTableColumn());
        authorsCol.setOnEditCommit(e-> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setAuthorName(e.getNewValue());
        });
        authors.setEditable(true);
    }

    List<String> getResult() {
        return this.result;
    }

    void setAttr(List<String> attr , List<Author> authorsArray) {
        isbn.setText(attr.get(0));
        bookTitle.setText(attr.get(1));
        publishername.setText(attr.get(2));
        publicationyear.setText(attr.get(3));
        sellingprice.setText(attr.get(4));
        for(int i=0 ;i<category.getItems().size();i++){
            if(category.getItems().get(i).toString().equals(attr.get(5))){
                SingleSelectionModel singleSelectionModel = category.getSelectionModel();
                singleSelectionModel.select(i);
                category.setSelectionModel(singleSelectionModel);
                break;
            } }

        numberofcopies.setText(attr.get(6));
        threshold.setText(attr.get(7));
        data.addAll(authorsArray);
        authors.setItems(data);
        windowTitle.setText("Modify Book");
    }
    public void onFinishClick() {
        if (isbn.getCharacters().toString().equals("") ||
                threshold.getCharacters().toString().equals("")
                || numberofcopies.getCharacters().toString().equals("")
                || bookTitle.getCharacters().toString().equals("")
                || publishername.getCharacters().toString().equals("")
                || publicationyear.getCharacters().toString().equals("")
                || sellingprice.getCharacters().toString().equals("")||data.size()==0) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("some text fields are empty\n-all fields must be filled\n-the book must have at least one author");
            a.show();
        } else {
            result.add(isbn.getCharacters().toString());
            result.add(bookTitle.getCharacters().toString());
            result.add(publishername.getCharacters().toString());
            result.add(publicationyear.getCharacters().toString());
            result.add(sellingprice.getCharacters().toString());
            result.add(category.getSelectionModel().getSelectedItem().toString());
            result.add(numberofcopies.getCharacters().toString());
            result.add(threshold.getCharacters().toString());
            //ArrayList<String> authorNames = new ArrayList<>();
            for(int i=0 ;i<data.size() ;i++){
                result.add(data.get(i).getAuthorName());
            }
            ((Stage) (finish.getScene().getWindow())).close();
        }
    }

    public void onCancelClick() {
        ((Stage) (cancel.getScene().getWindow())).close();
    }

    public void onAddAuthorAction() {
        Author newAuthor = new Author();
        newAuthor.setAuthorName("newAuthor" + data.size());
        data.add(newAuthor);
        authors.setItems(data);
    }

    public void onDeleteAuthorAction() {
        if(data.isEmpty())return ;
        data.remove(authors.getSelectionModel().getFocusedIndex());
        authors.setItems(data);
    }
}
