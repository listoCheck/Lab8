package lab5.Client.Types;

public class Dragon {
    private Integer id;
    private String name;
    private Double x;
    private Double y;
    private String date;
    private Integer age;
    private String color;
    private String type;
    private String character;
    private Integer cave;
    private String user;
    public Dragon(Integer id, String name, Double x, Double y, String date, Integer age, String color, String type, String character, Integer cave, String user){
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.date = date;
        this.age = age;
        this.color = color;
        this.type = type;
        this.character = character;
        this.cave = cave;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public Integer getAge() {
        return age;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public String getCharacter() {
        return character;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public String getDate() {
        return date;
    }

    public Integer getCave() {
        return cave;
    }

    public String getUser() {
        return user;
    }
}
