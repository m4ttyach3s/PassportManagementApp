package com.progetto.packController;

import com.progetto.packModel.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class EntryPoint extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Model model = new Model();
        ControllerFirstView controller;
        FXMLLoader loader = new FXMLLoader(EntryPoint.class.getResource("/com/progetto/packView/first-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        controller = loader.getController();
        controller.setModel(model);

        stage.setTitle("Gestione Passaporti");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}