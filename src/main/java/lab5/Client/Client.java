package lab5.Client;

import com.example.lab8.RegisterApplication;
import lab5.Client.Types.MyObject;
import lab5.Client.Types.NewDragon;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
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
    public static String language = "Русский";
    public static ResourceBundle bundle;
    public static String register() throws IOException {
        return (command + " " + login + " " + password);
    }
    public static void main(String[] args) throws InterruptedException, IOException {
        final int MAX_RETRIES = 5;
        int retries = 0;
        NewDragon newDragon = new NewDragon();
        boolean flag2 = false;
        //System.out.println(Locale.getDefault());

        //for (Locale loc : Locale.getAvailableLocales()){
        //    System.out.println(loc);
        //}
        //Locale loc = new Locale.Builder().setLanguage("ru").setRegion("RU").build();
        bundle = ResourceBundle.getBundle("login");
        //System.out.println(bundle_ru.getString("somevalue"));
        /*
        ResourceBundle bundle_ru = ResourceBundle.getBundle("login_ru", new Locale("ru", "RU"));
        ResourceBundle bundle_en_ca = ResourceBundle.getBundle("login_en_ca", new Locale("en", "CA"));
        ResourceBundle bundle_bg = ResourceBundle.getBundle("login_bg", new Locale("bg", "BG"));
        ResourceBundle bundle_is = ResourceBundle.getBundle("login_is", new Locale("is", "IS"));

         */
        RegisterApplication registerApplication = new RegisterApplication();
        registerApplication.main();
    }
}
