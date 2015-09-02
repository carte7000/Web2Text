package com.simonbrobert.web2text.cryptography;

import com.simonbrobert.web2text.domain.Message;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Simon on 2015-09-01.
 */

public class AESCryptographyStrategyTest {

    private static final String TEST_KEY = "simplelogin:2";
    private static final String TEST_MESSAGE = "test";
    private static final String CRYPTOJS_RESULT = "kDrVT87UR6tqGt7+9y76DQ==";

    AESCryptographyStrategy aesCryptographyStrategy;

    /*@Test
    public void encryptWithTestKeyAndTestMessageShouldReturnSameAsCryptoJSResult() throws Throwable {
        aesCryptographyStrategy = new AESCryptographyStrategy(TEST_KEY);
        String result = aesCryptographyStrategy.encrypt(TEST_MESSAGE);
        assertEquals(result, CRYPTOJS_RESULT);
    }*/

    @Test
    public void decryptWithTestKeyAndTestMessageShouldReturnSameAsCryptoJSResult() throws Throwable {
        Message message = new Message("");
        message.content = CRYPTOJS_RESULT;
        aesCryptographyStrategy = new AESCryptographyStrategy(TEST_KEY);
        String result = aesCryptographyStrategy.decrypt(message).content;
        assertEquals(result, TEST_MESSAGE);
    }
}
