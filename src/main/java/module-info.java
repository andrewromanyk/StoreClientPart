module ua.edu.ukma.storeapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires jakarta.persistence;
    requires org.json;
    requires com.fasterxml.jackson.databind;

    opens ua.edu.ukma.storeapp to javafx.fxml;
    exports ua.edu.ukma.storeapp;
    exports ua.edu.ukma.Controllers;
    opens ua.edu.ukma.Controllers to javafx.fxml;
    exports ua.edu.ukma;
    opens ua.edu.ukma to javafx.fxml;
}