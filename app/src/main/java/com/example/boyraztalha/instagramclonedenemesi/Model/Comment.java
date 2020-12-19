package com.example.boyraztalha.instagramclonedenemesi.Model;

public class Comment {

    String comment;
    String publisherid;

    public Comment(String comment, String publisherid) {
        this.comment = comment;
        this.publisherid = publisherid;
    }

    public Comment() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisherid() {
        return publisherid;
    }

    public void setPublisherid(String publisherid) {
        this.publisherid = publisherid;
    }
}
