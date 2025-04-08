module com.example.sistema_de_funcionario {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.example.sistema_de_funcionario to javafx.fxml;
    opens com.example.sistema_de_funcionario.controller to javafx.fxml;

    exports com.example.sistema_de_funcionario;
}
