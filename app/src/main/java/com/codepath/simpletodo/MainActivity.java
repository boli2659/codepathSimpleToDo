package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.codepath.simpletodo.TodoItemDatabase.getInstance;

public class MainActivity extends AppCompatActivity {
    ArrayList<Task> items;
    TaskAdapter itemsAdapter;
    ListView lvItems;
    TodoItemDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView)findViewById(R.id.lvItems);
        db = getInstance(this);
        readItems();
        items = new ArrayList<>();
        itemsAdapter = new TaskAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
        //items.add("First Item");
        //items.add("Second Item");
        setupListViewListener();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings) {
            return true;

        }
        return super.onOptionsItemSelected(item);
    }*/

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener(){
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id){
                        db.deleteTask(items.get(pos));
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        //writeItems();
                        return true;
                    }
                });
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id){
                        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                        i.putExtra("name", items.get(pos).name);
                        i.putExtra("date", items.get(pos).dueDate);
                        i.putExtra("priority", items.get(pos).priority);
                        i.putExtra("position", pos);
                        db.deleteTask(items.get(pos));
                        startActivityForResult(i, 2);
                        //writeItems();

                        //return true;
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        //System.out.println("SIXTH");
        if (resultCode == RESULT_OK && requestCode == 2) {
            // Extract name value from result extras
            String name = data.getExtras().getString("name");
            long dueDate = data.getExtras().getLong("date");
            float priority = data.getExtras().getInt("priority");
            int position = data.getExtras().getInt("position");
            Task task = new Task(name, dueDate, priority);
            //int code = data.getExtras().getInt("code", 0);
            // Toast the name to display temporarily on screen
            items.remove(position);
            items.add(position, task);
            itemsAdapter.notifyDataSetChanged();
            db.addTask(task);
            //writeItems();
            //Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
        }
    }

    public void onAddItem(View v) {
        EditText etNewItem= (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        Task t = new Task(itemText);
        itemsAdapter.add(t);
        etNewItem.setText("");
        itemsAdapter.notifyDataSetChanged();
        db.addTask(t);

    }
    private void readItems() {
        items = new ArrayList<>(db.getAllTasks());

    }
   /* private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
