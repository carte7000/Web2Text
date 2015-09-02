package com.simonbrobert.web2text;

import com.simonbrobert.web2text.domain.Message;

/**
 * Created by Simon on 2015-08-30.
 */
public interface CryptographyStrategy {
    public Message encrypt(Message message) throws Throwable;
    public Message decrypt(Message message) throws Throwable;
}
