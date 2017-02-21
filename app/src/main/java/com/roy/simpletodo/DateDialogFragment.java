package com.roy.simpletodo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by roy on 2/20/2017.
 */

public class DateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static String TAG = "DateDialogFragment";
    static Context mContext; //I guess hold the context that called it. Needed when making a DatePickerDialog. I guess its needed when conncting the fragment with the context
    static int mYear;
    static int mMonth;
    static int mDay;
    static DateDialogFragmentListener mListener;

    public DateDialogFragment() {}

    public static DateDialogFragment newInstance(Context context, DateDialogFragmentListener listener, Calendar now) {
        DateDialogFragment dialog = new DateDialogFragment();
        mContext = context;
        mListener = listener;

        mYear = now.get(Calendar.YEAR);
        mMonth = now.get(Calendar.MONTH);
        mDay = now.get(Calendar.DAY_OF_MONTH);
		/*I dont really see the purpose of the below*/
        Bundle args = new Bundle();
        args.putString("title", "Set Date");
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        Log.d(TAG, "Roy: on date set");
        mListener.updateChangedDate(yy, mm, dd);    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }
}
