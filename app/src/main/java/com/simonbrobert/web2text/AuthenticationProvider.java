package com.simonbrobert.web2text;

import com.simonbrobert.web2text.auth.HandlerNotRegisteredException;

/**
 * Created by Simon on 2015-08-25.
 */
public interface AuthenticationProvider {
    public void doFacebookLogin();
    public void doSimpleLogin(String email, String password) throws HandlerNotRegisteredException;
    public void registerOnLoginHandler(LoginHandler loginHandler);
    public void registerOnLogoutHandler(LogoutHandler logoutHandler);
    public void registerOnErrorHandler(Exception exception);
    public void doLogout();
}
