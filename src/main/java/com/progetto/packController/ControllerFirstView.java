package com.progetto.packController;

import com.progetto.packModel.Model;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerFirstView implements Initializable {
    private static final String IDLE_BUTTON_STYLE = "-fx-background-color: transparent";
    private static final String HOVERED_BUTTON_STYLE = "-fx-background-color: -fx-shadow-highlight-color, -fx-inner-border, -fx-body-color;";
    private Model model = Model.getInstance();
    @FXML
    private Button accedi_cittadini;
    @FXML
    private Button accedi_dipendenti;
    @FXML
    private Button registrati_prima;

    @FXML
    void accediCittadini(ActionEvent eventAccediCittadino) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/ViewCittadino/accedi-cittadino-view.fxml"));
        Stage stage = (Stage) ((Node) eventAccediCittadino.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void accediDipendenti(ActionEvent eventAccediDipendente) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/ViewDipendente/accedi-dipendente-view.fxml"));
        Stage stage = (Stage) ((Node) eventAccediDipendente.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void registrati(ActionEvent eventRegistrazione) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/ViewRegistrazione/registrazione-view.fxml"));
        Stage stage = (Stage) ((Node) eventRegistrazione.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accedi_dipendenti.setStyle(IDLE_BUTTON_STYLE);
        accedi_cittadini.setStyle(IDLE_BUTTON_STYLE);
        registrati_prima.setStyle(IDLE_BUTTON_STYLE);

        accedi_cittadini.setOnMouseEntered(e -> {
            accedi_cittadini.setStyle(HOVERED_BUTTON_STYLE);
            playAnimations(accedi_cittadini);
        });
        accedi_dipendenti.setOnMouseEntered(e -> {
            accedi_dipendenti.setStyle(HOVERED_BUTTON_STYLE);
            playAnimations(accedi_dipendenti);
        });
        registrati_prima.setOnMouseEntered(e -> {
            registrati_prima.setStyle(HOVERED_BUTTON_STYLE);
            playAnimations(registrati_prima);
        });

        accedi_cittadini.setOnMouseExited(e -> accedi_cittadini.setStyle(IDLE_BUTTON_STYLE));
        accedi_dipendenti.setOnMouseExited(e -> accedi_dipendenti.setStyle(IDLE_BUTTON_STYLE));
        registrati_prima.setOnMouseExited(e -> registrati_prima.setStyle(IDLE_BUTTON_STYLE));
    }

    private void playAnimations(Button button) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), button);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);

        scaleTransition.play();
    }
    protected void setModel(Model model) {
        this.model = model;
    }
}