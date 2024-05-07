package lab5.Server.file;

import lab5.Server.Test;

import java.sql.*;
import java.sql.ResultSet;
import java.util.*;

/**
 * класс, который читает данные из xml файла
 */
public class ReadFile{
    public static ResultSet resSet;
    public LinkedHashMap<Integer, String> dragons = new LinkedHashMap<>();
    public LinkedHashMap<Integer, String> dragons2 = new LinkedHashMap<>();
    HandleFile hf = new HandleFile();


    /**
     * метод, который читает xml файл и парсит его при помощи System.scanner
     * @return dragon
     */
    public LinkedHashMap<Integer, String> readXml(){
        /*
        try {
            System.out.println("Читаю");
            String info = "";
            String newChars;
            File file = new File("C:\\Users\\admin\\IdeaProjects\\lab2sem\\src\\lab5\\Server\\file\\lab5.xml");
            //System.out.println(file);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                //System.out.println(line.contains("\t</Dragon_"));
                if (line.contains("\t</Dragon") && !info.isEmpty()){
                    //System.out.println(info);
                    String dragon = info.split(",;;;,")[0];
                    //System.out.println(dragon);
                    dragons.put(Integer.parseInt(dragon), info.substring(dragon.length() + 5));
                    System.out.println("прочитал");
                    info = "";
                }
                newChars = "";
                for (int ch = 1; ch <= line.length() - 1; ch++){
                    if (line.charAt(ch) == '>' && ch != line.length() - 1){
                        ch += 1;
                        while (line.charAt(ch) != '<' && line.length() - 1 != ch){
                            newChars += line.charAt(ch);
                            ch += 1;
                        }
                    }
                }
                if (!newChars.isEmpty()){
                    info += newChars + ",;;;,";
                    //System.out.println(newChars);
                }
            }
            scanner.close();
        }catch (FileNotFoundException e){
            System.out.println("Файл не найден: " + e.getMessage());
        }catch (IndexOutOfBoundsException e){
            System.out.println("Проблема с индексом");
        }
        Set<Integer> keys = dragons.keySet();
        Dragon[] drg = new Dragon[keys.size()];
        int size = 0;
        for (Integer key : keys){
            drg[size] = hf.createObject(",;;;," + dragons.get(key), key);
            size++;
        }
        Arrays.sort(drg);
        for (Dragon d : drg){
            dragons2.put(d.getId(), dragons.get(d.getId()));
        }
        //System.out.println(dragons2);
        //return dragons2;

         */
    //.......................................................................
        try {
            System.out.println("Читаю");
            resSet = Test.statmt.executeQuery("SELECT * FROM Dragons");
            while(resSet.next()) {
                String dragonCh = "";
                int user_id = resSet.getInt("id");
                dragonCh += resSet.getString("name") + ",;;;,";
                dragonCh += resSet.getString("coord_x") + ",;;;,";
                dragonCh += resSet.getString("coord_y") + ",;;;,";
                dragonCh += resSet.getString("creation_date") + ",;;;,";
                dragonCh += resSet.getString("age") + ",;;;,";
                dragonCh += resSet.getString("color") + ",;;;,";
                dragonCh += resSet.getString("type") + ",;;;,";
                dragonCh += resSet.getString("character") + ",;;;,";
                dragonCh += resSet.getString("cave") + ",;;;,";
                dragonCh += resSet.getString("users") + ",;;;,";
                dragons2.put(user_id, dragonCh);
                System.out.println("прочитал");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dragons2;
    }
}
