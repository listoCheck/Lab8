package lab5.Server.file;

import lab5.Server.ifmo.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Класс для обработки файла (для создания полей класса dragon и создания экземпляра)
 */
public class HandleFile {
    ZonedDateTime currentTime = ZonedDateTime.now();
    /**
     * метод создающий и возвращающий объект класса дракон
     * @param data
     * @return dragon
     */
    public Dragon createObject(String data) {
        Stream<String> stream = Stream.of(data);
        Dragon dragon =  stream
                .map(s -> s.split(",;;;,"))
                .map(info -> {
                    String name = info[0];
                    Coordinates coordinates = new Coordinates((int) Double.parseDouble(info[1]), (int) Double.parseDouble(info[2]));
                    ZonedDateTime creationDate = currentTime;
                    int age = Integer.parseInt(info[4]);
                    Color color = Color.valueOf(info[5]);
                    DragonType type = DragonType.valueOf(info[6]);
                    DragonCharacter character = DragonCharacter.valueOf(info[7]);
                    DragonCave cave = new DragonCave(new DragonCave(Long.parseLong(info[8])).getDepth());
                    return new Dragon(1, name, coordinates, creationDate, age, color, type, character, cave);
                })
                .findFirst()
                .orElse(null);
        //System.out.println("Выполнил");
        return dragon;
    }

}
