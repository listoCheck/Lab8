package lab5.Server.commands;

import lab5.Server.file.WriteFile;

import java.sql.SQLException;

/**
 * класс для сортировки по параметру type
 */
public class PrintFieldDescendingType extends Command{
    /**
     * Конструктор класса PrintFileldDescendingType.
     * Устанавливает название команды.
     */
    public PrintFieldDescendingType(){
        super("print_field_descending_type");
    }
    /**
     * Метод для обработки команды и вызова метода команды
     * @param EnteredCommand
     */
    @Override
    public void execute(String EnteredCommand) throws SQLException {
        if (checkCommand(EnteredCommand)) {
            WriteFile.WRITE_FILE.sortByType();
        } else {
            System.out.println("Команда не найдена. Введите \"help\" для справки");
        }
    }

}
