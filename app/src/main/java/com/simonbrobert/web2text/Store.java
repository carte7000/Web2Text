package com.simonbrobert.web2text;

import com.simonbrobert.web2text.domain.Message;

/**
 * Created by Simon on 2015-08-22.
 */
public interface Store {
    public void saveMessage(Message message);
    public void saveRegistrationId(String id);
    public void addNewStoreMessageStrategy(OnNewStoreMessageStrategy strategy);
}
