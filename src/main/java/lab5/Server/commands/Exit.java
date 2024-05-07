package lab5.Server.commands;

import lab5.Server.Server;
import lab5.Server.Test;
import lab5.Server.file.WriteFile;
import java.sql.SQLException;

/**
 * класс, который реализует команду выхода
 */
public class Exit extends Command{
    /**
     * Конструктор класса Exit.
     * Устанавливает название команды.
     */
    public Exit(){
        super("exit_server");
    }
    /**
     * Метод для обработки команды и вызова метода команды
     * @param EnteredCommand
     */
    @Override
    public void execute(String EnteredCommand) throws SQLException {
        if (checkCommand(EnteredCommand)) {
            System.exit(0);
        } else {
            System.out.println("Команда не найдена. Введите \"help\" для справки");
        }
    }
}
