package com.example.lab8;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import lab5.Client.Client;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class RegisterController {


    @FXML
    public TextField usernameTextField;
    @FXML
    private Button logButton;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button regButton;
    @FXML
    public TextField errorField;
    @FXML
    private PasswordField hiddenPasswordTextField;
    @FXML
    private CheckBox showPassword;


    @FXML
    void changeVisibility(ActionEvent event) {
        if (showPassword.isSelected()) {
            passwordTextField.setText(hiddenPasswordTextField.getText());
            passwordTextField.setVisible(true);
            hiddenPasswordTextField.setVisible(false);
            return;
        }
        hiddenPasswordTextField.setText(passwordTextField.getText());
        hiddenPasswordTextField.setVisible(true);
        passwordTextField.setVisible(false);
    }

    @FXML
    void loginHandler(ActionEvent event) throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Client.clientSocket = new Socket("localhost", 6789); // этой строкой мы запрашиваем
        Client.in = new BufferedReader(new InputStreamReader(Client.clientSocket.getInputStream()));
        Client.out = new BufferedWriter(new OutputStreamWriter(Client.clientSocket.getOutputStream()));
        if (!Client.retry) {
            String username = usernameTextField.getText();
            String password = getPassword();
            Client.command = "login";
            Client.login = username;
            Client.password = password;
        }
        String outline;
        Client.out.write(Client.command + " " + Client.login + " " + Client.password + "\n");
        Client.out.flush();
        outline = Client.in.readLine();
        System.out.println(outline);
        System.out.println(Client.command + " " + Client.login + " " + Client.password);
        if (Client.login.equals("artmuz")) Client.admin = true;
        if (outline.equals("Пользователь успешно зарегистрирован") || outline.equals("Добро пожаловать на сервер")) {
            System.out.println("Введите 'help', чтобы узнать возможные команды:");
            Stage stage = (Stage) logButton.getScene().getWindow();
            stage.close();
            Stage primryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("table.fxml"));
            primryStage.setTitle("Table");
            primryStage.setScene(new Scene(root, 900, 670));
            //primryStage.initStyle(StageStyle.UNDECORATED);
            primryStage.setOnCloseRequest(e -> TableController.executor.shutdown());
            primryStage.setResizable(false);
            //primryStage.initStyle(StageStyle.TRANSPARENT);
            primryStage.show();
        }else{
            Client.clientSocket = new Socket("localhost", 6789); // этой строкой мы запрашиваем
            Client.in = new BufferedReader(new InputStreamReader(Client.clientSocket.getInputStream()));
            Client.out = new BufferedWriter(new OutputStreamWriter(Client.clientSocket.getOutputStream()));
            errorField.setText(outline);
            //errorField.backgroundProperty().
            errorField.setVisible(true);
        }
    }

    private String getPassword() {
        if (passwordTextField.isVisible()) {
            return passwordTextField.getText();
        } else {
            return hiddenPasswordTextField.getText();
        }
    }

    @FXML
    void createAccount(ActionEvent event) throws IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Client.clientSocket = new Socket("localhost", 6789); // этой строкой мы запрашиваем
        Client.in = new BufferedReader(new InputStreamReader(Client.clientSocket.getInputStream()));
        Client.out = new BufferedWriter(new OutputStreamWriter(Client.clientSocket.getOutputStream()));
        String username = usernameTextField.getText();
        String password = getPassword();
        updateLoginUsernamesAndPasswords();
        Client.command = "register";
        Client.login = username;
        Client.password = password;
        String outline;
        Client.out.write(Client.command + " " + Client.login + " " + password + "\n");
        Client.out.flush();
        outline = Client.in.readLine();
        System.out.println(outline);
        System.out.println(Client.command + " " + Client.login + " " + password);
        if (Client.login.equals("artmuz")) Client.admin = true;
        if (outline.equals("Пользователь успешно зарегистрирован") || outline.equals("Добро пожаловать на сервер")) {
            System.out.println("Введите 'help', чтобы узнать возможные команды:");
            Stage stage = (Stage) logButton.getScene().getWindow();
            stage.close();
            Stage primryStage = new Stage();
            //primryStage.initStyle(StageStyle.UTILITY);
            Parent root = FXMLLoader.load(getClass().getResource("table.fxml"));
            primryStage.setTitle("Table");
            primryStage.setScene(new Scene(root, 900, 670));
            //primryStage.initStyle(StageStyle.UNDECORATED);
            primryStage.setOnCloseRequest(e -> TableController.executor.shutdown());
            primryStage.setResizable(false);
            primryStage.show();
        }else{
            Client.clientSocket = new Socket("localhost", 6789); // этой строкой мы запрашиваем
            Client.in = new BufferedReader(new InputStreamReader(Client.clientSocket.getInputStream()));
            Client.out = new BufferedWriter(new OutputStreamWriter(Client.clientSocket.getOutputStream()));
            errorField.setText(outline);
            //errorField.backgroundProperty().
            errorField.setVisible(true);
        }
    }

    private void updateLoginUsernamesAndPasswords() throws IOException {

    }

    @FXML
    void loginEnter(KeyEvent event) {

    }

}
