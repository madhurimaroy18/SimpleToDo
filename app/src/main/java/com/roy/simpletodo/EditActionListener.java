package com.roy.simpletodo;

import java.util.Date;

/**
 * Created by roy on 2/20/2017.
 */

public interface EditActionListener {
    public void onFinishEditDialog(String position, String name, Integer pr, Date due, String notes, int action);    //0: Save, 1: Delete
}
