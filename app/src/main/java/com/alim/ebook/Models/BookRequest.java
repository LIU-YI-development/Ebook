package com.alim.ebook.Models;


public class BookRequest {
    private String uid;
    private String userName;
    private String userEmail;
    private String bookTitle;
    private String bookAuthor;
    private String bookTranslator;
    private String bookLanguage;

    public BookRequest() {
    }

    public String getUid() {
        return uid;
    }

    public BookRequest setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public BookRequest setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public BookRequest setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
        return this;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public BookRequest setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
        return this;
    }

    public String getBookTranslator() {
        return bookTranslator;
    }

    public BookRequest setBookTranslator(String bookTranslator) {
        this.bookTranslator = bookTranslator;
        return this;
    }

    public String getBookLanguage() {
        return bookLanguage;
    }

    public BookRequest setBookLanguage(String bookLanguage) {
        this.bookLanguage = bookLanguage;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public BookRequest setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
