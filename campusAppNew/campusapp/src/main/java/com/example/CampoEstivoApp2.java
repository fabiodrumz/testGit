package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javafx.beans.property.SimpleStringProperty;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CampoEstivoApp2 extends Application {

    private static List<Partecipante> partecipanti = new ArrayList<>();
    private TableView<Partecipante> tableView = new TableView<>();

    public static void setPartecipanti(List<Partecipante> partecipanti) {
        CampoEstivoApp2.partecipanti = partecipanti;
    }

    private static final String FILE_NAME = "partecipanti.dat";

    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage primaryStage) {
        //partecipanti = DatiPartecipantiManager.caricaDati(FILE_NAME);
        GridPane root = new GridPane();
        root.setPadding(new Insets(10));
        root.setHgap(5);
        root.setVgap(5);
        // primaryStage.setOnCloseRequest(event ->
        // DatiPartecipantiManager.salvaDati(FILE_NAME));

        // Labels e TextFields per le informazioni del partecipante
        TextField nomeField = new TextField();
        TextField cognomeField = new TextField();
        TextField telefonoField = new TextField();
        TextField genitoreField = new TextField();
        TextField accontoField = new TextField();
        TextField tagliaMagliettaField = new TextField();

        root.addRow(0, new Label("Nome:"), nomeField);
        root.addRow(1, new Label("Cognome:"), cognomeField);
        root.addRow(2, new Label("Telefono:"), telefonoField);
        root.addRow(3, new Label("Genitore:"), genitoreField);
        root.addRow(4, new Label("Acconto (€):"), accontoField);
        root.addRow(5, new Label("Taglia maglietta"), tagliaMagliettaField);

        // Bottone per aggiungere un partecipante
        Button addButton = new Button("Aggiungi");
        addButton.setOnAction(e -> {
            String nome = nomeField.getText();
            String cognome = cognomeField.getText();
            String telefono = telefonoField.getText();
            String genitore = genitoreField.getText();
            double acconto = Double.parseDouble(accontoField.getText());
            String tagliaMaglietta = tagliaMagliettaField.getText();
            Partecipante partecipante = new Partecipante(nome, cognome, telefono, genitore, acconto, tagliaMaglietta);
            partecipanti.add(partecipante);
            tableView.getItems().add(partecipante);
            aggiornaStatoAcconto(partecipante);

        });
        root.add(addButton, 1, 6);

        // Aggiungi un pulsante per la modifica
        Button modificaButton = new Button("Modifica");
        modificaButton.setOnAction(event -> {
            Partecipante selectedPartecipante = tableView.getSelectionModel().getSelectedItem();
            if (selectedPartecipante != null) {
                // Popola i campi di input con i valori del partecipante selezionato
                nomeField.setText(selectedPartecipante.getNome());
                cognomeField.setText(selectedPartecipante.getCognome());
                telefonoField.setText(selectedPartecipante.getTelefono());
                genitoreField.setText(selectedPartecipante.getGenitore());
                accontoField.setText(String.valueOf(selectedPartecipante.getAcconto()));
            }
        });

        // Aggiungi un pulsante per applicare le modifiche
        Button applicaModificheButton = new Button("Applica Modifiche");
        applicaModificheButton.setOnAction(event -> {
            Partecipante selectedPartecipante = tableView.getSelectionModel().getSelectedItem();
            if (selectedPartecipante != null) {
                // Applica le modifiche all'oggetto Partecipante selezionato
                selectedPartecipante.setNome(nomeField.getText());
                selectedPartecipante.setCognome(cognomeField.getText());
                selectedPartecipante.setTelefono(telefonoField.getText());
                selectedPartecipante.setGenitore(genitoreField.getText());
                selectedPartecipante.setAcconto(Double.parseDouble(accontoField.getText()));

                // Aggiorna la visualizzazione della tabella
                tableView.refresh();
                aggiornaStatoAcconto(selectedPartecipante);
            }

        });

        // Aggiungi un pulsante per la cancellazione
        Button cancellaButton = new Button("Cancella");
        cancellaButton.setOnAction(event -> {
            Partecipante selectedPartecipante = tableView.getSelectionModel().getSelectedItem();
            if (selectedPartecipante != null) {
                // Rimuovi il partecipante selezionato dalla lista
                partecipanti.remove(selectedPartecipante);
                // Aggiorna la visualizzazione della tabella
                tableView.getItems().remove(selectedPartecipante);
            }
        });

        root.addRow(10, modificaButton, applicaModificheButton, cancellaButton);

        // Tabella per visualizzare i partecipanti
        tableView.setEditable(true);

        TableColumn<Partecipante, String> nomeColumn = new TableColumn<>("Nome");
        nomeColumn.setCellValueFactory(cellData -> {
            String nome = cellData.getValue().getNome();
            return new SimpleStringProperty(nome);
        });

        TableColumn<Partecipante, String> cognomeColumn = new TableColumn<>("Cognome");
        cognomeColumn.setCellValueFactory(cellData -> {
            String cognome = cellData.getValue().getCognome();
            return new SimpleStringProperty(cognome);
        });

        TableColumn<Partecipante, String> telefonoColumn = new TableColumn<>("Telefono");
        telefonoColumn.setCellValueFactory(cellData -> {
            String telefono = cellData.getValue().getTelefono();
            return new SimpleStringProperty(telefono);
        });

        TableColumn<Partecipante, String> genitoreColumn = new TableColumn<>("Genitore");
        genitoreColumn.setCellValueFactory(cellData -> {
            String genitore = cellData.getValue().getGenitore();
            return new SimpleStringProperty(genitore);
        });

        TableColumn<Partecipante, String> tagliaMagliettaColumn = new TableColumn<>("Taglia Maglietta");
        tagliaMagliettaColumn.setCellValueFactory(cellData -> {
            String tagliaMaglietta = cellData.getValue().getTagliaMaglietta();
            return new SimpleStringProperty(tagliaMaglietta);
        });

        // Aggiungi le colonne alla TableView
        tableView.getColumns().addAll(nomeColumn, cognomeColumn, telefonoColumn, genitoreColumn, tagliaMagliettaColumn);

        root.addRow(7, tableView);

        // Esporta i partecipanti in un file Excel
        Button exportButton = new Button("Esporta in Excel");
        exportButton.setOnAction(e -> {
            try {
                exportToExcel();
                showAlert("Esportazione completata", "I partecipanti sono stati esportati in un file Excel.");
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert("Errore", "Si è verificato un errore durante l'esportazione.");
            }
        });
        root.add(exportButton, 1, 7);

        Scene scene = new Scene(root, 1080, 400);
        primaryStage.setTitle("Prenotazioni Campus GiFra 2024");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void aggiornaStatoAcconto(Partecipante partecipante) {
        if (partecipante.getAcconto() == 100) {
            partecipante.setStatoAcconto("Versato");
        } else {
            partecipante.setStatoAcconto("Non versato");
        }
    }

    private void exportToExcel() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salva File Excel");
        fileChooser.setInitialFileName("partecipanti.xlsx");

        // Settaggio dell'estensione del file
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("File Excel (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        // Mostra il dialogo per salvare il file
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            // Ottieni il percorso del file e il nome
            String filePath = file.getAbsolutePath();
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Partecipanti");

            // Header della tabella
            Row headerRow = sheet.createRow(0);
            String[] headers = { "Nome", "Cognome", "Telefono", "Genitore", "Acconto (€)", "Stato Acconto",
                    "Taglia Maglietta" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Contenuto della tabella
            for (int i = 0; i < partecipanti.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Partecipante partecipante = partecipanti.get(i);
                row.createCell(0).setCellValue(partecipante.getNome());
                row.createCell(1).setCellValue(partecipante.getCognome());
                row.createCell(2).setCellValue(partecipante.getTelefono());
                row.createCell(3).setCellValue(partecipante.getGenitore());
                row.createCell(4).setCellValue(partecipante.getAcconto());
                row.createCell(5).setCellValue(partecipante.getStatoAcconto());
                row.createCell(6).setCellValue(partecipante.getTagliaMaglietta());
            }

            // Salva il file Excel
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            workbook.close();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void stop() {
        DatiPartecipantiManager.salvaDati(partecipanti, FILE_NAME);
    }
    
}
