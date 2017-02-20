package com.roy.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.roy.simpletodo.com.roy.simpletodo.utils.DateTime;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ArrayList<Item> items;
    ArrayAdapter<Item> itemsAdapter;
    ListView lvItems;

    private GoogleApiClient client;
    DbActivity db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);
        db = new DbActivity(this);
        items = db.readAllFromDb();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);    //.simple_list_item_1
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        String id = DateTime.getCurrentDateTimeMS();
        Date todate = new Date();
        Item newItem = new Item(id, itemText, Item.PRIORITY.MEDIUM, todate);     //Set default PRIORITY
        itemsAdapter.add(newItem);
        etNewItem.setText("");
        db.writeToDb(newItem);
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        delete(pos);
                        itemsAdapter.notifyDataSetChanged();
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
                startActivityForResult(i, 1);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int index = -1;
        Item newItem;

        if(resultCode == RESULT_OK && requestCode == 1){
            if(data.hasExtra("position") && data.hasExtra("choice")){
                index = Integer.parseInt(data.getStringExtra("position"));
                if(index == -1) { String emsg = "Returned position is incorrect " + index; Log.v("ParseError", emsg); }
                newItem = extractIntentToItem (data);
                update(newItem, index);

            }else if(data.getExtras().getString("choice").equalsIgnoreCase("DELETE")){
                delete(index);
            }
            itemsAdapter.notifyDataSetChanged();
        }
    }

    private Item extractIntentToItem(Intent data) {
        Item newItem = new Item();
        if (data.getExtras().getString("choice").equalsIgnoreCase("EDIT")) {
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
