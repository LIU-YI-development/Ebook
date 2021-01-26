package com.alim.ebook.Models;

public class Comment {
    private String bookId;
    private String uid;
    private String userName;
    private float rating;
    private String comment;
    private long timeStamp = System.currentTimeMillis();

    public Comment() { }

    public String getBookId() {
        return bookId;
    }

    public Comment setBookId(String bookId) {
        this.bookId = bookId;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public Comment setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public Comment setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public float getRating() {
        return rating;
    }

    public Comment setRating(float rating) {
        this.rating = rating;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public Comment setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public Comment setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }
}
