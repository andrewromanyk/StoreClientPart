package ua.edu.ukma.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import ua.edu.ukma.storeapp.App;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class MainController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    private Runnable onLoginSuccessfulCallback;

    @FXML
    private void initialize() {
        // Initialization logic, if any
    }

    @FXML
    private void handleLogin() throws Exception {
        // Perform login validation (hypothetical example)
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean loginSuccessful = authenticate(username, password); // Example method

        if (loginSuccessful) {
            // Call the callback if login is successful
            if (onLoginSuccessfulCallback != null) {
                onLoginSuccessfulCallback.run();
            }
        } else {
            // Handle failed login attempt
            System.out.println("Login failed");
        }
    }

    // Method to set callback for successful login
    public void setOnLoginSuccessful(Runnable callback) {
        this.onLoginSuccessfulCallback = callback;
    }

    // Example method for authentication (replace with actual logic)
    private boolean authenticate(String username, String password) throws Exception {
        // Replace with actual authentication logic
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String myHash = bytesToHexString(digest);
        List<String> lst = null;
        try {
            lst = App.client.getHash(username);
        }
        catch (Exception e) {
            return false;
        }

        return username.equals(lst.get(0)) && myHash.equals(lst.get(1));
    }

    public static String bytesToHexString(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : arr) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
