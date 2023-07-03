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

    public static String getTutore1Cittadino() {
        return tutore1Cittadino;
    }

    public static String getTutore2Cittadino() {
        return tutore2Cittadino;
    }

    public static String getMailCittadino() {
        return mailCittadino;
    }

    public static String getNumero_passaporto() {
        return numero_passaporto;
    }

    public static String getTipo_passaporto() {
        return tipo_passaporto;
    }

    public static String getCategoriaCittadino() {
        return categoriaCittadino;
    }

    public static String getTessSanitariaCittadino() {
        return tessSanitariaCittadino;
    }

    // costruttore
    public Model() {
        this.Nome = new String();
        this.Cognome = new String();
        this.Sede = new String();
        this.Ruolo = new String();
        this.codiceSede = new String();

        this.connectDB = new ConnectDB();

        this.CFCittadino = new String();
        this.nomeCittadino = new String();
        this.cognomeCittadino = new String();
        this.luogoNCittadino = new String();
        this.dataNCittadino = new String();
        this.statoNCittadino = new String();
        this.categoriaCittadino = new String();
        this.mailCittadino = new String();
        this.tutore1Cittadino = new String();
        this.tutore2Cittadino = new String();
        this.tessSanitariaCittadino = new String();

        this.idPrenotazione = new String();
        this.numero_passaporto = new String();
        this.tipo_passaporto = new String();
        statusAnagrafica = false;
        tabellaCittadino = false;
    }

    public static Model getInstance() {
        if (model == null) {
            model = new Model();
        }

        return model;
    }


    // ----------- Sezione Getters -----------
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

    public String getIdPrenotazione() {
        return idPrenotazione;
    }

    public static String getCodiceSede() {
        return codiceSede;
    }

    public static Collection<String> getC() {
        return c;
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

    // ----------- METODI PER LE QUERY -----------
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

    // ---------------- METODO CONDIVISO DA CITTADINO E DIPENDENTE
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

    // -------- METODI PER IL CITTADINO ---------
    public ResultSet getPrenotazioniAttive() {
        String CF = cittadinoModel.getCodiceFiscale();
        String query = "SELECT \"ID\", città, giorno, ora, servizio, \"causaRilascio\", \"codSede\" " +
                "FROM public.prenotazione, public.sede WHERE \"codSede\" = codice" +
                " AND \"CFcittadino\" = '" + CF + "' AND stato = 'Confermata'";
        ResultSet rs = null;
        try {
            rs = setConnection(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet getPrenotazioniPassate() {
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
        //connectDB.closeConnection(this.connection);
        return rs;
    }

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

    public void makePDF(List<String> lista, String servizio, String causaR) {
        String name = new String("PDF_Passaporto_" + getId() + "_" + lista.get(0) + causaR + ".pdf");
        PDFGenerator pdfGenerator = new PDFGenerator(name);
        pdfGenerator.makePDF(lista, servizio, causaR);
    }

    // -------- METODI PER IL DIPENDENTE ---------

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


    // -------- METODI PER LA REGISTRAZIONE ---------
    public boolean checkPassword(String pwd) {
        return textPattern.matcher(pwd).matches() && pwd.length() >= 10;
    }

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

    private boolean checkCF(String cf) {
        String pattern = "\\w{6}\\d{2}\\w\\d{2}\\w\\d{3}\\w"; // uso per comparare con il mio input
        Pattern regex = Pattern.compile(pattern); // registro
        Matcher checker = regex.matcher(cf);

        if (checker.matches() && cf.length() == 16) {
            return true;
        }

        return false;
    }

    private boolean checkDataValida(LocalDate data) {
        Period etaCalcolata = Period.between(data, LocalDate.now());
        if (etaCalcolata.getYears() >= 18 && etaCalcolata.getYears() <= 110) {
            return true;
        }
        return false;
    }

    public boolean checkMail(String s) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

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

    public ResultSet showPrenotazioniDatePicker(String giornoDP) {
        ResultSet rs = null;
        try {
            String query = "SELECT \"dataOraPrenotazione\", \"ID\", \"CFcittadino\", giorno, ora, servizio, stato, città, \"matricolaAddetto \", \"causaRilascio\" " +
                    "FROM public.prenotazione INNER JOIN public.sede ON codice = \"codSede\" WHERE giorno = ? AND \"codSede\" = ? ORDER BY ora ASC";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setDate(1, java.sql.Date.valueOf(giornoDP));
            // TODO
            //  statement.setString(2, codiceSede);
            statement.setString(2, addettoModel.getCodiceSede());
            rs = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet showPrenotazioniOdierne(LocalDate now) {
        ResultSet rs = null;
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
        return rs;
    }

    public ResultSet showTuttePrenotazioni() {
        ResultSet rs = null;
        try {
            String query = "SELECT \"dataOraPrenotazione\", \"ID\", \"CFcittadino\", giorno, ora, servizio, stato, città, \"matricolaAddetto \", \"causaRilascio\" " +
                    "FROM public.prenotazione INNER JOIN public.sede ON codice = \"codSede\" WHERE \"matricolaAddetto \" = ? ORDER BY giorno DESC, ora ASC";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            rs = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet getPrenotazioniID(Integer idInserito) {
        ResultSet rs = null;
        try {
            String query = "SELECT \"dataOraPrenotazione\", \"ID\", \"CFcittadino\", giorno, ora, città, servizio, stato, \"matricolaAddetto \", \"causaRilascio\", \"codSede\" " +
                    "FROM public.prenotazione INNER JOIN public.sede ON codice = \"codSede\" WHERE \"ID\" = ? ORDER BY giorno DESC, ora ASC";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, idInserito);
            rs = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public void getInfoCittadino(String cfCittadino) throws SQLException {
        ResultSet rs = null;
        try {
            String query = "SELECT \"CF\", cognome, nome, \"luogoN\", \"dataN\", email, tutore1, tutore2, categoria, \"tessSanitaria\", \"statoN\" " +
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
            tutore1Cittadino = rs.getString("tutore1");
            tutore2Cittadino = rs.getString("tutore2");
            categoriaCittadino = rs.getString("categoria");
        }

    }

    public void updateSchedaPrenotazione(String idPrenotazione) {
        try {
            String query = "UPDATE public.prenotazione SET stato = 'in Esecuzione', \"matricolaAddetto \"= ? WHERE \"ID\" = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, id);
            statement.setInt(2, Integer.parseInt(idPrenotazione));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // TODO
    // classe esterna! devi crearle..
    /*
    public class PassaportiCittadinoResult {
    private ResultSet resultSet;
    private String numeroPassaporto;
    private String tipoPassaporto;

    public PassaportiCittadinoResult(ResultSet resultSet, String numeroPassaporto, String tipoPassaporto) {
        this.resultSet = resultSet;
        this.numeroPassaporto = numeroPassaporto;
        this.tipoPassaporto = tipoPassaporto;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public String getNumeroPassaporto() {
        return numeroPassaporto;
    }

    public String getTipoPassaporto() {
        return tipoPassaporto;
    }
}

public PassaportiCittadinoResult getPassaportiCittadino() throws SQLException {
    ResultSet rs = null;
    String numeroPassaporto = null;
    String tipoPassaporto = null;

    try {
        String query = "SELECT numero, tipo, \"dataScadenza\", stato, \"IDrilascio\", \"IDritiro\" FROM public.passaporto " +
                "WHERE \"CFcittadino\" = ? ORDER BY \"dataScadenza\" DESC";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, CFCittadino);
        rs = statement.executeQuery();

        if (rs.next()) {
            numeroPassaporto = rs.getString("numero");
            tipoPassaporto = rs.getString("tipo");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return new PassaportiCittadinoResult(rs, numeroPassaporto, tipoPassaporto);
}
PassaportiCittadinoResult result = getPassaportiCittadino();
ResultSet rs = result.getResultSet();
String numeroPassaporto = result.getNumeroPassaporto();
String tipoPassaporto = result.getTipoPassaporto();

// Do something with the ResultSet and the extracted values
// e.g., iterate over the ResultSet or use the values in some logic

     */

    /*
    public ResultSet getPassaportiCittadino() throws SQLException {

        ResultSet rs = null;
        try {
            String query = "SELECT numero, tipo, \"dataScadenza\", stato, \"IDrilascio\", \"IDritiro\" FROM public.passaporto " +
                    "WHERE \"CFcittadino\" = ? ORDER BY \"dataScadenza\" DESC";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, cittadinoModel.getCodiceFiscale());
            rs = statement.executeQuery();
            while (rs.next()) {
                numero_passaporto = rs.getString("numero");
                tipo_passaporto = rs.getString("tipo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

     */

    public List<Passaporto> getPassaportiCittadino() throws SQLException {
        List<Passaporto> passaportiList = new ArrayList<>();

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

        return passaportiList;
    }

    public void updateTerminePrenotazioni(String idPrenotazione) {
        try {
            String query = "UPDATE public.prenotazione SET stato = 'Terminata' WHERE \"ID\" = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(idPrenotazione));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    public String getDataScadenza() {

        String scadenza = String.valueOf(LocalDate.now().plusYears(10));
        passaportoModel.setDataScadenza(Date.valueOf(scadenza).toLocalDate());

        return scadenza;
    }

    public String getNumeroPassaporto() {
        String numeroGenerato = generaNumeroPassaporto();
        passaportoModel.setNumero(numeroGenerato);
        return numeroGenerato;
    }

    private String generaNumeroPassaporto() {
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

    private int checkNumeriPP(String numeroGenerato) {
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
        return rows;
    }

    public void insertNuovoPassaporto(String numeroPassaporto, String CFcitt, String tipoPassaporto, String dataSC, int ID) {
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
    }

    public void updateRitiroPP(String numPPR, int IDPP) {
        String query = "UPDATE public.passaporto SET \"IDritiro\" = ?, stato='ATTIVO' WHERE numero = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, IDPP);
            statement.setString(2, numPPR);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getPassaportiCittadinoRitiro() {
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
        return rs;

    }


    public ResultSet dipendentiData(String dataUpper) throws SQLException {
        ResultSet rs = null;

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
        return rs;
    }

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

    public void insertDataMancante(LocalDate dataSlot, String dataUpper) {
        try {
            String query = "INSERT INTO public.data(ggmmaaaa, giorno) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setObject(1, java.sql.Date.valueOf(dataSlot));
            statement.setString(2, dataUpper);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkPresenzaData(LocalDate dataSlot) throws SQLException {
        ResultSet rs = null;
        boolean checker = false;
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

        return checker;
    }

    public void insertDisponibilita(String mat, LocalDate dataSlot, boolean sceltaDipSlot) {
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
    }

    public boolean checkAddettoDisponibilita(String matricola, LocalDate dataSlot) {
        ResultSet rs;
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
        return false;
    }

    public ResultSet getAnteprimaSlot(LocalDate dataSlot) throws SQLException {
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
        return rs;
    }

    public void inserisciAppuntamento(String codiceSede, String data, String hslot, String serv, int numslot) {
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
    }

    public boolean checkPresenzaSlot(String codiceSede, String data, String hslot) throws SQLException {
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
        return true;
    }

    public ResultSet getDettagliPrenotazione(String serv, int id) {
        String rr;
        ResultSet rs = null;

        if(serv.equals("Rilascio")){
            rr = "\"IDrilascio\"";
        } else {
            rr = "\"IDritiro\"";
        }

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
        return rs;
    }

    public ResultSet getDipendentiSede() {
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
        return rs;
    }

    public String generaPasswordDipendente() {
        return RandomPasswordGenerator.generateRandomString();
    }

    public String generaMatricolaDipendente() throws SQLException {
        StringBuilder sb = new StringBuilder("AD");

        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int randomNumber = random.nextInt(10); // Generates a random number between 0 and 9
            sb.append(randomNumber);
        }

        boolean presenza = checkPresenzaMatricola(sb.toString());
        if(presenza){
            generaMatricolaDipendente();
        }
        return sb.toString();
    }

    private boolean checkPresenzaMatricola(String matricolaGenerata) throws SQLException {
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
        return true;
    }

    public boolean checkMailDipendente(String fieldMailNuovoDipendente) throws SQLException {
        String pattern = "^[A-Za-z0-9_]+@questura\\.it$";
        boolean mailchecker = MailChecker(fieldMailNuovoDipendente);
        if(fieldMailNuovoDipendente.matches(pattern) && !mailchecker){
            return true;
        }
        return false;
    }

    private boolean MailChecker(String fieldMailNuovoDipendente) throws SQLException {
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
        return true;
    }

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

    private boolean checkPresenzaNumero(String input) throws SQLException {
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
        return true;
    }

    public String setEncryption(String passwordGenerata) throws NoSuchAlgorithmException {
        EncryptionPass encryptedPassword = new EncryptionPass();
        String passwordEncrypted = encryptedPassword.setEncrypt(passwordGenerata);
        return passwordEncrypted;
    }

    public void insertNuovoDipendente(String matricola, String nome, String cognome, String contratto, String telefono, String mail, String pwdGenerata) {

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
    }

    public boolean checkPresenzaTurno(String matricola, String orario, String giorno) throws SQLException {

        ArrayList<LocalTime> orari = getOrari(orario);

        boolean checker = checkTurno(matricola, orari.get(0), orari.get(1), giorno);

        return checker;
    }

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

    private boolean checkTurno(String matricola, LocalTime orarioInizio, LocalTime orarioFine, String giorno) throws SQLException {
        ResultSet rs = null;
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
        return true;
    }

    public void insertTurnoDipendente(String matricola, String orario, String giorno) throws SQLException {
        String query = "INSERT INTO public.\"turnoLavoro\"(\"matricolaAddetto\", giorno, \"orarioInizio\", \"orarioFine\") VALUES (?, ?, ?, ?)";
        ArrayList<LocalTime> orari = getOrari(orario);

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
    }

    public void deleteTurnoDipendente(String matricola, String orario, String giorno) {
        ArrayList<LocalTime> orari = getOrari(orario);
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
    }

    public ResultSet getSlots(LocalDate dateFocus, String codice, String text) throws SQLException {
        String query = "SELECT ora, \"numeroPosti\" FROM public.appuntamento\n" +
                "WHERE giorno = ?\n" +
                "AND \"codSede\" = ? " + // Added a space here
                "AND servizio = ? " + // Added a space here
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

    public void setCFCittadino(String cfCittadino) {
        CFCittadino = cfCittadino;
        cittadinoModel.setCodiceFiscale(cfCittadino);
    }

    public void getPassaportoRitiro(String cfCittadino) {
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
    }
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

    public int disattivaPP(String text) throws SQLException {
        String query = "UPDATE public.passaporto\n" +
                "\tSET stato='NON ATTIVO'\n" +
                "\tWHERE numero = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, text);
        int affectedRows = statement.executeUpdate();
        return affectedRows;
    }

    /*
    public ArrayList<String> getSedeServizi(String value) {
        String servizio = new String();
        if(value.equals("Ritiro")){
            servizio = "Ritiro";
        } else {
            servizio = "Rilascio";
        }

        ArrayList<String> sedi = new ArrayList<>();

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
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return sedi;
    }

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

                statement.close(); // Close the statement after use
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sedi;
    }

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
        return count;
    }
}