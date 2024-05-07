package lab5.Server.commands;

import lab5.Server.file.WriteFile;

import java.sql.SQLException;

/**
 * класс реализующий команду clear
 */
public class Clear extends Command {
    /**
     * Конструктор класса Clear.
     * Устанавливает название команды.
     */
    public Clear(){
        super("clear");
    }
    /**
     * Метод для обработки команды и вызова метода команды
     * @param EnteredCommand
     */
    public void execute(String EnteredCommand) throws SQLException {
        if (checkCommand(EnteredCommand)) {
            WriteFile.WRITE_FILE.delDragon();
        } else {
            System.out.println("Команда не найдена. Введите \"help\" для справки");
        }
    }
}
