package com.example.lab8;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import lab5.Client.Client;
import lab5.Client.Types.ComandCheck;
import lab5.Client.Types.Dragon;
import lab5.Client.Types.MyObject;
import lab5.Client.Types.NewDragon;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.SocketException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class TableController {
    @FXML
    private TextField commandText;
    public static ZonedDateTime currentTime = ZonedDateTime.now();
    @FXML
    private TableColumn<Dragon, Integer> idColumn;
    @FXML
    private AnchorPane pane;

    @FXML
    private TableColumn<Dragon, String> nameColumn;

    @FXML
    private TableColumn<Dragon, Double> xCorColumn;

    @FXML
    private TableColumn<Dragon, Double> yCorColumn;

    @FXML
    private TableColumn<Dragon, String> creationDateColumn;

    @FXML
    private TableColumn<Dragon, Integer> ageColumn;

    @FXML
    private TableColumn<Dragon, String> colorColumn;

    @FXML
    private TableColumn<Dragon, String> typeColumn;

    @FXML
    private TableColumn<Dragon, String> characterColumn;

    @FXML
    private TableColumn<Dragon, Integer> caveColumn;

    @FXML
    private TableColumn<Dragon, String> userColumn;

    @FXML
    private TextArea serverOut;

    @FXML
    private Button submitButton;

    @FXML
    private Button deleatButton;

    @FXML
    private TableView<Dragon> tableView;

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
    private Map<String, Color> pointColors = new HashMap<>();
    private Map<String, Integer> pointXCoordinates = new HashMap<>();
    private Map<String, Integer> pointYCoordinates = new HashMap<>();
    private Map<String, Color> userColors = new HashMap<>();
    @FXML
    private TextField helpField1;

    private int maxNumber = 0;
    private int co = 1;
    private String userClicked = "";
    private String serverout;
    public static ScheduledExecutorService executor;
    @FXML
    private ComboBox<String> comboBox;
    private void setText() {
        submitButton.setText(Client.bundle.getString("Submit"));
        updateButton.setText(Client.bundle.getString("Update"));
        deleatButton.setText(Client.bundle.getString("Delete"));
        insertButton.setText(Client.bundle.getString("Add_new"));
        drawTable.setText(Client.bundle.getString("Draw"));
        helpField.setText(Client.bundle.getString("Введите_'help',_чтобы_узнать_возможные_команды"));
        serverOut.setText(Client.bundle.getString("Server_answer"));
        commandText.setPromptText(Client.bundle.getString("Command"));
        helpField1.setText(Client.bundle.getString("Если_вдруг_все_объекты_коллекцие_исчезли,_то_просто_нажмите_кнопку_отправить"));
    }

    public void initialize() throws IOException {
        comboBox.setValue("Select language");
        ObservableList<String> list = FXCollections.observableArrayList("Русский", "Islenska", "Български", "Canadian English");
        comboBox.setItems(list);
        setText();
        tableView.getItems().clear();
        userField.setText("User: " + Client.login);
        idColumn.setCellValueFactory(new PropertyValueFactory<Dragon, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Dragon, String>("name"));
        xCorColumn.setCellValueFactory(new PropertyValueFactory<Dragon, Double>("x"));
        yCorColumn.setCellValueFactory(new PropertyValueFactory<Dragon, Double>("y"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<Dragon, String>("date"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<Dragon, Integer>("age"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<Dragon, String>("color"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Dragon, String>("type"));
        characterColumn.setCellValueFactory(new PropertyValueFactory<Dragon, String>("character"));
        caveColumn.setCellValueFactory(new PropertyValueFactory<Dragon, Integer>("cave"));
        userColumn.setCellValueFactory(new PropertyValueFactory<Dragon, String>("user"));
        Client.out.write("show\n");
        Client.out.flush();
        serverout = Client.in.readLine();
        if (serverout.isEmpty()){
            new RegisterApplication().main();
        }
        System.out.println(serverout);
        System.out.println(serverout.split("===")[1]);
        //addPoint(userField.getText(), Color.GREEN, 0, 0);
        maxNumber = Integer.parseInt(serverout.split("===")[1]);
        for (String i : serverout.split("===")[0].split("::")) {
            System.out.println(i);
            if (!i.isEmpty()) {
                Dragon dragon = makeDragon(i);
                ObservableList<Dragon> customers = tableView.getItems();
                customers.add(dragon);
                tableView.setItems(customers);
            }
        }
        executor = Executors.newScheduledThreadPool(0);
        long initialDelay = 2;
        long period = 5;
        executor.scheduleAtFixedRate(() -> {
            try {
                Client.out.write("show\n");
                Client.out.flush();
                String serverout1 = Client.in.readLine();
                if (!serverout1.equals(serverout)) apdateTable();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, initialDelay, period, TimeUnit.SECONDS);

    }
    private void apdateTable() throws IOException {
        tableView.getItems().clear();
        userField.setText("User: " + Client.login);
        idColumn.setCellValueFactory(new PropertyValueFactory<Dragon, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Dragon, String>("name"));
        xCorColumn.setCellValueFactory(new PropertyValueFactory<Dragon, Double>("x"));
        yCorColumn.setCellValueFactory(new PropertyValueFactory<Dragon, Double>("y"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<Dragon, String>("date"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<Dragon, Integer>("age"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<Dragon, String>("color"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Dragon, String>("type"));
        characterColumn.setCellValueFactory(new PropertyValueFactory<Dragon, String>("character"));
        caveColumn.setCellValueFactory(new PropertyValueFactory<Dragon, Integer>("cave"));
        userColumn.setCellValueFactory(new PropertyValueFactory<Dragon, String>("user"));
        Client.out.write("show\n");
        Client.out.flush();
        serverout = Client.in.readLine();
        if (serverout.isEmpty()){
            new RegisterApplication().main();
        }
        System.out.println(serverout);
        System.out.println(serverout.split("===")[1]);
        //addPoint(userField.getText(), Color.GREEN, 0, 0);
        maxNumber = Integer.parseInt(serverout.split("===")[1]);
        for (String i : serverout.split("===")[0].split("::")) {
            System.out.println(i);
            if (!i.isEmpty()) {
                Dragon dragon = makeDragon(i);
                ObservableList<Dragon> customers = tableView.getItems();
                customers.add(dragon);
                tableView.setItems(customers);
            }
        }
    }
    private Dragon makeDragon(String data) {
        Integer id = Integer.valueOf(data.split(" ")[1].split(",")[0]);
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
        //maxNumber = id;
        addPoint(user+":"+co, Color.valueOf(color), (int) (Double.parseDouble(String.valueOf(x))), (int) (Double.parseDouble(String.valueOf(y))));
        co++;
        Dragon dragon = new Dragon(id, name, x, y, date, age, color, type, character, cave, user);
        return dragon;
    }

    public void addPoint(String user, Color color, int x, int y) {
        System.out.println(user + " " + color + " " + x + " " + y);
        if (!userColors.containsKey(user.split(":")[0])) {
            Random random = new Random();
            double red = random.nextDouble();
            double green = random.nextDouble();
            double blue = random.nextDouble();
            Color randomColor = new Color(red, green, blue, 1.0);
            userColors.put(user.split(":")[0], randomColor);
        }
        pointColors.put(user, color);
        pointXCoordinates.put(user, x);
        pointYCoordinates.put(user, y);
    }

    @FXML
    void draw() throws IOException {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Image backgroundImage = new Image("C:\\Users\\admin\\IdeaProjects\\Lab8\\src\\main\\java\\com\\example\\lab8\\img.jpg");
        StackPane root = new StackPane();
        root.setBackground(new Background(new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true))));
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, 800, 600);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setTitle("2D Map");
        primaryStage.setResizable(false);
        primaryStage.show();
        drawAnimation(gc, 0, 0);
        for (String id : pointColors.keySet()) {
            Color color = pointColors.get(id);
            int x = pointXCoordinates.get(id);
            int y = pointYCoordinates.get(id);
            Color colorUser = userColors.get(id.split(":")[0]);
            drawPoint(gc, color, x, y, scene, colorUser, id.split(":")[0]);
        }
        //drawAnimation(gc, 0, 0);
        //initialize();
    }


    private void drawPoint(GraphicsContext gc, Color color, double x, double y, Scene scene, Color colorUser, String user) {
        System.out.println("great");
        gc.setFill(colorUser);
        gc.fillOval((x + 400-2.5), (300 - y-2.5)-60, 10, 10); // Adjust size as needed
        gc.setFill(color);
        gc.fillOval((x + 400), (300 - y)-60, 5, 5); // Adjust size as needed
        gc.fillText(user + " " + x + " " + y, x+400, (300 - y)-60);
    }
    private void normaldrawPoint(GraphicsContext gc, Color color, double x, double y, Scene scene, Color colorUser, String user) {
        System.out.println("great");
        gc.setFill(colorUser);
        gc.fillOval((x + 400-2.5), (300 - y-2.5), 10, 10); // Adjust size as needed
        gc.setFill(color);
        gc.fillOval((x + 400), (300 - y), 5, 5); // Adjust size as needed
        gc.fillText(user + " " + x + " " + y, x+400, (300 - y));
    }
    private void drawAnimation(GraphicsContext gc, double x, double y){
        System.out.println("trurut");
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(Color.BLUE);
        gc.fillOval(x, y, 1, 1);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), gc.getCanvas());
        transition.setToY(y + 60);
        transition.play();
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
            if (word.contains("insert") || word.contains("update")|| word.contains("save")) {
                serverOut.setText(Client.bundle.getString("Эти_команды_реализованы_в_графическом_интерфейсе,_пожалуйста_поьзуйтесь_кнопками"));
            } else if ((word.contains("exit_server") && Client.admin)) {
                Client.out.write(obj.getName() + "\n");
                Client.out.flush();
            } else if ((word.contains("exit_server") && !Client.admin)) {
                System.out.println("Нет прав для выполнения данной команды");
            } else if (!word.contains("exit_server") && !Client.admin) {
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
                new RegisterController().loginHandler(event);
            } else {
                String answer = "";
                answer +=Client.bundle.getString("Ответ_сервера") + '\n'; // получив - выводим на экран
                if (serverWord.contains("===")){
                    serverWord = serverWord.split("===")[0];
                }
                for (String i : serverWord.split("::")) {
                    System.out.println(i);
                    if (i.contains(": ") && i.contains("_") && !i.contains("YELLOW") && !i.contains("BROWN") && !i.contains("RED")){
                        answer += i.split(": ")[0] + ": " + Client.bundle.getString(i.split(": ")[1]) + '\n';
                    }else{
                        answer += i + '\n';
                    }

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
                    Thread.sleep(5000); // ждем 5 секунд перед следующей попыткойClient.retry = true;
                    new RegisterController().loginHandler(event);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (InvalidAlgorithmParameterException | BadPaddingException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchPaddingException ex) {
                    throw new RuntimeException(ex);
                } catch (IllegalBlockSizeException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                } catch (InvalidKeyException ex) {
                    throw new RuntimeException(ex);
                }
                //clientSocket.close();
            } else {
                System.err.println("Превышено количество попыток подключения.");
                System.err.println("Лучше перезапустите клиент.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        tableView.refresh();
    }

    @FXML
    void rowClicked(MouseEvent event) {
        try {
            Dragon clickedDragon = tableView.getSelectionModel().getSelectedItem();
            idField.setText(String.valueOf(clickedDragon.getId()));
            nameField.setText(String.valueOf(clickedDragon.getName()));
            xFileld.setText(String.valueOf((int) Double.parseDouble(String.valueOf(clickedDragon.getX()))));
            yField.setText(String.valueOf((int) Double.parseDouble(String.valueOf(clickedDragon.getY()))));
            ageField.setText(String.valueOf(clickedDragon.getAge()));
            colorField.setText(String.valueOf(clickedDragon.getColor()));
            typeField.setText(String.valueOf(clickedDragon.getType()));
            characterField.setText(String.valueOf(clickedDragon.getCharacter()));
            caveField.setText(String.valueOf(clickedDragon.getCave()));
            userClicked = clickedDragon.getUser();
            String[] content = {"ID: ", "name: ", "x: ", "y: ", "creationdate: ", "age: ", "color: ", "type: ", "character: ", "cave: ", "user"};
            String[] type_of_content = {"Вводится_автоматически", "Введите_имя_дракона", "Координата_х,_где_находится_драков,_где_-330⩽x⩽330", "Координата_у,_где_находится_драков,_где_-200⩽y⩽200", "Вводится_автоматически", "Возраст_дракона,_больший_нуля", "Цвет_дракона_из_предложенных_RED,_YELLOW,_BROWN", "Тип_дракона_из_предложенных_WATER,_UNDERGROUND,_AIR,_FIRE", "Какой_Ваш_дракон_CUNNING,_EVIL,_CHAOTIC_EVIL,_FICKLE", "Глубина_шахты,_в_которой_обитает_дракон,_большая,_либо_равная_нулю"};
            String answer = "";
            for (int i = 0; i <= 9; i++) {
                answer += content[i] + Client.bundle.getString(type_of_content[i]) + '\n';
            }
            serverOut.setText(answer);
        }catch (NullPointerException e){
            System.out.println("этот прицел просто имба");
        }
    }

    @FXML
    void insert(ActionEvent event) throws IOException {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9а-яА-Я\\s]");
        String insert_dragon = "";
        insert_dragon += pattern.matcher(nameField.getText()).replaceAll("") + ",";
        insert_dragon += xFileld.getText() + ",";
        insert_dragon += yField.getText() + ",";
        insert_dragon += currentTime + ",";
        insert_dragon += ageField.getText() + ",";
        insert_dragon += colorField.getText() + ",";
        insert_dragon += typeField.getText() + ",";
        insert_dragon += characterField.getText() + ",";
        insert_dragon += caveField.getText() + ",";
        //maxNumber = 0;
        if (tableView.getItems().isEmpty()){
            maxNumber = 0;
        }
        initialize();
        /*else {
            maxNumber = 0;
            for (Dragon i : tableView.getItems()){
                maxNumber = max(maxNumber, Integer.parseInt(String.valueOf(i.getId())));
            }
        }
         */
        System.out.println(maxNumber);
        int i = 0;
        String message = "";
        for (String part : insert_dragon.split(",")) {
            String output = new ComandCheck().check(i, part);
            if (output.contains("Ошибка!")) {
                message += output + '\n';
            }
            i++;
        }
        if (!message.isEmpty()) {
            serverOut.setText(message);
        }else {
            Dragon dragon = new Dragon(++maxNumber, nameField.getText(), Double.parseDouble(xFileld.getText()), Double.parseDouble(yField.getText()), currentTime.toString(), Integer.parseInt(ageField.getText()), colorField.getText(), typeField.getText(), characterField.getText(), Integer.parseInt(caveField.getText()), Client.login);
            serverOut.setText(Client.bundle.getString("Все_окей"));
            String values = "";
            for (i = 0; i <= 8; i++) {
                if (i == 1 || i == 2)
                    values += (int) Double.parseDouble(String.valueOf(insert_dragon.split(",")[i])) + ",;;;,";
                else values += insert_dragon.split(",")[i] + ",;;;,";
            }
            //System.out.println("insert :::" + values+'\n');
            Client.out.write("insert :::" + values + Client.login + '\n');
            System.out.println("insert :::" + values + Client.login);
            Client.out.flush();
            String server = Client.in.readLine();
            if (server.equals("Дракон создан")) {
                ObservableList<Dragon> customers = tableView.getItems();
                customers.add(dragon);
                tableView.setItems(customers);
            }
            tableView.refresh();
            addPoint(dragon.getUser(), Color.valueOf(colorField.getText()), (int) (Double.parseDouble(String.valueOf(xFileld.getText()))), (int) (Double.parseDouble(String.valueOf(yField.getText()))));
        }
    }

    @FXML
    void update(ActionEvent event) throws IOException {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9а-яА-Я\\s]");
        String insert_dragon = "";
        insert_dragon += pattern.matcher(nameField.getText()).replaceAll("") + ",";
        insert_dragon += xFileld.getText() + ",";
        insert_dragon += yField.getText() + ",";
        insert_dragon += currentTime + ",";
        insert_dragon += ageField.getText() + ",";
        insert_dragon += colorField.getText() + ",";
        insert_dragon += typeField.getText() + ",";
        insert_dragon += characterField.getText() + ",";
        insert_dragon += caveField.getText() + ",";
        Dragon dragon = new Dragon(Integer.parseInt(idField.getText()), nameField.getText(), Double.parseDouble(xFileld.getText()), Double.parseDouble(yField.getText()), currentTime.toString(), Integer.parseInt(ageField.getText()), colorField.getText(), typeField.getText(), characterField.getText(), Integer.parseInt(caveField.getText()), Client.login);
        int i = 0;
        String message = "";
        for (String part : insert_dragon.split(",")) {
            String output = new ComandCheck().check(i, part);
            if (output.contains("Ошибка!")) {
                message += output + '\n';
            }
            i++;
        }
        if (message.isEmpty()) serverOut.setText(Client.bundle.getString("Все_окей"));
        else serverOut.setText(Client.bundle.getString(message));
        String values = "";
        for (i = 0; i <= 8; i++) {
            values += insert_dragon.split(",")[i] + ",;;;,";
        }
        if (userClicked.equals(Client.login)) {
            Client.out.write("update " + idField.getText() + " :::" + values + Client.login + "\n");
            //System.out.println("update " + idField.getText()+ " :::" + values + Client.login + "\n");
            Client.out.flush();
            String server = Client.in.readLine();
            serverOut.setText(Client.bundle.getString(server));
            ObservableList<Dragon> customers = tableView.getItems();
            if (dragon.getUser().equals(Client.login)) {
                customers.removeIf(drag -> drag.getId() == Integer.parseInt(idField.getText()));
                if (tableView.getItems().isEmpty()) {
                    customers.add(dragon);
                } else {
                    customers.add(dragon);
                }
                tableView.setItems(customers);
            }
            tableView.refresh();
            for (String name : pointXCoordinates.keySet()) {
                if (pointXCoordinates.get(name) == Integer.parseInt(xFileld.getText())) {
                    pointXCoordinates.remove(name);
                    pointYCoordinates.remove(name);
                    pointColors.remove(name);
                    //userColors.remove(name);
                    break;
                }
            }
            addPoint(dragon.getUser(), Color.valueOf(colorField.getText()), (int) (Double.parseDouble(String.valueOf(xFileld.getText()))), (int) (Double.parseDouble(String.valueOf(yField.getText()))));
        }else{
            serverOut.setText(Client.bundle.getString("Вы_не_можете_это_изменять,_потому_что_это_не_ваш_дракон"));
        }
    }

    @FXML
    void deleat(ActionEvent event) throws IOException {
        ObservableList<Dragon> customers = tableView.getItems();
        if (userClicked.equals(Client.login)) {
            for(String name : pointXCoordinates.keySet()){
                if (pointXCoordinates.get(name) == Integer.parseInt(xFileld.getText())){
                    pointXCoordinates.remove(name);
                    pointYCoordinates.remove(name);
                    pointColors.remove(name);
                    //userColors.remove(name);
                    break;
                }
            }
            customers.removeIf(drag -> drag.getId() == Integer.parseInt(idField.getText()));
            tableView.setItems(customers);
            Client.out.write("remove " + idField.getText() + '\n');
            Client.out.flush();
            System.out.println(Client.in.readLine());
        }else{
            serverOut.setText(Client.bundle.getString("Вы_не_можете_это_изменять,_потому_что_это_не_ваш_дракон"));
        }
        tableView.refresh();

    }

    @FXML
    void fieldClicked(MouseEvent event) {
        String[] content = {"ID: ", "name: ", "x: ", "y: ", "creationdate: ", "age: ", "color: ", "type: ", "character: ", "cave: ", "user"};
        String[] type_of_content = {"Вводится_автоматически", "Введите_имя_дракона", "Координата_х,_где_находится_драков,_где_-330⩽x⩽330", "Координата_у,_где_находится_драков,_где_-200⩽y⩽200", "Вводится_автоматически", "Возраст_дракона,_больший_нуля", "Цвет_дракона_из_предложенных_RED,_YELLOW,_BROWN", "Тип_дракона_из_предложенных_WATER,_UNDERGROUND,_AIR,_FIRE", "Какой_Ваш_дракон_CUNNING,_EVIL,_CHAOTIC_EVIL,_FICKLE", "Глубина_шахты,_в_которой_обитает_дракон,_большая,_либо_равная_нулю"};
        String answer = "";
        for (int i = 0; i <= 9; i++) {
            answer += content[i] + Client.bundle.getString(type_of_content[i]) + '\n';
        }
        serverOut.setText(answer);
    }

    @FXML
    void refresh(ActionEvent event) throws IOException {
        System.exit(0);
    }
    @FXML
    void changeLanguage(ActionEvent event) throws IOException {
        Client.language = comboBox.getSelectionModel().getSelectedItem();
        langChoice(Client.language);
    }

    void langChoice(String lang) throws IOException {
        try {
            //System.out.println(lang);
            switch (lang) {
                case "Русский":
                    Client.bundle = ResourceBundle.getBundle("login_ru_RU", new Locale("ru", "RU"));
                    comboBox.setPromptText("Русский");
                    ZoneId zone = ZoneId.of("Europe/Moscow");
                    currentTime = ZonedDateTime.now(zone);
                    break;
                case "Islenska":
                    Client.bundle = ResourceBundle.getBundle("login", new Locale("is"));
                    comboBox.setPromptText("Islenska");
                    zone = ZoneId.of("Iceland");
                    currentTime = ZonedDateTime.now(zone);
                    break;
                case "Български":
                    Client.bundle = ResourceBundle.getBundle("login", new Locale("bg"));
                    comboBox.setPromptText("Български");
                    zone = ZoneId.of("Europe/Zaporozhye");
                    currentTime = ZonedDateTime.now(zone);
                    break;
                case "Canadian English":
                    Client.bundle = ResourceBundle.getBundle("login", new Locale("en", "CA"));
                    comboBox.setPromptText("Canadian English");
                    zone = ZoneId.of("Canada/Yukon");
                    currentTime = ZonedDateTime.now(zone);
                    break;
            }
        } catch (NullPointerException e) {
            System.out.println(e);
        }
        setText();
    }
}