package com.example.dsa;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DSAController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}