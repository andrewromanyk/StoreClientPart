package ua.edu.ukma.storeapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ua.edu.ukma.Controllers.MainController;

import java.io.IOException;

public class App extends Application {

    public static StoreClientTCP client = new StoreClientTCP();
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        showLoginScene();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        client.stopConnection();
    }

    // Method to show the login scene
    private void showLoginScene() throws IOException, InterruptedException {
        System.out.println(getClass());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Store Client");
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
        //51.21.135.217
        client.startConnection("51.21.135.217",  5000);
        // Get the controller from FXMLLoader
        MainController controller = loader.getController();
        // Set the callback to handle successful login
        controller.setOnLoginSuccessful(this::showDashboardScene);
    }

    // Method to show the dashboard scene
    private void showDashboardScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("db_goods.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);

            // Show the dashboard scene
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
