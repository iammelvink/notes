package com.iammelvink.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    private ListView notesList;
    protected static ArrayList<String> notes = new ArrayList<>();
    protected static ArrayAdapter arrayAdapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Access storage*/
        sharedPreferences = getApplicationContext().getSharedPreferences("com.wiilearntech.notes", Context.MODE_PRIVATE);

        /*Connecting UI elements to code*/
        notesList = (ListView) findViewById(R.id.listNotes);

        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);
        if (set == null) {
            /*
             * Feeding the correct values to be placed in the ListView*/
            notes.add("Example Note");
        } else {
            /*Putting HashSet into ArrayList*/
            notes = new ArrayList<>(set);
        }

        /*
         * Putting values into ListView*/
        arrayAdapter = new ArrayAdapter(
                this, android.R.layout.simple_list_item_1, notes);

        notesList.setAdapter(arrayAdapter);

        /*
         * ListView OnItemClickListener*/
        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.i("NoteID: ", notes.get(position));

                /*Move to next Activity
                 * using Intent*/
                Intent intent = new Intent(getApplicationContext(), noteEditorActivity.class);

                /*Send info to next Activity*/
                intent.putExtra("noteID", position);

                /*Make it happen*/
                startActivity(intent);
            }
        });

        /*Delete on long click*/
        notesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

//                final int ITEM_TO_DELETE = position;

                /*Alert dialog*/
                new AlertDialog.Builder(MainActivity.this).setIcon(
                        /*Setting icon*/
                        android.R.drawable.ic_dialog_alert).setTitle(
                        /*Setting title*/
                        "Are you sure?"
                ).setMessage(
                        /*Setting message*/"Do you want to delete this note?").setPositiveButton(
                        /*Setting yes option*/"Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /*Delete note*/
//                                notes.remove(ITEM_TO_DELETE);
//                                arrayAdapter.notifyDataSetChanged();

                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                                /*Deleting note to storage*/
                                /*Convert ArrayList to HashSet*/
                                HashSet<String> set = new HashSet<>(MainActivity.notes);
                                /*Save HashSet to storage*/
                                sharedPreferences.edit().putStringSet("notes", set).apply();
                                Toast.makeText(MainActivity.this, "Note deleted! :(", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No", null).show();

                return true;
            }
        });
    }

    /*For menu*/
    /*Runs when app starts
     * creates menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /*Class to get menu items*/
        MenuInflater menuInflater = getMenuInflater();

        /*Connecting code to UI*/
        menuInflater.inflate(R.menu.add_note_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /*Runs when option is selected*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.menuAddNote) {
            Log.i("menuAddNote", "tapped");

            /*Move to next Activity
             * using Intent*/
            Intent intent = new Intent(getApplicationContext(), noteEditorActivity.class);

            /*Make it happen*/
            startActivity(intent);
            return true;
        }
        return false;
    }
}
