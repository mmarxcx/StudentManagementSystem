package com.example.studentmanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // This looks for the visual layout we built in SceneBuilder
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/welcome.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setTitle("Student Management System");
        stage.setScene(scene);
        stage.setResizable(false); // Prevents the user from messing up your layout
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}