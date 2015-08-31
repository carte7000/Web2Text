package com.simonbrobert.web2text.domain;

import com.simonbrobert.web2text.util.Epoch;

/**
 * Created by Simon on 2015-08-27.
 */
public class MessageBuilder {

    private String MY_PHONE_NUMBER = null;

    public MessageBuilder(String userPhoneNumber){
        MY_PHONE_NUMBER = userPhoneNumber;
    }

    public Message createSentSMS(String phoneNumber, String content, String source, boolean treated){
        Message message = new Message(MY_PHONE_NUMBER);
        message.content = content;
        message.receiverNumber = phoneNumber;
        message.source = source;
        message.senderNumber = MY_PHONE_NUMBER;
        message.sent_date = Epoch.getCurrentEpoch();
        message.treated = treated;
        return message;
    }

    public Message createReceivedMessage(String msg_from, String totalBody) {
        Message message = new Message(MY_PHONE_NUMBER);
        message.receiverNumber = MY_PHONE_NUMBER;
        message.senderNumber = msg_from;
        message.content = totalBody;
        message.source = "phone";
        message.sent_date = Epoch.getCurrentEpoch();
        message.treated = true;
        return message;
    }
}
