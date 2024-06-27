package ua.edu.ukma.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import ua.edu.ukma.storeapp.App;
import ua.edu.ukma.storeapp.goods;
import ua.edu.ukma.storeapp.group;
import ua.edu.ukma.storeapp.groupCell;

import java.io.IOException;
import java.util.List;

public class Updategood {
    private goods good;
    private List<group> lst;

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
    private ComboBox<group> comboBoxGroup;

    public void setGood(goods good) {
        this.good = good;
    }

    @FXML
    private Button back;

    @FXML
    private Text id_text;

    @FXML
    private TextField add;

    @FXML
    private TextField sell;

    @FXML
    private Text failureText;

    @FXML
    private void goBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ua/edu/ukma/storeapp/db_goods.fxml"));
        Parent root = loader.load();
        back.getScene().setRoot(root);
    }

    @FXML
    private void update(ActionEvent actionEvent) throws Exception {
        String name = this.name.getText();
        String description = this.description.getText();
        String manufacturer = this.manufacturer.getText();
        double price = this.price.getText().isEmpty() ? 0 : Double.parseDouble(this.price.getText());
        int amount = this.amount.getText().isEmpty() ? -1 : Integer.parseInt(this.amount.getText());
        int add = this.add.getText().isEmpty() ? 0 : Integer.parseInt(this.add.getText());
        int sell = this.sell.getText().isEmpty() ? 0 : Integer.parseInt(this.sell.getText());
        if(name.isEmpty() || description.isEmpty() || manufacturer.isEmpty() || price <= 0
        || amount == -1) {
            failureText.setText("Please fill all the fields");
            return;
        }
        else if(amount + add < sell) {
            failureText.setText("Trying to sell more than possible!");
            failureText.setTextAlignment(TextAlignment.CENTER);
            return;
        }
        int id_group = this.comboBoxGroup.getSelectionModel().getSelectedItem().getId_group();

        String res = App.client.updateGood(good.getId_good(), name, description, manufacturer, amount + add - sell, price, id_group);

        if (res.equals("Ok")) failureText.setText("Updated successfully!");
        else failureText.setText("Bad information");
    }

    @FXML
    void initialize() throws Exception {
        amount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]+") && !newValue.isEmpty()){
                amount.setText(oldValue);
            }
        });

        price.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^(\\d+)(.?)\\d*") && !newValue.isEmpty()){
                price.setText(oldValue);
            }
        });

        add.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]+") && !newValue.isEmpty()){
                amount.setText(oldValue);
            }
        });

        sell.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]+") && !newValue.isEmpty()){
                amount.setText(oldValue);
            }
        });

        lst = App.client.getAllGroupsList();

        comboBoxGroup.getItems().addAll(lst);
        comboBoxGroup.setButtonCell(new groupCell());
        comboBoxGroup.setCellFactory(sc -> new groupCell());
    }

    public void setGood(){
        name.setText(good.getName());
        description.setText(good.getDescription());
        manufacturer.setText(good.getManufacturer());
        price.setText(Double.toString(good.getPrice()));
        amount.setText(Integer.toString(good.getAmount()));
        id_text.setText("ID: " + Integer.toString(good.getId_group()));
        setGroup();
    }

    public void setGroup(){
        for (group gr : lst){
            if (gr.getId_group() == good.getId_group()){
                comboBoxGroup.getSelectionModel().select(gr);
            }
        }
    }
}
