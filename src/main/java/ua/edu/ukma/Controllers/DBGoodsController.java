package ua.edu.ukma.Controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ua.edu.ukma.storeapp.App;
import ua.edu.ukma.storeapp.goods;
import ua.edu.ukma.storeapp.group;
import ua.edu.ukma.storeapp.groupCell;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.text.DecimalFormat;
import java.util.List;

public class DBGoodsController {

    @FXML
    private Button group;



    @FXML
    private void handleGroup() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ua/edu/ukma/storeapp/db_groups.fxml"));
        Parent root = loader.load();
        group.getScene().setRoot(root);
    }


    @FXML
    private VBox vbox;

    @FXML
    private HBox hbox;

    @FXML
    private TableView<goods> tableview;

    @FXML
    private TableColumn<goods, Integer> id;

    @FXML
    private TableColumn<goods, String> name;

    @FXML
    private TableColumn<goods, String> description;

    @FXML
    private TableColumn<goods, String> manufacturer;

    @FXML
    private TableColumn<goods, Integer> price;

    @FXML
    private TableColumn<goods, Double> amount;

    @FXML
    private TableColumn<goods, Integer> id_group;

    @FXML
    private TableColumn<goods, goods> delete;

    @FXML
    private TableColumn<goods, goods> update;

    @FXML
    private Button addNewButton;

    @FXML
    private TextField filterField;

    @FXML
    private TextField filterManuf;

    @FXML
    private ComboBox<group> filterGroup;

    @FXML
    private ComboBox<String> filterPresence;

    @FXML
    private Text overAllPrice;

    @FXML
    public void initialize() throws Exception {
        id.setCellValueFactory(new PropertyValueFactory<goods, Integer>("id_good"));
        name.setCellValueFactory(new PropertyValueFactory<goods, String>("name"));
        description.setCellValueFactory(new PropertyValueFactory<goods, String>("description"));
        manufacturer.setCellValueFactory(new PropertyValueFactory<goods, String>("manufacturer"));
        price.setCellValueFactory(new PropertyValueFactory<goods, Integer>("price"));
        amount.setCellValueFactory(new PropertyValueFactory<goods, Double>("amount"));
        id_group.setCellValueFactory(new PropertyValueFactory<goods, Integer>("id_group"));

        delete.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        delete.setCellFactory(param -> new TableCell<goods, goods>() {
            private final Button deleteButton = new Button("delete");

            @Override
            protected void updateItem(goods good, boolean empty) {
                super.updateItem(good, empty);

                if (good == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(
                        event -> {
                            //getTableView().getItems().remove(good);
                            try {
                                App.client.deleteGood(good.getId_good());
                            } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException |
                                     IOException | InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                setTable();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                        //TODO: Add deletion of the said element
                );
            }
        });

        update.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        update.setCellFactory(param -> new TableCell<goods, goods>() {
            private final Button updateButton = new Button("update");

            @Override
            protected void updateItem(goods person, boolean empty) {
                super.updateItem(person, empty);

                if (person == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(updateButton);
                updateButton.setOnAction(
                        event -> {
                            //getTableView().getItems().remove(person);
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ua/edu/ukma/storeapp/updategood.fxml"));
                            Parent root = null;
                            try {
                                root = loader.load();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Updategood controller = loader.getController();
                            controller.setGood(getTableRow().getItem());
                            controller.setGood();
                            updateButton.getScene().setRoot(root);
                        }
                );
            }
        });



        setTable();


        filterGroup.getItems().add(new group(-1, "All", "all"));
        filterGroup.getItems().addAll(App.client.getAllGroupsList());
        filterGroup.getSelectionModel().selectFirst();
        filterGroup.setButtonCell(new groupCell());
        filterGroup.setCellFactory(sc -> new groupCell());

        filterPresence.getItems().addAll("All", "Present", "Not present");
        filterPresence.getSelectionModel().selectFirst();
    }

    private void setTableFilter(String name, String manuf, int id_group) throws Exception {
        List<goods> res = App.client.getAllProductsList();
        ObservableList<goods> lst = FXCollections.observableArrayList(res);
        FilteredList<goods> filteredList = new FilteredList<>(lst);

        filteredList.setPredicate(
                t -> t.getName().toLowerCase().contains(name.toLowerCase())
                        && t.getManufacturer().toLowerCase().contains(manuf.toLowerCase())
                        && (t.getId_group() == id_group || id_group == -1)
                        && ( filterPresence.getSelectionModel().getSelectedItem() != null &&
                            (filterPresence.getSelectionModel().getSelectedItem().equals("All")
                            || (filterPresence.getSelectionModel().getSelectedItem().equals("Present") && t.getAmount() != 0)
                            || (filterPresence.getSelectionModel().getSelectedItem().equals("Not present") && t.getAmount() == 0))
                        )
        );

        setTable(filteredList);
    }

    private void setTable(List<goods> res) {
        ObservableList<goods> lst = FXCollections.observableArrayList(res);
        tableview.setItems(lst);
        double sum = 0;
        for (goods good : lst){
            sum += good.getAmount()* good.getPrice();
        }
        overAllPrice.setText("Overall price: " + new DecimalFormat(".00").format(sum));
    }

    private void setTable() throws Exception {
        setTable(App.client.getAllProductsList());
    }

    @FXML
    public void addNew(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ua/edu/ukma/storeapp/creategood.fxml"));
        Parent root = loader.load();
        addNewButton.getScene().setRoot(root);
    }

    @FXML
    public void write() throws Exception {
        setTableFilter(filterField.getText(),
                filterManuf.getText(),
                filterGroup.getSelectionModel().getSelectedItem().getId_group());
    }

}
