package com.progetto.packController;

import com.progetto.packModel.Model;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

public class ControllerRegistrazioneTest {
    private ControllerRegistrazione controller;
    private Model mockModel = Model.getInstance();

    @Before
    public void setUp() {
        controller = new ControllerRegistrazione();
        mockModel = Model.getInstance();

        // componenti javafx
        controller.cfUtente = new TextField();
        controller.showPwd = new RadioButton();
        controller.choiceBox = new ChoiceBox<>();
        controller.cittaUtente = new TextField();
        controller.cognomeUtente = new TextField();
        controller.confermabutton = new Button();
        controller.dataNascita = new DatePicker();
        controller.hiddenTextfield = new TextField();
        controller.mailUtente = new TextField();
        controller.nomeUtente = new TextField();
        controller.passwordUtente = new PasswordField();
        controller.statoUtente = new TextField();
        controller.gridPane = new GridPane();
        controller.tessSanitaria = new TextField();
    }

    @Test
    public void testCheckInputs_datiValidi() throws SQLException, NoSuchAlgorithmException {
        String nomeUtente = "Leonardo";
        String cognomeUtente = "Cardillo";
        String cittaUtente = "Crotone";
        String statoUtente = "Italia";
        String cfUtente = "LRNCRD67D23M150F";
        String tessSanitaria = "54236";
        String mailUtente = "leonardo.cardillo@gmail.com";
        String passwordUtente = "Ciao123Ciao!";
        String choiceBoxValue = "Cittadino italiano";
        Collection<String> validationResult = new ArrayList<>();

        assertFalse(mockModel.checkRegistrazione(nomeUtente, cognomeUtente, null, cittaUtente, statoUtente,
                cfUtente, tessSanitaria, mailUtente, passwordUtente, choiceBoxValue).isEmpty());
        assertTrue(mockModel.isStatusAnagrafica());
        assertFalse(mockModel.isTabellaCittadino());

        controller.nomeUtente.setText(nomeUtente);
        controller.cognomeUtente.setText(cognomeUtente);
        controller.dataNascita.setValue(null);
        controller.cittaUtente.setText(cittaUtente);
        controller.statoUtente.setText(statoUtente);
        controller.cfUtente.setText(cfUtente);
        controller.tessSanitaria.setText(tessSanitaria);
        controller.mailUtente.setText(mailUtente);
        controller.passwordUtente.setText(passwordUtente);
        controller.choiceBox.setItems(FXCollections.observableArrayList(choiceBoxValue));
        controller.choiceBox.setValue(choiceBoxValue);

        controller.checkInputs(new ActionEvent());

        assert(!(mockModel.checkRegistrazione(nomeUtente, cognomeUtente, null, cittaUtente, statoUtente,
                cfUtente, tessSanitaria, mailUtente, passwordUtente, choiceBoxValue)).isEmpty());
        assertTrue(controller.confermabutton.isVisible());
        assertFalse(controller.gridPane.isMouseTransparent());
    }

    @Test
    public void testCheckInputs_datiNonValidi() throws SQLException, NoSuchAlgorithmException {
        String nomeUtente = "";
        String cognomeUtente = "Rossi";
        String cittaUtente = "Parigi";
        String statoUtente = "Roma";
        String cfUtente = "CFDDSS12F34Z678F";
        String tessSanitaria = "93841";
        String mailUtente = "giovanni.rossi@example.com";
        String passwordUtente = "password";
        String choiceBoxValue = "Cittadino straniero residente in italia";
        Collection<String> validationResult = new ArrayList<>();
        validationResult.add("Campo1");
        validationResult.add("Campo2");
        validationResult.add("Campo3");

        assert(mockModel.checkRegistrazione(nomeUtente, cognomeUtente, null, cittaUtente, statoUtente,
                cfUtente, tessSanitaria, mailUtente, passwordUtente, choiceBoxValue).isEmpty());
        assertFalse(mockModel.isStatusAnagrafica());
        assertFalse(mockModel.isTabellaCittadino());

        controller.nomeUtente.setText(nomeUtente);
        controller.cognomeUtente.setText(cognomeUtente);
        controller.dataNascita.setValue(null);
        controller.cittaUtente.setText(cittaUtente);
        controller.statoUtente.setText(statoUtente);
        controller.cfUtente.setText(cfUtente);
        controller.tessSanitaria.setText(tessSanitaria);
        controller.mailUtente.setText(mailUtente);
        controller.passwordUtente.setText(passwordUtente);
        controller.choiceBox.setItems(FXCollections.observableArrayList(choiceBoxValue));
        controller.choiceBox.setValue(choiceBoxValue);

        controller.checkInputs(new ActionEvent());

        assert(mockModel.checkRegistrazione(nomeUtente, cognomeUtente, null, cittaUtente, statoUtente,
                cfUtente, tessSanitaria, mailUtente, passwordUtente, choiceBoxValue)).isEmpty();
        assertFalse(controller.confermabutton.isVisible());
        assertTrue(controller.gridPane.isMouseTransparent());
    }
}
