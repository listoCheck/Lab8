module com.example.lab8 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.compiler;

    opens com.example.lab8 to javafx.fxml;
    opens lab5.Client.Types to javafx.base;
    exports com.example.lab8; // Важно экспортировать пакет com.example.lab8
    exports lab5.Client;
}
