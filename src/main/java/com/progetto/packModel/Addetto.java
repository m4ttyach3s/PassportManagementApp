package com.progetto.packModel;

public class Addetto {

    private String matricola;
    private String codiceSede;
    private boolean isResponsabile;

    public Addetto(String matricola, String codiceSede, boolean isResponsabile){
        this.matricola = matricola;
        this.codiceSede = codiceSede;
        this.isResponsabile = isResponsabile;
    }

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public boolean isResponsabile() {
        return isResponsabile;
    }

    public void setResponsabile(boolean responsabile) {
        isResponsabile = responsabile;
    }

    public String getCodiceSede() {
        return codiceSede;
    }

    public void setCodiceSede(String codiceSede) {
        this.codiceSede = codiceSede;
    }
}
