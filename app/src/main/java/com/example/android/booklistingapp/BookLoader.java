package com.example.android.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Jam on 01.07.2017.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<Book> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        Log.v("BookLoader.java", "Fetch w tle");
        List<Book> books = QueryUtils.fetchBookData(mUrl);
        return books;
    }

    protected void onStartLoading() {
        forceLoad();
    }
}
