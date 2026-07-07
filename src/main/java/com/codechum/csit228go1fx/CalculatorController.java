package com.codechum.csit228go1fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CalculatorController {
    private boolean prevOp = true;
    private String lastOp = "+";
    private int previous = 0;
    public TextField tfResult;

    public void btnNumberClicked(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        if (tfResult.getText().equals("0") || prevOp) {
            tfResult.clear();
            prevOp = false;
        }
        tfResult.setText(tfResult.getText() + btn.getText());
    }

    public void btnOperationClicked(ActionEvent actionEvent) {
        prevOp = true;
        Button btn = (Button) actionEvent.getSource();
        int curr = Integer.parseInt(tfResult.getText());
        switch (lastOp) {
            case "+":
                tfResult.setText(String.valueOf(previous + curr));
                previous = previous + curr;
                break;
            case "-":
                tfResult.setText(String.valueOf(previous - curr));
                previous = previous - curr;
                break;
            case "*":
                tfResult.setText(String.valueOf(previous * curr));
                previous = previous * curr;
                break;
            case "/":
                tfResult.setText(String.valueOf(previous / curr));
                previous = previous / curr;
                break;
        }
        lastOp = btn.getText();
    }
}
