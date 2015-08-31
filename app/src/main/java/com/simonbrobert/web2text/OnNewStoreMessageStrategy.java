package com.simonbrobert.web2text;

import com.simonbrobert.web2text.domain.Message;

/**
 * Created by Simon on 2015-08-22.
 */
public interface OnNewStoreMessageStrategy {
    public void newMessageInStore(Message message);
}
