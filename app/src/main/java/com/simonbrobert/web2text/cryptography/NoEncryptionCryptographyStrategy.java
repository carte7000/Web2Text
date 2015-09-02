package com.simonbrobert.web2text.cryptography;

import com.simonbrobert.web2text.CryptographyStrategy;
import com.simonbrobert.web2text.domain.Message;

/**
 * Created by Simon on 2015-08-30.
 */
public class NoEncryptionCryptographyStrategy implements CryptographyStrategy {

    @Override
    public Message encrypt(Message message) throws Throwable {
        return message;
    }

    @Override
    public Message decrypt(Message message) throws Throwable {
        return message;
    }
}
