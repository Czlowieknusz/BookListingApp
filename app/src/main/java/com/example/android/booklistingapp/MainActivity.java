package com.example.android.booklistingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    View searchButton;
    EditText typedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);
        typedText = (EditText) findViewById(R.id.typed_text);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_button:
                Intent i = new Intent(this, BookActivity.class);
                i.putExtra("searchPhrase",typedText.getText().toString());
                startActivity(i);
                break;
        }
    }
}
