package com.roy.simpletodo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.roy.simpletodo.com.roy.simpletodo.utils.DateTime;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ArrayList<Item> items;
 //   ArrayAdapter<Item> itemsAdapter;
    CustomItemAdapter customItemAdapter;
    ListView lvItems;

    private GoogleApiClient client;
    DbActivity db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setBackgroundColor(Color.parseColor("#2f4550"));
        myToolbar.showOverflowMenu();

        db = new DbActivity(this);
        items = db.readAllFromDb();
        //      itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);    //.simple_list_item_1
        customItemAdapter = new CustomItemAdapter(this, items);

        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(customItemAdapter);
        setupListViewListener();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        if(itemText != null && !itemText.isEmpty()) {
            String id = DateTime.getCurrentDateTimeMS();
            Date todate = new Date();
            Item newItem = new Item(id, itemText, Item.PRIORITY.MEDIUM, todate, "");     //Set default PRIORITY
            customItemAdapter.add(newItem);
            etNewItem.setText("");
            db.writeToDb(newItem);
        }else{
            Toast toast = Toast.makeText(this, "To add, enter a new item.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                    new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        delete(pos);
                        customItemAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );
        lvItems.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                //    String itemValue = lvItems.getItemAtPosition(pos).toString();
                Item editItem = (Item)lvItems.getItemAtPosition(pos);
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("name", editItem.getName());
                i.putExtra("priority", Integer.valueOf(editItem.getIntPriority()).toString());
                i.putExtra("date", DateTime.dateToString(editItem.getDate(), "yyyyMMdd"));
                i.putExtra("position", Integer.valueOf(pos).toString());
                i.putExtra("notes", editItem.getNotes());
                startActivityForResult(i, 1);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int index = -1;
        Item newItem;

        if(resultCode == RESULT_OK && requestCode == 1){
            if(data.hasExtra("position") && data.hasExtra("choice")) {
                index = Integer.parseInt(data.getStringExtra("position"));
                if (index == -1) {
                    String emsg = "Returned position is incorrect " + index;
                    Log.v("ParseError", emsg);
                }
                if (data.getExtras().getString("choice").equalsIgnoreCase("EDIT")) {
                    newItem = extractIntentToItem(data);
                    update(newItem, index);
                } else if (data.getExtras().getString("choice").equalsIgnoreCase("DELETE")) {
                    delete(index);
                }
            }
            customItemAdapter.notifyDataSetChanged();
        }
    }

    private Item extractIntentToItem(Intent data) {
        Item newItem = new Item();

        if (data.hasExtra("name")) {
            newItem.setName(data.getExtras().getString("name"));
        }
        if (data.hasExtra("priority")) {
            newItem.setPriority(Integer.parseInt(data.getExtras().getString("priority")));
        }
        if (data.hasExtra("date")) {      //format Feb 19, 2017
            String strd = data.getExtras().getString("date");
            Date d = DateTime.stringToDate(strd, "yyyyMMdd");           //If datepicker is selected format changes
            if (d == null)
                d = DateTime.stringToDate(strd, "MMM dd, yyyy");
            newItem.setDate(d);
        }
        if(data.hasExtra("notes")) {
            newItem.setNotes(data.getExtras().getString("notes"));
        }
        return newItem;
    }

    private void update(Item newItem, int index){
        Item oldItem = items.get(index);
        newItem.setId(oldItem.getId());
        boolean result = db.updateInDb(newItem, newItem.getId());
        String msg;
        if(result) {
            msg = "Successfully updated Db item = " + newItem.getName() + ", id = " +newItem.getId();
            Log.v("INFO", msg);
        }else{
            msg = "Failed to update Db item = " + newItem.getName() + ", id = " +newItem.getId();
            Log.v("ERROR", msg);
        }
        items.set(index, newItem);
    }
    private void delete(int index){
        Item todel = items.get(index);
        boolean result = db.deleteInDb(todel.getId());
        String msg;
        if(result) {
            msg = "Successfully delete in Db item = " + todel.getName() + ", id = " +todel.getId();
            Log.v("INFO", msg);
        }else{
            msg = "Failed to delete in Db item = " + todel.getName() + ", id = " +todel.getId();
            Log.v("ERROR", msg);
        }
        items.remove(index);
    }
/*
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "toodo.txt");
        try {
            items = new ArrayList<Item>(FileUtils.readLines(todoFile));
        }catch(IOException e){
            items = new ArrayList<Item>();
        }
    }

    private void writeItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "toodo.txt");
        try{
            FileUtils.writeLines(todoFile, items);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
*/



}
