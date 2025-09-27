module com.example.demo17 {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.example.demo17;

    opens com.example.demo17 to javafx.fxml;
    opens com.example.demo17.controlador to javafx.fxml;
}