package lab5.Client.Types;
import java.util.regex.Pattern;

/**
 * класс для проверки команды на валидность
 */
public class ComandCheck {
    /**
     * Конструктор класса ComandCheck
     */
    public ComandCheck() {
    }

    /**
     * метод реализующий проверку комнады
     * @param id
     * @param data
     * @return
     */
    public String check(int id, String data) {
        if (id == 0 && data.isEmpty()) { // name
            return "Ошибка: поле имя не должно быть пустым";
        }  if (id == 1) { // x
            try {
                int x = (int) Double.parseDouble(data);
                System.out.println(x);
            } catch (NumberFormatException e) {
                System.out.println("значение x должно быть integer");
                return "Ошибка: значение x должно быть integer";
            }
        }  if (id == 2) { // y
            try {
                int y = (int) Double.parseDouble(data);
                System.out.println(y);
            } catch (NumberFormatException e) {
                System.out.println("значение y должно быть integer");
                return "Ошибка: значение y должно быть integer";
            }
        }if (id == 4){ // age
            try {
                int nulle = Integer.parseInt(data);
                if (nulle <= 0){
                    System.out.println("Учимся читать, что пишется в строке");
                    return "Ошибка: значение age должно быть integer и больше нуля";
                }
            } catch (NumberFormatException e) {
                System.out.println("значение age должно быть integer");
                return "Ошибка: значение age должно быть integer";
            }
        }  if (id == 5){ // color
            try {
                Color.valueOf(data);
            } catch (IllegalArgumentException e) {
                System.out.println("значение Color должно быть из предложенных");
                return "Ошибка: значение Color должно быть из предложенных";
            }
        }  if (id == 6){ // type
            try {
                DragonType.valueOf(data);
            } catch (IllegalArgumentException e) {
                System.out.println("значение Type должно быть из предложенных");
                return "Ошибка: значение Type должно быть из предложенных";
            }
        }  if (id == 7){ // character
            try {
                DragonCharacter.valueOf(data);
            } catch (IllegalArgumentException e) {
                System.out.println("значение Character должно быть из предложенных");
                return "Ошибка: значение Character должно быть из предложенных";
            }
        }  if (id == 8){ // cave
            try {
                int nulle = Integer.parseInt(data);
                if (nulle < 0){
                    System.out.println("Учимся читать, что пишется в строке");
                    return "Ошибка: глубина должна быть меньше нуля";
                }
            } catch (NumberFormatException e) {
                System.out.println("значение cave должно быть integer");
                return "Ошибка: значение cave должно быть integer";
            }
        }
        return "Ok";
    }
}
