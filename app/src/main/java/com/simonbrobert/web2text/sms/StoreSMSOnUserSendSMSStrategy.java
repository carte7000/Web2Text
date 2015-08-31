package com.simonbrobert.web2text.sms;
import com.simonbrobert.web2text.OnUserSendSMSStrategy;
import com.simonbrobert.web2text.Store;
import com.simonbrobert.web2text.domain.Message;
import com.simonbrobert.web2text.store.FirebaseStore;

/**
 * Created by Simon on 2015-08-27.
 */
public class StoreSMSOnUserSendSMSStrategy implements OnUserSendSMSStrategy {

    private Store store;

    public StoreSMSOnUserSendSMSStrategy(Store store){
        this.store = store;
    }

    @Override
    public void MessageSent(Message message) {
        if(((FirebaseStore)store).secondSinceLastReceivedMessage(10)) {
            store.saveMessage(message);
        }
    }
}
