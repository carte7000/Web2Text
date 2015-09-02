package com.simonbrobert.web2text.context;

import android.app.Activity;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.firebase.client.Firebase;
import com.simonbrobert.web2text.AuthenticationProvider;
import com.simonbrobert.web2text.Web2TextPreferences;
import com.simonbrobert.web2text.auth.FirebaseAuthenticationProvider;
import com.simonbrobert.web2text.domain.MessageBuilder;
import com.simonbrobert.web2text.serviceLocator.ServiceLocator;

/**
 * Created by Simon on 2015-08-28.
 */
public class DemoContext extends Context {
    @Override
    protected void registerServices(android.content.Context context) {
        ServiceLocator serviceLocator = ServiceLocator.getInstance();

        MessageBuilder messageBuilder = new MessageBuilder(getUserPhoneNumber(context));

        Firebase.setAndroidContext(context);

        Firebase ref = new Firebase(Web2TextPreferences.FIREBASE_REF_URL);
        AuthenticationProvider provider = new FirebaseAuthenticationProvider(ref, context);

        serviceLocator.register(MessageBuilder.class, messageBuilder);
        serviceLocator.register(AuthenticationProvider.class, provider);
    }

    private String getUserPhoneNumber(android.content.Context context){
        TelephonyManager tMgr = (TelephonyManager)context.getSystemService(android.content.Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        return mPhoneNumber;
    }

    @Override
    protected void injectData() {

    }
}
