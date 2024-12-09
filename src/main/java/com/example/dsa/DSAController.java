package com.example.dsa;

import java.nio.file.Path;
import java.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Hlavný kontrolér aplikácie pre digitálny podpis
 * Zabezpečuje interakciu medzi užívateľským rozhraním a logickou vrstvou
 * Implementuje funkcionalitu pre:
 * - Generovanie kľúčov
 * - Vytváranie digitálnych podpisov
 * - Validáciu podpisov
 * - Správu súborov
 */
public class DSAController {
    // Inicializácia komponentov pre spracovanie podpisu
    /** Hlavný objekt pre prácu s DSA algoritmom */
    DSABrain brain = new DSABrain();
    /** Objekt pre RSA šifrovanie a podpisovanie */
    RSABrain rain = new RSABrain();

    // FXML komponenty užívateľského rozhrania
    @FXML private Button ButtonFile;      // Tlačidlo pre výber súboru na podpísanie
    @FXML private Button ButtonFile1;     // Tlačidlo pre výpočet hashu súboru
    @FXML private Button buttonver;       // Tlačidlo pre verifikáciu podpisu
    @FXML private Button buttonsuk;       // Tlačidlo pre načítanie súkromného kľúča
    @FXML private Button validate;        // Tlačidlo pre validáciu podpisu
    @FXML private Button sifrsa;          // Tlačidlo pre RSA šifrovanie hashu
    @FXML private Button ButtonGen;       // Tlačidlo pre generovanie RSA kľúčového páru
    @FXML private Button tozip;          // Tlačidlo pre vytvorenie ZIP archívu s podpisom

    // Textové polia pre zobrazenie a vstup dát
    @FXML private TextArea filePath;      // Zobrazuje cestu k vybranému súboru
    @FXML private TextArea tASHA;         // Zobrazuje vypočítaný SHA-3 hash
    @FXML private TextArea tABase64;      // Zobrazuje Base64 reprezentáciu hashu
    @FXML private TextArea tApub;         // Zobrazuje verejný kľúč v čitateľnej forme
    @FXML private TextArea tApriv;        // Zobrazuje súkromný kľúč v čitateľnej forme
    @FXML private TextArea tApubRSA;      // Zobrazuje verejný kľúč v RSA formáte
    @FXML private TextArea tAprivRSA;     // Zobrazuje súkromný kľúč v RSA formáte
    @FXML private TextArea tAsifhash;     // Zobrazuje zašifrovaný hash (podpis)
    @FXML private TextArea infosub;       // Zobrazuje informácie o súbore (podpis)
    @FXML private Text message;           // Zobrazuje stavové správy pre užívateľa

    // Polia pre zobrazenie RSA parametrov
    @FXML private TextField tfn;          // Zobrazuje hodnotu modulu n
    @FXML private TextField tfe;          // Zobrazuje hodnotu verejného exponentu e
    @FXML private TextField tfd;          // Zobrazuje hodnotu súkromného exponentu d

    /** Pomocná premenná pre uloženie súkromného kľúča */
    public String privpomoc;
    /** Aktuálne vybraný súbor na podpísanie */
    File selectedFile;

    /**
     * Inicializačná metóda kontroléra
     * Nastavuje vlastnosti textových polí pri štarte aplikácie
     */
    @FXML
    void initialize() {
        // Nastavenie automatického zalamovanie textu pre všetky textové polia
        filePath.setWrapText(true);
        tASHA.setWrapText(true);
        tApub.setWrapText(true);
        tApriv.setWrapText(true);
        tABase64.setWrapText(true);
        tAsifhash.setWrapText(true);
        infosub.setWrapText(true);
    }

    /**
     * Spracováva výber súboru na podpísanie
     * Otvára dialógové okno pre výber súboru a nastavuje ho pre ďalšie spracovanie
     */
    @FXML
    protected void handleButtonfile() {
        // Vytvorenie dialógu pre výber súboru
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");

        // Nastavenie filtrov pre typy súborov
        FileChooser.ExtensionFilter textFilter =
                new FileChooser.ExtensionFilter("All Files", "*.msg","*.*");
        fileChooser.getExtensionFilters().addAll(textFilter);

        // Nastavenie počiatočného adresára
        fileChooser.setInitialDirectory(new File("C:\\Users\\filip\\IdeaProjects\\DSA\\txtfiles"));

        // Zobrazenie dialógu a spracovanie výberu
        selectedFile = fileChooser.showOpenDialog(ButtonFile.getScene().getWindow());

        if (selectedFile != null) {
            String path = selectedFile.getAbsolutePath();
            filePath.setText(path);
            brain.setFile(new File(path));
            filePath.setEditable(false);
            filePath.positionCaret(0);
        } else {
            filePath.clear();
        }
        // Získanie informácií o súbore
        String fileName = selectedFile.getName(); // Názov súboru
        String filePath = selectedFile.getAbsolutePath(); // Absolútna cesta k súboru
        String fileExtension = ""; // Typ súboru (prípona)
        int extensionIndex = fileName.lastIndexOf('.');
        if (extensionIndex > 0 && extensionIndex < fileName.length() - 1) {
            fileExtension = fileName.substring(extensionIndex + 1);
        }
        long fileSize = selectedFile.length(); // Veľkosť súboru v bajtoch
        Date lastModified = new Date(selectedFile.lastModified()); // Dátum poslednej úpravy

        // Formátovanie informácií
        String fileInfo = String.format(
                "Názov: %s\nCesta: %s\nTyp: %s\nVeľkosť: %d bajtov\nPosledná úprava: %s",
                fileName, filePath, fileExtension, fileSize, lastModified.toString()
        );

        // Zobrazenie informácií v textovom poli
        infosub.setText(fileInfo);
    }

    /**
     * Generuje hash vybraného súboru
     * Vypočíta SHA-3 hash a jeho Base64 reprezentáciu
     */
    @FXML
    protected void handleButtonfile1() throws NoSuchAlgorithmException {
        tASHA.setText(brain.FileToHash(brain.getFile()));
        tABase64.setText(Base64.getEncoder().encodeToString(brain.FileToHashpole(brain.getFile())));
    }

    /**
     * Načíta verejný kľúč zo súboru
     * Spracuje a zobrazí verejný kľúč v rôznych formátoch
     */
    @FXML
    protected void handlepubk() throws IOException {
        // Vytvorenie dialógu pre výber súboru s verejným kľúčom
        FileChooser pubFileChooser = new FileChooser();
        pubFileChooser.setTitle("Select a File");

        // Nastavenie filtru pre súbory s verejným kľúčom
        FileChooser.ExtensionFilter textFilter =
                new FileChooser.ExtensionFilter("Pub Files", "*.pub");
        pubFileChooser.getExtensionFilters().addAll(textFilter);

        // Nastavenie počiatočného adresára
        pubFileChooser.setInitialDirectory(new File("C:\\Users\\filip\\IdeaProjects\\DSA\\txtfiles"));

        // Zobrazenie dialógu a spracovanie výberu
        File selectedFile = pubFileChooser.showOpenDialog(buttonver.getScene().getWindow());

        if (selectedFile != null) {
            filePath.setEditable(false);
            filePath.positionCaret(0);
        } else {
            filePath.clear();
        }

        assert selectedFile != null;
        String content = Files.readString(Path.of(selectedFile.getAbsolutePath()));
        tApub.setText(content);
        tApubRSA.setText("RSA "+content);
        brain.fromBase64returenN("RSA "+content);
        brain.fromBase64returenE("RSA "+content);
    }

    /**
     * Načíta súkromný kľúč zo súboru
     * Spracuje a zobrazí súkromný kľúč v rôznych formátoch
     */
    @FXML
    protected void handleprivk() throws IOException {
        // Vytvorenie dialógu pre výber súboru so súkromným kľúčom
        FileChooser privFileChooser = new FileChooser();
        privFileChooser.setTitle("Select a File");

        FileChooser.ExtensionFilter textFilter =
                new FileChooser.ExtensionFilter("Priv Files", "*.priv");
        privFileChooser.getExtensionFilters().addAll(textFilter);

        privFileChooser.setInitialDirectory(new File("C:\\Users\\filip\\IdeaProjects\\DSA\\txtfiles"));

        File selectedFile = privFileChooser.showOpenDialog(buttonsuk.getScene().getWindow());

        if (selectedFile != null) {
            filePath.setEditable(false);
            filePath.positionCaret(0);
        } else {
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

    /**
     * Generuje nový pár RSA kľúčov
     * Vytvorí nové prvočísla a vypočíta všetky potrebné parametre
     */
    @FXML
    protected void handleButtongen() {
        // Generovanie základných RSA parametrov
        rain.p = rain.generatePrvo();
        rain.q = rain.generatePrvo();
        rain.n = rain.nasobeniePQ(rain.p, rain.q);
        rain.generateKeys();

        // Zobrazenie parametrov v textových poliach
        tfn.setText(rain.n.toString());
        tfd.setText(rain.d.toString());
        tfe.setText(rain.e.toString());
        tApub.setText(rain.n.toString()+':'+rain.e.toString());
        tApriv.setText(rain.n.toString()+':'+rain.d.toString());

        // Príprava kľúčov pre Base64 kódovanie
        String inputpub = rain.n.toString()+':'+rain.e.toString();
        String inputpriv = rain.n.toString()+':'+rain.d.toString();
        byte[] inputBytespub = inputpub.getBytes(StandardCharsets.UTF_8);
        byte[] inputBytespriv = inputpriv.getBytes(StandardCharsets.UTF_8);

        // Kódovanie kľúčov do Base64
        String encodedStringpub = Base64.getEncoder().encodeToString(inputBytespub);
        String encodedStringpriv = Base64.getEncoder().encodeToString(inputBytespriv);

        // Zobrazenie zakódovaných kľúčov
        tApubRSA.setText("RSA "+encodedStringpub);
        tAprivRSA.setText("RSA "+encodedStringpriv);

        // Extrakcia parametrov pre ďalšie použitie
        brain.fromBase64returenN("RSA "+encodedStringpriv);
        brain.fromBase64returenD("RSA "+encodedStringpriv);
        brain.fromBase64returenE(tApubRSA.getText());
    }

    /**
     * Vytvára digitálny podpis hashu súboru
     * Používa RSA algoritmus so súkromným kľúčom
     */
    @FXML
    protected void handlesifrsa() {
        // Príprava súkromného kľúča
        String inputpriv = brain.fromBase64returenN(tAprivRSA.getText()).toString()+":"+
                brain.fromBase64returenD(tAprivRSA.getText()).toString();
        byte[] inputBytespriv = inputpriv.getBytes(StandardCharsets.UTF_8);
        String encodedStringpriv = Base64.getEncoder().encodeToString(inputBytespriv);

        // Vytvorenie podpisu
        tAsifhash.setText(sifrujhash(tABase64.getText(),
                brain.fromBase64returenD("RSA "+encodedStringpriv),
                brain.fromBase64returenN("RSA "+encodedStringpriv)));
    }

    /**
     * Šifruje hash pomocou RSA algoritmu
     * @param input vstupný hash v Base64 formáte
     * @param privatny súkromný kľúč pre podpisovanie
     * @param n modulus RSA
     * @return podpísaný hash
     */
    public String sifrujhash(String input, BigInteger privatny, BigInteger n) {
        List<Long> list = rain.convertToDecimalChunks(input);
        return rain.encryptMessage(list,privatny,n).toString();
    }

    /**
     * Vytvorí ZIP archív obsahujúci originálny súbor a jeho digitálny podpis
     * Vytvorí dočasný súbor podpisu a pridá ho spolu s originálnym súborom do ZIP archívu
     * Po dokončení odstráni dočasný súbor podpisu
     */
    @FXML
    protected void handletozip() {
        // Vytvorenie cesty pre súbor podpisu a výsledný ZIP
        File signFile = new File(selectedFile.getParent(), "Signature.sign");
        File zipFile = new File(selectedFile.getParent(), "To_Validate.zip");

        // Vytvorenie súboru s podpisom
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(signFile))) {
            writer.write("RSA_SHA3-512 " + Base64.getEncoder().encodeToString(tAsifhash.getText().getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Vytvorenie ZIP archívu a pridanie súborov
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            addFileToZip(signFile, zos);     // Pridanie súboru podpisu
            addFileToZip(selectedFile, zos);  // Pridanie originálneho súboru

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            signFile.delete();  // Odstránenie dočasného súboru podpisu
        }
        message.setText("To_Validate.zip bol vytvorený");
    }

    /**
     * Pomocná metóda pre pridanie súboru do ZIP archívu
     * @param file Súbor, ktorý má byť pridaný do archívu
     * @param zos ZIP výstupný prúd pre zápis
     * @throws IOException Pri chybe zápisu do ZIP archívu
     */
    private void addFileToZip(File file, ZipOutputStream zos) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zos.putNextEntry(zipEntry);

            // Kopírovanie obsahu súboru do ZIP archívu po blokoch
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        }
    }

    /**
     * Validuje digitálny podpis v ZIP archíve
     * Proces validácie:
     * 1. Rozbalí ZIP archív
     * 2. Načíta originálny súbor a jeho podpis
     * 3. Overí podpis pomocou verejného kľúča
     * 4. Vyčistí dočasné súbory
     */
    @FXML
    protected void handlevalidate() {
        // Vytvorenie dialógu pre výber ZIP súboru
        FileChooser zipChooser = new FileChooser();
        zipChooser.setTitle("Select Zip File to Validate");
        zipChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Zip Files", "*.zip")
        );
        zipChooser.setInitialDirectory(new File("C:\\Users\\filip\\IdeaProjects\\DSA\\txtfiles"));

        File zipFile = zipChooser.showOpenDialog(validate.getScene().getWindow());
        if (zipFile == null) return;

        try {
            // Vytvorenie dočasného adresára pre rozbalenie súborov
            Path tempDir = Files.createTempDirectory("validation");

            // Rozbalenie ZIP archívu
            java.util.zip.ZipFile zip = new java.util.zip.ZipFile(zipFile);
            File extractedFile = null;    // Originálny súbor
            File signatureFile = null;    // Súbor s podpisom

            // Rozbalenie všetkých súborov z archívu
            for (ZipEntry entry : Collections.list(zip.entries())) {
                Path targetPath = tempDir.resolve(entry.getName());
                Files.copy(zip.getInputStream(entry), targetPath);

                // Identifikácia súborov podľa prípony
                if (entry.getName().endsWith(".sign")) {
                    signatureFile = targetPath.toFile();
                } else {
                    extractedFile = targetPath.toFile();
                }
            }
            zip.close();

            // Kontrola prítomnosti potrebných súborov
            if (signatureFile == null || extractedFile == null) {
                message.setText("Invalid zip file structure");
                return;
            }

            // Načítanie a kontrola formátu podpisu
            String signatureContent = Files.readString(signatureFile.toPath());
            if (!signatureContent.startsWith("RSA_SHA3-512 ")) {
                message.setText("Invalid signature format");
                return;
            }

            // Výpočet hashu originálneho súboru
            brain.setFile(extractedFile);
            String fileBase64Hash = Base64.getEncoder().encodeToString(brain.FileToHashpole(extractedFile));
            List<Long> fileHashChunks = rain.convertToDecimalChunks(fileBase64Hash);

            // Získanie komponentov verejného kľúča
            BigInteger n = brain.fromBase64returenN(tApubRSA.getText());
            BigInteger e = brain.fromBase64returenE(tApubRSA.getText());

            // Dekódovanie podpisu
            String base64Signature = signatureContent.substring("RSA_SHA3-512 ".length());
            byte[] signatureBytes = Base64.getDecoder().decode(base64Signature);
            String signatureText = new String(signatureBytes, StandardCharsets.UTF_8);

            // Parsovanie podpisu do zoznamu BigInteger hodnôt
            List<BigInteger> signatureValues = Arrays.stream(signatureText
                            .substring(1, signatureText.length() - 1)
                            .split(", "))
                    .map(String::trim)
                    .map(BigInteger::new)
                    .collect(Collectors.toList());

            // Verifikácia každej časti podpisu samostatne
            List<Long> decryptedChunks = new ArrayList<>();
            for (BigInteger signatureValue : signatureValues) {
                // RSA verifikácia podpisu: podpis^e mod n
                BigInteger decryptedValue = signatureValue.modPow(e, n);
                decryptedChunks.add(decryptedValue.longValue());
            }

            // Porovnanie pôvodného hashu s dešifrovaným podpisom
            boolean isValid = fileHashChunks.equals(decryptedChunks);

            // Zobrazenie výsledku validácie
            if (isValid) {
                message.setText("Podpis je správny");
            } else {
                message.setText("WARNING: Nesprávny podpis");
            }

            // Vyčistenie dočasných súborov
            Files.walk(tempDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);

        } catch (IOException | NoSuchAlgorithmException e) {
            message.setText("WARNING: Nesprávny podpis");
        }
    }

}