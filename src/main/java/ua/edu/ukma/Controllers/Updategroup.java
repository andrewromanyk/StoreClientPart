package ua.edu.ukma.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import ua.edu.ukma.storeapp.App;
import ua.edu.ukma.storeapp.group;

import java.io.IOException;

public class Updategroup {

    private group grp;

    @FXML
    private Button back;

    @FXML
    private TextField name;

    @FXML
    private TextField description;

    @FXML
    private Text failureText;

    public void setGroup(group grp) {
        this.grp = grp;
    }

    public void setGroup(){
        name.setText(grp.getName());
        description.setText(grp.getDescription());
    }

    @FXML
    void initialize() throws Exception {
        
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ua/edu/ukma/storeapp/db_groups.fxml"));
        Parent root = loader.load();
        back.getScene().setRoot(root);
    }

    @FXML
    private void update(ActionEvent event) throws Exception {
        String name = this.name.getText();
        String description = this.description.getText();

        //System.out.println(description);

        String res = App.client.updateGroup(grp.getId_group(), name, description);

        if (res.equals("Ok")) failureText.setText("Updated successfully!");
        else failureText.setText("Bad information");
    }
}
