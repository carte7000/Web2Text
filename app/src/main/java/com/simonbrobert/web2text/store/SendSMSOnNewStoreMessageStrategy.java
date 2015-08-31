package com.simonbrobert.web2text.store;

import com.simonbrobert.web2text.OnNewStoreMessageStrategy;
import com.simonbrobert.web2text.SmsAdapter;
import com.simonbrobert.web2text.domain.Message;

/**
 * Created by Simon on 2015-08-24.
 */
public class SendSMSOnNewStoreMessageStrategy implements OnNewStoreMessageStrategy {

    private SmsAdapter smsAdapter;

    public SendSMSOnNewStoreMessageStrategy(SmsAdapter smsAdapter){
        this.smsAdapter = smsAdapter;
    }

    @Override
    public void newMessageInStore(Message message) {
        if(message.treated)
            return;

        if(message.userIsSender()) {
            smsAdapter.sendSMS(message);
        }
    }
}
