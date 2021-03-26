package com.example.simpletodo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    EditText updateText;
    Button saveButton;
    String old;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        updateText = findViewById(R.id.updateTextId);
        saveButton = findViewById(R.id.saveButtonId);
        getSupportActionBar().setTitle("Edit Item");

        old = getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT);
        updateText.setText(old);
        //  updateText.setText(getIntent().getStringExtra("item to update"));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updated = updateText.getText().toString();
                if (old.compareTo(updated)==0) {
                    showAlert(EditActivity.this);
                }
             else {
                    Intent i = new Intent();
                    i.putExtra(MainActivity.KEY_ITEM_TEXT, updateText.getText().toString());
                    i.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        });


    }

    private void showAlert(Context context){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("No Item Provided");
        alert.setMessage("You did not update the item.\nAre you sure you want to continue?");
        alert.setPositiveButton("Yes, Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alert.setNegativeButton("I want to update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.create().cancel();
            }
        });
        AlertDialog a = alert.create();
        a.setCancelable(false);
        a.show();

    }
}