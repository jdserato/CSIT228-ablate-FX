package com.codechum.csit228go1fx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    private boolean state = false;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        if (!state) {
            welcomeText.setText("Welcome to JavaFX Application!");
        } else {
            welcomeText.setText(null);
        }
        state = !state;
    }
}
