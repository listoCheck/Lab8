package lab5.Server.ifmo;

import java.time.ZonedDateTime;
public class Dragon implements Comparable<Dragon> {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int age; //Значение поля должно быть больше 0
    private Color color; //Поле не может быть null
    private DragonType type; //Поле не может быть null
    private DragonCharacter character; //Поле может быть null
    private DragonCave cave; //Поле может быть null

    /**
     * конструктор класса дракон
     * @param id
     * @param name
     * @param coordinates
     * @param creationDate
     * @param age
     * @param color
     * @param type
     * @param character
     * @param cave
     */
    public Dragon(int id, String name, Coordinates coordinates, java.time.ZonedDateTime creationDate, int age, Color color, DragonType type, DragonCharacter character, DragonCave cave){
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.age = age;
        this.color = color;
        this.type = type;
        this.character = character;
        this.cave = cave;
    }

    /**
     * метод возвращающий id
     * @return
     */
    public int getId() {
        return id;
    }
    /**
     * метод возвращающий name
     * @return
     */
    public String getName() {
        return name;
    }
    /**
     * метод возвращающий координаты
     * @return
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }
    /**
     * метод возвращающий дату создания
     * @return
     */
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }
    /**
     * метод возвращающий возраст
     * @return
     */
    public int getAge() {
        return age;
    }
    /**
     * метод возвращающий цвет
     * @return
     */
    public Color getColor() {
        return color;
    }
    /**
     * метод возвращающий id
     * @return
     */
    public DragonType getType() {
        return type;
    }
    /**
     * метод возвращающий персонажа
     * @return
     */
    public DragonCharacter getCharacter() {
        return character;
    }
    /**
     * метод возвращающий глубину шахты
     * @return
     */
    public DragonCave getCave() {
        return cave;
    }
    /**
     * метод выводящий данные о драконе
     * @return info
     */
    @Override
    public String toString() {
        String info = "";
        info += "Dragon\n";
        info += "\tId: " + getId() + "\n";
        info += "\tИмя: " + getName() + "\n";
        info += "\tКоординаты: " + getCoordinates() + "\n";
        info += "\tДата создания: " + getCreationDate() + "\n";
        info += "\tВозраст: " + getAge() + "\n";
        info += "\tЦвет: " + getColor() + "\n";
        info += "\tТип: " + getType() + "\n";
        info += "\tХарактер: " + getCharacter() + "\n";
        info += "\tШахта: " + getCave() + "\n";
        if (cave != null) {
            info += "\tРазмер головы: " + cave.getDepth() + "\n";
        }
        info += "\tКоординаты:\n";
        info += "\t\tx = " + coordinates.getX() + "\n";
        info += "\t\ty = " + coordinates.getY();
        return info;
    }

    @Override
    public int compareTo(Dragon other) {
        return Integer.compare(this.id, other.id);
    }
}
