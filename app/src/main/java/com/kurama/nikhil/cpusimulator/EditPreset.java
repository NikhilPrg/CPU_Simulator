package com.kurama.nikhil.cpusimulator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

public class EditPreset extends AppCompatActivity {

    SharedPreferences settings;
    SharedPreferences.Editor settingsEditor;
    ProcessRecordDataSupply dataSupply;
    int id;
    ScrollView lw;
    ArrayList<Process> processes = new ArrayList<Process>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_preset);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        settingsEditor = settings.edit();
        dataSupply = new ProcessRecordDataSupply(this);

        Intent intent = getIntent();
        id = intent.getIntExtra("ID", -1);

        if(id == -1){
            id = dataSupply.getListOfIds().length + 1;
        }

        ProcessRecordDataSupply dataSupply = new ProcessRecordDataSupply(this);
        processes = dataSupply.getPreset(id);

        generateItems();


        Button saveBtn = (Button) findViewById(R.id.edit_preset_btn_save);
        Button selectBtn = (Button) findViewById(R.id.edit_preset_btn_select);
        Button addBtn = (Button) findViewById(R.id.edit_preset_btn_add);

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
                if(process_items.size() <= 3){
                    Snackbar.make(view, "Add at least 4 processes.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                select();
                finish();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
    }

    private void add() {
        edit_process_item epiTemp = new edit_process_item(this, new Process("P", 0, 0));
        process_items.add(epiTemp);
        ll.addView(epiTemp);
    }

    LinearLayout ll;
    ArrayList<edit_process_item> process_items = new ArrayList<edit_process_item>();
    private void generateItems() {
        lw = (ScrollView) findViewById(R.id.preset_edit_list);
        ll = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ll.setOrientation(LinearLayout.VERTICAL);

        RelativeLayout temp;
        edit_process_item epiTemp;
        for (int i = 0; i < processes.size(); i++) {
            epiTemp = new edit_process_item(this, processes.get(i));
            temp = epiTemp;
            process_items.add(epiTemp);
            ll.addView(temp);
        }
        lw.addView(ll);
    }

    private void select() {
        save();
        settingsEditor.putInt("selectedPreset", id);
        settingsEditor.commit();
        Log.v("selected", Integer.toString(settings.getInt("selectedPreset",-1)));
    }


    private void save() {
        EditText name;
        EditText bTime;
        EditText aTime;
        ArrayList<Process> p1 = new ArrayList<Process>();
        Log.v("Save", "Save Called");


        for (int i = 0; i < process_items.size(); i++) {
            RelativeLayout rl = process_items.get(i);
            name = (EditText) rl.findViewById(R.id.edit_process_item_name);
            bTime = (EditText) rl.findViewById(R.id.edit_process_item_bTime);
            aTime = (EditText) rl.findViewById(R.id.edit_process_item_aTime);

            p1.add(new Process(name.getText().toString(), Integer.parseInt(bTime.getText().toString()), Integer.parseInt(aTime.getText().toString())));
        }
        dataSupply.setToPresetList(p1, id);
    }

}

