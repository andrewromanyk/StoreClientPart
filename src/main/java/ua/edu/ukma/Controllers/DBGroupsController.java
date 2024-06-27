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
import ua.edu.ukma.storeapp.App;
import ua.edu.ukma.storeapp.StoreClientTCP;
import ua.edu.ukma.storeapp.group;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.List;

public class DBGroupsController {

    @FXML
    private void handleGood() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ua/edu/ukma/storeapp/db_goods.fxml"));
        Parent root = loader.load();
        good.getScene().setRoot(root);
    }

    @FXML
    private Button good;


    @FXML
    private TableView<group> tableview;

    @FXML
    private TableColumn<group, Integer> id;

    @FXML
    private TableColumn<group, String> name;

    @FXML
    private TableColumn<group, String> description;

    @FXML
    private TableColumn<group, group> delete;

    @FXML
    private TableColumn<group, group> update;

    @FXML
    private Button addNewButton;

    @FXML
    private TextField filterField;

    @FXML
    public void initialize() throws Exception {
        id.setCellValueFactory(new PropertyValueFactory<group, Integer>("id_group"));
        name.setCellValueFactory(new PropertyValueFactory<group, String>("name"));
        description.setCellValueFactory(new PropertyValueFactory<group, String>("description"));

        delete.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        delete.setCellFactory(param -> new TableCell<group, group>() {
            private final Button deleteButton = new Button("delete");

            @Override
            protected void updateItem(group good, boolean empty) {
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
                                App.client.deleteGroup(good.getId_group());
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
//                        //TODO: Add deletion of the said element
                );
            }
        });

        update.setCellValueFactory(
                param -> new ReadOnlyObjectWrapper<>(param.getValue())
        );
        update.setCellFactory(param -> new TableCell<group, group>() {
            private final Button updateButton = new Button("update");

            @Override
            protected void updateItem(group person, boolean empty) {
                super.updateItem(person, empty);

                if (person == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(updateButton);
                updateButton.setOnAction(
                        event -> {
                            //getTableView().getItems().remove(person);
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ua/edu/ukma/storeapp/updategroup.fxml"));
                            Parent root = null;
                            try {
                                root = loader.load();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Updategroup controller = loader.getController();
                            controller.setGroup(getTableRow().getItem());
                            controller.setGroup();
                            updateButton.getScene().setRoot(root);
                        }
                );//
               }
        });



        setTable();
    }

    private void setTableFilterName(String name) throws Exception {
        List<group> res = App.client.getAllGroupsList();
        ObservableList<group> lst = FXCollections.observableArrayList(res);
        FilteredList<group> filteredList = new FilteredList<>(lst);

        filteredList.setPredicate(
                t -> t.getName().toLowerCase().contains(name.toLowerCase())
        );

        setTable(filteredList);
    }

    private void setTable(List<group> res) {
        ObservableList<group> lst = FXCollections.observableArrayList(res);
        tableview.setItems(lst);
    }

    private void setTable() throws Exception {
        System.out.println("Setting groups!");
        ObservableList<group> lst = FXCollections.observableArrayList(App.client.getAllGroupsList());
        printList(lst);
        tableview.setItems(lst);
    }

    @FXML
    public void addNew(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ua/edu/ukma/storeapp/creategroup.fxml"));
        Parent root = loader.load();
        addNewButton.getScene().setRoot(root);
    }

    @FXML
    public void write() throws Exception {
        setTableFilterName(filterField.getText());
    }

    public static <T> void printList(List<T> list){
        System.out.println("[");
        for (T item : list){
            if (list.indexOf(item) == list.size()-1){
                System.out.println("\t" + item + "\n");
            }
            else System.out.print("\t" + item + ",\n");
        }
        System.out.print("]");
    }
}
