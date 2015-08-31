package com.simonbrobert.web2text;

import com.simonbrobert.web2text.domain.Message;

/**
 * Created by Simon on 2015-08-27.
 */
public interface OnUserSendSMSStrategy {
    public void MessageSent(Message message);
}
