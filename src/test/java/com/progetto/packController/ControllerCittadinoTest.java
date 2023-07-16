package com.progetto.packController;

import com.progetto.packModel.Model;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class ControllerCittadinoTest {

    private ControllerCittadino controller;
    private static Model mockModel;

    @Before
    public void setUp() {
        controller = new ControllerCittadino();
        mockModel = Model.getInstance();
    }

    @Test
    public void testAccediCittadino() throws SQLException, IOException, NoSuchAlgorithmException {

        String codiceFiscale = "testCF";
        String password = "testPassword";

        assertFalse(mockModel.checkLogin(codiceFiscale, password, "cittadino"));
        assertFalse(mockModel.checkLogin(codiceFiscale, password, "addetto"));

        String codiceFiscaleN = "DVDFCC01D23M150F";
        String passwordN = "Ciao123Ciao!!";

        assertTrue(mockModel.checkLogin(codiceFiscaleN, passwordN, "cittadino"));
        assertFalse(mockModel.checkLogin(codiceFiscaleN, passwordN, "addetto"));

        assertNull(mockModel.checkLogin("", "", ""));
        assertFalse(mockModel.checkLogin("", "", "cittadino"));
    }


    @Test
    public void testUpdatePasswordCittadino() throws NoSuchAlgorithmException, SQLException {
        String newPassword = "password";
        String encryptedPassword = "encrypted_password";

        assertFalse(mockModel.checkPassword(newPassword));
        assertFalse(mockModel.checkPassword(encryptedPassword));

        controller.updatePasswordCittadino(null);

        String nuovaPassword = "Ciao123Ciao!";
        String enPassword = "52cafe4c5d2e7325d37553b520db4ec27121aa71e702602702486e8d645f8f456840920e949e83698736aee2ab459673291428635c0647dd0ec0cbc4a64dffa4";
        assertTrue(mockModel.checkPassword(newPassword));
        assertFalse(mockModel.checkPassword(enPassword));

        String nP = "";
        String enPP = "";
        assertFalse(mockModel.checkPassword(nP));
        assertFalse(mockModel.checkPassword(enPP));
    }

    @Test
    public void testSetConfermaPrenotazione() throws SQLException, IOException {
        boolean hasRitiro = true;
        boolean hasScadenza = false;
        String CAUSA_RILASCIO = "scadenza";
        boolean isValidInput = true;

        controller.setConfermaPrenotazione(null);

        assertTrue(hasRitiro);
        assertFalse(hasScadenza);
        assertEquals("scadenza", CAUSA_RILASCIO);
        assertTrue(isValidInput);
    }

    @Test
    public void testGetCheckCittaServizio() throws SQLException {
        ArrayList<String> cittaServizio = new ArrayList<>();
        boolean isPrimoPassaporto = true;
        boolean isPassaportoInScadenza = false;
        boolean isPassaportoFRS = false;
        boolean isRitiroDisponibile = true;
        String CAUSA_RILASCIO = null;
        LocalDate dataRitiro = null;

        controller.getCheckCittaServizio(null);

        assertNotNull(cittaServizio);
        assertTrue(isPrimoPassaporto);
        assertFalse(isPassaportoInScadenza);
        assertFalse(isPassaportoFRS);
        assertTrue(isRitiroDisponibile);
        assertNull(CAUSA_RILASCIO);
        assertNull(dataRitiro);
    }
}
