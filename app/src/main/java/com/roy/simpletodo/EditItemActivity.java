package com.roy.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.roy.simpletodo.com.roy.simpletodo.utils.DateTime;

import java.util.Date;

public class EditItemActivity extends AppCompatActivity {
    String position;    //need to pass as String

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String itemName = getIntent().getStringExtra("name");
        int priority = Integer.valueOf(getIntent().getStringExtra("priority"));
     //   String itemPriority = getIntent().getStringExtra("priority");
        String strDate = getIntent().getStringExtra("date");
        Date itemDate = DateTime.stringToDate(strDate, null);
        String toShowd = DateTime.dateToString(itemDate, "MMM dd, yyyy");
        String notes = getIntent().getStringExtra("notes");
        this.position = getIntent().getStringExtra("position");
     //   this.position = Integer.parseInt(strPos);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        EditText etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(itemName);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerPriority);
        spinner.setSelection(priority);
      //  priority.setLabelFor(spinnerPosition);

        TextView editTextFromDate = (TextView) findViewById(R.id.editTextFromDate);
        editTextFromDate.setText(toShowd);
        DateDialogActivity fromDate = new DateDialogActivity(editTextFromDate, this, itemDate);

        EditText etNotes = (EditText) findViewById(R.id.etNotes);
        etNotes.setText(notes);

    }

    public void onSaveItem(View view) {
        EditText etEditItem = (EditText) findViewById(R.id.etEditItem);
        String newItemValue = etEditItem.getText().toString();

        Spinner pos = (Spinner) findViewById(R.id.spinnerPriority);
        Integer priority = pos.getSelectedItemPosition();     //.getSelectedItem().toString();

        TextView editTextFromDate = (TextView) findViewById(R.id.editTextFromDate);
        String strDate = editTextFromDate.getText().toString();  //format Feb 19, 2017

        EditText etNotes = (EditText) findViewById(R.id.etNotes);
        String notes = etNotes.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("choice", "EDIT");
        intent.putExtra("position", this.position);
        intent.putExtra("name", newItemValue);
        intent.putExtra("priority", priority.toString());
        intent.putExtra("date", strDate);
        intent.putExtra("notes", notes);
        setResult(RESULT_OK, intent);
        finish();

    }

    public void onDeleteItem(View view){
        Intent intent = new Intent();
        intent.putExtra("choice", "DELETE");
        intent.putExtra("position", this.position);
        setResult(RESULT_OK, intent);
        finish();
    }


    /*
    public void onFinishEditDialog(String position, String name, Integer pr, String due, String notes) {
   //     Toast.makeText(this, "Hi, " + name, Toast.LENGTH_SHORT).show();
        Log.d("buttonClick", "returned to parent");

        Intent intent = new Intent();
        intent.putExtra("choice", "EDIT");
        intent.putExtra("position", position);
        intent.putExtra("name", name);
        intent.putExtra("priority", pr.toString());
        intent.putExtra("date", due);
        intent.putExtra("notes", notes);
        setResult(RESULT_OK, intent);
    }
    */
}
