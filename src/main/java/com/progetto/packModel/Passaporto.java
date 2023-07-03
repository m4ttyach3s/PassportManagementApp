package com.progetto.packModel;

import java.time.LocalDate;

public class Passaporto {
    private String numero;
    private String cfCittadino;
    private String tipo;
    private LocalDate dataScadenza;
    private String stato;

    public Passaporto(String numero, String cfCittadino, String tipo, LocalDate dataScadenza, String stato){
        this.numero = numero;
        this.cfCittadino = cfCittadino;
        this.tipo = tipo;
        this.dataScadenza = dataScadenza;
        this.stato = stato;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCfCittadino() {
        return cfCittadino;
    }

    public void setCfCittadino(String cfCittadino) {
        this.cfCittadino = cfCittadino;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(LocalDate dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    @Override
    public String toString() {
        return "Passaporto{" +
                "numero='" + numero + '\'' +
                ", cfCittadino='" + cfCittadino + '\'' +
                ", tipo='" + tipo + '\'' +
                ", dataScadenza=" + dataScadenza +
                ", stato='" + stato + '\'' +
                '}';
    }
}
