package lab5.Server.ifmo;

public class DragonCave {
    private Long depth; //Поле может быть null
    /**
     * конструктор класса DragonCave
     * @return depth
     */
    public DragonCave(long depth){
        this.depth = depth;
    }
    /**
     * метод возвращающий глубину
     * @return depth
     */
    public Long getDepth() {
        return depth;
    }

}