package com.simonbrobert.web2text;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.simonbrobert.web2text.serviceLocator.ServiceLocator;

import java.io.IOException;

/**
 * Created by Simon on 2015-08-26.
 */
public class DefaultRegistrationIntentService extends IntentService{

    private static final String TAG = "DefaultRegIntentService";

    public DefaultRegistrationIntentService(){
        super(TAG);
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + token);

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(sharedPreferences, token);

            // Subscribe to messages channel
            subscribeMessages(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(Web2TextPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]

        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(Web2TextPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Web2TextPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void subscribeMessages(String token) throws IOException{
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        pubSub.subscribe(token, "/topics/messages", null);
    }

    private void sendRegistrationToServer(SharedPreferences sharedPreferences, String token) {
        sharedPreferences.edit().putString(Web2TextPreferences.REGISTRATION_ID, token).apply();
    }

}
