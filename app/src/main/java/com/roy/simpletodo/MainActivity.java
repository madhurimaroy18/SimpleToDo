package com.roy.simpletodo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static android.widget.Toast.makeText;

    public class MainActivity extends AppCompatActivity implements EditActionListener {

    ArrayList<Item> items;
 //   ArrayAdapter<Item> itemsAdapter;
    CustomItemAdapter customItemAdapter;
    ListView lvItems;

    private GoogleApiClient client;
    DbActivity db;

    int editItemPosition = 0;

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
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        if(itemText != null && !itemText.isEmpty()) {
            String id = DateTime.getCurrentDateTimeMS();    //id is unique. Is based on MS
            Item newItem = new Item(id, itemText, Item.PRIORITY.MEDIUM, new Date(), "");     //Set default PRIORITY
            customItemAdapter.add(newItem);
            etNewItem.setText("");
            db.writeToDb(newItem);
        }else{
            Toast toast = makeText(this, "To add, enter a new item.", Toast.LENGTH_SHORT);
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
                Item editItem = (Item)lvItems.getItemAtPosition(pos);
                editItemPosition = pos;
                createDialog(editItem);
                /*
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("name", editItem.getName());
                i.putExtra("priority", Integer.valueOf(editItem.getIntPriority()).toString());
                i.putExtra("date", DateTime.dateToString(editItem.getDate(), "yyyyMMdd"));
                i.putExtra("position", Integer.valueOf(pos).toString());
                i.putExtra("notes", editItem.getNotes());
                startActivityForResult(i, 1);
                */
            }
        });
    }

    private void createDialog(Item editItem) {
        String toShowName = editItem.getName();
        int toShowPriority = editItem.getIntPriority();
        Date d = editItem.getDate();
        String notes = editItem.getNotes();

        showEditDialog(toShowName, toShowPriority, d, notes, null);
    }

    private void showEditDialog(String toShowName, int toShowPriority, Date dueDate, String notes, String pos) {
        FragmentManager fm = getSupportFragmentManager();
        EditDialogFragment editDialogFragment = EditDialogFragment.newInstance(toShowName, toShowPriority, dueDate, notes, pos);
        editDialogFragment.show(fm, "fragment_edit_name");      //tag
    }

    public void onFinishEditDialog(String position, String name, Integer pr, Date due, String notes, int action) {
        String message = "";

        switch (action){
            case 0: //Save Clicked
                if (name != null && !name.isEmpty()) {
                    Item newItem = new Item();
                    if (name != null && !name.isEmpty()) newItem.setName(name);
                    if (pr != null) newItem.setPriority(pr);
                    if (due != null) newItem.setDate(due);
                    if (notes != null && !notes.isEmpty()) newItem.setNotes(notes);
                    update(newItem, editItemPosition);
                    message = "Updated '" + name + "'";
                }else message = "!!Item not saved. Name can't be empty!!";
                break;
            case 1: //Delete Clicked
                delete(editItemPosition);
                message = "Deleted " + name;
                break;
            default: break;
        }
        customItemAdapter.notifyDataSetChanged();

        Toast toast = makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
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

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "toodo.txt");
        items = new ArrayList<Item>();
        try {
            FileReader fileReader = new FileReader(todoFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Item item = new Item();
                item.setItemFromFileRow(line, null);
                items.add(item);
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "toodo.txt");
        String customLine = null;
        try{
            for (Item i: items){
                customLine = i.getAllItemToString(null) + "\n";
            }
            FileUtils.writeStringToFile(todoFile, customLine);
        }catch (IOException e){
            e.printStackTrace();
        }
    }




}
