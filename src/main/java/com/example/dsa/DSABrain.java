package com.example.dsa;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class DSABrain {

    private File file;
    private RSABrain rsa;

    // Konstruktor triedy DSABrain
    public DSABrain() {

    };

    // Nastavenie objektu File
    public void setFile(File file) {
        this.file = file;
    }

    // Získanie aktuálneho objektu File
    public File getFile() {
        return file;
    }

    // Prevod súboru na SHA3-512 hash vo forme reťazca
    public String FileToHash(File input) throws NoSuchAlgorithmException {
        MessageDigest sha3 = MessageDigest.getInstance("SHA3-512");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            // Čítanie súboru po blokoch a aktualizácia hashu
            while ((bytesRead = fis.read(buffer)) != -1) {
                sha3.update(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Generovanie výsledného hash
        byte[] hashBytes = sha3.digest();

        return Arrays.toString(hashBytes);
        // Alternatívne: Base64 kódovanie hashu
        // return Base64.getEncoder().encodeToString(hashBytes);
    }

    // Prevod súboru na SHA3-512 hash vo forme bajtového poľa
    public byte[] FileToHashpole(File input) throws NoSuchAlgorithmException {
        MessageDigest sha3 = MessageDigest.getInstance("SHA3-512");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            // Čítanie súboru po blokoch a aktualizácia hashu
            while ((bytesRead = fis.read(buffer)) != -1) {
                sha3.update(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Generovanie výsledného hash
        byte[] hashBytes = sha3.digest();
        return hashBytes;
    }

    // Dekódovanie Base64 a získanie hodnoty 'N' z RSA kľúča
    public BigInteger fromBase64returenN(String input){
        // Odstránenie prefixu "RSA " z reťazca
        String minusRSA = input.replaceAll("RSA ", "");
        // Dekódovanie Base64 reťazca
        String decodedString = new String(Base64.getDecoder().decode(minusRSA), StandardCharsets.UTF_8);

        // Rozdelenie reťazca a návrat prvej časti ako BigInteger
        return new BigInteger(decodedString.split(":")[0]);
    }

    // Dekódovanie Base64 a získanie hodnoty 'D' z RSA kľúča
    public BigInteger fromBase64returenD(String input){
        // Odstránenie prefixu "RSA " z reťazca
        String minusRSA = input.replaceAll("RSA ", "");
        // Dekódovanie Base64 reťazca
        String decodedString = new String(Base64.getDecoder().decode(minusRSA), StandardCharsets.UTF_8);

        // Rozdelenie reťazca a návrat druhej časti ako BigInteger
        return new BigInteger(decodedString.split(":")[1]);
    }

    // Dekódovanie Base64 a získanie hodnoty 'E' z RSA kľúča
    public BigInteger fromBase64returenE(String input) {
        // Odstránenie prefixu "RSA " z reťazca
        String minusRSA = input.replaceAll("RSA ", "");
        // Dekódovanie Base64 reťazca
        String decodedString = new String(Base64.getDecoder().decode(minusRSA), StandardCharsets.UTF_8);

        // Rozdelenie reťazca a návrat druhej časti ako BigInteger
        return new BigInteger(decodedString.split(":")[1]);

    }

}
