package com.alim.ebook.Models;

import androidx.annotation.Keep;

import java.util.ArrayList;

public class FavBooks {
    private ArrayList<FavBook> books;

    public FavBooks(){}

    public ArrayList<FavBook> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<FavBook> books) {
        this.books = books;
    }
}
