package lab5.Server.commands;

import lab5.Server.file.WriteFile;

import java.sql.SQLException;

/**
 * класс для команды update
 */
public class Update extends Command {
    int key = 0;
    /**
     * Конструктор класса Update.
     * Устанавливает название команды.
     */
    public Update(){
        super("update");
    }
    /**
     * Метод для обработки команды и вызова метода команды
     * @param EnteredCommand
     */
    public void execute(String EnteredCommand, int key) throws SQLException {
        if (checkCommand(EnteredCommand)) {
            if (checkCommand(EnteredCommand)) {
                WriteFile.WRITE_FILE.addNew(key, "");
            } else if(this.key == 0){
                System.out.println("Команда не найдена. Введите \"help\" для справки");
            }
        } else {
            System.out.println("Команда не найдена. Введите \"help\" для справки");
        }
    }
}
