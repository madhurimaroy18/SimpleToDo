package com.roy.simpletodo;

import java.util.Date;

/**Defines each to do item
 * Created by roy on 2/19/2017.
 */

public class Item{
    private String id;
    private String name;
    private PRIORITY priority;
    private Date date;

    public enum PRIORITY {HIGH, MEDIUM, LOW;}

    public Item(String id, String name, PRIORITY priority, Date date) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.date = date;
    }

    public Item(){

    }

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

    @Override
    public String toString() {
        return this.getName();
    }
}
