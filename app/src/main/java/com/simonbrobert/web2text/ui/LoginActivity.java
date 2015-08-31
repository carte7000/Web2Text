package com.simonbrobert.web2text.ui;

import com.firebase.client.Firebase;
import com.simonbrobert.web2text.AuthToken;
import com.simonbrobert.web2text.AuthenticationProvider;
import com.simonbrobert.web2text.DefaultRegistrationIntentService;
import com.simonbrobert.web2text.LoginHandler;
import com.simonbrobert.web2text.LogoutHandler;
import com.simonbrobert.web2text.R;
import com.simonbrobert.web2text.SMSService;
import com.simonbrobert.web2text.Web2TextPreferences;
import com.simonbrobert.web2text.auth.FirebaseAuthenticationProvider;
import com.simonbrobert.web2text.auth.HandlerNotRegisteredException;
import com.simonbrobert.web2text.auth.StandardLoginHandler;
import com.simonbrobert.web2text.context.DemoContext;
import com.simonbrobert.web2text.serviceLocator.ServiceLocator;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends Activity {
    private AuthenticationProvider provider;
    private Button mainButton;
    private Intent gcmReceiver;
    private LoginActivity self;
    com.simonbrobert.web2text.context.Context context;
    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        self = this;
        //com.simonbrobert.web2text.context.Context context = new DemoContext();
        //context.apply(this);
        setContentView(R.layout.activity_login);
        mainButton = (Button) findViewById(R.id.dummy_button);
        mainButton.setEnabled(false);
        serviceIntent = new Intent(getApplicationContext(), SMSService.class);
        startService(serviceIntent);
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
        if(!getSharedPreferences(Web2TextPreferences.PREFERENCE, Context.MODE_PRIVATE).getBoolean(Web2TextPreferences.WAS_LOGGED_IN, false)) {
            setState("login");
        }
        else{
            setState("logout");
        }
    }

    /*@Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
    }*/

    private SMSService smsService;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            smsService = ((SMSService.LocalBinder)service).getServiceInstance();
            mainButton.setEnabled(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            smsService = null;
        }
    };


    private void setState(String state){
        if(state == "login"){
            mainButton.setOnClickListener(new ButtonLoginListener());
            mainButton.setText("Login");
        }
        if(state == "logout"){
            mainButton.setOnClickListener(new ButtonLogoutListener());
            mainButton.setText("Logout");
        }
    }

    private class ButtonLoginListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            try {
                smsService.doLogin("simrob7000@gmail.com", "test", new LoginHandler() {
                    @Override
                    public void apply(AuthToken authToken) {
                        setState("logout");
                    }
                });
            }
            catch (Exception e) {
                e.printStackTrace();
            } catch (HandlerNotRegisteredException e) {
                e.printStackTrace();
            }
        }
    }

    private class ButtonLogoutListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            smsService.doLogout(new LogoutHandler() {
                @Override
                public void apply() {
                    setState("login");
                    startService(serviceIntent);
                    bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
                }
            });
        }
    }
}
