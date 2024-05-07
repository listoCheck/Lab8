package lab5.Server;

import lab5.Server.commands.Console;
import lab5.Server.file.MyObject;
import lab5.Server.file.WriteFile;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Server {

    private static ServerSocket server; // серверсокет
    public static FileWriter log;
    public static String out_to_client = "";
    private static final Map<String, String> users = new HashMap<>();
    private static final ReentrantLock lock = new ReentrantLock();
    public static ArrayList<String> ports = new ArrayList<>();
    private static final Map<String, String> logins = new HashMap<>();

    public static void main(String[] args) throws IOException, SQLException {
        Test.test();
        WriteFile.WRITE_FILE.writeFileInMain();
        log = new FileWriter("C:\\Users\\admin\\IdeaProjects\\lab2sem\\src\\lab5\\Server\\log.txt", true);
        loadUsers();
        try {
            server = new ServerSocket(45000); // серверсокет прослушивает порт 45000
            System.out.println("Сервер запущен!");
            log.write("Сервер запущен!" + "\n");

            ExecutorService readingThreadPool = Executors.newCachedThreadPool();
            ForkJoinPool processingThreadPool = new ForkJoinPool();
            ExecutorService sendingThreadPool = Executors.newCachedThreadPool();

            while (true) {
                Socket clientSocket = server.accept();
                ports.add(String.valueOf(clientSocket.getPort()));
                /*
                for (String i : logins.keySet()){
                    if (!i.contains((CharSequence) ports)){
                        String value = logins.get(String.valueOf(clientSocket.getPort()));
                        logins.remove(String.valueOf(clientSocket.getPort()), value);
                    }
                }

                 */
                readingThreadPool.submit(new ClientHandler(clientSocket, processingThreadPool, sendingThreadPool));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (server != null) {
                    server.close(); // закрываем серверный сокет при завершении работы
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class ClientHandler implements Runnable {
        private static Socket clientSocket = null;
        private final ForkJoinPool processingThreadPool;
        private final ExecutorService sendingThreadPool;
        private static final Map<String, String> logins = new HashMap<>();

        public ClientHandler(Socket socket, ForkJoinPool processingThreadPool, ExecutorService sendingThreadPool) {
            clientSocket = socket;
            this.processingThreadPool = processingThreadPool;
            this.sendingThreadPool = sendingThreadPool;
            System.out.println("Клиент подключился по сокету: " + socket.getPort());
            try {
                log.write("Клиент подключился по сокету: " + socket.getPort() + "\n");
                log.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public static String socket(){
            //System.out.println(logins.get(String.valueOf(clientSocket.getPort())));
            return logins.get(String.valueOf(clientSocket.getPort()));
        }
        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                String line = in.readLine();
                if (line != null && line.split(" ").length == 3) {
                    String[] parts = line.split(" ");
                    String command = parts[0];
                    String login = parts[1];
                    String password = hashPassword(parts[2]);
                    if (command.equals("register")) {
                        if (!users.containsKey(login)) {
                            out.write("Пользователь успешно зарегистрирован\n");
                            out.flush();
                            users.put(login, password);
                            saveUsers();
                            System.out.println("Пользователь успешно зарегистрирован");
                        } else {
                            out.write("Пользователь с этим логином уже зарегистрирован\n");
                            out.flush();
                            System.out.println("Пользователь с этим логином уже зарегистрирован");
                            return;
                        }
                    } else if (command.equals("login")&& !logins.containsValue(login)) { //
                        if (!authenticateUser(login, password)) {
                            out.write("Аутентификация провалена\n");
                            out.flush();
                            System.out.println("Аутентификация провалена");
                            return;
                        } else {
                            logins.put(String.valueOf(clientSocket.getPort()), login);
                            System.out.println(logins);
                            out.write("Добро пожаловать на сервер\n");
                            out.flush();
                        }
                    }else {
                        out.write("Добро пожаловать на сервер\n");
                        out.flush();
                        //out.write("такой пользователь уже занят, \n");
                        //out.flush();
                        //System.out.println("такой пользователь уже занят");
                    }
                }
                String word = "";
                while ((word = new MyObject(in.readLine()).getName()) != null) {
                    String out_cl = word.contains(":::") ? word.split(":::")[0] : word;
                    Console console = new Console();
                    System.out.println("Получено сообщение от клиента: " + clientSocket.getPort() + " " + out_cl);
                    log.write("Получено сообщение от клиента: " + clientSocket.getPort() + " " + word + "\n");
                    log.flush();
                    String finalWord = word;
                    if (word.equals("exit_server") || word.equals("exit")) {
                        System.out.println(logins);
                        String value = logins.get(String.valueOf(clientSocket.getPort()));
                        logins.remove(String.valueOf(clientSocket.getPort()), value);
                        System.out.println("Клиент отключился");
                        log.write("Клиент отключился\n");
                        System.out.println(finalWord);
                        if (finalWord.equals("exit_server")){
                            System.exit(0);
                        }
                        break;
                    }
                    processingThreadPool.invoke(new RecursiveAction() {
                        @Override
                        protected void compute() {
                            lock.lock();
                            try {
                                console.start(finalWord, false, "");
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            } finally {
                                lock.unlock();
                            }
                        }
                    });

                    sendingThreadPool.submit(() -> {
                        try {
                            out.write(new MyObject(out_to_client).getName() + "\n");
                            out.flush();
                            out_to_client = "";
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    /*
                    if (word.equals("exit_server") || word.equals("exit")) {
                        System.out.println(logins);
                        String value = logins.get(String.valueOf(clientSocket.getPort()));
                        logins.remove(String.valueOf(clientSocket.getPort()), value);
                        System.out.println("Клиент отключился");
                        log.write("Клиент отключился\n");
                        break;
                    }

                     */


                }
            } catch (IOException e) {
                System.out.println("Ошибка при чтении сообщения от клиента: " + e.getMessage());
            } finally {
                try {
                    if (clientSocket != null) {
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean authenticateUser(String login, String password) {
        return users.containsKey(login) && users.get(login).equals(password);
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

    private static void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue() + "\n");
            }
            writer.flush();
            System.out.println("Пользователи обновлены");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
