package com.kurama.nikhil.cpusimulator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class EditPreset extends AppCompatActivity {

    SharedPreferences settings;
    SharedPreferences.Editor settingsEditor;
    ProcessRecordDataSupply dataSupply;
    int id;
    ListView lw;
    ArrayList<Process> processes = new ArrayList<Process>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_preset);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });

        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        settingsEditor = settings.edit();
        dataSupply = new ProcessRecordDataSupply(this);

        Intent intent = getIntent();
        id = intent.getIntExtra("ID", 0);

        ProcessRecordDataSupply dataSupply = new ProcessRecordDataSupply(this);
        processes = dataSupply.getPreset(id);

        generateItems();


        Button saveBtn = (Button) findViewById(R.id.edit_preset_btn_save);
        Button selectBtn = (Button) findViewById(R.id.edit_preset_btn_select);

        EditText name;
        EditText bTime;
        EditText aTime;

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select();
            }
        });
    }

    private void generateItems() {
        ArrayAdapter<Process> aa = new ProcessAdapter(this,R.layout.edit_process_item,processes);
        lw = (ListView) findViewById(R.id.preset_edit_list);
        lw.setAdapter(aa);

    }

    private void add() {
        Process p = new Process("P", 0, 0);
        processes.add(p);
        generateItems();
    }

    private void select() {
        save();
        settingsEditor.putInt("selectedPreset", id);
        settingsEditor.commit();
    }


    private void save() {
        EditText name;
        EditText bTime;
        EditText aTime;
        ArrayList<Process> p1 = new ArrayList<Process>();
        Log.v("Save", "Save Called");

        for (int i = 0; i < lw.getChildCount(); i++) {
            RelativeLayout rl = (RelativeLayout) lw.getChildAt(i);
            name =(EditText) rl.findViewById(R.id.edit_process_item_name);
            bTime = (EditText) rl.findViewById(R.id.edit_process_item_bTime);
            aTime = (EditText) rl.findViewById(R.id.edit_process_item_aTime);

            Process temp = new Process(name.getText().toString().trim(),
                    Integer.parseInt(bTime.getText().toString().trim()),
                    Integer.parseInt(aTime.getText().toString().trim()));

            p1.add(temp);
        }
        Log.v("Test",p1.get(2).getProcessName());
        dataSupply.setToPresetList(p1, id);
    }

}

