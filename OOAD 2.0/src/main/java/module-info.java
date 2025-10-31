module bankingsystem.ooadassignment {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql; // ✅ Needed for JDBC

    // Optional UI libraries
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    // Open your package to FXML
    opens bankingsystem.ooadassignment to javafx.fxml;
    exports bankingsystem.ooadassignment;
}
