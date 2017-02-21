package com.roy.simpletodo;

import com.roy.simpletodo.com.roy.simpletodo.utils.DateTime;

import java.util.Date;

/**Defines each to do item
 * Created by roy on 2/19/2017.
 */

public class Item{
    private String id;
    private String name;
    private PRIORITY priority;
    private Date date;
    private String notes;

    public enum PRIORITY {HIGH, MEDIUM, LOW;}

    public Item(String id, String name, PRIORITY priority, Date date, String n) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.date = date;
        this.notes = n;
    }

    public Item(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PRIORITY getPriority() {
        return priority;
    }

    public int getIntPriority() {
        int p = 1;
        if(this.priority == PRIORITY.HIGH) p = 0;
        if(this.priority == PRIORITY.MEDIUM) p = 1;
        if(this.priority == PRIORITY.LOW) p = 2;
        return p;
    }
    public void setPriority(PRIORITY pr) {
        this.priority = pr;
    }
    public void setPriority(int p) {
        if(p == 0) this.priority = PRIORITY.HIGH;
        if(p == 1) this.priority = PRIORITY.MEDIUM;
        if(p == 2) this.priority = PRIORITY.LOW;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date dt) {
        this.date = dt;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Item setItemFromFileRow(String row, String token) {
        if(token == null) token = ",";  //default token
        row.trim();
        String[] fields = row.split(token);
        Item item = new Item();
        item.setName(fields[0]);
        item.setPriority(PRIORITY.valueOf(fields[1]));
        item.setDate(DateTime.stringToDate(fields[2], "MMM dd yyyy"));
        item.setNotes(fields[3]);

        return item;
    }
    public String getAllItemToString(String token){
        if(token == null) token = ",";  //default token
        String textRow = this.getName() + token + this.getPriority().toString()
                + token + DateTime.dateToString(this.getDate(), "MMM dd yyyy")
                + token + this.getNotes();
        return textRow;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
