package com.roy.simpletodo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.roy.simpletodo.com.roy.simpletodo.utils.DateTime;

import java.util.Calendar;
import java.util.Date;

import static android.content.ContentValues.TAG;
import static com.roy.simpletodo.R.id.etEditItem;

/**
 * Created by Madhurima Roy on 2/20/2017.
 */

public class EditDialogFragment extends DialogFragment {

    private EditText etName;
    private Spinner spinnerPos; //priority;
    private TextView tvDueDate; //due;
    private EditText etNotes; //notes;
    private String position;

    private static String dateFormat = "MMM dd, yyyy";

    Calendar now;

    public EditDialogFragment() {}

    public static EditDialogFragment newInstance(String toShowName, int toShowPriority, Date due, String notes, String position) {
        EditDialogFragment frag = new EditDialogFragment();
        Bundle args = new Bundle();
        args.putString("name", toShowName);
        args.putInt("priority", toShowPriority);
        String dueStr = DateTime.dateToString(due, dateFormat);
        args.putString("due", dueStr);
        args.putString("notes", notes);
        args.putString("position", position);
        frag.setArguments(args);
        return frag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_edit_item, container);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fetch arguments from bundle
        String itemName = getArguments().getString("name");
        int priority = getArguments().getInt("priority");
        String toShow = getArguments().getString("due");
        String notes = getArguments().getString("notes");
        this.position = getArguments().getString("position");

        //Get field from view and set fields
        getsetEditTextName(view, itemName);
        getsetSpinnerPriority(view, priority);
        getsetTexViewDuedate(view, toShow);
        getsetEditTextNotes(view, notes);
        getsetButtonSave(view);
        getsetButtonDel(view);
        getDialog().setTitle("Edit Item");

        // Show soft keyboard automatically and request focus to field
        etName.requestFocus();
  /*      getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);*/
    }

    private void getsetEditTextName(View view, String itemName){
        this.etName = (EditText) view.findViewById(etEditItem);
        etName.setText(itemName);
    }
    private void getsetSpinnerPriority(View view, int priority) {
        this.spinnerPos = (Spinner) view.findViewById(R.id.spinnerPriority);
        spinnerPos.setSelection(priority);
    }
    private void getsetTexViewDuedate(View view, String toShow) {
        this.tvDueDate = (TextView) view.findViewById(R.id.editTextFromDate);
        tvDueDate.setText(toShow);

        now = Calendar.getInstance();
        tvDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Roy: onclick date textview");
                showDatePicker();
            }
        });
    }
    private void getsetEditTextNotes(View view, String notes){
        this.etNotes = (EditText) view.findViewById(R.id.etNotes);
        etNotes.setText(notes);
    }
    private void getsetButtonSave(View view){
        Button okButton = (Button) view.findViewById(R.id.btnSaveItem);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("buttonClick", "inside click.Save button click");
                onSelectButton(v, 0);
            }
        });
    }
    private void getsetButtonDel(View view){
        Button delButton = (Button) view.findViewById(R.id.btnDeleteItem);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("buttonClick", "inside click.Cancel button click");
                onSelectButton(v, 1);
            }
        });
    }

    private void showDatePicker() {
        FragmentTransaction ft = getFragmentManager().beginTransaction(); //get the fragment
        DateDialogFragment dateFrag = DateDialogFragment.newInstance(getContext(), new DateDialogFragmentListener() {
            public void updateChangedDate(int year, int month, int day) {
                Date newDue = DateTime.getDate(year, month, day);
                tvDueDate.setText(DateTime.dateToString(newDue, dateFormat));
                now.set(year, month, day);
            }
        }, now);
        dateFrag.show(ft, "DateDialogFragment");
    }

    public boolean onSelectButton(View v, int action) {     //0: Save, 1: Delete
        Log.d(TAG, "Save Clicked.");
        String name = etName.getText().toString();
       // Return input text back to activity through the implemented listener
        EditActionListener listener = (EditActionListener) getActivity();

        switch (action) {
            case 0:
                if(name != null && !name.isEmpty()) {
                    int pr = spinnerPos.getSelectedItemPosition();
                    Date newDue = DateTime.stringToDate(tvDueDate.getText().toString(), dateFormat);
                    String notes = etNotes.getText().toString();
                    listener.onFinishEditDialog(this.position, name, pr, newDue, notes, 0);
                }else listener.onFinishEditDialog(this.position, null,null, null, null, 0);
                break;
            case 1:
                listener.onFinishEditDialog(this.position, name, null, null, null, 1);
                break;
            default:  break;
        }
        // Close the dialog and return back to the parent activity
        dismiss();
        return true;
    }

}
