module com.example.javaend {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javaend to javafx.fxml;
    exports com.example.javaend;
}