package com.simonbrobert.web2text.store;

import android.os.Bundle;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.google.android.gms.gcm.GcmListenerService;
import com.simonbrobert.web2text.CryptographyStrategy;
import com.simonbrobert.web2text.domain.MessageBuilder;
import com.simonbrobert.web2text.serviceLocator.ServiceLocator;
import com.simonbrobert.web2text.util.Epoch;
import com.simonbrobert.web2text.domain.Message;
import com.simonbrobert.web2text.OnNewStoreMessageStrategy;
import com.simonbrobert.web2text.Store;

/**
 * Created by Simon on 2015-08-22.
 */
public class FirebaseStore implements Store {

    private static OnNewStoreMessageStrategy strategy;
    private static CryptographyStrategy cryptographyStrategy;
    private Firebase ref;
    private static Long lastReceivedMessageEpoch;

    public FirebaseStore(Firebase ref, CryptographyStrategy cryptographyStrategy){
        this.ref = ref;
        this.cryptographyStrategy = cryptographyStrategy;
    }

    public void addNewStoreMessageStrategy(OnNewStoreMessageStrategy strategy){
        this.strategy = strategy;
    }

    public boolean secondSinceLastReceivedMessage(int second){
        return ((Epoch.getCurrentEpoch() - lastReceivedMessageEpoch) / 1000) > second;
    }

    @Override
    public void saveMessage(Message message) {
        //.child(message.getDistantNumber())
        try {
            message.content = cryptographyStrategy.encrypt(message.content);
        } catch (Throwable throwable) {
            message.content = null;
        }
        ref.child("conversations").child(message.getDistantNumber()).child("messages").push().setValue(message);
    }

    @Override
    public void saveRegistrationId(String id) {
        ref.child("gcmId").setValue(id);
    }

    public static class DefaultGCMReceiver extends GcmListenerService {
        private static final String TAG = "DefaultGCMReceiver";

        @Override
        public void onMessageReceived(String from, Bundle data) {
            String content = data.getString("content");
            String receiverNumber = data.getString("receiverNumber");
            String source = data.getString("source");
            MessageBuilder builder = ServiceLocator.getInstance().fetch(MessageBuilder.class);
            Message message = builder.createSentSMS(receiverNumber, content, source, false);
            lastReceivedMessageEpoch = Epoch.getCurrentEpoch();
            if(strategy != null) {
                try {
                    message.content = cryptographyStrategy.decrypt(message.content);
                } catch (Throwable throwable) {
                    message.content = null;
                }
                strategy.newMessageInStore(message);
            }
        }
    }
}
