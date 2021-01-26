package com.alim.ebook.Models;

public class FavBook {
    private String bookId;
    private String bookTitle;
    private String bookThumbnail;
    private String bookDownloadLink;
    private String bookAuthor;

    public FavBook(){}

    public String getBookId() {
        return bookId;
    }

    public FavBook setBookId(String bookId) {
        this.bookId = bookId;
        return this;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public FavBook setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
        return this;
    }

    public String getBookThumbnail() {
        return bookThumbnail;
    }

    public FavBook setBookThumbnail(String bookThumbnail) {
        this.bookThumbnail = bookThumbnail;
        return this;
    }

    public String getBookDownloadLink() {
        return bookDownloadLink;
    }

    public FavBook setBookDownloadLink(String bookDownloadLink) {
        this.bookDownloadLink = bookDownloadLink;
        return this;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public FavBook setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
        return this;
    }
}
