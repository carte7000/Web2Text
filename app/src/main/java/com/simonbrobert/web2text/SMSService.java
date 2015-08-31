package com.simonbrobert.web2text;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.firebase.client.Firebase;
import com.simonbrobert.web2text.auth.FirebaseAuthenticationProvider;
import com.simonbrobert.web2text.auth.HandlerNotRegisteredException;
import com.simonbrobert.web2text.auth.StandardLoginHandler;
import com.simonbrobert.web2text.context.Context;
import com.simonbrobert.web2text.context.DemoContext;
import com.simonbrobert.web2text.debug.DebugOnReceivedSMSStrategy;
import com.simonbrobert.web2text.serviceLocator.ServiceLocator;
import com.simonbrobert.web2text.sms.StandardSmsAdapter;
import com.simonbrobert.web2text.store.FirebaseStore;
import com.simonbrobert.web2text.store.SendSMSOnNewStoreMessageStrategy;

/**
 * Created by Simon on 2015-08-22.
 */
public class SMSService extends Service {

    private AuthenticationProvider provider;
    private SMSService self;
    private Intent gcmReceiver;
    private final Binder binder = new LocalBinder();
    private ServiceLocator serviceLocator;
    com.simonbrobert.web2text.context.Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        self = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = new DemoContext();
        context.apply(getApplicationContext());

        provider = ServiceLocator.getInstance().fetch(AuthenticationProvider.class);

        provider.registerOnLoginHandler(new LoginHandler() {
            @Override
            public void apply(AuthToken authToken) {
                gcmReceiver = new Intent(self, DefaultRegistrationIntentService.class);
                startService(gcmReceiver);
                new StandardLoginHandler(getApplicationContext()).apply(authToken);
            }
        });

        if(getSharedPreferences(Web2TextPreferences.PREFERENCE, android.content.Context.MODE_PRIVATE).getBoolean(Web2TextPreferences.WAS_LOGGED_IN, false)){
            try {
                doLogin(
                        getSharedPreferences(Web2TextPreferences.PREFERENCE, android.content.Context.MODE_PRIVATE).getString(Web2TextPreferences.PREFERENCE_EMAIL, ""),
                        getSharedPreferences(Web2TextPreferences.PREFERENCE, android.content.Context.MODE_PRIVATE).getString(Web2TextPreferences.PREFERENCE_PASSWORD, ""),
                        new LoginHandler() {
                            @Override
                            public void apply(AuthToken authToken) {

                            }
                        }
                );
            } catch (HandlerNotRegisteredException e) {
                e.printStackTrace();
            }
        }

        return Service.START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void doLogin(String email, String password, LoginHandler handler) throws HandlerNotRegisteredException {
        provider.registerOnLoginHandler(handler);
        provider.doSimpleLogin(email, password);
    }

    public void doLogout(LogoutHandler handler){
        provider.registerOnLogoutHandler(handler);
        provider.doLogout();
        stopSelf();
    }

    public class LocalBinder extends Binder {
        public SMSService getServiceInstance(){
            return SMSService.this;
        }
    }
}
