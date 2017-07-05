package com.example.android.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jam on 03.07.2017.
 */

public class BookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private ProgressBar progressBar;
    private TextView mEmptyStateTextView;
    private String BOOK_SEPARATOR = " ";
    private static final int BOOK_LOADER_ID = 1;
    private static final String LOG_TAG = BookActivity.class.getName();

    private String USGS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private BookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);

        Intent intent = getIntent();

        String searchPhrase = intent.getStringExtra("searchPhrase");

        String allAuthors = "";
        if (searchPhrase.contains(BOOK_SEPARATOR)) {

            String[] parts = searchPhrase.split(BOOK_SEPARATOR);

            for (int i = 0; i < parts.length; i++) {
                allAuthors += parts[i];
                if (i + 1 < parts.length)
                    allAuthors += "+";
            }
            Log.v("BookActivity.java", allAuthors);
        } else {
            allAuthors = searchPhrase;
            Log.v("BookActivity.java", allAuthors);
        }
        USGS_REQUEST_URL+=allAuthors;


        ListView bookListView = (ListView) findViewById(R.id.book_list_view);

        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        bookListView.setAdapter(mAdapter);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        if (isOnline()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText("No internt connection");
            bookListView.setEmptyView(mEmptyStateTextView);
        }
    }

    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        Log.v("BookActivity.java", "USGS_REQUEST_URL = " + USGS_REQUEST_URL);
        return new BookLoader(this, USGS_REQUEST_URL);
    }

    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        mAdapter.clear();

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
        progressBar.setVisibility(View.GONE);
        mEmptyStateTextView.setText("There's nothing here");
    }

    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
