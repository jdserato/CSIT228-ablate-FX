package com.codechum.csit228go1fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterController {

    static String username_login;
    public TextField tfUsername;
    public PasswordField pfPassword;
    public Label lblError;
    static boolean isDark = false;
    public TextField tfEmail;

    public void onDarkModeClicked() {
        Scene scene = lblError.getScene();
        if (!isDark) {
            scene.getStylesheets().add(LoginController.class.getResource("dark.css").toExternalForm());
        } else {
            scene.getStylesheets().clear();
        }
        isDark = !isDark;
    }

    public void onSignInClicked() throws IOException {
        boolean success = false;
        String username = tfUsername.getText();
        String email = tfEmail.getText();
        String password = pfPassword.getText();

        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement stmt = c.prepareStatement("INSERT INTO users (name, email, password) VALUES (?, ?, ?);")
        ) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(rowsUpdated + " rows updated");
                success = true;
            } else {
                System.out.println("Failed to insert user");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }

//
//        for (int i = 0; i < usernames.length; i++) {
//            if (username.equals(usernames[i]) && password.equals(passwords[i])) {
//                lblError.setText("Successfully registered");
//                username_login = usernames[i];
//                success = true;
//                Stage stage = (Stage) lblError.getScene().getWindow();
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("landing-view.fxml"));
//                Scene scene = new Scene(fxmlLoader.load());
//                stage.setScene(scene);
//                break;
//            }
//        }
        if (!success) {
            lblError.setText("Invalid username or password");
        } else {
            lblError.setText("Successfully logged in");
            tfEmail.clear();
            tfUsername.clear();
            pfPassword.clear();
        }
    }

    public void onPasswordKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            onSignInClicked();
        }
    }

    public void onLoginClicked(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) lblError.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
