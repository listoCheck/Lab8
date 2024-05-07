package lab5.Server.file;



import java.sql.SQLException;
import java.util.ArrayList;

public interface Functional{
    void delDragon() throws SQLException;

    void delDragon(ArrayList<Integer> key) throws SQLException;
    void delDragon(int key) throws SQLException;

    //void addNew(int key, boolean update);

    void addNew(int key, String value) throws SQLException;

    void printDragons() throws SQLException;

    void printDragons(int id);

    void dragonsWhoNeedToDel(int id, boolean up) throws SQLException;

    void dragonsWhoNeedToDel(int id) throws SQLException;

    void minByName();

    void sortByCharacter();

    void writeXmlFile() throws SQLException;

}
