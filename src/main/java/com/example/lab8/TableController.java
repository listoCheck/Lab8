package com.example.lab8;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lab5.Client.Client;
import lab5.Client.Types.ComandCheck;
import lab5.Client.Types.Dragon;
import lab5.Client.Types.MyObject;
import lab5.Client.Types.NewDragon;
import lab5.Server.commands.Command;

import java.io.*;
import java.net.SocketException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TableController {
    @FXML
    private TextField commandText;
    ZonedDateTime currentTime = ZonedDateTime.now();
    @FXML
    private TableColumn<lab5.Client.Types.Dragon, Integer> idColumn;

    @FXML
    private TableColumn<lab5.Client.Types.Dragon, String> nameColumn;

    @FXML
    private TableColumn<lab5.Client.Types.Dragon, Double> xCorColumn;

    @FXML
    private TableColumn<lab5.Client.Types.Dragon, Double> yCorColumn;

    @FXML
    private TableColumn<lab5.Client.Types.Dragon, String> creationDateColumn;

    @FXML
    private TableColumn<lab5.Client.Types.Dragon, Integer> ageColumn;

    @FXML
    private TableColumn<lab5.Client.Types.Dragon, String> colorColumn;

    @FXML
    private TableColumn<lab5.Client.Types.Dragon, String> typeColumn;

    @FXML
    private TableColumn<lab5.Client.Types.Dragon, String> characterColumn;

    @FXML
    private TableColumn<lab5.Client.Types.Dragon, Integer> caveColumn;

    @FXML
    private TableColumn<lab5.Client.Types.Dragon, String> userColumn;

    @FXML
    private TextArea serverOut;

    @FXML
    private Button submitButton;

    @FXML
    private Button deleatButton;

    @FXML
    private TableView<lab5.Client.Types.Dragon> tableView;

    @FXML
    private TextField userField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField caveField;

    @FXML
    private TextField characterField;

    @FXML
    private TextField colorField;

    @FXML
    private Button drawTable;

    @FXML
    private TextField helpField;
    @FXML
    private TextField idField;

    @FXML
    private Button insertButton;

    @FXML
    private TextField nameField;

    @FXML
    private TextField typeField;



    @FXML
    private Button updateButton;

    @FXML
    private TextField xFileld;

    @FXML
    private TextField yField;
    private Map<Integer, Color> pointColors = new HashMap<>();
    private Map<Integer, Integer> pointXCoordinates = new HashMap<>();
    private Map<Integer, Integer> pointYCoordinates = new HashMap<>();
    private int maxNumber = 0;

    public void initialize() throws IOException {
        userField.setText("User: " + Client.login);
        idColumn.setCellValueFactory(new PropertyValueFactory<lab5.Client.Types.Dragon, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<lab5.Client.Types.Dragon, String>("name"));
        xCorColumn.setCellValueFactory(new PropertyValueFactory<lab5.Client.Types.Dragon, Double>("x"));
        yCorColumn.setCellValueFactory(new PropertyValueFactory<lab5.Client.Types.Dragon, Double>("y"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<lab5.Client.Types.Dragon, String>("date"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<lab5.Client.Types.Dragon, Integer>("age"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<lab5.Client.Types.Dragon, String>("color"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<lab5.Client.Types.Dragon, String>("type"));
        characterColumn.setCellValueFactory(new PropertyValueFactory<lab5.Client.Types.Dragon, String>("character"));
        caveColumn.setCellValueFactory(new PropertyValueFactory<lab5.Client.Types.Dragon, Integer>("cave"));
        userColumn.setCellValueFactory(new PropertyValueFactory<lab5.Client.Types.Dragon, String>("user"));
        Client.out.write("show\n");
        Client.out.flush();
        for (String i : Client.in.readLine().split("::")) {
            System.out.println(i);
            if (!i.isEmpty()) {
                Dragon dragon = makeDragon(i);
                ObservableList<lab5.Client.Types.Dragon> customers = tableView.getItems();
                customers.add(dragon);
                tableView.setItems(customers);
            }
        }
    }
    private Dragon makeDragon(String data){
        Integer id = Integer.valueOf(data.split(" ")[1].split(",")[0]);
        maxNumber = id;
        String name = (data.split(", ")[1].split(": ")[1]);
        Double x = Double.valueOf((data.split(", ")[2].split(": ")[1]));
        Double y = Double.valueOf((data.split(", ")[3].split(": ")[1]));
        String date = (data.split(", ")[4].split(": ")[1]);
        Integer age = Integer.valueOf((data.split(", ")[5].split(": ")[1]));
        String color = (data.split(", ")[6].split(": ")[1]);
        String type = (data.split(", ")[7].split(": ")[1]);
        String character = (data.split(", ")[8].split(": ")[1]);
        Integer cave = Integer.valueOf((data.split(", ")[9].split(": ")[1]));
        String user = (data.split(", ")[10].split(": ")[1]);
        lab5.Client.Types.Dragon dragon = new lab5.Client.Types.Dragon(id, name, x, y, date, age, color, type, character, cave, user);
        return dragon;
    }
    public void addPoint(int id, Color color, int x, int y) {
        pointColors.put(id, color);
        pointXCoordinates.put(id, x);
        pointYCoordinates.put(id, y);
    }
    @FXML
    void draw(){
        addPoint(1, Color.RED, 100, 100);
        addPoint(2, Color.YELLOW, 200, -100);
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (int id : pointColors.keySet()) {
            Color color = pointColors.get(id);
            int x = pointXCoordinates.get(id);
            int y = pointYCoordinates.get(id);
            drawPoint(gc, color, x, y);
        }
        StackPane root = new StackPane();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, 800, 600);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setTitle("2D Map");
        primaryStage.show();
    }
    private void drawPoint(GraphicsContext gc, Color color, double x, double y) {
        gc.setFill(color);
        gc.fillOval(x, y, 5, 5); // Adjust size as needed
    }
    @FXML
    void submit(ActionEvent event) throws IOException {
        serverOut.clear();
        Client.in = new BufferedReader(new InputStreamReader(Client.clientSocket.getInputStream()));
        Client.out = new BufferedWriter(new OutputStreamWriter(Client.clientSocket.getOutputStream()));
        try {
            String word = "";
            if (Client.retry) {
                word = Client.zapros;
                Client.retry = false;
            } else {
                word = commandText.getText();
                Client.zapros = word;
            }
            MyObject obj = new MyObject(word);
            NewDragon newDragon = new NewDragon();
            if (word.contains("insert")) {
                String dragon = obj.getName() + " :::" + newDragon.addNew();
                Thread.sleep(5);
                Client.out.write(dragon + "\n");
                Client.out.flush();
            } else if (word.contains("update") && !(word.split(" ")[1]).isEmpty()) {
                Client.out.write(obj.getName() + " :::" + newDragon.addNew() + ",;;;," + Client.login + "\n");
                Client.out.flush();
            } else if ((word.contains("exit_server") || word.contains("save")) && Client.admin) {
                Client.out.write(obj.getName() + "\n");
                Client.out.flush();
            } else if ((word.contains("exit_server") || word.contains("save")) && !Client.admin) {
                System.out.println("Нет прав для выполнения данной команды");
            } else if ((!word.contains("exit_server") || !word.contains("save")) && !Client.admin) {
                Client.out.write(obj.getName() + "\n");
                Client.out.flush();
            } else if (Client.admin) {
                Client.out.write(word + "\n");
                Client.out.flush();
            }
            if (word.equals("exit_server") || word.equals("exit")) {
                System.out.println("Клиент был закрыт...");
                Client.flag = false;
            }
            String serverWord = Client.in.readLine(); // ждем, что скажет сервер
            if (serverWord == null) {
                System.out.println("Просим прощения, непредвиденная ошибка");
                Client.retry = true;
            } else {
                String answer = "";
                answer += ("Ответ сервера: \n"); // получив - выводим на экран
                for (String i : serverWord.split("::")) {
                    //System.out.println(i);
                    answer += i + '\n';
                }
                serverOut.setText(answer);
            }
        } catch (SocketException e) {
            System.err.println("Ошибка соединения: " + e.getMessage());
            Client.retries++;
            if (Client.retries < Client.MAX_RETRIES) {
                try {
                    System.out.println("Попытка переподключения через 5 секунд...");
                    Client.retry = true;
                    Thread.sleep(5000); // ждем 5 секунд перед следующей попыткой
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                //clientSocket.close();
            } else {
                System.err.println("Превышено количество попыток подключения.");
                System.err.println("Лучше перезапустите клиент.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        tableView.refresh();
    }
    @FXML
    void rowClicked(MouseEvent event) {
        Dragon clickedDragon = tableView.getSelectionModel().getSelectedItem();
        idField.setText(String.valueOf(clickedDragon.getId()));
        nameField.setText(String.valueOf(clickedDragon.getName()));
        xFileld.setText(String.valueOf(clickedDragon.getX()));
        yField.setText(String.valueOf(clickedDragon.getY()));
        ageField.setText(String.valueOf(clickedDragon.getAge()));
        colorField.setText(String.valueOf(clickedDragon.getColor()));
        typeField.setText(String.valueOf(clickedDragon.getType()));
        characterField.setText(String.valueOf(clickedDragon.getCharacter()));
        caveField.setText(String.valueOf(clickedDragon.getCave()));
        userField.setText(String.valueOf(clickedDragon.getUser()));
        String[] content = {"ID: ", "name: ", "x: ", "y: ", "creationdate: ", "age: ", "color: ", "type: ", "character: ", "cave: ", "user"};
        String[] type_of_content = {"Вводится автоматически", "Введите имя дракона", "Координата х, где находится драков", "Координата у, где находится драков", "Вводится автоматически", "Возраст дракона, больший нуля", "Цвет дракона из предложенных: RED, YELLOW, BROWN", "Тип дракона из предложенных: WATER, UNDERGROUND, AIR, FIRE", "Какой Ваш дракон: CUNNING, EVIL, CHAOTIC_EVIL, FICKLE", "Глубина шахты, в которой обитает дракон, большая, либо равная нулю"};
        String answer = "";
        for (int i = 0; i < 9; i++){
            answer += content[i] + type_of_content[i] + '\n';
        }
        serverOut.setText(answer);
    }

    @FXML
    void insert(ActionEvent event) throws IOException {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9а-яА-Я\\s]");
        String insert_dragon = "";
        insert_dragon += pattern.matcher(nameField.getText()).replaceAll("")+",";
        insert_dragon += xFileld.getText()+",";
        insert_dragon += yField.getText()+",";
        insert_dragon += currentTime + ",";
        insert_dragon += ageField.getText()+",";
        insert_dragon += colorField.getText()+",";
        insert_dragon += typeField.getText()+",";
        insert_dragon += characterField.getText()+",";
        insert_dragon += caveField.getText()+",";
        Dragon dragon = new Dragon(maxNumber++, nameField.getText(), Double.parseDouble(xFileld.getText()), Double.parseDouble(yField.getText()), currentTime.toString(), Integer.parseInt(ageField.getText()), colorField.getText(), typeField.getText(), characterField.getText(), Integer.parseInt(caveField.getText()), Client.login);
        int i = 0;
        String message = "";
        for(String part : insert_dragon.split(",")){
            String output = new ComandCheck().check(i, part);
            if (output.contains("Ошибка: ")){
                message += output + '\n';
            }
            i++;
        }
        if (message.isEmpty()) serverOut.setText("Все окей");
        else serverOut.setText(message);
        String values = "";
        for (i = 0; i <= 8; i++) {
            values += insert_dragon.split(",")[i] + ",;;;,";
        }
        System.out.println(values);
        Client.out.write("insert :::" + values+'\n');
        Client.out.flush();
        String server = Client.in.readLine();
        if (server.equals("Дракон создан")){
            ObservableList<lab5.Client.Types.Dragon> customers = tableView.getItems();
            customers.add(dragon);
            tableView.setItems(customers);
        }
        Client.in = new BufferedReader(new InputStreamReader(Client.clientSocket.getInputStream()));
        Client.out = new BufferedWriter(new OutputStreamWriter(Client.clientSocket.getOutputStream()));
        tableView.refresh();
    }

    @FXML
    void update(ActionEvent event) throws IOException {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9а-яА-Я\\s]");
        String insert_dragon = "";
        insert_dragon += pattern.matcher(nameField.getText()).replaceAll("")+",";
        insert_dragon += xFileld.getText()+",";
        insert_dragon += yField.getText()+",";
        insert_dragon += currentTime + ",";
        insert_dragon += ageField.getText()+",";
        insert_dragon += colorField.getText()+",";
        insert_dragon += typeField.getText()+",";
        insert_dragon += characterField.getText()+",";
        insert_dragon += caveField.getText()+",";
        Dragon dragon = new Dragon(Integer.parseInt(idField.getText()), nameField.getText(), Double.parseDouble(xFileld.getText()), Double.parseDouble(yField.getText()), currentTime.toString(), Integer.parseInt(ageField.getText()), colorField.getText(), typeField.getText(), characterField.getText(), Integer.parseInt(caveField.getText()), Client.login);
        int i = 0;
        String message = "";
        for(String part : insert_dragon.split(",")){
            String output = new ComandCheck().check(i, part);
            if (output.contains("Ошибка: ")){
                message += output + '\n';
            }
            i++;
        }
        if (message.isEmpty()) serverOut.setText("Все окей");
        else serverOut.setText(message);
        String values = "";
        for (i = 0; i <= 8; i++) {
            values += insert_dragon.split(",")[i] + ",;;;,";
        }
        System.out.println(values);
        Client.out.write("update :::" + values+'\n');
        Client.out.flush();
        String server = Client.in.readLine();
        System.out.println(server);
        ObservableList<lab5.Client.Types.Dragon> customers = tableView.getItems();
        customers.remove(Integer.parseInt(idField.getText()) - 1);
        customers.add(Integer.parseInt(idField.getText()) - 1, dragon);
        tableView.setItems(customers);
        Client.in = new BufferedReader(new InputStreamReader(Client.clientSocket.getInputStream()));
        Client.out = new BufferedWriter(new OutputStreamWriter(Client.clientSocket.getOutputStream()));
        tableView.refresh();
    }

    @FXML
    void deleat(ActionEvent event) throws IOException {
        ObservableList<lab5.Client.Types.Dragon> customers = tableView.getItems();
        //System.out.println(customers.get(Integer.parseInt(idField.getText())-1));
        customers.remove(Integer.parseInt(idField.getText())-1);
        tableView.setItems(customers);
        Client.out.write("remove " + idField.getText()+'\n');
        Client.out.flush();
        System.out.println(Client.in.readLine());
        tableView.refresh();
    }
    @FXML
    void fieldClicked(MouseEvent event) {
        String[] content = {"ID: ", "name: ", "x: ", "y: ", "creationdate: ", "age: ", "color: ", "type: ", "character: ", "cave: ", "user"};
        String[] type_of_content = {"Вводится автоматически", "Введите имя дракона", "Координата х, где находится драков", "Координата у, где находится драков", "Вводится автоматически", "Возраст дракона, больший нуля", "Цвет дракона из предложенных: RED, YELLOW, BROWN", "Тип дракона из предложенных: WATER, UNDERGROUND, AIR, FIRE", "Какой Ваш дракон: CUNNING, EVIL, CHAOTIC_EVIL, FICKLE", "Глубина шахты, в которой обитает дракон, большая, либо равная нулю"};
        String answer = "";
        for (int i = 0; i < 9; i++){
            answer += content[i] + type_of_content[i] + '\n';
        }
        serverOut.setText(answer);
    }
}