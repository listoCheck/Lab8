package lab5.Server.commands;

import lab5.Server.file.WriteFile;

import java.sql.SQLException;

/**
 * класс команды remove
 */
public class Remove extends Command{
    private int key;
    /**
     * Конструктор класса Remove.
     * Устанавливает название команды.
     */
    public Remove(){
        super("remove");
    }
    /**
     * Метод для обработки команды и вызова метода команды
     * @param EnteredCommand
     */
    public void execute(String EnteredCommand, int key) throws SQLException {
        if (checkCommand(EnteredCommand)) {
            WriteFile.WRITE_FILE.delDragon(key);
        } else {
            System.out.println("Команда не найдена. Введите \"help\" для справки");
        }
    }
}
