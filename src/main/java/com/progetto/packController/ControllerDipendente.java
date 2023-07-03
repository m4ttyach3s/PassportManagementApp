package com.progetto.packController;

import com.progetto.packModel.Model;
import com.progetto.packModel.Passaporto;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ControllerDipendente implements Initializable {
    private Model model = Model.getInstance();
    private static ControllerDipendente controllerDipendente;
    Passaporto passaportoDip = new Passaporto(null, null, null, null, null);

    private static final String BACK_COLOR = "-fx-background-color: #c2c0c0";
    private static boolean dipendenteChecker = false;
    @FXML
    private TextField matricolaDipendente;
    @FXML
    private Label message = new Label();
    @FXML
    private PasswordField passwordDipendente;
    @FXML
    private Accordion AccordionUtente = new Accordion();

    @FXML
    private AnchorPane AnchorPane2 = new AnchorPane();

    @FXML
    private AnchorPane AnchorPane3 = new AnchorPane();

    @FXML
    private TitledPane PrenotazioniAttive = new TitledPane();

    @FXML
    private TitledPane PrenotazioniPassate = new TitledPane();

    @FXML
    private ScrollPane ScrollPaneAttive = new ScrollPane();

    @FXML
    private ScrollPane ScrollPanePassate = new ScrollPane();

    @FXML
    private Button assegnaSlot = new Button();

    @FXML
    private Button gestioneDipendenti = new Button();

    @FXML
    private Label labelCognome = new Label();

    @FXML
    private Label labelNome = new Label();

    @FXML
    private Label labelRuolo = new Label();

    @FXML
    private Label labelSede = new Label();

    @FXML
    private Line lineaDx = new Line();

    @FXML
    private Line lineaSx = new Line();

    @FXML
    private Label labelMatricola = new Label();

    @FXML
    private VBox vboxpattive = new VBox();

    @FXML
    private Rectangle rettangoloDipendente = new Rectangle();
    @FXML
    private VBox vboxppassate = new VBox();

    @FXML
    private TextField cercaPrenotazioneField = new TextField();
    @FXML
    private Button buttonCerca = new Button();
    @FXML
    private SplitMenuButton filtroPrenotazioni = new SplitMenuButton();
    @FXML
    private Button buttonNuovoPP = new Button();

    @FXML
    private Button buttonRinunciaPrenotazione = new Button();

    @FXML
    private Button buttonRitiraPP = new Button();

    @FXML
    private Label labelDettPrenotazione = new Label();

    @FXML
    private Label labelDettCittadino = new Label();
    @FXML
    private VBox vboxPP = new VBox();
    @FXML
    private Button buttonStorico = new Button();
    @FXML
    private Label rettSugg = new Label();
    @FXML
    private Button buttonDati = new Button();
    @FXML
    private Rectangle rettPP = new Rectangle();
    @FXML
    private Rectangle rettDP = new Rectangle();
    @FXML
    private Rectangle rettCC = new Rectangle();
    @FXML
    private Label labelPrenotazioneD = new Label();
    @FXML
    private Label labelCittadinoD = new Label();
    @FXML
    private ScrollPane scrollPanePP = new ScrollPane();
    @FXML
    private Label labelPassaportiStorico = new Label();
    @FXML
    private Line line1 = new Line();
    @FXML
    private Rectangle rettMostra = new Rectangle();

    @FXML
    private Label dataScadenzaPP = new Label();
    @FXML
    private Button generaNumeroPassaporto = new Button();
    @FXML
    private Label labelInserimentoCF = new Label();
    @FXML
    private Label numeroPassaporto = new Label();
    @FXML
    private ChoiceBox<String> choiceBox = new ChoiceBox<>();
    @FXML
    private Button confermaInserimentoPP = new Button();
    private static String numero_pp = new String();

    @FXML
    private Label CFPPR = new Label();
    @FXML
    private Button indietroPPR = new Button();
    @FXML
    private Label numPPR = new Label();
    @FXML
    private Button ritiroPPR = new Button();
    @FXML
    private Label scadenzaPPR = new Label();
    @FXML
    private Button terminaPPR = new Button();
    @FXML
    private Label tipoPPR = new Label();
    // --- fxml view slot ---
    @FXML
    private Button aggiungiSlot = new Button();
    @FXML
    private Button confermaDataSlot = new Button();
    @FXML
    private DatePicker datePickerSlot = new DatePicker();
    @FXML
    private Button indietroSlot = new Button();
    @FXML
    private ScrollPane scrollpaneAccSlot = new ScrollPane();
    @FXML
    private ScrollPane scrollpaneSlot = new ScrollPane();
    @FXML
    private VBox vboxAccSlot = new VBox();
    @FXML
    private VBox vboxSlot = new VBox();

    @FXML
    private TitledPane anteprimaSlot = new TitledPane();
    @FXML
    private Button caricaDipSlot = new Button();
    @FXML
    private Button caricaSlot = new Button();
    @FXML
    private Label labelGestioneSede = new Label();
    @FXML
    private TitledPane paneDipendentiPresenti = new TitledPane();
    @FXML
    private Button indietroDipendente = new Button();
    @FXML
    private VBox vboxPaneDipendenti = new VBox();
    @FXML
    private Button buttonAggiungiNuovoDipendente = new Button();

    @FXML
    private Button buttonGeneraMatricolaNuovoDipendente = new Button();

    @FXML
    private Button buttonGeneraPasswordNuovoDipendente = new Button();

    @FXML
    private Button buttonNuovoDipendente = new Button();
    //----- da inizializzare nascoste
    @FXML
    private ChoiceBox<String> choiceBoxContrattoNuovoDipendente = new ChoiceBox<>();
    @FXML
    private TextField fieldCognomeNuovoDipendente = new TextField();
    @FXML
    private TextField fieldMailNuovoDipendente = new TextField();
    @FXML
    private TextField fieldNomeNuovoDipendente = new TextField();
    @FXML
    private TextField fieldTelefonoAziendaleNuovoDipendente = new TextField();
    @FXML
    private Label labelCognomeNuovoDipendente = new Label();
    @FXML
    private Label labelMatricolaGenerataNuovoDipendente = new Label();
    @FXML
    private Label labelNomeNuovoDipendente = new Label();
    @FXML
    private Label labelPasswordNuovoDipendente = new Label();
    @FXML
    private Label labelTelNuovoDipendente = new Label();
    @FXML
    private Label labelTipoContrattoNuovoDipendente = new Label();
    @FXML
    private Label mailNuovoDipendente = new Label();


    private static String gg = new String();
    private static boolean sceltaDipSlot = false;
    private static LocalDate dataSlot;
    private static ArrayList<String> c = new ArrayList<>();
    DatePicker datePicker = new DatePicker();
    MenuItem datePickerItem = new MenuItem();
    private static int ndip;
    private boolean isYesPressed = false;
    private boolean isNoPressed = false;

    @FXML
    private ScrollPane scrollPaneGestioneDipendenti = new ScrollPane();


    @FXML
    void accediDipendente(ActionEvent eventAccediDipendente) throws SQLException, IOException, NoSuchAlgorithmException {
        boolean loginChecker = this.model.checkLogin(matricolaDipendente.getText(), passwordDipendente.getText(), "addetto");

        if (loginChecker) {
            Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/ViewDipendente/portale-dipendente-view.fxml"));
            Stage stage = (Stage) ((Node) eventAccediDipendente.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } else {
            message.setText("USERNAME O PASSWORD NON CORRETTI.");
            matricolaDipendente.setText("");
            matricolaDipendente.setPromptText("Matricola Personale");
            passwordDipendente.setText("");
            passwordDipendente.setPromptText("Password");
        }
    }

    private void initDipScene() {
        lineaSx.setVisible(false);
        lineaDx.setVisible(false);
        assegnaSlot.setVisible(false);
        gestioneDipendenti.setVisible(false);
    }

    @FXML
    void indietro(ActionEvent eventIndietro) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/first-view.fxml"));
        Stage stage = (Stage) ((Node) eventIndietro.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.model.getDipSede();
        dipendenteChecker = this.model.dipendenteCheck(this.model.getId());
        if (!dipendenteChecker) {
            initDipScene();
        }

        rettangoloDipendente.setStyle("-fx-background-color: #006642");
        labelMatricola.setText(this.model.getId());
        labelNome.setText(this.model.getNome());
        labelCognome.setText(this.model.getCognome());
        labelSede.setText("Sede di: " + this.model.getSede());
        labelRuolo.setText(this.model.getRuolo());

        AnchorPane2.setStyle(BACK_COLOR);
        AnchorPane3.setStyle(BACK_COLOR);

        filtroPrenotazioni.getItems().clear();

        datePicker.setPromptText("Tutte le prenotazioni del: ");
        datePickerItem.setGraphic(datePicker); // passo datepicker come 1 scelta
        MenuItem secondaScelta = new MenuItem("Tutte le prenotazioni da eseguire oggi, " + LocalDate.now());
        MenuItem terzaScelta = new MenuItem("Tutte le prenotazioni eseguite da: " + this.model.getId());
        filtroPrenotazioni.getItems().addAll(datePickerItem, secondaScelta, terzaScelta);


        datePicker.setOnAction((e) -> {
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate != null) {
                ResultSet rs = this.model.showPrenotazioniDatePicker(selectedDate.toString());
                try {
                    showResults(rs);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                PrenotazioniAttive.setText("Prenotazioni del giorno: " + selectedDate);
                PrenotazioniAttive.setExpanded(true);
            }
        });

        secondaScelta.setOnAction((e) -> {
            ResultSet rs = this.model.showPrenotazioniOdierne(LocalDate.now());
            try {
                showResults(rs);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            PrenotazioniAttive.setText("Prenotazioni odierne: " + LocalDate.now());
            PrenotazioniAttive.setExpanded(true);
            PrenotazioniAttive.setExpanded(true);
        });

        terzaScelta.setOnAction((e) -> {
            ResultSet rs = this.model.showTuttePrenotazioni();
            try {
                showResults(rs);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            PrenotazioniAttive.setText("Tutte le prenotazioni di: " + this.model.getId());
            PrenotazioniAttive.setExpanded(true);
        });

        buttonCerca.setOnAction((e) -> {
            if (cercaPrenotazioneField.getText().equals(null) || cercaPrenotazioneField.getText().equals("")) {
                startAlert();
            } else {
                ResultSet rs = this.model.getPrenotazioniID(Integer.parseInt(cercaPrenotazioneField.getText()));
                try {
                    showResults(rs);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                PrenotazioniAttive.setText("Prenotazioni con ID: " + cercaPrenotazioneField.getText());
                PrenotazioniAttive.setExpanded(true);
            }
        });

        rettSugg.setOnMouseEntered(e -> rettSugg.setText("Mostra"));
        rettSugg.setOnMouseExited(e -> rettSugg.setText(""));

        buttonStorico.setOnAction(e -> {
            try {
                showPassaportiCittadino();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        buttonDati.setOnMouseEntered(e -> {
            buttonDati.setStyle("-fx-background-color: #C4D5CF");
            buttonDati.setTextFill(Paint.valueOf("black"));
        });
        buttonDati.setOnMouseExited(e -> {
            buttonDati.setStyle("-fx-background-color: #76A997");
            buttonDati.setTextFill(Paint.valueOf("white"));
        });

        buttonDati.setOnAction(e -> {
            buttonDati.setStyle("-fx-background-color: #006642");
            buttonDati.toBack();
            buttonDati.setVisible(false);

            rettPP.setVisible(true);
            rettDP.setVisible(true);
            rettCC.setVisible(true);
            rettMostra.setVisible(true);
            rettSugg.setVisible(true);

            labelPrenotazioneD.setVisible(true);
            labelCittadinoD.setVisible(true);
            labelPassaportiStorico.setVisible(true);

            scrollPanePP.setVisible(true);
            vboxPP.setVisible(true);

            buttonNuovoPP.setVisible(true);
            buttonStorico.setVisible(true);
            buttonRitiraPP.setVisible(true);

            line1.setVisible(true);
            setLabels(c.get(0), c.get(1), c.get(2), c.get(3), c.get(4)); // per i dati labels
            if (c.get(0).equals("Rilascio")) {
                buttonRitiraPP.setMouseTransparent(true);
                buttonRitiraPP.setOpacity(0.5);
                buttonNuovoPP.setOpacity(1.0);
            } else if (c.get(0).equals("Ritiro")) {
                buttonNuovoPP.setMouseTransparent(true);
                buttonNuovoPP.setOpacity(0.5);
                buttonRitiraPP.setOpacity(1.0);
            }
        });

        buttonRinunciaPrenotazione.setOnAction(e -> {
            try {
                updateRinunciaPrenotazioni(e, c.get(1));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            //c.clear();
        });
        buttonNuovoPP.setOnAction(e -> {
            try {
                updateRilascioPassaporto(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        ObservableList<String> cat = FXCollections.observableArrayList("Passaporto ordinario", "Passaporto diplomatico", "Passaporto di servizio", "Passaporto di emergenza", "Passaporto per rifugiati", "Passaporto per minori");
        choiceBox.setItems(cat);

        labelInserimentoCF.setText(this.model.getCFCittadino());
        dataScadenzaPP.setText(this.model.getDataScadenza());

        generaNumeroPassaporto.setOnAction(e -> {
            numero_pp = this.model.getNumeroPassaporto();
            numeroPassaporto.setText(numero_pp);
            generaNumeroPassaporto.toBack();
            generaNumeroPassaporto.setMouseTransparent(true);
            generaNumeroPassaporto.setOpacity(0.0);
            numeroPassaporto.toFront();
        });

        confermaInserimentoPP.setOnAction(e -> {
            try {
                insertNuovoPP(e);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            c.clear();
        });


        buttonRitiraPP.setOnAction(e -> {
            try {
                updateRitiroPassaporto(e);
                setPassaportoRitiro(this.model.getCFCittadino());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        Platform.runLater(()->{
        numPPR.setText(this.model.getPassaportoModelNumero());
        CFPPR.setText(this.model.getCFCittadino());
        tipoPPR.setText(this.model.getPassaportoModelTipo());
        scadenzaPPR.setText(String.valueOf(this.model.getPassaportoModelDate()));
        });

        terminaPPR.setMouseTransparent(true);
        terminaPPR.setOpacity(0.5);

        ritiroPPR.setOnAction(e -> {
            this.model.updateRitiroPP(numPPR.getText(), Integer.parseInt(c.get(1)));
            ritiroPPR.setMouseTransparent(true);
            ritiroPPR.setOpacity(0.5);
            indietroPPR.setMouseTransparent(true);
            indietroPPR.setOpacity(0.5);
            terminaPPR.setMouseTransparent(false);
            terminaPPR.setOpacity(1.0);
            startSuccessAlert("Ritiro");
        });

        terminaPPR.setOnAction(e -> {
            this.model.updateTerminePrenotazioni((c.get(1)));
            ((Node) (e.getSource())).getScene().getWindow().hide();
            try {
                aggiornaDB();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            startSuccessAlert("Termine");
            c.clear();
        });

        assegnaSlot.setOnAction(e -> {
            try {
                openSlotView(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        indietroSlot.setOnAction(e -> {
            try {
                openPortaleView(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        anteprimaSlot.setMouseTransparent(true);
        anteprimaSlot.setOpacity(0.5);
        caricaSlot.setOnAction(e -> {
            try {
                apriAnteprimaSlot();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        gestioneDipendenti.setOnAction(e -> {
            try {
                apriGestioneDipendenti(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        labelGestioneSede.setText("Sede di: " + this.model.getSede() + ", numero: " + this.model.getCodiceSede());

        indietroDipendente.setOnAction(e -> {
            try {
                openPortaleView(e);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        paneDipendentiPresenti.setOnMouseClicked(e -> {
            try {
                apriDipendentiPresenti(e);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        buttonGeneraMatricolaNuovoDipendente.setVisible(false);
        buttonGeneraPasswordNuovoDipendente.setVisible(false);
        buttonAggiungiNuovoDipendente.setVisible(false);

        choiceBoxContrattoNuovoDipendente.setVisible(false);

        fieldCognomeNuovoDipendente.setVisible(false);
        fieldNomeNuovoDipendente.setVisible(false);
        fieldMailNuovoDipendente.setVisible(false);
        fieldTelefonoAziendaleNuovoDipendente.setVisible(false);

        labelCognomeNuovoDipendente.setVisible(false);
        labelNomeNuovoDipendente.setVisible(false);
        labelMatricolaGenerataNuovoDipendente.setVisible(false);
        labelPasswordNuovoDipendente.setVisible(false);
        labelTelNuovoDipendente.setVisible(false);
        labelTipoContrattoNuovoDipendente.setVisible(false);
        mailNuovoDipendente.setVisible(false);

        ObservableList<String> contratti = FXCollections.observableArrayList("Indeterminato", "Determinato", "Part-time", "Apprendistato", "Stagista");
        choiceBoxContrattoNuovoDipendente.setItems(contratti);
    }

    private void setPassaportoRitiro(String cfCittadino) {
        this.model.getPassaportoRitiro(cfCittadino);
    }

    /*
    private void setLabelsRitiro() {
        numPPR.setText(passaportoDip.getNumero());
        CFPPR.setText(passaportoDip.getCfCittadino());
        tipoPPR.setText(passaportoDip.getTipo());
        scadenzaPPR.setText(String.valueOf(passaportoDip.getDataScadenza()));

        System.out.println("numero "+numPPR.getText());
    }*/

    private void apriGestioneDipendenti(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/ViewDipendente/gestione-dipendenti-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    // TODO
    //      bloccare se vuole inserire di nuovo gli stessi slot
    private void apriAnteprimaSlot() throws SQLException {
        anteprimaSlot.setMouseTransparent(false);
        anteprimaSlot.setOpacity(1.0);
        anteprimaSlot.setExpanded(true);

        vboxAccSlot.getChildren().clear();

        ResultSet rs = this.model.getAnteprimaSlot(datePickerSlot.getValue());

        if (rs == null) {
            startSlotAlert();
            anteprimaSlot.setMouseTransparent(true);
            anteprimaSlot.setOpacity(0.5);
            anteprimaSlot.setExpanded(false);
        } else {
            while (rs.next()) {
                String codiceSede = rs.getString("codSede");
                Label codSedeLabel = new Label("Codice sede: " + codiceSede);

                String data = rs.getString("ggmmaaaa");
                Label dateLabel = new Label("Giorno: " + data);

                String hslot = rs.getString("OraSlot");
                Label oraSlotLabel = new Label("Orario slot: " + hslot);

                String serv = rs.getString("servizio");
                Label servizioLabel = new Label("Servizio: " + serv);

                int numslot = rs.getInt("numSlot");
                Label numSlotLabel = new Label("Numero di slot: " + numslot);

                Button ins = new Button("Inserisci slot");

                ins.setOnAction(event -> {
                    boolean slotPresente = false;
                    try {
                        slotPresente = this.model.checkPresenzaSlot(codiceSede, data, hslot);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    if (slotPresente) {
                        startPresenzaSlotAlert();
                    } else {
                        this.model.inserisciAppuntamento(codiceSede, data, hslot, serv, numslot);
                    }
                    ins.setMouseTransparent(true);
                    ins.setOpacity(0.5);
                });

                HBox entryBox = new HBox();
                entryBox.getChildren().addAll(codSedeLabel, dateLabel, oraSlotLabel, servizioLabel, numSlotLabel, ins);
                entryBox.setSpacing(10);
                entryBox.setAlignment(Pos.CENTER);
                entryBox.setSnapToPixel(true);
                vboxAccSlot.getChildren().add(entryBox);
            }
        }
        scrollpaneAccSlot.setContent(vboxAccSlot);
    }

    private void startPresenzaSlotAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setHeaderText("Operazione non concessa");
        alert.setContentText("Lo slot è già stato inserito precedentemente");
        alert.showAndWait();
    }

    @FXML
    private void confermaSlotData(ActionEvent event) throws SQLException {
        ResultSet rs = null;
        dataSlot = datePickerSlot.getValue();
        LocalDate date = datePickerSlot.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE", new Locale("it", "IT"));
        gg = date.format(formatter);
        boolean check = this.model.checkPresenzaData(dataSlot);
        boolean dataValida = this.model.checkValiditaData(dataSlot, gg.toUpperCase());

        if (check == false && dataValida == true) {
            this.model.insertDataMancante(dataSlot, gg.toUpperCase());
            rs = this.model.dipendentiData(gg.toUpperCase());
            showDataDipendenti(rs);
        } else if (check == true && dataValida == true) {
            rs = this.model.dipendentiData(gg.toUpperCase());
            showDataDipendenti(rs);
        } else if (dataValida == false) {
            startConfermaAlert();
            VBox VboxSlot = new VBox();
            VboxSlot.setSpacing(10);
            VboxSlot.setPadding(new Insets(10));
            Label avviso = new Label("Non è presente nessuna disponbilità!");
            HBox entryBox = new HBox(avviso);

            entryBox.setSpacing(10);
            entryBox.setAlignment(Pos.CENTER);
            entryBox.setSnapToPixel(true);
            VboxSlot.getChildren().addAll(entryBox);
            scrollpaneSlot.setContent(VboxSlot);
        }
    }

    private void showDataDipendenti(ResultSet rs) throws SQLException {
        VBox VboxSlot = new VBox();
        VboxSlot.setSpacing(10);
        VboxSlot.setPadding(new Insets(10));

        VboxSlot.getChildren().clear();

        if (!rs.isBeforeFirst()) { // vedo se è vuoto
            Label avviso = new Label("Non è presente nessuna disponbilità!");
            HBox entryBox = new HBox(avviso);

            entryBox.setSpacing(10);
            entryBox.setAlignment(Pos.CENTER);
            entryBox.setSnapToPixel(true);
            VboxSlot.getChildren().addAll(entryBox);
        } else {
            while (rs.next()) {
                String mat = rs.getString("matricola");
                Label matricola = new Label("Matricola: " + mat + "\t");

                String nome = rs.getString("nome");
                Label name = new Label("Nome: " + nome + "\t");

                String cognome = rs.getString("cognome");
                Label surname = new Label("Cognome: " + cognome + "\t");

                Label disp = new Label("Disponibilità: \t");
                Button yes = new Button("Si");
                Button no = new Button("No");

                Button modificaScelta = new Button("Modifica");
                Button conferma = new Button("Conferma");

                modificaScelta.setMouseTransparent(true);
                modificaScelta.setOpacity(0.5);
                conferma.setMouseTransparent(true);
                conferma.setOpacity(0.5);

                HBox entryBox = new HBox();

                yes.setOnAction(e -> {
                    ndip = 0;
                    System.out.println("ndip yes " + ndip);
                    boolean ans = checkYesPressed();
                    if (ans) {
                        modificaScelta.setMouseTransparent(false);
                        modificaScelta.setOpacity(1.0);
                        conferma.setMouseTransparent(false);
                        conferma.setOpacity(1.0);
                        no.setOpacity(0.5);
                        no.setMouseTransparent(true);
                        yes.setStyle("-fx-background-color: #76A997");
                        yes.setMouseTransparent(true);
                    }
                    isYesPressed = !isYesPressed;
                    sceltaDipSlot = true;
                });

                no.setOnAction(e -> {
                    ndip = 0;
                    boolean ans = checkNoPressed();
                    if (ans) {
                        modificaScelta.setMouseTransparent(false);
                        modificaScelta.setOpacity(1.0);
                        conferma.setMouseTransparent(false);
                        conferma.setOpacity(1.0);
                        yes.setOpacity(0.5);
                        yes.setMouseTransparent(true);
                        no.setStyle("-fx-background-color: #76A997");
                        no.setMouseTransparent(true);
                    }
                    isNoPressed = !isNoPressed;
                    sceltaDipSlot = false;
                });

                modificaScelta.setOnAction(e -> {
                    ndip = 1;
                    yes.setMouseTransparent(false);
                    yes.setOpacity(1.0);
                    no.setStyle("");
                    no.setMouseTransparent(false);
                    no.setOpacity(1.0);
                    yes.setStyle("");
                });

                conferma.setOnAction(e -> {
                    if (ndip == 0) {
                        conferma.setMouseTransparent(false);
                        conferma.setOpacity(1.0);
                        yes.setMouseTransparent(true);
                        no.setMouseTransparent(true);
                        yes.setOpacity(0.5);
                        no.setOpacity(0.5);
                        modificaScelta.setMouseTransparent(true);
                        modificaScelta.setOpacity(0.5);
                        boolean dispPresenza = this.model.checkAddettoDisponibilita(mat, dataSlot);

                        if (!dispPresenza) {
                            this.model.insertDisponibilita(mat, dataSlot, sceltaDipSlot);
                        } else {
                            startPresenzaAlert();
                            modificaScelta.setMouseTransparent(true);
                            modificaScelta.setOpacity(0.5);
                            yes.setMouseTransparent(true);
                            no.setMouseTransparent(true);
                            yes.setOpacity(0.5);
                            no.setOpacity(0.5);
                            conferma.setMouseTransparent(true);
                            conferma.setOpacity(0.5);
                        }
                    } else {
                        startConfermaAlert();
                        conferma.setMouseTransparent(true);
                        conferma.setOpacity(0.5);
                    }
                    conferma.setMouseTransparent(true);
                    conferma.setOpacity(0.5);
                });

                entryBox.getChildren().addAll(matricola, name, surname, disp, yes, no, modificaScelta, conferma);
                entryBox.setSpacing(15);
                entryBox.setAlignment(Pos.CENTER);
                entryBox.setSnapToPixel(true);
                VboxSlot.getChildren().addAll(entryBox);
            }

            scrollpaneSlot.setContent(VboxSlot);
        }
        scrollpaneSlot.setContent(VboxSlot);
    }

    private void startPresenzaAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setHeaderText("Operazione non concessa");
        alert.setContentText("La presenza è già stata inserita precedentemente");
        alert.showAndWait();
    }

    public void startConfermaAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setHeaderText("Operazione non concessa");
        alert.setContentText("Non hai selezionato una opzione valida.");
        alert.showAndWait();
    }

    private boolean checkNoPressed() {
        isNoPressed = !isNoPressed;
        return isNoPressed;
    }

    private boolean checkYesPressed() {
        isYesPressed = !isYesPressed;
        return isYesPressed;
    }

    private void openPortaleView(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/ViewDipendente/portale-dipendente-view.fxml"));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void openSlotView(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/ViewDipendente/slot-view.fxml"));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void updateRitiroPassaporto(ActionEvent e) throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/ViewDipendente/ritiro-passaporto-view.fxml"));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void startFailureAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setHeaderText("Operazione non permessa!");
        alert.setContentText("Non è presente nessun passaporto da ritirare!");
        alert.showAndWait();
    }

    private void insertNuovoPP(ActionEvent e) throws SQLException {
        this.model.insertNuovoPassaporto(numeroPassaporto.getText(), labelInserimentoCF.getText(), choiceBox.getValue(), dataScadenzaPP.getText(), Integer.parseInt(c.get(1)));
        startSuccessAlert("Rilascio");
        updateTerminaPrenotazioni(e, c.get(1));
        aggiornaDB();
    }

    private void startSuccessAlert(String tipo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confermato!");
        alert.setHeaderText("Operazione completata");
        if (tipo.equals("Ritiro")) {
            alert.setContentText("Operazione completata con successo! Il passaporto è stato ritirato dall'utente.");
        } else if (tipo.equals("Rilascio")) {
            alert.setContentText("Operazione completata con successo! Il nuovo passaporto è stato inserito ed è in processo.");
        } else if (tipo.equals("Termine")) {
            alert.setContentText("Operazione terminata! Puoi prendere in carico una nuova prenotazione.");
        } else if(tipo.equals("turno di lavoro")){
            alert.setContentText("Operazione completata con successo! Il turno di lavoro è stato inserito.");
        } else if(tipo.equals("cancellazione turno")){
            alert.setContentText("Operazione completata con successo! Il turno di lavoro è stato cancellato.");
        } else if(tipo.equals("disattivazione passaporto")){
            alert.setContentText("Operazione completata con successo! Il passaporto è stato disattivato.");
        }
        alert.showAndWait();
    }

    private void updateRilascioPassaporto(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/ViewDipendente/inserimento-passaporto-view.fxml"));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void tornaGestioneDati(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/ViewDipendente/gestisci-prenotazione-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void updateRinunciaPrenotazioni(ActionEvent e, String idPrenotazione) throws SQLException {
        PrenotazioniAttive.setExpanded(false);
        scrollPanePP.setContent(null);
        vboxPP.getChildren().clear();
        datePicker.setValue(LocalDate.now());

        this.model.updateRinunciaPrenotazioni(idPrenotazione);
        ((Node) (e.getSource())).getScene().getWindow().hide();
        aggiornaDB();
    }

    private void updateTerminaPrenotazioni(ActionEvent e, String idPrenotazione) throws SQLException {
        PrenotazioniAttive.setExpanded(false);
        scrollPanePP.setContent(null);
        datePicker.setValue(LocalDate.now());
        vboxPP.getChildren().clear();

        this.model.updateTerminePrenotazioni(idPrenotazione);
        ((Node) (e.getSource())).getScene().getWindow().hide();
        aggiornaDB();
    }

    @FXML
    private void aggiornaDB() throws SQLException {
        ResultSet rs = this.model.showPrenotazioniOdierne(LocalDate.now());
        datePicker.setValue(LocalDate.now());
        showResults(rs);
    }

    // TODO
    //      Rilascio Passaporto --> operazioni fatte, da inserire Disattiva PP
    //      DA FARE TIMESTAMP X FILA
    /*
    private void showPassaportiCittadino() throws SQLException {
        ResultSet rs = this.model.getPassaportiCittadino();

        if (!rs.isBeforeFirst()) { // vedo se è vuoto
            vboxPP.getChildren().clear();
            Label avviso = new Label("Non hai nessun passaporto per questa ricerca");
            Line line = new Line(10, 10, 880, 10);

            HBox entryBox = new HBox(avviso);

            entryBox.setSpacing(10);
            entryBox.setAlignment(Pos.CENTER);
            entryBox.setSnapToPixel(true);
            vboxPP.getChildren().addAll(entryBox, line);
        } else {
            while (rs.next()) {
                vboxPP.getChildren().clear();
                HBox entryBox = new HBox();
                Line line = new Line(10, 10, 890, 10);
                Label numero = new Label("Numero: " + rs.getString("numero"));
                Label tipo = new Label("Tipo: " + rs.getString("tipo"));
                Label scadenza = new Label("Scadenza: " + rs.getString("dataScadenza"));
                Label stato = new Label("Stato: " + rs.getString("stato"));
                Label idRL = new Label("ID rilascio: " + rs.getInt("IDrilascio"));
                Label idRT = new Label("ID ritiro: " + rs.getInt("IDritiro"));

                entryBox.getChildren().addAll(numero, tipo, scadenza, stato, idRL, idRT);

                entryBox.setSpacing(15);
                entryBox.setAlignment(Pos.CENTER);
                entryBox.setSnapToPixel(true);
                vboxPP.getChildren().addAll(entryBox, line);
            }
            scrollPanePP.setContent(vboxPP);
        }
    }

     */

    private void showPassaportiCittadino() throws SQLException {
        List<Passaporto> passports = this.model.getPassaportiCittadino();

        if (passports.isEmpty()) { // vedo se è vuoto
            vboxPP.getChildren().clear();
            Label avviso = new Label("Non hai nessun passaporto per questa ricerca");
            Line line = new Line(10, 10, 880, 10);

            HBox entryBox = new HBox(avviso);

            entryBox.setSpacing(10);
            entryBox.setAlignment(Pos.CENTER);
            entryBox.setSnapToPixel(true);
            vboxPP.getChildren().addAll(entryBox, line);
        } else {
            int j = passports.size();
            for(int i = 0; i<j; i++) {
                vboxPP.getChildren().clear();
                for (Passaporto passport : passports) {
                    HBox entryBox = new HBox();
                    Line line = new Line(10, 10, 890, 10);


                    Label numero = new Label("Numero: " + passport.getNumero());
                    Label tipo = new Label("Tipo: " + passport.getTipo());
                    Label scadenza = new Label("Scadenza: " + passport.getDataScadenza());
                    Label stato = new Label("Stato: " + passport.getStato());

                    Button disattiva = new Button("Disattiva Passaporto");
                    disattiva.setVisible(false);
                    disattiva.setMouseTransparent(true);
                    if(passport.getStato().equals("ATTIVO")){
                        disattiva.setVisible(true);
                        disattiva.setMouseTransparent(false);
                    }

                    entryBox.getChildren().addAll(numero, tipo, scadenza, stato, disattiva);

                    entryBox.setSpacing(15);
                    entryBox.setAlignment(Pos.CENTER);
                    entryBox.setSnapToPixel(true);



                    disattiva.setOnAction(e->{
                        try {
                            int key = this.model.disattivaPP(passport.getNumero());
                            if(key==0){
                                startDisattivaAlert();
                            } else {
                                startSuccessAlert("disattivazione passaporto");
                            }
                            showPassaportiCittadino();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    });

                    vboxPP.getChildren().addAll(entryBox, line);
                }
            }
            scrollPanePP.setContent(vboxPP);
        }
    }

    private void startDisattivaAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setHeaderText("Operazione non permessa!");
        alert.setContentText("Non è stato possibile disattivare il passaporto.");
        alert.showAndWait();
    }

    @FXML
    void nuovoDipendente(ActionEvent event) {
        buttonGeneraMatricolaNuovoDipendente.setVisible(true);
        buttonGeneraPasswordNuovoDipendente.setVisible(true);
        buttonAggiungiNuovoDipendente.setVisible(true);

        choiceBoxContrattoNuovoDipendente.setVisible(true);

        fieldCognomeNuovoDipendente.setVisible(true);
        fieldNomeNuovoDipendente.setVisible(true);
        fieldMailNuovoDipendente.setVisible(true);
        fieldTelefonoAziendaleNuovoDipendente.setVisible(true);

        labelCognomeNuovoDipendente.setVisible(true);
        labelNomeNuovoDipendente.setVisible(true);
        labelMatricolaGenerataNuovoDipendente.setVisible(true);
        labelPasswordNuovoDipendente.setVisible(true);
        labelTelNuovoDipendente.setVisible(true);
        labelTipoContrattoNuovoDipendente.setVisible(true);
        mailNuovoDipendente.setVisible(true);

        buttonGeneraPasswordNuovoDipendente.setOnAction(e -> {
            labelPasswordNuovoDipendente.setText(this.model.generaPasswordDipendente());
        });

        buttonGeneraMatricolaNuovoDipendente.setOnAction(e -> {
            try {
                labelMatricolaGenerataNuovoDipendente.setText(this.model.generaMatricolaDipendente());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        buttonAggiungiNuovoDipendente.setOnAction(e -> {

            if (fieldTelefonoAziendaleNuovoDipendente.getText().equals("") || fieldNomeNuovoDipendente.getText().equals("") || fieldCognomeNuovoDipendente.getText().equals("") || fieldMailNuovoDipendente.getText().equals("") ||
                    labelMatricolaGenerataNuovoDipendente.getText().equals("") || labelPasswordNuovoDipendente.getText().equals("") || choiceBoxContrattoNuovoDipendente.getValue() == null) {
                startDipendenteAlert();
            } else {
                boolean checkerMail;
                try {
                    checkerMail = this.model.checkMailDipendente(fieldMailNuovoDipendente.getText());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                boolean telephoneChecker;
                try {
                    telephoneChecker = this.model.checkNumeroTelefono(fieldTelefonoAziendaleNuovoDipendente.getText());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                String pwdGenerata;
                try {
                    pwdGenerata = this.model.setEncryption(labelPasswordNuovoDipendente.getText());
                } catch (NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                }

                if (checkerMail && telephoneChecker) {
                    this.model.insertNuovoDipendente(labelMatricolaGenerataNuovoDipendente.getText(), fieldNomeNuovoDipendente.getText(), fieldCognomeNuovoDipendente.getText(), choiceBoxContrattoNuovoDipendente.getValue(),
                            fieldTelefonoAziendaleNuovoDipendente.getText(), fieldMailNuovoDipendente.getText(), pwdGenerata);
                    startSuccessAlert("inserimento nuovo dipendente, " + labelNomeNuovoDipendente.getText() + " " + labelCognomeNuovoDipendente.getText() + " " + labelMatricolaGenerataNuovoDipendente.getText());
                    buttonGeneraMatricolaNuovoDipendente.setVisible(false);
                    buttonGeneraPasswordNuovoDipendente.setVisible(false);
                    buttonAggiungiNuovoDipendente.setVisible(false);

                    choiceBoxContrattoNuovoDipendente.setVisible(false);

                    fieldCognomeNuovoDipendente.setVisible(false);
                    fieldNomeNuovoDipendente.setVisible(false);
                    fieldMailNuovoDipendente.setVisible(false);
                    fieldTelefonoAziendaleNuovoDipendente.setVisible(false);

                    labelCognomeNuovoDipendente.setVisible(false);
                    labelNomeNuovoDipendente.setVisible(false);
                    labelMatricolaGenerataNuovoDipendente.setVisible(false);
                    labelPasswordNuovoDipendente.setVisible(false);
                    labelTelNuovoDipendente.setVisible(false);
                    labelTipoContrattoNuovoDipendente.setVisible(false);
                    mailNuovoDipendente.setVisible(false);
                } else {
                    startDipendenteAlert();
                }
            }
        });
    }

    /*
        void apriDipendentiPresenti(MouseEvent event) throws SQLException {
            ResultSet rs = this.model.getDipendentiSede();
            vboxPaneDipendenti.getChildren().clear();

            while(rs.next()){
                HBox entryBox = new HBox();

                String matricola = rs.getString("matricola");
                String nomeD = rs.getString("nome");
                String cognomeD = rs.getString("cognome");
                String contrattoD = rs.getString("contratto");
                String sedeD = rs.getString("sedeLavoro");
                String tel = rs.getString("telUfficio");
                String mail = rs.getString("email");

                Label mat = new Label("Matricola: "+matricola);
                Label nome = new Label("Nome: "+nomeD);
                Label cognome = new Label("Cognome: "+cognomeD);
                Label contratto = new Label("Tipologia contratto: "+contrattoD);
                Label sede = new Label("Sede numero: "+sedeD);
                Label telefono = new Label("Numero aziendale: "+tel);
                Label email = new Label("E-mail aziendale: "+mail);

                mat.setWrapText(true);
                mat.setMaxWidth(150);
                nome.setWrapText(true);
                nome.setMaxWidth(150);
                cognome.setWrapText(true);
                cognome.setMaxWidth(150);
                contratto.setWrapText(true);
                contratto.setMaxWidth(150);
                sede.setWrapText(true);
                sede.setMaxWidth(150);
                telefono.setWrapText(true);
                telefono.setMaxWidth(150);
                email.setWrapText(true);
                email.setMaxWidth(150);
                Line line = new Line(10, 10, 700, 10);

                ChoiceBox<String> Turno = new ChoiceBox<>();
                ObservableList<String> turno = FXCollections.observableArrayList("Mattino 9-14", "Pomeriggio 15-17", "Mattino e Pomeriggio");
                Turno.setItems(turno);
                DatePicker giornolavoro = new DatePicker();
                Button confermaTurno = new Button("Inserisci Turno");

                entryBox.getChildren().addAll(mat, nome, cognome, contratto, sede, telefono, email, Turno, giornolavoro, confermaTurno);

                entryBox.setSpacing(20);
                entryBox.setAlignment(Pos.BASELINE_LEFT);
                entryBox.setSnapToPixel(true);
                vboxPaneDipendenti.getChildren().addAll(entryBox, line); // Updated variable name

            }
            scrollPaneGestioneDipendenti.setContent(vboxPaneDipendenti);
        }
    */
    void apriDipendentiPresenti(MouseEvent event) throws SQLException {
        ResultSet rs = this.model.getDipendentiSede();
        vboxPaneDipendenti.getChildren().clear();

        while(rs.next()){
            HBox entryBox = new HBox();

            String matricola = rs.getString("matricola");
            String nomeD = rs.getString("nome");
            String cognomeD = rs.getString("cognome");
            String contrattoD = rs.getString("contratto");
            String sedeD = rs.getString("sedeLavoro");
            String tel = rs.getString("telUfficio");
            String mail = rs.getString("email");

            Label mat = new Label("Matricola: "+matricola);
            Label nome = new Label("Nome: "+nomeD);
            Label cognome = new Label("Cognome: "+cognomeD);
            Label contratto = new Label("Tipologia contratto: "+contrattoD);
            Label sede = new Label("Sede numero: "+sedeD);
            Label telefono = new Label("Numero aziendale: "+tel);
            Label email = new Label("E-mail aziendale: "+mail);

            mat.setWrapText(true);
            mat.setMaxWidth(150);
            nome.setWrapText(true);
            nome.setMaxWidth(150);
            cognome.setWrapText(true);
            cognome.setMaxWidth(150);
            contratto.setWrapText(true);
            contratto.setMaxWidth(150);
            sede.setWrapText(true);
            sede.setMaxWidth(150);
            telefono.setWrapText(true);
            telefono.setMaxWidth(150);
            email.setWrapText(true);
            email.setMaxWidth(400);

            HBox personalInfoBox = new HBox(mat, nome, cognome, contratto, sede, telefono, email);
            personalInfoBox.setSpacing(20);

            ChoiceBox<String> Turno = new ChoiceBox<>();
            ObservableList<String> turno = FXCollections.observableArrayList("9:00 - 12:00", "14:00 - 17:00");
            Turno.setItems(turno);
            ChoiceBox<String> Giorno = new ChoiceBox<>();
            ObservableList<String> giorno = FXCollections.observableArrayList("LUN", "MAR", "MER", "GIO", "VEN");
            Giorno.setItems(giorno);

            Button confermaTurno = new Button("Inserisci Turno");
            Label qTurno = new Label("Scegli il turno lavorativo: ");
            Label qGiorno = new Label("Scegli il giorno lavorativo: ");

            HBox turnoBox = new HBox(qGiorno, Giorno, qTurno, Turno, confermaTurno);
            turnoBox.setSpacing(20);

            VBox entryContainer = new VBox(personalInfoBox, turnoBox);
            entryContainer.setSpacing(10);
            entryContainer.setAlignment(Pos.BASELINE_LEFT);
            confermaTurno.setOnAction(e->{
                if(Turno.getValue().equals("") || Giorno.getValue().equals("")){
                    startDipendenteAlert();
                }
                boolean controlloPresenza;
                try {
                    controlloPresenza = this.model.checkPresenzaTurno(matricola, Turno.getValue(), Giorno.getValue());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                if(controlloPresenza){
                    String opzione = "Cancella turno";
                    int scegliAzione = mostraOpzioniAzione("Questo giorno presenta già questo turno. Come desideri proseguire?", opzione);
                    if(scegliAzione==0){
                        startSuccessAlert("cancellazione turno");
                        this.model.deleteTurnoDipendente(matricola, Turno.getValue(), Giorno.getValue());
                    }
                } else {
                    try {
                        this.model.insertTurnoDipendente(matricola, Turno.getValue(), Giorno.getValue());
                        startSuccessAlert("turno di lavoro");
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            });
            Line line = new Line(10, 10, 950, 10);

            vboxPaneDipendenti.getChildren().addAll(entryContainer, line);
        }
        scrollPaneGestioneDipendenti.setContent(vboxPaneDipendenti);
    }

    private int mostraOpzioniAzione(String s, String opzione) {
            // Creo la finestra di dialogo
        ChoiceDialog<String> dialog = new ChoiceDialog<>(opzione);
        dialog.setTitle("Scegli una azione");
        dialog.setHeaderText(s);
        dialog.setContentText("Opzioni:");

        // Show the dialog and wait for the user's choice
        Optional<String> result = dialog.showAndWait();

        // Return the index of the selected option
        if (result.isPresent()) {
            return 0;
        }

        return -1;
    }

    private void startAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setHeaderText("Operazione non concessa");
        alert.setContentText("Il campo ID non è corretto e/o vuoto. Inserisci un ID valido.");
        alert.showAndWait();
        cercaPrenotazioneField.setText("");
    }

    private void startAlertTurno(String giorno, String Turno) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setHeaderText("Operazione non concessa");
        alert.setContentText("Il turno del "+giorno+", "+Turno+" è già presente.");
        alert.showAndWait();
        cercaPrenotazioneField.setText("");
    }

    private void startSlotAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setHeaderText("Operazione non concessa");
        alert.setContentText("Devi prima scegliere una data e confermare la disponibilità degli addetti.");
        alert.showAndWait();
        cercaPrenotazioneField.setText("");
    }

    private void startDipendenteAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setHeaderText("Operazione non concessa");
        alert.setContentText("Non tutti i campi necessari sono stati riempiti e/o contengono valori non concessi.");
        alert.showAndWait();
    }

    // lo uso per far vedere cos'ho nei filtri --> portale dipendente view, è l'accordion
    private void showResults(ResultSet rs) throws SQLException {
        VBox vboxpattive = new VBox();
        vboxpattive.setSpacing(10);
        vboxpattive.setPadding(new Insets(10));
        if (!rs.isBeforeFirst()) { // vedo se è vuoto
            Label avviso = new Label("Non hai nessuna prenotazione per questa ricerca");
            Line line = new Line(10, 10, 930, 10);

            HBox entryBox = new HBox(avviso);

            entryBox.setSpacing(10);
            entryBox.setAlignment(Pos.CENTER);
            entryBox.setSnapToPixel(true);
            vboxpattive.getChildren().addAll(entryBox, line);
        } else {
            while (rs.next()) {
                c.clear();
                int id = rs.getInt("ID");
                Label idLabel = new Label("ID Prenotazione: " + id);

                String cfCittadino = rs.getString("CFCittadino");
                Label CFcittadino = new Label("Codice Fiscale: " + cfCittadino);

                String ora = rs.getString("ora");
                Label Ora = new Label("Ora della prenotazione: " + ora);

                String serv = rs.getString("servizio");
                Label Serv = new Label("Servizio richiesto: " + serv);
                String data = rs.getString("giorno");

                Button gestisciButton = new Button("Prendi in carico");
                Button dettagliP = new Button("Dettagli prenotazione");
                String causa = rs.getString("causaRilascio");

                String csede = rs.getString("città");
                Label sede = new Label("presso " + csede);

                HBox entryBox = new HBox();

                Line line = new Line(10, 10, 930, 10);
                if (rs.getString("stato").equals("Confermata")) {
                    entryBox.getChildren().addAll(idLabel, CFcittadino, Ora, Serv, sede, gestisciButton);
                } else {
                    entryBox.getChildren().addAll(idLabel, CFcittadino, Ora, Serv, sede, dettagliP);
                }
                gestisciButton.setOnAction((e) -> {
                    try {
                        this.model.updateSchedaPrenotazione(String.valueOf(id));
                        this.model.getInfoCittadino(cfCittadino);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("/com/progetto/packView/ViewDipendente/gestisci-prenotazione-view.fxml"));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    Stage secondaryStage = new Stage();
                    Scene scene2 = new Scene(root);
                    secondaryStage.setScene(scene2);
                    secondaryStage.setResizable(false);
                    secondaryStage.show();
                });
                c.add(serv); //servizio c.get(0)
                c.add(String.valueOf(id));  // id prenotazione c.get(1)
                c.add(causa); // causale c.get(2)
                c.add(data); // data c.get(3)
                c.add(ora); // ora c.get(4)
                // ho il cfCittadino
                entryBox.setSpacing(15);
                entryBox.setAlignment(Pos.CENTER);
                entryBox.setSnapToPixel(true);
                vboxpattive.getChildren().addAll(entryBox, line);

                dettagliP.setOnAction(e -> {
                    try {
                        popUpDettagli(serv, id, cfCittadino);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    dettagliP.setStyle("-fx-background-color: #76A997");
                });
            }
            ScrollPaneAttive.setContent(vboxpattive);
        }
        ScrollPaneAttive.setContent(vboxpattive);
    }

    private void popUpDettagli(String serv, int id, String cfCittadino) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dettagli");
        alert.setHeaderText("Dettagli della prenotazione");
        ResultSet rs = this.model.getDettagliPrenotazione(serv, id);
        String ora = new String();
        String matricola = new String();
        String nomeC = new String();
        String cognomeC = new String();
        String nomeA = new String();
        String cognomeA = new String();
        String nPassaporto = new String();
        String citta = new String();
        String statoPrenotazione = new String();

        while (rs.next()) {
            ora = rs.getString("dataOraPrenotazione");
            matricola = rs.getString("matricolaAddetto ");
            nomeC = rs.getString("cittadino_nome");
            cognomeC = rs.getString("cittadino_cognome");
            nomeA = rs.getString("addetto_nome");
            cognomeA = rs.getString("addetto_cognome");
            citta = rs.getString("città");
            nPassaporto = rs.getString("numero");
            statoPrenotazione = rs.getString("stato");
        }
        if(statoPrenotazione.equals("Annullata")){
            nomeA = "-";
            cognomeA = "-";
            matricola = "-";
        }
        alert.setContentText("ID prenotazione: " + id +"\nStato: " +statoPrenotazione+"\nSede: " + citta + "\nPrenotazione confermata dall'utente il: " + ora
                + "\nServizio: " + serv + "\n" +
                "Assegnato all'addetto: " + matricola + "" + ", " + nomeA + " " + cognomeA + "\n\n" +
                "Nome cittadino: " + nomeC + " " + cognomeC + "\nCodice fiscale associato: " + cfCittadino +
                "\nNumero del passaporto: " + nPassaporto);
        alert.showAndWait();
    }

    private void setLabels(String serv, String id, String causa, String data, String ora) {

        labelDettPrenotazione.setVisible(true);
        labelDettCittadino.setVisible(true);

        if (serv.equals("Ritiro")) {
            labelDettPrenotazione.setText("Prenotazione con ID " + id + " per: " + serv + " del giorno " + data + " alle ore " + ora + ".");
        } else {
            labelDettPrenotazione.setText("Prenotazione con ID " + id + " per: " + serv + " con causale " + causa + " del giorno " + data + " alle ore " + ora + ".");
        }

        if (this.model.getCategoriaCittadino().equals("Cittadino minorenne")) {
            labelDettCittadino.setText(this.model.getNomeCittadino() + " " + this.model.getCognomeCittadino() + " " + this.model.getCFCittadino() + ". Nato a " + this.model.getLuogoNCittadino() + ", " + this.model.getStatoNCittadino() + " il " + this.model.getDataNCittadino() + "\n\tmail: " + this.model.getMailCittadino() +
                    ", codice tessera sanitaria: " + this.model.getTessSanitariaCittadino() + " e con tutori:" + this.model.getTutore1Cittadino() + " " + this.model.getTutore2Cittadino());
        } else {
            labelDettCittadino.setText(this.model.getNomeCittadino() + " " + this.model.getCognomeCittadino() + " " + this.model.getCFCittadino() + ". Nato a " + this.model.getLuogoNCittadino() + ", " + this.model.getStatoNCittadino() + " il " + this.model.getDataNCittadino() + "\n\tmail: " + this.model.getMailCittadino() +
                    ", codice tessera sanitaria: " + this.model.getTessSanitariaCittadino());
        }
        this.model.setCFCittadino(this.model.getCFCittadino());
    }

    //---- controller slot ----


}
