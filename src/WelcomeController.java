package com.example.studentmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class WelcomeController {

    @FXML
    protected void onGoClicked(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/main.fxml"));
            Scene mainScene = new Scene(fxmlLoader.load());

            // 2. Find the current window (the projector)
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 3. Swap the slide to the main screen!
            window.setScene(mainScene);
            window.centerOnScreen();
            window.show();

        } catch (IOException e) {
            System.out.println("Could not load the main screen!");
            e.printStackTrace();
        }
    }
}