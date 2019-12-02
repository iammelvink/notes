package com.iammelvink.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class noteEditorActivity extends AppCompatActivity {
    private EditText editNote;
    private int noteID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        /*Connecting UI elements to code*/
        editNote = (EditText) findViewById(R.id.txtEditNote);

        Intent intent = getIntent();
        noteID = intent.getIntExtra("noteID", -1);

        /*If note exists*/
        if (noteID != -1) {
            editNote.setText(MainActivity.notes.get(noteID));
            /*else create new note*/
        } else {
            MainActivity.notes.add("");
            noteID = MainActivity.notes.size() - 1;
        }

        /*Code runs when text changes*/
        editNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*Add date saved*/
//                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
//                String date = sdf.format(new Date());

                /*Update ArrayList*/
                MainActivity.notes.set(noteID, String.valueOf(s));
//                MainActivity.notes.set(noteID, date + "\n" + String.valueOf(s));
                /*Notify ArrayAdapter of update*/
                MainActivity.arrayAdapter.notifyDataSetChanged();

                /*Saving note to storage*/
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.wiilearntech.notes", Context.MODE_PRIVATE);
                /*Convert ArrayList to HashSet*/
                HashSet<String> set = new HashSet<>(MainActivity.notes);
                /*Save HashSet to storage*/
                sharedPreferences.edit().putStringSet("notes", set).apply();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
