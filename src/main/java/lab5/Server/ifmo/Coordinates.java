package lab5.Server.ifmo;

public class Coordinates {
    private float x;
    private double y;

    /**
     * конструктор класса координат
     * @param x
     * @param y
     */
    public Coordinates(float x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * метод возвращения координаты x
     * @return
     */
    public float getX() {
        return x;
    }
    /**
     * метод возвращения координаты y
     * @return
     */
    public double getY() {
        return y;
    }
}