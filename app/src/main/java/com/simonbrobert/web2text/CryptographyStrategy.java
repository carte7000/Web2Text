package com.simonbrobert.web2text;

/**
 * Created by Simon on 2015-08-30.
 */
public interface CryptographyStrategy {
    public String encrypt(String message) throws Throwable;
    public String decrypt(String message) throws Throwable;
}
