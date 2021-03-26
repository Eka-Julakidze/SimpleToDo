package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_ITEM_TEXT="item_text";
    public static final String KEY_ITEM_POSITION="item_position";
    public static final int EDIT_TEXT_CODE =20;

    Button addButton;
    EditText addText;
    RecyclerView rv;

    List<String> items;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadItems();

        addButton=findViewById(R.id.addButtonId);
        addText=findViewById(R.id.addTextId);
        rv=findViewById(R.id.rvId);

        ItemsAdapter.OnClickListener onNormalClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.e("MainActivity", "Single Click at position " + position);
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };
        
        

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){

            @Override
            public void onItemLongClicked(int position) {
                //Delete the item from the model
                items.remove(position);
                // Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(MainActivity.this, "Item Removed", Toast.LENGTH_SHORT).show();
                saveItems();

            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onNormalClickListener);
        rv.setAdapter(itemsAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = addText.getText().toString();
                if(s.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No Item Provided", Toast.LENGTH_SHORT).show();
                }
                else {
                    //add item to the model
                    items.add(s);

                    //notify adapter that an item is inserted
                    itemsAdapter.notifyItemInserted(items.size() - 1);
                    addText.setText("");
                    Toast.makeText(MainActivity.this, "New Item Added", Toast.LENGTH_SHORT).show();
                    saveItems();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            String s = data.getStringExtra(KEY_ITEM_TEXT);
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            //update model at the right place
            items.set(position, s);

            //notify
            itemsAdapter.notifyItemChanged(position);
            //persist changes
            saveItems();

            Toast.makeText(this, "Updated Successfuly", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(),"data.txt");
    }

    private void loadItems() {
        try {
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch(IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items=new ArrayList<>();

        }

    }

    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing the File", e);
        }

    }
}