module com.example.demo8 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.example.demo8 to javafx.fxml;
    exports com.example.demo8;
}