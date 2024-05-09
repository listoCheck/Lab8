package com.example.lab8;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lab5.Client.Client;

import java.io.*;
import java.net.Socket;

public class RegisterApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RegisterApplication.class.getResource("register.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 440, 400);
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Registration");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    public void main() throws IOException {
        launch();
    }
}