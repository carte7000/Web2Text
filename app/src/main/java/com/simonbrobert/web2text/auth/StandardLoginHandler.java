package com.simonbrobert.web2text.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.firebase.client.Firebase;
import com.simonbrobert.web2text.AESCryptographyStrategy;
import com.simonbrobert.web2text.AuthToken;
import com.simonbrobert.web2text.CryptographyStrategy;
import com.simonbrobert.web2text.NoEncryptionCryptographyStrategy;
import com.simonbrobert.web2text.OnUserSendSMSStrategy;
import com.simonbrobert.web2text.Web2TextPreferences;
import com.simonbrobert.web2text.serviceLocator.ServiceLocator;
import com.simonbrobert.web2text.sms.StoreSMSOnReceivedSMSStrategy;
import com.simonbrobert.web2text.LoginHandler;
import com.simonbrobert.web2text.OnNewStoreMessageStrategy;
import com.simonbrobert.web2text.OnReceivedSMSStrategy;
import com.simonbrobert.web2text.sms.StoreSMSOnUserSendSMSStrategy;
import com.simonbrobert.web2text.store.SendSMSOnNewStoreMessageStrategy;
import com.simonbrobert.web2text.sms.StandardSmsAdapter;
import com.simonbrobert.web2text.store.FirebaseStore;

import java.util.prefs.Preferences;

/**
 * Created by Simon on 2015-08-25.
 */
public class StandardLoginHandler implements LoginHandler {
    private Context context;
    private OnReceivedSMSStrategy receivedSMSStrategy;
    private OnNewStoreMessageStrategy newStoreMessageStrategy;
    private OnUserSendSMSStrategy userSendSMSStrategy;

    public StandardLoginHandler(Context context) {
        this.context = context;
    }

    @Override
    public void apply(AuthToken authToken) {
        Firebase ref = new Firebase("https://web2text.firebaseIO.com/");
        Firebase userRef = ref.child(authToken.getUserId());
        CryptographyStrategy cryptographyStrategy = new NoEncryptionCryptographyStrategy();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        StandardSmsAdapter smsAdapter = new StandardSmsAdapter(context, cryptographyStrategy);
        FirebaseStore store = new FirebaseStore(userRef, cryptographyStrategy);

        receivedSMSStrategy = new StoreSMSOnReceivedSMSStrategy(store);
        userSendSMSStrategy = new StoreSMSOnUserSendSMSStrategy(store);
        newStoreMessageStrategy = new SendSMSOnNewStoreMessageStrategy(smsAdapter);

        store.saveRegistrationId(sharedPreferences.getString(Web2TextPreferences.REGISTRATION_ID, ""));
        store.addNewStoreMessageStrategy(newStoreMessageStrategy);
        smsAdapter.addOnReceivedSMSStrategy(receivedSMSStrategy);
        smsAdapter.addOnUserSendSMSStrategy(userSendSMSStrategy);
    }
}
