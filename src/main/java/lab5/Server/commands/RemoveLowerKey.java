package lab5.Server.commands;

import lab5.Server.file.WriteFile;

import java.sql.SQLException;

/**
 * класс для комады remove_lower_key
 */
public class RemoveLowerKey extends Command{
    /**
     * Конструктор класса RemoveLowerKey.
     * Устанавливает название команды.
     */
    public RemoveLowerKey(){
        super("remove_lower_key");
    }
    /**
     * Метод для обработки команды и вызова метода команды
     * @param EnteredCommand
     */
    @Override
    public void execute(String EnteredCommand, int key) throws SQLException {
        if (checkCommand(EnteredCommand)) {
            WriteFile.WRITE_FILE.dragonsWhoNeedToDel(key);
        } else {
            System.out.println("Команда не найдена. Введите \"help\" для справки");
        }
    }
}
