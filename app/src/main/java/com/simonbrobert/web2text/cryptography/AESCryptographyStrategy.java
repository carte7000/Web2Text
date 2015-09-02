package com.simonbrobert.web2text.cryptography;

import com.simonbrobert.web2text.CryptographyStrategy;

import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Simon on 2015-08-30.
 */
public class AESCryptographyStrategy implements CryptographyStrategy {

    private static final String IV = "F27D5C9927726BCEFE7510B1BDD3D137";
    private static final String SALT = "3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55";


    private String key;
    private AesUtil aes;

    public AESCryptographyStrategy(String key){
        this.key = key;
        this.aes = new AesUtil(128, 2);
    }

    public String encrypt(String message) throws Throwable {
        return aes.encrypt(SALT,IV, key, message);
    }

    public String decrypt(String message) throws Throwable {
        return aes.decrypt(SALT,IV,key,message);
    }
}
