package com.example.android.booklistingapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jam on 01.07.2017.
 */

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {

    }

    private static List<Book> extractFeatureFromJson(String bookJSON) {
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }
        List<Book> books = new ArrayList<>();

        try {
            //Log.v("QueryUtils.java", "bookJSON = "+bookJSON);
            JSONObject baseJsonResponse = new JSONObject(bookJSON);
            // przeanalizować jakie hasło
            JSONArray bookArray = baseJsonResponse.getJSONArray("items");
            //Log.v("QueryUtils.java", "bookArray = "+bookArray);

            for (int i = 0; i < bookArray.length(); i++) {
                JSONObject currentBook = bookArray.getJSONObject(i);
                Log.v("QueryUtils.java", "currentBook = " + currentBook);
                // przeanalizować jakie hasło
                JSONObject info = currentBook.getJSONObject("volumeInfo");
                Log.v("QueryUtils.java", "info = " + info);
                Log.v("QueryUtils.java", "title = " + info.getString("title"));

                String title = info.getString("title");
                Log.v("QueryUtils.java", "title = " + title);

                JSONArray authorArray = info.getJSONArray("authors");
                Log.v("QueryUtils.java", "Tablica załadowana.\n Jej długość to: " + authorArray.length());
                ArrayList<String> authors = new ArrayList<>();
                for (int j = 0; j < authorArray.length(); j++) {
                    Log.v("QueryUtils.java", "\n ten nieszczęsny string: " + authorArray.getString(j) + "\n");
                    authors.add(authorArray.getString(j));
                    Log.v("QueryUtils.java", "dodano do listy: ");
                }
                for (int j = 0; j<authors.size();j++){
                    Log.v("QueryUtils.java", j + " autor: " + authors.get(j));
                }
                /*
                    Tu wstaw dane do przypisania z internetu
                 */

                Book book = new Book(title, authors);

                books.add(book);
            }
        } catch (JSONException e) {
            Log.v("QueryUtils", "Problem with parsing the book JSON results");
            e.printStackTrace();
        }
        return books;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // in case the URL equals null, then return
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null) {
                inputStream.close();
            }
        }
        Log.v("QueryUtils", String.valueOf(urlConnection.getResponseCode()));
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<Book> fetchBookData(String requestUrl) {
        Log.v("QueryUtils.java", "Nawiazywaniee polaczenia z " + requestUrl);

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Book> books = extractFeatureFromJson(jsonResponse);

        return books;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }
}
