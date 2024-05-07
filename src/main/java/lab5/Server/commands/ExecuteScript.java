package lab5.Server.commands;

import java.sql.SQLException;

/**
 * класс, который обрабатывет скрипт из файлы
 */
public class ExecuteScript extends Command{
    /**
     * Конструктор класса ExecuteScript.
     * Устанавливает название команды.
     */
    public ExecuteScript(){
        super("execute_script");
    }
    /**
     * Метод для обработки команды и вызова метода команды
     * @param EnteredCommand
     */
    public void execute(String EnteredCommand, String file_path) throws SQLException {
        if (checkCommand(EnteredCommand)) {
            System.out.println("Найден путь: " + file_path);
            new Console().start("", true, file_path);
        } else {
            System.out.println("Команда не найдена. Введите \"help\" для справки");
        }
    }
}
