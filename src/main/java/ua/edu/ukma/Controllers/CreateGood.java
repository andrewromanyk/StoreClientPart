package ua.edu.ukma.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import ua.edu.ukma.storeapp.App;
import ua.edu.ukma.storeapp.group;
import ua.edu.ukma.storeapp.groupCell;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.List;

public class CreateGood {
    @FXML
    private Button back;

    @FXML
    private ComboBox<group> comboBoxGroup;

    @FXML
    private void goBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ua/edu/ukma/storeapp/db_goods.fxml"));
        Parent root = loader.load();
        back.getScene().setRoot(root);
    }

    @FXML
    private TextField name;

    @FXML
    private TextField description;

    @FXML
    private TextField manufacturer;

    @FXML
    private TextField price;

    @FXML
    private TextField amount;

    @FXML
    private Text failureText;

    @FXML
    private void addNew(ActionEvent actionEvent) throws Exception {
        String name = this.name.getText();
        String description = this.description.getText();
        String manufacturer = this.manufacturer.getText();
        Double price = Double.parseDouble(this.price.getText());
        Integer amount = Integer.parseInt(this.amount.getText());
        int id_group = this.comboBoxGroup.getSelectionModel().getSelectedItem().getId_group();

        String res = App.client.createGood(name, description, manufacturer, amount, price, id_group);
        if (res.equals("Ok")) failureText.setText("Created successfully!");
        else failureText.setText("Bad information");
    }

    @FXML
    void initialize() throws Exception {
//        ObservableList<group> lst = FXCollections.observableArrayList(
//                new group(1, "fruit", "normal fruits"),
//                new group(2, "meat", "pork, beef, chicken"));
        List<group> lst = App.client.getAllGroupsList();

        amount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d+") && !newValue.isEmpty()){
                amount.setText(oldValue);
            }
        });

        price.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^(\\d+)(.?)\\d*") && !newValue.isEmpty()){
                price.setText(oldValue);
            }
        });

        comboBoxGroup.getItems().addAll(lst);
        comboBoxGroup.setButtonCell(new groupCell());
        comboBoxGroup.setCellFactory(sc -> new groupCell());
    }
}
