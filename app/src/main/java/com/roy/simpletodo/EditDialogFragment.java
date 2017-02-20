package com.roy.simpletodo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import static com.roy.simpletodo.R.id.etEditItem;

/**
 * Created by roy on 2/20/2017.
 */

public class EditDialogFragment extends DialogFragment  {

    private EditText etName;
    private Spinner spinnerPos; //priority;
    private EditText editTextFromDate; //due;
    private EditText etNotes; //notes;
    private String position;

    public EditDialogFragment() {}

    public static EditDialogFragment newInstance(String toShowName, int toShowPriority, String toShowDate, String notes, String position) {
        EditDialogFragment frag = new EditDialogFragment();
        Bundle args = new Bundle();
        args.putString("name", toShowName);
        args.putInt("priority", toShowPriority);
        args.putString("due", toShowDate);
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
        String toShowd = getArguments().getString("due");
        String notes = getArguments().getString("notes");
        this.position = getArguments().getString("position");

        //Get field from view and set fields
        this.etName = (EditText) view.findViewById(etEditItem);
        etName.setText(itemName);
        this.spinnerPos = (Spinner) view.findViewById(R.id.spinnerPriority);
        spinnerPos.setSelection(priority);
        this.editTextFromDate = (EditText) view.findViewById(R.id.editTextFromDate);
        editTextFromDate.setText(toShowd);
        this.etNotes = (EditText) view.findViewById(R.id.etNotes);
        etNotes.setText(notes);

        getDialog().setTitle("Edit Item");

        Button okButton = (Button) view.findViewById(R.id.btnSaveItem);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("buttonClick", "inside click");
                onEditorAction(v, 1, null);
              //  getDialog().dismiss();
            }
        });

        // Show soft keyboard automatically and request focus to field
        etName.requestFocus();
  /*      getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);*/


    }

    public boolean onEditorAction(View v, int actionId, KeyEvent event) {       //ActionId DELETE = 0 SAVE = 1
        Log.d("buttonClick", "inside onEditorAction");
        if (actionId == 1) {
            // Return input text back to activity through the implemented listener
            EditDialogListener listener = (EditDialogListener) getActivity();
            String name = etName.getText().toString();
            int pr = spinnerPos.getSelectedItemPosition();
            String strDate = editTextFromDate.getText().toString();
            String notes = etNotes.getText().toString();
            listener.onFinishEditDialog(this.position, name, pr, strDate, notes);
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }


}
