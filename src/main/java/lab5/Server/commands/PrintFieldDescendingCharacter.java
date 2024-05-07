package lab5.Server.commands;

import lab5.Server.file.WriteFile;

/**
 * класс для сортировки по параметру character
 */
public class PrintFieldDescendingCharacter extends Command{
    /**
     * Конструктор класса PrintFieldDescendingCharacter.
     * Устанавливает название команды.
     */
    public PrintFieldDescendingCharacter(){
        super("print_field_descending_character");
    }
    /**
     * Метод для обработки команды и вызова метода команды
     * @param EnteredCommand
     */
    @Override
    public void execute(String EnteredCommand) {
        if (checkCommand(EnteredCommand)) {
            WriteFile.WRITE_FILE.sortByCharacter();
        } else {
            System.out.println("Команда не найдена. Введите \"help\" для справки");
        }
    }
}
