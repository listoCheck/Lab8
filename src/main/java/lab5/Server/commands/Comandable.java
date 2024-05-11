package lab5.Server.commands;

public interface Comandable {
    void execute(String EnteredCommand);
    void execute(String EnteredCommand, int id);
}
