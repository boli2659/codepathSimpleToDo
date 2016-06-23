package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditItemActivity extends AppCompatActivity {
int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        String name = getIntent().getStringExtra("name");
        pos = getIntent().getExtras().getInt("position");
        EditText editText = (EditText)findViewById(R.id.etEditItem);
        editText.setText(name, TextView.BufferType.EDITABLE);
        System.out.println("FIRST");
    }

    public void onSaveItem(View v) {
        EditText etNewItem= (EditText) findViewById(R.id.etEditItem);
        String editedItem = etNewItem.getText().toString();
        // Prepare data intent
        System.out.println("SECOND");
        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("name", editedItem);
        data.putExtra("position", pos);
        System.out.println("THIRD");
        //data.putExtra("code", 2); // ints work too
        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        System.out.println("FOURTH");
        finish(); // closes the activity, pass data to parent
        System.out.println("FIFTH-- OPTIONAL");

    }
}
