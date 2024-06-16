module ua.edu.ukma.storeapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;

    opens ua.edu.ukma.storeapp to javafx.fxml;
    exports ua.edu.ukma.storeapp;
}