package com.simonbrobert.web2text.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.simonbrobert.web2text.AuthToken;
import com.simonbrobert.web2text.AuthenticationProvider;
import com.simonbrobert.web2text.LoginHandler;
import com.simonbrobert.web2text.LogoutHandler;
import com.simonbrobert.web2text.Web2TextPreferences;

import java.util.ArrayList;

/**
 * Created by Simon on 2015-08-25.
 */
public class FirebaseAuthenticationProvider implements AuthenticationProvider {

    private Firebase ref;
    private Context context;
    private ArrayList<LoginHandler> loginHandler = new ArrayList<LoginHandler>();
    private ArrayList<LogoutHandler> logoutHandler = new ArrayList<LogoutHandler>();

    public FirebaseAuthenticationProvider(Firebase ref, Context context){
        this.context = context;
        this.ref = ref;
    }

    public void doSimpleLogin(String email, String password) {
        ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                AuthToken token = new FirebaseAuthToken(authData, ref);

                for(LoginHandler handler : loginHandler){
                    handler.apply(token);
                }

                context.getSharedPreferences(Web2TextPreferences.PREFERENCE, Context.MODE_PRIVATE).edit().putBoolean(Web2TextPreferences.WAS_LOGGED_IN, true).commit();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                System.out.print(firebaseError.getMessage());
            }
        });
        context.getSharedPreferences(Web2TextPreferences.PREFERENCE, Context.MODE_PRIVATE).edit().putString(Web2TextPreferences.PREFERENCE_EMAIL, email).commit();
        context.getSharedPreferences(Web2TextPreferences.PREFERENCE, Context.MODE_PRIVATE).edit().putString(Web2TextPreferences.PREFERENCE_PASSWORD, password).commit();
    }

    @Override
    public void doFacebookLogin() {

    }


        @Override
    public void registerOnLoginHandler(LoginHandler loginHandler) {
        this.loginHandler.add(loginHandler);
    }

    @Override
    public void registerOnLogoutHandler(LogoutHandler logoutHandler) {
        this.logoutHandler.add(logoutHandler);
    }

    @Override
    public void registerOnErrorHandler(Exception exception) {

    }

    @Override
    public void doLogout() {
        this.ref.unauth();

        for(LogoutHandler handler : this.logoutHandler){
            handler.apply();
        }

        context.getSharedPreferences(Web2TextPreferences.PREFERENCE, Context.MODE_PRIVATE).edit().putBoolean(Web2TextPreferences.WAS_LOGGED_IN, false).commit();
    }
}
