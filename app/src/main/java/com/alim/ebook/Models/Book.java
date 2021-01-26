package com.alim.ebook.Models;

import androidx.annotation.Keep;

import java.util.ArrayList;


public class Book {
    private String authorId;
    private String bookThumbnail;
    private String bookTitle;
    private String bookDescription;
    private String bookAuthor;
    private String bookTranslator;
    private String bookDownloadLink;
    private String bookShortUrl;
    private String categoryParent;
    private String categoryChild;
    private String categoryChild2;
    private int clickCount;
    private float rating;
    private long timeStamp = System.currentTimeMillis();
    private String key;
    private ArrayList<String> tags;

    public Book() { }

    public String getAuthorId() {
        return authorId;
    }

    public Book setAuthorId(String authorId) {
        this.authorId = authorId;
        return this;
    }

    public String getBookThumbnail() {
        return bookThumbnail;
    }

    public Book setBookThumbnail(String bookThumbnail) {
        this.bookThumbnail = bookThumbnail;
        return this;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public Book setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
        return this;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public Book setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
        return this;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public Book setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
        return this;
    }

    public String getBookTranslator() {
        return bookTranslator;
    }

    public Book setBookTranslator(String bookTranslator) {
        this.bookTranslator = bookTranslator;
        return this;
    }

    public String getBookDownloadLink() {
        return bookDownloadLink;
    }

    public Book setBookDownloadLink(String bookDownloadLink) {
        this.bookDownloadLink = bookDownloadLink;
        return this;
    }

    public String getBookShortUrl() {
        return bookShortUrl;
    }

    public Book setBookShortUrl(String bookShortUrl) {
        this.bookShortUrl = bookShortUrl;
        return this;
    }

    public String getCategoryParent() {
        return categoryParent;
    }

    public Book setCategoryParent(String categoryParent) {
        this.categoryParent = categoryParent;
        return this;
    }

    public String getCategoryChild() {
        return categoryChild;
    }

    public Book setCategoryChild(String categoryChild) {
        this.categoryChild = categoryChild;
        return this;
    }

    public String getCategoryChild2() {
        return categoryChild2;
    }

    public Book setCategoryChild2(String categoryChild2) {
        this.categoryChild2 = categoryChild2;
        return this;
    }

    public int getClickCount() {
        return clickCount;
    }

    public Book setClickCount(int clickCount) {
        this.clickCount = clickCount;
        return this;
    }

    public float getRating() {
        return rating;
    }

    public Book setRating(float rating) {
        this.rating = rating;
        return this;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public Book setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public String getKey() {
        return key;
    }

    public Book setKey(String key) {
        this.key = key;
        return this;
    }

    public ArrayList<String> getTags(){
        return this.tags;
    }

    public Book setTags(ArrayList<String> tags){
        this.tags = tags;
        return this;
    }
}