package com.example.appnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static com.example.appnotes.MainActivity.myAdapter;
import static com.example.appnotes.MainActivity.notes;
import static com.example.appnotes.MainActivity.sharedPreferences;

public class NoteEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        EditText  text = findViewById(R.id.eTNoteEditor);

        Intent intent = getIntent();
        final int noteId = intent.getIntExtra("noteId" , -1);
        Log.i("!!!!!!!!11111111111111111111111!!!!!!!!", String.valueOf(noteId));
        if (noteId != -1){
            text.setText(notes.get(noteId));
        }


        //to update listview
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                notes.set(noteId , String.valueOf(charSequence));
                MainActivity.sharedPreferences  = getApplicationContext().getSharedPreferences("com.example.appnotes;" ,MODE_PRIVATE);
                HashSet<String> hashSet = new HashSet<>(notes);
                sharedPreferences.edit().putStringSet("notes" , hashSet).apply();
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });








    }
}