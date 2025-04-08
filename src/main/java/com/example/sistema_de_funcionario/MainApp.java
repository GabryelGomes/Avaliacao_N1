package com.example.sistema_de_funcionario;

import com.example.sistema_de_funcionario.utils.PATHFXML;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.File;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        File fxmlFile = new File(PATHFXML.MAIN);
        FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setTitle("Sistema de Funcionário");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
