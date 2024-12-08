package com.example.dsa;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;

public class DSAController {

    DSABrain brain = new DSABrain();
    RSABrain rain = new RSABrain();
    @FXML
    private Button ButtonFile;
    @FXML
    private Button ButtonFile1;
    @FXML
    private Button buttonver;
    @FXML
    private Button buttonsuk;
    @FXML
    private Button ButtonGen;
    @FXML
    private TextArea filePath;
    @FXML
    private TextArea tASHA;
    @FXML
    private TextArea tApub;
    @FXML
    private TextArea tApriv;

    @FXML
    private TextField tfn;
    @FXML
    private TextField tfe;
    @FXML
    private TextField tfd;

    @FXML
    void initialize(){
        filePath.setWrapText(true);
        tASHA.setWrapText(true);

    }

    @FXML
    protected void handleButtonfile() {
        // Create a new FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");

        // Add file filters (optional)
        FileChooser.ExtensionFilter textFilter =
                new FileChooser.ExtensionFilter("All Files", "*.msg","*.*");

        fileChooser.getExtensionFilters().addAll(textFilter);

        // Set initial directory to user's home directory (optional)
        fileChooser.setInitialDirectory(new File("C:\\Users\\admin\\IdeaProjects\\DSA\\txtfiles"));

        // Show the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(ButtonFile.getScene().getWindow());

        // Handle the selected file
        if (selectedFile != null) {
            // File was selected
            String path = selectedFile.getAbsolutePath();
            System.out.println("Selected file: " + path);

            filePath.setText(path);
            brain.setFile(new File(path));


            // Optional: Make the TextArea non-editable to prevent user modification
            filePath.setEditable(false);

            // Optional: If you want to ensure the entire path is visible
            filePath.positionCaret(0);
        } else {
            // Optional: Clear the TextArea if no file was selected
            filePath.clear();
        }

    }

    @FXML
    protected void handleButtonfile1() throws NoSuchAlgorithmException {
        tASHA.setText(brain.FileToHash(brain.getFile()));

    }
    @FXML
    protected void handlepubk() throws IOException {
        FileChooser pubFileChooser = new FileChooser();
        pubFileChooser.setTitle("Select a File");

        // Add file filters (optional)
        FileChooser.ExtensionFilter textFilter =
                new FileChooser.ExtensionFilter("Pub Files", "*.pub");

        pubFileChooser.getExtensionFilters().addAll(textFilter);

        // Set initial directory to user's home directory (optional)
        pubFileChooser.setInitialDirectory(new File("C:\\Users\\admin\\IdeaProjects\\DSA\\txtfiles"));

        // Show the file chooser dialog
        File selectedFile = pubFileChooser.showOpenDialog(buttonver.getScene().getWindow());

        // Handle the selected file
        if (selectedFile != null) {
            // File was selected
            String path = selectedFile.getAbsolutePath();
            System.out.println("Selected file: " + path);


            // Optional: Make the TextArea non-editable to prevent user modification
            filePath.setEditable(false);

            // Optional: If you want to ensure the entire path is visible
            filePath.positionCaret(0);
        } else {
            // Optional: Clear the TextArea if no file was selected
            filePath.clear();
        }
        assert selectedFile != null;
        String content = Files.readString(Path.of(selectedFile.getAbsolutePath()));
        System.out.println(content);
        tApub.setText(content);

    }
    @FXML
    protected void handleprivk() throws IOException {
        FileChooser privFileChooser = new FileChooser();
        privFileChooser.setTitle("Select a File");

        // Add file filters (optional)
        FileChooser.ExtensionFilter textFilter =
                new FileChooser.ExtensionFilter("Priv Files", "*.priv");

        privFileChooser.getExtensionFilters().addAll(textFilter);

        // Set initial directory to user's home directory (optional)
        privFileChooser.setInitialDirectory(new File("C:\\Users\\admin\\IdeaProjects\\DSA\\txtfiles"));

        // Show the file chooser dialog
        File selectedFile = privFileChooser.showOpenDialog(buttonsuk.getScene().getWindow());

        // Handle the selected file
        if (selectedFile != null) {
            // File was selected
            String path = selectedFile.getAbsolutePath();
            System.out.println("Selected file: " + path);

            // Optional: Make the TextArea non-editable to prevent user modification
            filePath.setEditable(false);

            // Optional: If you want to ensure the entire path is visible
            filePath.positionCaret(0);
        } else {
            // Optional: Clear the TextArea if no file was selected
            filePath.clear();
        }
        assert selectedFile != null;
        String content = Files.readString(Path.of(selectedFile.getAbsolutePath()));
        System.out.println(content);
        tApriv.setText(content);

    }

    @FXML
    protected  void handleButtongen() {
        rain.p = rain.generatePrvo();
        rain.q = rain.generatePrvo();
        rain.n = rain.nasobeniePQ(rain.p, rain.q);
        rain.generateKeys();
        tfn.setText(rain.n.toString());
        tfd.setText(rain.d.toString());
        tfe.setText(rain.e.toString());
        tApub.setText(rain.n.toString()+':'+rain.e.toString());
        tApriv.setText(rain.n.toString()+':'+rain.d.toString());

    }
}