package lab5.Client;

import com.example.lab8.RegisterApplication;
import lab5.Client.Types.MyObject;
import lab5.Client.Types.NewDragon;
import java.io.IOException;
import java.util.Scanner;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Client implements Serializable {
    public static Socket clientSocket; //сокет для общения
    public static BufferedReader in; // поток чтения из сокета
    public static boolean flag = true;
    public static BufferedWriter out; // поток записи в сокет
    public static String command = "";
    public static String login = "";
    public static String password = "";
    public static boolean admin = false;
    public static boolean flag2 = false;
    public static int MAX_RETRIES = 5;
    public static int retries = 0;
    public static boolean retry = false;
    public static String zapros = "";
    public static String register() throws IOException {
        return (command + " " + login + " " + password);
    }
    public static void main(String[] args) throws InterruptedException, IOException {
        final int MAX_RETRIES = 5;
        int retries = 0;
        NewDragon newDragon = new NewDragon();
        boolean flag2 = false;
        clientSocket = new Socket("localhost", 6789); // этой строкой мы запрашиваем
        RegisterApplication registerApplication = new RegisterApplication();
        registerApplication.main();
    }
}
