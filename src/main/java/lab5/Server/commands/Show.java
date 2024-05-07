package lab5.Server.commands;

import lab5.Server.file.WriteFile;

import java.sql.SQLException;

/**
 * класс для команды show
 */
public class Show extends Command{
    /**
     * Конструктор класса Show.
     * Устанавливает название команды.
     */
    public Show(){
        super("show");
    }
    /**
     * Метод для обработки команды и вызова метода команды
     * @param EnteredCommand
     */
    @Override
    public void execute(String EnteredCommand) throws SQLException {
        if (checkCommand(EnteredCommand)) {
            WriteFile.WRITE_FILE.printDragons();
        } else {
            System.out.println("Команда не найдена. Введите \"help\" для справки");
        }
    }
    @Override
    public void execute(String EnteredCommand, int id) {

    }
}
