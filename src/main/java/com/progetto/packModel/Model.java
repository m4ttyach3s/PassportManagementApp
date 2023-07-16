package com.progetto.packModel;

import com.progetto.EncryptionPass;
import com.progetto.PDFGenerator;
import com.progetto.RandomPasswordGenerator;
import com.progetto.packController.ConnectDB;

import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Model {
    private static Model model;
    private static String id;
    private static String pwd;
    private static String idPrenotazione;
    private static String Nome;
    private static String Cognome;
    private static String Sede;
    private static String Ruolo;
    private static String codiceSede;
    private static String CFCittadino;
    private static String nomeCittadino;
    private static String cognomeCittadino;
    private static String luogoNCittadino;
    private static String dataNCittadino;
    private static String statoNCittadino;
    private static String tutore1Cittadino;
    private static String tutore2Cittadino;
    private static String mailCittadino;
    private static String categoriaCittadino;
    private static String tessSanitariaCittadino;
    private static String numero_passaporto;
    private static String tipo_passaporto;
    private Addetto addettoModel = new Addetto(null, null, false);
    private Cittadino cittadinoModel = new Cittadino(null, null, null);
    private Passaporto passaportoModel = new Passaporto(null, null, null, null, null);
    private ConnectDB connectDB;
    private Connection connection;
    private final Pattern textPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[0-9]).+$");
    private final Pattern numeroPattern = Pattern.compile("^(?=.*[A-Z])(?=.*[0-9]).+$");
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static Collection<String> c = new ArrayList<>();
    private static boolean statusAnagrafica;
    private static boolean tabellaCittadino;

    // costruttore
    private Model(){this.connectDB = new ConnectDB();}

    public static Model getInstance() {
        if (model == null) {model = new Model();}
        return model;
    }
    // --- sezione getters
    public static String getCFCittadino() {
        return CFCittadino;
    }
    public static String getNomeCittadino() {
        return nomeCittadino;
    }
    public static String getCognomeCittadino() {
        return cognomeCittadino;
    }
    public static String getLuogoNCittadino() {
        return luogoNCittadino;
    }
    public static String getDataNCittadino() {
        return dataNCittadino;
    }
    public static String getStatoNCittadino() {
        return statoNCittadino;
    }
    public static String getMailCittadino() {return mailCittadino;}
    public static String getCategoriaCittadino() {
        return categoriaCittadino;
    }
    public static String getTessSanitariaCittadino() {
        return tessSanitariaCittadino;
    }
    public String getId() {
        return id;
    }
    public String getNome() {
        return Nome;
    }
    public String getCognome() {
        return Cognome;
    }
    public String getSede() {
        return Sede;
    }
    public String getRuolo() {
        return Ruolo;
    }
    public static String getCodiceSede() {
        return codiceSede;
    }
    public static boolean isStatusAnagrafica() {
        return statusAnagrafica;
    }
    public boolean isTabellaCittadino() {
        return tabellaCittadino;
    }
    // ------------- Fine Getters ------------


    // ----------- Sezione Setters -----------
    public static void setModel(Model model) {
        Model.model = model;
    }

    // ------------- Fine Setters ------------


    // ----------- Sezione Metodi -----------

    /**
     * Stabilisce la connessione tra il database, model e controller ed esegue le query ritenute safe da SQL injection.
     * @param query
     * @return ResultSet
     * @throws SQLException
     */
    public ResultSet setConnection(String query) throws SQLException {
        this.connection = connectDB.getConnection();
        ResultSet rs = null;

        try {
            Statement statement = connection.createStatement();
            rs = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    /**
     * Metodo per CITTADINO e DIPENDENTE per controllare l'integrità tra username e password con i dati presenti nel DB.
     * Discrimina tra la classe Cittadino e la classe Dipendente.
     * @param ID
     * @param password
     * @param table
     * @return true, if present. false, not present.
     * @throws NoSuchAlgorithmException
     */
    public boolean checkLogin(String ID, String password, String table) throws NoSuchAlgorithmException {
        EncryptionPass encryptedPassword = new EncryptionPass();
        String enpassword = encryptedPassword.setEncrypt(password);
        ResultSet rs;
        boolean result = false;
        String queryID = new String();

        if (table.equals("cittadino")) {
            queryID = "CF";
        } else if (table.equals("addetto")) {
            queryID = "matricola";
        }

        String query = "SELECT " + "\"" + queryID + "\"" + ", \"password\", nome, cognome FROM public.\"" + table + "\" WHERE " + "\"" + queryID + "\"='" + ID
                + "' " + "AND \"password\" = '" + enpassword + "'";
        try {
            rs = setConnection(query);
            while (rs.next()) {
                id = rs.getString(queryID);
                pwd = rs.getString("password");
                Nome = rs.getString(3);
                Cognome = rs.getString(4);
                if (ID.equals(id) && enpassword.equals(pwd)) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(table.equals("cittadino")){
            cittadinoModel.setCodiceFiscale(id);
            id = cittadinoModel.getCodiceFiscale();
            cittadinoModel.setNome(Nome);
            cittadinoModel.setCognome(Cognome);
        }

        if(table.equals("addetto")){
            addettoModel.setMatricola(id);
            addettoModel.setResponsabile(dipendenteCheck(addettoModel.getMatricola()));
            addettoModel.setCodiceSede(getAddettoCodiceSede(id));
        }


        connectDB.closeConnection(this.connection);
        return result;
    }

    /**
     * Metodo per DIPENDENTE per ottenere il codice sede assegnato all'addetto.
     * @param id
     * @return string con il codice della sede.
     */
    private String getAddettoCodiceSede(String id) {
        String querySede = "SELECT \"sedeLavoro\" FROM public.addetto, public.sede WHERE \"sedeLavoro\" = codice AND matricola = '" + id + "'";
        ResultSet rs;
        String codice = new String();
        try {
            rs = setConnection(querySede);
            while (rs.next()) {
                codice = rs.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return codice;
    }

    /**
     * Metodo per CITTADINO per ottenere le sue prenotazioni attive.
     * @return ResultSet con le prenotazioni.
     */
    public ResultSet getPrenotazioniAttive() throws SQLException {
        String CF = cittadinoModel.getCodiceFiscale();
        this.connection = connectDB.getConnection();
        String query = "SELECT \"ID\", città, giorno, ora, servizio, \"causaRilascio\", \"codSede\" " +
                "FROM public.prenotazione, public.sede WHERE \"codSede\" = codice" +
                " AND \"CFcittadino\" = '" + CF + "' AND stato = 'Confermata'";
        ResultSet rs = null;
        try {
            rs = setConnection(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
        return rs;
    }

    /**
     * Metodo per CITTADINO per ottenere le sue prenotazioni passate.
     * @return ResultSet con le prenotazioni
     */
    public ResultSet getPrenotazioniPassate() throws SQLException {
        this.connection = connectDB.getConnection();
        String CF = cittadinoModel.getCodiceFiscale();
        String query = "SELECT \"ID\", città, giorno, ora, servizio,  \"causaRilascio\"," +
                " \"matricolaAddetto \", stato FROM public.prenotazione, public.sede" +
                " WHERE \"codSede\" = codice AND \"CFcittadino\" = '" + CF + "' AND (stato = 'Terminata' OR stato = 'Annullata') " +
                "ORDER BY giorno, ora ASC";
        ResultSet rs = null;
        try {
            rs = setConnection(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
        return rs;
    }

    /**
     * Metodo per CITTADINO per annullare una prenotazione.
     * @param id
     * @param giorno
     * @param ora
     * @param codSede
     * @return true, if deleted. false, if not.
     * @throws SQLException
     */
    public boolean annullaPrenotazioneAttive(String id, Date giorno, Time ora, String codSede) throws SQLException {
        String query = "BEGIN;\n" +
                "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;\n" +
                "UPDATE public.prenotazione\n" +
                "\tSET stato = 'Annullata'\n" +
                "\tWHERE \"ID\" = ?;\n" +
                "UPDATE public.appuntamento\n" +
                "\tSET \"numeroPosti\" = \"numeroPosti\" + 1\n" +
                "\tWHERE \"codSede\" = ?::character(5) AND giorno = ? AND ora = ?;\n" +
                "COMMIT;";
        connection = connectDB.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, Integer.parseInt(id));
            statement.setString(2, codSede);
            statement.setDate(3, giorno);
            statement.setTime(4, ora);
            int rows = statement.executeUpdate();
            return rows == 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Metodo per CITTADINO per la generazione del pdf della prenotazione.
     * @param lista
     * @param servizio
     * @param causaR
     */
    public void makePDF(List<String> lista, String servizio, String causaR) {
        String name = new String("PDF_Passaporto_" + getId() + "_" + lista.get(0) + causaR + ".pdf");
        PDFGenerator pdfGenerator = new PDFGenerator(name);
        pdfGenerator.makePDF(lista, servizio, causaR);
    }

    /**
     * Metodo per DIPENDENTE per controllare il suo ruolo.
     * @param matricolaDipendente
     * @return true, if admin. false, if employee.
     */
    public boolean dipendenteCheck(String matricolaDipendente) {
        String query = "SELECT COUNT(*)\n" +
                "FROM public.sede\n" +
                "WHERE \"matricolaResponsabile\" = '" + matricolaDipendente + "'";

        ResultSet rs;

        try {
            rs = setConnection(query);
            while (rs.next()) {
                if (rs.getInt(1) == 1) {
                    Ruolo = "Amministratore";
                    return true;
                } else {
                    Ruolo = "Addetto";
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Metodo per DIPENDENTE per ottenere la città di qualsiasi dipendente desiderato.
     */
    public void getDipSede() {
        String querySede = "SELECT città, \"sedeLavoro\" FROM public.addetto, public.sede WHERE \"sedeLavoro\" = codice AND matricola = '" + id + "'";
        ResultSet rs;
        try {
            rs = setConnection(querySede);
            while (rs.next()) {
                Sede = rs.getString(1);
                codiceSede = rs.getString(2);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo condiviso da REGISTRAZIONE e CITTADINO per il controllo di integrità della password inserita e/o cambio password.
     * @param pwd
     * @return true, if it matches with regex. false, if not.
     */
    public boolean checkPassword(String pwd) {
        return textPattern.matcher(pwd).matches() && pwd.length() >= 10;
    }

    /**
     * Metodo per REGISTRAZIONE per il controllo di integrità dei dati inseriti tramite una Collection<String>.
     * Controlla se la persona è presente in public.anagrafica oppure in public.cittadino. Se è presente in public.anagrafica
     * permette l'inserimento in public.cittadino. Se è presente in public.anagrafica & public.cittadino, mostra pop-up di alert.
     * Se non è presente in public.anagrafica, mostra pop-up di alert di diverso tipo.
     * @param nome
     * @param cognome
     * @param data
     * @param citta
     * @param stato
     * @param cf
     * @param tesssanitaria
     * @param mail
     * @param pwd
     * @param categoria
     * @return Collection<String> con i valori non corretti.
     * @throws SQLException
     */
    public Collection<String> checkRegistrazione(String nome, String cognome, LocalDate data, String citta, String stato, String cf, String tesssanitaria, String mail, String pwd, String categoria) throws SQLException {
        if (nome.equals("")) {
            c.add("Nome");
        }
        if (cognome.equals("")) {
            c.add("Cognome");
        }
        if (citta.equals("")) {
            c.add("Città");
        }
        if (stato.equals("")) {
            c.add("Stato");
        }
        if (cf.equals("") || !checkCF(cf)) {
            c.add("Codice Fiscale");
        }
        if (mail.equals("") || !checkMail(mail)) {
            c.add("Mail");
        }
        if (categoria.equals("null")) {
            c.add("Categoria");
        }
        if (tesssanitaria.equals("") || tesssanitaria.length() > 5) {
            c.add("Tessera Sanitaria");
        }
        if (data == null) {
            data = LocalDate.now();
            c.add("Data");
        } else if (!checkDataValida(data)) {
            c.add("Data");
        }
        if (!checkPassword(pwd)) {
            c.add("Password");
        }

        statusAnagrafica = checkSchedaAnagrafica(cf, cognome, nome, data, citta, stato);
        tabellaCittadino = checkSchedaCittadino(cf, cognome, nome, data, citta, stato);

        return c;
    }

    /**
     * Metodo per REGISTRAZIONE utilizzato da checkRegistrazione per il controllo di integrità dei dati nella tabella public.cittadino.
     * @param cf
     * @param cognome
     * @param nome
     * @param data
     * @param citta
     * @param stato
     * @return true, if present. false, if not.
     */
    private boolean checkSchedaCittadino(String cf, String cognome, String nome, LocalDate data, String citta, String stato) {
        String query = "SELECT COUNT(\"CF\")\n" +
                "\tFROM public.cittadino\n" +
                "\tWHERE \"CF\" = '" + cf + "' AND cognome = '" + cognome + "' AND nome = '" + nome + "' AND \"dataN\" = '" + data + "' AND \"luogoN\" = '" + citta + "' AND \"statoN\"='" + stato + "'";
        ResultSet rs;
        try {
            rs = setConnection(query);
            while (rs.next()) {
                if (rs.getInt(1) == 1) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Metodo per REGISTRAZIONE per controllare che il codice fiscale sia composto nel modo corretto.
     * @param cf
     * @return true, if matches. false, if not.
     */
    private boolean checkCF(String cf) {
        String pattern = "\\w{6}\\d{2}\\w\\d{2}\\w\\d{3}\\w"; // uso per comparare con il mio input
        Pattern regex = Pattern.compile(pattern); // registro
        Matcher checker = regex.matcher(cf);

        if (checker.matches() && cf.length() == 16) {
            return true;
        }

        return false;
    }

    /**
     * Metodo per REGISTRAZIONE per il controllo di integrità della data per il metodo checkRegistrazione. Controlla
     * se il cittadino è minorenne o maggiorenne.
     * @param data
     * @return true, if user is of age. false, if not.
     */
    private boolean checkDataValida(LocalDate data) {
        Period etaCalcolata = Period.between(data, LocalDate.now());
        if (etaCalcolata.getYears() >= 18 && etaCalcolata.getYears() <= 110) {
            return true;
        }
        return false;
    }

    /**
     * Metodo condiviso da REGISTRAZIONE e CITTADINO per il controllo della mail. La stringa deve contenere solo determinati
     * caratteri specificati in un registro e deve mantenere una struttura specificata in un pattern.
     * @param s
     * @return true, if it matches. false, if not.
     */
    public boolean checkMail(String s) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    /**
     * Metodo per REGISTRAZIONE per controllare la presenza dell'utente in public.anagrafica
     * @param CF
     * @param cognome
     * @param nome
     * @param dataN
     * @param luogoN
     * @param stato
     * @return true, if present. false, if not.
     */
    private boolean checkSchedaAnagrafica(String CF, String cognome, String nome, LocalDate dataN, String luogoN, String stato) {

        if (dataN == null) {
            dataN = LocalDate.now();
        }
        String query = "SELECT COUNT(\"CF\")\n" +
                "\tFROM public.anagrafica\n" +
                "\tWHERE \"CF\" = '" + CF + "' AND cognome = '" + cognome + "' AND nome = '" + nome + "' AND \"dataN\" = '" + dataN + "' AND \"luogoN\" = '" + luogoN + "' AND \"statoN\"='" + stato + "'";
        ResultSet rs;
        try {
            rs = setConnection(query);
            while (rs.next()) {
                if (rs.getInt(1) == 1) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Metodo per REGISTRAZIONE per l'inserimento dei dati dell'utente nella tabella public.cittadino.
     * @param nome
     * @param cognome
     * @param data
     * @param citta
     * @param tesssanitaria
     * @param stato
     * @param cf
     * @param mail
     * @param pwd
     * @param categoria
     * @throws NoSuchAlgorithmException
     */
    public void insertDatiCittadino(String nome, String cognome, LocalDate data, String citta, String tesssanitaria, String stato, String cf, String mail, String pwd, String categoria) throws NoSuchAlgorithmException {
        EncryptionPass encryptedPassword = new EncryptionPass();
        String password = encryptedPassword.setEncrypt(pwd);
        String query = "INSERT INTO public.cittadino(\"CF\", \"tessSanitaria\", cognome, nome, \"luogoN\", \"dataN\", email, categoria, password, \"statoN\") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, cf);
            statement.setString(2, tesssanitaria);
            statement.setString(3, cognome);
            statement.setString(4, nome);
            statement.setString(5, citta);
            statement.setDate(6, java.sql.Date.valueOf(data));
            statement.setString(7, mail);
            statement.setString(8, categoria);
            statement.setString(9, password);
            statement.setString(10, stato);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo per CITTADINO per aggiornare/modifica la password.
     * @param id
     * @param pwd
     * @throws SQLException
     */
    public void updateCittadinoPassword(String id, String pwd) throws SQLException {
        String query = "UPDATE public.cittadino SET password = ? WHERE \"CF\" = ?";
        connection = connectDB.getConnection();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, pwd);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo per CITTADINO per aggiornare/modificare la mail.
     * @param id
     * @param text
     * @throws SQLException
     */
    public void updateCittadinoMail(String id, String text) throws SQLException {
        connection = connectDB.getConnection();
        String query = "UPDATE public.cittadino SET email = ? WHERE \"CF\" = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, text);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Metodo per DIPENDENTE per visualizzare le prenotazioni per data.
     * @param giornoDP
     * @return ResultSet con prenotazioni.
     */
    public ResultSet showPrenotazioniDatePicker(String giornoDP) throws SQLException {
        ResultSet rs = null;
        this.connection = connectDB.getConnection();
        try {
            String query = "SELECT \"dataOraPrenotazione\", \"ID\", \"CFcittadino\", giorno, ora, servizio, stato, città, \"matricolaAddetto \", \"causaRilascio\" " +
                    "FROM public.prenotazione INNER JOIN public.sede ON codice = \"codSede\" WHERE giorno = ? AND \"codSede\" = ? ORDER BY ora ASC";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setDate(1, java.sql.Date.valueOf(giornoDP));
            statement.setString(2, addettoModel.getCodiceSede());
            rs = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
        return rs;
    }

    /**
     * Metodo per DIPENDENTE per visualizzare le prenotazioni per il giorno odierno.
     * @param now
     * @return ResultSet con prenotazioni.
     */
    public ResultSet showPrenotazioniOdierne(LocalDate now) throws SQLException {
        ResultSet rs = null;
        this.connection = connectDB.getConnection();
        try {
            String query = "SELECT \"dataOraPrenotazione\", \"ID\", \"CFcittadino\", giorno, ora, servizio, stato, città, \"matricolaAddetto \", \"causaRilascio\" " +
                    "FROM public.prenotazione INNER JOIN public.sede ON codice = \"codSede\" WHERE giorno = ? AND \"codSede\" = ? AND stato = 'Confermata' ORDER BY ora ASC";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setDate(1, java.sql.Date.valueOf(now));
            statement.setString(2, codiceSede);
            rs = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
        return rs;
    }

    /**
     * Metodo per DIPENDENTE per visualizzare tutte le prenotazioni eseguite dall'addetto che ha svolto l'accesso.
     * @return ResultSet con prenotazioni.
     */
    public ResultSet showTuttePrenotazioni() throws SQLException {
        ResultSet rs = null;
        this.connection = connectDB.getConnection();
        try {
            String query = "SELECT \"dataOraPrenotazione\", \"ID\", \"CFcittadino\", giorno, ora, servizio, stato, città, \"matricolaAddetto \", \"causaRilascio\" " +
                    "FROM public.prenotazione INNER JOIN public.sede ON codice = \"codSede\" WHERE \"matricolaAddetto \" = ? ORDER BY giorno DESC, ora ASC";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            rs = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
        return rs;
    }

    /**
     * Metodo per DIPENDENTE per visualizzare le prenotazioni con un ID specifico.
     * @param idInserito
     * @return ResultSet con prenotazioni.
     */
    public ResultSet getPrenotazioniID(Integer idInserito) throws SQLException {
        ResultSet rs = null;
        this.connection = connectDB.getConnection();
        try {
            String query = "SELECT \"dataOraPrenotazione\", \"ID\", \"CFcittadino\", giorno, ora, città, servizio, stato, \"matricolaAddetto \", \"causaRilascio\", \"codSede\" " +
                    "FROM public.prenotazione INNER JOIN public.sede ON codice = \"codSede\" WHERE \"ID\" = ? ORDER BY giorno DESC, ora ASC";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, idInserito);
            rs = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
        return rs;
    }

    /**
     * Metodo per DIPENDENTE per visualizzare le informazioni dell'utente riguardo alla sua prenotazione.
     * @param cfCittadino
     * @throws SQLException
     */
    public void getInfoCittadino(String cfCittadino) throws SQLException {
        ResultSet rs = null;
        this.connection = connectDB.getConnection();
        try {
            String query = "SELECT \"CF\", cognome, nome, \"luogoN\", \"dataN\", email, categoria, \"tessSanitaria\", \"statoN\" " +
                    "FROM public.cittadino WHERE \"CF\" = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, cfCittadino);
            rs = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (rs.next()) {
            CFCittadino = cfCittadino;
            nomeCittadino = rs.getString("nome");
            cognomeCittadino = rs.getString("cognome");
            luogoNCittadino = rs.getString("luogoN");
            statoNCittadino = rs.getString("statoN");
            dataNCittadino = rs.getString("dataN");
            mailCittadino = rs.getString("email");
            tessSanitariaCittadino = rs.getString("tessSanitaria");
            categoriaCittadino = rs.getString("categoria");
        }

        connection.close();
    }

    /**
     * Metodo per DIPENDENTE per aggiornare lo stato della prenotazione, ie l'addetto prende in carico la prenotazione.
     * @param idPrenotazione
     */
    public void updateSchedaPrenotazione(String idPrenotazione) throws SQLException {
        this.connection = connectDB.getConnection();
        try {
            String query = "UPDATE public.prenotazione SET stato = 'in Esecuzione', \"matricolaAddetto \"= ? WHERE \"ID\" = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, id);
            statement.setInt(2, Integer.parseInt(idPrenotazione));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
    }

    public void updatePrenotazioneNoShowUp(String idPrenotazione) throws SQLException {
        this.connection = connectDB.getConnection();
        try {
            String query = "UPDATE public.prenotazione SET stato = 'Annullata' WHERE \"ID\" = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, Integer.parseInt(idPrenotazione));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
    }


    /**
     * Metodo per DIPENDENTE per mostrare tutti i passaporti posseduti dall'utente fino a quel momento.
     * @return List<Passaporto>
     * @throws SQLException
     */
    public List<Passaporto> getPassaportiCittadino() throws SQLException {
        List<Passaporto> passaportiList = new ArrayList<>();
        this.connection = connectDB.getConnection();
        try {
            String query = "SELECT numero, tipo, \"dataScadenza\", stato, \"IDrilascio\", \"IDritiro\" FROM public.passaporto " +
                    "WHERE \"CFcittadino\" = ? ORDER BY \"dataScadenza\" DESC";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, cittadinoModel.getCodiceFiscale());
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String numeroPassaporto = rs.getString(1);
                String data = rs.getString(3);
                String tipoPassaporto = rs.getString(2);
                String stato = rs.getString(4);
                // Retrieve other columns as needed

                Passaporto passaporto = new Passaporto(numeroPassaporto, cittadinoModel.getCodiceFiscale(), tipoPassaporto, LocalDate.parse(data),stato);

                passaportiList.add(passaporto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        connection.close();
        return passaportiList;
    }

    /**
     * Metodo per DIPENDENTE per aggiornare lo stato di una prenotazione quando è terminata.
     * @param idPrenotazione
     */
    public void updateTerminePrenotazioni(String idPrenotazione) throws SQLException {
        this.connection = connectDB.getConnection();
        try {
            String query = "UPDATE public.prenotazione SET stato = 'Terminata' WHERE \"ID\" = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(idPrenotazione));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
    }

    /**
     * Metodo per DIPENDENTE per aggiornare lo stato di una prenotazione, quando l'addetto non vuole più eseguirla.
     * @param idPrenotazione
     */
    public void updateRinunciaPrenotazioni(String idPrenotazione) {
        try {
            String query = "UPDATE public.prenotazione SET stato = 'Confermata', \"matricolaAddetto \"= null WHERE \"ID\" = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(idPrenotazione));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo per DIPENDENTE per ottenere la data di scadenza del passaporto relativo ad un cittadino.
     * @return
     */
    public String getDataScadenza() {
        String scadenza = String.valueOf(LocalDate.now().plusYears(10));
        passaportoModel.setDataScadenza(Date.valueOf(scadenza).toLocalDate());
        return scadenza;
    }

    /**
     * Metodo per DIPENDENTE generare un numero di passaporto univoco per un cittadino.
     * @return String con numero generato.
     */
    public String getNumeroPassaporto() throws SQLException {
        String numeroGenerato = generaNumeroPassaporto();
        passaportoModel.setNumero(numeroGenerato);
        return numeroGenerato;
    }

    /**
     * Metodo per DIPENDENTE che genera un nuovo numero passaporto.
     * @return String con numero generato.
     */
    private String generaNumeroPassaporto() throws SQLException {
        String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int NUM_DIGITS = 7;

        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 2; i++) {
            int randomIndex = random.nextInt(ALPHABET.length());
            char randomChar = ALPHABET.charAt(randomIndex);
            sb.append(randomChar);
        }
        for (int i = 0; i < NUM_DIGITS; i++) {
            int randomDigit = random.nextInt(10);
            sb.append(randomDigit);
        }

        String numeroGenerato = sb.toString();

        int rows = checkNumeriPP(numeroGenerato);

        if (rows != 0) {
            getNumeroPassaporto();
        }

        return numeroGenerato;
    }

    /**
     * Metodo per DIPENDENTE per la generazione del numero di passaporto. Controlla che i numeri siano univoci e non presenti
     * all'interno del database.
     * @param numeroGenerato
     * @return #rows presenti nel DB.
     */
    private int checkNumeriPP(String numeroGenerato) throws SQLException {
        this.connection = connectDB.getConnection();
        int rows = 1;
        try {
            String query = "SELECT COUNT(*) FROM public.passaporto WHERE numero = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, numeroGenerato);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                if (rs.getInt("count") == 0) {
                    rows = 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
        return rows;
    }

    /**
     * Metodo per DIPENDENTE per l'inserimento di un nuovo passaporto nel database.
     * @param numeroPassaporto
     * @param CFcitt
     * @param tipoPassaporto
     * @param dataSC
     * @param ID
     */
    public void insertNuovoPassaporto(String numeroPassaporto, String CFcitt, String tipoPassaporto, String dataSC, int ID) throws SQLException {

        this.connection = connectDB.getConnection();

        passaportoModel.setNumero(numeroPassaporto);
        passaportoModel.setCfCittadino(CFcitt);
        passaportoModel.setTipo(tipoPassaporto);
        passaportoModel.setDataScadenza(Date.valueOf(dataSC).toLocalDate());
        passaportoModel.setStato("IN PROCESSO");

        try {
            String query = "INSERT INTO public.passaporto(numero, \"CFcittadino\", tipo, \"dataScadenza\", stato, \"IDrilascio\") " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, numeroPassaporto);
            statement.setString(2, CFcitt);
            statement.setString(3, tipoPassaporto);
            statement.setObject(4, java.sql.Date.valueOf(dataSC));
            statement.setString(5, "IN PROCESSO");
            statement.setInt(6, ID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        connection.close();
    }

    /**
     * Metodo per DIPENDENTE per aggiornare lo stato di un passaporto quando viene ritirato.
     * @param numPPR
     * @param IDPP
     */
    public void updateRitiroPP(String numPPR, int IDPP) throws SQLException {
        this.connection = connectDB.getConnection();
        String query = "UPDATE public.passaporto SET \"IDritiro\" = ?, stato='ATTIVO' WHERE numero = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, IDPP);
            statement.setString(2, numPPR);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
    }

    /**
     * Metodo per DIPENDENTE per vedere i passaporti dell'utente in stato di processo.
     * @return ResultSet con passaporti.
     */
    public ResultSet getPassaportiCittadinoRitiro() throws SQLException {
        this.connection = connectDB.getConnection();
        ResultSet rs = null;
        try {
            String query = "SELECT numero, tipo, \"dataScadenza\", \"CFcittadino\" FROM public.passaporto " +
                    "WHERE \"CFcittadino\" = ? AND stato='IN PROCESSO'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, CFCittadino);
            rs = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
        return rs;
    }

    /**
     * Metodo per DIPENDENTE per ottenere tutti i dati dei dipendenti che lavorano nella sede del responsabile.
     * @param dataUpper
     * @return ResultSet con dati dipendenti.
     * @throws SQLException
     */
    public ResultSet dipendentiData(String dataUpper) throws SQLException {
        ResultSet rs = null;
        this.connection = connectDB.getConnection();
        try {
            String query = "SELECT DISTINCT AD.matricola, AD.nome, AD.cognome " +
                    "FROM public.addetto AS AD " +
                    "INNER JOIN public.\"turnoLavoro\" AS TL ON AD.matricola = TL.\"matricolaAddetto\" " +
                    "WHERE AD.\"sedeLavoro\" = ? AND TL.giorno = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, codiceSede);
            statement.setString(2, dataUpper);
            rs = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        connection.close();
        return rs;
    }

    /**
     * Metodo per DIPENDENTE per il controllo delle date. Controlla che le date non ricadano in periodi di festività,
     * sabato e/o domenica, periodi di ferie ecc..
     * @param dataSlot
     * @param dataUpper
     * @return true, if date is correct. false, if not.
     */
    public boolean checkValiditaData(LocalDate dataSlot, String dataUpper) {
        LocalDate festaLavoro = LocalDate.of(dataSlot.getYear(), Month.MAY, 1);
        LocalDate festaRep = LocalDate.of(dataSlot.getYear(), Month.JUNE, 2);
        LocalDate festaSanti = LocalDate.of(dataSlot.getYear(), Month.NOVEMBER, 1);
        LocalDate festaImmacolata = LocalDate.of(dataSlot.getYear(), Month.DECEMBER, 8);
        List<LocalDate> festivita = Arrays.asList(festaLavoro, festaRep, festaSanti, festaImmacolata);
        LocalDate dateToCheck = dataSlot.withYear(2000); // mi serve per comparare solo mesi e giorni

        if (dataUpper.equals("DOM") || dataUpper.equals("SAB")) {
            return false;
        }

        if (festivita.contains(dataSlot)) {
            return false;
        }

        if (dateToCheck.getMonthValue() == Month.DECEMBER.getValue() && dateToCheck.getDayOfMonth() >= 24) {
            return false;
        }

        if (dateToCheck.getMonthValue() == Month.JANUARY.getValue() && dateToCheck.getDayOfMonth() <= 7) {
            return false;
        }

        if (dateToCheck.getMonthValue() == Month.AUGUST.getValue() && (dateToCheck.getDayOfMonth() >= 1 && dateToCheck.getDayOfMonth() <= 15)) {
            return false;
        }

        return true;
    }

    /**
     * Metodo per DIPENDENTE per inserire la data di lavoro all'interno del database.
     * @param dataSlot
     * @param dataUpper
     * @throws SQLException
     */
    public void insertDataMancante(LocalDate dataSlot, String dataUpper) throws SQLException {
        this.connection = connectDB.getConnection();
        try {
            String query = "INSERT INTO public.data(ggmmaaaa, giorno) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, java.sql.Date.valueOf(dataSlot));
            statement.setString(2, dataUpper);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
    }

    /**
     * Metodo per DIPENDENTE per controllare se la giornata selezionata abbia o meno slot.
     * @param dataSlot
     * @return true, if it has been inserted. false, if not.
     * @throws SQLException
     */
    public boolean checkPresenzaData(LocalDate dataSlot) throws SQLException {
        ResultSet rs = null;
        boolean checker = false;
        this.connection = connectDB.getConnection();
        try {
            String query = "SELECT COUNT(*) ggmmaaaa FROM public.data WHERE ggmmaaaa=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, java.sql.Date.valueOf(dataSlot));
            rs = statement.executeQuery();
            while (rs.next()) {
                if (rs.getInt(1) == 1) {
                    checker = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        connection.close();

        return checker;
    }

    /**
     * Metodo per DIPENDENTE per l'inserimento della disponibilità di un addetto.
     * @param mat
     * @param dataSlot
     * @param sceltaDipSlot
     * @throws SQLException
     */
    public void insertDisponibilita(String mat, LocalDate dataSlot, boolean sceltaDipSlot) throws SQLException {

        this.connection = connectDB.getConnection();
        try {
            String query = "INSERT INTO public.\"disponibilità\"(\"matricolaAddetto\", ggmmaaaa, presenza) VALUES (?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, mat);
            statement.setObject(2, java.sql.Date.valueOf(dataSlot));
            statement.setBoolean(3, sceltaDipSlot);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        connection.close();
    }

    /**
     * Metodo per DIPENDENTE per il controllo della presenza del dipendente in un determinato turno e giorno lavorativo.
     * @param matricola
     * @param dataSlot
     * @return true, if it's not present. false, if it is.
     * @throws SQLException
     */
    public boolean checkAddettoDisponibilita(String matricola, LocalDate dataSlot) throws SQLException {
        ResultSet rs;
        this.connection = connectDB.getConnection();
        try {
            String query = "SELECT \"matricolaAddetto\" FROM public.\"disponibilità\"" +
                    "WHERE \"matricolaAddetto\"=? AND ggmmaaaa=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, matricola);
            statement.setObject(2, java.sql.Date.valueOf(dataSlot));
            rs = statement.executeQuery();
            while (rs.next()) {
                if (!rs.isBeforeFirst()) { // se è vuoto
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
        return false;
    }

    /**
     * Metodo per DIPENDENTE per ottenere gli slot a seconda del giorno selezionato e della disponibilità dei dipendenti.
     * @param dataSlot
     * @return ResultSet con gli slots.
     * @throws SQLException
     */
    public ResultSet getAnteprimaSlot(LocalDate dataSlot) throws SQLException {
        this.connection = connectDB.getConnection();
        ResultSet rs = null;

        if(dataSlot == null){
            return rs;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE", new Locale("it", "IT"));
        String gg = dataSlot.format(formatter);

        boolean checker = checkValiditaData(dataSlot, gg);

        if (checker) {
            String query = "SELECT \"codSede\", ggmmaaaa, \"OraSlot\", servizio, 3 * ("
                    + "SELECT COUNT(matricola) "
                    + "FROM public.addetto "
                    + "INNER JOIN public.\"turnoLavoro\" TL ON TL.\"matricolaAddetto\" = matricola "
                    + "INNER JOIN public.data D ON D.giorno = TL.giorno "
                    + "INNER JOIN public.disponibilità DISP ON DISP.ggmmaaaa = D.ggmmaaaa AND DISP.\"matricolaAddetto\" = matricola "
                    + "WHERE addetto.\"sedeLavoro\" = ? "
                    + "AND D.ggmmaaaa = ? "
                    + "AND DISP.presenza "
                    + "AND PL.\"OraSlot\" BETWEEN TL.\"orarioInizio\" AND TL.\"orarioFine\" "
                    + ") AS \"numSlot\" "
                    + "FROM public.\"programmazioneSlot\" PL "
                    + "INNER JOIN public.data D ON D.giorno = PL.\"giornoSettimana\" "
                    + "WHERE \"codSede\" = ? "
                    + "AND ggmmaaaa = ?";
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, codiceSede);
                statement.setObject(2, java.sql.Date.valueOf(dataSlot));
                statement.setString(3, codiceSede);
                statement.setObject(4, java.sql.Date.valueOf(dataSlot));
                rs = statement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        connection.close();
        return rs;
    }

    /**
     * Metodo per DIPENDENTE per inserire il numero di appuntamenti nello slot orario selezionato.
     * @param codiceSede
     * @param data
     * @param hslot
     * @param serv
     * @param numslot
     * @throws SQLException
     */
    public void inserisciAppuntamento(String codiceSede, String data, String hslot, String serv, int numslot) throws SQLException {
        this.connection = connectDB.getConnection();
        String query = "INSERT INTO public.appuntamento(\"codSede\", giorno, ora, servizio, \"numeroPosti\") VALUES (?, ?, ?, ?, ?);";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, codiceSede);
            statement.setObject(2, java.sql.Date.valueOf(data));
            statement.setObject(3, java.sql.Time.valueOf(hslot));
            statement.setString(4, serv);
            statement.setInt(5, numslot);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
    }

    /**
     * Metodo per DIPENDENTE per controllare se lo slot non sia già stato inserito.
     * @param codiceSede
     * @param data
     * @param hslot
     * @return true, if it's present. false, if it isn't.
     * @throws SQLException
     */
    public boolean checkPresenzaSlot(String codiceSede, String data, String hslot) throws SQLException {
        this.connection = connectDB.getConnection();
        String query = "SELECT \"codSede\", giorno, ora FROM public.appuntamento WHERE \"codSede\"=? AND giorno=? AND ora=?";
        ResultSet rs = null;
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, codiceSede);
            statement.setObject(2, java.sql.Date.valueOf(data));
            statement.setObject(3, java.sql.Time.valueOf(hslot));
            rs = statement.executeQuery();
        } catch(SQLException e){
            e.printStackTrace();
        }

        if(!rs.next()){
            return false;
        }
        connection.close();
        return true;
    }

    /**
     * Metodo per DIPENDENTE per ottenere i dettagli della prenotazione svolta.
     * @param serv
     * @param id
     * @return ResultSet con dettagli utente, dipendente e passaporto.
     * @throws SQLException
     */
    public ResultSet getDettagliPrenotazione(String serv, int id) throws SQLException {
        String rr;
        ResultSet rs = null;

        if(serv.equals("Rilascio")){
            rr = "\"IDrilascio\"";
        } else {
            rr = "\"IDritiro\"";
        }
        this.connection = connectDB.getConnection();

        String query = "SELECT \"dataOraPrenotazione\", CT.nome AS cittadino_nome, CT.cognome AS cittadino_cognome, \"CF\", città, \"matricolaAddetto \", AD.nome AS addetto_nome, AD.cognome AS addetto_cognome, numero, P.stato\n" +
                "FROM public.prenotazione P\n" +
                "INNER JOIN public.cittadino CT ON \"CFcittadino\" = \"CF\"\n" +
                "LEFT JOIN public.addetto AD ON \"matricolaAddetto \" = matricola\n" +
                "INNER JOIN public.sede ON \"codSede\" = codice\n" +
                "LEFT JOIN public.passaporto ON "+rr+" = \"ID\" WHERE \"ID\" = ?";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            rs = statement.executeQuery();
        } catch(SQLException e){
            e.printStackTrace();
        }
        connection.close();
        return rs;
    }

    /**
     * Metodo per DIPENDENTE per ottenere solo gli addetti della sede.
     * @return ResultSet con dipendenti.
     * @throws SQLException
     */
    public ResultSet getDipendentiSede() throws SQLException {
        this.connection = connectDB.getConnection();
        ResultSet rs = null;
        String query = "SELECT matricola, nome, cognome, contratto, \"sedeLavoro\", \"telUfficio\", email " +
                "FROM public.addetto WHERE \"sedeLavoro\"=?";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, codiceSede);
            rs = statement.executeQuery();
        } catch(SQLException e){
            e.printStackTrace();
        }
        connection.close();
        return rs;
    }

    /**
     * Metodo per DIPENDENTE per la generazione di una password random.
     * @return String con password generata.
     */
    public String generaPasswordDipendente() {
        return RandomPasswordGenerator.generateRandomString();
    }

    /**
     * Metodo per DIPENDENTE per la generazione di una marticola univoca.
     * @return String con matricola
     * @throws SQLException
     */
    public String generaMatricolaDipendente() throws SQLException {
        StringBuilder sb = new StringBuilder("AD");

        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int randomNumber = random.nextInt(10);
            sb.append(randomNumber);
        }

        boolean presenza = checkPresenzaMatricola(sb.toString());
        if(presenza){
            generaMatricolaDipendente();
        }
        return sb.toString();
    }

    /**
     * Metodo per DIPENDENTE di supporto al metodo precedente per controllare che la matricola sia univoca.
     * @param matricolaGenerata
     * @return true, if it's unique. false, if not.
     * @throws SQLException
     */
    private boolean checkPresenzaMatricola(String matricolaGenerata) throws SQLException {

        this.connection = connectDB.getConnection();
        String query = "SELECT matricola FROM public.addetto WHERE matricola=?";
        ResultSet rs = null;
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, matricolaGenerata);
            rs = statement.executeQuery();
        } catch(SQLException e){
            e.printStackTrace();
        }
        if(!rs.next()){
            return false;
        }
        connection.close();
        return true;
    }

    /**
     * Metodo per DIPENDENTE per controllare l'integrità della mail inserita dal responsabile.
     * @param fieldMailNuovoDipendente
     * @return true, if it matches. false, if not.
     * @throws SQLException
     */
    public boolean checkMailDipendente(String fieldMailNuovoDipendente) throws SQLException {
        String pattern = "^[A-Za-z0-9_]+@questura\\.it$";
        boolean mailchecker = MailChecker(fieldMailNuovoDipendente);
        if(fieldMailNuovoDipendente.matches(pattern) && !mailchecker){
            return true;
        }
        return false;
    }

    /**
     * Metodo per DIPENDENTE di supporto al metodo precedente per controllare che la mail non sia già presente.
     * @param fieldMailNuovoDipendente
     * @return true, if it's present. false, if it's not.
     * @throws SQLException
     */
    private boolean MailChecker(String fieldMailNuovoDipendente) throws SQLException {
        this.connection = connectDB.getConnection();

        String query = "SELECT email FROM public.addetto WHERE email=?";
        ResultSet rs = null;
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, fieldMailNuovoDipendente);
            rs = statement.executeQuery();
        } catch(SQLException e){
            e.printStackTrace();
        }
        if(!rs.next()){
            return false;
        }
        connection.close();
        return true;
    }

    /**
     * Metodo per DIPENDENTE per il controllo della stringa relativa al numero di telefono inserito.
     * @param input
     * @return true, if it matches. false, if it doesn't.
     * @throws SQLException
     */
    public boolean checkNumeroTelefono(String input) throws SQLException {
        if (input.length() != 10) {
            return false;
        }

        if (!input.startsWith("03")) {
            return false;
        }

        if(checkPresenzaNumero(input)){
            return false;
        }

        return true;
    }

    /**
     * Metodo per DIPENDENTE di supporto al metodo precedente per controllare che il numero di telefono non sia già presente.
     * @param input
     * @return true, if it's not present. false, if it is.
     * @throws SQLException
     */
    private boolean checkPresenzaNumero(String input) throws SQLException {
        this.connection = connectDB.getConnection();
        String query = "SELECT \"telUfficio\" FROM public.addetto WHERE \"telUfficio\"=?";
        ResultSet rs = null;
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, input);
            rs = statement.executeQuery();
        } catch(SQLException e){
            e.printStackTrace();
        }
        if(!rs.next()){
            return false;
        }
        connection.close();
        return true;
    }

    /**
     * Metodo per DIPENDENTE per l'encryption della nuova password autogenerata.
     * @param passwordGenerata
     * @return String con password encrypted.
     * @throws NoSuchAlgorithmException
     */
    public String setEncryption(String passwordGenerata) throws NoSuchAlgorithmException {
        EncryptionPass encryptedPassword = new EncryptionPass();
        String passwordEncrypted = encryptedPassword.setEncrypt(passwordGenerata);
        return passwordEncrypted;
    }

    /**
     * Metodo per DIPENDENTE per l'inserimento del nuovo dipendente nel database.
     * @param matricola
     * @param nome
     * @param cognome
     * @param contratto
     * @param telefono
     * @param mail
     * @param pwdGenerata
     * @throws SQLException
     */
    public void insertNuovoDipendente(String matricola, String nome, String cognome, String contratto, String telefono, String mail, String pwdGenerata) throws SQLException {
        this.connection = connectDB.getConnection();

        String query = "INSERT INTO public.addetto(matricola, nome, cognome, contratto, \"sedeLavoro\", \"telUfficio\", email, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, matricola);
            statement.setString(2, nome);
            statement.setString(3, cognome);
            statement.setString(4, contratto);
            statement.setString(5, codiceSede);
            statement.setString(6, telefono);
            statement.setString(7, mail);
            statement.setString(8, pwdGenerata);
            int rows = statement.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
        connection.close();
    }

    /**
     * Metodo per DIPENDENTE per controllare la presenza del turno di lavoro del dipendente.
     * @param matricola
     * @param orario
     * @param giorno
     * @return true, if it's present. false, if it's not.
     * @throws SQLException
     */
    public boolean checkPresenzaTurno(String matricola, String orario, String giorno) throws SQLException {

        ArrayList<LocalTime> orari = getOrari(orario);

        boolean checker = checkTurno(matricola, orari.get(0), orari.get(1), giorno);

        return checker;
    }

    /**
     * Metodo per MODEL per la gestione della formattazione degli slot orario tra i controller, user input e database.
     * @param orario
     * @return ArrayList<LocalTime> orari convertiti
     */
    private ArrayList<LocalTime> getOrari(String orario) {
        LocalTime orarioInizio = null;
        LocalTime orarioFine = null;

        if(orario.equals("9:00 - 12:00")){
            orarioInizio = LocalTime.of(9, 00, 00);
            orarioFine = LocalTime.of(12, 00, 00);
        }

        if(orario.equals("14:00 - 17:00")){
            orarioInizio = LocalTime.of(14, 00, 00);
            orarioFine = LocalTime.of(17, 00, 00);
        }

        ArrayList<LocalTime> orari = new ArrayList<>();
        orari.add(orarioInizio);
        orari.add(orarioFine);
        return orari;
    }

    /**
     * Metodo per DIPENDENTE di supporto a checkPresenzaTurno per il controllo di consistenza del turno.
     * @param matricola
     * @param orarioInizio
     * @param orarioFine
     * @param giorno
     * @return true, if it's present. false, if it's not.
     * @throws SQLException
     */
    private boolean checkTurno(String matricola, LocalTime orarioInizio, LocalTime orarioFine, String giorno) throws SQLException {
        ResultSet rs = null;
        this.connection = connectDB.getConnection();
        String query = "SELECT * FROM public.\"turnoLavoro\" WHERE \"matricolaAddetto\" = ? AND giorno = ? AND \"orarioInizio\" = ? AND \"orarioFine\" = ?";
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, matricola);
            statement.setString(2, giorno);
            statement.setObject(3, java.sql.Time.valueOf(orarioInizio));
            statement.setObject(4, java.sql.Time.valueOf(orarioFine));
            rs = statement.executeQuery();
        } catch(SQLException e){
            e.printStackTrace();
        }

        if(!rs.next()){
            return false;
        }
        connection.close();
        return true;
    }

    /**
     * Metodo per DIPENDENTE per l'inserimento del turno di lavoro.
     * @param matricola
     * @param orario
     * @param giorno
     * @throws SQLException
     */
    public void insertTurnoDipendente(String matricola, String orario, String giorno) throws SQLException {
        String query = "INSERT INTO public.\"turnoLavoro\"(\"matricolaAddetto\", giorno, \"orarioInizio\", \"orarioFine\") VALUES (?, ?, ?, ?)";
        ArrayList<LocalTime> orari = getOrari(orario);
        this.connection = connectDB.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, matricola);
            statement.setString(2, giorno);
            statement.setObject(3, java.sql.Time.valueOf(orari.get(0)));
            statement.setObject(4, java.sql.Time.valueOf(orari.get(1)));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
    }

    /**
     * Metodo per DIPENDENTE per eliminare un turno di lavoro già presente.
     * @param matricola
     * @param orario
     * @param giorno
     * @throws SQLException
     */
    public void deleteTurnoDipendente(String matricola, String orario, String giorno) throws SQLException {
        ArrayList<LocalTime> orari = getOrari(orario);
        this.connection = connectDB.getConnection();

        String query = "DELETE FROM public.\"turnoLavoro\" WHERE \"matricolaAddetto\"=? AND giorno=? AND \"orarioInizio\"=? AND \"orarioFine\"=?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, matricola);
            statement.setString(2, giorno);
            statement.setObject(3, java.sql.Time.valueOf(orari.get(0)));
            statement.setObject(4, java.sql.Time.valueOf(orari.get(1)));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
    }

    /**
     * Metodo per CITTADINO per ottenere gli slots degli orari giorno per giorno.
     * @param dateFocus
     * @param codice
     * @param text
     * @return ResultSet con orari.
     * @throws SQLException
     */
    public ResultSet getSlots(LocalDate dateFocus, String codice, String text) throws SQLException {
        String query = "SELECT ora, \"numeroPosti\" FROM public.appuntamento\n" +
                "WHERE giorno = ?\n" +
                "AND \"codSede\" = ? " +
                "AND servizio = ? " +
                "ORDER BY ora ASC";
        ResultSet rs = null;
        this.connection = connectDB.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, Date.valueOf(dateFocus));
            statement.setString(2, codice);
            statement.setString(3, text);
            rs = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * Metodo per CITTADINO per contare quanti slots sono presenti in un giorno.
     * @param dateC
     * @return int con numero slots.
     * @throws SQLException
     */
    public int countSlots(LocalDate dateC) throws SQLException {
        String query = "SELECT COUNT(giorno) from public.appuntamento WHERE giorno = ?";
        ResultSet rs = null;
        int count = 0;
        this.connection = connectDB.getConnection();

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, java.sql.Date.valueOf(dateC));
            rs = statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        while(rs.next()){
            count = rs.getInt(1);
        }
        return count;
    }

    /**
     * Metodo per MODEL per settare il codice fiscale del cittadino, nel caso l'oggetto Cittadino non lo possieda.
     * @param cfCittadino
     */
    public void setCFCittadino(String cfCittadino) {
        CFCittadino = cfCittadino;
        cittadinoModel.setCodiceFiscale(cfCittadino);
    }

    /**
     * Metodo per DIPENDENTE per visualizzare tutti i passaporti con stato in processo e che possono essere ritirati.
     * @param cfCittadino
     * @throws SQLException
     */
    public void getPassaportoRitiro(String cfCittadino) throws SQLException {
        this.connection = connectDB.getConnection();

        try {
            String query = "SELECT numero, tipo, \"dataScadenza\", stato, \"IDrilascio\", \"IDritiro\" FROM public.passaporto " +
                    "WHERE \"CFcittadino\" = ? AND stato='IN PROCESSO'";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, cfCittadino);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                String numeroPassaporto = rs.getString(1);
                String data = rs.getString(3);
                String tipoPassaporto = rs.getString(2);
                String stato = rs.getString(4);
                passaportoModel.setTipo(tipoPassaporto);
                passaportoModel.setNumero(numeroPassaporto);
                passaportoModel.setStato(stato);
                passaportoModel.setDataScadenza(LocalDate.parse(data));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.close();
    }
    // -- richiamo di metodi che esistono già nella classe passaporto.
    public String getPassaportoModelNumero(){
        return passaportoModel.getNumero();
    }
    public String getPassaportoModelTipo(){
        return passaportoModel.getTipo();
    }
    public String getPassaportoModelCF(){
        return passaportoModel.getCfCittadino();
    }
    public LocalDate getPassaportoModelDate(){
        return passaportoModel.getDataScadenza();
    }
    public String getSedeAddetto() {
        return addettoModel.getCodiceSede();
    }
    // -- fine di questi metodi, sono stati inseriti per la correttezza del codice ma non utilizzati

    /**
     * Metodo per DIPENDENTE per disattivare un passaporto
     * @param text
     * @return # of deactivated passports
     * @throws SQLException
     */
    public int disattivaPP(String text) throws SQLException {
        this.connection = connectDB.getConnection();
        String query = "UPDATE public.passaporto\n" +
                "\tSET stato='NON ATTIVO'\n" +
                "\tWHERE numero = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, text);
        int affectedRows = statement.executeUpdate();
        connection.close();
        return affectedRows;
    }

    /**
     * Metodo per CITTADINO per controllare i servizi presente in una sede
     * @param value
     * @return ArrayList<String> servizi
     * @throws SQLException
     */
    public ArrayList<String> getSedeServizi(String value) throws SQLException {
        String servizio = value.equals("Ritiro") ? "Ritiro" : "Rilascio";
        ArrayList<String> sedi = new ArrayList<>();

        this.connection = connectDB.getConnection();
        try {
                String query = "SELECT DISTINCT città\n" +
                        "FROM public.appuntamento\n" +
                        "JOIN public.sede ON \"codSede\" = codice\n" +
                        "WHERE servizio = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, servizio);
                ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    sedi.add(rs.getString(1));
                }

                statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sedi;
    }

    /**
     * Metodo condiviso da CITTADINO e DIPENDENTE per il controllo del codice della sede e della città.
     * @param value
     * @return String to compare.
     * @throws SQLException
     */
    public String getNSede(String value) throws SQLException {
        this.connection = connectDB.getConnection();
        String codSede = new String();

        try{
            String query = "SELECT codice\n" +
                    "FROM public.sede \n" +
                    "WHERE città = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, value);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                codSede = rs.getString(1);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return codSede;
    }

    /**
     * Metodo per CITTADINO per visualizzare quali slots non sono più disponibili.
     * @param dateC
     * @param numSedePP
     * @param servPP
     * @return # number of empty slots per day.
     * @throws SQLException
     */
    public int getZeroSlot(LocalDate dateC, String numSedePP, String servPP) throws SQLException {

        String query = "SELECT COUNT(\"numeroPosti\")\n" +
                "\tFROM public.appuntamento\n" +
                "\tWHERE \"codSede\" = ?\n" +
                "\tAND servizio = ?\n" +
                "\tAND giorno = ?\n" +
                "\tAND \"numeroPosti\" = '0'";

        ResultSet rs = null;
        int count = 0;
        this.connection = connectDB.getConnection();

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, numSedePP);
            statement.setString(2, servPP);
            statement.setDate(3, java.sql.Date.valueOf(dateC));
            rs = statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        while(rs.next()){
            count = rs.getInt(1);
        }
        connection.close();
        return count;
    }

    /**
     * Metodo per CITTADINO per visualizzare la causale di rilascio.
     * @param string
     * @return String.
     */
    public String getCausaRilascio(String string){
        String pattern = "Rilascio per ";

        int patternIndex = string.indexOf(pattern);
        String substring = new String();

        if (patternIndex != -1) {
            substring = string.substring(patternIndex + pattern.length());
        }
        if(string.equals("Ritiro")){
            substring = null;
        }
        return substring;
    }

    /**
     * Metodo per CITTADINO per l'inizializzazione della coda.
     * @param servPP
     * @param numSedePP
     * @param value
     * @param timeStamp
     * @throws SQLException
     */
    public void setCoda(String servPP, String numSedePP, String value, Timestamp timeStamp) throws SQLException {
        this.connection = connectDB.getConnection();
        int anno = 1900;
        int mese = 01;
        int giorno = 01;

        LocalDate defaultDate = LocalDate.of(anno, mese, giorno);

        String query = "INSERT INTO public.prenotazione(" +
                "\"dataOraPrenotazione\", \"CFcittadino\", \"codSede\", servizio, stato, \"causaRilascio\", giorno, ora) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        String substring = getCausaRilascio(value);

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setTimestamp(1, timeStamp);
            statement.setString(2, cittadinoModel.getCodiceFiscale());
            statement.setString(3, numSedePP);
            statement.setString(4, servPP);
            statement.setString(5, "in coda");
            statement.setString(6, substring);
            statement.setDate(7, java.sql.Date.valueOf(defaultDate));
            statement.setObject(8, java.sql.Time.valueOf("00:00:00"));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connection.close();
    }

    /**
     * Metodo per CITTADINO per ottenere il numero della coda.
     * @param servPP
     * @param numSedePP
     * @param timeStampEntrata
     * @return # of queue
     * @throws SQLException
     */
    public int getCoda(String servPP, String numSedePP, Timestamp timeStampEntrata) throws SQLException {
        int numeroCoda = 0;
        ResultSet rs;

        this.connection = connectDB.getConnection();

        String query = "SELECT COUNT(*)\n" +
                "FROM public.prenotazione\n" +
                "WHERE \"codSede\"=? AND servizio=? AND stato='in coda' AND \"dataOraPrenotazione\" < ?";
        try{
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, numSedePP);
            statement.setString(2, servPP);
            statement.setTimestamp(3, timeStampEntrata);
            rs = statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        while(rs.next()){
            numeroCoda = rs.getInt(1);
        }
        connection.close();
        return numeroCoda;
    }

    /**
     * Metodo per CITTADINO per l'inserimento della prenotazione.
     * @param numSedePP
     * @param giornoPrenotazione
     * @param slotPrenotazione
     * @param timeStampEntrata
     * @param servizioEntrata
     * @throws SQLException
     */
    public void setPrenotazione(String numSedePP, LocalDate giornoPrenotazione, String slotPrenotazione, Timestamp timeStampEntrata, String servizioEntrata) throws SQLException {

        this.connection = connectDB.getConnection();
        String query = "BEGIN;\n" +
                "SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;\n" +
                "UPDATE public.prenotazione\n" +
                "SET giorno=?, ora=?, servizio=?, stato='Confermata'\n" +
                "WHERE \"dataOraPrenotazione\"=? AND \"CFcittadino\"=?;\n" +
                "UPDATE public.appuntamento\n" +
                "SET \"numeroPosti\" = \"numeroPosti\" - 1\n" +
                "WHERE \"codSede\" = ? AND giorno = ? AND ora = ?;\n" +
                "COMMIT;";

        LocalTime ora = getOrarioSlot(slotPrenotazione);

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, java.sql.Date.valueOf(giornoPrenotazione));
            statement.setObject(2, java.sql.Time.valueOf(ora));
            statement.setString(3, servizioEntrata);
            statement.setTimestamp(4, timeStampEntrata);
            statement.setString(5, cittadinoModel.getCodiceFiscale());
            statement.setString(6, numSedePP);
            statement.setDate(7, java.sql.Date.valueOf(giornoPrenotazione));
            statement.setObject(8, java.sql.Time.valueOf(ora));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connection.close();
    }

    /**
     * Metodo per MODEL per conversione degli slots in LocalTime.
     * @param slotPrenotazione
     * @return LocalTime
     */
    private LocalTime getOrarioSlot(String slotPrenotazione) {
        LocalTime ora = null;

        if(slotPrenotazione.equals("9-10")){
            ora = LocalTime.of(9, 0, 0);
        }
        if(slotPrenotazione.equals("10-11")){
            ora = LocalTime.of(10, 0, 0);
        }
        if(slotPrenotazione.equals("11-12")){
            ora = LocalTime.of(11, 0, 0);
        }
        if(slotPrenotazione.equals("14-15")){
            ora = LocalTime.of(14, 0, 0);
        }
        if(slotPrenotazione.equals("15-16")){
            ora = LocalTime.of(15, 0, 0);
        }
        if(slotPrenotazione.equals("16-17")){
            ora = LocalTime.of(16, 0, 0);
        }

        return ora;
    }

    /**
     * Metodo per CITTADINO per il controllo della prenotazione del passaporto nel database per il primo rilascio.
     * @return true, if it's present. false, if not.
     * @throws SQLException
     */
    public boolean getPresenzaPassaporto() throws SQLException {

        String query = "SELECT COUNT(*)\n" +
                "FROM public.passaporto \n" +
                "WHERE \"CFcittadino\" = ?";

        ResultSet rs = null;
        this.connection = connectDB.getConnection();

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, cittadinoModel.getCodiceFiscale());
            rs = statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        while(rs.next()){
            if(rs.getInt(1)==0){
                return true;
            }
        }
        connection.close();
        return false;
    }

    /**
     * Metodo per CITTADINO per il controllo della prenotazione del passaporto per Furto, deteRioramento e/o Smarrimento.
     * @return true, if it's present. false, if not.
     * @throws SQLException
     */
    public boolean getPassaportoFRS() throws SQLException {

        String query = "SELECT COUNT(*)\n" +
                "FROM public.passaporto\n" +
                "WHERE \"CFcittadino\" = ?\n" +
                "AND stato = 'ATTIVO'\n" +
                "AND \"dataScadenza\" - CURRENT_DATE > 0";

        ResultSet rs = null;
        this.connection = connectDB.getConnection();

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, cittadinoModel.getCodiceFiscale());
            rs = statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        while(rs.next()){
            if(rs.getInt(1)==1){
                return true;
            }
        }
        connection.close();
        return false;
    }

    /**
     * Metodo per CITTADINO per il controllo della prenotazione del passaporto per la presenza di un passaporto in processo
     * per poter effettuare il ritiro.
     * @return LocalDate of last appointment.
     * @throws SQLException
     */
    public LocalDate getIfPassaportoRitiro() throws SQLException {
        LocalDate dataRitiro = null;

        String query = "SELECT giorno\n" +
                "FROM public.passaporto PA\n" +
                "JOIN public.prenotazione PR ON \"IDrilascio\" = \"ID\"\n" +
                "WHERE PA.\"CFcittadino\" = ?\n" +
                "AND PA.stato = 'IN PROCESSO'";

        ResultSet rs = null;
        this.connection = connectDB.getConnection();

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, cittadinoModel.getCodiceFiscale());
            rs = statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        while(rs.next()){
            dataRitiro = rs.getDate(1).toLocalDate();
        }
        connection.close();
        return dataRitiro;
    }

    /**
     * Metodo per CITTADINO per eliminare la prenotazione dal database.
     * @param timeStampEntrata
     * @throws SQLException
     */
    public void deletePrenotazione(Timestamp timeStampEntrata) throws SQLException {
        String query = "DELETE FROM public.prenotazione\n" +
                "\tWHERE \"dataOraPrenotazione\" = ?\n" +
                "\tAND \"CFcittadino\" = ?";

        this.connection = connectDB.getConnection();

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setTimestamp(1, timeStampEntrata);
            statement.setString(2, cittadinoModel.getCodiceFiscale());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        connection.close();
    }

    /**
     * Metodo per CITTADINO per controllare la presenza di un passaporto in processo e che può essere ritirato.
     * @param giornoPrenotazione
     * @return true, if it's present. false, if not.
     * @throws SQLException
     */
    public boolean checkRitiro(LocalDate giornoPrenotazione) throws SQLException {

        ResultSet rs = null;

        String query = "SELECT ? - giorno\n" +
                "FROM public.passaporto PA\n" +
                "JOIN public.prenotazione PR ON \"IDrilascio\" = \"ID\"\n" +
                "WHERE PA.\"CFcittadino\" = ?\n" +
                "AND PA.stato = 'IN PROCESSO'";

        this.connection = connectDB.getConnection();

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, java.sql.Date.valueOf(giornoPrenotazione));
            statement.setString(2, cittadinoModel.getCodiceFiscale());
            rs = statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        while(rs.next()){
            if(rs.getInt(1)>30){
                return true;
            }
        }
        connection.close();
        return false;
    }

    /**
     * Metodo per CITTADINO per controllare se il passaporto è in scadenza.
     * @return true, if it is. false, if not.
     * @throws SQLException
     */
    public boolean checkScadenzaPP() throws SQLException {
        ResultSet rs = null;

        String query = "SELECT (\"dataScadenza\" - CURRENT_DATE) < 180\n" +
                "FROM public.passaporto\n" +
                "WHERE \"CFcittadino\" = ?\n" +
                "AND stato = 'ATTIVO'";
        this.connection = connectDB.getConnection();

        try{
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, cittadinoModel.getCodiceFiscale());
            rs = statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        while(rs.next()){
            if(rs.getBoolean(1)==true){
                return true;
            }
        }
        connection.close();
        return false;
    }

    /**
     * Metodo per CITTADINO che controlla se l'utente ha già una prenotazione in corso.
     * @return true if it's present. false, if not.
     * @throws SQLException
     */
    public boolean getPrenotazioneUnica() throws SQLException {
        ResultSet rs = null;

        String query = "SELECT COUNT(*)=1\n" +
                "FROM public.prenotazione\n" +
                "WHERE \"CFcittadino\" = ? AND (stato='Confermata' OR stato='in coda')";

        this.connection = connectDB.getConnection();

        try{
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, cittadinoModel.getCodiceFiscale());
            rs = statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        while(rs.next()){
            if(rs.getBoolean(1) == true){
                return true;
            }
        }
        connection.close();
        return false;
    }
}