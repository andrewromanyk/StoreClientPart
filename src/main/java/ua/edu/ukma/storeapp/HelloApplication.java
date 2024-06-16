package ua.edu.ukma.storeapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        // Set the dimensions of the application
        int width = 800;
        int height = 600;

        // Create the root BorderPane
        BorderPane root = new BorderPane();

        // Create a VBox for the toolbar on the left
        VBox toolbar = new VBox();
        toolbar.setPadding(new Insets(10));
        toolbar.setSpacing(10);

        // Create and customize buttons
        Button button1 = createCustomButton("Button 1", stage);
        Button button2 = createCustomButton("Button 2", stage);
        Button button3 = createCustomButton("Button 3", stage);

        // Add buttons to the toolbar
        toolbar.getChildren().addAll(button1, button2, button3);

        // Bind the width of the toolbar to 20% of the window's width
        toolbar.prefWidthProperty().bind(stage.widthProperty().multiply(0.2));

        // Ensure the toolbar occupies the available space
        VBox.setVgrow(button1, Priority.ALWAYS);
        VBox.setVgrow(button2, Priority.ALWAYS);
        VBox.setVgrow(button3, Priority.ALWAYS);

        // Set the toolbar on the left of the BorderPane
        root.setLeft(toolbar);

        // Add a border to the toolbar
        toolbar.setBorder(new Border(new BorderStroke(
                Color.BLACK, // Border color
                BorderStrokeStyle.SOLID, // Border style
                CornerRadii.EMPTY, // Corner radii
                new BorderWidths(1) // Border widths
        )));

        // Create the scene and set the dimensions
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm()); // Add this line to apply the CSS

        // Set the title of the window
        stage.setTitle("JavaFX Application with Toolbar");

        // Set the scene to the primary stage
        stage.setScene(scene);

        // Show the primary stage
        stage.show();
    }

    private Button createCustomButton(String text, Stage stage) {
        Button button = new Button(text);
        button.prefWidthProperty().bind(stage.widthProperty().multiply(0.2));
        button.getStyleClass().add("button"); // Apply the button style
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}