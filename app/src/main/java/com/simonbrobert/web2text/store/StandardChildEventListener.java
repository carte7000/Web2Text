package com.simonbrobert.web2text.store;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.simonbrobert.web2text.domain.Message;
import com.simonbrobert.web2text.OnNewStoreMessageStrategy;

/**
 * Created by Simon on 2015-08-22.
 */
public class StandardChildEventListener implements ChildEventListener {
    private OnNewStoreMessageStrategy strategy;
    private Firebase ref;

    public StandardChildEventListener(Firebase ref, OnNewStoreMessageStrategy strategy) {
        this.strategy = strategy;
        this.ref = ref;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Message message = dataSnapshot.getValue(Message.class);
        strategy.newMessageInStore(message);
        ref.child(dataSnapshot.getKey()).child("treated").setValue(true);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
