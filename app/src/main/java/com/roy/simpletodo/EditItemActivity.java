package com.roy.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

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
        this.position = getIntent().getStringExtra("position");
     //   this.position = Integer.parseInt(strPos);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        EditText etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(itemName);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerPriority);
        spinner.setSelection(priority);
      //  priority.setLabelFor(spinnerPosition);

        EditText editTextFromDate = (EditText) findViewById(R.id.editTextFromDate);
        editTextFromDate.setText(toShowd);
        DateTime fromDate = new DateTime(editTextFromDate, this, itemDate);

    }

    public void onSaveItem(View view) {
        EditText etEditItem = (EditText) findViewById(R.id.etEditItem);
        String newItemValue = etEditItem.getText().toString();

        Spinner pos = (Spinner) findViewById(R.id.spinnerPriority);
        Integer priority = pos.getSelectedItemPosition();     //.getSelectedItem().toString();

        EditText editTextFromDate = (EditText) findViewById(R.id.editTextFromDate);
        String strDate = editTextFromDate.getText().toString();  //format Feb 19, 2017

        Intent intent = new Intent();
        intent.putExtra("choice", "EDIT");
        intent.putExtra("position", this.position);
        intent.putExtra("name", newItemValue);
        intent.putExtra("priority", priority.toString());
        intent.putExtra("date", strDate);
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
}
