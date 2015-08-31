package com.simonbrobert.web2text;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Simon on 2015-08-26.
 */
public class DefaultInstanceIdListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh(){
        Intent intent = new Intent(this, DefaultRegistrationIntentService.class);
        startService(intent);
    }
}
