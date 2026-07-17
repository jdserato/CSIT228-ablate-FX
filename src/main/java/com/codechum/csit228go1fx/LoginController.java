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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginController {
    static User user_login;
    public TextField tfUsername;
    public PasswordField pfPassword;
    public Label lblError;
    static boolean isDark = false;

    private String[] usernames = {"admin", "user"};
    private String[] passwords = {"pass123", "hello1"};

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
        String password = pfPassword.getText();

        try (Connection c = MySQLConnection.getConnection();
             Statement stmt = c.createStatement();) {
            String query = "SELECT * FROM users";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String pass = rs.getString("password");
                boolean isActive = rs.getBoolean("active");
                int balance = rs.getInt("balance");
                if (name.equals(username) && pass.equals(password)) {
                    if (!isActive) {
                        success = true;
                        lblError.setText("Inactive account");
                        break;
                    }
                    lblError.setText("Successfully logged in");
                    User user = new User(id, name, email, pass, balance);
                    try (ObjectOutputStream oos = new ObjectOutputStream(
                            new FileOutputStream("user.ser")
                    )) {
                        oos.writeObject(user);
                    } catch (IOException e) {
                        System.out.println("File cannot be saved");
                    }
                    user_login = user;
                    success = true;
                    Stage stage = (Stage) lblError.getScene().getWindow();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("landing-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    stage.setScene(scene);
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }

        if (!success) {
            lblError.setText("Invalid username or password");
        }
    }

    public void onPasswordKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            onSignInClicked();
        }
    }

    public void onRegisterClicked(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) lblError.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
