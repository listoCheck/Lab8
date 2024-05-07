package lab5.Server.commands;

import lab5.Server.file.WriteFile;

import java.sql.SQLException;

/**
 * класс для команды insert
 */
public class Insert extends Command{
    int key;
    /**
     * Конструктор класса Insert.
     * Устанавливает название команды.
     */
    public Insert(){
        super("insert");
    }
    /**
     * Метод для обработки команды и вызова метода команды
     * @param EnteredCommand
     */
    public void execute(String EnteredCommand, int key) throws SQLException {
        if (checkCommand(EnteredCommand)) {
            WriteFile.WRITE_FILE.addNew(key, "");
        } else if(this.key == 0){
            System.out.println("Команда не найдена. Введите \"help\" для справки");
        }
    }
}
