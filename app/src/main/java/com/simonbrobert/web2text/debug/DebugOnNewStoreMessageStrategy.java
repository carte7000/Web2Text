package com.simonbrobert.web2text.debug;

import com.simonbrobert.web2text.domain.Message;
import com.simonbrobert.web2text.OnNewStoreMessageStrategy;

/**
 * Created by Simon on 2015-08-22.
 */
public class DebugOnNewStoreMessageStrategy implements OnNewStoreMessageStrategy {
    @Override
    public void newMessageInStore(Message message) {
        System.out.print(message.content);
    }
}
