package lab5.Server.commands;

import lab5.Server.file.WriteFile;

import java.sql.SQLException;

/**
 * класс для команды save
 */
public class Save extends Command {
    /**
     * Конструктор класса Save.
     * Устанавливает название команды.
     */
    public Save(){
        super("save");
    }
    /**
     * Метод для обработки команды и вызова метода команды
     * @param EnteredCommand
     */
    @Override
    public void execute(String EnteredCommand) throws SQLException {
        if (checkCommand(EnteredCommand)) {
            WriteFile.WRITE_FILE.writeXmlFile();
        } else {
            System.out.println("Команда не найдена. Введите \"help\" для справки");
        }
    }
}
