package ua.edu.ukma.storeapp;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static ua.edu.ukma.storeapp.Key.KEY;

public class Encryption {
    static Cipher cipher;

    static {
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Can't instantiate Cipher", e);
        }
    }

    public static void main(String[] args) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        byte[] message = {1, 2, 3, 4, 6, 10, -3, 11, 1, 1, 1, 1, 1, 1, 1, 1};
        byte[] encripted = encrypt(message, KEY);
        System.out.println("Size: " + message.length);
        System.out.println("Message: " + Arrays.toString(message));
        System.out.println("Encripted: " + Arrays.toString(encripted));
        System.out.println("Encripted size: " + encripted.length);
        System.out.println("Decrypted: " + Arrays.toString(decrypt(encripted, KEY)));
    }

    public static byte[] decrypt(byte[] data, Key key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        return decrypt(data, key, 0, data.length);
    }

    public static byte[] decrypt(byte[] data, Key key, int offset, int length) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data, offset, length);
    }

    public static byte[] encrypt(byte[] data, Key key) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        return encrypt(data, key, 0, data.length);
    }

    public static byte[] encrypt(byte[] data, Key key, int offset, int length) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data, offset, length);
    }
}
