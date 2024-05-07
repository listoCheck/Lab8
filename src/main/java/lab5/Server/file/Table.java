package lab5.Server.file;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Table {

    // Метод для получения строк из таблицы
    public void table(Connection connection, String tableName) throws SQLException {
        List<String> rows = getRowsFromTable(connection, tableName);

        // Перемещение пустых строк в конец списка
        List<String> shiftedRows = shiftRows(rows);

        // Обновление порядка строк в базе данных
        updateRowsInDatabase(connection, tableName, shiftedRows);
    }

    // Метод для получения строк из таблицы
    private static List<String> getRowsFromTable(Connection connection, String tableName) throws SQLException {
        List<String> rows = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            while (resultSet.next()) {
                rows.add(resultSet.getString("column_name")); // Замените "column_name" на имя нужного столбца
            }
        }
        return rows;
    }

    // Метод для сдвига строк (перемещение пустых строк в конец списка)
    private static List<String> shiftRows(List<String> rows) {
        List<String> nonEmptyRows = new ArrayList<>();
        List<String> emptyRows = new ArrayList<>();

        // Разделение строк на непустые и пустые
        for (String row : rows) {
            if (!row.isEmpty()) {
                nonEmptyRows.add(row);
            } else {
                emptyRows.add(row);
            }
        }

        // Объединение непустых и пустых строк
        nonEmptyRows.addAll(emptyRows);
        return nonEmptyRows;
    }

    // Метод для обновления порядка строк в базе данных
    private static void updateRowsInDatabase(Connection connection, String tableName, List<String> rows) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE " + tableName); // Очистка таблицы перед обновлением
            for (String row : rows) {
                statement.executeUpdate("INSERT INTO " + tableName + " (column_name) VALUES ('" + row + "')"); // Замените "column_name" на имя нужного столбца
            }
        }
    }
}
