package com.simonbrobert.web2text.auth;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.simonbrobert.web2text.AuthToken;

/**
 * Created by Simon on 2015-08-25.
 */
public class FirebaseAuthToken implements AuthToken {

    private AuthData authData;
    private Firebase ref;

    public FirebaseAuthToken(AuthData authData, Firebase ref){
        this.authData = authData;
        this.ref = ref;
    }

    @Override
    public String getUserId() {
        return authData.getUid();
    }

    @Override
    public void logout() {
        this.ref.unauth();
    }
}
