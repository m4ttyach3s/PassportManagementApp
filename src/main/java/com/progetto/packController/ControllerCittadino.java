package com.progetto.packController;

import com.progetto.EncryptionPass;
import com.progetto.packModel.Model;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

public class ControllerCittadino implements Initializable {
    private static final String BACK_COLOR = "-fx-background-color: #c2c0c0";
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
    private RadioButton radioButtonPasswordModifica = new RadioButton();
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

    private static final int START_TIME = 5 * 60; // 5 minutes in seconds
    private int remainingTime;
    private Label labelTempoRimasto;
    private Timeline timeline;

    //TODO
    //  aggiungere timestamp
    @FXML
    void accediCittadino(ActionEvent eventAccediCittadino) throws SQLException, IOException, NoSuchAlgorithmException {
        boolean loginChecker = this.model.checkLogin(codiceFiscale.getText(), passwordCittadino.getText(), "cittadino");

        if(loginChecker){
            Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/ViewCittadino/portale-cittadino-view.fxml"));
            Stage stage = (Stage) ((Node) eventAccediCittadino.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String login_date = dtf.format(now);
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
                    List<String> lista = new ArrayList<String>();
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


    @FXML
    void prenotazioniPassate() throws SQLException {
        VBox vboxpassate = new VBox();
        vboxpassate.setSpacing(10);
        vboxpassate.setPadding(new Insets(10));

        ResultSet rs = this.model.getPrenotazioniPassate();
        while (rs.next()) {
            Label idLabel = new Label("ID Prenotazione: " + rs.getString("ID"));
            Label causaRilascioLabel = new Label("Causale: " +rs.getString("causaRilascio"));
            Label servizioLabel = new Label("Servizio: " +rs.getString("servizio"));

            String id = rs.getString("ID");
            String citta = rs.getString("città");
            Time ora = rs.getTime("ora");
            Date giorno = rs.getDate("giorno");
            String servizio = rs.getString("servizio");
            String causaR = rs.getString("causaRilascio");

            Button dettagliButton = new Button("PDF Dettagli");

            dettagliButton.setOnAction(event -> {
                List<String> lista = new ArrayList<String>();
                lista.add(id); // 0
                lista.add(citta); // 1
                lista.add(String.valueOf(ora)); // 2
                lista.add(String.valueOf(giorno)); // 3
                lista.add(servizio); // 4
                lista.add(causaR); // 5
                lista.add(this.model.getNome()); // 6
                lista.add(this.model.getCognome()); // 7
                lista.add(this.model.getId()); // 8
                this.model.makePDF(lista, servizio, causaR);
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

    private void startPDFSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confermato!");
        alert.setHeaderText("Operazione completata");
        alert.setContentText("Operazione completata con successo! Troverà il pdf scaricato nella cartella Download.");
        alert.showAndWait();
    }

    // TODO
    //  creare la classe Calendario con i pulsanti per gli orari
    @FXML
    void openPortalePrenotazioni(ActionEvent nP) throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/CalendarPre.fxml"));
        Stage stage = (Stage) ((Node) nP.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
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
        alert.setContentText("Il campo "+string+" non è corretto.");
        alert.showAndWait();
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
        drawCalendar(buttonRitiro.getText()); // Draw the updated calendar
        updateButtonStatus(backOne); // Update the button status after changing the date
    }

    @FXML
    void forwardOneMonth(ActionEvent event) throws SQLException {
        dateFocus = dateFocus.plusMonths(1); // Move focus date one month forward
        calendar.getChildren().clear(); // Clear the calendar grid
        drawCalendar(buttonRitiro.getText()); // Draw the updated calendar
        updateButtonStatus(backOne); // Update the button status after changing the date
    }

    private void updateButtonStatus(Button button) {
        button.setDisable(!canGoBack());
    }

    private boolean canGoBack() {
        ZonedDateTime initialDate = ZonedDateTime.now(); // Set the initial date
        return !dateFocus.isBefore(initialDate);
    }

    private void drawCalendar(String text) throws SQLException {
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

                if (calculatedDate > dateOffset) { // Check if the date is valid for the current month
                    int currentDate = calculatedDate - dateOffset; // Calculate the current date of the month
                    if (currentDate <= monthMaxDate) { // Check if the current date is within the valid range
                        Label data = new Label(String.valueOf(currentDate));
                        data.setStyle("-fx-background-color: #f0fafc");
                        data.setPrefWidth(rectangleWidth);
                        data.setAlignment(Pos.CENTER);
                        data.setTranslateY(-(rectangleHeight)/2 - 5);
                        stackPane.getChildren().add(data);

                        int cYear = dateFocus.getYear();
                        int cMonth = dateFocus.getMonthValue();
                        LocalDate dateC = LocalDate.of(cYear, cMonth, currentDate);

                        ResultSet rs = this.model.getSlots(dateC);
                        boolean hasSlots = false; // Flag to check if there are slots
                        boolean isEmpty = false;

                        // if numero posti <= 0 --> rosso
                        // if data non c'è giallo
                        if (!rs.next()) {
                            rectangle.setFill(Paint.valueOf("#f7ce5c")); //giallo
                        } else {
                            if(rs.getInt(2) <=0){
                                isEmpty = true;
                            }
                            hasSlots = true;
                            rectangle.setFill(Paint.valueOf("#6ebf5a")); //verde
                        }

                        VBox buttonContainer = new VBox(); // VBox to hold the buttons vertically

                        while (rs.next()) {
                            Button button = new Button(trasformaOrario(rs.getString(1)));
                            button.setMaxWidth(rectangleWidth - strokeWidth); // Set max width to fit within rectangle
                            buttonContainer.setSpacing(5);
                            buttonContainer.getChildren().add(button); // Add the button to the VBox
                            buttonContainer.setStyle("-fx-background-color: #6ebf5a");
                            if(rs.getInt(2)<=0){
                                button.setStyle("-fx-background-color: #f76363; -fx-border-color: black; -fx-border-width: 1px;");
                                button.setMouseTransparent(true);
                            } else {
                                button.setStyle("-fx-background-color: #6ebf5a; -fx-border-color: black; -fx-border-width: 1px;");
                                button.setTextFill(Paint.valueOf("#ffffff"));
                            }
                        }

                        if (hasSlots) {
                            ScrollPane scrollPane = new ScrollPane(buttonContainer); // Wrap the VBox in a ScrollPane
                            scrollPane.setPrefWidth(rectangleWidth);
                            scrollPane.setPrefHeight(rectangleHeight);
                            scrollPane.setFitToWidth(true); // Enable horizontal scrolling if necessary
                            scrollPane.setFitToHeight(true); // Enable vertical scrolling if necessary
                            stackPane.getChildren().addAll(scrollPane); // Add the scroll pane to the stack pane

                        }
                        if(isEmpty){
                            rectangle.setFill(Paint.valueOf("#f76363")); //rosso
                            rectangle.setStroke(Paint.valueOf("#ff0000"));
                            rectangle.toFront();
                        }

                        if (dateC.getDayOfWeek() == DayOfWeek.SATURDAY || dateC.getDayOfWeek() == DayOfWeek.SUNDAY) {
                            rectangle.setFill(Paint.valueOf("#808080"));
                        }
                    }
                    if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth()
                            && today.getDayOfMonth() == currentDate) {
                        rectangle.setFill(Paint.valueOf("#add8e6"));
                    }
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
                    updateTimerLabel();

                    if (remainingTime <= 0) {
                        stopTimer();
                    }
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void updateTimerLabel() {
        int minutes = remainingTime / 60;
        int seconds = remainingTime % 60;
        String formattedTime = String.format("%02d:%02d", minutes, seconds);
        labelTempoRimasto.setText(formattedTime);
    }

    private void stopTimer() {
        timeline.stop();
    }


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



        buttonRilascio.setOnAction(e->{
            try {
                openCalendar(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            try {
                drawCalendar(buttonRilascio.getText());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        buttonRitiro.setOnAction(e->{
            try {
                openCalendar(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            try {
                drawCalendar(buttonRitiro.getText());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        Platform.runLater(() -> {
            try {
                drawCalendar("Ritiro"); // Call drawCalendar once on the JavaFX Application Thread
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void openCalendar(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/Calendar.fxml"));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
