package com.example.lab8;

import javafx.event.ActionEvent;
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
import javafx.stage.Stage;
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
        String username = usernameTextField.getText();
        String password = getPassword();
        Client.command = "login";
        Client.login = username;
        Client.password = password;
        Client.in = new BufferedReader(new InputStreamReader(Client.clientSocket.getInputStream()));
        Client.out = new BufferedWriter(new OutputStreamWriter(Client.clientSocket.getOutputStream()));
        String outline = "";
        if (!Client.flag2) {
            Client.out.write(Client.command + " " + Client.login + " " + password + "\n");
            Client.out.flush();
            outline = Client.in.readLine();
            System.out.println(outline);
            System.out.println(Client.command + " " + Client.login + " " + password);
            if (Client.login.equals("artmuz")) Client.admin = true;
        }else{
            Client.command = "login";
            System.out.println(Client.command + " " + Client.login + " " + password);
        }
        if (outline.equals("Пользователь успешно зарегистрирован") || outline.equals("Добро пожаловать на сервер")) {
            System.out.println("Введите 'help', чтобы узнать возможные команды:");
            Stage stage = (Stage) logButton.getScene().getWindow();
            stage.close();
            Stage primryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("table.fxml"));
            primryStage.setTitle("Table");
            primryStage.setScene(new Scene(root, 900, 650));
            primryStage.show();
        }
    }

    private String getPassword(){
        if(passwordTextField.isVisible()){
            return passwordTextField.getText();
        } else {
            return hiddenPasswordTextField.getText();
        }
    }

    @FXML
    void createAccount(ActionEvent event) throws IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String username = usernameTextField.getText();
        String password = getPassword();
        updateLoginUsernamesAndPasswords();
        Client.command = "register";
        Client.login = username;
        Client.password = password;
    }

    private void updateLoginUsernamesAndPasswords() throws IOException {

    }
    @FXML
    void loginEnter(KeyEvent event) {

    }

}
