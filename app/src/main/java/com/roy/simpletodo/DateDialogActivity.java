package com.roy.simpletodo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by roy on 2/20/2017.
 */

public class DateDialogActivity implements View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener{

    private TextView textView;
    private Calendar myCalendar;
    private Context context;

    public DateDialogActivity(TextView textview, Context ctx, Date d){
        this.textView = textview;
        this.textView.setOnFocusChangeListener(this);
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

        textView.setText(sdformat.format(myCalendar.getTime()));

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            new DatePickerDialog(this.context, this, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        }
    }
}
