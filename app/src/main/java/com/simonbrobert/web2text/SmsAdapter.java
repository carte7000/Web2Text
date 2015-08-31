package com.simonbrobert.web2text;

import com.simonbrobert.web2text.domain.Message;

/**
 * Created by Simon on 2015-08-21.
 */
public interface SmsAdapter {
    public void sendSMS(Message message);
    public void addOnReceivedSMSStrategy(OnReceivedSMSStrategy strategy);
    public void addOnUserSendSMSStrategy(OnUserSendSMSStrategy strategy);
}
