package com.roy.simpletodo.com.roy.simpletodo.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by roy on 2/18/2017.
 */

public class DateTime implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener {

    private EditText editText;
    private Calendar myCalendar;
    private Context context;

    public DateTime(EditText editText, Context ctx){
        this.editText = editText;
        this.editText.setOnFocusChangeListener(this);
        myCalendar = Calendar.getInstance();
        this.context = ctx;
    }
    public DateTime(EditText editText, Context ctx, Date d){
        this.editText = editText;
        this.editText.setOnFocusChangeListener(this);
        myCalendar = Calendar.getInstance();
        this.context = ctx;
        myCalendar.setTime(d);
    }
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)     {
        String myFormat = "MMM dd, yyyy"; //In which you need put here
        SimpleDateFormat sdformat = new SimpleDateFormat(myFormat, Locale.US);
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        editText.setText(sdformat.format(myCalendar.getTime()));

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // TODO Auto-generated method stub
        if (hasFocus) {
            new DatePickerDialog(this.context, this, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        }
    }

    public static String getCurrentDateTimeMS() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        String datetime = ft.format(dNow);
        return datetime;
    }

    public static Date stringToDate(String aDate, String aFormat) {
        if(aDate==null) return null;
        if(aFormat==null || aFormat.isEmpty()) aFormat = "yyyyMMdd";
     //   Date d = DateFormat.parse(aDate);
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
       Date d = simpledateformat.parse(aDate, pos);
        return d;
    }

    public static String dateToString(Date aDate, String aFormat) {
        String strDate = null;
        if(aDate==null) return null;
        if(aFormat==null || aFormat.isEmpty()) aFormat = "yyyyMMdd";
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        strDate = simpledateformat.format(aDate);
        return strDate;
    }
}
