package com.progetto.packController;
import com.progetto.packModel.Model;
import javafx.event.ActionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ControllerDipendenteTest {

    private ControllerDipendente controller;
    private Model mockModel;

    @BeforeEach
    void setUp() {
        controller = new ControllerDipendente();
        mockModel = Model.getInstance();
    }

    @Test
    void testNuovoDipendente_datiValidi() throws SQLException, NoSuchAlgorithmException {

        String matricola = "AD123456";
        String nome = "Gaetano";
        String cognome = "Cardillo";
        String contratto = "Indeterminato";
        String telefono = "0372894957";
        String mail = "gaetanoCardillo@questura.it";
        String password = "ADMIN@g3n4880[";

        assertTrue(mockModel.checkMailDipendente(mail));
        assertTrue(mockModel.checkNumeroTelefono(telefono));

        assertFalse(mockModel.insertNuovoDipendente(matricola, nome, cognome, contratto, telefono, mail, password));
        assertFalse(mockModel.insertNuovoDipendente("", "", "", "", "", "", ""));
        assertTrue(mockModel.insertNuovoDipendente("AD938312", "Giovanni", "Rossi", "Part-time", "0319437621", "giovanniRossi@questura.it", "xd@12345"));
    }

    @Test
    void testApriAnteprimaSlot_resultSetVuoto() throws SQLException {
        String dayOfWeek = null;
        LocalDate selectedDate = null;

        controller.apriAnteprimaSlot();

        assertFalse(controller.anteprimaSlot.isVisible());
        assertTrue(controller.anteprimaSlot.isMouseTransparent());
        assertEquals(0.5, controller.anteprimaSlot.getOpacity());
        assertFalse(controller.anteprimaSlot.isExpanded());

        assertFalse(mockModel.checkPresenzaData(selectedDate));
        assertFalse(mockModel.checkValiditaData(selectedDate, dayOfWeek));
        assertFalse(mockModel.insertDataMancante(selectedDate, dayOfWeek));
        assertFalse(mockModel.dipendentiData(dayOfWeek));
    }

    @Test
    void testConfermaSlotData_datiValidi() throws SQLException {

        LocalDate selectedDate = LocalDate.now();
        String dayOfWeek = selectedDate.getDayOfWeek().toString().toUpperCase();

        assertFalse(mockModel.checkPresenzaData(selectedDate));
        assertTrue(mockModel.checkValiditaData(selectedDate, dayOfWeek));
        assert(mockModel.dipendentiData(dayOfWeek));

        controller.confermaSlotData(null);
        controller.apriAnteprimaSlot();

        assertTrue(controller.anteprimaSlot.isVisible());
        assertFalse(controller.anteprimaSlot.isMouseTransparent());
        assertEquals(0.5, controller.anteprimaSlot.getOpacity());
        assertTrue(controller.anteprimaSlot.isExpanded());

        assertFalse(mockModel.checkPresenzaData(selectedDate));
        assertTrue(mockModel.checkValiditaData(selectedDate, dayOfWeek));
        assertTrue(mockModel.insertDataMancante(selectedDate, dayOfWeek));
        assertTrue(mockModel.dipendentiData(dayOfWeek));
    }

    @Test
    void testConfermaSlotData_datiNonValidi() throws SQLException {
        LocalDate selectedDate = null;
        String dayOfWeek = null;

        assertTrue(mockModel.checkPresenzaData(selectedDate));
        assertFalse(mockModel.checkValiditaData(selectedDate, dayOfWeek));
        ActionEvent e = new ActionEvent();

        controller.confermaSlotData(e);

        assertFalse(mockModel.checkPresenzaData(selectedDate));
        assertFalse(mockModel.checkValiditaData(selectedDate, dayOfWeek));
        assertFalse(mockModel.dipendentiData(dayOfWeek));
    }

    @Test
    public void testAccediDipendente() throws NoSuchAlgorithmException {

        String codiceFiscale = "testCF";
        String password = "testPassword";

        assertFalse(mockModel.checkLogin(codiceFiscale, password, "addetto"));
        assertFalse(mockModel.checkLogin(codiceFiscale, "", "addetto"));

        String codiceFiscaleN = "DVDFCC01D23M150F";
        String passwordN = "Ciao123Ciao!";

        assertFalse(mockModel.checkLogin(codiceFiscaleN, passwordN, "cittadino"));
        assertTrue(mockModel.checkLogin(codiceFiscaleN, passwordN, "addetto"));

        assertNull(mockModel.checkLogin("", "", ""));
        assertFalse(mockModel.checkLogin("", "", "cittadino"));
        assertFalse(mockModel.checkLogin("", "", "addetto"));
    }

}
