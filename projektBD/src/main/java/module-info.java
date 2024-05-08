module com.example.projektbd {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires AnimateFX;

    opens com.example.projektbd to javafx.fxml;
    exports com.example.projektbd;
}