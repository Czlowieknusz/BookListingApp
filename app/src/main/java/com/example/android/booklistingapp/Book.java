package com.example.android.booklistingapp;

import java.util.ArrayList;

/**
 * Created by Jam on 01.07.2017.
 */

public class Book {
    private String mTitle;
    private ArrayList<String> mAuthors;

    public Book(String title, ArrayList<String> authors){
        mAuthors=authors;
        mTitle=title;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public ArrayList<String> getmAuthors() {
        return mAuthors;
    }

    public void setmAuthors(ArrayList<String> mAuthors) {
        this.mAuthors = mAuthors;
    }
}
