package com.progetto.packController;

import com.progetto.EncryptionPass;
import com.progetto.packModel.Model;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ControllerCittadino implements Initializable {
    private static final String BACK_COLOR = "-fx-background-color: #c2c0c0";
    private static final int START_TIME_CODA = 10;
    private boolean isEditPWD = false;
    private boolean isEditMAIL = false;
    private boolean isPWDHidden = false;
    @FXML
    private TextField hiddenTF = new TextField();
    private Model model = Model.getInstance();
    @FXML
    private TextField codiceFiscale;
    @FXML
    private Label message;
    @FXML
    private PasswordField passwordCittadino;
    @FXML
    private Label nomeUser = new Label();
    @FXML
    private ScrollPane ScrollPaneAttive;
    @FXML
    private ScrollPane ScrollPanePassate;
    @FXML
    private TitledPane PrenotazioniAttive = new TitledPane();
    @FXML
    private AnchorPane AnchorPane2 = new AnchorPane();
    @FXML
    private Label LabelCf = new Label();
    @FXML
    private Label LabelCognome = new Label();
    @FXML
    private Label LabelNome = new Label();
    @FXML
    private AnchorPane AnchorPane3 = new AnchorPane();
    @FXML
    private Label LabelCf2 = new Label();
    @FXML
    private Label LabelCognome2 = new Label();
    @FXML
    private Label LabelNome2 = new Label();
    @FXML
    private Button confermaModificaPassword = new Button();
    @FXML
    private Button confermaModificaMail = new Button();
    @FXML
    private Label labelNuovaMail = new Label();
    @FXML
    private Label labelNuovaPassword = new Label();
    @FXML
    private TextField nuovaMail = new TextField();
    @FXML
    private PasswordField nuovaPassword = new PasswordField();
    @FXML
    private Button buttonModPwd = new Button();
    @FXML
    private Button buttonModMail = new Button();
    @FXML
    private Button buttonRilascio = new Button();
    @FXML
    private Button buttonRitiro = new Button();
    @FXML
    private Button indietroButtonPrenotazione = new Button();
    @FXML
    private RadioButton radioButtonPasswordModifica = new RadioButton();
    @FXML
    private ChoiceBox<String> comboBoxServizio = new ChoiceBox<>();
    @FXML
    private ChoiceBox<String> comboBoxSede = new ChoiceBox<>();
    @FXML
    private Button confermaPrenotazioneServizioSede = new Button();
    @FXML
    private Button scegliServizio = new Button();
    @FXML
    private Button buttonIndietroPrenotazioneIntermedia = new Button();
    private static int count = 0;
    ZonedDateTime dateFocus;
    ZonedDateTime today;
    @FXML
    private Text year = new Text();
    @FXML
    private Text month = new Text();
    @FXML
    private FlowPane calendar = new FlowPane();
    @FXML
    private Button backOne = new Button();
    private static final int START_TIME = 8*60;
    private int remainingTime;
    @FXML
    private Label labelTempoRimasto = new Label();
    private Timeline timeline;
    private String causaPassaporto;
    private boolean isTimerOver = false;
    @FXML
    private Label labelGrigia = new Label();
    private boolean isPrenotazioneClicked = false;
    private boolean stageSetBack = false;
    private static String servPP = new String();
    private static String numSedePP = new String();
    @FXML
    private Button buttonAggiornaCoda = new Button();
    @FXML
    private Label labelCoda = new Label();
    boolean hasCodaStarted = false;
    private Duration timer = Duration.minutes(5);
    @FXML
    private Button buttonConfermaPrenotazione = new Button();
    private static LocalDate giornoPrenotazione;
    private static String slotPrenotazione = new String();
    private static Timestamp TIME_STAMP_ENTRATA;
    private static String SERVIZIO_ENTRATA;
    private static Stage primaryStage;
    private boolean HAS_ENTERED;
    private boolean calendarLogicExecuted = false;
    private boolean HAS_CODA_FINISHED = false;
    private boolean isRed;
    @FXML
    private Button indietrohidden = new Button();
    private static String CAUSA_RILASCIO = new String();
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Gestisce l'apertura della vista per l'accesso del cittadino a seconda del risultato ottenuto dal model.
     * @param eventAccediCittadino
     * @throws SQLException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    @FXML
    void accediCittadino(ActionEvent eventAccediCittadino) throws SQLException, IOException, NoSuchAlgorithmException {
        boolean loginChecker = this.model.checkLogin(codiceFiscale.getText(), passwordCittadino.getText(), "cittadino");

        if(loginChecker){
            openPortaleCittadino(eventAccediCittadino);
        } else {
            message.setText("USERNAME O PASSWORD NON CORRETTI.");
            codiceFiscale.setText("");
            codiceFiscale.setPromptText("Codice Fiscale");
            passwordCittadino.setText("");
            passwordCittadino.setPromptText("Password");
        }
    }

    /**
     * Gestisce l'apertura della vista per l'accesso al portale del cittadino.
     * @param eventAccediCittadino
     * @throws IOException
     */
    private void openPortaleCittadino(ActionEvent eventAccediCittadino) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/ViewCittadino/portale-cittadino-view.fxml"));
        Stage stage = (Stage) ((Node) eventAccediCittadino.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Gestisce l'apertura della vista per tornare al portale del cittadino.
     * @param eventAccediCittadino
     * @throws IOException
     */
    @FXML
    private void tornaPortaleCittadino(ActionEvent eventAccediCittadino) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/ViewCittadino/portale-cittadino-view.fxml"));
        Stage stage = (Stage) ((Node) eventAccediCittadino.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Gestisce l'apertura della vista per il logout.
     * @param eventIndietro
     * @throws IOException
     */
    @FXML
    void indietro(ActionEvent eventIndietro) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/first-view.fxml"));
        Stage stage = (Stage) ((Node) eventIndietro.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Gestisce l'apertura del portale cittadino con un actionevent specifico per ri-ottenere il primary stage.
     * @param eventIndietroCitt
     * @throws IOException
     */
    @FXML
    void indietroPortaleCittadino(ActionEvent eventIndietroCitt)throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/ViewCittadino/portale-cittadino-view.fxml"));
        Stage stage = (Stage) ((Node) eventIndietroCitt.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    /**
     * Gestisce l'apertura della vista per la modifica dei dati del cittadino.
     * @param eventDati
     * @throws IOException
     */
    @FXML
    void modificaDati(ActionEvent eventDati) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/ViewCittadino/modifica-dati-cittadino-view.fxml"));
        Stage stage = (Stage) ((Node) eventDati.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Gestisce l'apertura della selezione del servizio e della sede, a seconda della risposta del model. Gestisce output
     * negativi tramite popup.
     * @param nP
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void openPortalePrenotazioni(ActionEvent nP) throws IOException, SQLException {

        boolean isPrenotazionePresente = this.model.getPrenotazioneUnica();

        if(!isPrenotazionePresente) {
            Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/ViewCittadino/prenotazione-intermedia-view.fxml"));
            Stage stage = (Stage) ((Node) nP.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } else {
            startAlert("prenotazione presente");
        }
    }

    /**
     * Gestisce l'apertura della vista del Calendario per le prenotazioni.
     * @param e
     * @throws IOException
     */
    private void openCalendar(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/Calendar.fxml"));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        if (stage == null) {
            stage = new Stage();
        }
        ControllerCittadino.setPrimaryStage(stage);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Alert per errori sulla prenotazione
     */
    private void startAlertAnnullamento() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setHeaderText("Operazione non completata!");
        alert.setContentText("La tua prenotazione è già stata annullata oppure risulta esserci qualche problema.\nProva ad uscire e a rientrare dal portale.");
        alert.showAndWait();
    }

    /**
     * Alert per segnalae il successo dell'annullamente della prenotazione.
     */
    private void startSuccessAnnullamento() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confermato!");
        alert.setHeaderText("Operazione completata");
        alert.setContentText("Operazione completata con successo! La tua prenotazione è stata annullata");
        alert.showAndWait();
    }

    /**
     * Alert per segnalare il download del pdf generato.
     */
    private void startPDFSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confermato!");
        alert.setHeaderText("Operazione completata");
        alert.setContentText("Operazione completata con successo! Troverà il pdf scaricato nella cartella Download.");
        alert.showAndWait();
    }

    /**
     * Alert per segnalare la modifica dei dati.
     * @param text
     */
    private void startSuccess(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confermato!");
        alert.setHeaderText("Operazione completata");
        alert.setContentText("Operazione completata con successo! Al prossimo login i nuovi dati saranno effettivi");
        alert.showAndWait();

        if(text.equals("Mail")) {
            hideMail();
        } else {
            hidePwd();
        }
    }

    /**
     * Metodo che gestisce la presenza dei pop-up tramite una stringa di input. A seconda del valore della stringa,
     * verranno mostrati all'utente diversi pop up di errore.
     * @param string
     */
    private void startAlert(String string) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setHeaderText("Operazione non concessa");
        if(string.equals("slot orario")){
            alert.setContentText("Il campo " + string + " non è disponibile.");
        } else if(string.equals("primo passaporto")){
            alert.setContentText("Attenzione, non è il suo primo passaporto! Scelga un servizio corretto.");
        } else if(string.equals("in scadenza")){
            alert.setContentText("Attenzione, il suo passaporto non è in scadenza! Scelga un servizio corretto.");
        } else if(string.equals("attenzione")){
            alert.setContentText("Attenzione, questo servizio non è conforme con i suoi dati.");
        }else if(string.equals("ritiro")){
            alert.setContentText("Attenzione, questa azione non è permessa.\nNon possiede nessun passaporto da ritirare.");
        }else if(string.equals("ritiro mancante")){
            alert.setContentText("Attenzione, la data dell'appuntamento di ritiro da Lei scelta non è permessa.\nLa data deve essere scelta ad un mese dalla sua domanda di rilascio.");
        } else if(string.equals("data scadenza")){
            alert.setContentText("Attenzione, la data dell'appuntamento da Lei scelta non è permessa.\nLa data deve essere scelta sei mesi prima dalla data di scadenza del suo passaporto.");
        } else if(string.equals("prenotazione presente")){
            alert.setContentText("Attenzione, è già presente una prenotazione oppure sta tentando di effettuare una prenotazione con un dispositivo diverso.\nPuoi visualizzarla nella scheda Prenotazioni Attive.\nPer proseguire deve attendere l'esecuzione dell'appuntamento precedente oppure annullare la prenotazione presente.");
        }else {
            alert.setContentText("Il campo " + string + " non è corretto.");
        }
        alert.showAndWait();
    }

    /**
     * Metodo che gestisce graficamente la presenza delle prenotazioni attive con l'aggiunta di pulsanti per scaricare pdf e annullare la prenotazione
     * Gli elementi grafici dipendono dalla risposta del model.
     * @throws SQLException
     */
    @FXML
    void prenotazioniAttive() throws SQLException {
        VBox vboxpattive = new VBox();
        vboxpattive.setSpacing(10);
        vboxpattive.setPadding(new Insets(10));

        ResultSet rs = this.model.getPrenotazioniAttive();
        while (rs.next()) {
            String id = rs.getString("ID");
            Label idLabel = new Label("ID Prenotazione: " + id);

            String citta = rs.getString("città");
            Label cittaLabel = new Label("Luogo: " + citta);

            Time ora = rs.getTime("ora");
            Date giorno = rs.getDate("giorno");
            Label giornoOraLabel = new Label("Data: " + giorno + " alle ore " +ora );

            String servizio = rs.getString("servizio");
            Label servizioLabel = new Label("Servizio: " + servizio);

            String causaR = rs.getString("causaRilascio");
            Label causaRilascioLabel = new Label("Causale: " + causaR);

            String codSede = rs.getString("codSede");

            Button dettagliButton = new Button("PDF Dettagli");
            Button annullaButton = new Button("Annulla");

            dettagliButton.setOnAction(event -> {
                List<String> lista = new ArrayList<>();
                lista.add(id);
                lista.add(citta);
                lista.add(String.valueOf(ora));
                lista.add(String.valueOf(giorno));
                lista.add(servizio);
                lista.add(causaR);
                lista.add(this.model.getNome());
                lista.add(this.model.getCognome());
                lista.add(this.model.getId());
                this.model.makePDF(lista, servizio, causaR);
                startPDFSuccess();
            });

            annullaButton.setOnAction(event -> {
                try {
                    boolean annullaResult = this.model.annullaPrenotazioneAttive(id,giorno,ora,codSede);
                    if (annullaResult) {
                        startSuccessAnnullamento();
                        PrenotazioniAttive.setExpanded(false);
                        this.model.getPrenotazioniAttive();
                    } else {
                        startAlertAnnullamento();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            Line line = new Line(0, 0, 790, 0); // Horizontal line

            HBox entryBox = new HBox(idLabel, cittaLabel, giornoOraLabel, servizioLabel, causaRilascioLabel,
                    dettagliButton, annullaButton);

            entryBox.setSpacing(15);
            entryBox.setAlignment(Pos.CENTER);
            entryBox.setSnapToPixel(true);
            vboxpattive.getChildren().addAll(entryBox, line);
        }
        ScrollPaneAttive.setContent(vboxpattive);
    }

    /**
     * Metodo che gestisce graficamente la presenza delle prenotazioni passate con l'aggiunta di pulsante per scaricare pdf.
     * Gli elementi grafici dipendono dalla risposta del model.
     * @throws SQLException
     */
    @FXML
    void prenotazioniPassate() throws SQLException {
        VBox vboxpassate = new VBox();
        vboxpassate.setSpacing(10);
        vboxpassate.setPadding(new Insets(10));

        ResultSet rs = this.model.getPrenotazioniPassate();
        while (rs.next()) {
            Label idLabel = new Label("ID Prenotazione: " + rs.getString("ID"));
            Label causaRilascioLabel = new Label("Causale: Ritiro passaporto");
            Label servizioLabel = new Label("Servizio: " +rs.getString("servizio"));

            String id = rs.getString("ID");
            String citta = rs.getString("città");
            Time ora = rs.getTime("ora");
            Date giorno = rs.getDate("giorno");
            String servizio = rs.getString("servizio");

            Button dettagliButton = new Button("PDF Dettagli");

            dettagliButton.setOnAction(event -> {
                List<String> lista = new ArrayList<>();
                lista.add(id); // 0
                lista.add(citta); // 1
                lista.add(String.valueOf(ora)); // 2
                lista.add(String.valueOf(giorno)); // 3
                lista.add(servizio); // 4
                lista.add("");
                lista.add(this.model.getNome()); // 6
                lista.add(this.model.getCognome()); // 7
                lista.add(this.model.getId()); // 8
                this.model.makePDF(lista, servizio, "Ritiro passaporto");
                startPDFSuccess();
            });

            Line line = new Line(0, 0, 790, 0); // Horizontal line
            HBox entryBox = new HBox(idLabel, servizioLabel, causaRilascioLabel, dettagliButton);
            entryBox.setSpacing(15);
            entryBox.setAlignment(Pos.CENTER);
            entryBox.setSnapToPixel(true);
            vboxpassate.getChildren().addAll(entryBox, line);
        }
        ScrollPanePassate.setContent(vboxpassate);
    }

    /**
     * Metodo che gestisce graficamente se l'utente ha richiesto di modificare la password
     */
    private void checkEditPwdButton() {
        isEditPWD = !isEditPWD;
        if(isEditPWD){editPassword();}else{hidePwd();}
    }

    /**
     * Metodo che nasconde graficamente il campo di modifica della password
     */
    private void hidePwd() {
        labelNuovaPassword.setMouseTransparent(true);
        nuovaPassword.setMouseTransparent(true);
        radioButtonPasswordModifica.setMouseTransparent(true);
        confermaModificaPassword.setMouseTransparent(true);
        nuovaPassword.setText("");
        labelNuovaPassword.setVisible(false);
        radioButtonPasswordModifica.setVisible(false);
        confermaModificaPassword.setVisible(false);
        nuovaPassword.setVisible(false);
        hiddenTF.setVisible(false);
        hiddenTF.setMouseTransparent(false);
        hiddenTF.setText("");
    }

    /**
     * Metodo che gestisce graficamente se l'utente ha richiesto di modificare la mail
     */
    private void checkEditMailButton() {
        isEditMAIL = !isEditMAIL;
        if(isEditMAIL){editMail();}else{hideMail();}
    }

    /**
     * Metodo che nasconde graficamente il campo di modifica della mail
     */
    private void hideMail() {
        labelNuovaMail.setVisible(false);
        nuovaMail.setVisible(false);
        confermaModificaMail.setVisible(false);
        labelNuovaMail.setMouseTransparent(true);
        nuovaMail.setMouseTransparent(true);
        confermaModificaMail.setMouseTransparent(true);
        nuovaMail.setText("");
    }

    /**
     * Metodo che mostra graficamente il campo di modifica della mail
     */
    private void editMail() {
        labelNuovaMail.setMouseTransparent(false);
        nuovaMail.setMouseTransparent(false);
        labelNuovaMail.setVisible(true);
        nuovaMail.setVisible(true);
        confermaModificaMail.setVisible(true);
        confermaModificaMail.setMouseTransparent(false);
    }

    /**
     * Metodo che mostra graficamente il campo di modifica della password
     */
    private void editPassword() {
        labelNuovaPassword.setMouseTransparent(false);
        nuovaPassword.setMouseTransparent(false);
        radioButtonPasswordModifica.setMouseTransparent(false);
        labelNuovaPassword.setVisible(true);
        nuovaPassword.setVisible(true);
        radioButtonPasswordModifica.setVisible(true);
        confermaModificaPassword.setVisible(true);
        confermaModificaPassword.setMouseTransparent(false);
    }

    /**
     * Metodo che mostra graficamente il risultato della modifica della password, a seconda della risposta ottenuta dal model.
     * @param event
     * @throws NoSuchAlgorithmException
     * @throws SQLException
     */
    @FXML
    void updatePasswordCittadino(ActionEvent event) throws NoSuchAlgorithmException, SQLException {
        EncryptionPass encryptedPassword = new EncryptionPass();
        String pwd = encryptedPassword.setEncrypt(nuovaPassword.getText());
        if(this.model.checkPassword(nuovaPassword.getText())){
            this.model.updateCittadinoPassword(this.model.getId(), pwd);
            startSuccess("Password");
        } else {
            startAlert("Password");
        }
    }

    /**
     * Metodo che mostra graficamente il risultato della modifica della mail, a seconda della risposta ottenuta dal model.
     * @param event
     * @throws SQLException
     */
    @FXML
    void UpdateMailCittadino(ActionEvent event) throws SQLException {

        if(this.model.checkMail(nuovaMail.getText())) {
            this.model.updateCittadinoMail(this.model.getId(), nuovaMail.getText());
            startSuccess("Mail");
        } else {
            startAlert("Mail");
        }
    }

    /**
     * Metodo che gestisce se l'utente ha richiesto di vedere la password che sta digitando
     */
    private void checkHiddenPwd() {
        isPWDHidden = !isPWDHidden;
        if(isPWDHidden){
            showHiddenPWD();
        } else {
            hideHiddenPWD();
        }
    }

    /**
     * Metodo che gestisce se l'utente ha richiesto di nascondere la password che sta digitando
     */
    private void hideHiddenPWD() {
        hiddenTF.setVisible(false);
        hiddenTF.toBack();
        hiddenTF.setMouseTransparent(true);
        nuovaPassword.toFront();
        nuovaPassword.setText(hiddenTF.getText());
        nuovaPassword.setVisible(true);
    }

    /**
     * Metodo che mostra graficamente all'utente la password che sta digitando
     */
    private void showHiddenPWD() {
        hiddenTF.toFront();
        hiddenTF.setVisible(true);
        hiddenTF.setMouseTransparent(false);
        nuovaPassword.toBack();
        nuovaPassword.setVisible(false);
        hiddenTF.setText(nuovaPassword.getText());
    }

    /**
     * Metodo per tornare un mese indietro nel calendario
     * @param event
     * @throws SQLException
     */
    @FXML
    void backOneMonth(ActionEvent event) throws SQLException {
        dateFocus = dateFocus.minusMonths(1); // Muovi la focus date indietro di un mese
        calendar.getChildren().clear(); // resetta il calendario
        drawCalendar(); // ridisegna il calendario
        updateButtonStatus(backOne); // aggiorna il pulsante
    }

    /**
     * Metodo per andare un mese avanti nel calendario
     * @param event
     * @throws SQLException
     */
    @FXML
    void forwardOneMonth(ActionEvent event) throws SQLException {
        dateFocus = dateFocus.plusMonths(1); // Muovi la focus date avanti di un mese
        calendar.getChildren().clear(); // resetta il calendario
        drawCalendar(); // ridisegna il calendario
        updateButtonStatus(backOne); // aggiorna il pulsante
    }

    /**
     * Metodo che disabilita il pulsante per tornare indietro di un mese dal mese corrente.
     * @param button
     */
    private void updateButtonStatus(Button button) {
        button.setDisable(!canGoBack());
    }

    /**
     * Metodo che comunica se l'utente può tornare indietro.
     * @return
     */
    private boolean canGoBack() {
        ZonedDateTime initialDate = ZonedDateTime.now(); // data iniziale
        return !dateFocus.isBefore(initialDate);
    }

    /**
     * Metodo che gestisce dinamicamente l'apertura del calendario a seconda della risposta ottenuta dal model.
     * @param e
     * @throws SQLException
     */
    @FXML
    private void openCalendarApp(ActionEvent e) throws SQLException {
        HAS_ENTERED = true;

        int numeroCoda;

        LocalDateTime timestamp = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String timeStampUtente = timestamp.format(formatter);
        Timestamp timeStamp = Timestamp.valueOf(timeStampUtente);

        if(!hasCodaStarted) {
            if(comboBoxServizio.getValue().equals("Ritiro")){
                servPP = "Ritiro";
            } else {
                servPP = "Rilascio";
            }

            TIME_STAMP_ENTRATA = timeStamp;
            SERVIZIO_ENTRATA = servPP;

            try {
                numSedePP = this.model.getNSede(comboBoxSede.getValue());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            this.model.setCoda(servPP, numSedePP, comboBoxServizio.getValue(), timeStamp);
        }


        try {
            numeroCoda = this.model.getCoda(servPP, numSedePP, TIME_STAMP_ENTRATA);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        if(numeroCoda>0){
            startTimerCoda(numeroCoda);
            buttonIndietroPrenotazioneIntermedia.setOpacity(0.5);
            buttonIndietroPrenotazioneIntermedia.setMouseTransparent(true);
        } else {
            try {
                openCalendar(e);

                Platform.runLater(()->{
                    try{
                        drawCalendar();
                        startTimer();
                    } catch (SQLException ex){
                        throw new RuntimeException(ex);
                    }
                });

                executeCalendarLogic();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Metodo che disegna dinamicamente il calendario. Considera l'anno bisestile, considera la distribuzione dei giorni
     * in uno schema da che va da domenica a sabato. Controlla con il model se nei giorni sono presenti slot, controlla
     * l'orario degli slot, il numero degli appuntamenti e lo mostra graficamente con un linguaggio color coded.
     * Grigio = giorno non permesso e/o precedente al giorno attuale.
     * Rosso = slot con numero di appuntamenti terminati
     * Verde = slot disponibile
     * Giallo = giorno con slot non ancora assegnati.
     * @throws SQLException
     */
    private void drawCalendar() throws SQLException {
        year.setText(String.valueOf(dateFocus.getYear())); // anno
        month.setText(dateFocus.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN).toUpperCase());

        // Dimensions and styling for the calendar elements
        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        int monthMaxDate = dateFocus.getMonth().maxLength(); // ottengo il numero di giorni massimi in un mese
        // Controllo se è bisestile
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone())
                .getDayOfWeek().getValue(); // Offset dei giorni del mese dal primo giorno del mese

        for (int i = 0; i < 6; i++) { // Mi muovo lungo le righe del calendario
            for (int j = 0; j < 7; j++) { // mi muovo lungo le colonne
                StackPane stackPane = new StackPane(); // Creo uno stack pane PER OGNI cella del calendario
                Rectangle rectangle = new Rectangle(); // Creo un rettangolo PER OGNI cella e stackpane
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH; // Devo calcolare la larghezza del rettangolo a seconda dei giorni presenti
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV; // Devo calcolare la lunghezza del rettangolo a seconda dei giorni presenti
                rectangle.setHeight(rectangleHeight);

                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 1) + (7 * i); // Calcola la data per ogni cella spostandola
                Label data = new Label();
                ArrayList<String> giorniRossi = new ArrayList<>();
                if (calculatedDate > dateOffset) { // Controlla se la data è coerente con il mese corrente
                    int currentDate = calculatedDate - dateOffset; // Trova il giorno attuale
                    if (currentDate <= monthMaxDate) { //  Controlla se il giorno attuale rispetta il range di giorni del mese
                        data.setText(String.valueOf(currentDate));
                        data.setStyle("-fx-background-color: #f0fafc; -fx-border-color: black");
                        data.setPrefWidth(rectangleWidth);
                        data.setAlignment(Pos.CENTER);
                        data.setTranslateY(-(rectangleHeight) / 2 - 5);
                        stackPane.getChildren().add(data);

                        int cYear = dateFocus.getYear();
                        int cMonth = dateFocus.getMonthValue();
                        LocalDate dateC = LocalDate.of(cYear, cMonth, currentDate);

                        ResultSet rs = this.model.getSlots(dateC, numSedePP, servPP);
                        int hasAllZeros = this.model.getZeroSlot(dateC, numSedePP, servPP);
                        boolean hasSlots = rs.next(); // Flag per controllare se ci sono slots
                        boolean isEmpty = false;

                        // if numero posti <= 0 --> rosso
                        // if data non c'è giallo
                        if (!hasSlots) {
                            rectangle.setFill(Paint.valueOf("#f7ce5c")); // giallo
                        } else {
                            rectangle.setFill(Paint.valueOf("#6ebf5a")); // verde
                            rectangle.toBack();
                        }

                        VBox buttonContainer = new VBox(); // VBox per mettere gli slots verticalmente
                        int counter = 0;
                        if (hasSlots) {
                            do {
                                LocalTime slotHour = LocalTime.parse(rs.getString(1));
                                Button button = new Button(trasformaOrario(String.valueOf(slotHour)));
                                Label labelRed = new Label(trasformaOrario(rs.getString(1)));

                                button.setText(trasformaOrario(rs.getString(1)));
                                button.setMaxWidth(rectangleWidth - strokeWidth); // larghezza massima per ogni slots


                                int slotsNumber = rs.getInt(2);

                                if(dateC.isBefore(LocalDate.now())){
                                    rectangle.setFill(Paint.valueOf("#808080"));
                                    rectangle.toBack();
                                }

                                if (slotsNumber <= 0) {
                                    counter++;
                                    button.setStyle("-fx-background-color: #f76363; -fx-border-color: black; -fx-border-width: 1px;");
                                    button.setMouseTransparent(true);
                                    button.toFront();
                                    giorniRossi.add(button.getText());
                                } else if(dateC.isBefore(LocalDate.now())){
                                    button.setStyle("-fx-background-color: #808080; -fx-border-color: black; -fx-border-width: 1px;");
                                    button.setMouseTransparent(true);
                                    rectangle.setFill(Paint.valueOf("#808080"));
                                    button.setTextFill(Paint.valueOf("#ffffff"));
                                    button.toFront();
                                } else if(dateC.equals(LocalDate.now()) && slotHour.isBefore(LocalTime.now())){
                                    button.setStyle("-fx-background-color: #808080; -fx-border-color: black; -fx-border-width: 1px;");
                                    button.setMouseTransparent(true);
                                    rectangle.setFill(Paint.valueOf("#808080"));
                                    button.toFront();
                                } else {
                                    button.setStyle("-fx-background-color: #6ebf5a; -fx-border-color: black; -fx-border-width: 1px;");
                                    button.setTextFill(Paint.valueOf("#ffffff"));
                                }

                                button.setOnMouseClicked(ex->button.setStyle("-fx-background-color: #6ebf9f; -fx-border-color: black; -fx-border-width: 1px;"));

                                if(counter == hasAllZeros){
                                    rectangle.setFill(Paint.valueOf("#f76363")); // rosso
                                    rectangle.setStroke(Paint.valueOf("#ff0000"));
                                    buttonContainer.setStyle("-fx-background-color: #f76363;");
                                    labelRed.setStyle("-fx-background-color: #f76363;");
                                    rectangle.toFront();
                                }

                                button.setOnAction(e -> {
                                        giornoPrenotazione = dateC;
                                        System.out.println(giornoPrenotazione);
                                        slotPrenotazione = button.getText();
                                });

                                buttonContainer.setSpacing(5);
                                buttonContainer.setStyle("-fx-background-color: transparent");
                                buttonContainer.getChildren().add(button);
                            } while (rs.next());
                        }

                        if (hasSlots) {
                            ScrollPane scrollPane = new ScrollPane(buttonContainer); // Wrapping del VBOX nel Scrollpane
                            scrollPane.setPrefWidth(rectangleWidth);
                            scrollPane.setPrefHeight(rectangleHeight);
                            scrollPane.setFitToWidth(true);
                            scrollPane.setFitToHeight(true);
                            stackPane.getChildren().addAll(scrollPane);
                        }

                        if (dateC.getDayOfWeek() == DayOfWeek.SATURDAY || dateC.getDayOfWeek() == DayOfWeek.SUNDAY || dateC.isBefore(LocalDate.now())) {
                            rectangle.setFill(Paint.valueOf("#808080")); // se è sabato o domenica o il giorno è prima del giorno attuale --> grigio
                        }
                    }
                    if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth()
                            && today.getDayOfMonth() == currentDate) {
                        rectangle.setFill(Paint.valueOf("#add8e6"));
                        data.setStyle("-fx-background-color: #add8e6; -fx-border-color: black");
                    }
                } else {
                    data.setStyle("-fx-background-color: #f0fafc; -fx-border-color: black");
                    data.setPrefWidth(rectangleWidth);
                    data.setAlignment(Pos.CENTER);
                    data.setTranslateY(-(rectangleHeight) / 2 - 5);
                    stackPane.getChildren().add(data);
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }

    /**
     * Fa partire il timer del calendario.
     */
    private void startTimer() {
        remainingTime = START_TIME;

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    remainingTime--;
                    String j = updateTimerLabel();
                    labelTempoRimasto.setText(j);

                    if (remainingTime <= 0) {
                        try {
                            stopTimer();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Aggiorna graficamente il timer del calendario
     * @return
     */
    private String updateTimerLabel() {
        int minutes = remainingTime / 60;
        int seconds = remainingTime % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Chiude il calendario e mostra graficamente tale azione all'utente allo scadere del tempo prestabilito.
     * @throws IOException
     * @throws SQLException
     */
    private void stopTimer() throws IOException, SQLException {
        timeline.stop();
        isTimerOver = true;
        stageSetBack = true;

        indietroButtonPrenotazione.fire();
    }

    /**
     * ignorare.
     */
    private String trasformaOrario(String string) {
        String rs = new String();

        if(string.equals("09:00:00")){
            rs = "9-10";
        }
        if(string.equals("10:00:00")){
            rs = "10-11";
        }
        if(string.equals("11:00:00")){
            rs =  "11-12";
        }
        if(string.equals("14:00:00")){
            rs = "14-15";
        }
        if(string.equals("15:00:00")){
            rs = "15-16";
        }
        if(string.equals("16:00:00")){
            rs = "16-17";
        }
        return rs;
    }

    /**
     * Metodo per far partire il timer della cosa.
     * @param numeroCoda
     */
    private void startTimerCoda(int numeroCoda) {
        remainingTime = START_TIME_CODA;
        startControlloCoda(numeroCoda);
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            remainingTime--;
            if (remainingTime <= 0) {
                try {
                    stopTimerCoda();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Metodo che termina il timer della coda.
     * @throws IOException
     */
    private void stopTimerCoda() throws IOException {
        if(hasCodaStarted){
            buttonAggiornaCoda.fire();
        } else {
            timeline.stop();
            HAS_CODA_FINISHED = true;
            calendarLogicExecuted = false;
        }
    }

    /**
     * Gestisce graficamente la confermare della prenotazione, controllando tramite il model a quali servizi l'utente può
     * avere accesso.
     * @param event
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    void setConfermaPrenotazione(ActionEvent event) throws SQLException, IOException {
            boolean hasRitiro = this.model.checkRitiro(giornoPrenotazione);
            boolean hasScadenza = this.model.checkScadenzaPP();
            System.out.println("giorno prenotazione " + giornoPrenotazione);
            System.out.println("RITIRO: " + (CAUSA_RILASCIO.equals("")) + " " + isValidInput() + " " + hasRitiro);
            System.out.println("SCADENZA: " + CAUSA_RILASCIO + " " + isValidInput() + " " + hasScadenza);
            System.out.println("Causa rilascio: " + CAUSA_RILASCIO);

            if (isValidInput()) {
                if (CAUSA_RILASCIO.equals("") && hasRitiro) {
                    this.model.setPrenotazione(numSedePP, giornoPrenotazione, slotPrenotazione, TIME_STAMP_ENTRATA, SERVIZIO_ENTRATA);
                    startAzioneSuccess("slot");
                    openPortaleCittadino(event);
                } else if (CAUSA_RILASCIO.equals("scadenza") && hasScadenza) {
                    this.model.setPrenotazione(numSedePP, giornoPrenotazione, slotPrenotazione, TIME_STAMP_ENTRATA, SERVIZIO_ENTRATA);
                    startAzioneSuccess("slot");
                    openPortaleCittadino(event);
                } else if (CAUSA_RILASCIO.equals("scadenza")) {
                    startAlert("data scadenza");
                } else if(CAUSA_RILASCIO.equals("")){
                    startAlert("ritiro mancante");
                } else {
                    this.model.setPrenotazione(numSedePP, giornoPrenotazione, slotPrenotazione, TIME_STAMP_ENTRATA, SERVIZIO_ENTRATA);
                    startAzioneSuccess("slot");
                    openPortaleCittadino(event);
                }
            } else {
                startAlert("slot orario");
            }
    }

    /**
     * Metodo di supporto per controllare che l'input scelto dall'utente sia coerente.
     * @return
     */
    private boolean isValidInput() {
        return slotPrenotazione != null && !slotPrenotazione.isEmpty() && giornoPrenotazione != null;
    }

    /**
     * Alert per indicare la conferma della prenotazione.
     * @param value
     */
    private void startAzioneSuccess(String value) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confermato!");
        alert.setHeaderText("Operazione completata");


        if(value.equals("slot")){
            alert.setContentText("Operazione completata con successo! La sua prenotazione per il giorno "+giornoPrenotazione +" tra le ore "+slotPrenotazione +" è stata inserita.");
        }
        alert.showAndWait();
    }

    /**
     * Metodo initialize che deve essere presente per ogni classe che implementa Initializable.
     * Inizializza diversi elementi grafici e gestisce l'interazione dell'utente con alcuni di questi elementi.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nomeUser.setText(this.model.getNome());

        LabelCf.setText(this.model.getId());
        LabelCf2.setText(this.model.getId());

        LabelNome.setText(this.model.getNome());
        LabelNome2.setText(this.model.getNome());

        LabelCognome.setText(this.model.getCognome());
        LabelCognome2.setText(this.model.getCognome());

        AnchorPane2.setStyle(BACK_COLOR);
        AnchorPane3.setStyle(BACK_COLOR);

        radioButtonPasswordModifica.setOnAction(e->checkHiddenPwd());
        buttonModMail.setOnAction(e->checkEditMailButton());
        buttonModPwd.setOnAction(e->checkEditPwdButton());

        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        updateButtonStatus(backOne);

        comboBoxSede.setMouseTransparent(true);
        comboBoxSede.setOpacity(0.5);

        ObservableList<String> serviziPrenotazioni = FXCollections.observableArrayList("Ritiro", "Rilascio per primo passaporto", "Rilascio per scadenza", "Rilascio per furto", "Rilascio per smarrimento", "Rilascio per deterioramento");
        comboBoxServizio.setItems(serviziPrenotazioni);

        buttonAggiornaCoda.setOnAction(e->{
            try {
                openCalendarApp(e);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        confermaPrenotazioneServizioSede.setOpacity(0.5);
        confermaPrenotazioneServizioSede.setMouseTransparent(true);

        Platform.runLater(()->{
            try{
                drawCalendar();
                startTimer();
            } catch (SQLException e){
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Metodo che gestisce graficamente la coda.
     * @param numeroCoda
     */
    private void startControlloCoda(int numeroCoda) {
        hasCodaStarted = true;
        confermaPrenotazioneServizioSede.setVisible(false);
        confermaPrenotazioneServizioSede.setMouseTransparent(true);
        comboBoxServizio.setMouseTransparent(true);
        comboBoxServizio.setOpacity(0.5);
        comboBoxSede.setMouseTransparent(true);
        comboBoxSede.setOpacity(0.5);
        labelCoda.setText("Hai "+numeroCoda+" persone in coda.");
        buttonAggiornaCoda.setVisible(true);
    }

    /**
     * Alert per segnalare l'assenza di un servizio.
     */
    private void startAlertServizio() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setHeaderText("Operazione non riuscita");
        alert.setContentText("Non c'è nessuna sede disponibile per questo servizio.");
        alert.showAndWait();
    }

    /**
     * Metodo che gestisce graficamente la presenza del calendario
     */
    private void executeCalendarLogic() {
        if (calendarLogicExecuted || isTimerOver || indietroButtonPrenotazione.isPressed()) {
            return;
        }

        try {
            drawCalendar();
            startTimer();
            calendarLogicExecuted = true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Metodo che gestisce graficamente la presenza delle città a seconda del servizio che l'utente può richiedere.
     * @param e
     * @throws SQLException
     */
    @FXML
    void getCheckCittaServizio(ActionEvent e) throws SQLException {
        ArrayList<String> cittaServizio = new ArrayList<>();
        boolean isPrimoPassaporto = false;
        boolean isPassaportoInScadenza = false;
        boolean isPassaportoFRS = false;
        boolean isRitiroDisponibile = false;

        CAUSA_RILASCIO = this.model.getCausaRilascio(comboBoxServizio.getValue());
        LocalDate dataRitiro = null;

        if (CAUSA_RILASCIO == null) {
            CAUSA_RILASCIO = "";
            dataRitiro = this.model.getIfPassaportoRitiro();
            isRitiroDisponibile = (dataRitiro != null);
            if (!isRitiroDisponibile) {
                startAlert("ritiro");
                comboBoxSede.setMouseTransparent(true);
                comboBoxSede.setOpacity(0.5);
            }
        } else if (CAUSA_RILASCIO.equals("primo passaporto")) {
            CAUSA_RILASCIO = "primo passaporto";
            isPrimoPassaporto = this.model.getPresenzaPassaporto();
            if (!isPrimoPassaporto) {
                startAlert("primo passaporto");
                comboBoxSede.setMouseTransparent(true);
                comboBoxSede.setOpacity(0.5);
            }
        } else if (CAUSA_RILASCIO.equals("scadenza")) {
            CAUSA_RILASCIO = "scadenza";
            isPassaportoInScadenza = this.model.checkScadenzaPP();
            if (!isPassaportoInScadenza) {
                startAlert("in scadenza");
                comboBoxSede.setMouseTransparent(true);
                comboBoxSede.setOpacity(0.5);
            }
        } else if (CAUSA_RILASCIO.equals("furto") || CAUSA_RILASCIO.equals("deterioramento") || CAUSA_RILASCIO.equals("smarrimento")) {
            isPassaportoFRS = this.model.getPassaportoFRS();
            if (!isPassaportoFRS) {
                startAlert("attenzione");
                comboBoxSede.setMouseTransparent(true);
                comboBoxSede.setOpacity(0.5);
            }
        }

        if (isPrimoPassaporto || isPassaportoInScadenza || isPassaportoFRS || isRitiroDisponibile) {
            confermaPrenotazioneServizioSede.setOpacity(1);
            confermaPrenotazioneServizioSede.setMouseTransparent(false);
            try {
                cittaServizio = this.model.getSedeServizi(comboBoxServizio.getValue());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            if(cittaServizio.isEmpty()){
                startAlertServizio();
            } else {
                int dim = cittaServizio.size();
                ObservableList<String> sediDisp = FXCollections.observableArrayList();
                for(int i = 0; i<dim; i++){
                    sediDisp.add(cittaServizio.get(i));
                }
                comboBoxSede.setItems(sediDisp);
                comboBoxSede.setMouseTransparent(false);
                comboBoxSede.setOpacity(1);
            }
        }

    }

    /**
     * Metodo che gestisce l'apertura del portale del cittadino con actionevent diverso e che dipende da una operazione
     * di cambiamento dei dati del database da parte del model.
     * @param e
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    private void indietroPrenotazioneUtente(ActionEvent e) throws IOException, SQLException {
        this.model.deletePrenotazione(TIME_STAMP_ENTRATA);
        openPortaleCittadino(e);
    }
}
