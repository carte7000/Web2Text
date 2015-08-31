package com.simonbrobert.web2text.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

import com.simonbrobert.web2text.CryptographyStrategy;
import com.simonbrobert.web2text.OnUserSendSMSStrategy;
import com.simonbrobert.web2text.auth.StandardLoginHandler;
import com.simonbrobert.web2text.domain.Message;
import com.simonbrobert.web2text.OnReceivedSMSStrategy;
import com.simonbrobert.web2text.SmsAdapter;
import com.simonbrobert.web2text.domain.MessageBuilder;
import com.simonbrobert.web2text.serviceLocator.ServiceLocator;
import com.simonbrobert.web2text.util.Epoch;

import java.net.ContentHandlerFactory;

/**
 * Created by Simon on 2015-08-22.
 */
public class StandardSmsAdapter implements SmsAdapter {

    private static OnReceivedSMSStrategy onReceivedSMSStrategy;
    private static CryptographyStrategy cryptographyStrategy = null;
    private OnUserSendSMSStrategy onUserSendSMSStrategy;
    private final Context context;
    private String lastMessageId = "";

    public StandardSmsAdapter(Context context, CryptographyStrategy cryptographyStrategy) {
        this.context = context;
        this.cryptographyStrategy = cryptographyStrategy;
        registerContentObserver();
    }

    private void registerContentObserver() {
        context.getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, new SentSMSObserver(new Handler()));
    }

    @Override
    public void addOnReceivedSMSStrategy(OnReceivedSMSStrategy onReceivedSMSStrategy){
        this.onReceivedSMSStrategy = onReceivedSMSStrategy;
    }

    @Override
    public void addOnUserSendSMSStrategy(OnUserSendSMSStrategy onUserSendSMSStrategy) {
        this.onUserSendSMSStrategy = onUserSendSMSStrategy;
    }

    @Override
    public void sendSMS(Message message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(message.receiverNumber, null, message.content, null, null);
    }

    public static class SmsListener extends BroadcastReceiver {

        private SharedPreferences preferences;

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
                Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                SmsMessage[] msgs = null;
                String msg_from = "";
                if (bundle != null){
                    //---retrieve the SMS message received---
                    try{
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        String totalBody = "";
                        for(int i=0; i<msgs.length; i++){
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            msg_from = msgs[i].getOriginatingAddress();
                            String msgBody = msgs[i].getMessageBody();
                            totalBody += msgBody;
                        }
                        MessageBuilder builder = ServiceLocator.getInstance().fetch(MessageBuilder.class);
                        Message message = builder.createReceivedMessage(msg_from, totalBody);
                        onReceivedSMSStrategy.MessageRecieved(message);
                    }catch(Exception e){
//                            Log.d("Exception caught",e.getMessage());
                    }
                }
            }
        }
    }

    private class SentSMSObserver extends ContentObserver{

        public SentSMSObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            // save the message to the SD card here
            Uri uriSMSURI = Uri.parse("content://sms");
            Cursor cur = context.getContentResolver().query(uriSMSURI, null, null, null, null);
            // this will make it point to the first record, which is the last SMS sent
            cur.moveToNext();
            if(cur.getInt(cur.getColumnIndex("type")) == 2) {
                if(isNotLastMessage(cur.getString(cur.getColumnIndex("_id")))) {
                    lastMessageId = cur.getString(cur.getColumnIndex("_id"));
                    String content = cur.getString(cur.getColumnIndex("body"));
                    String phoneNumber = cur.getString(cur.getColumnIndex("address"));
                    MessageBuilder builder = ServiceLocator.getInstance().fetch(MessageBuilder.class);
                    Message message = builder.createSentSMS(phoneNumber, content, "phone", true);
                    onUserSendSMSStrategy.MessageSent(message);
                }
            }
        }

        private boolean isNotLastMessage(String id) {
            if(lastMessageId.equals("")) {
                return true;
            }else {
                if (id.equals(lastMessageId)) {
                    return false;
                }else {
                    return true;
                }
            }
        }
    }

}
