package com.example.lab8;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.InputMethodEvent;
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
import java.net.SocketException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
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
    private ComboBox<String> comboBox;

    @FXML
    public void initialize() {
        comboBox.setValue("Select language");
        //comboBox.getItems().addAll("Русский", "Islenska", "Български", "Canadian English");
        ObservableList<String> list = FXCollections.observableArrayList("Русский", "Islenska", "Български", "Canadian English");
        comboBox.setItems(list);
    }

    @FXML
    void changeLanguage(ActionEvent event) {
        Client.language = comboBox.getSelectionModel().getSelectedItem();
        langChoice(Client.language);
    }

    void langChoice(String lang) {
        try {
            //System.out.println(lang);
            switch (lang) {
                case "Русский":
                    Client.bundle = ResourceBundle.getBundle("login_ru_RU", new Locale("ru", "RU"));
                    comboBox.setPromptText("Русский");
                    ZoneId zone = ZoneId.of("Europe/Moscow");
                    TableController.currentTime = ZonedDateTime.now(zone);
                    break;
                case "Islenska":
                    Client.bundle = ResourceBundle.getBundle("login", new Locale("is"));
                    comboBox.setPromptText("Islenska");
                    zone = ZoneId.of("Iceland");
                    TableController.currentTime = ZonedDateTime.now(zone);
                    break;
                case "Български":
                    Client.bundle = ResourceBundle.getBundle("login", new Locale("bg"));
                    comboBox.setPromptText("Български");
                    zone = ZoneId.of("Europe/Zaporozhye");
                    TableController.currentTime = ZonedDateTime.now(zone);
                    break;
                case "Canadian English":
                    Client.bundle = ResourceBundle.getBundle("login", new Locale("en", "CA"));
                    comboBox.setPromptText("Canadian English");
                    zone = ZoneId.of("Canada/Yukon");
                    TableController.currentTime = ZonedDateTime.now(zone);
                    break;
            }
        }catch (NullPointerException e){
            System.out.println(e);
        }
        //executor.shutdown();
        Stage stage = (Stage) comboBox.getScene().getWindow();
        stage.setTitle(Client.bundle.getString("Registration"));
        usernameTextField.setPromptText(Client.bundle.getString("login"));
        hiddenPasswordTextField.setPromptText(Client.bundle.getString("password"));
        logButton.setText(Client.bundle.getString("Login2"));
        regButton.setText(Client.bundle.getString("Create_Account"));
        errorField.setText(Client.bundle.getString("Аутентификация_провалена"));
        //initialize();
    }

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
        //errorField.setVisible(false);
        try {
            Client.clientSocket = new Socket("localhost", 6789); // этой строкой мы запрашиваем
        } catch (SocketException e){
            //errorField.setVisible(true);
            //errorField.setText(Client.bundle.getString("socked_null"));
            return;
        }
        Client.in = new BufferedReader(new InputStreamReader(Client.clientSocket.getInputStream()));
        Client.out = new BufferedWriter(new OutputStreamWriter(Client.clientSocket.getOutputStream()));
        if (!Client.retry) {
            String username = usernameTextField.getText();
            String password = getPassword();
            Client.command = "login";
            Client.login = username;
            Client.password = password;
        }
        if (!comboBox.getValue().equals("Select language")) Client.language = comboBox.getValue();
        String outline;
        Client.out.write(Client.command + " " + Client.login + " " + Client.password + "\n");
        Client.out.flush();
        try{
            outline = Client.in.readLine();
        } catch (SocketException e){
            errorField.setVisible(true);
            errorField.setText(Client.bundle.getString("socked_null"));
            return;
        }
        System.out.println(outline);
        System.out.println(Client.command + " " + Client.login + " " + Client.password);
        if (Client.login.equals("artmuz")) Client.admin = true;
        if (outline.equals("Пользователь успешно зарегистрирован") || outline.equals("Добро пожаловать на сервер")) {
            //executor.shutdown();
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
        } else {
            Client.clientSocket = new Socket("localhost", 6789); // этой строкой мы запрашиваем
            //Client.in = new BufferedReader(new InputStreamReader(Client.clientSocket.getInputStream()));
            //Client.out = new BufferedWriter(new OutputStreamWriter(Client.clientSocket.getOutputStream()));
            errorField.setText(outline);
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
        errorField.setVisible(false);
        try {
            Client.clientSocket = new Socket("localhost", 6789); // этой строкой мы запрашиваем
        } catch (SocketException e){
            errorField.setVisible(true);
            errorField.setText(Client.bundle.getString("socked_null"));
            return;
        }
        Client.in = new BufferedReader(new InputStreamReader(Client.clientSocket.getInputStream()));
        Client.out = new BufferedWriter(new OutputStreamWriter(Client.clientSocket.getOutputStream()));
        String username = usernameTextField.getText();
        String password = getPassword();
        //updateLoginUsernamesAndPasswords();
        Client.command = "register";
        Client.login = username;
        Client.password = password;
        String outline;
        Client.out.write(Client.command + " " + Client.login + " " + password + "\n");
        Client.out.flush();
        try{
            outline = Client.in.readLine();
        } catch (SocketException e){
            errorField.setVisible(true);
            errorField.setText(Client.bundle.getString("socked_null"));
            return;
        }
        System.out.println(outline);
        if (!comboBox.getValue().equals("Select language")) Client.language = comboBox.getValue();
        System.out.println(Client.command + " " + Client.login + " " + password);
        if (Client.login.equals("artmuz")) Client.admin = true;
        if (outline.equals("Пользователь успешно зарегистрирован") || outline.equals("Добро пожаловать на сервер")) {
            //executor.shutdown();
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
        } else {
            Client.clientSocket = new Socket("localhost", 6789); // этой строкой мы запрашиваем
            Client.in = new BufferedReader(new InputStreamReader(Client.clientSocket.getInputStream()));
            Client.out = new BufferedWriter(new OutputStreamWriter(Client.clientSocket.getOutputStream()));
            errorField.setText(outline);
            errorField.setVisible(true);
        }
    }

    @FXML
    void loginEnter(KeyEvent event) {

    }

}
