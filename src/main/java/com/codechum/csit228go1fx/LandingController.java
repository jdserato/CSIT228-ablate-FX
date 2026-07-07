package com.codechum.csit228go1fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

import static com.codechum.csit228go1fx.LoginController.isDark;

public class LandingController {
    public Label lblHello;
    public ComboBox<String> cbProgram;
    public TextArea taResult;
    public TextField tfName;
    public RadioButton rbMale;
    public ToggleGroup gender;
    public RadioButton rbFemale;
    public AnchorPane apPane;

    public void initialize() {
        lblHello.setText(lblHello.getText() + LoginController.username_login);
        cbProgram.getItems().addAll("BSIT", "BSCS", "BSCpE", "BSN", "BSArch");
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
            taResult.setText("Name: " + tfName.getText());
            taResult.appendText("\nGender: " + ((RadioButton) gender.getSelectedToggle()).getText());
            if (cbProgram.getValue() == null){
                throw new Exception("Please select a program");
            }
            taResult.appendText("\nProgram: " + cbProgram.getValue());
        } catch (NullPointerException e) {
            taResult.setText("Please select a gender.");
        } catch (Exception e) {
            taResult.setText(e.getMessage());
        }
    }

    public void onLogoutClicked(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) apPane.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }
}
