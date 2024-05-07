package com.example.lab8;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RegisterApplication.class.getResource("register.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 440, 400);
        stage.setTitle("Registration");
        stage.setScene(scene);
        stage.show();

    }

    public void main() {
        launch();
    }
}