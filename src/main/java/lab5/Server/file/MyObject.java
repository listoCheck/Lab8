package lab5.Server.file;

import java.io.Serializable;

public class MyObject implements Serializable {
    private static final long serialVersionUID = 1L; // Идентификатор версии сериализованного класса
    private String name;

    // Конструктор
    public MyObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
