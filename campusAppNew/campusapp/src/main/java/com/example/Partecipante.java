package com.example;

import java.io.Serializable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Partecipante implements Serializable {
    private final StringProperty nome;
    private final StringProperty cognome;
    private final StringProperty telefono;
    private final StringProperty genitore;
    private final DoubleProperty acconto;
    private final StringProperty statoAcconto;
    private final StringProperty tagliaMaglietta;

    public Partecipante(String nome, String cognome, String telefono, String genitore, double acconto,
            String tagliaMaglietta) {
        this.nome = new SimpleStringProperty(nome);
        this.cognome = new SimpleStringProperty(cognome);
        this.telefono = new SimpleStringProperty(telefono);
        this.genitore = new SimpleStringProperty(genitore);
        this.tagliaMaglietta = new SimpleStringProperty(tagliaMaglietta);
        this.acconto = new SimpleDoubleProperty(acconto);
        this.statoAcconto = new SimpleStringProperty("Non versato");
    }

    public void setNome(String nome) {
        this.nome.set(nome);
    }

    public void setCognome(String cognome) {
        this.cognome.set(cognome);
    }

    public void setTelefono(String telefono) {
        this.telefono.set(telefono);
    }

    public void setGenitore(String genitore) {
        this.genitore.set(genitore);
    }

    public void setAcconto(double acconto) {
        this.acconto.set(acconto);
    }

    public void setStatoAcconto(String statoAcconto) {
        this.statoAcconto.set(statoAcconto);
    }

    public void setTagliaMaglietta(String tagliaMaglietta) {
        this.tagliaMaglietta.set(tagliaMaglietta);
    }

    public String getTagliaMaglietta() {
        return tagliaMaglietta.get();
    }

    public String getNome() {
        return nome.get();
    }

    public String getCognome() {
        return cognome.get();
    }

    public String getTelefono() {
        return telefono.get();
    }

    public String getGenitore() {
        return genitore.get();
    }

    public String getStatoAcconto() {
        return statoAcconto.get();
    }

    // Metodi per le proprietà osservabili
    public StringProperty nomeProperty() {
        return nome;
    }

    public StringProperty cognomeProperty() {
        return cognome;
    }

    public StringProperty telefonoProperty() {
        return telefono;
    }

    public StringProperty genitoreProperty() {
        return genitore;
    }

    public StringProperty statoAccontoProperty() {
        return statoAcconto;
    }

    public DoubleProperty accontoProperty() {
        return acconto;
    }

    public StringProperty tagliaMagliettaProperty() {
        return tagliaMaglietta;
    }

    public double getAcconto() {
        return acconto.get();
    }
}
