package com.example.dsa;

import java.nio.file.Path;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
    private Button validate;
    @FXML
    private Button sifrsa;
    @FXML
    private Button ButtonGen;
    @FXML
    private Button tozip;
    @FXML
    private TextArea filePath;
    @FXML
    private TextArea tASHA;
    @FXML
    private TextArea tABase64;
    @FXML
    private TextArea tApub;
    @FXML
    private TextArea tApriv;
    @FXML
    private TextArea tApubRSA;
    @FXML
    private TextArea tAprivRSA;
    @FXML
    private TextArea tAsifhash;
    @FXML
    private Text message;

    @FXML
    private TextField tfn;
    @FXML
    private TextField tfe;
    @FXML
    private TextField tfd;

    public String privpomoc;

    @FXML
    void initialize(){
        filePath.setWrapText(true);
        tASHA.setWrapText(true);
        tApub.setWrapText(true);
        tApriv.setWrapText(true);
        tABase64.setWrapText(true);
        tAsifhash.setWrapText(true);


    }

    File selectedFile;

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
        fileChooser.setInitialDirectory(new File("C:\\Users\\filip\\IdeaProjects\\DSA\\txtfiles"));

        // Show the file chooser dialog
        selectedFile = fileChooser.showOpenDialog(ButtonFile.getScene().getWindow());

        // Handle the selected file
        if (selectedFile != null) {
            // File was selected
            String path = selectedFile.getAbsolutePath();

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
        tABase64.setText(Base64.getEncoder().encodeToString(brain.FileToHashpole(brain.getFile())));

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
        pubFileChooser.setInitialDirectory(new File("C:\\Users\\filip\\IdeaProjects\\DSA\\txtfiles"));

        // Show the file chooser dialog
        File selectedFile = pubFileChooser.showOpenDialog(buttonver.getScene().getWindow());

        // Handle the selected file
        if (selectedFile != null) {
            // File was selected
            String path = selectedFile.getAbsolutePath();


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
        tApub.setText(content);
        tApubRSA.setText("RSA "+content);
        brain.fromBase64returenN("RSA "+content);
        brain.fromBase64returenE("RSA "+content);

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
        privFileChooser.setInitialDirectory(new File("C:\\Users\\filip\\IdeaProjects\\DSA\\txtfiles"));

        // Show the file chooser dialog
        File selectedFile = privFileChooser.showOpenDialog(buttonsuk.getScene().getWindow());

        // Handle the selected file
        if (selectedFile != null) {
            // File was selected
            String path = selectedFile.getAbsolutePath();

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
        tApriv.setText(content);
        tAprivRSA.setText("RSA "+content);
        this.privpomoc = content;
        brain.fromBase64returenN("RSA "+content);
        brain.fromBase64returenD("RSA "+content);

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
        String inputpub = rain.n.toString()+':'+rain.e.toString();
        String inputpriv = rain.n.toString()+':'+rain.d.toString();
        byte[] inputBytespub = inputpub.getBytes(StandardCharsets.UTF_8);
        byte[] inputBytespriv = inputpriv.getBytes(StandardCharsets.UTF_8);

        // Encode bytes to Base64
        String encodedStringpub = Base64.getEncoder().encodeToString(inputBytespub);
        String encodedStringpriv = Base64.getEncoder().encodeToString(inputBytespriv);
        tApubRSA.setText("RSA "+encodedStringpub);
        tAprivRSA.setText("RSA "+encodedStringpriv);
        brain.fromBase64returenN("RSA "+encodedStringpriv);
        brain.fromBase64returenD("RSA "+encodedStringpriv);
        brain.fromBase64returenE(tApubRSA.getText());


    }
    @FXML
    protected void handlesifrsa(){

        String inputpriv = brain.fromBase64returenN(tAprivRSA.getText()).toString()+":"+brain.fromBase64returenD(tAprivRSA.getText()).toString();
        byte[] inputBytespriv = inputpriv.getBytes(StandardCharsets.UTF_8);
        String encodedStringpriv = Base64.getEncoder().encodeToString(inputBytespriv);
        tAsifhash.setText(sifrujhash(tABase64.getText(),brain.fromBase64returenD("RSA "+encodedStringpriv),brain.fromBase64returenN("RSA "+encodedStringpriv) ));


    }

    public String sifrujhash(String input, BigInteger privatny, BigInteger n) {
        List<Long> list ;
        list = rain.convertToDecimalChunks(input);

        return rain.encryptMessage(list,privatny,n).toString();
    }

    @FXML
    protected void handletozip() {
        File signFile = new File(selectedFile.getParent(), "Signature.sign");
        File zipFile = new File(selectedFile.getParent(), "To_Validate.zip");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(signFile))) {
            writer.write("RSA_SHA3-512 " + Base64.getEncoder().encodeToString(tAsifhash.getText().getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            addFileToZip(signFile, zos);
            addFileToZip(selectedFile, zos);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            signFile.delete();
        }
        message.setText("To_Validate.zip bol vytvorený");
    }

    private void addFileToZip(File file, ZipOutputStream zos) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        }
    }
    @FXML
    protected void handlevalidate() {
        FileChooser zipChooser = new FileChooser();
        zipChooser.setTitle("Select Zip File to Validate");
        zipChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Zip Files", "*.zip")
        );
        zipChooser.setInitialDirectory(new File("C:\\Users\\filip\\IdeaProjects\\DSA\\txtfiles"));

        File zipFile = zipChooser.showOpenDialog(validate.getScene().getWindow());
        if (zipFile == null) return;

        try {
            // Create a temporary directory to extract files
            Path tempDir = Files.createTempDirectory("validation");

            // Extract files from zip
            java.util.zip.ZipFile zip = new java.util.zip.ZipFile(zipFile);
            File extractedFile = null;
            File signatureFile = null;

            for (ZipEntry entry : Collections.list(zip.entries())) {
                Path targetPath = tempDir.resolve(entry.getName());
                Files.copy(zip.getInputStream(entry), targetPath);

                if (entry.getName().endsWith(".sign")) {
                    signatureFile = targetPath.toFile();
                } else {
                    extractedFile = targetPath.toFile();
                }
            }
            zip.close();

            if (signatureFile == null || extractedFile == null) {
                message.setText("Invalid zip file structure");
                return;
            }

            // Read signature file
            String signatureContent = Files.readString(signatureFile.toPath());
            if (!signatureContent.startsWith("RSA_SHA3-512 ")) {
                message.setText("Invalid signature format");
                return;
            }

            // Calculate hash of the extracted file
            brain.setFile(extractedFile);
            String fileBase64Hash = Base64.getEncoder().encodeToString(brain.FileToHashpole(extractedFile));
            List<Long> fileHashChunks = rain.convertToDecimalChunks(fileBase64Hash);

            // Get public key components
            BigInteger n = brain.fromBase64returenN(tApubRSA.getText());
            BigInteger e = brain.fromBase64returenE(tApubRSA.getText());

            // Extract and decode the signature
            String base64Signature = signatureContent.substring("RSA_SHA3-512 ".length());
            byte[] signatureBytes = Base64.getDecoder().decode(base64Signature);
            String signatureText = new String(signatureBytes, StandardCharsets.UTF_8);

            // Parse the signature into BigInteger list (removing brackets and splitting)
            List<BigInteger> signatureValues = Arrays.stream(signatureText
                            .substring(1, signatureText.length() - 1)
                            .split(", "))
                    .map(String::trim)
                    .map(BigInteger::new)
                    .collect(Collectors.toList());

            // Verify each chunk individually
            List<Long> decryptedChunks = new ArrayList<>();
            for (BigInteger signatureValue : signatureValues) {
                // This is the proper RSA signature verification: signature^e mod n
                BigInteger decryptedValue = signatureValue.modPow(e, n);
                decryptedChunks.add(decryptedValue.longValue());
            }

            boolean isValid = fileHashChunks.equals(decryptedChunks);

            if (isValid) {
                message.setText("Podpis je správny");
            } else {
                message.setText("WARNING: Nesprávny podpis");
            }

            // Clean up temporary files
            Files.walk(tempDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);

        } catch (IOException | NoSuchAlgorithmException e) {
            message.setText("WARNING: Nesprávny podpis");

        }
    }







}