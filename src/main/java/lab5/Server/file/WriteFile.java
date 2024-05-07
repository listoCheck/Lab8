package lab5.Server.file;

import lab5.Server.Server;
import lab5.Server.Test;
import lab5.Server.commands.Insert;
import lab5.Server.ifmo.*;

import javax.annotation.processing.SupportedSourceVersion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static java.util.Collections.max;

/**
 * Класс, в котором обрабатывается большинство команд, и записываются данные в файл
 */
public class WriteFile implements Functional{
    public ArrayList<String> pathes = new ArrayList<>();
    public static ResultSet resSet;
    public LinkedHashMap<Integer, String> dragons;
    String[] content = {"name: ", "coordinate_x: ", "coordinate_y: ", "creationdate: ", "age: ", "color: ", "type: ", "character: ", "cave: ", "user: "};
    //String[] type_of_content = {"Введите имя дракона", "Координата х, где находится драков", "Координата у, где находится драков", "Вводится автоматически", "Возраст дракона, больший нуля", "Цвет дракона из предложенных: RED, YELLOW, BROWN", "Тип дракона из предложенных: WATER, UNDERGROUND, AIR, FIRE", "Какой Ваш дракон: CUNNING, EVIL, CHAOTIC_EVIL, FICKLE", "Глубина шахты, в которой обитает дракон, большая, либо равная нулю"};
    //Scanner sc = new Scanner(System.in);
    public final static WriteFile WRITE_FILE = new WriteFile();
    //ZonedDateTime currentTime = ZonedDateTime.now();
    /**
     * метод для чтения данных из xml файла
     */
    public void writeFileInMain() throws SQLException {
        Test.statmt.execute("CREATE TABLE if not exists Dragons (id SERIAL PRIMARY KEY, name TEXT, coord_x TEXT, coord_y TEXT, creation_date TEXT, age TEXT, color TEXT, type TEXT, character TEXT, cave TEXT, users TEXT);");
        ReadFile rf = new ReadFile();
        dragons = rf.readXml();
        //System.out.println(dragons);
    }
    /**
     * метда для команды clear
     * @param key
     */
    public void delDragon(ArrayList<Integer> key) throws SQLException {
        for (Integer i : key){
            if (dragons.get(i).split(",;;;,")[9].equals(Server.ClientHandler.socket())){
                dragons.remove(i);
            }
            Test.statmt.execute(("DELETE FROM Dragons WHERE id = " + i + " AND users = '" + Server.ClientHandler.socket() + "';"));
        }
        //new Table().table(Test.connection, "Dragons");
    }

    /**
     * Метод удаляющий объекта дракона по ключу
     * @param key
     */
    public void delDragon(int key) throws SQLException {
        if (dragons.get(key).split(",;;;,")[9].equals(Server.ClientHandler.socket())){
            dragons.remove(key);
        }
        Test.statmt.execute(("DELETE FROM Dragons WHERE id = " + key + " AND users = '" + Server.ClientHandler.socket() + "';"));
        //new Table().table(Test.connection, "Dragons");
    }

    public void insert(String string) throws SQLException {
        //System.out.println(string.split(":::")[1]);
        Set<Integer> keys = dragons.keySet();
        Dragon hf = new HandleFile().createObject(string.split(":::")[1]);
        //System.out.println("cladad");
        Integer maxkey;
        if (keys.isEmpty()){
            maxkey = 0;
            //System.out.println(0);
        }else{
            maxkey = max(keys);
            //System.out.println(maxkey);
        }
        //System.out.println(string.split(",;;;,")[9]);
        if (string.contains("insert")){
            //System.out.println(string.split(",;;;,")[9]);
            Test.statmt.execute("INSERT INTO Dragons(name, coord_x, coord_y, creation_date, age, color, type, character, cave, users) VALUES ('" + hf.getName() + "', '" + hf.getCoordinates().getX() + "', '" + hf.getCoordinates().getY() + "', '" + hf.getCreationDate() + "', '" + hf.getAge() + "', '" + hf.getColor() + "', '" + hf.getType() + "', '" + hf.getCharacter() + "', '" + hf.getCave().getDepth() + "', '" + Server.ClientHandler.socket() + "');");
            dragons.put(maxkey + 1, string.split(":::")[1] + ",;;;," + Server.ClientHandler.socket());
            Server.out_to_client += "Дракон создан";
        }else if(string.contains("update") && keys.contains(Integer.parseInt(string.split(" ")[1])) && string.split(",;;;,")[9].equals(Server.ClientHandler.socket())){
            //System.out.println(string.split(",;;;,")[9]);
            dragons.remove(Integer.parseInt(string.split(" ")[1]));
            //Test.statmt.execute(("DELETE FROM Dragons WHERE id = " + Integer.parseInt(string.split(" ")[1])));
            dragons.put(Integer.parseInt(string.split(" ")[1]), string.split(":::")[1]);
            Test.statmt.execute("UPDATE Dragons SET name = '" + hf.getName() + "', coord_x = '" + hf.getCoordinates().getX() + "', coord_y = '" + hf.getCoordinates().getY() + "', creation_date = '" + hf.getCreationDate() + "', age = '" + hf.getAge() + "', color = '" + hf.getColor() + "', type = '" + hf.getType() + "', character = '" + hf.getCharacter() + "', cave = '" + hf.getCave().getDepth() + "', users = '" + Server.ClientHandler.socket() + "' WHERE id = " + string.split(" ")[1] + ";");
            //Server.out_to_client += dragons.get(Integer.parseInt(string.split(" ")[1]));
            System.out.println(dragons.get(Integer.parseInt(string.split(" ")[1])));
            Server.out_to_client += "Дракон создан";
        }else{
            Server.out_to_client += "Ошибка с индексом, дракон не создан, возможно он не принадлежит Вам((";
        }
        //new Table().table(Test.connection, "Dragon");
    }

    /**
     * метод для удаления всех драконов
     */
    public void delDragon() throws SQLException {
        Set<Integer> keys = dragons.keySet();
        for (Integer dragon : keys){
            //System.out.println(dragons.get(dragon).split(",;;;,")[9] + ":::" + Server.ClientHandler.socket());
            if (dragons.get(dragon).split(",;;;,")[9].equals(Server.ClientHandler.socket())){
                dragons.remove(dragon);
            }
        }
        Test.statmt.execute("DELETE FROM Dragons WHERE users = '" + Server.ClientHandler.socket() + "';");
    }
    /*
    /**
     * метод для команды insert
     * @param key
     * @param update

    public void addNew(int key, boolean update) {
        String values = "";
        for (int i = 0; i <= 8; i++) {
            if (i == 3) {
                values += currentTime + ",";
            } else {
                System.out.print("(" + type_of_content[i] + ") " + content[i]);
                String value = sc.nextLine();
                while (true){
                    if (new ComandCheck().check(i, value)) {
                        values += value + ",";
                        //System.out.println(values);
                        break;
                    }else{
                        value = sc.nextLine();
                    }
                }
            }
        }
        //writeXmlFile(values);
        //System.out.println(values);
        if (update) {
            this.dragons.remove(key);
            this.dragons.put(key, values);
        }else {
            this.dragons.put(key, values);
        }
        System.out.println("Добавлен новый дракон " + key + " " + values.substring(0, values.length()-1));
    }
*/
    /**
     * метод для содания нового экземплярая дракона через txt файл
     * @param key
     * @param value
     */
    public void addNew(int key, String value) throws SQLException {
        Set<Integer> keys = dragons.keySet();
        Integer maxkey;
        if (keys.isEmpty()){
            maxkey = 0;
        }else{
            maxkey = max(keys);
        }
        //System.out.println("PLEASE");
        Dragon hf = new HandleFile().createObject(value);
        this.dragons.put(maxkey, value);
        Test.statmt.execute("INSERT INTO Dragons(name, coord_x, coord_y, creation_date, age, color, type, character, cave, users) VALUES ('" + hf.getName() + "', '" + hf.getCoordinates().getX() + "', '" + hf.getCoordinates().getY() + "', '" + hf.getCreationDate() + "', '" + hf.getAge() + "', '" + hf.getColor() + "', '" + hf.getType() + "', '" + hf.getCharacter() + "', '" + hf.getCave().getDepth() + "', '" + "artmuz" + "');");
        //System.out.println(value);
        //Server.out_to_client +=(value);
    }

    // методы для команды show и min_by_name

    /**
     * метод для команды show
     */
    public void printDragons() throws SQLException {

        Set<Integer> keys = dragons.keySet();
        for (Integer key : keys) {
            Server.out_to_client += ("id: " + key);
            int number_in_content = 0;
            for (String dragon_info : this.dragons.get(key).split(",;;;,")) {
                Server.out_to_client += (", " + content[number_in_content] + dragon_info);
                number_in_content++;
            }
            Server.out_to_client += "::";
        }


        /*
        resSet = Test.statmt.executeQuery("SELECT * FROM Dragons");
        int number_in_content = 0;
        String dragonCh = "";
        int user_id = 0;
        while(resSet.first()) {
            user_id = resSet.getInt("id");
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
            Server.out_to_client += ("id: " + user_id);
            for (String dragon_info : dragonCh.split(",;;;,")) {
                Server.out_to_client += (", " + content[number_in_content] + dragon_info);
                number_in_content++;
            }
            Server.out_to_client += "::";
        }

         */
    }

    /**
     * метод для команды min_by_name
     * @param id
     */

    public void printDragons(int id) {
        int number_in_content = 0;
        for (String dragon_info : this.dragons.get(id).split(",;;;,")) {
            Server.out_to_client += (content[number_in_content] + dragon_info + " ");
            number_in_content++;
        }
        Server.out_to_client += "::";
    }


    //методы для реализации последних команд, которые нужны для удаления объектов из коллекции

    /**
     * метод для удаления предществующих и последствующих объектов
     * @param id
     * @param up
     */
    public void dragonsWhoNeedToDel(int id, boolean up) throws SQLException {
        int l = dragons.get(id).length();
        //System.out.println("remove");
        Set<Integer> keys = dragons.keySet();
        ArrayList<String> dragonstr = new ArrayList<>();
        for (Integer key : keys){
            dragonstr.add(dragons.get(key));
        }
        Test.statmt.execute("DELETE FROM Dragons WHERE users = '" + Server.ClientHandler.socket() + "';");
        if (up) {
            //System.out.println(l);
            dragonstr.removeIf(element -> element.length() > l);
            dragons.entrySet().removeIf(entry -> entry.getValue().length() > l && entry.getValue().split(",;;;,")[9].equals(Server.ClientHandler.socket()));
            //System.out.println("remove1");
            for (Integer key : keys) {
                Dragon d = new HandleFile().createObject(",;;;," + dragons.get(key));
                Test.statmt.execute("INSERT INTO Dragons(name, coord_x, coord_y, creation_date, age, color, type, character, cave, users) VALUES ('" + d.getName() + "', '" + d.getCoordinates().getX() + "', '" + d.getCoordinates().getY() + "', '" + d.getCreationDate() + "', '" + d.getAge() + "', '" + d.getColor() + "', '" + d.getType() + "', '" + d.getCharacter()+"', '"+d.getCave().getDepth()+"', '" + Server.ClientHandler.socket()+"');");
            }
        }else {
            dragonstr.removeIf(element -> element.length() < l);
            dragons.entrySet().removeIf(entry -> entry.getValue().length() < l && entry.getValue().split(",;;;,")[9].equals(Server.ClientHandler.socket()));
            for (Integer key : keys) {
                Dragon d = new HandleFile().createObject(",;;;," + dragons.get(key));
                Test.statmt.execute("INSERT INTO Dragons(name, coord_x, coord_y, creation_date, age, color, type, character, cave, users) VALUES ('" + d.getName() + "', '" + d.getCoordinates().getX() + "', '" + d.getCoordinates().getY() + "', '" + d.getCreationDate() + "', '" + d.getAge() + "', '" + d.getColor() + "', '" + d.getType() + "', '" + d.getCharacter()+"', '"+d.getCave().getDepth()+"', '" + Server.ClientHandler.socket()+"');");
            }
        }
        //new Table().table(Test.connection, "Dragons");
        for (String i : dragonstr){
            System.out.println(i + " " + i.length());
            Server.out_to_client += (i + " " + i.length()) + "::";
        }
    }

    /**
     * метод для удаления объектов меньших, чем данный
     * @param id
     */
    public void dragonsWhoNeedToDel(int id) throws SQLException {
        dragons.entrySet().removeIf(entry -> entry.getKey() < id && entry.getValue().split(",;;;,")[9].equals(Server.ClientHandler.socket()));
        Test.statmt.execute(("DELETE FROM Dragons WHERE id < " + id));
        //new Table().table(Test.connection, "Dragons");
        /*
        Set<Integer> keys = dragons.keySet();
        for (Integer dragon : keys){
            //System.out.println(dragons.get(dragon).split(",;;;,")[9] + ":::" + Server.ClientHandler.socket());
            if (dragon < id && dragons.get(dragon).split(",;;;,")[9].equals(Server.ClientHandler.socket())){
                dragons.remove(dragon);
            }
        }
        Test.statmt.execute("DELETE FROM Dragons WHERE id < " + id  + " AND users = '" + Server.ClientHandler.socket() + "';");

         */
    }

    /**
     * метод для нахождения минимального имени
     */
    public void minByName() {
        int id = 0, lenName = 10000;
        Set<Integer> keys = dragons.keySet();
        for (Integer key : keys) {
            //System.out.println(dragons.get(key).split(",")[0].length());
            if (dragons.get(key).split(",;;;,")[0].length() < lenName) {
                //System.out.println(dragons.get(key).split(",")[0] + " " +dragons.get(key).split(",")[0].length() + " " + lenName);
                id = key;
                lenName = dragons.get(key).split(",;;;,")[0].length();
            }
        }
        printDragons(id);
    }

    // следующие 2 метода нужны для сортировки по какому-либо признаку (type и character)

    /**
     * метод для сортировки по type
     */
    public void sortByType() throws SQLException {
        LinkedHashMap<String, Integer> types = new LinkedHashMap<>();
        types.put("WATER", 0);
        types.put("UNDERGROUND", 0);
        types.put("AIR", 0);
        types.put("FIRE", 0);
        Set<Integer> keys = dragons.keySet();
        for (Integer key : keys) {
            String type = dragons.get(key).split(",;;;,")[6];
            //System.out.println(types.get(type) + type);
            types.put(type, types.get(type) + 1);
        }
        //System.out.println(types);
        ArrayList<String> sorted = bubbleSort(types);
        //System.out.println(sorted + "12");
        for (String type : sorted) {
            for (Integer key : keys) {
                if (dragons.get(key).split(",;;;,")[6].equals(type)) {
                    //System.out.println(key);
                    printDragons(key);
                }
            }
        }
    }

    /**
     * метод для сортировки по character
     */
    public void sortByCharacter() {
        LinkedHashMap<String, Integer> characters = new LinkedHashMap<>();
        characters.put("CUNNING", 0);
        characters.put("EVIL", 0);
        characters.put("CHAOTIC_EVIL", 0);
        characters.put("FICKLE", 0);
        Set<Integer> keys = dragons.keySet();
        for (Integer key : keys) {
            String type = dragons.get(key).split(",;;;,")[7];
            characters.put(type, characters.get(type) + 1);
        }
        ArrayList<String> sorted = bubbleSort(characters);
        for (String type : sorted) {
            for (Integer key : keys) {
                if (dragons.get(key).split(",;;;,")[7].equals(type)) {
                    printDragons(key);
                }
            }
        }
    }
    // метод для сортировки типов

    /**
     * метод для сортировки по типу или персонажу, от большего к меньшему
     * @param trash
     * @return sorted
     */
    public ArrayList<String> bubbleSort(LinkedHashMap<String, Integer> trash) {
        Set<String> keys = trash.keySet();
        ArrayList<String> sort = new ArrayList<>(keys);
        int x = 4;
        while (x > 1)
            for (int i = 1; i <= 3; i++) {
                int first = trash.get(sort.get(i-1));
                int second = trash.get(sort.get(i));
                System.out.println(trash + " :: " + sort);
                if (first >= second) {
                    System.out.println(x);
                    x--;
                }else{
                    String f = sort.get(i-1);
                    String s = sort.get(i);
                    sort.set(i-1, s);
                    sort.set(i, f);
                }
            }
        return sort;
    }

    /**
     * метод для записи в xml файл
     */
    public void writeXmlFile() throws SQLException {
    /*
        try (FileWriter writer = new FileWriter("C:\\Users\\admin\\IdeaProjects\\lab2sem\\src\\lab5\\Server\\file\\lab5.xml")) {
            writer.write("<Dragons>\n");
            Set<Integer> keys = dragons.keySet();
            for (Integer key : keys) {
                Dragon dragon = new HandleFile().createObject(",;;;," + dragons.get(key), key);
                writer.write("\t<Dragon>\n");
                writer.write("\t\t<id>" + dragon.getId() + "</id>\n");
                writer.write("\t\t<name>" + dragon.getName() + "</name>\n");
                writer.write("\t\t<coordinates>\n");
                writer.write("\t\t\t<x>" + dragon.getCoordinates().getX() + "</x>\n");
                writer.write("\t\t\t<y>" + dragon.getCoordinates().getY() + "</y>\n");
                writer.write("\t\t</coordinates>\n");
                writer.write("\t\t<creationdate>" + dragon.getCreationDate() + "</creationdate>\n");
                writer.write("\t\t<age>" + dragon.getAge() + "</age>\n");
                writer.write("\t\t<color>" + dragon.getColor() + "</color>\n");
                writer.write("\t\t<type>" + dragon.getType() + "</type>\n");
                writer.write("\t\t<character>" + dragon.getCharacter() + "</character>\n");
                writer.write("\t\t<cave>" + dragon.getCave().getDepth() + "</cave>\n");
                writer.write("\t</Dragon>\n");
                Server.out_to_client += ("Данные успешно записаны в файл") + "::";
            }
            writer.write("</Dragons>\n");
        } catch (FileNotFoundException e) {
            Server.out_to_client += ("Файл не найден: " + e.getMessage());
        } catch (IOException e) {
            Server.out_to_client += ("Ошибка при записи в файл: " + e.getMessage());
        }

*/
        /////////////////////////////////////////////////////////
        Set<Integer> keys = dragons.keySet();
        Test.statmt.execute("DROP TABLE Dragons;");
        Test.statmt.execute("CREATE TABLE Dragons (id SERIAL PRIMARY KEY, name TEXT, coord_x TEXT, coord_y TEXT, creation_date TEXT, age TEXT, color TEXT, type TEXT, character TEXT, cave TEXT, users TEXT);");
        System.out.println(keys);
        for (Integer key : keys) {
            //System.out.println(dragons.get(key));
            Dragon d = new HandleFile().createObject(dragons.get(key));
            Test.statmt.execute("INSERT INTO Dragons(name, coord_x, coord_y, creation_date, age, color, type, character, cave) VALUES ('" + d.getName() + "', '" + d.getCoordinates().getX() + "', '" + d.getCoordinates().getY() + "', '" + d.getCreationDate() + "', '" + d.getAge() + "', '" + d.getColor() + "', '" + d.getType() + "', '" + d.getCharacter() + "', '" + d.getCave().getDepth()+"', '" + dragons.get(key).split(",;;;,")[9]+"');");
        }
        Server.out_to_client += ("файл сохранен\n");
    }
}