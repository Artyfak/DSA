package com.example.dsa;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Base64;

public class DSABrain {

    private File file;
    private RSABrain rsa;

    public DSABrain() {


    };

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public String FileToHash(File input) throws NoSuchAlgorithmException {
        MessageDigest sha3 = MessageDigest.getInstance("SHA3-512");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                sha3.update(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Generate the hash
        byte[] hashBytes = sha3.digest();

        // Convert the byte array to a hexadecimal string
        System.out.println("Hashbytes = "+Arrays.toString(hashBytes));
        System.out.println("Base64 = "+Base64.getEncoder().encodeToString(hashBytes));
        return Base64.getEncoder().encodeToString(hashBytes);

    }


}
