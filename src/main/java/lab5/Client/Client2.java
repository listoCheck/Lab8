package lab5.Client;

import lab5.Client.Types.MyObject;
import lab5.Client.Types.NewDragon;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Client2 implements Serializable {
    private static Socket clientSocket; //сокет для общения
    private static BufferedReader in; // поток чтения из сокета
    public static boolean flag = true;
    private static BufferedWriter out; // поток записи в сокет
    //private static ArrayList<String> command = new ArrayList<>();
    //private static int command_index = 0;

    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner scanner = new Scanner(System.in);
        boolean admin = false;
        //System.out.println(admin);
        final int MAX_RETRIES = 3;
        int retries = 0;
        NewDragon newDragon = new NewDragon();
        boolean flag2 = false;
        String login = "";
        String password = "";
        String command;
        boolean retry = false;
        String zapros = "";
        while (retries < MAX_RETRIES && flag) {
            try {
                Scanner scanner2 = new Scanner(System.in);
                //helios.cs.ifmo.ru
                //localhost
                clientSocket = new Socket("localhost", 45000); // этой строкой мы запрашиваем
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                String outline = "";
                if (!flag2) {
                    System.out.print("Реистрация или вход?  : (register/login): ");
                    command = scanner.nextLine();
                    if (command.equals("register") || command.equals("login")) {
                        Pattern pattern = Pattern.compile("[^a-zA-Z0-9а-яА-Я\\s]");
                        System.out.print("Введите Ваш логин (он не должен содержать специальный символ ':'): ");
                        login = scanner.nextLine();
                        login = pattern.matcher(login).replaceAll("");
                        while (":".contains(login)) {
                            login = scanner.nextLine();
                        }
                        if (login.equals("artmuz")) admin = true;
                        System.out.print("Введите пароль (он не должен содержать пробелов): ");
                        password = scanner.nextLine();
                        while (" ".contains(password)) {
                            password = scanner.nextLine();
                        }
                        out.write(command + " " + login + " " + password + "\n");
                        out.flush();
                        outline = in.readLine();
                        System.out.println(outline);
                    } else {
                        break;
                    }
                    System.out.println(command + " " + login + " " + password);
                    flag2 = true;
                }else{
                    command = "login";
                    System.out.println(command + " " + login + " " + password);
                    out.write(command + " " + login + " " + password + "\n");
                    out.flush();
                    outline = in.readLine();
                    System.out.println(outline);
                }
                if (outline.equals("Пользователь успешно зарегистрирован") || outline.equals("Добро пожаловать на сервер")) {
                    System.out.println("Введите 'help', чтобы узнать возможные команды:");
                }else{
                    break;
                }

                while (true) {
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    // Ждем пока клиент что-нибудь напишет в консоль
                    String word = "";
                    if (retry){
                        word = zapros;
                        retry = false;
                    }else{
                        word = scanner2.nextLine();
                        zapros = word;
                    }
                    //command.add(word);
                    MyObject obj = new MyObject(word);
                    if (word.contains("insert")) {
                        //out.write(word);
                        //MyObject obj = new MyObject(word);
                        String dragon = obj.getName() + " :::" + newDragon.addNew();
                        //System.out.println(dragon + "\n");
                        Thread.sleep(5);
                        out.write(dragon + "\n");
                        out.flush();
                    } else if (word.contains("update") && !(word.split(" ")[1]).isEmpty()) {
                        //MyObject obj = new MyObject(word);
                        out.write(obj.getName() + " :::" + newDragon.addNew() + ",;;;," + login + "\n");
                        out.flush();
                    } else if ((word.contains("exit_server") || word.contains("save")) && admin) {
                        out.write(obj.getName() + "\n");
                        out.flush();
                    } else if ((word.contains("exit_server") || word.contains("save")) && !admin) {
                        System.out.println("Нет прав для выполнения данной команды");
                        continue;
                    } else if ((!word.contains("exit_server") || !word.contains("save")) && !admin) {
                        out.write(obj.getName() + "\n");
                        out.flush();
                    } else if (admin) {
                        out.write(obj.getName() + "\n");
                        out.flush();
                    }
                    if (word.equals("exit_server") || word.equals("exit")) {
                        System.out.println("Клиент был закрыт...");
                        flag = false;
                        break;
                    }
                    String serverWord = new MyObject(in.readLine()).getName(); // ждем, что скажет сервер

                    if (serverWord == null) {
                        System.out.println("Просим прощения, непредвиденная ошибка");
                        retry = true;
                        //clientSocket = new Socket("localhost", 45000);
                        break;
                    } else {
                        System.out.println("Ответ сервера: "); // получив - выводим на экран
                        for (String i : serverWord.split("::")) {
                            System.out.println(i);
                        }
                    }
                }
            } catch (SocketException e) {
                System.err.println("Ошибка соединения: " + e.getMessage());
                retries++;
                if (retries < MAX_RETRIES) {
                    try {
                        System.out.println("Попытка переподключения через 5 секунд...");
                        retry = true;
                        Thread.sleep(5000); // ждем 5 секунд перед следующей попыткой
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    //clientSocket.close();
                } else {
                    System.err.println("Превышено количество попыток подключения. Завершение программы.");
                    System.err.println("Лучше перезапустите клиент.");
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (clientSocket != null) {
                        clientSocket.close(); // закрываем сокет клиента при завершении работы
                    }
                    if (in != null) {
                        in.close(); // закрываем поток чтения
                    }
                    if (out != null) {
                        out.close(); // закрываем поток записи
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not found", e);
        }
    }
}
