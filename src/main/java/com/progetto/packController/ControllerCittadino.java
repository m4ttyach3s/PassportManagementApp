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
    ZonedDateTime dateFocus; // Focus date for the calendar
    ZonedDateTime today; // Current date

    @FXML
    private Text year = new Text(); // Text element displaying the year

    @FXML
    private Text month = new Text(); // Text element displaying the month

    @FXML
    private FlowPane calendar = new FlowPane(); // FlowPane representing the calendar grid
    @FXML
    private Button backOne = new Button();

    private static final int START_TIME = 8*60; // 5 minutes in seconds
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

    // -------- sezione apertura FXML ----------
    //TODO
    //  aggiungere timestamp
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
        // prendere time stamp > per prenotazione
        // fare update db
    }

    private void openPortaleCittadino(ActionEvent eventAccediCittadino) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/ViewCittadino/portale-cittadino-view.fxml"));
        Stage stage = (Stage) ((Node) eventAccediCittadino.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void tornaPortaleCittadino(ActionEvent eventAccediCittadino) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/ViewCittadino/portale-cittadino-view.fxml"));
        Stage stage = (Stage) ((Node) eventAccediCittadino.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void indietro(ActionEvent eventIndietro) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/first-view.fxml"));
        Stage stage = (Stage) ((Node) eventIndietro.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void indietroPortaleCittadino(ActionEvent eventIndietroCitt)throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/ViewCittadino/portale-cittadino-view.fxml"));
        Stage stage = (Stage) ((Node) eventIndietroCitt.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    @FXML
    void modificaDati(ActionEvent eventDati) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/ViewCittadino/modifica-dati-cittadino-view.fxml"));
        Stage stage = (Stage) ((Node) eventDati.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


    // TODO
    //  creare la classe Calendario con i pulsanti per gli orari
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


    private void openCalendar(ActionEvent e) throws IOException {
        /*
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/Calendar.fxml"));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        ControllerCittadino.setPrimaryStage(stage);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

         */
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

    // ---------- fine FXML

    // ---------- Sezione ALERT ------------
    private void startAlertAnnullamento() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setHeaderText("Operazione non completata!");
        alert.setContentText("La tua prenotazione è già stata annullata oppure risulta esserci qualche problema.\nProva ad uscire e a rientrare dal portale.");
        alert.showAndWait();
    }

    private void startSuccessAnnullamento() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confermato!");
        alert.setHeaderText("Operazione completata");
        alert.setContentText("Operazione completata con successo! La tua prenotazione è stata annullata");
        alert.showAndWait();
    }

    private void startPDFSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confermato!");
        alert.setHeaderText("Operazione completata");
        alert.setContentText("Operazione completata con successo! Troverà il pdf scaricato nella cartella Download.");
        alert.showAndWait();
    }


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
// ------ fine ALERT ------


    // --------- sezione METODI ---------

    //      DVDFCC01D23M150F
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


    private void checkEditPwdButton() {
        isEditPWD = !isEditPWD;
        if(isEditPWD){editPassword();}else{hidePwd();}
    }

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

    private void checkEditMailButton() {
        isEditMAIL = !isEditMAIL;
        if(isEditMAIL){editMail();}else{hideMail();}
    }

    private void hideMail() {
        labelNuovaMail.setVisible(false);
        nuovaMail.setVisible(false);
        confermaModificaMail.setVisible(false);
        labelNuovaMail.setMouseTransparent(true);
        nuovaMail.setMouseTransparent(true);
        confermaModificaMail.setMouseTransparent(true);
        nuovaMail.setText("");
    }

    private void editMail() {
        labelNuovaMail.setMouseTransparent(false);
        nuovaMail.setMouseTransparent(false);
        labelNuovaMail.setVisible(true);
        nuovaMail.setVisible(true);
        confermaModificaMail.setVisible(true);
        confermaModificaMail.setMouseTransparent(false);
    }

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

    @FXML // --> to model
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

    @FXML // --> to model
    void UpdateMailCittadino(ActionEvent event) throws SQLException {

        if(this.model.checkMail(nuovaMail.getText())) {
            this.model.updateCittadinoMail(this.model.getId(), nuovaMail.getText());
            startSuccess("Mail");
        } else {
            startAlert("Mail");
        }
    }

    private void checkHiddenPwd() {
        isPWDHidden = !isPWDHidden;
        if(isPWDHidden){
            showHiddenPWD();
        } else {
            hideHiddenPWD();
        }
    }

    private void hideHiddenPWD() {
        hiddenTF.setVisible(false);
        hiddenTF.toBack();
        hiddenTF.setMouseTransparent(true);
        nuovaPassword.toFront();
        nuovaPassword.setText(hiddenTF.getText());
        nuovaPassword.setVisible(true);
    }

    private void showHiddenPWD() {
        hiddenTF.toFront();
        hiddenTF.setVisible(true);
        hiddenTF.setMouseTransparent(false);
        nuovaPassword.toBack();
        nuovaPassword.setVisible(false);
        hiddenTF.setText(nuovaPassword.getText());
    }

    @FXML
    void backOneMonth(ActionEvent event) throws SQLException {
        dateFocus = dateFocus.minusMonths(1); // Move focus date one month back
        calendar.getChildren().clear(); // Clear the calendar grid
        drawCalendar(); // Draw the updated calendar
        updateButtonStatus(backOne); // Update the button status after changing the date
    }

    @FXML
    void forwardOneMonth(ActionEvent event) throws SQLException {
        dateFocus = dateFocus.plusMonths(1); // Move focus date one month forward
        calendar.getChildren().clear(); // Clear the calendar grid
        drawCalendar(); // Draw the updated calendar
        updateButtonStatus(backOne); // Update the button status after changing the date
    }

    private void updateButtonStatus(Button button) {
        button.setDisable(!canGoBack());
    }

    private boolean canGoBack() {
        ZonedDateTime initialDate = ZonedDateTime.now(); // Set the initial date
        return !dateFocus.isBefore(initialDate);
    }

    @FXML
    private void openCalendarApp(ActionEvent e){
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
                //Platform.runLater(this::executeCalendarLogic);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    // TODO
    //      Se la data e l'ora sono minori dell'ora e data attuale --> button in rosso
    //      Se il button è rosso --> do not retrieve text.
    private void drawCalendar() throws SQLException {
        year.setText(String.valueOf(dateFocus.getYear())); // Set the year text
        month.setText(dateFocus.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALIAN).toUpperCase());

        // Dimensions and styling for the calendar elements
        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        int monthMaxDate = dateFocus.getMonth().maxLength(); // Get the maximum number of days in the month
        // Check for leap year
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone())
                .getDayOfWeek().getValue(); // Get the day of the week offset for the first day of the month

        for (int i = 0; i < 6; i++) { // Iterate over the calendar grid rows
            for (int j = 0; j < 7; j++) { // Iterate over the calendar grid columns
                StackPane stackPane = new StackPane(); // Create a stack pane for each calendar cell
                Rectangle rectangle = new Rectangle(); // Create a rectangle element for the cell
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH; // Calculate the width of the rectangle
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV; // Calculate the height of the rectangle
                rectangle.setHeight(rectangleHeight);

                stackPane.getChildren().add(rectangle); // Add the rectangle to the stack pane

                int calculatedDate = (j + 1) + (7 * i); // Calculate the date for the current cell
                Label data = new Label();
                ArrayList<String> giorniRossi = new ArrayList<>();
                if (calculatedDate > dateOffset) { // Check if the date is valid for the current month
                    int currentDate = calculatedDate - dateOffset; // Calculate the current date of the month
                    if (currentDate <= monthMaxDate) { // Check if the current date is within the valid range
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
                        boolean hasSlots = rs.next(); // Flag to check if there are slots
                        boolean isEmpty = false;

                        // if numero posti <= 0 --> rosso
                        // if data non c'è giallo
                        if (!hasSlots) {
                            rectangle.setFill(Paint.valueOf("#f7ce5c")); // giallo
                        } else {
                            rectangle.setFill(Paint.valueOf("#6ebf5a")); // verde
                            rectangle.toBack();
                        }

                        VBox buttonContainer = new VBox(); // VBox to hold the buttons vertically
                        int counter = 0;
                        if (hasSlots) {
                            do {
                                //giorniRossi.clear();

                                LocalTime slotHour = LocalTime.parse(rs.getString(1));
                                Button button = new Button(trasformaOrario(String.valueOf(slotHour)));
                                Label labelRed = new Label(trasformaOrario(rs.getString(1)));

                                button.setText(trasformaOrario(rs.getString(1)));
                                button.setMaxWidth(rectangleWidth - strokeWidth); // Set max width to fit within rectangle


                                int slotsNumber = rs.getInt(2);

                                if(dateC.isBefore(LocalDate.now())){
                                    rectangle.setFill(Paint.valueOf("#808080"));
                                    rectangle.toBack();
                                }

                                //if (slotsNumber <= 0 || (slotsNumber<=0 && slotHour.isBefore(LocalTime.now()))) {

                                if (slotsNumber <= 0) {
                                    counter++;
                                    button.setStyle("-fx-background-color: #f76363; -fx-border-color: black; -fx-border-width: 1px;");
                                    button.setMouseTransparent(true);
                                    button.toFront();
                                    giorniRossi.add(button.getText());
                                }
                                if(dateC.isBefore(LocalDate.now())){
                                    button.setStyle("-fx-background-color: #808080; -fx-border-color: black; -fx-border-width: 1px;");
                                    button.setMouseTransparent(true);
                                    rectangle.setFill(Paint.valueOf("#808080"));
                                    button.setTextFill(Paint.valueOf("#ffffff"));
                                    button.toFront();
                                } else {
                                    button.setStyle("-fx-background-color: #6ebf5a; -fx-border-color: black; -fx-border-width: 1px;");
                                    button.setTextFill(Paint.valueOf("#ffffff"));
                                }

                                if(dateC.equals(LocalDate.now()) && slotHour.isBefore(LocalTime.now())){
                                    button.setStyle("-fx-background-color: #808080; -fx-border-color: black; -fx-border-width: 1px;");
                                    button.setMouseTransparent(true);
                                    rectangle.setFill(Paint.valueOf("#808080"));
                                    button.toFront();
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
                                //buttonContainer.setOpacity(0);
                                buttonContainer.getChildren().add(button); // Add the button to the VBox
                            } while (rs.next());
                        }

                        if (hasSlots) {
                            ScrollPane scrollPane = new ScrollPane(buttonContainer); // Wrap the VBox in a ScrollPane
                            scrollPane.setPrefWidth(rectangleWidth);
                            scrollPane.setPrefHeight(rectangleHeight);
                            scrollPane.setFitToWidth(true); // Enable horizontal scrolling if necessary
                            scrollPane.setFitToHeight(true); // Enable vertical scrolling if necessary
                            stackPane.getChildren().addAll(scrollPane); // Add the scroll pane to the stack pane
                        }

                        if (dateC.getDayOfWeek() == DayOfWeek.SATURDAY || dateC.getDayOfWeek() == DayOfWeek.SUNDAY || dateC.isBefore(LocalDate.now())) {
                            rectangle.setFill(Paint.valueOf("#808080"));
                        }
                    }
                    if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth()
                            && today.getDayOfMonth() == currentDate) {
                        rectangle.setFill(Paint.valueOf("#add8e6"));
                        data.setStyle("-fx-background-color: #add8e6; -fx-border-color: black");
                    }
                } else {
                    //Label data = new Label("    ");
                    data.setStyle("-fx-background-color: #f0fafc; -fx-border-color: black");
                    data.setPrefWidth(rectangleWidth);
                    data.setAlignment(Pos.CENTER);
                    data.setTranslateY(-(rectangleHeight) / 2 - 5);
                    stackPane.getChildren().add(data);
                }
                calendar.getChildren().add(stackPane); // Add the stack pane to the calendar grid
            }
        }
    }

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

    private String updateTimerLabel() {
        int minutes = remainingTime / 60;
        int seconds = remainingTime % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void stopTimer() throws IOException, SQLException {
        timeline.stop();
        isTimerOver = true;
        stageSetBack = true;

        indietroButtonPrenotazione.fire();

        /*
        indietrohidden.setOnAction(e->{
            try {
                this.model.deletePrenotazione(TIME_STAMP_ENTRATA);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            try {
                openPortaleCittadino(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });*/
    }

    // to model
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

    private void stopTimerCoda() throws IOException {
        if(hasCodaStarted){
            buttonAggiornaCoda.fire();
        } else {
            timeline.stop();
            HAS_CODA_FINISHED = true;
            calendarLogicExecuted = false;
        }
    }

    /*
    @FXML
    private void setConfermaPrenotazione(ActionEvent e) throws SQLException {
        if((slotPrenotazione != null || !slotPrenotazione.equals("")) && giornoPrenotazione!=null){

            if(RITIRO_TRENTA_GIORNI){
                boolean checker = this.model.checkRitiro(giornoPrenotazione);
                if(checker){
                    this.model.setPrenotazione(numSedePP, giornoPrenotazione, slotPrenotazione, TIME_STAMP_ENTRATA, SERVIZIO_ENTRATA);
                } else {
                    startAlert("ritiro mancante");
                }
            }

            // controllo 6 mesi.
            this.model.setPrenotazione(numSedePP, giornoPrenotazione, slotPrenotazione, TIME_STAMP_ENTRATA, SERVIZIO_ENTRATA);
            try {
                startAzioneSuccess("slot");
                openPortaleCittadino(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            startAlert("slot orario");
        }
    }
*/
    @FXML
    private void setConfermaPrenotazione(ActionEvent event) throws SQLException, IOException {
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

    private boolean isValidInput() {
        return slotPrenotazione != null && !slotPrenotazione.isEmpty() && giornoPrenotazione != null;
    }

    private void startAzioneSuccess(String value) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confermato!");
        alert.setHeaderText("Operazione completata");


        if(value.equals("slot")){
            alert.setContentText("Operazione completata con successo! La sua prenotazione per il giorno "+giornoPrenotazione +" tra le ore "+slotPrenotazione +" è stata inserita.");
        }
        alert.showAndWait();
    }

    // ---------- fine METODI

    // -- !! INITIALIZE !! --

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

        dateFocus = ZonedDateTime.now(); // Set the focus date to the current date
        today = ZonedDateTime.now(); // Set today's date
        updateButtonStatus(backOne); // Update the initial button status

        comboBoxSede.setMouseTransparent(true);
        comboBoxSede.setOpacity(0.5);

        ObservableList<String> serviziPrenotazioni = FXCollections.observableArrayList("Ritiro", "Rilascio per primo passaporto", "Rilascio per scadenza", "Rilascio per furto", "Rilascio per smarrimento", "Rilascio per deterioramento");
        comboBoxServizio.setItems(serviziPrenotazioni);

        buttonAggiornaCoda.setOnAction(e->{
            openCalendarApp(e);
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

    private void startAlertServizio() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setHeaderText("Operazione non riuscita");
        alert.setContentText("Non c'è nessuna sede disponibile per questo servizio.");
        alert.showAndWait();
    }

    private void executeCalendarLogic() {
        if (calendarLogicExecuted || isTimerOver || indietroButtonPrenotazione.isPressed()) {
            return;
        }

        try {
            drawCalendar(); // Call drawCalendar once on the JavaFX Application Thread
            startTimer();
            calendarLogicExecuted = true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    private void getCheckCittaServizio(ActionEvent e) throws SQLException {
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


    @FXML
    private void indietroPrenotazioneUtente(ActionEvent e) throws IOException, SQLException {
        this.model.deletePrenotazione(TIME_STAMP_ENTRATA);
        openPortaleCittadino(e);
    }
}
