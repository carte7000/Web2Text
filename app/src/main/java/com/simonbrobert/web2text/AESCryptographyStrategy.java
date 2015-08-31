package com.simonbrobert.web2text;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Simon on 2015-08-30.
 */
public class AESCryptographyStrategy implements CryptographyStrategy {

    private String key;

    public AESCryptographyStrategy(String key){
        this.key = key;
    }

    public String encrypt(String message) {
        try {
            return AESHelper.encrypt(key, message);
        } catch (Exception e) {
            return null;
        }
    }

    public String decrypt(String message) {
        try {
            return AESHelper.decrypt(key, message);
        } catch (Exception e) {
            return null;
        }
    }
}
