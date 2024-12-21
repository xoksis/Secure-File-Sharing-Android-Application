package com.example.securefiletransfer.file_share.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

public class MyEncryptionClass {

    public static void encryptionAES(File file, String keys) {
        try {
            Cipher cipher;
            cipher = Cipher.getInstance(keys);
            KeyGenerator keyg = KeyGenerator.getInstance("DES");
            Key key = keyg.generateKey();
            cipher.init(Cipher.ENCRYPT_MODE, key);
            CipherInputStream cis = new CipherInputStream(new FileInputStream(file), cipher);
            FileOutputStream fos = new FileOutputStream(file);
            int i;
            while ((i = cis.read()) != -1) {
                fos.write(i);
            }

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void encryptionDES(File file, String keys) {
        try {
            Cipher cipher;
            cipher = Cipher.getInstance(keys);
            KeyGenerator keyg = KeyGenerator.getInstance("DES");
            Key key = keyg.generateKey();
            cipher.init(Cipher.ENCRYPT_MODE, key);
            CipherInputStream cis = new CipherInputStream(new FileInputStream(file), cipher);
            FileOutputStream fos = new FileOutputStream(file);
            int i;
            while ((i = cis.read()) != -1) {
                fos.write(i);
            }


        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decryptionAES(File file, String keys) throws FileNotFoundException {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(keys);
            KeyGenerator keyg = KeyGenerator.getInstance(keys);
            Key key = keyg.generateKey();
            cipher.init(Cipher.DECRYPT_MODE, key);
            CipherInputStream ciss = new CipherInputStream(new FileInputStream(file), cipher);
            FileOutputStream foss = null;
            foss = new FileOutputStream(new File(file.getAbsolutePath() + "/decrypt.pdf"));

            int j = 0;
            while (true) {
                try {
                    if ((j = ciss.read()) == -1) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                foss.write(j);
            }
        } catch (Exception e) {

        }


    }

    public static void decryptionDES(File file, String keys) throws FileNotFoundException {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(keys);
            KeyGenerator keyg = KeyGenerator.getInstance(keys);
            Key key = keyg.generateKey();
            cipher.init(Cipher.DECRYPT_MODE, key);
            CipherInputStream ciss = new CipherInputStream(new FileInputStream(file), cipher);
            FileOutputStream foss = null;
            foss = new FileOutputStream(new File(file.getAbsolutePath() + "/decrypt.pdf"));

            int j = 0;
            while (true) {
                try {
                    if ((j = ciss.read()) == -1) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                foss.write(j);
            }
        } catch (Exception e) {

        }


    }
}
