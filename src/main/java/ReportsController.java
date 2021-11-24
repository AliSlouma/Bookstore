import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import database.entities.ConnectionPool;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ReportsController {
    private static final Font FONT = FontFactory.getFont(FontFactory.COURIER_BOLD, 20, BaseColor.BLACK);
    @FXML
    public TableView<Map<String, String>> customers, topBooks;
    public Label totalSales;
    public TableColumn<Map, String> userName, noOFBooks, ISBNCol, titleCol, bookQuantity;
    private Connection connection;
    private Document document;

    public ReportsController() {
        document = new Document(PageSize.A4, 20, 20, 20, 20);
        try {
            PdfWriter.getInstance(document, new FileOutputStream("report.pdf"));
            document.open();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {

        ISBNCol.setCellValueFactory(new MapValueFactory<>("ISBN"));
        titleCol.setCellValueFactory(new MapValueFactory<>("title"));
        userName.setCellValueFactory(new MapValueFactory<>("Name"));
        noOFBooks.setCellValueFactory(new MapValueFactory<>("TotalPurchased"));
        bookQuantity.setCellValueFactory(new MapValueFactory<>("Quantity"));
        connection = ConnectionPool.getInstance().getConnection();

    }

    public void begin() {
        getTotalSales();
        getTopCustomers();
        getTopBooks();
        document.close();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Report PDF has been generated.");
        alert.setHeaderText("Report PDF has been successfully generated.");
        alert.showAndWait();
    }

    public void getTotalSales() {
        String query = "call total_sales()";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                totalSales.setText(resultSet.getString("count(id)"));
                addText(String.format("Total Sales: %s", resultSet.getString("count(id)")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void addTableHeader(PdfPTable table, String... columnNames) {
        Stream.of(columnNames)
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addText(String text) {
        try {
            Paragraph paragraph = new Paragraph(text, FONT);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void getTopCustomers() {
        String query = "call top_customers()";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            int count = 0;
            PdfPTable table = new PdfPTable(2);
            addTableHeader(table, "Name", "Total Purchased");
            addText("Top Customers");
            while (resultSet.next() && count < 5) {
                String userName = resultSet.getString(1);
                String noOfBooks = resultSet.getString(2);


                ObservableList<Map<String, String>> items =
                        FXCollections.<Map<String, String>>observableArrayList();

                Map<String, String> map = new HashMap<>();
                map.put("Name", userName);
                map.put("TotalPurchased", noOfBooks);
                table.addCell(userName);
                table.addCell(noOfBooks);
                items.add(map);

                customers.getItems().addAll(items);
                count++;


            }
            table.setSpacingBefore(2.5f);
            table.setSpacingAfter(2.5f);
            document.add(table);

        } catch (SQLException | DocumentException throwables) {
            throwables.printStackTrace();
        }
    }

    public void getTopBooks() {
        String query = "call top_books()";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            int count = 0;
            PdfPTable table = new PdfPTable(3);
            addTableHeader(table, "ISBN", "Title", "Quantity");
            addText("Top 10 Selling Books");
            while (resultSet.next() && count != 10) {
                String ISBN = resultSet.getString(1);
                String title = resultSet.getString(2);
                String noOfBooks = resultSet.getString(3);


                ObservableList<Map<String, String>> items =
                        FXCollections.<Map<String, String>>observableArrayList();

                Map<String, String> map = new HashMap<>();
                map.put("ISBN", ISBN);
                map.put("title", title);
                map.put("Quantity", noOfBooks);
                table.addCell(ISBN);
                table.addCell(title);
                table.addCell(noOfBooks);
                items.add(map);

                topBooks.getItems().addAll(items);
                count++;


            }
            table.setSpacingBefore(2.5f);
            table.setSpacingAfter(2.5f);
            document.add(table);
        } catch (SQLException | DocumentException throwables) {
            throwables.printStackTrace();
        }

    }

}
