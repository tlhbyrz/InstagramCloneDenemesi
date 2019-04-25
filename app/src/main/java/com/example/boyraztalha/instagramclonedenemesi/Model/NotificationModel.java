package com.example.boyraztalha.instagramclonedenemesi.Model;

public class NotificationModel {

    String userid;
    String text;
    String postd;
    boolean ispost;

    public NotificationModel(String userid, String text, String postd, boolean ispost) {

        this.userid = userid;
        this.text = text;
        this.postd = postd;
        this.ispost = ispost;
    }

    public NotificationModel() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostd() {
        return postd;
    }

    public void setPostd(String postd) {
        this.postd = postd;
    }

    public boolean isIspost() {
        return ispost;
    }

    public void setIspost(boolean ispost) {
        this.ispost = ispost;
    }


}
