package com.codechum.csit228go1fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.codechum.csit228go1fx.LoginController.*;

public class LandingController {
    public Label lblHello;
    public ComboBox<String> cbProgram;
    public TextArea taResult;
    public TextField tfName;
    public RadioButton rbMale;
    public ToggleGroup gender;
    public RadioButton rbFemale;
    public AnchorPane apPane;
    public TextField tfEmail;
    public ComboBox<String> cbPeople;
    public TextField tfAmount;

    public void initialize() {
        lblHello.setText(lblHello.getText() + LoginController.user_login.getUsername());
        tfName.setText(user_login.getUsername());
        tfEmail.setText(user_login.getEmail());
        cbProgram.getItems().addAll("BSIT", "BSCS", "BSCpE", "BSN", "BSArch");
        List<String> allNames = new ArrayList<>();
        try (Connection c = MySQLConnection.getConnection();
             PreparedStatement stmt = c.prepareStatement("SELECT name FROM users WHERE id != ?")){
            stmt.setInt(1, user_login.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                allNames.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to database");
        }
        cbPeople.getItems().addAll(allNames);
//        Thread.sleep(1000);
        Scene scene = apPane.getScene();
        if (isDark && scene != null) {
            scene.getStylesheets().add(LandingController.class.getResource("dark.css").toExternalForm());
        }
    }

    public void onSaveClicked() {
        taResult.clear();
        taResult.setEditable(false);
        try {
            if (tfName.getText().isEmpty()) {
                throw new Exception("Please enter your name");
            }
            if (tfEmail.getText().isEmpty() || !tfEmail.getText().contains("@")) {
                throw new Exception("Please enter your valid email");
            }
            taResult.setText("Name: " + tfName.getText());
            taResult.appendText("\nGender: " + ((RadioButton) gender.getSelectedToggle()).getText());
            if (cbProgram.getValue() == null) {
                throw new Exception("Please select a program");
            }
            try (Connection c = MySQLConnection.getConnection();) {
                c.setAutoCommit(false);
                int amount = Integer.parseInt(tfAmount.getText());
                String receiver = cbPeople.getValue();
                PreparedStatement statement = c.prepareStatement(
                        "UPDATE users SET balance = ? WHERE name = ?;");
                    statement.setInt(1, amount);
                    statement.setString(2, receiver);
                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Successfully received");
                    }

                        PreparedStatement statement2 = c.prepareStatement(
                                "UPDATE users SET balance = ? WHERE id = ?;");
                    user_login.setBalance(user_login.getBalance() - amount);
                    statement2.setInt(1, user_login.getBalance());
                    statement2.setInt(2, user_login.getId());
                    rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Successfully sent");
                    } else {
                        System.out.println("Failed to send");
                    }
                c.commit();
            }


            taResult.appendText("\nProgram: " + cbProgram.getValue());
            try (Connection c = MySQLConnection.getConnection();
                 PreparedStatement stmt = c.prepareStatement(
                         "UPDATE users SET name=?, email=? WHERE id=?;"
                 )) {
                stmt.setString(1, tfName.getText());
                stmt.setString(2, tfEmail.getText());
                stmt.setInt(3, user_login.getId());
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Successfully updated");
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (NullPointerException e) {
            taResult.setText("Please select a gender.");
        } catch (Exception e) {
            taResult.setText(e.getMessage());
        }
    }

    public void onLogoutClicked(ActionEvent actionEvent) throws IOException {
        File file = new File("user.ser");
        file.delete();
        Stage stage = (Stage) apPane.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    public void onDeleteClicked(ActionEvent actionEvent) {
        try (Connection c = MySQLConnection.getConnection();
            PreparedStatement stmt = c.prepareStatement(
                    "UPDATE users SET active=0 WHERE id=?"
            )) {
            stmt.setInt(1, user_login.getId());
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Successfully set to inactive");
            }
            onLogoutClicked(actionEvent);
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
