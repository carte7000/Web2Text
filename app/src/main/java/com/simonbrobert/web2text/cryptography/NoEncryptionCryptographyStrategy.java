package com.simonbrobert.web2text.cryptography;

import com.simonbrobert.web2text.CryptographyStrategy;

/**
 * Created by Simon on 2015-08-30.
 */
public class NoEncryptionCryptographyStrategy implements CryptographyStrategy {
    @Override
    public String encrypt(String message) {
        return message;
    }

    @Override
    public String decrypt(String message) {
        return message;
    }
}
