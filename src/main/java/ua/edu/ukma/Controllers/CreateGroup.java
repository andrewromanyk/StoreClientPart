package ua.edu.ukma.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import ua.edu.ukma.storeapp.App;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.InvalidKeyException;

public class CreateGroup {

    @FXML
    private TextField name;

    @FXML
    private TextField description;

    @FXML
    private Button back;

    @FXML
    private Text failureText;

    @FXML
    private void addNew() throws Exception {
        String name = this.name.getText();
        String description = this.description.getText();

        String res = App.client.createGroup(name, description);
        if (res.equals("Ok")) failureText.setText("Created successfully!");
        else failureText.setText("Bad information");
    }

    @FXML
    private void goBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ua/edu/ukma/storeapp/db_groups.fxml"));
        Parent root = loader.load();
        back.getScene().setRoot(root);
    }
}
