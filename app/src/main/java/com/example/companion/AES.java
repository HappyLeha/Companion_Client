package com.example.companion;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    private static SecretKey secretKey;
    static {
        try {
            byte[] keyBytes=new byte[]{35,80,71,-30,-119,-94,94,18,88,29,-4,-117,36,36,67,-2,-9,101,-71,108,-32,-46,-54,-5,78,20,59,56,18,-127,80,59};
            secretKey= new SecretKeySpec(keyBytes, "AES");
        }
        catch (Exception e) {

        }
    }
    public static byte[] encrypt(String message)  {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(message.getBytes());
        }
        catch (Exception e) {
           return null;
        }
    }
}
