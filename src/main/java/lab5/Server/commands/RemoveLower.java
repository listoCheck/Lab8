package lab5.Server.commands;

import lab5.Server.file.WriteFile;

import java.sql.SQLException;

/**
 * метод для команды remove lower
 */
public class RemoveLower extends Command {
    /**
     * Конструктор класса RemoveLower.
     * Устанавливает название команды.
     */
    public RemoveLower() {
        super("remove_lower");
    }
    /**
     * Метод для обработки команды и вызова метода команды
     * @param EnteredCommand
     */
    @Override
    public void execute(String EnteredCommand, int id) throws SQLException {
        if (checkCommand(EnteredCommand)) {
            WriteFile.WRITE_FILE.dragonsWhoNeedToDel(id, false);
        } else {
            System.out.println("Команда не найдена. Введите \"help\" для справки");
        }
    }
}