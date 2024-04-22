package com.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class DatiPartecipantiManager {

    public static void salvaDati(List<Partecipante> partecipanti, String fileName) {
        try (Writer writer = new FileWriter(fileName)) {
            Gson gson = new Gson();
            gson.toJson(partecipanti, writer);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore durante il salvataggio dei dati.");
        }
    }

    public static List<Partecipante> caricaDati(String fileName) {
        try (Reader reader = new FileReader(fileName)) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Partecipante>>(){}.getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore durante il caricamento dei dati.");
            return null;
        }
    }
}