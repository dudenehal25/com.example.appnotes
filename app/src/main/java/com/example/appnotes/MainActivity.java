package com.example.appnotes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static ArrayList<String> notes  = new ArrayList<>();
    static SharedPreferences sharedPreferences;
    static ArrayAdapter<String> myAdapter;

    //Adding menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

       if (R.id.ADD == item.getItemId()){
           notes.add("new note");
           myAdapter.notifyDataSetChanged();
           return true;
       }
       else return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sharedPreferences  = getApplicationContext().getSharedPreferences("com.example.appnotes;" ,MODE_PRIVATE);


        //ListView
        ListView listView_notes = findViewById(R.id.listView);

        HashSet<String> result_set =  (HashSet<String>) sharedPreferences.getStringSet("notes" , null);

        if (result_set == null)
            notes.add("Example is here");
        else  notes = new ArrayList(result_set);




        //setting Adapter to show
        myAdapter = new ArrayAdapter<>(getApplicationContext() , android.R.layout.simple_expandable_list_item_1 ,notes);
        listView_notes.setAdapter(myAdapter);
        listView_notes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                intent.putExtra("noteId" , i);
                startActivity(intent);
            }

        });

        listView_notes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int itemDlete = i;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you want This to DELETE")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(itemDlete);
                                myAdapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();

                                HashSet<String> hashSet = new HashSet<>(notes);
                                sharedPreferences.edit().putStringSet("notes" , hashSet).apply();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MainActivity.this, "NOT deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                return true;

            }
        });

        sharedPreferences  = this.getSharedPreferences("com.example.appnotes;" ,MODE_PRIVATE);

        try {
            sharedPreferences.edit().putString("friend" , ObjectSerializer.serialize(notes)).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}