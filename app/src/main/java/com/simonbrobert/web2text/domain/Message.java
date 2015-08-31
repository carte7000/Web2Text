package com.simonbrobert.web2text.domain;

/**
 * Created by Simon on 2015-08-21.
 */
public class Message {

    private String userPhoneNumber;

    public Message(String userPhoneNumber){
        this.userPhoneNumber = userPhoneNumber;
    }

    public String senderNumber;
    public String receiverNumber;
    public String content;
    public String source;
    public long sent_date;
    public boolean treated;

    public boolean userIsSender() {
        return true;
    }

    public String getDistantNumber(){
        return getNormalizedNumber();
    }

    private String getNormalizedNumber() {
        if(senderNumber == userPhoneNumber){
            return normalize(receiverNumber);
        }
        else{
            return normalize(senderNumber);
        }
    }

    private String normalize(String number){
        return number.substring(Math.max(number.length() - 10, 0));
    }
}
