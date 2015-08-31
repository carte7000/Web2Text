package com.simonbrobert.web2text.sms;

import com.simonbrobert.web2text.OnReceivedSMSStrategy;
import com.simonbrobert.web2text.Store;
import com.simonbrobert.web2text.domain.Message;

/**
 * Created by Simon on 2015-08-27.
 */
public class StoreSMSOnReceivedSMSStrategy implements OnReceivedSMSStrategy {

    private Store store;

    public StoreSMSOnReceivedSMSStrategy(Store store){
        this.store = store;
    }

    @Override
    public void MessageRecieved(Message message) {
        this.store.saveMessage(message);
    }
}
