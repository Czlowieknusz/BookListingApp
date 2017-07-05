package com.example.android.booklistingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jam on 01.07.2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);

        }
        Book currentBook = getItem(position);

        TextView titleView = (TextView) listItemView.findViewById(R.id.title);

        titleView.setText(currentBook.getmTitle());

        TextView authorsView = (TextView) listItemView.findViewById(R.id.authors);

        String allAuthors = "";
        for (int i = 0; i < currentBook.getmAuthors().size(); i++) {
            allAuthors += currentBook.getmAuthors().get(i);
            if(i+i< currentBook.getmAuthors().size())
                allAuthors+="\n";
        }
        authorsView.setText(allAuthors);

        return listItemView;
    }
}
