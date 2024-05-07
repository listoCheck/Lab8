package lab5.Client.Types;

//import lab5.Server.file.WriteFile;

import lab5.Client.Types.ComandCheck;

import java.time.ZonedDateTime;
import java.util.Scanner;
import java.util.regex.Pattern;

public class NewDragon {
    public String[] content = {"name: ", "coordinate_x: ", "coordinate_y: ", "creationdate: ", "age: ", "color: ", "type: ", "character: ", "cave: ", ""};
    String[] type_of_content = {"Введите имя дракона", "Координата х, где находится драков", "Координата у, где находится драков", "Вводится автоматически", "Возраст дракона, больший нуля", "Цвет дракона из предложенных: RED, YELLOW, BROWN", "Тип дракона из предложенных: WATER, UNDERGROUND, AIR, FIRE", "Какой Ваш дракон: CUNNING, EVIL, CHAOTIC_EVIL, FICKLE", "Глубина шахты, в которой обитает дракон, большая, либо равная нулю"};
    Scanner sc = new Scanner(System.in);
    //public final static WriteFile WRITE_FILE = new WriteFile();
    ZonedDateTime currentTime = ZonedDateTime.now();

    public String addNew() {
        String values = "";
        for (int i = 0; i <= 8; i++) {
            if (i == 3) {
                values += currentTime + ",;;;,";
            } else {
                System.out.print("(" + type_of_content[i] + ") " + content[i]);
                String value = sc.nextLine();
                if (i == 0){
                    Pattern pattern = Pattern.compile("[^a-zA-Z0-9а-яА-Я\\s]");
                    value = pattern.matcher(value).replaceAll("");
                }
                while (true) {
                    if (new ComandCheck().check(i, value).contains("Ok")) {
                        if (i == 8){
                            values += value;
                        }else {
                            values += value + ",;;;,";
                        }
                        //System.out.println(values);
                        break;

                    } else {
                        value = sc.nextLine();
                    }
                }
            }
        }
        //System.out.println(values);
        return values;
    }
}


